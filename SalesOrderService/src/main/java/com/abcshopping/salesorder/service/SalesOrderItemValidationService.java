package com.abcshopping.salesorder.service;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.abcshopping.salesorder.domain.SalesOrder;
import com.abcshopping.salesorder.domain.SalesOrderItem;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class SalesOrderItemValidationService {

	public SalesOrderItemValidationService() {
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	private ServiceDiscoveryService serviceDiscoveryService;
	private RestTemplate restTemplate = new RestTemplate();

	public String getSalesOrderItems(SalesOrder salesOrder, String serviceName, List<SalesOrderItem> salesOrderitems,
			List<SalesOrderItem> responseSalesOrderitems) {
		
		String headerMessage = "";
		for(SalesOrderItem item: salesOrderitems) {
			
			SalesOrderItem  responseItem= null;
			
			ResponseEntity<Resource<SalesOrderItem>> response = callItemService(serviceName, item);

			if(response == null) {
				headerMessage  = headerMessage + "Item Service currently unavailable. Please try again later!";
				return headerMessage;
			}
			
			assert response != null;
			if(response.getStatusCode() == HttpStatus.OK){
				if(response.getBody() == null) {
					headerMessage = headerMessage + "No matching item found with the given name: " + item.getItemName() + "\n";
					continue;
				}
				
				responseItem = response.getBody().getContent();
				assert responseItem != null;

				if(responseItem == null) {
					headerMessage = headerMessage + "No matching item found with the given name: " + item.getItemName() + "\n";
					continue;
				}
			} 
			
			if(responseItem != null) {
				responseItem.setItemQuantity(item.getItemQuantity());
				salesOrder.setTotalPrice(salesOrder.getTotalPrice() + (responseItem.getItemPrice() * responseItem.getItemQuantity()));
				responseSalesOrderitems.add(responseItem);
			}
		}
		return headerMessage;
	}

	public ResponseEntity<Resource<SalesOrderItem>> callItemService(String serviceName, SalesOrderItem item) {
		String fetchServiceUrl = serviceDiscoveryService.fetchServiceUrl(serviceName);
		if(fetchServiceUrl == null) {
			return null;
		}
		String url = fetchServiceUrl + "/items/" + item.getItemName();

		ParameterizedTypeReference<Resource<SalesOrderItem>> salesOrderItem = new ParameterizedTypeReference<Resource<SalesOrderItem>>() {};
		ResponseEntity<Resource<SalesOrderItem>> response = restTemplate.exchange(
				RequestEntity.get(URI.create(url))
				.accept(MediaTypes.HAL_JSON)
				.build(),
				salesOrderItem
				);
		return response;
	}
	
	
}
