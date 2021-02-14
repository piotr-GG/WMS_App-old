package app.model;

import java.time.LocalDate;
import java.util.GregorianCalendar;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee extends Person {

	private final IntegerProperty EmployeeID;
	private Position position;
	private Department department;
	private static int nextID; //needed for adding new employee
	
	
	
	
	/**
	 * Default constructor for Employee class
	 * @param EmployeeID
	 * @param firstName
	 * @param lastName
	 * @param birthDate
	 * @param hireDate
	 * @param address
	 * @param city
	 * @param country
	 * @param phoneNumber
	 * @param email
	 * @param position
	 */
	
	public Employee(int EmployeeID, String firstName, String lastName, LocalDate birthDate, LocalDate hireDate, String address, String city, String postalCode, String country, String phoneNumber, String email, String position, Department department ){
		super(firstName,lastName,birthDate,hireDate,address,city, postalCode, country,phoneNumber,email);  //invoke constructor from parent class
		this.EmployeeID = new SimpleIntegerProperty(EmployeeID);
		
        try{
        this.position = Enum.valueOf(Position.class, position);
        }
        catch(IllegalArgumentException e){
            System.err.println("Wyst¹pi³ b³¹d przypisania stanowiska do pracownika!");
        }
        
		if(nextID > EmployeeID){
			nextID++;
		} else{
			nextID = EmployeeID + 1;
		}
		
		this.department = department;
		
	}
	
	/**
	 * Constructor used to initialize instance with blank values
	 */
	
	public Employee(){
		super();
		this.EmployeeID = new SimpleIntegerProperty(nextID);
	}
	

	/**
	 * Getters - properties
	 */
	
	public IntegerProperty getEmployeeIDProperty(){
		return this.EmployeeID;
	}
	
	/**
	 * Getters - values
	 */
	
	/*
	 * Gets the extended version of enum type Position. 
	 */
	public Position getPositionNotString(){
		return this.position;
	}
	
	public String getPosition(){
		return this.position.toString();
		
	}
	
	public String getExtendedPosition(){
		return this.position.getExtendedVersion();
	}
	
	public int getEmployeeID(){
		return EmployeeID.get();
	}
	
	public Department getDepartment(){
		return this.department;
	}

	public static int getNextID(){
		return nextID;
	}
	
	
	/**
	 * Setters 
	 */
	public void setPosition(String position){
		try{
		  this.position = Enum.valueOf(Position.class, position);
		   }
		catch(IllegalArgumentException e){
		  System.err.println("Wyst¹pi³ b³¹d przypisania stanowiska do pracownika!");
		   }
	}
	
	public static void decreaseNextID(){
		nextID = nextID - 1;
	}
	
	public void setEmployeeID(int EmployeeID){
		this.EmployeeID.set(EmployeeID);
	}
	
	public void setDepartment(Department department)
	{
		this.department = department;
	}
	/**
	 * For testing class and printing basic info
	 */
	public String toString(){
		return "ID: " + getEmployeeID() + "\nFirst Name:  " + getFirstName() + "\nLast Name:  " + getLastName();
	}
	
	
	/**
	 * Inner class used for one of the Employee class fields (Position)
	 * @author Piotrek
	 *
	 */
	public enum Position{
		Administrator("Administrator"), PracownikBiurowy("Pracownik biurowy"), Ksiêgowy("Ksiêgowy"), PrzedstawicielHandlowy("Przedstawiciel handlowy"), Magazynier("Magazynier");
		
		private String extendedVersion;
		private Position(String extendedVersion) {this.extendedVersion = extendedVersion;}
		
		public String getExtendedVersion(){
			return extendedVersion;
		}
		
		//Gets extended versions of the Postion enum class objects
		public String[] valuesExtended(){
			Position[] values = Position.values();
			String[] extendedValues = new String[values.length]; 
			for(int i = 0; i < values.length; i++){
				extendedValues[i] = values[i].getExtendedVersion();
			}
			return extendedValues;
		}
	}
	

	
	public boolean equalsEmployee(Employee other) {
		Employee otherEmployee = (Employee) other;
		
		if(this.getEmployeeID() != otherEmployee.getEmployeeID())   				return false;
		else if (!(this.getFirstName().equals(otherEmployee.getFirstName())))		return false;
		else if (!(this.getLastName().equals(otherEmployee.getLastName())))			return false;
		else if (!(this.getBirthDate().isEqual(otherEmployee.getBirthDate())))  	return false;
		else if (!(this.getHireDate().isEqual(otherEmployee.getHireDate())))		return false;	
		else if (!(this.getAddress().equals(otherEmployee.getAddress())))			return false;
		else if (!(this.getCountry().equals(otherEmployee.getCountry())))			return false;
		else if (!(this.getCity().equals(otherEmployee.getCity())))					return false;
		else if (!(this.getPhoneNumber().equals(otherEmployee.getPhoneNumber())))	return false;
		else if (!(this.getEmail().equals(otherEmployee.getEmail())))				return false;
		else if (!(this.getPosition().equals(otherEmployee.getPosition())))			return false;
		else if (!(this.getPostalCode().equals(otherEmployee.getPostalCode())))		return false;
		
		else  return true;

	}
	
}









