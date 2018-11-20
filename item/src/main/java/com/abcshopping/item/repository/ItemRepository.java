package com.abcshopping.item.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.abcshopping.item.domain.Item;

@Transactional
public interface ItemRepository extends CrudRepository<Item, String>{
	List<Item> findItemByName(String name);
}
