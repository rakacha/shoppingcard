package com.abcshopping.salesorder.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcshopping.salesorder.domain.Customer_SOS;
import com.abcshopping.salesorder.domain.SalesOrder;
import com.abcshopping.salesorder.repository.CustomerSOSRepository;

@Service
public class CustomerSOSService {
	
	@Autowired
	private CustomerSOSRepository customerSOSRepository;

	public Customer_SOS saveCustomerSOSData(Customer_SOS customerSOS) {
		return customerSOSRepository.save(customerSOS);
	}
	
	public Optional<Customer_SOS> fetchCustomerSOS(SalesOrder salesOrder) {
		return customerSOSRepository.findById(salesOrder.getCustomerId());
	}

}
