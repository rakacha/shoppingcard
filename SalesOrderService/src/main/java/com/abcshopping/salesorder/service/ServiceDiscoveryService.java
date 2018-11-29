package com.abcshopping.salesorder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient.RibbonServer;
import org.springframework.stereotype.Service;

import com.abcshopping.salesorder.domain.SalesOrderItem;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.loadbalancer.Server;

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
	public String fetchServiceUrl(String serviceName, SalesOrderItem item) {
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
	
	public String fetchDefaultServiceUrl(String serviceName, SalesOrderItem item) {
		return null;
	}

}
