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
import com.netflix.discovery.EurekaClient;

@Service
public class SalesOrderService {

	public SalesOrderService() {
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	private ServiceDiscoveryService serviceDiscoveryService;
	private RestTemplate restTemplate = new RestTemplate();

	public String getSalesOrderItems(SalesOrder salesOrder, String headerMessage, String serviceName, List<SalesOrderItem> salesOrderitems,
			List<SalesOrderItem> responseSalesOrderitems) {
		for(SalesOrderItem item: salesOrderitems) {

			String fetchServiceUrl = serviceDiscoveryService.fetchServiceUrl(serviceName);
			String url = fetchServiceUrl + "/item/" + item.getItemName();

			ParameterizedTypeReference<Resource<SalesOrderItem>> salesOrderItem = new ParameterizedTypeReference<Resource<SalesOrderItem>>() {};
			ResponseEntity<Resource<SalesOrderItem>> response = restTemplate.exchange(
					RequestEntity.get(URI.create(url))
					.accept(MediaTypes.HAL_JSON)
					.build(),
					salesOrderItem
					);

			assert response != null;
			if(response.getStatusCode() == HttpStatus.OK){
				SalesOrderItem  responseItem = response.getBody().getContent();
				assert responseItem != null;

				if(responseItem == null) {
					headerMessage = headerMessage + "No matching item found with the given name: " + item.getItemName() + "\n";
				}
				else {
					responseItem.setItemQuantity(item.getItemQuantity());
					salesOrder.setTotalPrice(salesOrder.getTotalPrice() + (responseItem.getItemPrice() * responseItem.getItemQuantity()));
					responseSalesOrderitems.add(responseItem);
				}
			} 

		}
		return headerMessage;
	}
	

}
