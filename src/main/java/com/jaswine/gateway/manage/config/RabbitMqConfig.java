package com.jaswine.gateway.manage.config;

import com.jaswine.bean.message.MessageConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbit MQ消息队列
 *
 * @author : Jaswine
 * @date : 2020-06-07 15:33
 **/
@Configuration
@Slf4j
public class RabbitMqConfig {

	@Bean
	public Queue gatewayAddQueue(){
		log.info("配置'网关新增-队列'");
		return new Queue(MessageConstant.MSG_TOPIC_ADD_GATEWAY);
	}

	@Bean
	public Queue gatewayInitQueue(){
		log.info("配置'网关从Redis中初始化-队列'");
		return new Queue(MessageConstant.MSG_TOPIC_INIT_GATEWAY);
	}

	/**
	 * 配置rabbitmq模板
	 * @param connectionFactory 连接工厂
	 * @return RabbitMQ模板
	 */
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(new Jackson2JsonMessageConverter());
		return template;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(new Jackson2JsonMessageConverter());
		return factory;
	}

	/**
	 * RabbitMQ事务管理器
	 * @return 事务管理器
	 */
	@Bean(value = "rabbitTransactionManager")
	public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory){
		log.info("配置RabbitMQ事务管理器");
		return new RabbitTransactionManager(connectionFactory);
	}

}
