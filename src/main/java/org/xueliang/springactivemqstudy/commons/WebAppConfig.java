package org.xueliang.springactivemqstudy.commons;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@ComponentScan(basePackages = "org.xueliang.springactivemqstudy")
@PropertySource({"classpath:springactivemqstudy.properties"})
public class WebAppConfig {
	
	/**
	 * 真正可以产生Connection的ConnectionFactory，由对应的 JMS服务厂商提供
	 * @return
	 */
	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory(@Value("${jms.broker_url}") String brokerURL) {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerURL);
		return activeMQConnectionFactory;
	}
	
	/**
	 * Spring用于管理真正的ConnectionFactory的ConnectionFactory
	 * SingleConnectionFactory会对每个请求对返回同一个连接
	 * @param pooledConnectionFactory
	 * @return
	 */
	@Bean
	public SingleConnectionFactory singleConnectionFactory(@Autowired ActiveMQConnectionFactory activeMQConnectionFactory) {
		// 目标ConnectionFactory对应真实的可以产生JMS Connection的ConnectionFactory
		SingleConnectionFactory singleConnectionFactory = new SingleConnectionFactory(activeMQConnectionFactory);
		singleConnectionFactory.setReconnectOnException(true);
		return singleConnectionFactory;
	}
	
	/**
	 * 默认情况下，基础结构会查找名为jmsListenerContainerFactory的bean，
	 * 作为工厂用于创建消息侦听器容器的源
	 * @param singleConnectionFactory
	 * @return
	 */
	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(@Autowired SingleConnectionFactory singleConnectionFactory){
		DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
		defaultJmsListenerContainerFactory.setConnectionFactory(singleConnectionFactory);
		return defaultJmsListenerContainerFactory;
	}
	
	/**
	 * Spring提供的JMS工具类，它可以进行消息发送、接收等
	 * @param singleConnectionFactory
	 * @return
	 */
	@Bean
	public JmsTemplate jmsTemplate(@Autowired SingleConnectionFactory singleConnectionFactory, @Value("${jms.defaults.destination.name}") String defaultDestinationName) {
		JmsTemplate jmsTemplate = new JmsTemplate(singleConnectionFactory);
		jmsTemplate.setDefaultDestinationName(defaultDestinationName);
//		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}
}
