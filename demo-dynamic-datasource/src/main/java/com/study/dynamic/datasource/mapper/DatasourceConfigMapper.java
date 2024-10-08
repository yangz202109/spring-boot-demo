package com.study.dynamic.datasource.mapper;

import com.study.dynamic.datasource.config.MyMapper;
import com.study.dynamic.datasource.model.DatasourceConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 数据源配置 Mapper
 * </p>
 *
 * @author  yangz
 * @date Created in 2019-09-04 16:20
 */
@Mapper
public interface DatasourceConfigMapper extends MyMapper<DatasourceConfig> {
}
