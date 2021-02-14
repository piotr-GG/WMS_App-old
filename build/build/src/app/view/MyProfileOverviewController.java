package app.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import app.MainApp;
import app.model.Employee;
import app.model.User;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller for dialog stage in myProfile
 * @author Piotrek
 *
 */
public class MyProfileOverviewController extends DialogBasedController {
	
	@FXML
	private RadioButton editEmployeeRB;
	@FXML
	private TextField nameTF;
	@FXML
	private TextField surnameTF;
	@FXML
	private ComboBox<String> postitionCBox;
	@FXML
	private TextField departmentTF;
	@FXML
	private DatePicker birthDateDP;
	@FXML
	private DatePicker hireDateDP;
	@FXML
	private TextField addressTF;
	@FXML
	private TextField cityTF;
	@FXML
	private TextField postalCodeTF;
	@FXML
	private TextField countryTF;
	@FXML
	private TextField phoneNumberTF;
	@FXML
	private TextField emailTF;
	@FXML
	private RadioButton editUserRB;
	@FXML
	private TextField userNameTF;
	@FXML
	private PasswordField passwordPF;
	@FXML
	private PasswordField repeatPasswordPF;
	@FXML
	private Button OKButton;
	@FXML
	private ImageView photoIMG;
	@FXML
	private Label nameLabel;
	
	private User user;
	private boolean okClicked = false;
	private ObservableList<Node> employeeFields;
	private ObservableList<Node> userFields;
	
	@FXML
	private void initialize(){
		employeeFields = FXCollections.observableArrayList();
		userFields = FXCollections.observableArrayList();
		
		employeeFields.addAll(nameTF, surnameTF, postitionCBox, birthDateDP, hireDateDP, addressTF, cityTF, postalCodeTF, countryTF, phoneNumberTF, emailTF);
		userFields.addAll(userNameTF, passwordPF, repeatPasswordPF);
	}
	
	
	/**
	 * Get data about user and insert it into given fields
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void setDataIntoFields(User user){
		
		Employee emp = user.getEmployee();
		
		//ComboBox
		if(emp.getPosition().equals("Administrator")){
			//no possibility for administrator to change his position
			postitionCBox.getItems().add(emp.getPosition());
			postitionCBox.getSelectionModel().select("Administrator");
		} else {
			//no posibility for other users to become admin
			postitionCBox.getItems().addAll(emp.getPositionNotString().valuesExtended());
			postitionCBox.getItems().remove("Administrator");
			
			postitionCBox.getSelectionModel().select(emp.getExtendedPosition());
		}
		
		
		//Label
		StringExpression expression = (emp.getFirstNameProperty().concat(" ")).concat(emp.getLastNameProperty()); //Concatenates first name string property with "" string and then with last name string property
		nameLabel.textProperty().bind(expression);

		
		//Employee part
		
		nameTF.setText(emp.getFirstName());
		surnameTF.setText(emp.getLastName());
		departmentTF.setText(emp.getDepartment().getDepartmentName());
		birthDateDP.setValue(emp.getBirthDate());
		hireDateDP.setValue(emp.getHireDate());
		addressTF.setText(emp.getAddress());
		cityTF.setText(emp.getCity());
		postalCodeTF.setText(emp.getPostalCode());
		countryTF.setText(emp.getCountry());
		phoneNumberTF.setText(emp.getPhoneNumber());
		emailTF.setText(emp.getEmail());
		
		//User part
		userNameTF.setText(user.getLogin());
		passwordPF.setText(user.getPassword());
		repeatPasswordPF.setText(user.getPassword());
		
		
		if(user.getUserImage() != null){
			Bindings.bindBidirectional(this.photoIMG.imageProperty(), user.getUserImage());
		}
	}
	
	/**
	 * Returns okClicked value
	 * @return okClicked
	 */
	public boolean isOkClicked(){
		return okClicked;
	}
	
	/**
	 * Handles click action of the "OK" button
	 */
	
