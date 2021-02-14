package app.view.settingsPanes;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import app.model.EnterpriseData;
import app.utility.UTF8Control;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import app.view.inheritance.LanguageInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class EnterprisePaneController extends DialogBasedController implements LanguageInterface{

	@FXML
	private Label enterpriseOptionsLabel;
	@FXML
	private Label enterpriseNameLabel;
	@FXML
	private Label streetLabel;
	@FXML
	private Label cityLabel;
	@FXML
	private Label postalCodeLabel;
	@FXML
	private Label countryLabel;
	@FXML
	private Label phoneNumberLabel;
	@FXML
	private Label emailLabel;
	@FXML
	private Label nipLabel;
	@FXML
	private Label regonLabel;
	@FXML
	private Button applyButton;
	@FXML
	private Button cancelButton;
	
	@FXML
	private TextField companyNameField;
	@FXML
	private TextField streetField;
	@FXML
	private TextField cityField;
	@FXML
	private TextField postalCodeField;
	@FXML
	private TextField phoneNumberField;
	@FXML
	private TextField countryField;
	@FXML
	private TextField emailField;
	@FXML
	private TextField nipField;
	@FXML
	private TextField regonField;
	@FXML
	private CheckBox editDataCheckBox;
	
	private ObservableList<Node> fieldData;
	private ResourceBundle languagePack;
	
	@FXML
	private void initialize(){
		fieldData = FXCollections.observableArrayList();
		fieldData.addAll(companyNameField, streetField, cityField, postalCodeField, countryField, phoneNumberField, emailField, nipField, regonField);

		editDataCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldState, Boolean newState) {
				if(newState == true){
					for(Node n : fieldData) n.setDisable(false);
				} else {
					for(Node n : fieldData) n.setDisable(true);
				}	
			}
		});
	}
	
	public void fillTheFields(){

		companyNameField.setText(EnterpriseData.getEnterpriseName());
		streetField.setText(EnterpriseData.getStreet());
		cityField.setText(EnterpriseData.getCity());
		postalCodeField.setText(EnterpriseData.getPostalCode());
		phoneNumberField.setText(EnterpriseData.getPhoneNumber());
		countryField.setText(EnterpriseData.getCountry());
		emailField.setText(EnterpriseData.getEmail());
		nipField.setText(EnterpriseData.getNip());
		regonField.setText(EnterpriseData.getRegon());
		

	}
	
	@FXML
	private void handleApplyClick(){
		if(isInputValid()){
			
			ObservableList<String> data = FXCollections.observableArrayList();
			
			String enterpriseName = companyNameField.getText();
			String street = streetField.getText();
			String city = cityField.getText();
			String country = countryField.getText();
			String postalCode = postalCodeField.getText();
			String phoneNumber = phoneNumberField.getText();
			String email = emailField.getText();
			String nip = nipField.getText();
			String regon = regonField.getText();
			
			data.addAll(enterpriseName, street, city, country, postalCode, phoneNumber, email, nip, regon);

			boolean isDataEqual = isDataEqual(data);
			if(!isDataEqual){
				Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Zapisz dane", "PotwierdŸ dane", "Czy chcesz zapisaæ dane firmy?", getCurrentStage()).showAndWait();
				if(result.get() == ButtonType.OK){
					boolean successfullyUpdated = mainApp.getController().getDatabase().updateEnterpriseData(data);
					if(successfullyUpdated) {
						UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Pomyœlnie zapisano dane", "Dane zosta³y pomyœlnie zapisane", getCurrentStage()).showAndWait();
						EnterpriseData.setEnterpriseData(enterpriseName, street, city, country, postalCode, phoneNumber, email, nip, regon);
						fillTheFields();
					}
					else UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Wyst¹pi³ b³¹d", "Podczas zapisywania danych wyst¹pi³ b³¹d!", getCurrentStage()).showAndWait();
					
				}
			} 
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
		else if (!nipField.getText().matches("^(\\d{3}-\\d{2}-\\d{2}-\\d{3})$")) errorMsg += "Nieprawid³owy format numeru NIP\n";
		
		if(regonField.getText().length() == 0) errorMsg += "Brak podanego numeru REGON\n";
		else if (!(regonField.getText().matches("^(\\d{9})$") || regonField.getText().matches("^(\\d{14})$"))) errorMsg += "Numer REGON musi zawieraæ 9 lub 14 cyfr!\n";
		
		if(errorMsg.length() == 0) return true;
		else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Napotkano na poni¿sze b³êdy:", errorMsg, getCurrentStage()).showAndWait();
			return false;
		}
	}
	
	private boolean isDataEqual(ObservableList<String> data){
		
		ObservableList<String> enterpriseData = EnterpriseData.toArray();
		
		for(int i = 0; i < enterpriseData.size(); i++){
			if (!data.get(i).equals(enterpriseData.get(i))) return false;
		}
		return true;
	}
	
	@FXML
	private void handleCancelClick(){
		ownerStage.close();
	}

	public void initializeLanguageDisplay(){
		updateDisplay();
		
		mainApp.getController().getLocaleProperty().addListener(new ChangeListener<Locale>(){
			@Override
			public void changed(ObservableValue<? extends Locale> observable, Locale oldValue, Locale newValue) {
				updateDisplay();
			}
		});
	}
	
	@Override
	public void updateDisplay() {
		languagePack = ResourceBundle.getBundle("app.view.language.EnterprisePane", mainApp.getController().getCurrentLocale(), new UTF8Control());
		enterpriseOptionsLabel.setText(languagePack.getString("enterpriseOptionsLabel"));
		enterpriseNameLabel.setText(languagePack.getString("enterpriseNameLabel"));
		streetLabel.setText(languagePack.getString("streetLabel"));
		cityLabel.setText(languagePack.getString("cityLabel"));
		postalCodeLabel.setText(languagePack.getString("postalCodeLabel"));
		countryLabel.setText(languagePack.getString("countryLabel"));
		phoneNumberLabel.setText(languagePack.getString("phoneNumberLabel"));
		emailLabel.setText(languagePack.getString("emailLabel"));
		nipLabel.setText(languagePack.getString("nipLabel"));
		regonLabel.setText(languagePack.getString("regonLabel"));
		applyButton.setText(languagePack.getString("applyButton"));
		cancelButton.setText(languagePack.getString("cancelButton"));
		editDataCheckBox.setText(languagePack.getString("editDataCheckBox"));
	}
	
	
	
}
