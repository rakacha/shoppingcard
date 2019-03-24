package com.abcshopping.salesorder.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcshopping.salesorder.domain.SalesOrder;
import com.abcshopping.salesorder.domain.SalesOrderItem;
import com.abcshopping.salesorder.repository.SalesOrderRepository;

@Service
@Transactional
public class SalesOrderSavingService {
	@Autowired
	private SalesOrderRepository salesOrderRepository;
	
	public SalesOrder saveSalesOrder(SalesOrder salesOrder) throws Exception{
		List<SalesOrderItem> salesOrderitems = salesOrder.getSalesOrderitems();
		if(salesOrderitems.size() > 0) {
			SalesOrder savedSalesOrder = salesOrderRepository.save(salesOrder);
			return savedSalesOrder;
		}
		return null;
	}
	

}
