package com.study.ali.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.study.ali.config.AliPayConfig;
import com.study.ali.constant.AliPayConst;
import com.study.ali.domain.Order;
import com.study.ali.mapper.OrderMapper;
import com.study.ali.service.AliPayService;
import com.study.ali.vo.PayRequestVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝接入业务类
 *
 * @author yangz
 * @date 2024/8/30 - 10:14
 */
@Slf4j
@Service
public class AliPayServiceImpl implements AliPayService {

    @Resource
    private AliPayConfig aliPayConfig;
    @Resource
    private OrderMapper orderMapper;

    @Override
    public String pay(PayRequestVO payRequestVO) {
        String outTradeNo = payRequestVO.getOutTradeNo();
        Order order = orderMapper.findById(outTradeNo);
        if (order == null) {
            log.error("订单不存在,orderId:{}", outTradeNo);
           return null;
        }
        if (order.getOrderStatus() != 0) {
            log.error("订单状态异常,orderId:{}", outTradeNo);
            return null;
        }

        // 获取建客户端
        AlipayClient alipayClient = aliPayConfig.getAlipayClient();
        // 创建API请求
        AlipayTradePagePayRequest alipayRequest = getTradePagePayRequest(order);

        try {
            AlipayTradePagePayResponse tradePagePayResponse = alipayClient.pageExecute(alipayRequest);

            if (tradePagePayResponse.isSuccess()) {
                log.info("订单支付成功,orderId:{}", outTradeNo);
                //返回用于跳转支付宝页面的信息
                return tradePagePayResponse.getPageRedirectionData();
            } else {
                log.error("订单支付失败,orderId:{} errorMsg:{}", outTradeNo, tradePagePayResponse.getSubMsg());
                return null;
            }
        } catch (AlipayApiException e) {
            log.info("订单:{},支付异常 errorMsg:{}", outTradeNo, e.getMessage());
            return null;
        }
    }

    @Override
    public boolean payReturn(HttpServletRequest request) {
        return this.verify(request);
    }

    @Override
    public boolean payNotify(HttpServletRequest request) {
        boolean verify = this.verify(request);
        if (verify) {
            // 获取通知中的参数
            String tradeStatus = request.getParameter("trade_status");
            String outTradeNo = request.getParameter("out_trade_no");

            Order order = orderMapper.findById(outTradeNo);

            if (order == null) {
                log.error("订单不存在修改状态失败,orderId:{}", outTradeNo);
                return false;
            }

            if (tradeStatus.equals("TRADE_SUCCESS")) {
                //支付成功
                order.setOrderStatus(1);
            } else {
                //支付失败
                order.setOrderStatus(2);
            }
            //修改订单状态
            orderMapper.updateOrderStatus(order.getOrderStatus());
        }
        return false;
    }

    @Override
    public boolean aliRefund(PayRequestVO payRequestVO) {
        String outTradeNo = payRequestVO.getOutTradeNo();
        Order order = orderMapper.findById(outTradeNo);
        if (order == null) {
            log.error("订单不存在退款失败,orderId:{}", outTradeNo);
            return false;
        }
        if (order.getOrderStatus() != 1) {
            log.error("订单状态异常退款失败,orderId:{}", outTradeNo);
            return false;
        }

        AlipayClient alipayClient = aliPayConfig.getAlipayClient();
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();

        // 设置商户订单号
        model.setOutTradeNo(outTradeNo);
        // 设置支付宝交易号 支付宝交易号和商户订单号不能同时为空,两者同时存在时,优先取值trade_no
        //model.setTradeNo("2014112611001004680073956707");
        //设置退款金额
        model.setRefundAmount(order.getAmount().toString());
        // 设置退款原因说明
        model.setRefundReason("正常退款");
        request.setBizModel(model);

        try {
            AlipayTradeRefundResponse refundResponse = alipayClient.execute(request);
            if (refundResponse.isSuccess()) {
                log.info("退款成功,orderId:{}", outTradeNo);
                return true;
            } else {
                log.error("退款失败,orderId:{} errorMsg:{}", outTradeNo, refundResponse.getSubMsg());
                return false;
            }
        } catch (AlipayApiException e) {
            log.info("订单:{},退款异常 errorMsg:{}", outTradeNo, e.getMessage());
            return false;
        }
    }

    /**
     * 根据订单构建支付请求
     */
    private @NotNull AlipayTradePagePayRequest getTradePagePayRequest(Order order) {
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //同步通知地址(APP支付和当面付的接口都是不支持的)
        alipayRequest.setReturnUrl(AliPayConst.RETURN_URL);
        //异步通知地址
        alipayRequest.setNotifyUrl(AliPayConst.NOTIFY_URL);

        // 填充业务参数
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(order.getOrderId());
        model.setTotalAmount(order.getAmount().toString());
        model.setSubject(order.getContent()); //订单标题
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setBody("商品描述");
        alipayRequest.setBizModel(model);
        return alipayRequest;
    }

    /**
     * 校验支付宝回调请求
     */
    private boolean verify(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息，将异步通知中收到的待验证所有参数都存放到map中
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            return AlipaySignature.rsaCheckV1(params, aliPayConfig.getPublicKey(), aliPayConfig.getCharset(), aliPayConfig.getSignType());
        } catch (AlipayApiException e) {
            log.error("支付宝校验签名异常！");
        }
        return false;
    }

}
