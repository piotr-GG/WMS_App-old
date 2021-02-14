package app.view.addNewDialogs;

import app.model.Shipper;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class NewShipperDialogController extends DialogBasedController {
	
	@FXML
	private TextField shipperIDField;
	@FXML
	private TextField shipperCompanyNameField;
	@FXML
	private TextField phoneNumberField;
	@FXML
	private TextField emailField;
	@FXML
	private Button executeButton;
	
	private boolean showInfoMode = false;
	
	private Shipper shipper;

	private void addTextRegexes(){
		emailField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldString, String newString) {
			   if(!newString.equals("")){
				if(UtilityClass.containsListOfChars(newString)) {
					emailField.setText(oldString);
				}
			   }
			}
			
		});
		
		phoneNumberField.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldString, String newString) {
				if(!newString.equals("")){
				if (!newString.matches("\\d+")) {
					phoneNumberField.setText(oldString);
				}
				
				if(newString.length() > 9){
					phoneNumberField.setText(oldString);
				}
			}
		  }
			
		});
		
		shipperCompanyNameField.textProperty().addListener(new ChangeListener<String>(){
			public void changed(ObservableValue<? extends String> arg0, String oldString, String newString) {
			  if(!newString.equals("")){	
				if (!newString.matches("\\D+")){
					shipperCompanyNameField.setText(oldString);
				}
			  }
			}
		});
	}
	
	@FXML
	private void handleExecute(){
		if(shipper == null){
			if(isInputValid()){
				int shipperID = mainApp.getController().getDatabase().getNextShipperID();
				String companyName = shipperCompanyNameField.getText();
				String phone = phoneNumberField.getText();
				String email = emailField.getText();
				
				Shipper newShipper = new Shipper(shipperID, companyName, phone, email);
				
				boolean successfullyInserted = mainApp.getController().getDatabase().insertShipperRecord(newShipper);
				if(successfullyInserted) UtilityClass.showAlert(AlertType.INFORMATION, "Dodaj spedytora", "Dodano spedytora", "Nowy spedytor o ID: " + shipperID + " zosta³ pomyœlnie dodany!", getCurrentStage()).showAndWait();
				else UtilityClass.showAlert(AlertType.ERROR, "Dodaj spedytora", "Wyst¹pi³ b³¹d!", "Podczas dodawania spedytora wyst¹pi³ niespodziewany b³¹d!", getCurrentStage()).showAndWait();
				
				getCurrentStage().close();
			}
		} else {
			getCurrentStage().close();
		}
	}
	
	@FXML
	private void handleCancel(){
		getCurrentStage().close();
	}
	
	private boolean isInputValid(){
		String errMsg = "";
		if(shipperCompanyNameField.getText().length() == 0) errMsg += "Brak danych w polu nazwy spedytora!\n";
		if(phoneNumberField.getText().length() == 0) errMsg += "Brak danych w polu numeru telefonu!\n";
		if(emailField.getText().length() == 0) errMsg += "Brak danych w polu email!\n";
		
		if(errMsg.length() == 0) return true;
		else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Napotkano nastêpuj¹ce b³êdy:", errMsg, getCurrentStage()).showAndWait();
			return false;
		}
	}
	
	private void fillTheFields(){
		
		shipperIDField.setText(String.valueOf(shipper.getShipperID()));
		shipperCompanyNameField.setText(shipper.getCompanyName());
		phoneNumberField.setText(shipper.getPhone());
		emailField.setText(shipper.getEmail());
		
		shipperIDField.setEditable(false);
		shipperCompanyNameField.setEditable(false);
		phoneNumberField.setEditable(false);
		emailField.setEditable(false);
		
		executeButton.setText("OK");
	}
	
	/**
	 * shipper = null means adding new shipper, shipper != null means showing info about shipper
	 */

	public void setShipper(Shipper shipper){
		this.shipper = shipper;
		if(shipper != null) fillTheFields();
		else addTextRegexes();
	}
	
}
