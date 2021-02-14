package app.view;

import java.util.LinkedHashMap;

import app.model.EnterpriseData;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class EnterpriseDataDialogController extends DialogBasedController{
	
	@FXML
	private TextField companyNameField;
	@FXML
	private TextField streetField;
	@FXML
	private TextField cityField;
	@FXML
	private TextField countryField;
	@FXML
	private TextField postalCodeField;
	@FXML
	private TextField phoneNumberField;
	@FXML
	private TextField emailField;
	@FXML
	private TextField nipField;
	@FXML
	private TextField regonField;
	
	private boolean isCorrectlyAdded = false;
	
	
	
	@FXML
	private void handleApplyClick(){
		if(isInputValid()){
			LinkedHashMap<String, String> data = new LinkedHashMap<>();
			String enterpriseName = companyNameField.getText();
			String street = streetField.getText();
			String city = cityField.getText();
			String country = countryField.getText();
			String postalCode = postalCodeField.getText();
			String phoneNumber = phoneNumberField.getText();
			String email = emailField.getText();
			String nip = nipField.getText();
			String regon = regonField.getText();
			EnterpriseData.setEnterpriseData(enterpriseName, street, city, country, postalCode, phoneNumber, email, nip, regon);
			isCorrectlyAdded = mainApp.getController().getDatabase().insertEnterpriseData(EnterpriseData.toArray());
			if(isCorrectlyAdded) {
				UtilityClass.showAlert(AlertType.INFORMATION, "Dane firmy", "Dane firmy zosta�y zapisane", "Wprowadzone dane firmy zosta�y pomy�lnie zapisane", getCurrentStage()).showAndWait();
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B��d", "Wyst�pi� niespodziewany b��d", "Wyst�pi� b��d podczas dodawania rekord�w do bazy", getCurrentStage()).showAndWait();
			}
			
			getCurrentStage().close();
		}
	}
	
	
	private boolean isInputValid(){
		String errorMsg = "";
		
		if(companyNameField.getText().length() == 0) errorMsg += "Brak podanej nazwy firmy!\n";
		if(streetField.getText().length() == 0) errorMsg += "Brak podanej ulicy!\n";
		if(cityField.getText().length() == 0) errorMsg += "Brak podanego miasta!\n";
		if(countryField.getText().length() == 0) errorMsg += "Brak podanego kraju!\n";
		
		if(postalCodeField.getText().length() == 0) errorMsg += "Brak podanego kodu pocztowego!\n";
		else if(!postalCodeField.getText().matches("^(\\d{2}-\\d{3})$")){
			errorMsg += "Kod pocztowy musi by� formatu XX-XXX!\n";
		}
		
		if(phoneNumberField.getText().length() == 0) errorMsg += "Brak podanego numeru telefonu!\n";
		else if (!UtilityClass.isLongParsable(phoneNumberField.getText())) errorMsg += "Nieprawid�owy format numeru telefonu\n";
		
		if(emailField.getText().length() == 0) errorMsg += "Brak podanego adresu e-mail\n";
		else if (!emailField.getText().matches("^(\\S+@\\S+)$")) errorMsg += "Nieprawid�owy format adresu e-mail\n";
		
		if(nipField.getText().length() == 0) errorMsg += "Brak podanego numeru NIP\n";
		else if (!nipField.getText().matches("^(\\d{3}-\\d{2}-\\d{2}-\\d{3})$")) errorMsg += "Nieprawid�owy format numeru NIP\nPrzyk�adowy numer NIP: 123-45-67-819\n";
		
		if(regonField.getText().length() == 0) errorMsg += "Brak podanego numeru REGON\n";
		else if (!(regonField.getText().matches("^(\\d{9})$") || regonField.getText().matches("^(\\d{14})$"))) errorMsg += "Numer REGON musi zawiera� 9 lub 14 cyfr!\nPrzyk�adowy REGON: 123456785\n";
			
		if(errorMsg.length() == 0) return true;
		else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Napotkano na poni�sze b��dy:", errorMsg, getCurrentStage()).showAndWait();
			return false;
		}
	}
	
	public boolean isCorrectlyAdded(){
		return this.isCorrectlyAdded;
	}

}
