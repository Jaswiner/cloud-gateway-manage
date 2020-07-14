//package com.jaswine.gateway.manage.event;
//
//import com.jaswine.bean.message.MessageConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
///**
// * @author : Jaswine
// * @date : 2020-07-07 19:38
// **/
//@Component
//@Slf4j
//@RabbitListener(queuesToDeclare = @Queue(value = MessageConstant.MSG_TOPIC_ADD_GATEWAY,autoDelete = "false"))
//public class GatewayEvent {
//
//
//
//	@RabbitHandler
//	public void consumeMessage(String  message){
//		try {
//			//TODO：接收String
//			log.info("接收String消息： {} ",message);
//		}catch (Exception e){
//			log.error("监听消费消息 发生异常： ",e.fillInStackTrace());
//		}
//	}
//
//}
