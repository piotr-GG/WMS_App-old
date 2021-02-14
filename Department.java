package app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Department extends GeographicalData {
	
	private IntegerProperty departmentID;
	private StringProperty departmentName;
	private StringProperty phone;
	private StringProperty email;
	
	public Department(int departmentID, String departmentName, String phone, String address, String city, String postalCode, String country, String email){
		super(address, city, postalCode, country);
		this.departmentID = new SimpleIntegerProperty(departmentID);
		this.departmentName = new SimpleStringProperty(departmentName);
		this.phone = new SimpleStringProperty(phone);
		this.email = new SimpleStringProperty(email);
	}

	/**
	 * Getters - properties
	 * 
	 */
	
	public IntegerProperty getDepartmentIDProperty() {
		return departmentID;
	}

	public StringProperty getDepartmentNameProperty() {
		return departmentName;
	}

	public StringProperty getPhoneProperty() {
		return phone;
	}

	public StringProperty getEmailProperty() {
		return email;
	}
	
	/**
	 * Getters - values
	 */
	
	public int getDepartmentID() {
		return departmentID.get();
	}

	public String getDepartmentName() {
		return departmentName.get();
	}

	public String getPhone() {
		return phone.get();
	}

	public String getEmail() {
		return email.get();
	}
	
	/**
	 * Setters
	 */
	
	public void setDepartmentID(int departmentID){
		this.departmentID.set(departmentID);
	}
	
	public void setDepartmentName(String departmentName){
		this.departmentName.set(departmentName);
	}
	
	public void setPhone(String phone){
		this.phone.set(phone);
	}
	
	public void setEmail(String email){
		this.email.set(email);
	}
	
	
}
