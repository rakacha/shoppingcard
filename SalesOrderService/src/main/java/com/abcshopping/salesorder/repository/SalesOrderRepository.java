package com.abcshopping.salesorder.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.abcshopping.salesorder.domain.SalesOrder;

public interface SalesOrderRepository extends CrudRepository<SalesOrder, String> {

	

}
