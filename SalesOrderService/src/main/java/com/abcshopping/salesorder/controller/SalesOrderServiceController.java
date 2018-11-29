package com.abcshopping.salesorder.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abcshopping.salesorder.domain.Customer_SOS;
import com.abcshopping.salesorder.domain.SalesOrder;
import com.abcshopping.salesorder.repository.CustomerSOSRepository;
import com.abcshopping.salesorder.service.SalesOrderItemValidationService;
import com.abcshopping.salesorder.service.SalesOrderSavingService;

@RefreshScope
@RestController
public class SalesOrderServiceController {
	
	@Autowired
	private CustomerSOSRepository customerSOSRepository;
	
	
	@RabbitListener(queues = "#{'${customer.queue}'}")
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
		
		validateInputRequest(salesOrder, errorMessages);
		HttpHeaders httpHeaders = new HttpHeaders();
		
		if(errorMessages == null || errorMessages.size() <= 0) {

			try {
				SalesOrder savedSalesOrder = salesOrderSavingService.saveSalesOrderItems(salesOrder);
				
				if(savedSalesOrder != null) {
					httpHeaders.setLocation(ServletUriComponentsBuilder
							.fromCurrentRequest().path("/" + savedSalesOrder.getId())
							.buildAndExpand().toUri());
				}
				return new ResponseEntity<>(savedSalesOrder, httpHeaders, HttpStatus.CREATED);
			}catch (Exception e) {
				errorMessages.add("Failed while saving the sales order and items!");
			}
		}
			
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest().path("/error")
				.build().toUri());
		
		return new ResponseEntity<>(errorMessages.toString(), httpHeaders, HttpStatus.FAILED_DEPENDENCY);
		
		
	}

	/**
	 * @param salesOrder
	 * @param errorMessages
	 * @param responseSalesOrderitems
	 */
	private void validateInputRequest(SalesOrder salesOrder, List<String> errorMessages) {
		Optional<Customer_SOS> customerSOS = customerSOSRepository.findById(salesOrder.getCustomerId());
		if(customerSOS == null || !customerSOS.isPresent()) {
			errorMessages.add("No Customer Found in Order Database!");
			return;
		}
		
		if(salesOrder.getSalesOrderitems() == null || salesOrder.getSalesOrderitems().size() <= 0) {
			errorMessages.add("No items for creating order!");
			return;
		}
		
		salesOrderItemValidationService.getSalesOrderItems(salesOrder, serviceName, errorMessages);
	}

}
