package com.abcshopping.customer.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerConfig {
	private final ConnectionFactory connectionFactory;
	private final String queueUrl;
    @Autowired
    public CustomerConfig(@Value("${customer.queue}") String queueUrl, ConnectionFactory connectionFactory){
        this.queueUrl = queueUrl;
    	this.connectionFactory = connectionFactory;
    }

    @Bean
    public RabbitTemplate template() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(this.connectionFactory);
        rabbitTemplate.setRoutingKey(queueUrl);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
