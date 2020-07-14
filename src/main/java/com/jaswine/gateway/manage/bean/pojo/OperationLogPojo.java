package com.jaswine.gateway.manage.bean.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jaswine.tools.log.BaseLogPojo;
import lombok.Data;

/**
 * 日志对象
 *
 * @author : Jaswine
 * @date : 2020-04-16 23:30
 **/
@Data
@TableName(value = "operation_log")
public class OperationLogPojo extends BaseLogPojo {

}