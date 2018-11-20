package com.abcshopping.salesorder;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.abcshopping.salesorder.domain.Customer_SOS;
import com.abcshopping.salesorder.repository.CustomerSOSRepository;

@SpringBootApplication
public class SalesOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesOrderServiceApplication.class, args);
	}
	
	
	@Autowired
	private CustomerSOSRepository customerSOSRepository;
	
	@RabbitListener(queues = "spring-boot-customer")
	public void receiveMessage(Customer_SOS customerSOS) {
		Customer_SOS savedCustomerSOS  = customerSOSRepository.save(customerSOS);
		
		assert savedCustomerSOS != null;
	}

}
