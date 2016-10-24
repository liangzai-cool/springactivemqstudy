package org.xueliang.springactivemqstudy.jms;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.JMSException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * 消息发送方
 * @author XueLiang
 * @date 2016年10月24日 下午6:02:24
 * @version 1.0
 */
@Component
public class Producer {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	private ScheduledExecutorService scheduledExecutorService;

	@PostConstruct		// init method
	public void init() {
		LOGGER.info("init!");
		scheduledExecutorService = Executors.newScheduledThreadPool(2);
		scheduledExecutorService.scheduleWithFixedDelay(new Thread(new Runnable() {
			
			@Override
			public void run() {
				sendMessage("hello! now is " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
		}), 3000, 2000, TimeUnit.MILLISECONDS);
	}
	
	@PreDestroy			// destroy method
	public void destroy() {
		LOGGER.info("destroy!");
	}
	
	public void sendMessage(String text) {
		try {
			jmsTemplate.convertAndSend(text);
		} catch (JmsException e) {
			if (e instanceof UncategorizedJmsException) {
				Throwable throwable = e.getCause();
				if (throwable instanceof JMSException) {
					Throwable throwable2 = throwable.getCause();
					if (throwable2 instanceof ConnectException) {
						LOGGER.warn("connect error", throwable2);
						return;
					}
				}
			}
			LOGGER.info("send message error", e);
		}
	}
}
