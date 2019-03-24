package com.abcshopping.customer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcshopping.customer.domain.Customer;
import com.abcshopping.customer.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
    CustomerRepository customerRepository;

	public List<Customer> getCustomers() {
		return (List<Customer>) customerRepository.findAll();
	}

	public Customer saveCustomer(Customer customer) throws Exception{
		return customerRepository.save(customer);
	}

}
