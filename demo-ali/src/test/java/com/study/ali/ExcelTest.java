package com.study.ali;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.study.ali.config.BookReadListener;
import com.study.ali.domain.Book;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * easyExcel测试类
 * @author yangz
 * @date 2024/8/30 - 15:52
 */
public class ExcelTest extends AliApplicationTests{

    /**
     * 最简单的写
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link Book}
     * <p>
     * 2. 直接写即可
     */
    @Test
    public void simpleWrite() {
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入

        String fileName = FileUtil.getUserHomePath() + "/simpleWrite" + System.currentTimeMillis() + ".xlsx";

        List<Book> books = new ArrayList<>(1);

        // 写法1 JDK8+
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, Book.class)
                .sheet("模板")
                .doWrite(() -> {
                    // 分页查询数据
                    return books;
                });

        // 写法2
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, BookReadListener.class).sheet("模板").doWrite(books);

        // 写法3
        // 这里 需要指定写用哪个class去写
        try (ExcelWriter excelWriter = EasyExcel.write(fileName, Book.class).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            excelWriter.write(books, writeSheet);
        }
    }
}
