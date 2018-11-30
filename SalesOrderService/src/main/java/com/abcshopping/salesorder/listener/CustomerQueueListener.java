package com.abcshopping.salesorder.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abcshopping.salesorder.domain.Customer_SOS;
import com.abcshopping.salesorder.service.CustomerSOSService;

@Component
public class CustomerQueueListener {
	
	@Autowired
	private CustomerSOSService customerSOSService;
	
	@RabbitListener(queues = "${customer.queue}")
	public void receiveMessage(Customer_SOS customerSOS) {
		Customer_SOS savedCustomerSOS  = customerSOSService.saveCustomerSOSData(customerSOS);
		
		assert savedCustomerSOS != null;
	}


}
