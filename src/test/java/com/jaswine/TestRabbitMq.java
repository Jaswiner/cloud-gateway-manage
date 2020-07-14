package com.jaswine;

import com.jaswine.bean.message.MessageConstant;
import com.jaswine.gateway.manage.GatewayManageApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * RabbitMQ单元测试
 *
 *
 * @author : Jaswine
 * @date : 2020-07-07 19:54
 **/
@SpringBootTest(classes = GatewayManageApplication.class)
@RunWith(SpringRunner.class)
public class TestRabbitMq {

	@Resource
	private RabbitTemplate rabbitTemplate;

	@Test
	public void testSendMsg(){
		rabbitTemplate.convertAndSend(MessageConstant.MSG_TOPIC_ADD_GATEWAY,"test");
	}
}
