package com.abcshopping.salesorder.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcshopping.salesorder.domain.SalesOrder;
import com.abcshopping.salesorder.domain.SalesOrderItem;

@Service
public class SalesOrderPricingService {

	public SalesOrderPricingService() {
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	private ItemServiceInvokerService itemServiceInvokerService;

	public double getSalesOrderTotalPrice(SalesOrder salesOrder, String serviceName, List<String> errorMessages) {
		
		List<SalesOrderItem> salesOrderitems = salesOrder.getSalesOrderitems();
		List<SalesOrderItem> pricedSalesOrderitems = new ArrayList<SalesOrderItem>();
		double totalPrice = 0.0d;
		for(SalesOrderItem item: salesOrderitems) {
			
			SalesOrderItem responseItem = itemServiceInvokerService.callItemService(serviceName, item, errorMessages);

			if(responseItem != null) {
				item.setItemPrice(responseItem.getItemPrice());
				totalPrice += responseItem.getItemPrice() * item.getItemQuantity();
				pricedSalesOrderitems.add(item);
			}
		}
		salesOrder.setSalesOrderitems(pricedSalesOrderitems);
		return totalPrice;
	}

	
}
