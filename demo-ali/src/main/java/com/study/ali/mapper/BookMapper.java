package com.study.ali.mapper;

import com.study.ali.domain.Book;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 模拟数据库操作
 * @author yangz
 * @date 2024/8/29 - 17:25
 */
@Component
public interface BookMapper {

    void save(Book book);

    void save(List<Book> books);

    Book findById(Long id);

    void delById(Long id);
}
