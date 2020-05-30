package com.jaswine.gateway.manage.bean.cd;

import com.lanswon.commons.web.custom.cd.BaseCd;
import lombok.Data;

import java.util.Date;

/**
 * @author : Jaswine
 * @date : 2020-04-17 11:05
 **/
@Data
public class LogCD extends BaseCd {

	private String userName;

	private Date startTime;

	private Date endTime;

}
