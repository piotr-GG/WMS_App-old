package app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Category {

	private IntegerProperty categoryID;
	private StringProperty categoryName;
	private StringProperty description;
	private ObjectProperty categoryPhoto;
	
	/**
	 * Default constructor
	 */
	
	public Category(int categoryID, String categoryName, String description, Image categoryPhoto){
		this.categoryID = new SimpleIntegerProperty(categoryID);
		this.categoryName = new SimpleStringProperty(categoryName);
		this.description = new SimpleStringProperty(description);
		this.categoryPhoto = new SimpleObjectProperty<>(categoryPhoto);
	}
	
	public Category(String categoryName){
		this.categoryName = new SimpleStringProperty(categoryName);
		this.categoryID = new SimpleIntegerProperty();
		this.description = new SimpleStringProperty();
		this.categoryPhoto = new SimpleObjectProperty<>();
	}
	/**
	 * Getters - properties
	 */
	
	public IntegerProperty getCategoryIDProperty(){
		return this.categoryID;
	}
	
	public StringProperty getCategoryNameProperty(){
		return this.categoryName;
	}
	
	public StringProperty getDescriptionProperty(){
		return this.description;
	}
	
	public ObjectProperty getCategoryPhoto(){
		return this.categoryPhoto;
	}
	
	/**
	 * Getters - values
	 */
	
	public int getCategoryID(){
		return this.categoryID.get();
	}
	
	public String getCategoryName(){
		return this.categoryName.get();
	}
	
	public String getDescription(){
		return this.description.get();
	}
	
	/**
	 * Setters - values
	 */
	
	public void setCategoryID(int categoryID){
		this.categoryID.set(categoryID);
	}
	
	public void setCategoryName(String categoryName){
		this.categoryName.set(categoryName);
	}
	
	public void setDescription(String description){
		this.description.set(description);
	}
	
	public void setCategoryPhoto(ObjectProperty categoryPhoto){
		this.categoryPhoto.set(categoryPhoto);
	}
	
	
	
}
