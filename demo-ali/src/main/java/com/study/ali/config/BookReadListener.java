package com.study.ali.config;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.study.ali.domain.Book;
import com.study.ali.mapper.BookMapper;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

// 有个很重要的点 DemoDataListener 不能被spring管理,要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
public class BookReadListener implements ReadListener<Book> {

    /**
     * 每隔100条,然后清理list,方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    /**
     * 缓存的数据
     */
    private List<Book> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);


    private BookMapper bookMapper;

    public BookReadListener(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    /**
     * 读取数据回调方法
     * 这个每一条数据解析都会来调用
     *
     * @param data  one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(Book data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSONUtil.toJsonStr(data));

        cachedDataList.add(data);
        // 达到BATCH_COUNT了,需要去存储一次数据库,防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());

        if (cachedDataList.isEmpty()){
            log.info("已经没有可存储的数据!");
        }

        bookMapper.save(cachedDataList);
        log.info("存储数据库成功！");
    }
}