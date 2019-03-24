package com.abcshopping.salesorder.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerListenerConfig {
	private ConnectionFactory connectionFactory;
	private final String queueUrl;
    @Autowired
    public CustomerListenerConfig(@Value("${customer.queue}") String queueUrl, ConnectionFactory connectionFactory){
        this.queueUrl = queueUrl;
    	this.connectionFactory = connectionFactory;
    }


    public String getQueueUrl() {
		return queueUrl;
	}


	@Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(this.connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

    @Bean
    public Queue queue(){
        return new Queue(queueUrl, false);
    }
}
