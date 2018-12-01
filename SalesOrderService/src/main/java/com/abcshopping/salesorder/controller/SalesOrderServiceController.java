package com.abcshopping.salesorder.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.abcshopping.salesorder.domain.SalesOrderItem;
import com.abcshopping.salesorder.service.CustomerSOSService;
import com.abcshopping.salesorder.service.SalesOrderPricingService;
import com.abcshopping.salesorder.service.SalesOrderSavingService;

@RefreshScope
@RestController
public class SalesOrderServiceController {
	@Autowired
	private CustomerSOSService customerSOSService;

	@Autowired
	private SalesOrderPricingService salesOrderPricingService;
	
	@Autowired
	private  SalesOrderSavingService salesOrderSavingService;
	
	private final String serviceName;

	SalesOrderServiceController(@Value("${item.service.name}") String serviceName){
		this.serviceName = serviceName;
	}

	@PostMapping("/orders")
	public ResponseEntity<?> createOrder(@RequestBody SalesOrder salesOrder) {
		List<String> errorMessages = new ArrayList<String>();
		
		if(validateAndRebuildInputRequest(salesOrder, errorMessages)) {
			double totalOrderPrice = salesOrderPricingService.getSalesOrderTotalPrice(salesOrder, serviceName, errorMessages);
			salesOrder.setTotalPrice(totalOrderPrice);

		}
		
		if(salesOrder.getTotalPrice() > 0) {

			try {
				SalesOrder savedSalesOrder = salesOrderSavingService.saveSalesOrder(salesOrder);
				return buildAndReturnSavedSalesOrder(savedSalesOrder, errorMessages);
			}catch (Exception e) {
				errorMessages.add("Failed while saving the sales order and items!");
			}
		}
		
		return buildAndReturnErrorResponse(errorMessages);
	}

	private ResponseEntity<?> buildAndReturnSavedSalesOrder(SalesOrder savedSalesOrder, List<String> errorMessages) {
		HttpHeaders httpHeaders = new HttpHeaders();
		if(errorMessages != null && errorMessages.size() > 0) {
			httpHeaders.add("Item_save_failed", errorMessages.toString());
		}
		if(savedSalesOrder != null) {
			httpHeaders.setLocation(ServletUriComponentsBuilder
					.fromCurrentRequest().path("/" + savedSalesOrder.getId())
					.buildAndExpand().toUri());
		}
		return new ResponseEntity<>(savedSalesOrder, httpHeaders, HttpStatus.CREATED);
	}

	private ResponseEntity<?> buildAndReturnErrorResponse(List<String> errorMessages) {
		HttpHeaders httpHeaders = new HttpHeaders();
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
	private boolean validateAndRebuildInputRequest(SalesOrder salesOrder, List<String> errorMessages) {
		Optional<Customer_SOS> customerSOS = customerSOSService.fetchCustomerSOS(salesOrder);
		if(customerSOS == null || !customerSOS.isPresent()) {
			errorMessages.add("No Customer Found in Order Database!");
			return false;
		}
		
		if(salesOrder.getSalesOrderitems() == null || salesOrder.getSalesOrderitems().size() <= 0) {
			errorMessages.add("No items for creating order!");
			return false;
		}
		List<SalesOrderItem> salesOrderitems = salesOrder.getSalesOrderitems();
		
		for(SalesOrderItem item: salesOrderitems) {
			item.setOrder(salesOrder);
		}
		
		
		return true;
	}


}
