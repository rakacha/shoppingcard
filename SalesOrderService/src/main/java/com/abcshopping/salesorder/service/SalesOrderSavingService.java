package com.abcshopping.salesorder.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcshopping.salesorder.domain.SalesOrder;
import com.abcshopping.salesorder.domain.SalesOrderItem;
import com.abcshopping.salesorder.repository.SalesOrderItemRepository;
import com.abcshopping.salesorder.repository.SalesOrderRepository;

@Service
@Transactional
public class SalesOrderSavingService {
	@Autowired
	private SalesOrderRepository salesOrderRepository;
	
	@Autowired
	private SalesOrderItemRepository salesOrderItemRepository;
	
	@Transactional
	public SalesOrder saveSalesOrderItems(SalesOrder salesOrder, List<SalesOrderItem> responseSalesOrderitems) {
		SalesOrder savedSalesOrder = null;
		if(responseSalesOrderitems.size() > 0) {
			savedSalesOrder = salesOrderRepository.save(salesOrder);
			
			for(SalesOrderItem item : responseSalesOrderitems) {
				item.setOrderId(savedSalesOrder.getId());
				salesOrderItemRepository.save(item);
			}
		}
		return savedSalesOrder;
	}
	

}
