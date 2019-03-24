package com.abcshopping.customer.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.abcshopping.customer.domain.Customer;

@Transactional
public interface CustomerRepository extends CrudRepository<Customer, String>{

}
