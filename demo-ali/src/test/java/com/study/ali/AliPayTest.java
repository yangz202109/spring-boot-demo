package com.study.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.study.ali.config.AliPayConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 支付宝测试类
 * <a href="https://opendocs.alipay.com/open/204/105051?pathHash=b91b9616"/>
 *
 * @author yangz
 * @date 2024/8/30 - 15:53
 */
@Slf4j
public class AliPayTest extends AliApplicationTests {
    @Resource
    private AliPayConfig aliPayConfig;

    /**
     * 扫描支付
     * alipay.trade.precreate(统一收单线下交易预创建)
     * 后台调用支付宝接口,生成二维码后,展示给用户,由用户扫描二维码完成订单支付。
     * 预下单请求生成的二维码有效时间为2小时
     * <p>
     * 该方法结果返回一个本次订单生成的收款二维码信息,再前端根据信息生成二维码给用户去扫描支付
     */
    @Test
    public void scanPay() throws AlipayApiException {
        // 初始化SDK
        AlipayClient alipayClient = aliPayConfig.getAlipayClient();

        // 构造请求参数以调用接口
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();

        // 设置商户订单号
        model.setOutTradeNo("20150320010101001");
        // 设置订单总金额
        model.setTotalAmount("88.88");
        // 设置订单标题
        model.setSubject("Iphone6 16G");
        request.setBizModel(model);

        AlipayTradePrecreateResponse response = alipayClient.execute(request);

        log.info(response.getBody());

        log.info("生成的二维码信息:{}", response.getQrCode());
        if (response.isSuccess()) {
            log.info("调用成功");
        } else {
            log.error("调用失败");
        }
    }


    /**
     * app支付
     * alipay.trade.app.pay(app支付接口2.0)
     * 该接口是 签名数据准备接口,用于生成可信签名字符串(orderStr).
     * 可信签名串中包含业务参数及商户身份信息,可防止数据被篡改,一般用于打开支付宝客户端。
     * <p>
     * 该方法结果返回一个本次订单生成的可信签名字符串给外部商户APP唤起快捷SDK创建订单并支付
     */
    @Test
    public void appPay() throws AlipayApiException {
        // 初始化SDK
        AlipayClient alipayClient = aliPayConfig.getAlipayClient();
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        // 设置商户订单号
        model.setOutTradeNo("70501111111S001111119");
        // 设置订单总金额
        model.setTotalAmount("9.00");
        // 设置订单标题
        model.setSubject("大乐透");
        request.setBizModel(model);

        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        String orderStr = response.getBody();

        log.info("签名字符串:{}", orderStr);
        if (response.isSuccess()) {
            log.info("调用成功");
        } else {
            log.error("调用失败");
        }
    }


    /**
     * 电脑Web支付
     * alipay.trade.page.pay(统一收单下单并支付页面接口)
     * 该接口是页面跳转接口,用于生成用户访问支付宝的跳转链接。
     * <p>
     * 该方法结果返回一个用于跳转到支付宝页面或已二维码,返回到用户浏览器渲染或重定向跳转到支付宝页面。
     */
    @Test
    public void pcWebPay() throws AlipayApiException {
        // 初始化SDK
        AlipayClient alipayClient = aliPayConfig.getAlipayClient();

        // 构造请求参数以调用接口
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();

        // 设置商户订单号
        model.setOutTradeNo("20150320010101001");
        // 设置订单总金额
        model.setTotalAmount("88.88");
        // 设置订单标题
        model.setSubject("Iphone6 16G");
        // 设置产品码 商家和支付宝签约的产品码。
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        /*
         * 不写默认是post
         * 使用POST方法执行,结果为html form表单,在浏览器渲染即可;
         * 使用GET方法会得到支付宝URL,需要打开或重定向到该URL
         */
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
        // 如果需要返回GET请求，请使用
        // AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");

        String pageRedirectionData = response.getBody();
        log.info(pageRedirectionData);
        if (response.isSuccess()) {
            log.info("调用成功");
        } else {
            log.error("调用失败");
        }
    }
}
