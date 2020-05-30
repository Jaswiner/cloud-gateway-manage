package com.jaswine.gateway.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanswon.gateway.manage.bean.cd.RouterCd;
import com.lanswon.gateway.manage.bean.pojo.Router;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 路由Mapper
 *
 * @author jaswine
 */
@Repository
@Mapper
public interface RouterMapper extends BaseMapper<Router> {

	/** 检测表是否存在 */
	@Select("SHOW TABLES LIKE #{tableName} ")
	String checkTableExist(String tableName);

	/** 获得所有路由信息 */
	IPage<Router> getAllRouter(@Param("page") Page page,
	                           @Param("cd") RouterCd cd);
}
