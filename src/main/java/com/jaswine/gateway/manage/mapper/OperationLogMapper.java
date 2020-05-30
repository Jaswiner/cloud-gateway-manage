package com.jaswine.gateway.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanswon.gateway.manage.bean.cd.LogCD;
import com.lanswon.gateway.manage.bean.pojo.OperationLogPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 日志Mapper
 *
 * @author jaswine
 */
@Repository
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLogPojo> {

	IPage<OperationLogPojo> getOperationLogPage(@Param("page") Page<Object> page,
	                                            @Param("cd") LogCD cd);
}
