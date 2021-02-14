package app.view.addNewDialogs;

import java.util.Optional;

import app.model.Supplier;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NewSupplierDialogController extends DialogBasedController {
	
	@FXML
	private TextField supplierIDField;
	@FXML
	private TextField companyNameField;
	@FXML
	private TextField representativeField;
	@FXML
	private TextField addressField;
	@FXML
	private TextField cityField;
	@FXML
	private TextField postalCodeField;
	@FXML
	private TextField countryField;
	@FXML
	private TextField phoneNumberField;
	@FXML
	private TextArea otherArea;
	@FXML
	private Button executeButton;
	@FXML
	private Label dialogLabel;
	
	private Supplier supplier = null;
	private boolean newSupplierMode = true;

	@FXML
	public void handleExecuteClick(){
		if(isInputValid()){
			if(newSupplierMode){
				addNewSupplierProcedure();
			} else {
				editSupplierProcedure();
			}
		}
	}
	
	@FXML
	public void handleExitClick(){
		if(newSupplierMode) {
			Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "WyjdŸ", "PotwierdŸ polecenie", "Proszê potwierdziæ wyjœcie z okna nowego dostawcy", getCurrentStage()).showAndWait();
			if(result.get() == ButtonType.OK) getCurrentStage().close();
		} else {
			getCurrentStage().close();
		}
		
	}
	
	private void addNewSupplierProcedure(){
		
		String companyName = companyNameField.getText();
		String representative = representativeField.getText();
		String address = addressField.getText();
		String city = cityField.getText();
		String postalCode = postalCodeField.getText();
		String country = countryField.getText();
		String phoneNumber = phoneNumberField.getText();
		String other = otherArea.getText();
		
		int nextID = mainApp.getController().getDatabase().getNextSupplierID();
		
		supplier = new Supplier(nextID, companyName, representative, address, city, postalCode, country, phoneNumber, other);
		boolean successfullyInserted = mainApp.getController().getDatabase().insertSupplierRecord(supplier);
		if(successfullyInserted){
			UtilityClass.showAlert(AlertType.INFORMATION, "Dodaj dostawcê", "Dostawca zosta³ dodany", "Dostawca o ID: " + nextID +" zosta³ pomyœlnie dodany do programu", getCurrentStage()).showAndWait();
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d", "Podczas dodawania dostawcy wyst¹pi³ b³¹d!", getCurrentStage()).showAndWait();
		}
		getCurrentStage().close();
	}
	
	private void editSupplierProcedure(){
		String companyName = companyNameField.getText();
		String representative = representativeField.getText();
		String address = addressField.getText();
		String city = cityField.getText();
		String postalCode = postalCodeField.getText();
		String country = countryField.getText();
		String phoneNumber = phoneNumberField.getText();
		String other = otherArea.getText();
		Supplier newSupplier = new Supplier(supplier.getSupplierID(), companyName, representative, address, city, postalCode, country, phoneNumber, other);
		boolean areEqual = newSupplier.areSuppliersEqual(supplier);
		if(!areEqual){
			boolean successfullyUpdated = mainApp.getController().getDatabase().updateSupplierRecord(newSupplier);
			if(successfullyUpdated){
				UtilityClass.showAlert(AlertType.INFORMATION, "Dodaj dostawcê", "Dostawca zosta³ dodany", "Dostawca zosta³ pomyœlnie zaktualizowany", getCurrentStage()).showAndWait();
				
				supplier.setCompanyName(companyName);
				supplier.setRepresentative(representative);
				supplier.setAddress(address);
				supplier.setCity(city);
				supplier.setPostalCode(postalCode);
				supplier.setCountry(country);
				supplier.setPhoneNumber(phoneNumber);
				supplier.setOther(other);
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d", "Podczas aktualizowania dostawcy wyst¹pi³ b³¹d!", getCurrentStage()).showAndWait();
			}
			

			
			getCurrentStage().close();
		}
	}
	
	
	public void fillTheFields(){
		this.supplierIDField.setText(String.valueOf(supplier.getSupplierID()));
		this.companyNameField.setText(supplier.getCompanyName());
		this.representativeField.setText(supplier.getRepresentative());
		this.addressField.setText(supplier.getAddress());
		this.cityField.setText(supplier.getCity());
		this.postalCodeField.setText(supplier.getPostalCode());
		this.countryField.setText(supplier.getCountry());
		this.phoneNumberField.setText(supplier.getPhoneNumber());
		this.otherArea.setText(supplier.getOther());
		this.executeButton.setText("Zmieñ");
		this.dialogLabel.setText("Edytuj dostawcê");
	}
	
	public void setSupplier(Supplier supplier){
		this.supplier = supplier;
	}
	
	public void setNewSupplierMode(boolean newSupplierMode){
		this.newSupplierMode = newSupplierMode;
	}
	
	private boolean isInputValid(){
		String errorMsg = "";
		
		if(companyNameField.getText().length() == 0) errorMsg += "Pole Nazwa firmy jest puste\n";
		if(representativeField.getText().length() == 0) errorMsg += "Pole Przedstawiciel jest puste\n";
		if(addressField.getText().length() == 0) errorMsg += "Pole Adres jest puste\n";
		if(cityField.getText().length() == 0) errorMsg += "Pole Miasto jest puste\n";
		if(postalCodeField.getText().length() == 0) errorMsg += "Pole Kod pocztowy jest puste\n";
		if(countryField.getText().length() == 0) errorMsg += "Pole Kraj jest puste\n";
		if(phoneNumberField.getText().length() == 0) errorMsg += " Pole Telefon jest puste\n";
		if(otherArea.getText().length() == 0) errorMsg += "Pole Inne jest puste\n";
		
		if(errorMsg.length() == 0) return true;
		else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Proszê poprawiæ nastêpuje b³êdy", errorMsg, getCurrentStage()).showAndWait();
			return false;
		}
	}
}
