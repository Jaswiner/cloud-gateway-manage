//package com.jaswine.gateway.manage.rabbit;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//
///**
// * @author : Jaswine
// * @date : 2020-07-12 18:35
// **/
//@Component
//@Slf4j
//public class RabbitMqSender implements RabbitTemplate.ReturnCallback {
//
//
//	@PostConstruct
//	private void init() {
//		//启用事务模式,不能开确认回调
//		//rabbitTemplate.setConfirmCallback(this);
//		rabbitTemplate.setReturnCallback(this);
//		rabbitTemplate.setChannelTransacted(true);
//		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//	}
//
//	@Transactional(rollbackFor = Exception.class,transactionManager = "rabbitTransactionManager")
//	public void sendIngateQueue(TradePayModelRes msg) {
//		logger.info("进闸消息已发送 {}",msg.getOutTradeNo());
//		rabbitTemplate.convertAndSend(exchange,ingateQueue,msg);
//	}
//
//	@Override
//	public void returnedMessage(Message message, int i, String s, String s1, String s2) {
//
//	}
//
//
//}
