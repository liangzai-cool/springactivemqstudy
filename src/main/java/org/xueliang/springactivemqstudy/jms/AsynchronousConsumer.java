package org.xueliang.springactivemqstudy.jms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 异步接收消息
 * @author XueLiang
 * @date 2016年10月24日 下午6:00:06
 * @version 1.0
 */
@Component
@EnableJms
public class AsynchronousConsumer {

	private static final Logger LOGGER = LogManager.getLogger();
	
	@JmsListener(destination = "myDestination")
	public void receive(String message) {
		LOGGER.info("receive: {}", message);
	}
}
