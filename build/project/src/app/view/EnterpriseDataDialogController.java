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
				UtilityClass.showAlert(AlertType.INFORMATION, "Dane firmy", "Dane firmy zosta³y zapisane", "Wprowadzone dane firmy zosta³y pomyœlnie zapisane", getCurrentStage()).showAndWait();
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ niespodziewany b³¹d", "Wyst¹pi³ b³¹d podczas dodawania rekordów do bazy", getCurrentStage()).showAndWait();
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
			errorMsg += "Kod pocztowy musi byæ formatu XX-XXX!\n";
		}
		
		if(phoneNumberField.getText().length() == 0) errorMsg += "Brak podanego numeru telefonu!\n";
		else if (!UtilityClass.isLongParsable(phoneNumberField.getText())) errorMsg += "Nieprawid³owy format numeru telefonu\n";
		
		if(emailField.getText().length() == 0) errorMsg += "Brak podanego adresu e-mail\n";
		else if (!emailField.getText().matches("^(\\S+@\\S+)$")) errorMsg += "Nieprawid³owy format adresu e-mail\n";
		
		if(nipField.getText().length() == 0) errorMsg += "Brak podanego numeru NIP\n";
		else if (!nipField.getText().matches("^(\\d{3}-\\d{2}-\\d{2}-\\d{3})$")) errorMsg += "Nieprawid³owy format numeru NIP\nPrzyk³adowy numer NIP: 123-45-67-819\n";
		
		if(regonField.getText().length() == 0) errorMsg += "Brak podanego numeru REGON\n";
		else if (!(regonField.getText().matches("^(\\d{9})$") || regonField.getText().matches("^(\\d{14})$"))) errorMsg += "Numer REGON musi zawieraæ 9 lub 14 cyfr!\nPrzyk³adowy REGON: 123456785\n";
			
		if(errorMsg.length() == 0) return true;
		else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Napotkano na poni¿sze b³êdy:", errorMsg, getCurrentStage()).showAndWait();
			return false;
		}
	}
	
	public boolean isCorrectlyAdded(){
		return this.isCorrectlyAdded;
	}

}
