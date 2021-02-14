package app.view;

import java.time.LocalDate;

import app.model.Customer;
import app.model.Employee;
import app.model.Order;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class NewOrderDialogController extends DialogBasedController {

	@FXML
	private TextField customerIDField;
	@FXML
	private TextField employeeResponsibleIDField;
	@FXML
	private DatePicker orderDateField;
	@FXML
	private DatePicker requiredDateField;
	@FXML
	private TextField freightField;
	@FXML
	private TextField companyNameField;
	@FXML
	private TextField shippedAddressField;
	@FXML
	private TextField shippedCityField;
	@FXML
	private TextField shippedPostalCodeField;
	@FXML
	private TextField shippedCountryField;
	@FXML
	private CheckBox shipmentDataCheckBox;
	
	private Order order;
	private ObservableList<TextField> shipmentData;
	private boolean newOrderAdded = false;
	
	public void setOrder(Order order){
		this.order = order;
	}
	
	@FXML
	private void initialize(){
		shipmentData = FXCollections.observableArrayList();
		shipmentData.addAll(shippedAddressField, shippedCityField, shippedPostalCodeField, shippedCountryField);

	}
	
	/**
	 * Fill the fields
	 * @param isOrderExisting - specifies if the order passed into dialog is just initialized (and has only a few fields with data) or if it exists in DB and has full data
	 */
	public void fillTheFields(boolean isOrderExisting){

		Employee employee = mainApp.getController().getLoggedUser().getEmployee();
		String employeeText = employee.getFirstName() + " " + employee.getLastName();
		
		customerIDField.setText(order.getCustomer().getCustomerID());
		employeeResponsibleIDField.setText(employeeText);
		orderDateField.setValue(order.getOrderDate());
		companyNameField.setText(order.getCustomer().getCompanyName());
		
		if(isOrderExisting){
			//if the order is already in DB, it can be edited
			shipmentDataCheckBox.setDisable(true);
			shippedAddressField.setText(order.getAddress());
			shippedCityField.setText(order.getCity());
			shippedPostalCodeField.setText(order.getPostalCode());
			shippedCountryField.setText(order.getPostalCode());
		} else {
			//don't allow the user to change vital data before the order is inserted into DB
			customerIDField.setDisable(true);
			employeeResponsibleIDField.setDisable(true);
			orderDateField.setDisable(true);
			companyNameField.setDisable(true);
			
		}
	}
	
	private boolean isInputValid(){
		String errorString = "";
		
			if(customerIDField.getText().isEmpty()){
				errorString += "\nPole ID klienta jest niewype³nione\n";
			}
			if(employeeResponsibleIDField.getText().isEmpty()){
				errorString += "Pole pracownika jest niewype³nione\n";
			}
			if(orderDateField.getValue() == null){
				errorString += "Brak daty zamówienia\n";
			}
			if(requiredDateField.getValue() == null){
				errorString += "Brak wymaganej daty\n";
			}
			if(companyNameField.getText().isEmpty()){
				errorString += "Brak nazwy firmy klienta\n";
			}
			if(shippedAddressField.getText().isEmpty()){
				errorString += "Pole adresu wysy³ki jest niewype³nione\n";
			}
			if(shippedCityField.getText().isEmpty()){
				errorString += "Brak pola miasto\n";
			}
			if(shippedPostalCodeField.getText().isEmpty()){
				errorString += "Pole kod pocztowy jest niewype³nione\n";
			}
			if(shippedCountryField.getText().isEmpty()){
				errorString += "Pole kraj jest niewype³nione\n";
			}
			
			if(errorString.length() ==0){
				return true;
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Proszê poprawiæ nastêpuj¹ce problemy", errorString, getCurrentStage()).showAndWait();
				return false;
			}
			
			

		
	}
	
	@FXML
	private void confirmClick(){
		boolean isInputValid = isInputValid();
		if(isInputValid){
			 int orderID = mainApp.getController().getDatabase().getNextOrderID();
			 LocalDate requiredDate = requiredDateField.getValue();
			 double freight = Double.parseDouble(freightField.getText());
			 
			 String shipName = companyNameField.getText();
			 String shipAddress = shippedAddressField.getText();
			 String shipCity = shippedCityField.getText();
			 String shipPostalCode = shippedPostalCodeField.getText();
			 String shipCountry = shippedCountryField.getText();
			 
			 order.setOrderID(orderID);
			 order.setRequiredDate(requiredDate);
			 order.setFreight(freight);
			 order.setShipName(shipName);
			 order.setAddress(shipAddress);
			 order.setCity(shipCity);
			 order.setPostalCode(shipPostalCode);
			 order.setCountry(shipCountry);

			 boolean successfullyInserted = mainApp.getController().getDatabase().insertOrderRecord(order);
			 if(successfullyInserted) UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Pomyœlnie dodano rekord", "\nNowe zamówienie zosta³o dodane do bazy danych, jego zamówienie to : " + String.valueOf(orderID) + "\n", getCurrentStage()).showAndWait();
			 else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d", "Podczas dodawania nowego zamówienia wystapi³ b³¹d", getCurrentStage()).showAndWait();
			 
			 newOrderAdded = true;
			 getCurrentStage().close();
		}
	}
	
	@FXML
	private void cancelClick(){
		getCurrentStage().close();
	}
	
	
	
	@FXML
	private void checkBoxClickAction(){
		if(shipmentDataCheckBox.isSelected()){
			Customer c = order.getCustomer();
			shippedAddressField.setText(c.getAddress());
			shippedCityField.setText(c.getCity());
			shippedPostalCodeField.setText(c.getPostalCode());
			shippedCountryField.setText(c.getCountry());
			
			shipmentData.forEach(tf->tf.setDisable(true));
		} else {
			shippedAddressField.setText("");
			shippedCityField.setText("");
			shippedPostalCodeField.setText("");
			shippedCountryField.setText("");
			shipmentData.forEach(tf->tf.setDisable(false));

		}
	}
	
	public boolean isNewOrderAdded(){
		return this.newOrderAdded;
	}
}