	@FXML
	private void handleOKClick(){
		if(isInputValid()){
			
			String position;
			String input = postitionCBox.getSelectionModel().getSelectedItem().toString();
			int blankPosition = input.indexOf(" ");
			
			
			if(blankPosition >= 0){
				position = getPositionAsSentence(input, blankPosition);
			} else {
				position = input;
			}
			
			Employee employee = user.getEmployee();
			Employee tempEmployee = new Employee(employee.getEmployeeID(), nameTF.getText(), surnameTF.getText(), birthDateDP.getValue(), hireDateDP.getValue() , addressTF.getText(), cityTF.getText(), postalCodeTF.getText(), countryTF.getText(), phoneNumberTF.getText(), emailTF.getText(), position, null);
		
			User tempUser = new User(user.getUserID(), userNameTF.getText(), passwordPF.getText(), null, null, null); 
			
			//compare the previous instance of employee and the one built with data from fields 
			
			boolean equalEmployees = employee.equalsEmployee(tempEmployee);  //checks if the two objects are equals. If they are equal, no changes should be saved. If they are different, changes should be saved.
			boolean equalUsers = user.equalsOtherUser(tempUser);
			
			if(equalEmployees && equalUsers){  								
				//equal objects
				okClicked = true;
				getCurrentStage().close();
			} 
			else {						
				//different objects
				Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Potwierdz zmiany", "Zmiany w profilu pracownika", "Czy chcesz zapisaæ zmiany dokonane w profilu pracownika?", getCurrentStage()).showAndWait();
			    if(result.get() == ButtonType.OK){	
				    saveData();
					okClicked = true; } 
			    getCurrentStage().close();

			}
		}
		
	}
	/**
	 * Shows a new stage that informs user of its (his) permissions 
	 */
	@FXML
	private void handleShowPermissionsClick(){
		if(user.getPermission() != null){ 
			PermissionOverviewController controller = (PermissionOverviewController) UtilityClass.showDialogFactory("view/PermissionOverview.fxml", dialogStage, mainApp, ownerStage, "Uprawnienia");
			controller.setPermission(user.getPermission());
			controller.getCurrentStage().setResizable(false);
			controller.getCurrentStage().showAndWait();
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak uprawnieñ", "Brak przypisanych uprawnieñ do u¿ytkownika", getCurrentStage()).showAndWait();
		}
	}
	
	/**
	 * Returns employee position as string ready to be saved in database and program
	 * @param input			- employee's position as string
	 * @param blankPosition - position of white space (" ")
	 * @return
	 */
	public String getPositionAsSentence(String input, int blankPosition){
		String input1 = input.subSequence(0, blankPosition).toString();
		String input2 = input.subSequence(blankPosition, input.length()).toString();
		input2 = input2.trim();

		input1 = Character.toString((Character.toUpperCase(input1.charAt(0)))).concat(input1.substring(1));
		input2 = Character.toString((Character.toUpperCase(input2.charAt(0)))).concat(input2.substring(1));
		input = input1.concat(input2);
		
		return input;
	}
	
	/**
	 * Saves the changes made in user instance
	 */
	
	public boolean saveData(){
		
		Employee employee = user.getEmployee();
		if(isInputValid()){

			//Convert combobox value into string readable by program and database
			String position;
			String input = postitionCBox.getSelectionModel().getSelectedItem().toString();
			int blankPosition = input.indexOf(" ");
			
			if(blankPosition >= 0){
				position = getPositionAsSentence(input, blankPosition);
			} else {
				position = input;
			}
			
			
			//Employee part
			employee.setFirstName(nameTF.getText());
			employee.setLastName(surnameTF.getText());
			employee.setPosition(position);
			employee.setBirthDate(birthDateDP.getValue());
			employee.setHireDate(hireDateDP.getValue());
			employee.setAddress(addressTF.getText());
			employee.setCity(cityTF.getText());
			employee.setCountry(countryTF.getText());
			employee.setPostalCode(postalCodeTF.getText());
			employee.setPhoneNumber(phoneNumberTF.getText());
			employee.setEmail(emailTF.getText());
			

			//User part
			user.setLogin(userNameTF.getText());
			user.setPassword(passwordPF.getText());
			
			boolean successfullyUpdated = mainApp.getController().getDatabase().updateEmployee(employee);
			successfullyUpdated = mainApp.getController().getDatabase().updateUser(user);
			
			if(successfullyUpdated) UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Pomyœlnie zaktualizowane dane osobowe", "Dane osobowe zosta³y zaktualizowane", getCurrentStage()).showAndWait();
			else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d", "Podczas aktualizacji danych wyst¹pi³ b³¹d!", getCurrentStage()).showAndWait();
			
			return true;

		} else {
			return false;
		}

	}
	
	/**
	 * Checks if the data in the fields is correct
	 */
	
