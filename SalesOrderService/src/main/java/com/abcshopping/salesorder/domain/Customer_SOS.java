package com.abcshopping.salesorder.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CUSTOMER_SOS_236220")
public class Customer_SOS {

	@Id
    private String id;
    private String email;
    private String firstname;
    private String lastname;

    public Customer_SOS() {}

    public Customer_SOS(String email, String firstname, String lastname) {
        this.id = java.util.UUID.randomUUID().toString();
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getId() {
        return id;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
    

}
