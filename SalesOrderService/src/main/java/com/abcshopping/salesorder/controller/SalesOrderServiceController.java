package com.abcshopping.salesorder.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abcshopping.salesorder.domain.SalesOrder;
import com.abcshopping.salesorder.domain.SalesOrderItem;
import com.abcshopping.salesorder.repository.SalesOrderItemRepository;
import com.abcshopping.salesorder.repository.SalesOrderRepository;
import com.abcshopping.salesorder.service.SalesOrderService;

@RestController
public class SalesOrderServiceController {

	@Autowired
	private SalesOrderRepository salesOrderRepository;
	
	@Autowired
	private SalesOrderItemRepository salesOrderItemRepository;
	
	@Autowired
	private SalesOrderService salesOrderService;
	
	private String serviceName;

	SalesOrderServiceController(@Value("${item.service.name}") String serviceName){
		this.serviceName = serviceName;
	}

	@PostMapping("/orders")
	public ResponseEntity<?> createOrder(@RequestBody SalesOrder salesOrder) {
		String headerMessage = null;
		List<SalesOrderItem> salesOrderitems = salesOrder.getSalesOrderitems();
		List<SalesOrderItem> responseSalesOrderitems = new ArrayList<SalesOrderItem>();
		
		if(salesOrderitems == null) {
			headerMessage = "No items for creating order!";
		}else {
			headerMessage = salesOrderService.getSalesOrderItems(salesOrder, headerMessage, serviceName, salesOrderitems, responseSalesOrderitems);
		}
		SalesOrder savedSalesOrder = null;
		if(responseSalesOrderitems.size() > 0) {
			savedSalesOrder = salesOrderRepository.save(salesOrder);
			
			for(SalesOrderItem item : responseSalesOrderitems) {
				item.setOrderId(savedSalesOrder.getId());
				salesOrderItemRepository.save(item);
			}
		}
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(ServletUriComponentsBuilder
				.fromCurrentRequest().path("/" + savedSalesOrder.getId())
				.buildAndExpand().toUri());

		if(headerMessage == null || headerMessage.trim().length() <= 0) {
			headerMessage = "Order created successfully. See you soon!!";
		}
		httpHeaders.set("Order creation message:",headerMessage);

		return new ResponseEntity<>(savedSalesOrder, httpHeaders, HttpStatus.CREATED);
	}


}
