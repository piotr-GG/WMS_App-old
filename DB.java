package app.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Set;



import app.model.Category;
import app.model.Customer;
import app.model.Delivery;
import app.model.DeliveryDetails;
import app.model.Department;
import app.model.Employee;
import app.model.EnterpriseData;
import app.model.Location;
import app.model.Message;
import app.model.Order;
import app.model.OrderDetails;
import app.model.Permission;
import app.model.Picking;
import app.model.Product;
import app.model.Shipment;
import app.model.ShipmentDetails;
import app.model.Shipper;
import app.model.ShippingInfo;
import app.model.Supplier;
import app.model.User;
import app.model.Delivery.DeliveryStatus;
import app.model.Picking.PickingDetails;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.LocalDateStringConverter;

/**
 * Klasa obs³uguj¹ca bazê danych
 * @author Piotrek
 *
 */



public class DB {
	
	public static final String DRIVER = "org.sqlite.JDBC";
	//public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:sqlite:test.db";
	//public static final String DB_URL = "jdbc:mysql://db4free.net:3306/warehouse_db?useUnicode=true&characterEncoding=utf-8&useSSL=false";	
	
	
	private Statement stmt;
	private Connection con;
	
	
	public DB(){
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception e){
			System.err.println("NO JDBC DRIVER");
			e.printStackTrace();
		}

