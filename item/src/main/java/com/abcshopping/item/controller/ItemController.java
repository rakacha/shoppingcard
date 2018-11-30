package com.abcshopping.item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.abcshopping.item.domain.Item;
import com.abcshopping.item.exception.ItemNotFoundException;
import com.abcshopping.item.service.ItemService;



@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;
	
    @GetMapping("/items")
    public List<Item> items() {
        return itemService.getItems();
    }

    @GetMapping("/items/{name}")
    public Item getItem(@PathVariable("name") String name){
        List<Item> items = itemService.getItemByName(name);
        
        if(items != null && items.size() > 0) {
        	return items.get(0);
        }
        
        throw new ItemNotFoundException(name);
    }

    
}
