package com.abcshopping.salesorder.service;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient.RibbonServer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.abcshopping.salesorder.domain.SalesOrderItem;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.loadbalancer.Server;

@Service
public class ItemServiceInvokerService {

	/*
	 * Commented out the method as using the client side load balancer using ribbon
	 */
/*	@Autowired
	private EurekaClient discoveryClient;
	
	public String fetchServiceUrl(String serviceName) {
	    InstanceInfo instance = discoveryClient.getNextServerFromEureka(serviceName, false);

	    String serviceUrl = instance.getHomePageUrl();
	    System.out.println("discovery serviceUrl " + serviceUrl);
	    return serviceUrl;
	  }
*/
	
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	private RestTemplate restTemplate = new RestTemplate();

	@HystrixCommand(fallbackMethod="callDefaultItemService")
	public SalesOrderItem callItemService(String serviceName, SalesOrderItem item, List<String> errorMessages) {
		String fetchServiceUrl = fetchServiceUrl(serviceName, item);
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
		
		if(validateResponse(item, errorMessages, response)) {
			return response.getBody().getContent();
		}

		return null;
	}

	private boolean validateResponse(SalesOrderItem item, List<String> errorMessages,
			ResponseEntity<Resource<SalesOrderItem>> response) {
		if(response == null) {
			errorMessages.add("No matching item found with the given name: " + item.getItemName() + "\n");
			return false;
		}
		
		assert response != null;
		if(response.getStatusCode() == HttpStatus.OK){
			if(response.getBody() == null) {
				errorMessages.add("No matching item found with the given name: " + item.getItemName() + "\n");
				return false;
			}
			
			SalesOrderItem responseItem = response.getBody().getContent();
			assert responseItem != null;

			if(responseItem == null) {
				errorMessages.add("No matching item found with the given name: " + item.getItemName() + "\n");
				return false;
			}
			
			return true;
		}
		return false;
	}
	
	public SalesOrderItem callDefaultItemService(String serviceName, SalesOrderItem item, List<String> errorMessages) {
		errorMessages.add("No response from the item service for item : " + item.getItemName() +" . Please try again later!");
		return null;
	}
	
	
	private String fetchServiceUrl(String serviceName, SalesOrderItem item) {
	    ServiceInstance instance = loadBalancerClient.choose(serviceName);
	    
	    if(instance instanceof RibbonLoadBalancerClient.RibbonServer) {
	    	RibbonServer serverInstance = (RibbonServer)instance;
	    	Server server = serverInstance.getServer();
	    	if(server != null && server.getMetaInfo() != null) {
	    		String instanceId = server.getMetaInfo().getInstanceId();
	    		item.setServiceInstanceId(instanceId);
	    	}
	    }
	    
	    String serviceUrl = instance.getUri().toString();
	    System.out.println("discovery service instance id " + instance.getInstanceId() + "discovery serviceUrl " + serviceUrl);
	    return serviceUrl;
	}
	
}
