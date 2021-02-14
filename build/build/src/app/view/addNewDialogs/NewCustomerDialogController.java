package app.view.addNewDialogs;

import app.model.Customer;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NewCustomerDialogController extends DialogBasedController {

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
	private boolean customerAdded = false;
	
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
	
	private boolean isInputValid(){
		String errorMessage = "";
		if(customerIDField.getText().isEmpty()) errorMessage += "Puste pole ID Klienta";
		if(companyNameField.getText().isEmpty()) errorMessage += "Puste pole Nazwa firmy";
		if(representativeField.getText().isEmpty()) errorMessage += "Puste pole Przedstawiciel firmy";
		if(addressField.getText().isEmpty()) errorMessage += "Puste pole Adres firmy";
		if(cityField.getText().isEmpty()) errorMessage += "Puste pole miasto";
		if(countryField.getText().isEmpty()) errorMessage += "Puste pole kraj";
		if(postalCodeField.getText().isEmpty()) errorMessage += "Puste pole kod pocztowy";
		if(phoneField.getText().isEmpty()) errorMessage += "Puste pole firmowy numer telefonu";
		if(emailField.getText().isEmpty()) errorMessage += "Puste pole email firmowy";
		
		if(errorMessage.length() == 0) return true;
		else return false;
	
	}
	
	public boolean isCustomerAdded(){
		return customerAdded;
	}
	
	public Customer getCustomer(){
		return this.customer;
	}
	
	@FXML
	private void addCustomer(){
		if(isInputValid()){

			
			String customerID = customerIDField.getText();
			String companyName = companyNameField.getText();
			String representative = representativeField.getText();
			String address = addressField.getText();
			String city = cityField.getText();
			String country = countryField.getText();
			String postalCode = postalCodeField.getText();
			String phone = phoneField.getText();
			String email = emailField.getText();
			
			customer = new Customer(customerID, companyName, representative, address, city, country, postalCode, phone, email);
			boolean successfullyAdded = mainApp.getController().getDatabase().insertCustomerRecord(customer);
			
			if(successfullyAdded){
				UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Dodano klienta", "Klient zosta³ dodany pomyœlnie", getCurrentStage()).showAndWait();
			} else 
			{
				UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "B³¹d", "Wyst¹pi³ b³¹d przy dodawaniu klienta", getCurrentStage()).showAndWait();				
			}
			
			customerAdded = true;
			getCurrentStage().close();
		}

	}
	
	@FXML
	private void cancelClick(){
		customerAdded = false;
		getCurrentStage().close();
	}
	
}
