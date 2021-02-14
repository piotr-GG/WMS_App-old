package app.view.addNewDialogs;

import app.model.Customer;
import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CustomerDetailsDialogController extends DialogBasedController {

	@FXML
	private TextField customerIDField;
	@FXML
	private TextField companyNameField;
	@FXML
	private TextField representativeField;
	@FXML 
	private TextField addressField;
	@FXML 
	private TextField cityField; 
	@FXML  
	private TextField countryField;
	@FXML
	private TextField postalCodeField;
	@FXML 
	private TextField phoneField; 
	@FXML
	private TextField emailField;
	
	private Customer customer;
	
	public void setCustomer(Customer customer){
		this.customer = customer;
		fillTheFields();
	}
	
	private void fillTheFields(){
		customerIDField.setText(customer.getCustomerID());
		companyNameField.setText(customer.getCompanyName());
		representativeField.setText(customer.getRepresentative());
		addressField.setText(customer.getAddress());
		cityField.setText(customer.getCity());
		countryField.setText(customer.getCountry());
		postalCodeField.setText(customer.getPostalCode());
		phoneField.setText(customer.getPhone());
		emailField.setText(customer.getEmail());
	
	}
	
	@FXML
	private void handleOKClick(){
		getCurrentStage().close();
	}
}