		try{
			con = DriverManager.getConnection(DB_URL);
			stmt = con.createStatement();
		}
		catch (SQLException e){
			System.err.println("Error occurred while connecting to DB");
			e.printStackTrace();
		}
	}

	/*
	 * EMPLOYEE TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public boolean insertEmployeeRecord(Employee employee){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO employees VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			prepStmt.setInt(1, employee.getEmployeeID());
			prepStmt.setString(2, employee.getFirstName());
			prepStmt.setString(3, employee.getLastName());
			prepStmt.setString(4, employee.getPosition());
			prepStmt.setInt(5, employee.getDepartment().getDepartmentID());
			prepStmt.setString(6, employee.getBirthDate().toString());
			prepStmt.setString(7, employee.getHireDate().toString());
			prepStmt.setString(8, employee.getAddress());
			prepStmt.setString(9, employee.getCity());
			prepStmt.setString(10, employee.getPostalCode());
			prepStmt.setString(11, employee.getCountry());
			prepStmt.setString(12, employee.getPhoneNumber());
			prepStmt.setString(13, employee.getEmail());
			prepStmt.execute();
			return true;
			
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Updates record from employees table
	 * @param EmployeeID
	 * @param firstName
	 * @param lastName
	 * @param birthDate
	 * @param hireDate
	 * @param address
	 * @param city
	 * @param country
	 * @param position
	 * @return
	 */
	
	public boolean updateEmployee(Employee employee){
		try{
		PreparedStatement prepStmt = con.prepareStatement("UPDATE employees SET FirstName = ?, LastName = ?, BirthDate = ?, HireDate = ?, Address = ?, City = ?, Country = ?, Position = ?, PhoneNumber = ?, Email = ?, PostalCode = ? WHERE EmployeeID = ?;");
		prepStmt.setString(1, employee.getFirstName());
		prepStmt.setString(2, employee.getLastName());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(employee.getBirthDate().toString(), formatter);
		Date birthD = Date.valueOf(localDate);
		prepStmt.setString(3, birthD.toString());
		
		localDate = LocalDate.parse(employee.getHireDate().toString(), formatter);
		Date hireD = Date.valueOf(localDate);
		prepStmt.setString(4, hireD.toString() );
		
		prepStmt.setString(5, employee.getAddress());
		prepStmt.setString(6, employee.getCity());
		prepStmt.setString(7, employee.getCountry());
		prepStmt.setString(8, employee.getPosition());
		prepStmt.setString(9, employee.getPhoneNumber());
		prepStmt.setString(10, employee.getEmail());
		prepStmt.setString(11, employee.getPostalCode());
		prepStmt.setInt(12, employee.getEmployeeID());
		
		prepStmt.execute();
		
		return true;
		} 
		catch(SQLException e){
		    System.err.println("ERROR while updating record from Employees Table");
	        e.printStackTrace();
			return false;
		}
		
	}
	

	public int getNextEmployeeID(){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT MAX(EmployeeID) + 1 as NextID FROM employees;");
			ResultSet rs = prepStmt.executeQuery();

			int nextID = 1;
			while(rs.next()){
				nextID = rs.getInt("NextID");
			}
			return nextID;
			
			} catch(SQLException e){
				e.printStackTrace();
				return -1;
				
			}
	}
	

	/**
	 * Get data from database and put it in Employee ObservableList
	 * 
	 */
	
	public ObservableList<Employee> getEmployees(){
		
		ObservableList<Employee> tempList = FXCollections.observableArrayList();
		ObservableList<Department> departments = FXCollections.observableArrayList();
		departments = getAvailableDepartments();
		
		String sql = "SELECT * FROM employees ORDER BY EmployeeID";
		try{
			ResultSet rs = stmt.executeQuery(sql);
			tempList = getEmployeeDataFromResultSet(rs, false);
			return tempList;
		}
		catch(SQLException e){
			System.err.println("ERROR while getting  Data From Employee Table");
			e.printStackTrace();
			return null;			
		}
	}
	/**
	 * Gets employees that have no attributed user
	 * @param name
	 * @param lastName
	 * @return
	 */
	public ObservableList<Employee> getAllEmployeesToAdd(String name, String lastName){
		ObservableList<Employee> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM employees e LEFT OUTER JOIN users u ON u.EmployeeID = e.EmployeeID WHERE u.EmployeeID is null AND e.FirstName = ? AND e.LastName = ?");
			prepStmt.setString(1, name);
			prepStmt.setString(2, lastName);
			ResultSet rs = prepStmt.executeQuery();
			
			tempList = getEmployeeDataFromResultSet(rs, false);

			return tempList;
		} 
		catch(SQLException e){
			System.err.println("ERROR while getting Employees to add");
			e.printStackTrace();
			return null;
		}
	}
	
	public int getCountEmployeesWithoutUser(String name, String lastName){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT COUNT(*) AS counted FROM employees e LEFT OUTER JOIN users u ON u.EmployeeID = e.EmployeeID WHERE u.EmployeeID is null AND e.FirstName = ? AND e.LastName = ?");
			prepStmt.setString(1, name);
			prepStmt.setString(2, lastName);
			ResultSet rs = prepStmt.executeQuery();
			
			int count = 0;
			while(rs.next()){
				count = rs.getInt("counted");
			}
			return count;
			
		}catch(SQLException e){
			System.err.println("ERROR while counting Employees to add");
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Delete an employee from database table by EmployeeID
	 * @param employee
	 * @return
	 */
	
	public boolean deleteEmployee(Employee employee){
		try{
			PreparedStatement prepStmt = con.prepareStatement("DELETE FROM Employees WHERE EmployeeID = ?");
			prepStmt.setInt(1, employee.getEmployeeID());
			prepStmt.execute();
			return true;
			
		}
		catch(SQLException e){
			System.err.println("ERROR while deleting Employee from DB");
			e.printStackTrace();
			return false;
		}
	}
	

	public int getEmployeesCount(){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT Count(*) as Value FROM employees");
			ResultSet rs = prepStmt.executeQuery();
			int value = -1;
			while(rs.next()){
				value = rs.getInt("Value");	
			}
			return value;
			
			} catch(SQLException e){
				e.printStackTrace();
				return -1;
			}
	}
	
	public Employee getEmployeeByID(int employeeID){
	try{
		Employee employee = null;
		PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM employees WHERE EmployeeID = ?");
		prepStmt.setInt(1, employeeID);
		ResultSet rs = prepStmt.executeQuery();
		ObservableList<Employee> result = getEmployeeDataFromResultSet(rs, true);
		if(!result.isEmpty()){
			employee = result.get(0);
		}
		
		return employee;
		
	 } catch(SQLException e){
		 e.printStackTrace();
		 return null;
	 }

	}
	
	private ObservableList<Employee> getEmployeeDataFromResultSet(ResultSet rs, boolean getSingleRecordOnly) throws SQLException{
		ObservableList<Employee> tempList = FXCollections.observableArrayList();
		while(rs.next()){
			int EmployeeID = rs.getInt("EmployeeID");
			String firstName = rs.getString("FirstName");
			String lastName = rs.getString("LastName");
			

			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			//Convert date string from database into LocalDate class instance
			String bdstring = rs.getString("BirthDate");
			LocalDate birthDate = LocalDate.parse(bdstring, formatter);

			String hdstring = rs.getString("HireDate");
			LocalDate hireDate = LocalDate.parse(hdstring, formatter);
			
			String address = rs.getString("Address");
			String city = rs.getString("City");
			String postalCode = rs.getString("PostalCode");
			String country = rs.getString("Country");
			String phoneNumber = rs.getString("PhoneNumber");
			String email = rs.getString("Email");
			String position = rs.getString("Position");

			int departmentID = rs.getInt("DepartmentID");
			Department department = getDepartmentByID(departmentID);
			
			tempList.add(new Employee(EmployeeID, firstName, lastName, birthDate, hireDate, address, city, postalCode, country, phoneNumber, email, position, department));
			
			if(getSingleRecordOnly) return tempList;

		}
		return tempList;
	}
	/*
	 * USER TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public boolean checkIfUserExits(String login){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM users WHERE login = ?");
			prepStmt.setString(1, login);
			ResultSet rs = prepStmt.executeQuery();
			if(rs.next()){
				return true;
			} else {
				return false;
			}
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public User loginProcedure(String login, String password){
		User user = null;
		try{
		PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM users WHERE login = ? AND password = ? AND Confirmed = 'TRUE'");
		prepStmt.setString(1, login);
		prepStmt.setString(2, password);
		ResultSet rs = prepStmt.executeQuery();
		ObservableList<User> result = getUserDataFromResultSet(rs, true);
		if(!result.isEmpty()){
			user = result.get(0);
		} 
		
		return user;

		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Inserts new user into database
	 */
	public boolean insertUserRecord(User user){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO users VALUES (?,?,?,?,?,?)");
			prepStmt.setInt(1, user.getUserID());
			prepStmt.setString(2, user.getLogin());
			prepStmt.setString(3, user.getPassword());
			prepStmt.setInt(4, user.getEmployee().getEmployeeID());
			prepStmt.setString(5, "");
			prepStmt.setString(6, "FALSE");

			prepStmt.execute();
			
			insertPermissions(user.getUserID(), user.getPermission());
			
			return true;
		} 
		catch(SQLException e){
	        e.printStackTrace();
			return false;
		}

	}
	
	/**
	 * Updates user record 
	 * @param User user 
	 */
	
	public boolean updateUser(User user){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE users SET Login = ?, Password = ?, EmployeeID = ? WHERE UserID = ? ");
			prepStmt.setString(1, user.getLogin());
			prepStmt.setString(2, user.getPassword());
			prepStmt.setInt(3, user.getEmployee().getEmployeeID());
			prepStmt.setInt(4, user.getUserID());
			prepStmt.execute();
			return true;

		} catch (SQLException e){
			System.err.println("ERROR while updating record from Users Table");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Get data from "USERS" table in database and insert it into program
	 */
	
	public ObservableList<User> getUsers(boolean includeUnconfirmedUsers) throws IOException{
		ObservableList<User> tempList = FXCollections.observableArrayList(); 
		
		String sql = "";
		if(includeUnconfirmedUsers) sql = "SELECT * FROM users";
		else sql = "SELECT * FROM users WHERE Confirmed = 'TRUE'";

		try{
			ResultSet rs = stmt.executeQuery(sql);
			tempList = getUserDataFromResultSet(rs, false);
			return tempList;
		}
		catch(SQLException e){
			System.err.println("ERROR while inserting User Data From DB");
			e.printStackTrace();
			return null;
		}
		
	}
	

	/**
	 * Get all the user with field 'Confirmed' set to 'FALSE'
	 * @param employeeList
	 * @return
	 */
	public ObservableList<User> getUsersToBeConfirmed(){
		ObservableList<User> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM users WHERE Confirmed = 'FALSE'");
			ResultSet rs = prepStmt.executeQuery();
			tempList = getUserDataFromResultSet(rs, false);
			return tempList;
			
		} 
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Get user data from result set, using this function shortens the class code length and makes it look more clearly.  
	 * The boolean value specifies if the function should stop after reading first record
	 * @param rs
	 * @param inputSingleRecord
	 * @param employeeList
	 * @return
	 * @throws SQLException
	 */
	public ObservableList<User> getUserDataFromResultSet(ResultSet rs, boolean inputSingleRecord) throws SQLException{
		ObservableList<User> tempList = FXCollections.observableArrayList();
		while(rs.next()){
		int userID = rs.getInt("UserID");
		String login = rs.getString("Login");
		String password = rs.getString("Password");
		int employeeID = rs.getInt("EmployeeID");
			

		Image userPhoto = null;
		
		String hasPhoto = rs.getString("Photo");
		if(hasPhoto.trim().length() != 0){
			InputStream input = rs.getBinaryStream("Photo");
			userPhoto = new Image(input);
		}

			
		Permission permission = getPermissionForUser(userID);
			
		//Add employee reference to the new user instance (it's essential for the program)
		
		Employee employee = getEmployeeByID(employeeID);
		if(employee != null){
			tempList.add(new User(userID,login,password, employee, permission, userPhoto));	
		} else {
			System.err.println("Brak przypisanego pracownika do u¿ytkownika!");
		}
		
		if(inputSingleRecord) {
			return tempList;
		}
	}
		
		return tempList;
	}
	
	/**
	 * Sets value of field 'Confirmed' To 'True' and returns true if the query has been executed properly
	 * @param userID
	 * @return
	 */
	public boolean confirmUser(int userID){
		try {
			PreparedStatement prepStmt = con.prepareStatement("UPDATE users SET Confirmed = 'TRUE' WHERE UserID = ?");
			prepStmt.setInt(1, userID);
			prepStmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteUser(int userID){
		try {
			PreparedStatement prepStmt = con.prepareStatement("DELETE FROM users WHERE UserID = ?");
			prepStmt.setInt(1, userID);
			prepStmt.execute();
			
			deletePermissions(userID);
			deleteMessages(userID);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public User getUserByID(int userID){
		User user = null;
		try{
		PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM users WHERE UserID = ?");
		prepStmt.setInt(1, userID);

		ResultSet rs = prepStmt.executeQuery();
		ObservableList<User> result = getUserDataFromResultSet(rs, true);
		if(!result.isEmpty()){
			user = result.get(0);
		} 
		
		return user;

		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Saves the given file as BLOB ( or Binary Stream) object into database. The function is used when changing user profile photo (MyProfileOverview.fxml)
	 * @param file - image passed as file to be saved into DB
	 * @param userID - determines the user whose 'Photo' field should be updated
	 * @throws SQLException
	 */
	public void saveImage(File file, int userID) throws SQLException{
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(file);
			
		} catch (FileNotFoundException e) {
			
		}
		
		int fileLength = (int) file.length();
		
		PreparedStatement prepStmt = con.prepareStatement("UPDATE users SET Photo = ? WHERE UserID = ?");
		prepStmt.setBinaryStream(1, fis, fileLength);
		prepStmt.setInt(2, userID);
		prepStmt.execute();
		
		}
	
	/**
	 * Gets the overall count of users existing in database
	 * @return userCount
	 */
	public int getUsersCount(){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT Count(*) as Value FROM users");
			ResultSet rs = prepStmt.executeQuery();
			int value = -1;
			while(rs.next()){
				value = rs.getInt("Value");	
			}
			return value;
			
			} catch(SQLException e){
				e.printStackTrace();
				return -1;
			}
	}
	
	/**
	 * Gets an integer value that has not been yet used in database . The function is used to avoid UNIQUE OrderID constraint violations
	 * @return
	 */
	public int getNextUserID(){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT MAX(UserID) + 1 as NextID FROM users;");
			ResultSet rs = prepStmt.executeQuery();

			int nextID = -1;
			while(rs.next()){
				nextID = rs.getInt("NextID");
			}
			return nextID;
			
			} catch(SQLException e){
				e.printStackTrace();
				
			}
		return -1;
	}
	
	
	/*
	 * CATEGORY TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	/**
	 * Gets the category with CategoryID passed into the function
	 * @param categoryID
	 * @return
	 */
	
	public Category getCategoryByID(int categoryID){
		Category category = null;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM categories WHERE CategoryID = ?");
			prepStmt.setInt(1, categoryID);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				int catID = rs.getInt("CategoryID");
				String categoryName = rs.getString("CategoryName");
				String description = rs.getString("Description");
				
				Image categoryPhoto = null;
				
				String hasPhoto = rs.getString("CategoryPhoto");
				if(hasPhoto.trim().length() != 0){
					InputStream input = rs.getBinaryStream("CategoryPhoto");
					categoryPhoto = new Image(input);
				}
				category = new Category(catID, categoryName, description, categoryPhoto);
			}
			return category;
		} 
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets an observableList of Strings which represent Category Names. The function is used to insert strings into ComboBoxes. If a new category is added into database, the comboboxes won't need to be customized manually
	 * @return
	 */
	
	public ObservableList<String> getCategoryNames(){
		try{
			ObservableList<String> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = con.prepareStatement("SELECT CategoryName FROM categories");

			
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				tempList.add(rs.getString("CategoryName"));
			}
			return tempList;
		}
		catch(SQLException e){
			System.err.print("ERROR while gettting category names");
			e.printStackTrace();
			return null;
		}	
	
	}
	
	public Category getCategoryByName(String categoryName){
		Category category = null;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM categories WHERE CategoryName = ?");
			prepStmt.setString(1, categoryName);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				int catID = rs.getInt("CategoryID");
				String description = rs.getString("Description");
				Image categoryPhoto = null;
				
				String hasPhoto = rs.getString("CategoryPhoto");
				if(hasPhoto.trim().length() != 0){
					InputStream input = rs.getBinaryStream("CategoryPhoto");
					categoryPhoto = new Image(input);
				}
				category = new Category(catID, categoryName, description, categoryPhoto);
			}
			return category;
		} 
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
    public int getNextCategoryID(){
	int nextID = 1;
	try{
		PreparedStatement prepStmt = con.prepareStatement("SELECT MAX(CategoryID) + 1 as NextID FROM categories");
		ResultSet rs = prepStmt.executeQuery();

		while(rs.next()){
			nextID = rs.getInt("NextID");

		}
			return nextID;
		} catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
    
    public boolean insertCategoryRecord(Category c, File categoryPhoto){
    	try{
    		PreparedStatement prepStmt = con.prepareStatement("INSERT INTO categories VALUES (?, ?, ?, ?)");
    		prepStmt.setInt(1, c.getCategoryID());
    		prepStmt.setString(2, c.getCategoryName());
    		prepStmt.setString(3, c.getDescription());
    		prepStmt.setString(4, "");
    		
    		prepStmt.execute();
    		
    		updateCategoryImage(c.getCategoryID(), categoryPhoto);

    		return true;

    		} catch(SQLException e){
    			e.printStackTrace();
    			return false;
    		}
    }
	
    public boolean updateCategoryImage(int categoryID, File file){
    	try {
			FileInputStream fis = null;
			try{
				fis = new FileInputStream(file);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			PreparedStatement prepStmt = con.prepareStatement("UPDATE categories SET CategoryPhoto = ? WHERE CategoryID = ?");
			int fileLength = (int) file.length();
			prepStmt.setBinaryStream(1, fis, fileLength);
			prepStmt.setInt(2, categoryID);
			prepStmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		
		
		
		

		
		
    }
	/*
	 * DEPARTMENT TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	/**
	 * Get all departments from DB
	 */
	
	private ObservableList<Department> getAvailableDepartments(){
		ObservableList<Department> tempList = FXCollections.observableArrayList();
		String sql = "SELECT * FROM departments";
		try{
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				int departmentID = rs.getInt("DepartmentID");
				String departmentName = rs.getString("DepartmentName");
				String phone = rs.getString("Phone");
				String address = rs.getString("Address");
				String city = rs.getString("City");
				String postalCode = rs.getString("PostalCode");
				String country = rs.getString("Country");
				String email = rs.getString("Email");
				
				tempList.add(new Department(departmentID, departmentName, phone, address, city, postalCode, country, email));
			}
			return tempList;
		}
		catch(SQLException e){
			System.err.println("ERROR while getting Departments From Employee Table");
			e.printStackTrace();
			return null;			
		}
	}
	
	/**
	 * Get one department from DB
	 */
	
	public Department getDepartment(String departmentName){
		Department dep = null;

		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM departments WHERE DepartmentName = ?");
			prepStmt.setString(1, departmentName);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				int departmentID = rs.getInt("DepartmentID");
				departmentName = rs.getString("DepartmentName");
				String phone = rs.getString("Phone");
				String address = rs.getString("Address");
				String city = rs.getString("City");
				String postalCode = rs.getString("PostalCode");
				String country = rs.getString("Country");
				String email = rs.getString("Email");
				
				dep = new Department(departmentID, departmentName, phone, address, city, postalCode, country, email);
			}
			return dep;
		}
		catch(SQLException e){
			System.err.println("ERROR while getting Departments From Employee Table");
			e.printStackTrace();
			return null;	
		}	
	}
	
	
	public Department getDepartmentByID(int departmentID){
		Department dep = null;

		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM departments WHERE DepartmentID = ?");
			prepStmt.setInt(1, departmentID);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				departmentID = rs.getInt("DepartmentID");
				String departmentName = rs.getString("DepartmentName");
				String phone = rs.getString("Phone");
				String address = rs.getString("Address");
				String city = rs.getString("City");
				String postalCode = rs.getString("PostalCode");
				String country = rs.getString("Country");
				String email = rs.getString("Email");
				
				dep = new Department(departmentID, departmentName, phone, address, city, postalCode, country, email);
			}
			return dep;
		}
		catch(SQLException e){
			System.err.println("ERROR while getting Departments From Employee Table");
			e.printStackTrace();
			return null;	
		}	
	}
	
	/**
	 * Gets an observableList of Strings which represent Department Names. The function is used to insert strings into ComboBoxes. If a new department is added into database, the comboboxes won't need to be customized manually
	 * @return
	 */
	public ObservableList<String> getDepartmentNames(){
		ObservableList<String> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT DepartmentName FROm departments");
			ResultSet rs = prepStmt.executeQuery();

			while(rs.next()){
				String departmentName = rs.getString("DepartmentName");
				tempList.add(departmentName);
			}
			return tempList;
			
			} catch(SQLException e){
				e.printStackTrace();
				
			}
		return null;
	}
	

	
	/*
	 * MESSAGES TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	

	/**
	 * Get the messages to be displayed in UserInteractionOverview
	 * @param userList
	 * @return
	 */
	public ObservableList<Message> getMessages(){
		try{
			ObservableList<Message> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM messages ORDER BY Date ASC");
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				int messageID = rs.getInt("MessageID");
				int userID = rs.getInt("UserID");
				String title = rs.getString("Title");
				String content = rs.getString("Content");
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				
				//Convert date string from database into LocalDate class instance
				String dateString = rs.getString("Date");
				LocalDate date = LocalDate.parse(dateString, formatter);
				
				User user = getUserByID(userID);
				if(user != null){
					tempList.add(new Message(messageID, user, title, content, date));
					
				} else {
					System.err.println("ERROR ERROR I JESZCZE RAZ ERROR");
				}
				
			}
			
			
			return tempList;
			
		} catch(SQLException e){
			System.err.println("ERROR while getting messages");
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * Inserts new record into messages table 
	 * @param message
	 * @return
	 */
	
	public boolean insertMessageRecord(Message message){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO messages (MessageID, UserID, Title, Content,Date) VALUES (?,?,?,?,?)");
			prepStmt.setInt(1,message.getMessageID());
			prepStmt.setInt(2, message.getUser().getUserID());
			prepStmt.setString(3, message.getTitle());
			prepStmt.setString(4, message.getContent());
			prepStmt.setString(5, message.getDate().toString());
			
			prepStmt.execute();
			return true;
		}
		catch (SQLException e){
			System.err.println("Error WHILE INSERTING NEW RECORD INTO MESSAGE TABLE");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Updates an already existing record in message table
	 * @param message
	 * @return
	 */
	public boolean updateMessageRecord(Message message){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE messages SET Title = ?, Content = ?, Date = ? WHERE MessageID = ? ");
			prepStmt.setString(1, message.getTitle());
			prepStmt.setString(2, message.getContent());
			prepStmt.setString(3, message.getDate().toString());
			prepStmt.setInt(4, message.getMessageID());
			prepStmt.execute();
			return true;
		}
		catch(SQLException e){
			System.err.println("ERROR WHILE UPDATING A RECORD IN MESSAGE TABLE");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Get overall message count in the database 
	 * @return
	 */
	public int getCountMessages(){
		try{
			int count = -1;
			PreparedStatement prepStmt = con.prepareStatement("SELECT COUNT(*) AS count FROM messages");
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				count = rs.getInt("count");
			}
			return count;
		}
		catch(SQLException e){
			System.err.println("ERROR WHILE UPDATING A RECORD IN MESSAGE TABLE");
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Get X messages specified by given parameters
	 * @param userList
	 * @param offsetMultiplier
	 * @return
	 */
	public ObservableList<Message> getSomeMessages(int offsetMultiplier, int howManyMsg){
		try{
			ObservableList<Message> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM messages ORDER BY MessageID DESC LIMIT (?) OFFSET (? * ? ) ");
			prepStmt.setInt(1, howManyMsg);
			prepStmt.setInt(2, howManyMsg);
			prepStmt.setInt(3, offsetMultiplier - 1);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				int messageID = rs.getInt("MessageID");
				int userID = rs.getInt("UserID");
				String title = rs.getString("Title");
				String content = rs.getString("Content");
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				
				//Convert date string from database into LocalDate class instance
				String dateString = rs.getString("Date");
				LocalDate date = LocalDate.parse(dateString, formatter);
				
				User user = getUserByID(userID);
				if(user != null){
					tempList.add(new Message(messageID, user, title, content, date));
					
				} else {
					System.err.println("ERROR ERROR I JESZCZE RAZ ERROR");
				}
				
			}
			
			
			return tempList;
			
		} catch(SQLException e){
			System.err.println("ERROR while getting messages");
			e.printStackTrace();
			return null;
		}
	}
	
	public void deleteMessages(int userID){
		try{
			PreparedStatement prepStmt = con.prepareStatement("DELETE FROM messages WHERE UserID = ?");
			prepStmt.setInt(1, userID);
			prepStmt.execute();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public boolean deleteMessage(int messageID){
		try{
			PreparedStatement prepStmt = con.prepareStatement("DELETE FROM messages WHERE MessageID = ?");
			prepStmt.setInt(1, messageID);
			prepStmt.execute();
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*
	 * CUSTOMER TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/**
	 * Gets an integer value representing overall count of customers existing in database
	 * @return
	 */
	public int getCustomersCount(){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT Count(*) as Value FROM customers");
			ResultSet rs = prepStmt.executeQuery();
			int value = -1;
			while(rs.next()){
				value = rs.getInt("Value");	
			}
			return value;
			
			} catch(SQLException e){
				e.printStackTrace();
				return -1;
			}
	}
	
	/**
	 * Gets a customer instance with ID equal to the passed parameter
	 * @param customerID
	 * @return
	 */
	public Customer getCustomerByID(String customerID){
		Customer customer = null;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM customers WHERE CustomerID = ?");
			prepStmt.setString(1, customerID);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				String companyName = rs.getString("CompanyName");
				String representative = rs.getString("Representative");
				String address = rs.getString("Address");
				String city = rs.getString("City");
				String country = rs.getString("Country");
				String postalCode = rs.getString("PostalCode");
				String phone = rs.getString("Phone");
				String email = rs.getString("Email");
				customer = new Customer(customerID, companyName, representative, address, city, country, postalCode, phone, email);
			}
			return customer;
			
			} catch(SQLException e){
				e.printStackTrace();
				return null;
			}		
	}
	
	public boolean insertCustomerRecord(Customer customer){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO customers VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ");
			prepStmt.setString(1, customer.getCustomerID());
			prepStmt.setString(2, customer.getCompanyName());
			prepStmt.setString(3, customer.getRepresentative());
			prepStmt.setString(4, customer.getAddress());
			prepStmt.setString(5, customer.getCity());
			prepStmt.setString(6, customer.getCountry());
			prepStmt.setString(7, customer.getPostalCode());
			prepStmt.setString(8, customer.getPhone());
			prepStmt.setString(9, customer.getEmail());
			
			prepStmt.execute();
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*
	 * ORDER TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	/**
	 * Get all the orders existing in database
	 * @return ObservableList<Order>
	 */
	
	public ObservableList<Order> getOrders(){
		ObservableList<Order> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM orders");
			ResultSet rs = prepStmt.executeQuery();
			tempList = getOrderDataFromResultSet(rs);
			return tempList;
			
			} catch(SQLException e){
				e.printStackTrace();
				return null;
			}
	}
	
	
	/**
	 * Gets every order with status equal to the parameter
	 * Possible status strings:
	 * Inserted, Accepted, Rejected, Pick_Suggestions_Issued, In_Progress, Carried_Out, 
	 * Being_Checked, Shipment_Ready, Awaits_Shipment, Shipped
	 */
	public ObservableList<Order> getOrdersByOrderStatus(String orderStatus){
		ObservableList<Order> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM orders WHERE OrderStatus = ?");
			prepStmt.setString(1, orderStatus);
			ResultSet rs = prepStmt.executeQuery();
			tempList = getOrderDataFromResultSet(rs);
			return tempList;
			
			} catch(SQLException e){
				e.printStackTrace();
				return null;
			}
	}
	
	/**
	 * Updates certain order record and sets its OrderStatus field to value from OrderStatus parameter
	 * @param orderID
	 * @param orderStatus
	 * @return
	 */
	public boolean changeOrderStatus(int orderID, String orderStatus){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE orders SET OrderStatus = ? WHERE OrderID = ?");
			prepStmt.setString(1, orderStatus);
			prepStmt.setInt(2, orderID);
			prepStmt.execute();
			return true;
		
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public Order getOrderByID(int orderID){
		Order order = null;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM orders WHERE OrderID = ?");
			prepStmt.setInt(1, orderID);
			ResultSet rs = prepStmt.executeQuery();
			
			ObservableList<Order> result = getOrderDataFromResultSet(rs);
			if(!result.isEmpty()) order = result.get(0);
			return order;
			
			} catch(SQLException e){
				e.printStackTrace();
				return null;
			}
	}

	
	/**
	 * Gets an integer value that is not yet used in Order table
	 * @return
	 */
	public int getNextOrderID(){
		int orderID = -1;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT MAX(OrderID) as NextID FROM orders");
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				orderID = rs.getInt("NextID");
				orderID = orderID + 1;
			}
			if(orderID == -1 || orderID == 0) return 1; //means that there is no record in order table
			else return orderID;
		} 
		catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
		
	}
	
	/**
	 * Inserts certain order record into order table
	 * @param o
	 * @return
	 */
	public boolean insertOrderRecord(Order o){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO orders (OrderID, CustomerID, EmployeeID, OrderDate, RequiredDate, Freight, ShippedToName, ShippedToAddress, ShippedToCity, ShippedToPostalCode, ShippedToCountry, OrderStatus) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			prepStmt.setInt(1, o.getOrderID());
			prepStmt.setString(2, o.getCustomer().getCustomerID());
			prepStmt.setInt(3, o.getInsertEmployee().getEmployeeID());
			prepStmt.setString(4, o.getOrderDate().toString());
			prepStmt.setString(5, o.getRequiredDate().toString());
			prepStmt.setDouble(6, o.getFreight());
			prepStmt.setString(7, o.getShipName());
			prepStmt.setString(8, o.getAddress());
			prepStmt.setString(9, o.getCity());
			prepStmt.setString(10, o.getPostalCode());
			prepStmt.setString(11, o.getCountry());
			prepStmt.setString(12, o.getOrderStatus().toString());
			prepStmt.execute();
			
			for(OrderDetails od: o.getOrderDetails()){
				insertOrderDetailsRecord(od, o.getOrderID());
			}
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public ObservableList<Order> getOrderWithCustomerParameters(String parameter, String value){
		ObservableList<Order> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM orders o INNER JOIN customers c ON c.CustomerID = o.CustomerID WHERE c." + parameter + " = ?");
			prepStmt.setString(1, value);
			ResultSet rs = prepStmt.executeQuery();
			tempList = getOrderDataFromResultSet(rs);
			return tempList;
			
			} catch(SQLException e){
				e.printStackTrace();
				return null;
			}
	}
	
	public ObservableList<Order> getOrderWithOrderStatus(String orderStatus){
		ObservableList<Order> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM orders WHERE OrderStatus = ?");
			prepStmt.setString(1, orderStatus);
			ResultSet rs = prepStmt.executeQuery();
			
			tempList = getOrderDataFromResultSet(rs);
			
			return tempList;
			
			} catch(SQLException e){
				e.printStackTrace();
				return null;
			}
	}
	
	public ObservableList<Order> getOrderWithDate(String dateFieldName, String dateValue ){
		ObservableList<Order> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM orders WHERE " + dateFieldName + " = ?");
			//prepStmt.setString(1, dateFieldName);
			prepStmt.setString(1, dateValue);
			
			ResultSet rs = prepStmt.executeQuery();
			tempList = getOrderDataFromResultSet(rs);
			
			return tempList;
			
			} catch(SQLException e){
				e.printStackTrace();
				return null;
			}		
	}
	
	private ObservableList<Order> getOrderDataFromResultSet(ResultSet rs) throws SQLException{
		ObservableList<Order> tempList = FXCollections.observableArrayList();
		if(rs != null){
			while(rs.next()){
				int orderID = rs.getInt("OrderID");
				String customerID = rs.getString("CustomerID");
				
				int insertEmployeeID = rs.getInt("EmployeeID");
				Employee employee = getEmployeeByID(insertEmployeeID);
				Customer customer = getCustomerByID(customerID);
				
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String orderString = rs.getString("OrderDate");
				LocalDate orderDate = LocalDate.parse(orderString, formatter);
				
				String requiredString = rs.getString("RequiredDate");
				LocalDate requiredDate = LocalDate.parse(requiredString, formatter);
				
				Shipper shipper = null;
				double freight = rs.getDouble("Freight");
				String shipName = rs.getString("ShippedToName");
				String shipAddress = rs.getString("ShippedToAddress");
				String shipCity = rs.getString("ShippedToCity");
				String shipPostalCode = rs.getString("ShippedToPostalCode");
				String shipCountry = rs.getString("ShippedToCountry");
				String orderStatus = rs.getString("OrderStatus");
				ObservableList<OrderDetails> orderDetails = getOrderDetails(orderID);
				Order order = new Order(orderID, customer, employee, orderDate, requiredDate, LocalDate.now(), shipper, freight, shipName, shipAddress, shipCity, shipPostalCode, shipCountry, orderDetails, orderStatus);
				tempList.add(order);
			}
		}
		return tempList;
	}
	
	public boolean setPickEmployee(Employee employee, Order order){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE orders SET PickEmployeeID = ? WHERE OrderID = ?");
			prepStmt.setInt(1, employee.getEmployeeID());
			prepStmt.setInt(2, order.getOrderID());
			prepStmt.executeUpdate();
			return true;
		} catch (SQLException e){
			e. getMessage();
			return false;
		}
	}
	
	
	/* 
	 * ORDER DETAILS QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public ObservableList<OrderDetails> getOrderDetails(int orderID){
		ObservableList<OrderDetails> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM order_details WHERE OrderID = ?");
			prepStmt.setInt(1, orderID);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				int productID = rs.getInt("ProductID");
				Product product = getProductByID(productID);
				int quantity = rs.getInt("Quantity");
				double unitPrice = rs.getDouble("UnitPrice");
				double discount = rs.getDouble("Discount");
				
				tempList.add(new OrderDetails(product, quantity, unitPrice, discount));
			}
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<OrderDetails> getOrderDetailsWithPickLocation(int orderID){
		ObservableList<OrderDetails> tempList = getOrderDetails(orderID);
		
		for(OrderDetails od: tempList){
			Product p = od.getProduct();
			Location l = getSuggestedPickLocation(p.getProductID());
			p.setSuggestedPickLocation(l);
		}
		return tempList;
	}
	
	public boolean insertOrderDetailsRecord(OrderDetails od, int orderID){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO order_details VALUES (?, ?, ?, ?, ?)");
			prepStmt.setInt(1, orderID);
			prepStmt.setInt(2, od.getProduct().getProductID());
			prepStmt.setDouble(3, od.getUnitPrice());
			prepStmt.setInt(4, od.getQuantity());
			prepStmt.setDouble(5, od.getDiscount());
			prepStmt.execute();
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 * CHART DATA QUERIES	
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	public XYChart.Series<String, Number> getSaleIncomesData(int customerCount){
		try{
			XYChart.Series<String, Number> tempSeries = new Series<String, Number>();

			PreparedStatement prepStmt = con.prepareStatement("SELECT c.CompanyName , SUM(od.UnitPrice * od.Quantity * (1-od.Discount)) AS sum FROM orders o "
					+ "INNER JOIN order_details od ON o.OrderID = od.OrderID INNER JOIN customers c ON c.CustomerID = o.CustomerID GROUP BY c.CompanyName ORDER BY sum DESC LIMIT ?");
			prepStmt.setInt(1, customerCount);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				String companyName = rs.getString("CompanyName");
				double value = rs.getDouble("sum");
				tempSeries.getData().add(new XYChart.Data<String, Number>(companyName, Math.round(value)));
			}
			return tempSeries;
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get the demand data with quantity being the main criterion
	 * @return
	 */
	
	public ObservableList<PieChart.Data> getDemandDataQuantity(int productQuantity){
		try{
		ObservableList<PieChart.Data> tempList = FXCollections.observableArrayList();
		PreparedStatement prepStmt = con.prepareStatement("SELECT p.ProductName, SUM(od.Quantity) as quantity FROM products p INNER JOIN order_details od ON p.ProductID = od.ProductID GROUP BY p.ProductID,p.ProductName ORDER BY quantity DESC LIMIT ? ;");
		prepStmt.setInt(1, productQuantity);
		ResultSet rs = prepStmt.executeQuery();
		
		while(rs.next()){
			String productName = rs.getString("ProductName");
			double value = rs.getDouble("quantity");
			tempList.add(new PieChart.Data(productName, Math.round(value)));
		}
		
		//get the summary value of other products which didn't make it to the TOP 10
		prepStmt = con.prepareStatement("SELECT  SUM(od.Quantity) as others FROM products p INNER JOIN order_details od ON p.ProductID = od.ProductID WHERE od.ProductID NOT IN (SELECT p.ProductID FROM products p "
				+ "INNER JOIN order_details od ON p.ProductID = od.ProductID GROUP BY p.ProductID,p.ProductName ORDER BY SUM(od.Quantity) DESC LIMIT ?)");
		prepStmt.setInt(1, productQuantity);
		rs = prepStmt.executeQuery();
		
		while(rs.next()){
			String productName = "Inne";
			double value = rs.getDouble("others");
			tempList.add(new PieChart.Data(productName, Math.round(value)));
		}
		
		
		return tempList;
		
		}
		catch(SQLException e){
			System.err.print("ERROR while getting DemandData");
			e.printStackTrace();
			return null;
		}
	}
	 
	/**
	 * Get the demand data with sale incomes being the main criterion
	 * @return
	 */
	public ObservableList<PieChart.Data> getDemandDataSaleIncomes(int productQuantity){
		try{
			ObservableList<PieChart.Data> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = con.prepareStatement("SELECT p.ProductName, SUM(od.Quantity * od.UnitPrice * (1-od.Discount)) as [Sale incomes] FROM products p INNER JOIN order_details od ON p.ProductID = od.ProductID GROUP BY p.ProductID,p.ProductName ORDER BY [Sale incomes] DESC LIMIT ?  ;");
			prepStmt.setInt(1, productQuantity);
			ResultSet rs = prepStmt.executeQuery();
		
			while(rs.next()){
				String productName = rs.getString("ProductName");
				double value = rs.getDouble("Sale incomes");
				tempList.add(new PieChart.Data(productName, Math.round(value)));
			}
		
			//get the summary value of other products which didn't make it to the TOP 10
			prepStmt = con.prepareStatement("SELECT  SUM(od.Quantity * od.UnitPrice * (1-od.Discount)) as others FROM products p INNER JOIN order_details od ON p.ProductID = od.ProductID WHERE od.ProductID NOT IN (SELECT p.ProductID FROM products p  "
				+ "INNER JOIN order_details od ON p.ProductID = od.ProductID GROUP BY p.ProductID,p.ProductName ORDER BY SUM(od.Quantity * od.UnitPrice * (1-od.Discount)) DESC LIMIT ?);");
			prepStmt.setInt(1, productQuantity);
			rs = prepStmt.executeQuery();
		
			while(rs.next()){
				String productName = "Inne";
				double value = rs.getDouble("others");
				tempList.add(new PieChart.Data(productName, Math.round(value)));
			}
		
		
			return tempList;
		
			}
			catch(SQLException e){
				System.err.print("ERROR while getting DemandData Sale Incomes");
				e.printStackTrace();
				return null;
			}		
	}
	
	@SuppressWarnings("unchecked")
	public ObservableList<XYChart.Series<String, Number>> getDemandDataCommon(int productQuantity){
		try{
			ObservableList<XYChart.Series<String, Number>> tempList = FXCollections.observableArrayList();
			XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
			XYChart.Series<String, Number> series2 = new XYChart.Series<String, Number>();
		
			PreparedStatement prepStmt = con.prepareStatement("SELECT p.ProductName, (SUM(od.Quantity * od.UnitPrice * (1-od.Discount))/(SELECT SUM(Quantity * UnitPrice * (1 - Discount)) FROM order_details)) * 100 as sale  , SUM(od.Quantity) * 100 / (SELECT SUM(Quantity) FROM order_details) as quan  FROM products p INNER JOIN order_details od ON p.ProductID = od.ProductID WHERE p.ProductID IN (SELECT p.ProductID FROM products p  INNER JOIN order_details od "
				+ "ON p.ProductID = od.ProductID GROUP BY p.ProductID,p.Product"
				+ "Name ORDER BY SUM(od.Quantity * od.UnitPrice * (1-od.Discount)) DESC LIMIT ?) "
				+ "GROUP BY p.ProductID, p.ProductName ORDER BY quan DESC ;");
			prepStmt.setInt(1, productQuantity);
			ResultSet rs = prepStmt.executeQuery();
		
			while(rs.next()){
				String productName = rs.getString("ProductName");
				double value = rs.getDouble("quan");
				double value2 = rs.getDouble("sale");
				series1.getData().add(new XYChart.Data<String, Number>(productName, Math.round(value)));
				series2.getData().add(new XYChart.Data<String, Number>(productName, Math.round(value2)));
			}
		
			tempList.addAll(series1, series2);
			return tempList;
		
		}
		catch(SQLException e){
			System.err.print("ERROR while getting common DemandData");
			e.printStackTrace();
			return null;
		}	
	}
	
	public XYChart.Series<String, Number> getIncomesOverTimeData(String timeSpan){
		try{
		XYChart.Series<String, Number> tempSeries = new Series<String, Number>();

		PreparedStatement prepStmt = con.prepareStatement("SELECT o.OrderDate, SUM(od.Quantity * od.UnitPrice * (1 - od.Discount)) as value FROM orders o INNER JOIN order_details od ON od.OrderID = o.OrderID WHERE o.OrderDate <= date('now') AND o.OrderDate > ? GROUP BY o.OrderDate ");
		prepStmt.setString(1, timeSpan);
		ResultSet rs = prepStmt.executeQuery();
		
		while(rs.next()){
			String date = rs.getString("OrderDate");
			double value = rs.getDouble("value");
			tempSeries.getData().add(new XYChart.Data<String, Number>(date, Math.round(value)));
		}
		
		return tempSeries;
		
		}
		catch(SQLException e){
			System.err.print("ERROR while getting IncomeData");
			e.printStackTrace();
			return null;
		}	
	}
	
	public XYChart.Series<String, Number> getDeliveryValuesOverTime(String timeSpan){
		try{
			XYChart.Series<String, Number> tempSeries = new Series<String, Number>();
			PreparedStatement prepStmt = con.prepareStatement("SELECT d.ArrivalDate, SUM(p.UnitPrice * dd.Quantity) as value FROM delivery_details dd INNER JOIN products p ON p.ProductID = dd.ProductID INNER JOIN deliveries d ON d.DeliveryID = dd.DeliveryID WHERE d.DeliveryStatus = 'Arrived' OR d.DeliveryStatus = 'Taken_By_Warehouse' AND d.ArrivalDate <= date('now') AND d.ArrivalDate > ? GROUP BY d.ArrivalDate ORDER BY d.ArrivalDate");
			prepStmt.setString(1, timeSpan);
			ResultSet rs = prepStmt.executeQuery();
			
			while(rs.next()){
				String date = rs.getString("ArrivalDate");
				double value = rs.getDouble("value");
				tempSeries.getData().add(new XYChart.Data<String, Number>(date, Math.round(value)));
			}
			
			return tempSeries;
		} catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<PieChart.Data> getCategoryIncomesData(){
		try{
			ObservableList<PieChart.Data> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = con.prepareStatement("SELECT c.CategoryName, SUM(od.UnitPrice * od.Quantity * (1 - od.Discount)) AS Value FROM categories c INNER JOIN products p ON c.CategoryID = p.CategoryID INNER JOIN order_details od ON od.ProductID = p.ProductID GROUP BY c.CategoryName ORDER BY Value");
			ResultSet rs = prepStmt.executeQuery();
		
			while(rs.next()){
				String categoryName = rs.getString("CategoryName");
				double value = rs.getDouble("Value");
				tempList.add(new PieChart.Data(categoryName, Math.round(value)));
			}

			return tempList;
			}
			catch(SQLException e){
				System.err.print("ERROR while getting Category Incomes Data");
				e.printStackTrace();
				return null;
			}	
	}
	
	public XYChart.Series<Integer, Number> getLinearSmoothingData(){
		try{
			XYChart.Series<Integer, Number> tempSeries = new Series<Integer, Number>();
			PreparedStatement prepStmt = con.prepareStatement("SELECT strftime('%W',OrderDate) AS Week, SUM(od.Quantity * od.UnitPrice * (1-od.Discount)) AS value FROM orders o INNER JOIN order_details od ON od.OrderID = o.OrderID GROUP BY Week");
			ResultSet rs = prepStmt.executeQuery();
			
			while(rs.next()){
				Integer week = rs.getInt("Week");
				double value = rs.getDouble("value");
				tempSeries.getData().add(new XYChart.Data<Integer, Number>(week, Math.round(value)));
			}
			
			return tempSeries;
		} catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	/*
	 * PRODUCT TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	
	public Product getProductByName(String productName){
		try{
			Product product = null;
			PreparedStatement prepStmt = null;
			prepStmt = con.prepareStatement("SELECT * FROM products WHERE ProductName = ?");
			prepStmt.setString(1, productName);

			ResultSet rs = prepStmt.executeQuery();
			ObservableList<Product> result = getProductDataFromResultSet(rs, true);
			if(!result.isEmpty()) product = result.get(0);
			return product;
		}
		catch(SQLException e){
			System.err.print("ERROR while searching for a product");
			e.printStackTrace();
			return null;
		}	
	}

	public Product getProductByID(int productID){
		try{
			Product product = null;
			PreparedStatement prepStmt = null;
			prepStmt = con.prepareStatement("SELECT * FROM products WHERE ProductID = ?");
			prepStmt.setInt(1, productID);
			
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<Product> result = getProductDataFromResultSet(rs, true);
			if(result != null && !result.isEmpty()) product = result.get(0);
			
			return product;

		}
		catch(SQLException e){
			System.err.print("ERROR while searching for a product");
			e.printStackTrace();
			return null;
		}	
	}
	
	
	public ObservableList<Product> getCategorizedProducts(String categoryName){
		try{
			ObservableList<Product> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM products WHERE CategoryID = (SELECT CategoryID FROM categories WHERE CategoryName = ?)");
			prepStmt.setString(1, categoryName);
			
			ResultSet rs = prepStmt.executeQuery();
			tempList = getProductDataFromResultSet(rs, false);
			return tempList;
			} 
		catch(SQLException e)
		{
			System.err.print("ERROR while gettting categorized products");
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Product> getProductsBelowMinStockValue(){
		try{
			ObservableList<Product> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM products WHERE UnitsOnStock < ReorderLevel;");

			ResultSet rs = prepStmt.executeQuery();
			tempList =  getProductDataFromResultSet(rs, false);
			return tempList;
			} 
		catch(SQLException e)
		{
			System.err.print("ERROR WHILE GETTING PRODUCTS BELOW MINIMAL VALUE");
			e.printStackTrace();
			return null;
		}
	}
	
	public int getNextProductID(){
		try{
			int nextID = -1;
			PreparedStatement prepStmt = con.prepareStatement("SELECT MAX(ProductID) AS NextID FROM products");

			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				nextID = rs.getInt("NextID");
			}
			if(nextID == -1) {
				//if function has not even entered the while loop which means that there are no records in product table
				return 1;
			} else {
				return ++nextID;
			}
		} 
		catch(SQLException e)
		{
			System.err.print("ERROR WHILE GETTING PRODUCTS BELOW MINIMAL VALUE");
			e.printStackTrace();
			return -1;
		}
	}
	
	public boolean updateUnitsOnStockValue(Product product, int quantityToAdd){
		int unitsOnStock = 0;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT UnitsOnStock FROM products WHERE ProductID = ? ");
			prepStmt.setInt(1, product.getProductID());
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				unitsOnStock = rs.getInt("UnitsOnStock");
			}
			
			prepStmt = con.prepareStatement("UPDATE products SET UnitsOnStock = ? WHERE ProductID = ?");
			int valueToBeSet = unitsOnStock + quantityToAdd;
			prepStmt.setInt(1, valueToBeSet );
			prepStmt.setInt(2, product.getProductID());
			System.out.println(unitsOnStock + quantityToAdd);
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Gets an array of products from passed ResultSet object and returns it as observableList
	 * @param rs
	 * @param inputSingleRecord - determines if the process of getting product data should stop after first product has been added. 'False' value means that the function should return an array of all the products meeting certain condition
	 * @return
	 * @throws SQLException
	 */
	public ObservableList<Product> getProductDataFromResultSet(ResultSet rs, boolean inputSingleRecord) throws SQLException{
		ObservableList<Product> tempList = FXCollections.observableArrayList();
		while(rs.next()){
			int productID = rs.getInt("ProductID");
			String productName = rs.getString("ProductName");
			int categoryID = rs.getInt("CategoryID");
			
			Category category = getCategoryByID(categoryID);
			
			String quantityPerUnit = rs.getString("QuantityPerUnit");
			double unitPrice = rs.getDouble("UnitPrice");
			int unitsOnStock = rs.getInt("UnitsOnStock");
			int reorderLevel = rs.getInt("ReorderLevel");
			boolean discontinued = Boolean.parseBoolean(rs.getString("Discontinued"));
			
			tempList.add(new Product(productID, productName, category, quantityPerUnit, unitPrice, unitsOnStock, reorderLevel, discontinued));
			
			if(inputSingleRecord){
				return tempList;
			}
		}
		return tempList;
	} 
	
	public boolean checkIfProductExists(int productID){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM products WHERE ProductID = ? ");
			prepStmt.setInt(1, productID);
			ResultSet rs = prepStmt.executeQuery();
			
			boolean hasResults = rs.next();
			
			return hasResults;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean insertProductRecord(Product product){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO products VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			prepStmt.setInt(1, product.getProductID());
			prepStmt.setString(2, product.getProductName());
			prepStmt.setInt(3, product.getCategory().getCategoryID());
			prepStmt.setString(4, product.getQuantityPerUnit());
			prepStmt.setDouble(5, product.getUnitPrice());
			prepStmt.setInt(6, product.getUnitsInStock());
			prepStmt.setInt(7, product.getReorderLevel());
			prepStmt.setBoolean(8, product.getDiscontinued());
			
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateProductRecord(Product product){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE products SET ProductName = ?, CategoryID = ?, QuantityPerUnit = ?, UnitPrice = ?, UnitsOnStock = ?, ReorderLevel = ?, Discontinued = ? WHERE ProductID = ?");
			prepStmt.setString(1, product.getProductName());
			prepStmt.setInt(2, product.getCategory().getCategoryID());
			prepStmt.setString(3, product.getQuantityPerUnit());
			prepStmt.setDouble(4, product.getUnitPrice());
			prepStmt.setInt(5, product.getUnitsInStock());
			prepStmt.setInt(6, product.getReorderLevel());
			prepStmt.setBoolean(7, product.getDiscontinued());
			prepStmt.setInt(8, product.getProductID());
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*
	 * LOCATION QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public ObservableList<Location> getProductLocationsByName(String productName){
		try{
			ObservableList<Location> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = null;
			prepStmt = con.prepareStatement("SELECT p.ProductID, p.ProductName, ld.Quantity, l.locationID, l.AcceptableLoad, l.AcceptableVolume FROM products p INNER JOIN location_details ld ON ld.ProductID = p.ProductID INNER JOIN locations l ON l.LocationID = ld.LocationID WHERE p.ProductName = ?");
			prepStmt.setString(1, productName);

			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				String locationID = rs.getString("LocationID");
				int acceptableLoad = rs.getInt("AcceptableLoad");
				double acceptableVolume = rs.getDouble("AcceptableVolume");
				
				int productID = rs.getInt("ProductID");
				String prodName = rs.getString("ProductName");
				int quantity = rs.getInt("Quantity");
				ObservableList<Product> productsStored = FXCollections.observableArrayList();
				productsStored.add(new Product(productID, prodName, quantity));
				
				tempList.add(new Location(locationID, acceptableLoad, (int) acceptableVolume, productsStored));
			}
			return tempList;
		}
		catch(SQLException e){
			System.err.print("ERROR while getting product Locations");
			e.printStackTrace();
			return null;
		}	
	}
	
	public ObservableList<Location> getProductLocationsByID(int productID){
		try{
			ObservableList<Location> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = null;
			prepStmt = con.prepareStatement("SELECT p.ProductID, p.ProductName, ld.Quantity, l.locationID, l.AcceptableLoad, l.AcceptableVolume FROM products p INNER JOIN location_details ld ON ld.ProductID = p.ProductID INNER JOIN locations l ON l.LocationID = ld.LocationID WHERE p.ProductID = ?");
			prepStmt.setInt(1, productID);
			
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				String locationID = rs.getString("LocationID");
				int acceptableLoad = rs.getInt("AcceptableLoad");
				double acceptableVolume = rs.getDouble("AcceptableVolume");
				

				ObservableList<Product> productsStored = FXCollections.observableArrayList();
				prepStmt = con.prepareStatement("SELECT p.ProductID, p.ProductName, ld.Quantity , ld.LocationID FROM location_details ld INNER JOIN products p ON p.ProductID = ld.ProductID WHERE ld.LocationID = ?");
				prepStmt.setString(1, locationID);
				ResultSet productRS = prepStmt.executeQuery();
				
				while(productRS.next()){
					int prodID = productRS.getInt("ProductID");
					String prodName = productRS.getString("ProductName");
					int quantity = productRS.getInt("Quantity");
					productsStored.add(new Product(prodID, prodName, quantity));
				}
				
				tempList.add(new Location(locationID, acceptableLoad, (int) acceptableVolume, productsStored));
			}
			return tempList;
		}
		catch(SQLException e){
			System.err.print("ERROR while getting product Locations");
			e.printStackTrace();
			return null;
		}	
	}
	
	private Location getSuggestedPickLocation(int productID){
		Location location = null;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM location_details ld INNER JOIN products p ON p.ProductID = ld.ProductID INNER JOIN locations l ON l.LocationID = ld.LocationID WHERE p.ProductID = ? "
					+ "AND ld.Quantity = (SELECT MIN(Quantity) FROM location_details WHERE ProductID = ?)" );
			prepStmt.setInt(1, productID);
			prepStmt.setInt(2, productID);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				String locationID = rs.getString("LocationID");
				int acceptableLoad = rs.getInt("AcceptableLoad");
				int acceptableVolume = rs.getInt("AcceptableVolume");
				location = new Location(locationID, acceptableLoad, acceptableVolume, null);
			}
			
			if(location == null){
				location = new Location("Unavailable", -1, -1, null);
			}
			return location;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean checkIfLocationExists(String locationID){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM locations WHERE LocationID = ? ");
			prepStmt.setString(1, locationID);
			ResultSet rs = prepStmt.executeQuery();
			
			boolean hasResults = rs.next();
			
			return hasResults;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public int checkIfLocationHasProduct(String locationID, int productID){
		int quantity = - 1;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM location_details ld INNER JOIN products p ON p.ProductID = ld.ProductID INNER JOIN locations l ON l.LocationID = ld.LocationID WHERE p.ProductID = ? "
					+ "AND ld.LocationID = ?" );
			prepStmt.setInt(1, productID);
			prepStmt.setString(2, locationID);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				quantity = rs.getInt("Quantity");
			}
			
			return quantity;
		} catch (SQLException e){
			e.printStackTrace();
			return -232;
		}
	}
	
	public boolean insertProductIntoLocation(String locationID, int productID, int quantity){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO location_details VALUES (?, ?, ?)" );
			prepStmt.setString(1, locationID);
			prepStmt.setInt(2, productID);
			prepStmt.setInt(3, quantity);
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateLocationDetailsRecord(String locationID, int productID, int quantity){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE location_details SET Quantity = ? WHERE LocationID = ? AND ProductID = ?" );
			prepStmt.setInt(1, quantity);
			prepStmt.setString(2, locationID);
			prepStmt.setInt(3, productID);
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean getTheProductsFromLocation(String locationID, int productID, int quantity){
		try{
			con.setAutoCommit(false);
			PreparedStatement prepStmt = con.prepareStatement("SELECT Quantity FROM location_details WHERE LocationID = ? AND ProductID = ?");
			prepStmt.setString(1, locationID);
			prepStmt.setInt(2, productID);
			ResultSet rs = prepStmt.executeQuery();
			int quantityStored = 0;
			while(rs.next()){
				quantityStored = rs.getInt("Quantity");
			}
			if(quantityStored - quantity == 0) {
				prepStmt = con.prepareStatement("DELETE FROM location_details WHERE LocationID = ? AND ProductID = ?" );
				prepStmt.setString(1, locationID);
				prepStmt.setInt(2, productID);
			} else {
				prepStmt = con.prepareStatement("UPDATE location_details SET Quantity = ? WHERE LocationID = ? AND ProductID = ?");
				prepStmt.setInt(1, quantityStored - quantity);
				prepStmt.setString(2, locationID);
				prepStmt.setInt(3, productID);
			}
			prepStmt.executeUpdate();
			
			prepStmt = con.prepareStatement("SELECT UnitsOnStock FROM products WHERE ProductID = ?");
			prepStmt.setInt(1, productID);
			rs = prepStmt.executeQuery();
			int unitsOnStock = 0;
			while(rs.next()){
				unitsOnStock = rs.getInt("UnitsOnStock");
			}
			
			prepStmt = con.prepareStatement("UPDATE products SET UnitsOnStock = ? WHERE ProductID = ?");
			prepStmt.setInt(1, unitsOnStock - quantity);
			prepStmt.setInt(2, productID);
			prepStmt.executeUpdate();
			
			con.commit();
			con.setAutoCommit(true);
			prepStmt.close();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean insertLocationRecord(Location location){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO locations (LocationID, AcceptableLoad, AcceptableVolume) VALUES (?, ?, ?)");
			prepStmt.setString(1, location.getLocationID());
			prepStmt.setInt(2, location.getAcceptableLoad());
			prepStmt.setInt(3, location.getAcceptableVolume());
			prepStmt.execute();
			
			if(location.getProductsStored() != null) {
				for(Product p : location.getProductsStored()) 
					insertProductIntoLocation(location.getLocationID(), p.getProductID(), p.getUnitsInStock());
			}
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateLocationRecord(Location location){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE locations SET AcceptableLoad = ?, AcceptableVolume = ? WHERE LocationID = ?");
			
			prepStmt.setInt(1, location.getAcceptableLoad());
			prepStmt.setInt(2, location.getAcceptableVolume());
			prepStmt.setString(3, location.getLocationID());
			
			prepStmt.execute();
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public Location getLocationByID(String locationID){
		try{
			
			Location location = null;
			PreparedStatement prepStmt = null;
			prepStmt = con.prepareStatement("SELECT * FROM locations WHERE LocationID  = ?");
			prepStmt.setString(1, locationID);

			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){

				int acceptableLoad = rs.getInt("AcceptableLoad");
			    double acceptableVolume = rs.getDouble("AcceptableVolume");
				
				location = new Location(locationID, acceptableLoad, (int) acceptableVolume, null);
			}
			
			if(location != null){
			
				prepStmt = con.prepareStatement("SELECT p.ProductID, p.ProductName, ld.Quantity FROM products p INNER JOIN location_details ld ON ld.ProductID = p.ProductID WHERE ld.LocationID = ?");
				prepStmt.setString(1, locationID);
				rs = prepStmt.executeQuery();
				ObservableList<Product> productsStored = FXCollections.observableArrayList();
			
				while(rs.next()){
					String prodName = rs.getString("ProductName");
					int prodID = rs.getInt("ProductID");
					int quantity = rs.getInt("Quantity");
					productsStored.add(new Product(prodID, prodName, quantity));
				}
			
				location.setProductsStored(productsStored);
			}
			
			return location;
		}
		catch(SQLException e){
			System.err.print("ERROR while getting location");
			e.printStackTrace();
			return null;
		}	
	}
	
	public ObservableList<Location> getEmptyLocations(){
		try{
			ObservableList<Location> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM locations l LEFT OUTER JOIN location_details ld ON ld.LocationID = l.LocationID WHERE ld.ProductID IS NULL");
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				String locationID = rs.getString("LocationID");
				int acceptableLoad = rs.getInt("AcceptableLoad");
			    double acceptableVolume = rs.getDouble("AcceptableVolume");
				
				tempList.add(new Location(locationID, acceptableLoad, (int) acceptableVolume, null));
			}
			return tempList;
			
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Location> getLocationsWith(int condition, int value){
		
		ObservableList<Location> tempList = FXCollections.observableArrayList();
		
		PreparedStatement prepStmt = null;
		
		try{
		switch(condition){
			case 1: {
				prepStmt = con.prepareStatement("SELECT * FROM locations WHERE LocationID  LIKE '" + value + "-%-%'");
				break;
			}
			case 2: {
				prepStmt = con.prepareStatement("SELECT * FROM locations WHERE LocationID  LIKE '%-" + value + "-%'");
				break;
			}
			case 3: {
				prepStmt = con.prepareStatement("SELECT * FROM locations WHERE LocationID  LIKE '%-%-" + value + "'");
				break;
			}
			default: return null;
		}

		ResultSet rs = prepStmt.executeQuery();
		
		while(rs.next()){
			String locationID = rs.getString("LocationID");
			int acceptableLoad = rs.getInt("AcceptableLoad");
		    double acceptableVolume = rs.getDouble("AcceptableVolume");
			
		    ObservableList<Product> productsStored = getProductsStored(locationID);
			tempList.add(new Location(locationID, acceptableLoad, (int) acceptableVolume, productsStored));
		}

		return tempList;
	}
	catch(SQLException e){
		e.printStackTrace();
		return null;
		}	
	}
	
	private ObservableList<Product> getProductsStored(String locationID){
		try{
		    ObservableList<Product> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM location_details ld INNER JOIN products p ON p.ProductID = ld.ProductID WHERE LocationID = ?");
			prepStmt.setString(1, locationID);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				String prodName = rs.getString("ProductName");
				int prodID = rs.getInt("ProductID");
				int quantity = rs.getInt("Quantity");
				tempList.add(new Product(prodID, prodName, quantity));
			}
			return tempList;
		} catch(SQLException e){
			e.printStackTrace();
			return null;
		}	
	}
		
	/*
	 * PERMISSION TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public Permission getPermissionForUser(int userID){
		try{

			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM permissions WHERE UserID = ?");
			prepStmt.setInt(1, userID);
			
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				
				boolean blockedAccount;
				boolean mayEdit;
				boolean mayInsertData;
				boolean mayExecuteQueries;
				boolean hasAdminRights;


				blockedAccount = Boolean.parseBoolean(rs.getString("BlockedAccount"));
				mayEdit = Boolean.parseBoolean(rs.getString("MayEdit"));
				blockedAccount = Boolean.parseBoolean(rs.getString("BlockedAccount"));
				mayInsertData = Boolean.parseBoolean(rs.getString("MayInsertData"));
				mayExecuteQueries = Boolean.parseBoolean(rs.getString("MayExecuteQueries"));
				hasAdminRights = Boolean.parseBoolean(rs.getString("HasAdminRights"));
				
				return new Permission(blockedAccount, mayEdit, mayInsertData, mayExecuteQueries, hasAdminRights);
				

			}

		}
		catch(SQLException e){
			System.err.print("ERROR while getting permissions");
			e.printStackTrace();
			return null;
		}
		
		return null;	
	}
	
	public boolean updatePermissions(int userID, Permission p){
		try {
			PreparedStatement prepStmt = con.prepareStatement("UPDATE permissions SET BlockedAccount = ?, MayEdit = ?, MayInsertData = ?, MayExecuteQueries = ?, HasAdminRights = ? WHERE UserID = ?");
			prepStmt.setString(1, String.valueOf(p.isBlockedAccount()));
			prepStmt.setString(2, String.valueOf(p.isMayEdit()));
			prepStmt.setString(3, String.valueOf(p.isMayInsertData()));
			prepStmt.setString(4, String.valueOf(p.isMayExecuteQueries()));
			prepStmt.setString(5, String.valueOf(p.isHasAdminRights()));
			prepStmt.setInt(6, userID);
			prepStmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean insertPermissions(int userID, Permission p){
		try {
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO permissions VALUES (?, ?, ?, ?, ?, ?)");
			prepStmt.setInt(1, userID);
			prepStmt.setString(2, String.valueOf(p.isBlockedAccount()));
			prepStmt.setString(3, String.valueOf(p.isMayEdit()));
			prepStmt.setString(4, String.valueOf(p.isMayInsertData()));
			prepStmt.setString(5, String.valueOf(p.isMayExecuteQueries()));
			prepStmt.setString(6, String.valueOf(p.isHasAdminRights()));
			
			prepStmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deletePermissions(int userID){
		try {
			PreparedStatement prepStmt = con.prepareStatement("DELETE FROM permissions WHERE UserID = ?");
			prepStmt.setInt(1, userID);
			prepStmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/*
	 * SUPPLIER QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	
	/**
	 * Get list of all suppliers that have supplied specified product
	 * @param productName
	 * @return
	 */
	public ObservableList<Supplier> getSuppliersByProductName(String productName){
		try{
			ObservableList<Supplier> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = con.prepareStatement("SELECT s.SupplierID, s.CompanyName, s.Representative, s.Address, s.City, s.PostalCode, s.Country, s.PhoneNumber, s.other FROM deliveries d INNER JOIN delivery_details dd ON d.DeliveryID = dd.DeliveryID INNER JOIN suppliers s ON s.SupplierID = d.SupplierID WHERE dd.ProductID = (SELECT ProductID From products WHERE ProductName = ?) GROUP BY s.SupplierID");
			prepStmt.setString(1, productName);
			ResultSet rs = prepStmt.executeQuery();
			tempList = getSupplierDataFromResultSet(rs, false);
			return tempList;

		} 	catch(SQLException e){
			System.err.print("ERROR while getting supplier of a product");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get list of all suppliers that have supplied specified product
	 * @param productID
	 * @return
	 */
	public ObservableList<Supplier> getSuppliersByProductID(int productID){
		try{
			ObservableList<Supplier> tempList = FXCollections.observableArrayList();
			PreparedStatement prepStmt = con.prepareStatement("SELECT s.SupplierID, s.CompanyName, s.Representative, s.Address, s.City, s.PostalCode, s.Country, s.PhoneNumber, s.other FROM deliveries d INNER JOIN delivery_details dd ON d.DeliveryID = dd.DeliveryID INNER JOIN suppliers s ON s.SupplierID = d.SupplierID WHERE dd.ProductID = ? GROUP BY s.SupplierID");
			prepStmt.setInt(1, productID);
			ResultSet rs = prepStmt.executeQuery();
			tempList = getSupplierDataFromResultSet(rs, false);
			
			return tempList;
		} 	catch(SQLException e){
			System.err.print("ERROR while getting supplier of a product");
			e.printStackTrace();
			return null;
		}
	}
	
	
	public int getSuppliersCount(){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT Count(*) as Value FROM suppliers");
			ResultSet rs = prepStmt.executeQuery();
			int value = -1;
			while(rs.next()){
				value = rs.getInt("Value");	
			}
			return value;
			
			} catch(SQLException e){
				e.printStackTrace();
				return -1;
			}
	}
	
	public Supplier getSupplierByID(int supplierID){
		try{
			Supplier supplier = null;
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM suppliers WHERE SupplierID = ?");
			prepStmt.setInt(1, supplierID);
			ResultSet rs = prepStmt.executeQuery();
			ObservableList<Supplier> result = getSupplierDataFromResultSet(rs, true);
			
			
			if(!result.isEmpty()) supplier = result.get(0);
			return supplier;
			
		} catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Supplier> getSupplierDataFromResultSet(ResultSet rs, boolean getSingleRecord) throws SQLException{
		ObservableList<Supplier> tempList = FXCollections.observableArrayList();
		while(rs.next()){
			int supplierID = rs.getInt("SupplierID");
			String companyName = rs.getString("CompanyName");
			String representative = rs.getString("Representative");
			String address = rs.getString("Address");
			String city = rs.getString("City");
			String postalCode = rs.getString("PostalCode");
			String country = rs.getString("Country");
			String phoneNumber = rs.getString("PhoneNumber");
			String other = rs.getString("Other");
			
			tempList.add(new Supplier(supplierID, companyName, representative, address, city,postalCode, country, phoneNumber, other));
			if(getSingleRecord) return tempList;
		}
		
		return tempList;
	}
	
	public int getNextSupplierID(){
		int nextID = 1;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT MAX(SupplierID) + 1 AS NextID FROM suppliers");
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				nextID = rs.getInt("NextID");
			}
			return nextID;
		} catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public boolean insertSupplierRecord(Supplier s){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO suppliers VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			prepStmt.setInt(1, s.getSupplierID());
			prepStmt.setString(2, s.getCompanyName());
			prepStmt.setString(3, s.getRepresentative());
			prepStmt.setString(4, s.getAddress());
			prepStmt.setString(5, s.getCity());
			prepStmt.setString(6, s.getPostalCode());
			prepStmt.setString(7, s.getCountry());
			prepStmt.setString(8, s.getPhoneNumber());
			prepStmt.setString(9, s.getOther());
			prepStmt.execute();
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateSupplierRecord(Supplier s){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE suppliers SET CompanyName = ?, Representative = ?, Address = ?, City = ?, PostalCode = ?, Country = ?, PhoneNumber = ?, Other = ? WHERE SupplierID = ?");
			prepStmt.setString(1, s.getCompanyName());
			prepStmt.setString(2, s.getRepresentative());
			prepStmt.setString(3, s.getAddress());
			prepStmt.setString(4, s.getCity());
			prepStmt.setString(5, s.getPostalCode());
			prepStmt.setString(6, s.getCountry());
			prepStmt.setString(7, s.getPhoneNumber());
			prepStmt.setString(8, s.getOther());
			prepStmt.setInt(9, s.getSupplierID());
			prepStmt.execute();
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	/*
	 * SHIPPER TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	public Shipper getShipperByID(int shipperID){
		Shipper shipper = null;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM shippers WHERE ShipperID = ?");
			prepStmt.setInt(1, shipperID);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				String companyName = rs.getString("CompanyName");
				String phone = rs.getString("Phone");
				String email = rs.getString("Email");
				shipper = new Shipper(shipperID, companyName, phone, email);
			}
			return shipper;
		} 
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Shipper getShipperByName(String shipperName){
		Shipper shipper = null;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM shippers WHERE CompanyName = ?");
			prepStmt.setString(1, shipperName);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				int shipperID = rs.getInt("ShipperID");
				String phone = rs.getString("Phone");
				String email = rs.getString("Email");
				shipper = new Shipper(shipperID, shipperName, phone, email);
			}
			return shipper;
		} 
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public ObservableList<String> getShipperNames(){
		ObservableList<String> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT CompanyName FROM shippers");
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				String companyName = rs.getString("CompanyName");
				tempList.add(companyName);
			}
			return tempList;
		} 
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public int getNextShipperID(){
		int nextID = 1;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT MAX(ShipperID) + 1 as NextID FROM shippers");
			
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				nextID = rs.getInt("NextID");
			}
			return nextID;
		} catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public boolean insertShipperRecord(Shipper shipper){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO shippers VALUES (?, ?, ?, ?)");
			prepStmt.setInt(1, shipper.getShipperID());
			prepStmt.setString(2, shipper.getCompanyName());
			prepStmt.setString(3, shipper.getPhone());
			prepStmt.setString(4, shipper.getEmail());
			prepStmt.execute();
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 * SHIPMENT TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	public int getNextShipmentID(){
		int shipmentID = -1;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT MAX(ShipmentID) as NextID FROM shipments");
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				shipmentID = rs.getInt("NextID");
			}
			if(shipmentID == -1 || shipmentID == 0) return 1; //means that there is no record in shipment table
			else return ++shipmentID;
		} 
		catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
		
	}

	
	public boolean insertShipmentRecord(Shipment shipment){
		try{
			boolean successfullyExecuted = false;
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO shipments VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
			prepStmt.setInt(1, shipment.getShipmentID());
			prepStmt.setInt(2, shipment.getShipper().getShipperID());
			prepStmt.setString(3, shipment.getCustomer().getCustomerID());
			prepStmt.setInt(4, shipment.getPalletsCount());
			prepStmt.setString(5, shipment.getAddress());
			prepStmt.setString(6, shipment.getShippedName());
			prepStmt.setString(7, shipment.getCity());
			prepStmt.setString(8, shipment.getPostalCode());
			prepStmt.setString(9, shipment.getCountry());
			prepStmt.setString(10, shipment.getShippedDate().toString());
			prepStmt.setString(11, shipment.getShipmentStatus().toString());
			prepStmt.execute();
			
			successfullyExecuted = true;
			ObservableList<ShipmentDetails> shipmentDetails = shipment.getShipmentDetails();
			for(ShipmentDetails sd: shipmentDetails){
				successfullyExecuted = insertShipmentDetailsRecord(sd);
			}
			return successfullyExecuted;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	

	public Shipment getShipmentByID(int shipmentID){
		Shipment shipment = null;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM shipments WHERE ShipmentID = ?");
			prepStmt.setInt(1, shipmentID);
			ResultSet rs = prepStmt.executeQuery();

			ObservableList<Shipment> shipmentData = getShipmentDataFromResultSet(rs);
			if(!shipmentData.isEmpty()){
				shipment = shipmentData.get(0);
			}

			
			return shipment;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Shipment> getShipmentsWithStatus(String shipmentStatus){
		ObservableList<Shipment> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM shipments WHERE ShipmentStatus = ?");
			prepStmt.setString(1, shipmentStatus);
			ResultSet rs = prepStmt.executeQuery();

			tempList = getShipmentDataFromResultSet(rs);
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Shipment> getAllShipmentsOfCustomer(String parameterName, String parameterValue){
		ObservableList<Shipment> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM shipments s INNER JOIN customers c ON c.CustomerID = s.CustomerID WHERE c." + parameterName + " = ?");
			prepStmt.setString(1, parameterValue);
			ResultSet rs = prepStmt.executeQuery();

			tempList = getShipmentDataFromResultSet(rs);

			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	private ObservableList<Shipment> getShipmentDataFromResultSet(ResultSet rs) throws SQLException{
		ObservableList<Shipment> tempList = FXCollections.observableArrayList();
		while(rs.next()){
			int shipmentID = rs.getInt("ShipmentID");
			
			int shipperID = rs.getInt("ShipperID");
			Shipper shipper = getShipperByID(shipperID);
			
			String customerID = rs.getString("CustomerID");
			Customer customer = getCustomerByID(customerID);
			
			int palletsCount = rs.getInt("PalletsCount");
			String shippedAddress = rs.getString("ShippedAddress");
			String shippedName = rs.getString("ShippedName");
			String shippedCity = rs.getString("ShippedCity");
			String shippedPostalCode = rs.getString("ShippedPostalCode");
			String shippedCountry = rs.getString("ShippedCountry");
			String shippedDateString = rs.getString("ShippedDate");
			String shipmentStatus = rs.getString("ShipmentStatus");
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate shippedDate = LocalDate.parse(shippedDateString, formatter);
			
			ObservableList<ShipmentDetails> shipmentDetails = getShipmentDetails(shipmentID);
			tempList.add(new Shipment(shipmentID, shipper, customer, palletsCount, shippedAddress, shippedName, shippedCity, shippedPostalCode, shippedCountry, shippedDate, shipmentDetails, shipmentStatus));
			
		}
		return tempList;
	}
	
	public ObservableList<Shipment> getShipmentByShippedDate(String shippedDate){
		ObservableList<Shipment> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM shipments WHERE ShippedDate = ?");
			prepStmt.setString(1, shippedDate);
			ResultSet rs = prepStmt.executeQuery();
			tempList = getShipmentDataFromResultSet(rs);
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}	
	
	
	public int getShipmentsCount(){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT Count(*) as Value FROM shipments");
			ResultSet rs = prepStmt.executeQuery();
			int value = -1;
			while(rs.next()){
				value = rs.getInt("Value");	
			}
			return value;
			
			} catch(SQLException e){
				e.printStackTrace();
				return -1;
			}
	}
	
	public boolean updateShipmentStatus(Shipment shipment, String orderStatus){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE shipments SET shipmentStatus = ? WHERE ShipmentID = ?");
			prepStmt.setString(1, shipment.getShipmentStatus().toString());
			prepStmt.setInt(2, shipment.getShipmentID());
			prepStmt.execute();
			
			ObservableList<ShipmentDetails> shipmentDetails = shipment.getShipmentDetails();
			for(ShipmentDetails sd : shipmentDetails){
				changeOrderStatus(sd.getOrder().getOrderID(), orderStatus);
			}
				
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*
	 * SHIPMENT DETAILS TABLE QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	public boolean insertShipmentDetailsRecord(ShipmentDetails sd){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO shipment_details VALUES (?, ?, ?) ");
			prepStmt.setInt(1, sd.getShipmentID());
			prepStmt.setInt(2, sd.getOrder().getOrderID());
			prepStmt.setInt(3, sd.getPalletsCount());
			prepStmt.execute();
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	private ObservableList<ShipmentDetails> getShipmentDetails(int shipmentID){
		ObservableList<ShipmentDetails> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM shipment_details WHERE ShipmentID = ?");
			prepStmt.setInt(1, shipmentID);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				int orderID = rs.getInt("OrderID");
				Order order = getOrderByID(orderID);
				int palletsCount = rs.getInt("PalletsCount");
				tempList.add(new ShipmentDetails(shipmentID, order, palletsCount));
			}
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	/*
	 * SHIPPING INFO QUERIES
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public boolean insertShippingInfoRecord(ShippingInfo si){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO shipping_info (ShipmentID, ArrivalDate, DistanceCovered, ShipmentDuration) VALUES (?, ?, ?, ?)");
			prepStmt.setInt(1, si.getShipment().getShipmentID());
			prepStmt.setString(2, si.getArrivalDate().toString());
			prepStmt.setInt(3, si.getDistanceCovered());
			prepStmt.setDouble(4, si.getShipmentDuration());
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public ObservableList<ShippingInfo> getShippingInfos(){
		ObservableList<ShippingInfo> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM shipping_info");
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				int shipmentID = rs.getInt("ShipmentID");
				Shipment shipment = getShipmentByID(shipmentID);
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String arrivalDateString = rs.getString("ArrivalDate");
				LocalDate arrivalDate = LocalDate.parse(arrivalDateString, formatter);
				
				int distanceCovered = rs.getInt("DistanceCovered");
				double shipmentDuration = rs.getDouble("ShipmentDuration");
				
				tempList.add(new ShippingInfo(shipment, arrivalDate, distanceCovered, shipmentDuration));
			}
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * DELIVERY QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public Delivery getDeliveryByID(int deliveryID){
		Delivery delivery = null;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM deliveries WHERE DeliveryID = ?");
			prepStmt.setInt(1, deliveryID);
			ResultSet rs = prepStmt.executeQuery();
			
			ObservableList<Delivery> result = getDeliveryDataFromResultSet(rs, true);
			if(!result.isEmpty()) delivery = result.get(0);
			return delivery;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Delivery> getDeliveriesWithArrivalDate(String arrivalDate){
		ObservableList<Delivery> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM deliveries WHERE ArrivalDate LIKE ?");
			prepStmt.setString(1, arrivalDate);
			ResultSet rs = prepStmt.executeQuery();
			
			tempList = getDeliveryDataFromResultSet(rs, false);
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Delivery> getDeliveriesBySupplierID(int supplierID){
		ObservableList<Delivery> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM deliveries WHERE SupplierID = ?");
			prepStmt.setInt(1, supplierID);
			ResultSet rs = prepStmt.executeQuery();
			
			tempList = getDeliveryDataFromResultSet(rs, false);
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Delivery> getDeliveriesBySupplierName(String supplierName){
		ObservableList<Delivery> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM deliveries WHERE SupplierID = (SELECT SupplierID FROM suppliers WHERE CompanyName = ?)");
			prepStmt.setString(1, supplierName);
			ResultSet rs = prepStmt.executeQuery();
			
			tempList = getDeliveryDataFromResultSet(rs, false);
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Delivery> getMostImportantSuppliersOfCategory(String categoryName){
		ObservableList<Delivery> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT d.SupplierID, SUM(dd.Quantity) FROM deliveries d INNER JOIN delivery_details dd ON d.DeliveryID = dd.DeliveryID INNER JOIN products p ON p.ProductID = dd.ProductID INNER JOIN categories c ON c.CategoryID = p.CategoryID WHERE c.CategoryName = ? GROUP BY d.SupplierID;");
			prepStmt.setString(1, categoryName);
			ResultSet rs = prepStmt.executeQuery();
			
			tempList = getDeliveryDataFromResultSet(rs, false);
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Delivery> getDeliveriesOfProductByID(int productID){
		ObservableList<Delivery> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT d.DeliveryID FROM deliveries d INNER JOIN delivery_details dd ON dd.DeliveryID = d.DeliveryID INNER JOIN products p ON p.ProductID = dd.ProductID WHERE p.ProductID = ?");
			prepStmt.setInt(1, productID);
			ResultSet rs = prepStmt.executeQuery();
			
			while(rs.next()){
				int deliveryID = rs.getInt("DeliveryID");
				tempList.add(getDeliveryByID(deliveryID));
			}
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public ObservableList<Delivery> getDeliveriesOfProductByName(String productName){
		ObservableList<Delivery> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT d.DeliveryID FROM deliveries d INNER JOIN delivery_details dd ON dd.DeliveryID = d.DeliveryID INNER JOIN products p ON p.ProductID = dd.ProductID WHERE p.ProductName = ?");
			prepStmt.setString(1, productName);
			ResultSet rs = prepStmt.executeQuery();
			
			while(rs.next()){
				int deliveryID = rs.getInt("DeliveryID");
				tempList.add(getDeliveryByID(deliveryID));
			}
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public int getNextDeliveryID(){
		int deliveryID = -1;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT MAX(DeliveryID) as NextID FROM deliveries");
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				deliveryID = rs.getInt("NextID");
				
			}
			
			
			if(deliveryID == -1 || deliveryID == 0) return 1; //means that there is no record in order table
			else return ++deliveryID;
		} 
		catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
	
	
	public boolean insertDeliveryRecord(Delivery d){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO deliveries (DeliveryID, SupplierID, EmployeeID, DeliveryDate, PalletCount, DeliveryStatus) VALUES (?, ?, ?, ?, ?, ?)");
			prepStmt.setInt(1, d.getDeliveryID());
			prepStmt.setInt(2, d.getSupplier().getSupplierID());
			prepStmt.setInt(3, d.getEmployee().getEmployeeID());
			prepStmt.setString(4, d.getRequiredDeliveryDate().toString());
			prepStmt.setInt(5, d.getPalletCount());
			prepStmt.setString(6, d.getDeliveryStatus().toString());
			prepStmt.execute();
			
			for(DeliveryDetails dd : d.getDeliveryDetails()){
				insertDeliveryDetailsRecord(dd, d.getDeliveryID());
			}
			
			return true;
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public ObservableList<Delivery> getDeliveriesWithDeliveryStatus(String deliveryStatus){
		ObservableList<Delivery> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM deliveries WHERE DeliveryStatus = ?");
			prepStmt.setString(1, deliveryStatus);
			ResultSet rs = prepStmt.executeQuery();
			
			tempList = getDeliveryDataFromResultSet(rs, false);
			
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	private ObservableList<Delivery> getDeliveryDataFromResultSet(ResultSet rs, boolean getSingleRecord) throws SQLException{
		ObservableList<Delivery> tempList = FXCollections.observableArrayList();
		while(rs.next()){
			int deliveryID = rs.getInt("DeliveryID");
			
			int supplierID = rs.getInt("SupplierID");
			Supplier supplier = getSupplierByID(supplierID);
			
			int employeeID = rs.getInt("EmployeeID") ;
			Employee employee = getEmployeeByID(employeeID);
			
			int palletCount = rs.getInt("PalletCount");
			LocalDate requiredDeliveryDate = LocalDate.now();
			LocalDate deliveryArrivalDate = LocalDate.now();
			String driverName = rs.getString("DriverName");
			ObservableList<DeliveryDetails> deliveryDetails = getDeliveryDetails(deliveryID);
			
			String deliveryStatus = rs.getString("DeliveryStatus");
			tempList.add(new Delivery(deliveryID, supplier, employee, palletCount, deliveryDetails, deliveryStatus, requiredDeliveryDate, deliveryArrivalDate, driverName));
			
			if(getSingleRecord) return tempList;
		}
		return tempList;
	}
	
	
	public boolean updateDeliveryRecord(Delivery d){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE deliveries SET SupplierID = ?, EmployeeID = ?, DeliveryDate = ?, ArrivalDate = ?, PalletCount = ?, DriverName = ?, DeliveryStatus = ? WHERE DeliveryID = ? ");
			prepStmt.setInt(1, d.getSupplier().getSupplierID());
			prepStmt.setInt(2, d.getEmployee().getEmployeeID());
			prepStmt.setString(3, d.getRequiredDeliveryDate().toString());
			prepStmt.setString(4, d.getDeliveryArrivalDate().toString());
			prepStmt.setInt(5, d.getPalletCount());
			prepStmt.setString(6, d.getDriverName());
			prepStmt.setString(7, d.getDeliveryStatus().toString());
			prepStmt.setInt(8, d.getDeliveryID());
			prepStmt.execute();
			
			
			deleteDeliveryDetailsRecords(d.getDeliveryID());
			for(DeliveryDetails dd : d.getDeliveryDetails()){
				updateDeliveryDetailsRecord(dd, d.getDeliveryID());
			}
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateDeliveryStatus(Delivery d){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE deliveries SET DeliveryStatus = ? WHERE DeliveryID = ? ");
			prepStmt.setString(1, d.getDeliveryStatus().toString());
			prepStmt.setInt(2, d.getDeliveryID());
			
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/*
	 * DELIVERY DETAILS QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public boolean insertDeliveryDetailsRecord(DeliveryDetails dd, int deliveryID){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO delivery_details (DeliveryID, ProductID, Quantity) VALUES (?, ?, ?)");
			prepStmt.setInt(1, deliveryID);
			prepStmt.setInt(2, dd.getProduct().getProductID());
			prepStmt.setInt(3, dd.getQuantity());
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public ObservableList<DeliveryDetails> getDeliveryDetails(int deliveryID){
		ObservableList<DeliveryDetails> tempList = FXCollections.observableArrayList();
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM delivery_details WHERE DeliveryID = ? ");
			prepStmt.setInt(1, deliveryID);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				int productID = rs.getInt("ProductID");
				Product product = getProductByID(productID);
				int quantity = rs.getInt("Quantity");
				int quantityTaken = rs.getInt("QuantityTaken");
				int palletCount = rs.getInt("PalletCount");
				
				tempList.add(new DeliveryDetails(product, quantity, palletCount, quantityTaken));
			}
			
			return tempList;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	public boolean updateDeliveryDetailsRecord(DeliveryDetails dd, int deliveryID){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO delivery_details (DeliveryID, ProductID, Quantity, PalletCount) VALUES (?, ?, ?, ?)");
			prepStmt.setInt(1, deliveryID);
			prepStmt.setInt(2, dd.getProduct().getProductID());
			prepStmt.setInt(3, dd.getQuantity());
			prepStmt.setInt(4, dd.getPalletCount());
			
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean deleteDeliveryDetailsRecords(int deliveryID){
		try{
			PreparedStatement prepStmt = con.prepareStatement("DELETE FROM delivery_details WHERE DeliveryID = ?");
			prepStmt.setInt(1, deliveryID);
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateDeliveryDetailsRecord(DeliveryDetails dd, int deliveryID, int quantity){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE delivery_details SET Quantity = ? WHERE ProductID = ? AND DeliveryID = ?");
			prepStmt.setInt(1, quantity);
			prepStmt.setInt(2, dd.getProduct().getProductID());
			prepStmt.setInt(3, deliveryID);
			
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateQuantityTakenField(DeliveryDetails dd, int deliveryID, int quantity){
		try{
			int quantityAlreadyTaken = 0;
			PreparedStatement prepStmt = con.prepareStatement("SELECT QuantityTaken FROM delivery_details WHERE ProductID = ? AND DeliveryID = ?");
			prepStmt.setInt(1, dd.getProduct().getProductID());
			prepStmt.setInt(2, deliveryID);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				quantityAlreadyTaken = rs.getInt("QuantityTaken");
			}
			
			prepStmt = con.prepareStatement("UPDATE delivery_details SET QuantityTaken = ?  WHERE ProductID = ? AND DeliveryID = ?");
			prepStmt.setInt(1, quantity + quantityAlreadyTaken);
			prepStmt.setInt(2, dd.getProduct().getProductID());
			prepStmt.setInt(3, deliveryID);
			prepStmt.execute();

			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
		
	/*
	 * 
	 * PICKING QUERIES PART
	 * 
	 * 
	 * 
	 */
	
	public Picking getPickingInProgress(Employee employee){
		Picking picking = null;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM picking WHERE EmployeeID = ?");
			prepStmt.setInt(1, employee.getEmployeeID());
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				int pickingID = rs.getInt("PickingID");
				int orderID = rs.getInt("OrderID");
				Order order = getOrderByID(orderID);
				picking = new Picking(pickingID, order, employee, FXCollections.observableArrayList());
			}
			
			if(picking != null){
				prepStmt = con.prepareStatement("SELECT * FROM picking_details WHERE PickingID = ?");
				prepStmt.setInt(1, picking.getPickingID());
				rs = prepStmt.executeQuery();
				while(rs.next()){
					int productID = rs.getInt("ProductID");
					Product product = getProductByID(productID);
					int quantityToBePicked = rs.getInt("QuantityToBePicked");
					picking.addPickingDetail(product, quantityToBePicked);
				}
			}
			return picking;
		} catch (SQLException e){
			e.getStackTrace();
			return null;
		}
	}
	
	public boolean checkIfEmployeeHasPicking(Employee employee){
		boolean employeeHasAlreadyPicking = false;
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM picking WHERE EmployeeID = ?");
			prepStmt.setInt(1, employee.getEmployeeID());
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				employeeHasAlreadyPicking = true;
			}
			
			return employeeHasAlreadyPicking;
		} catch (SQLException e){
			e.getStackTrace();
			return false;
		}
	}
	
	
	
	public boolean insertPickingRecord(Employee employee, Order order){
		try{

			con.setAutoCommit(false);
			
			PreparedStatement insertPicking = con.prepareStatement("INSERT INTO picking (OrderID, EmployeeID) VALUES (?, ?)");
			insertPicking.setInt(1, order.getOrderID());
			insertPicking.setInt(2, employee.getEmployeeID());
			insertPicking.executeUpdate();
			
			PreparedStatement nextIDStatement = con.prepareStatement("Select last_insert_rowid() as Value");
			ResultSet rs = nextIDStatement.executeQuery();
			
			int nextID = 0;
			while(rs.next()){
				nextID = rs.getInt("Value");
			}
			
			PreparedStatement insertPickingDetails = null;
			for(OrderDetails od : order.getOrderDetails()){
				insertPickingDetails = con.prepareStatement("INSERT INTO picking_details (PickingID, ProductID, QuantityToBePicked) VALUES ( ?, ?, ?) ");
				insertPickingDetails.setInt(1, nextID);
				insertPickingDetails.setInt(2, od.getProduct().getProductID());
				insertPickingDetails.setInt(3, od.getQuantity());
				insertPickingDetails.executeUpdate();
			}
			
			con.commit();
			
			insertPicking.close();
			nextIDStatement.close();
			insertPickingDetails.close();
			
			con.setAutoCommit(true);
			return true;
		} catch (SQLException e){
			e.getMessage();
			return false;
		}
		
	}
	
	public boolean updatePickingDetailsRecord(int pickingID, PickingDetails pd){
		try{
			PreparedStatement prepStmt = con.prepareStatement("UPDATE picking_details SET QuantityToBePicked = ? WHERE PickingID = ? AND ProductID = ? ");
			prepStmt.setInt(1, pd.getQuantityToBePicked());
			prepStmt.setInt(2, pickingID);
			prepStmt.setInt(3, pd.getProduct().getProductID());
			
			prepStmt.executeUpdate();
			return true;
		} catch (SQLException e){
			e.getMessage();
			return false;
		}
		
	}
	
	public boolean deletePickingRecord(Picking picking){
		try{
			con.setAutoCommit(false);
			PreparedStatement prepStmt = con.prepareStatement("DELETE FROM picking_details WHERE PickingID = ?");
			prepStmt.setInt(1, picking.getPickingID());
			prepStmt.executeUpdate();
			
			prepStmt = con.prepareStatement("DELETE FROM picking WHERE PickingID = ?");
			prepStmt.setInt(1, picking.getPickingID());
			prepStmt.executeUpdate();
			con.commit();
			con.setAutoCommit(true);
			return true;
		}  catch (SQLException e){
			e.getMessage();
			return false;
		}
	}

	/*
	 * OTHER QUERIES PART
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	

	public int getTotalOrderValue(){
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT SUM(od.UnitPrice * od.QUantity * (1 - od.Discount)) as Value FROM orders o INNER JOIN order_details od ON od.OrderID = o.OrderID;");
			ResultSet rs = prepStmt.executeQuery();
			int value = -1;
			while(rs.next()){
				value = (int) Math.round(rs.getDouble("Value"));	
			}
			return value;
			
			} catch(SQLException e){
				e.printStackTrace();
				return -1;
			}
	
	}

	public String checkIfMovingWareIsDoable(String locationID, int productID, int quantity){
		
		String result = "Pod wskazan¹ lokalizacj¹ nie ma podanego produktu";
		try{
			PreparedStatement prepStmt = con.prepareStatement("SELECT ld.LocationID, ld.ProductID, ld.Quantity "
					+ "FROM locations l INNER JOIN location_details ld ON ld.LocationID = l.LocationID WHERE l.LocationID = ? AND ld.ProductID = ?;" );
			prepStmt.setString(1, locationID);
			prepStmt.setInt(2, productID);
			ResultSet rs = prepStmt.executeQuery();


			while(rs.next()){
				result = "";
				int quantityStored = rs.getInt("Quantity");
				if(quantityStored < quantity) {
					result = "W podanej lokalizacji sk³adowana jest mniejsza iloœæ towaru ni¿ wskazana";
				}
			}
		   return result;
			
		} 
		catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public String checkIfPickingIsPossible(String locationID, int productID, int quantity){
		boolean doesLocationExist = checkIfLocationExists(locationID);
		
		if(doesLocationExist) {
			String result = checkIfMovingWareIsDoable(locationID, productID, quantity);
			return result;
		} else {
			return "Lokacja nie istnieje!";
		}
	}
	
	@SuppressWarnings("resource")
	public boolean moveTheStock(String oldLocationID, String newLocationID, int productID, int quantity){
		try{
			//get new quantity stored
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM location_details WHERE LocationID = ? AND ProductID = ?" );
			prepStmt.setString(1, newLocationID);
			prepStmt.setInt(2, productID);
			ResultSet rs = prepStmt.executeQuery();

			int newQuantityStored = 0;
			boolean isProductThere = false;
			while(rs.next()){
				isProductThere = true;
				newQuantityStored = rs.getInt("Quantity");
			}
			
			//get old quantity stored
			int oldQuantityStored = 0;
			prepStmt = con.prepareStatement("SELECT * FROM location_details WHERE LocationID = ? AND ProductID = ?" );
			prepStmt.setString(1, oldLocationID);
			prepStmt.setInt(2, productID);
			
			rs = prepStmt.executeQuery();

			while(rs.next()){
				oldQuantityStored = rs.getInt("Quantity");
			}
			
			if(isProductThere){
				//add the quantity into the existing product storage
				prepStmt = con.prepareStatement("UPDATE location_details SET Quantity = ? WHERE LocationID = ? AND ProductID = ?");
				prepStmt.setInt(1, newQuantityStored + quantity );
				prepStmt.setString(2, newLocationID);
				prepStmt.setInt(3, productID);
				prepStmt.execute();
				
			} else {
				//insert new record
				prepStmt = con.prepareStatement("INSERT INTO location_details VALUES (?, ?, ?)");
				prepStmt.setString(1, newLocationID);
				prepStmt.setInt(2, productID);
				prepStmt.setInt(3, quantity);
				prepStmt.execute();

				}
			//subtract from the quantity stored in the old location
			prepStmt = con.prepareStatement("UPDATE location_details SET Quantity = ? WHERE LocationID = ? AND ProductID = ?");
			prepStmt.setInt(1, oldQuantityStored - quantity );
			prepStmt.setString(2, oldLocationID);
			prepStmt.setInt(3, productID);
			prepStmt.execute();
			return true;

			}
		catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}


	public boolean getEnterpriseData(){
		try{
			LinkedHashMap<String, String> map = null;
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM enterprise_data");
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()){
				EnterpriseData.setEnterpriseName(rs.getString("EnterpriseName"));
				EnterpriseData.setStreet(rs.getString("Street"));
				EnterpriseData.setCity(rs.getString("City"));
				EnterpriseData.setCountry(rs.getString("Country"));
				EnterpriseData.setPostalCode(rs.getString("PostalCode"));
				EnterpriseData.setPhoneNumber(rs.getString("PhoneNumber"));
				EnterpriseData.setEmail(rs.getString("Email"));
				EnterpriseData.setNip(rs.getString("NIP"));
				EnterpriseData.setRegon(rs.getString("REGON"));
				return true;
			}
			
			return false;
		} catch(SQLException e){
			return false;
		}
	}
	
	public boolean insertEnterpriseData(ObservableList<String> strings){
		try{
			PreparedStatement prepStmt = con.prepareStatement("INSERT INTO enterprise_data VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int i = 1;
			for(String s : strings){
				prepStmt.setString(i++, s);
			}
			prepStmt.execute();
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateEnterpriseData(ObservableList<String> strings){
		try{
			con.setAutoCommit(false);
			PreparedStatement prepStmt = con.prepareStatement("DELETE FROM enterprise_data");
			prepStmt.executeUpdate();
			insertEnterpriseData(strings);
			con.commit();
			con.setAutoCommit(true);
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}



	

	

	

	



	
}	
	

