package app.model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer extends GeographicalData  {
	
	private final StringProperty customerID;
	private StringProperty companyName;
	private StringProperty representative;
	private StringProperty phone;
	private StringProperty email;
	
	public Customer(String customerID, String companyName, String representative, String address, String city, String country, String postalCode, String phone, String email){
		super(address, city, postalCode, country);
		this.customerID = new SimpleStringProperty(customerID);
		this.companyName = new SimpleStringProperty(companyName);
		this.representative = new SimpleStringProperty(representative);
		this.phone = new SimpleStringProperty(phone);
		this.email = new SimpleStringProperty(email);

		
	}
	
	public Customer(){
		super("", "", "", "");
		this.customerID = new SimpleStringProperty();
		this.companyName = new SimpleStringProperty();
		this.representative = new SimpleStringProperty();
		this.phone = new SimpleStringProperty();
		this.email = new SimpleStringProperty();
	}
	
	/*
	 * Getters - properties
	 */
	
	public StringProperty getCustomerIDProperty(){
		return this.customerID;
	}
	
	public StringProperty getCompanyNameProperty(){
		return this.companyName;
	}
	
	public StringProperty getRepresentativeProperty(){
		return this.representative;
	}
	
	/*
	 * Getters - values
	 */
	
	public String getCustomerID(){
		return this.customerID.get();
	}
	
	public String getCompanyName(){
		return this.companyName.get();
	}
	
	public String getRepresentative(){
		return this.representative.get();
	}
	
	
	public String getPhone(){
		return this.phone.get();
	}
	
	public String getEmail(){
		return this.email.get();
	}
	
	
	/*
	 * Setters
	 */
	
	public void setCompanyName(String companyName){
		this.companyName.set(companyName);
	}
	
	public void setRepresentative(String representative){
		this.representative.set(representative);
	}
	
	public boolean areCustomersEqual(Customer other){
		if(!this.getCustomerID().equals(other.getCustomerID())) return false;
		if(!this.getCompanyName().equals(other.getCompanyName())) return false;
		return true;
	}
	

}