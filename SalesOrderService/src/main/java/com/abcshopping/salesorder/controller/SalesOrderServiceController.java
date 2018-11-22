package com.abcshopping.salesorder.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abcshopping.salesorder.domain.Customer_SOS;
import com.abcshopping.salesorder.domain.SalesOrder;
import com.abcshopping.salesorder.domain.SalesOrderItem;
import com.abcshopping.salesorder.repository.CustomerSOSRepository;
import com.abcshopping.salesorder.service.SalesOrderSavingService;
import com.abcshopping.salesorder.service.SalesOrderItemValidationService;

@RestController
public class SalesOrderServiceController {
	
	@Autowired
	private CustomerSOSRepository customerSOSRepository;
	
	
	@RabbitListener(queues = "spring-boot-customer-236220")
	public void receiveMessage(Customer_SOS customerSOS) {
		Customer_SOS savedCustomerSOS  = customerSOSRepository.save(customerSOS);
		
		assert savedCustomerSOS != null;
	}



	@Autowired
	private SalesOrderItemValidationService salesOrderItemValidationService;
	
	@Autowired
	private  SalesOrderSavingService salesOrderSavingService;
	
	private final String serviceName;

	SalesOrderServiceController(@Value("${item.service.name}") String serviceName){
		this.serviceName = serviceName;
	}

	@PostMapping("/orders")
	public ResponseEntity<?> createOrder(@RequestBody SalesOrder salesOrder) {
		List<String> errorMessages = new ArrayList<String>();
		List<SalesOrderItem> responseSalesOrderitems = new ArrayList<SalesOrderItem>();
		
		validateInputRequest(salesOrder, errorMessages, responseSalesOrderitems);
		HttpHeaders httpHeaders = new HttpHeaders();
		
		if(errorMessages == null || errorMessages.size() <= 0) {
			SalesOrder savedSalesOrder = salesOrderSavingService.saveSalesOrderItems(salesOrder, responseSalesOrderitems);
		
			if(savedSalesOrder != null) {
				httpHeaders.setLocation(ServletUriComponentsBuilder
						.fromCurrentRequest().path("/" + savedSalesOrder.getId())
						.buildAndExpand().toUri());
			}
			return new ResponseEntity<>(savedSalesOrder, httpHeaders, HttpStatus.CREATED);
		}else {
			
			httpHeaders.setLocation(ServletUriComponentsBuilder
					.fromCurrentRequest().path("/error")
					.build().toUri());
			
			return new ResponseEntity<>(errorMessages.toString(), httpHeaders, HttpStatus.FAILED_DEPENDENCY);
		}
		
	}

	private void validateInputRequest(SalesOrder salesOrder, List<String> errorMessages,
			List<SalesOrderItem> responseSalesOrderitems) {
		Optional<Customer_SOS> customerSOS = customerSOSRepository.findById(salesOrder.getCustomerId());
		if(customerSOS == null || !customerSOS.isPresent()) {
			errorMessages.add("No Customer Found in Order Database!");
			return;
		}
		
		List<SalesOrderItem> salesOrderitems = salesOrder.getSalesOrderitems();
		
		if(salesOrderitems == null) {
			errorMessages.add("No items for creating order!");
			return;
		}
		
		String errorMessage = salesOrderItemValidationService.getSalesOrderItems(salesOrder, serviceName, salesOrderitems, responseSalesOrderitems);
		if(errorMessage != null && errorMessage.trim().length() > 0) {
			errorMessages.add(errorMessage);
		}
		
	}

}
