package com.study.ali.controller;

import com.alibaba.excel.EasyExcel;
import com.study.ali.config.BookReadListener;
import com.study.ali.domain.Book;
import com.study.ali.mapper.BookMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yangz
 * @date 2024/8/29 - 17:04
 */
@RequestMapping("/book")
@RestController
public class BookController {

    @Resource
    private BookMapper bookMapper;

    /**
     * web下载excel
     */
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        /*防止中文名称导致乱码*/
        //String fileName = URLEncoder.encode("图书", "UTF-8");

        /*设置响应头*/
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment; filename=books.xlsx");

        LocalDateTime now = LocalDateTime.now();

        List<Book> books = new ArrayList<>(3);
        books.add(new Book(1000L, "三国演义", 45.0, "四大名著之一", now, now));
        books.add(new Book(2000L, "百年孤独", 35.5, "获得诺贝尔文学奖", now, now));
        books.add(new Book(3000L, "人性的弱点", 50.0, "人生必读之一", now, now));

        EasyExcel.write(response.getOutputStream(), Book.class)
                .sheet("book").doWrite(books);

        // 也可以根据用户传入字段来导出 bookName
       /* Set<String> includeColumnFiledNames = new HashSet<>();
        includeColumnFiledNames.add("bookName");
        EasyExcel.write(response.getOutputStream(), Book.class).includeColumnFieldNames(includeColumnFiledNames)
                .sheet("book").doWrite(books);*/
    }

    /**
     * excel上传
     */
    @PostMapping("upload")
    public String upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), Book.class, new BookReadListener(bookMapper)).sheet().doRead();
        return "success";
    }
}
