package com.study.dynamic.datasource;

import com.study.dynamic.datasource.datasource.DatasourceConfigCache;
import com.study.dynamic.datasource.datasource.DatasourceConfigContextHolder;
import com.study.dynamic.datasource.mapper.DatasourceConfigMapper;
import com.study.dynamic.datasource.model.DatasourceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * <p>
 * 启动器
 * </p>
 *
 * @author  yangz
 * @date Created in 2019-09-04 17:57
 */
@SpringBootApplication
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DynamicDatasourceApplication implements CommandLineRunner {
    private final DatasourceConfigMapper configMapper;

    public static void main(String[] args) {
        SpringApplication.run(DynamicDatasourceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // 设置默认的数据源
        DatasourceConfigContextHolder.setDefaultDatasource();
        // 查询所有数据库配置列表
        List<DatasourceConfig> datasourceConfigs = configMapper.selectAll();
        System.out.println("加载其余数据源配置列表: " + datasourceConfigs);
        // 将数据库配置加入缓存
        datasourceConfigs.forEach(config -> DatasourceConfigCache.INSTANCE.addConfig(config.getId(), config));
    }
}
