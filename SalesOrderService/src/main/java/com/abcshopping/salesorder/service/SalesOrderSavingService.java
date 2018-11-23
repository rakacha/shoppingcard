package com.abcshopping.salesorder.service;

import java.util.ArrayList;
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
	public SalesOrder saveSalesOrderItems(SalesOrder salesOrder) throws Exception{
		List<SalesOrderItem> salesOrderitems = salesOrder.getSalesOrderitems();
		if(salesOrderitems.size() > 0) {
			SalesOrder savedSalesOrder = salesOrderRepository.save(salesOrder);
			List<SalesOrderItem> savedSalesOrderItems = new ArrayList<SalesOrderItem>();
			savedSalesOrder.setSalesOrderitems(savedSalesOrderItems);
			for(SalesOrderItem item : salesOrderitems) {
				item.setOrderId(savedSalesOrder.getId());
				SalesOrderItem savedItem = salesOrderItemRepository.save(item);
				if(savedItem != null) {
					savedSalesOrderItems.add(savedItem);
				}
			}
			return savedSalesOrder;
		}
		return null;
	}
	

}
