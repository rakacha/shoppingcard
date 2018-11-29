package com.abcshopping.item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.abcshopping.item.domain.Item;
import com.abcshopping.item.repository.ItemRepository;

@RestController
public class ItemController {
	@Autowired
    ItemRepository itemRepository;

    @GetMapping("/items")
    public List<Item> items() {
        return (List<Item>) itemRepository.findAll();
    }

    @GetMapping("/items/{name}")
    public Item getItem(@PathVariable("name") String name) {
        List<Item> items = itemRepository.findItemByName(name);
        
        if(items != null && items.size() > 0) {
        	return items.get(0);
        }
        
        return null;
    }

    
}
