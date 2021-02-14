package app.model;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Person extends GeographicalData {

	/*
	 * Class to be inherited by other classes
	 */
	
	private StringProperty firstName;
	private StringProperty lastName;
	private LocalDate birthDate;
	private LocalDate hireDate;
	private StringProperty address;
	private StringProperty city;
	private StringProperty country;
	private StringProperty phoneNumber;
	private StringProperty email;
	
	/**
	 * The primary constructor used to create instances of class with real data
	 */
	
	public Person(String firstName, String lastName, LocalDate birthDate, LocalDate hireDate, String address, String city, String postalCode, String country, String phoneNumber, String email){
		super(address, city, postalCode, country);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.birthDate = birthDate;
		this.hireDate = hireDate;
		this.phoneNumber = new SimpleStringProperty(phoneNumber);
		this.email = new SimpleStringProperty(email);
	}
	
	/**
	 * Constructor used to initialize field with blank values
	 * 
	 */
	
	public Person(){
		this.firstName = new SimpleStringProperty("");
		this.lastName = new SimpleStringProperty("");
		this.birthDate = LocalDate.now();
		this.hireDate = LocalDate.now();
		this.address = new SimpleStringProperty("");
		this.city = new SimpleStringProperty("");
		this.country = new SimpleStringProperty("");
		this.phoneNumber = new SimpleStringProperty("");
		this.email = new SimpleStringProperty("");
	}

	/*
	 * Getters - properties
	 * 
	 */

	public StringProperty getFirstNameProperty(){
		return this.firstName;
	}

	public StringProperty getLastNameProperty(){
		return this.lastName;
	}
	

	public StringProperty getPhoneNumberProperty(){
		return this.phoneNumber;
	}
	
	public StringProperty getEmailProperty(){
		return this.email;
	}
	
	public StringProperty getFullNameProperty(){
		return new SimpleStringProperty(firstName.get() + " " + lastName.get());
	}
	/*
	 * Getters - values
	 */
	
	public String getFirstName(){
		return firstName.get();
	}

	public String getLastName(){
		return lastName.get();
	}
	
	public LocalDate getBirthDate(){
		return this.birthDate;
	}
	
	public LocalDate getHireDate(){
		return this.hireDate;
	}


	public String getPhoneNumber(){
		return phoneNumber.get();
	}
	
	public String getEmail(){
		return email.get();
	}
	
	/**
	 * Setters 
	 */
	
	public void setFirstName(String firstName){
		this.firstName.set(firstName);
	}

	public void setLastName(String lastName){
		this.lastName.set(lastName);
	}
	
	public void setBirthDate(LocalDate birthDate){
		this.birthDate = birthDate;
	}
	
	public void setHireDate(LocalDate hireDate){
		this.hireDate = hireDate;
	}

	
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber.set(phoneNumber);
	}
	
	public void setEmail(String email){
		this.email.set(email);
	}
	
	
}

