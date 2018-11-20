package com.abcshopping.salesorder.repository;

import org.springframework.data.repository.CrudRepository;

import com.abcshopping.salesorder.domain.SalesOrderItem;

public interface SalesOrderItemRepository extends CrudRepository<SalesOrderItem, String> {

}
