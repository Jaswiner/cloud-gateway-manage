package com.jaswine.gateway.manage.bean.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新增网关信息消息DTO
 *
 * @author : Jaswine
 * @date : 2020-05-14 22:45
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GatewayAddMsgDTO {

	private String msg;
}
