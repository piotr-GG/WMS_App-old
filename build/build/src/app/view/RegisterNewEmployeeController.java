package app.view;

import java.time.LocalDate;

import app.MainApp;
import app.model.Department;
import app.model.Employee;
import app.model.Permission;
import app.model.User;
import app.utility.UtilityClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RegisterNewEmployeeController {
	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private TextField nameTF;
	@FXML
	private TextField surnameTF;
	@FXML
	private ComboBox<String> postitionCBox;
	@FXML
	private ComboBox<String> departmentBox;
	@FXML
	private DatePicker birthDateDP;
	@FXML
	private DatePicker hireDateDP;
	@FXML
	private TextField addressTF;
	@FXML
	private TextField cityTF;
	@FXML
	private TextField countryTF;
	@FXML
	private TextField phoneNumberTF;
	@FXML
	private TextField emailTF;
	@FXML
	private TextField postalCodeTF;
	@FXML
	private Button OKButton;
	
	private User user;
	private Employee employee;
	
	private MainApp mainApp;
	private Stage dialogStage;
	private Stage ownerStage;
	
	private boolean okClicked = false;

	
	private void initializeBoxes(){
		
		//Insert positions into combo box
		 
		ObservableList<String> data = FXCollections.observableArrayList(
				"Pracownik biurowy","Ksiêgowy", 
				"Przedstawiciel handlowy", "Magazynier");
		postitionCBox.getItems().addAll(data);
		ObservableList<String> departments = mainApp.getController().getDatabase().getDepartmentNames();
		departmentBox.getItems().addAll(departments);
		
		updateCSSStyles();
		mainApp.getController().getStyleSheetPathProperty().addListener(changeListener -> {
			updateCSSStyles();
		});
	}
	
	private void updateCSSStyles(){
		mainPane.getStylesheets().clear();
		mainPane.getStylesheets().add(mainApp.getController().getStyleSheetPath());
	}
	
	public void setDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}
	
	public void setOwnerStage(Stage ownerStage){
		this.ownerStage = ownerStage;
	}
	
	/**
	 * Returns okClicked value
	 * @return okClicked
	 */
	public boolean isOkClicked(){
		return okClicked;
	}
	
	
	/**
	 * Checks if the data in the fields is correct
	 */
	
	public boolean isInputValid(){
		String errorMsg = "";
		
		if(nameTF.getText() == null || nameTF.getText().length() == 0) errorMsg += "Nieprawid³owe imiê pracownika\n";
		if(surnameTF.getText() == null || surnameTF.getText().length() == 0) errorMsg += "Nieprawid³owe nazwisko pracownika\n";
		if(postitionCBox.getSelectionModel().getSelectedItem() == null) errorMsg += "Brak podanej pozycji pracownika\n";
		if(departmentBox.getSelectionModel().getSelectedItem() == null) errorMsg += "Brak podanego dzia³u\n";
			
		if(birthDateDP.getValue() == null) errorMsg += "Brak podanej daty urodzenia!\n";
		else if(birthDateDP.getValue().isAfter(LocalDate.now())) errorMsg += "Nieprawid³owa wartoœæ daty urodzenia!\n";
		
		if(hireDateDP.getValue() == null) errorMsg += "Brak podanej daty zatrudnienia!\n";
		else if (hireDateDP.getValue().isAfter(LocalDate.now())) errorMsg += "Nieprawid³owa wartoœæ daty zatrudnienia!\n";
		
		if(birthDateDP.getValue() != null && hireDateDP.getValue() != null && birthDateDP.getValue().isAfter(hireDateDP.getValue())){
			errorMsg += "Data urodzenia jest pózniejsza ni¿ data zatrudnienia\n";
		}
		
		if(postalCodeTF.getText().length() == 0) errorMsg += "Brak podanego kodu pocztowego!\n";
		else if(!postalCodeTF.getText().matches("^(\\d{2}-\\d{3})$")){
			errorMsg += "Kod pocztowy musi byæ formatu XX-XXX!\n";
		}
		
		if(addressTF.getText() == null || addressTF.getText().length() == 0) errorMsg += "Nieprawid³owy adres\n";
		if(cityTF.getText() == null || cityTF.getText().length() == 0) errorMsg += "Nieprawid³owe miasto\n";
		if(countryTF.getText() == null || countryTF.getText().length() == 0) errorMsg += "Nieprawid³owy kraj\n";
		
		if(phoneNumberTF.getText().length() == 0) errorMsg += "Brak podanego numeru telefonu!\n";
		else if (!UtilityClass.isLongParsable(phoneNumberTF.getText())) errorMsg += "Nieprawid³owy format numeru telefonu\n";
		
		if(emailTF.getText().length() == 0) errorMsg += "Brak podanego adresu e-mail\n";
		else if (!emailTF.getText().matches("^(\\S+@\\S+)$")) errorMsg += "Nieprawid³owy format adresu e-mail\n";

		if(errorMsg.length() == 0){
			return true;
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Niepoprawne wartoœci", "Proszê poprawiæ nastêpuj¹ce b³êdy we wprowadzonych wartoœciach", errorMsg, dialogStage).showAndWait();
			return false;
		}
	
	}
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		initializeBoxes();
	}
	
	public void setUser(User user){
		this.user = user;
		this.employee = user.getEmployee();
		
		nameTF.textProperty().bind(employee.getFirstNameProperty());
		surnameTF.textProperty().bind(employee.getLastNameProperty());
		
	}
	

	@FXML
	private void handleOKButtonClick(){
		if(isInputValid()){
			
			//Converts the comboBox value into readable string
			String position;
			String input = postitionCBox.getSelectionModel().getSelectedItem().toString();
			int blankPosition = input.indexOf(" ");
			
			
			if(blankPosition >= 0){
				position = getPositionAsSentence(input, blankPosition);
			} else {
				position = input;
			}
			System.out.println();
			
			String departmentName = String.valueOf(departmentBox.getSelectionModel().getSelectedItem());
			Department department = mainApp.getController().getDatabase().getDepartment(departmentName);
			
			int nextID = mainApp.getController().getDatabase().getNextEmployeeID();
			
			LocalDate birthDate = birthDateDP.getValue() ;
			LocalDate hireDate = hireDateDP.getValue();
			String address = addressTF.getText();
			String city = cityTF.getText() ;
			String postalCode = postalCodeTF.getText() ;
			String country = countryTF.getText();
			String phoneNumber = phoneNumberTF.getText();
			String email = emailTF.getText();
			
			employee.setEmployeeID(nextID);
			employee.setBirthDate(birthDate);
			employee.setHireDate(hireDate);
			employee.setAddress(address);
			employee.setCity(city);
			employee.setPosition(position);
			employee.setCountry(country);
			employee.setPhoneNumber(phoneNumber);
			employee.setPostalCode(postalCode);
			employee.setEmail(email);
			employee.setPosition(position);
			employee.setDepartment(department);
			
			user.setPermission(Permission.getDefaultPermission());


			mainApp.getController().getDatabase().insertEmployeeRecord(employee);
			mainApp.getController().getDatabase().insertUserRecord(user);
			
			
			
			
			String contentText = "Nowy pracownik zosta³ przypisany do utworzonego konta u¿ytkownika.\n\nKonto u¿ytkownika przed zakoñczeniem rejestracji musi zostaæ potwierdzone przez administratora. Stosowna informacja odnoœnie procesu"
					+ " rejestracji zostanie przes³ana po wydaniu decyzji w sprawie utworzenia konta przez administratora.";
			
			UtilityClass.showAlert(AlertType.INFORMATION, "Przypisanie nowego u¿ytkownika", "Pomyœlnie przypisano pracownika", contentText, dialogStage).showAndWait();
			
			dialogStage.close();
			ownerStage.close();
		} 
	}
	
	public String getPositionAsSentence(String input, int blankPosition){
		String input1 = input.subSequence(0, blankPosition).toString();
		String input2 = input.subSequence(blankPosition, input.length()).toString();
		input2 = input2.trim();

		input1 = Character.toString((Character.toUpperCase(input1.charAt(0)))).concat(input1.substring(1));
		input2 = Character.toString((Character.toUpperCase(input2.charAt(0)))).concat(input2.substring(1));
		input = input1.concat(input2);
		
		return input;
	}
	
}
