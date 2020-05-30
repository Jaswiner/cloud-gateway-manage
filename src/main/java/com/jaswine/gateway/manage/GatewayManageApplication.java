package com.jaswine.gateway.manage;


//import com.lanswon.gateway.manage.mq.SourceChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * 网关管理APP
 *
 *
 * @author jaswine
 */
@SpringBootApplication
//@EnableBinding({SourceChannel.class})
public class GatewayManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayManageApplication.class,args);
	}
}
