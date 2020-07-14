package com.jaswine.gateway.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jaswine.gateway.manage.bean.cd.RouterCd;
import com.jaswine.gateway.manage.bean.pojo.Route;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 路由Mapper
 *
 * @author jaswine
 */
@Repository
@Mapper
public interface RouterMapper extends BaseMapper<Route> {

	/** 检测表是否存在 */
	@Select("SHOW TABLES LIKE #{tableName} ")
	String checkTableExist(String tableName);

	/** 获得所有路由信息 */
	IPage<Route> getAllRouter(@Param("page") Page page,
	                          @Param("cd") RouterCd cd);

	Set<Route> getAllRouteWithOutPage();

	void updateRouteStatusById(@Param("id") Long id,
	                           @Param("status") Integer status);
}
