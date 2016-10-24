package org.xueliang.springactivemqstudy.jms;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * 同步接收消息
 * @author XueLiang
 * @date 2016年10月24日 下午6:00:26
 * @version 1.0
 */
@Component
public class SynchronousConsumer {

	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@Value("${jms.defaults.destination.name}") 
	private String defaultDestionationName;
	
	@PostConstruct
	public void init() {
		LOGGER.info("init!");
		jmsTemplate.setReceiveTimeout(3000);
		(new Thread(new Runnable() {			
			@Override
			public void run() {
				while (true) {
					try {
						Message message = jmsTemplate.receive();
						if (message instanceof TextMessage) {
							try {
								String text = ((TextMessage) message).getText();
								LOGGER.info("receive: {}", text);
							} catch (JMSException e) {
								LOGGER.error("get message error", e);
							}
						}
					} catch (JmsException e1) {
						LOGGER.error("receive message error", e1);
					}
				}
			}
		})).start();
	}
}
