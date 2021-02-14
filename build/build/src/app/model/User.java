package app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * User class
 * @author Piotrek
 *
 */


public class User {

	private final IntegerProperty userID;
	private StringProperty login;
	private StringProperty password;
	private Permission permission;
	private Employee employee;
	private ObjectProperty userImage;
	
	private static int nextID = 1;

	
	/**
	 * Basic constructor
	 */
	
	public User(int userID, String login, String password, Employee employee, Permission permission, Image userImage){
		this.userID = new SimpleIntegerProperty(userID);
		this.login = new SimpleStringProperty(login);
		this.password = new SimpleStringProperty(password);
		this.permission = permission;
		this.employee = employee;
		this.userImage = new SimpleObjectProperty<>(userImage);
		
		if(nextID > userID){
			
		} else{
			nextID = userID + 1;
		}
	}
	
	/**
	 * Getters - properties
	 * 
	 */
	public IntegerProperty getUserIDProperty() {
		return this.userID;
	}

	public StringProperty getLoginProperty(){
		return this.login;
	};
	
	public StringProperty getPasswordProperty(){
		return this.password;
	}
	
	
	/**
	 * Getters - values
	 *
	 */
	
	
	
	public int getUserID(){
		return userID.get();
	}
	
	public String getLogin(){
		return login.get();
	}
	
	public String getPassword(){
		return password.get();
	}
	
	public Employee getEmployee(){
		return this.employee;
	}
	
	public Permission getPermission(){
		return this.permission;
	}
	
	public ObjectProperty getUserImage(){
		if(this.userImage.get() != null){
			return this.userImage;
		} 
		else {
			Image defaultImg = new Image("file:src/app/view/images/default-user.png");
			this.userImage = new SimpleObjectProperty<>(defaultImg);
			return this.userImage;
		}
		
	}
	public static int getNextID(){
		return nextID;
	}
	
	/**
	 * Setters
	 */

	public void setLogin(String login){
		this.login.set(login);
	}
	
	public void setPassword(String password){
		this.password.set(password);
	}
	
	public void setEmployee(Employee employee){
		this.employee = employee;
	}
	
	public void setPermission(Permission permission){
		this.permission = permission;
	}
	
	public void setUserImage(Image userImage){
		this.userImage.set(userImage);
	}
	
	public static void decreaseNextID(){
		nextID = nextID - 1;
	}


	public boolean equalsOtherUser(User user){
		if(this.getUserID() != user.getUserID()) return false;
		if(!this.getLogin().equals(user.getLogin())) return false;
		if(!this.getPassword().equals(user.getPassword())) return false;
		
		return true;
	}
	
}
