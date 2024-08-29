package com.study.ali.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 图书实体类
 * <p>
 * ExcelProperty：核心注解，value属性可用来设置表头名称，converter属性可以用来设置类型转换器；
 * ColumnWidth：用于设置表格列的宽度；
 * DateTimeFormat：用于设置日期转换格式；
 * NumberFormat：用于设置数字转换格式。
 * @author yangz
 * @date 2024/8/29 - 16:30
 */
@Data
public class Book {

    @ExcelIgnore //写入时忽略
    private Long id;

    @ColumnWidth(15)  //宽度
    @ExcelProperty(index = 0,value = "图书名称")  // 列索引为1
    private String bookName;

    @ColumnWidth(15)
    @ExcelProperty(index = 1,value = "图书价格")  // 列索引为2
    private Double bookPrice;

    @ColumnWidth(30)
    @ExcelProperty(index = 2,value = "图书描述")
    private String bookDesc;

    @ColumnWidth(15)
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(index = 3,value = "图书生产时间")
    private LocalDateTime createTime;

    @ExcelIgnore //写入时忽略
    private LocalDateTime updateTime;

    public Book(Long id, String bookName, Double bookPrice, String bookDesc, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.bookDesc = bookDesc;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
