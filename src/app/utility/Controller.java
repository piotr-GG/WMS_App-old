
package app.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;

import app.MainApp;
import app.database.DB;
import app.model.Message;
import app.model.User;
import app.view.EnterpriseDataDialogController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

/**
 * Class designed to control every DB query and to interact with data in observableLists
 * @author Piotrek
 *
 */

public class Controller {

	
	private DB database;


	private User loggedUser  = null;    //currently logged user
	private boolean isLogged = false;   //true - logged , false - not logged
	
    private Locale currentLocale;
    private Locale espLocale;
    
    private ObjectProperty<Locale> localeProperty;
    private StringProperty styleSheetPathProperty;

	public Controller(){
		
    	database = new DB();
		espLocale = new Locale.Builder().setLanguage("esp").setLanguageTag("esp").build();
		currentLocale = Locale.ROOT;
		localeProperty = new SimpleObjectProperty<Locale>(Locale.ROOT);
		styleSheetPathProperty = new SimpleStringProperty("file:src/app/view/MyTheme.css");
	}
	
	
	/**
	 * Returns true if user is logged in and false if user is not logged in
	 * @return boolean
	 */
	public boolean getLoginStatus(){
		return isLogged;
	}
	
	/**
	 * Sets login status
	 * @param isLogged
	 */
	public void setLoginStatus(boolean isLogged){
		this.isLogged = isLogged;
	}
	/**
	 * Returns currently logged user
	 */
	public User getLoggedUser(){
		return loggedUser;
	}
	
	/**
	 * Sets the loggedUser field
	 * @param loggedUser
	 */
	public void setLoggedUser(User loggedUser){
		this.loggedUser = loggedUser;
	}

	/**
	 * Executes login procedure
	 * @param login
	 * @param password
	 * @return
	 */
	

	
	public boolean logout(){
		if(getLoggedUser() != null && isLogged == true){
			setLoggedUser(null);
			setLoginStatus(false);
			return true;
		} else {
			System.out.println("Brak zalogowanego u¿ytkownika.");
			return false;
		}
	}
	
	/**
	 * Getter for DB instance
	 * @return
	 */
	public DB getDatabase(){
		return database;
	}
	

	
	/**
	 * Search through the messages and find a message by it's ID
	 * @param messageID
	 * @param messageData
	 * @return
	 */
	
	public Message getMessageByID(int messageID, ObservableList<Message> messageData){
		for(Message m : messageData){
			if(m.getMessageID() == messageID) return m;
		}
		return null;
	}
	
	public void setCurrentLocale(Locale locale){
		currentLocale = locale;
		localeProperty.setValue(locale);
	}

	public Locale getCurrentLocale(){
		return this.currentLocale;
	}
	
	public Locale getEspLocale(){
		return this.espLocale;
	}
	
	public ObjectProperty<Locale> getLocaleProperty(){
		return this.localeProperty;
	}
	
	public StringProperty getStyleSheetPathProperty(){
		return this.styleSheetPathProperty;
	}
	
	public String getStyleSheetPath(){
		return this.styleSheetPathProperty.get();
	}
	
	public void setStyleSheetPath(String styleSheetPath){
		this.styleSheetPathProperty.set(styleSheetPath);
	}
	
	
	
}
