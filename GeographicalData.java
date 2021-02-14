package app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
	 * Class that contains fields to be inherited and used to inform of order address, shipment postal code and , for example, user country etc.
	 * @author Piotrek
	 *
	 */
public abstract class GeographicalData {
	
	private StringProperty address;
	private StringProperty city;
	private StringProperty postalCode;
	private StringProperty country;
	
	
	public StringProperty getAddressProperty() {
		return address;
	}

	public StringProperty getCityProperty() {
		return city;
	}

	public StringProperty getPostalCodeProperty() {
		return postalCode;
	}

	public StringProperty getCountryProperty() {
		return country;
	}
	
	public String getAddress() {
		return address.get();
	}

	public String getCity() {
		return city.get();
	}

	public String getPostalCode() {
		return postalCode.get();
	}

	public String getCountry() {
		return country.get();
	}
	
	
	public void setAddress(String address){
		this.address.set(address);
	}
	
	public void setCity(String city){
		this.city.set(city);
	}
	
	public void setPostalCode(String postalCode){
		this.postalCode.set(postalCode);
	}
	
	public void setCountry(String country){
		this.country.set(country);
	}
	
	public GeographicalData(String address, String city, String postalCode, String country){
		this.address = new SimpleStringProperty(address);
		this.city = new SimpleStringProperty(city);
		this.postalCode = new SimpleStringProperty(postalCode);
		this.country = new SimpleStringProperty(country);
	}
	
	public GeographicalData(){
		this.address = new SimpleStringProperty();
		this.city = new SimpleStringProperty();
		this.postalCode = new SimpleStringProperty();
		this.country = new SimpleStringProperty();
	}
}
