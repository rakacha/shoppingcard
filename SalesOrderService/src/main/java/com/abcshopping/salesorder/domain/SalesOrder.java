package com.abcshopping.salesorder.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="SALES_ORDER_236220")
public class SalesOrder {

	public SalesOrder() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	
	private Date orderDate;
	
	private String customerId;
	private String orderDesc;
	private double totalPrice;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SalesOrderItem> salesOrderitems = new ArrayList<SalesOrderItem>();
	
	public List<SalesOrderItem> getSalesOrderitems() {
		return salesOrderitems;
	}
	public void setSalesOrderitems(List<SalesOrderItem> salesOrderitems) {
		this.salesOrderitems = salesOrderitems;
	}
	
	public void addSalesOrderitem(SalesOrderItem item) {
		salesOrderitems.add(item);
		item.setOrder(this);
	}
	
	public void removeSalesOrderitem(SalesOrderItem item) {
		salesOrderitems.remove(item);
		item.setOrder(null);
	}
	
	public void removeAllSalesOrderItems() {
		salesOrderitems.clear();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}
