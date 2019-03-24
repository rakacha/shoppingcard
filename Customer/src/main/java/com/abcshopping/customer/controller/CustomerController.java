package com.abcshopping.customer.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abcshopping.customer.domain.Customer;
import com.abcshopping.customer.service.CustomerQueueingService;
import com.abcshopping.customer.service.CustomerService;

@RestController
public class CustomerController {
	
	private final Logger LOG = Logger.getLogger(CustomerController.class.getName());
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerQueueingService queueService;
	
    @GetMapping("/customers")
    public List<Customer> customers() {
        return customerService.getCustomers();
    }

    @PostMapping("/customer")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
    	LOG.log(Level.ALL, "Adding customer");
    	try {
    		Customer savedCustomer = customerService.saveCustomer(customer);
            
            assert savedCustomer != null;
            
            queueService.sendMessageToQueue(savedCustomer);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/" + savedCustomer.getId())
                    .build().toUri());

            return new ResponseEntity<>(savedCustomer, httpHeaders, HttpStatus.CREATED);
    	}catch (Exception e) {
    		return new ResponseEntity<>("Exception occurred while adding new customer" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
         
    }

}