	public boolean isInputValid(){
		String errorMsg = "";
		if(nameTF.getText() == null || nameTF.getText().length() == 0){
			errorMsg += "Nieprawid³owe imiê pracownika\n";
		}
		if(surnameTF.getText() == null || surnameTF.getText().length() == 0){
			errorMsg += "Nieprawid³owe nazwisko pracownika\n";
		}
		if(postitionCBox.getSelectionModel().getSelectedItem() == null){
			errorMsg += "B³¹d w stanowisku pracownika\n";
		}
		
		if(birthDateDP.getValue() == null) errorMsg += "Brak podanej daty urodzenia!\n";
		else if(birthDateDP.getValue().isAfter(LocalDate.now())) errorMsg += "Nieprawid³owa wartoœæ daty urodzenia!\n";
		
		if(hireDateDP.getValue() == null) errorMsg += "Brak podanej daty zatrudnienia!\n";
		else if (hireDateDP.getValue().isAfter(LocalDate.now())) errorMsg += "Nieprawid³owa wartoœæ daty zatrudnienia!\n";
		
		if(birthDateDP.getValue() != null && hireDateDP.getValue() != null && birthDateDP.getValue().isAfter(hireDateDP.getValue())){
			errorMsg += "Data urodzenia jest pózniejsza ni¿ data zatrudnienia\n";
		}
		
		if(addressTF.getText() == null || addressTF.getText().length() == 0){
			errorMsg += "Nieprawid³owy adres\n";
		}
		if(cityTF.getText() == null || cityTF.getText().length() == 0){
			errorMsg += "Nieprawid³owe miasto\n";
		}
		
		if(postalCodeTF.getText().length() == 0) errorMsg += "Brak podanego kodu pocztowego!\n";
		else if(!postalCodeTF.getText().matches("^(\\d{2}-\\d{3})$")){
			errorMsg += "Kod pocztowy musi byæ formatu XX-XXX!\n";
		}
		
		if(countryTF.getText() == null || countryTF.getText().length() == 0){
			errorMsg += "Nieprawid³owy kraj\n";
		}
		
		if(phoneNumberTF.getText().length() == 0) errorMsg += "Brak podanego numeru telefonu!\n";
		else if (!UtilityClass.isLongParsable(phoneNumberTF.getText())) errorMsg += "Nieprawid³owy format numeru telefonu\n";
		
		if(emailTF.getText().length() == 0) errorMsg += "Brak podanego adresu e-mail\n";
		else if (!emailTF.getText().matches("^(\\S+@\\S+)$")) errorMsg += "Nieprawid³owy format adresu e-mail\n";
		
		if(userNameTF.getText() == null || userNameTF.getText().length() == 0){
			errorMsg += "Nieprawid³owa nazwa u¿ytkownika\n";
		}
		if(!(passwordPF.getText().equals(repeatPasswordPF.getText()))){
			errorMsg += "Podane has³a nie zgadzaj¹ siê ze sob¹\n";
		}
		if(passwordPF.getText() == null || passwordPF.getText().length() == 0){
			errorMsg += "Nieprawid³owe has³o\n";
		}
		if(repeatPasswordPF.getText() == null || repeatPasswordPF.getText().length() == 0){
			errorMsg += "Nieprawid³owa wartoœæ w polu powtórz has³o\n";
		}
		if(errorMsg.length() == 0){
			return true;
		} else {
			//New alert mesage
			UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Niepoprawne wartoœci w polach", "Proszê poprawiæ b³êdy we wprowadzonych wartoœciach\n\n" + errorMsg, getCurrentStage()).showAndWait();
			return false;
		}
	
	}
	
	public void setUser(User user){
		this.user = user;
		setDataIntoFields(user);
	}
	
	
	/**
	 * Disables or enables fields of an employee according to the state of radiobutton (clicked, not clicked)
	 */
	@FXML
	private void handleEditEmployeeSelected(){
		if(editEmployeeRB.isSelected())	
			employeeFields.forEach((node) -> node.setDisable(false)); 
		else 							
			employeeFields.forEach((node) -> node.setDisable(true));
	}
	
	/**
	 * Disables or enables fields of a user according to the state of radiobutton (clicked, not clicked)
	 */
	
	@FXML
	private void handleEditUserSelected(){
		if(editUserRB.isSelected()) 	
			userFields.forEach((node) -> node.setDisable(false));	
		else 							
			userFields.forEach((node) -> node.setDisable(true));
	}
	
	@FXML
	private void handlePhotoClick() throws FileNotFoundException{
		File file = chooseTheProfilePhoto();
		if(file != null){
			try {
				Image image = new Image(new FileInputStream(file));
				user.setUserImage(image);
				mainApp.getController().getDatabase().saveImage(file, mainApp.getController().getLoggedUser().getUserID());
			} catch (SQLException e) {
				System.err.println("ERROR WHILE SAVING USER PHOTO");
				e.printStackTrace();
			}
		}
	}
	
	private File chooseTheProfilePhoto(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Zmieñ zdjêcie profilowe");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("plik graficzny", "*.jpg", "*.png", "*.gif" )
		);
		File file = fileChooser.showSaveDialog(dialogStage);
		return file;
	}
			
	
}
