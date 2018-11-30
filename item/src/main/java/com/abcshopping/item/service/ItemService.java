package com.abcshopping.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcshopping.item.domain.Item;
import com.abcshopping.item.repository.ItemRepository;

@Service
public class ItemService {
	@Autowired
    ItemRepository itemRepository;

	public List<Item> getItems() {
		return (List<Item>) itemRepository.findAll();
	}
	
	public List<Item> getItemByName(String itemName){
		return itemRepository.findItemByName(itemName);
	}


}
