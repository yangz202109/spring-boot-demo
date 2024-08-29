package com.study.multi.datasource.jpa;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Snowflake;
import com.study.multi.datasource.jpa.entity.primary.PrimaryMultiTable;
import com.study.multi.datasource.jpa.repository.primary.PrimaryMultiTableRepository;
import com.study.multi.datasource.jpa.repository.second.SecondMultiTableRepository;
import com.study.multi.datasource.jpa.entity.second.SecondMultiTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
@Slf4j
public class MultiDatasourceJpaApplicationTests {
    @Autowired
    private PrimaryMultiTableRepository primaryRepo;
    @Autowired
    private SecondMultiTableRepository secondRepo;
    @Autowired
    private Snowflake snowflake;

    @Test
    public void testInsert() {
        PrimaryMultiTable primary = new PrimaryMultiTable(snowflake.nextId(), "测试名称-1");
        primaryRepo.save(primary);

        SecondMultiTable second = new SecondMultiTable();
        BeanUtil.copyProperties(primary, second);
        secondRepo.save(second);
    }

    @Test
    public void testUpdate() {
        primaryRepo.findAll().forEach(primary -> {
            primary.setName("修改后的" + primary.getName());
            primaryRepo.save(primary);

            SecondMultiTable second = new SecondMultiTable();
            BeanUtil.copyProperties(primary, second);
            secondRepo.save(second);
        });
    }

    @Test
    public void testDelete() {
        primaryRepo.deleteAll();

        secondRepo.deleteAll();
    }

    @Test
    public void testSelect() {
        List<PrimaryMultiTable> primary = primaryRepo.findAll();
        log.info("【primary】= {}", primary);

        List<SecondMultiTable> second = secondRepo.findAll();
        log.info("【second】= {}", second);
    }

}

