package com.abcshopping.salesorder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class ServiceDiscoveryService {

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

	@HystrixCommand(fallbackMethod="fetchDefaultServiceUrl")
	public String fetchServiceUrl(String serviceName) {
	    ServiceInstance instance = loadBalancerClient.choose(serviceName);

	    String serviceUrl = instance.getUri().toString();
	    System.out.println("discovery serviceUrl " + serviceUrl);
	    return serviceUrl;
	}
	
	public String fetchDefaultServiceUrl(String serviceName) {
		return null;
	}

}
