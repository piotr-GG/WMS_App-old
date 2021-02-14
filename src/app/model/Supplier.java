package app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Supplier extends GeographicalData{

	private IntegerProperty supplierID;
	private StringProperty companyName;
	private StringProperty representative;
	private StringProperty phoneNumber;
	private StringProperty other;
	

	public Supplier(int supplierID, String companyName, String representativeName, String address, String city, String postalCode, String country, String phoneNumber, String other)
	{
		super(address, city, postalCode, country);
		this.supplierID = new SimpleIntegerProperty(supplierID);
		this.companyName = new SimpleStringProperty(companyName);
		this.representative = new SimpleStringProperty(representativeName);
		this.phoneNumber = new SimpleStringProperty(phoneNumber);
		this.other = new SimpleStringProperty(other);

	}

	public Supplier(){
		super("", "", "", "");
		this.supplierID = new SimpleIntegerProperty();
		this.companyName = new SimpleStringProperty();
		this.representative = new SimpleStringProperty();
		this.phoneNumber = new SimpleStringProperty();
		this.other = new SimpleStringProperty();
	}
	/**
	 * Getters - properties
	 */
	public IntegerProperty getSupplierIDProperty() {
		return supplierID;
	}

	public StringProperty getCompanyNameProperty() {
		return companyName;
	}

	public StringProperty getRepresentativeProperty() {
		return representative;
	}

	public StringProperty getPhoneNumberProperty() {
		return phoneNumber;
	}

	public StringProperty getOtherProperty() {
		return other;
	}
	
	
	/**
	 * Getters - values
	 */
	
	public int getSupplierID() {
		return supplierID.get();
	}

	public String getCompanyName() {
		return companyName.get();
	}

	public String getRepresentative() {
		return representative.get();
	}

	public String getPhoneNumber() {
		return phoneNumber.get();
	}

	public String getOther() {
		return other.get();
	}
	
	/**
	 * Setters - values
	 */
	
	public void setSupplierID(int supplierID){
		this.supplierID.set(supplierID);
	}

	public void setCompanyName(String companyName){
		this.companyName.set(companyName);
	}

	public void setRepresentative(String representative){
		this.representative.set(representative);
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber.set(phoneNumber);
	}
	
	public void setOther(String other){
		this.other.set(other);
	}


	
	public boolean areSuppliersEqual(Supplier other){
		if(getSupplierID() != other.getSupplierID()) return false;
		if(!getCompanyName().equals(other.getCompanyName())) return false;
		if(!getRepresentative().equals(other.getRepresentative())) return false;
		if(!getAddress().equals(other.getAddress())) return false;
		if(!getCity().equals(other.getCity())) return false;
		if(!getPostalCode().equals(other.getPostalCode())) return false;
		if(!getCountry().equals(other.getCountry())) return false;
		if(!getPhoneNumber().equals(other.getPhoneNumber())) return false;
		if(!getOther().equals(other.getOther())) return false;
		
		return true;

	}

}
