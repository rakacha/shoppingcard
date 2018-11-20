package com.abcshopping.customer.controller;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import com.abcshopping.customer.repository.CustomerRepository;

@RestController
public class CustomerController {
	@Autowired
    CustomerRepository customerRepository;
	
	@Autowired
	private RabbitTemplate template;
	
    @GetMapping("/customers")
    public List<Customer> customers() {
        return (List<Customer>) customerRepository.findAll();
    }

    @PostMapping("/customer")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
         Customer savedCustomer = customerRepository.save(customer);
         
         assert savedCustomer != null;
         
         template.convertAndSend(savedCustomer);
         HttpHeaders httpHeaders = new HttpHeaders();
         httpHeaders.setLocation(ServletUriComponentsBuilder
                 .fromCurrentRequest().path("/" + savedCustomer.getId())
                 .buildAndExpand().toUri());

         return new ResponseEntity<>(savedCustomer, httpHeaders, HttpStatus.CREATED);
    }

}
