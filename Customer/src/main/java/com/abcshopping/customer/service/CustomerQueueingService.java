package com.abcshopping.customer.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcshopping.customer.domain.Customer;

@Service
public class CustomerQueueingService {
	
	
	@Autowired
	private RabbitTemplate template;

	public void sendMessageToQueue(Customer savedCustomer) {
		template.convertAndSend(savedCustomer);
	}


}
