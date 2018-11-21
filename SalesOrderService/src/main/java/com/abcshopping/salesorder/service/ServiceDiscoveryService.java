package com.abcshopping.salesorder.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

public class ServiceDiscoveryService {

	@Autowired
	private EurekaClient discoveryClient;

	public ServiceDiscoveryService() {
		// TODO Auto-generated constructor stub
	}
	public String fetchServiceUrl(String serviceName) {
	    InstanceInfo instance = discoveryClient.getNextServerFromEureka(serviceName, false);

	    String serviceUrl = instance.getHomePageUrl();
	    System.out.println("discouvery serviceUrl " + serviceUrl);
	    return serviceUrl;
	  }

}
