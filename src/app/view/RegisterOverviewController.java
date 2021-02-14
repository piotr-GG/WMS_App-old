package app.view;

import app.MainApp;
import app.model.Employee;
import app.model.User;
import app.utility.UtilityClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class RegisterOverviewController {

	private MainApp mainApp;
	private Stage dialogStage;
	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private TextField nameTF;
	@FXML
	private TextField surnameTF;
	@FXML
	private TextField userNameTF;
	@FXML
	private PasswordField passwordPF;
	@FXML
	private PasswordField repeatPasswordPF;
	@FXML
	private Button registerButton;
	

	public Stage getDialogStage(){
		return this.dialogStage;
	}
	
	public void setDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		
		updateCSSStyles();
		mainApp.getController().getStyleSheetPathProperty().addListener(changeListener -> {
			updateCSSStyles();
		});
	}
	
	private void updateCSSStyles(){
		mainPane.getStylesheets().clear();
		mainPane.getStylesheets().add(mainApp.getController().getStyleSheetPath());
	}

	@FXML
	private void handleRegisterButton(){
		if(isInputValid()){
		//User part
		String userName = userNameTF.getText();
		String password = passwordPF.getText();
		
		boolean checkIfUserExits = mainApp.getController().getDatabase().checkIfUserExits(userName);
		if(!checkIfUserExits){
		//Employee part
		String name = nameTF.getText();
		String lastName = surnameTF.getText();
		
		int nextUserID = mainApp.getController().getDatabase().getNextUserID();
		User newUser = new User(nextUserID, userName, password, null, null, null);
		
		int countEmployeesWithoutUser = mainApp.getController().getDatabase().getCountEmployeesWithoutUser(name, lastName);
		if(countEmployeesWithoutUser != 0 ){
			ObservableList<Employee> dialogData = mainApp.getController().getDatabase().getAllEmployeesToAdd(name, lastName);
			mainApp.showRegisterFoundEmployeeDialog(dialogData, newUser, dialogStage);
		} else 
		{
			Employee newEmployee = new Employee();
			newEmployee.setFirstName(name);
			newEmployee.setLastName(lastName);
			
			newUser.setEmployee(newEmployee);
			
			mainApp.showRegisterNewEmployeeView(newUser, dialogStage);
		}
		}
	 else {
		UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d rejestracji", "U¿ytkownik o podanym loginie ju¿ istnieje w bazie danych!", dialogStage).showAndWait();
	}
		}
		
	}
	
	private boolean isInputValid(){
		String errMsg = "";
		if(userNameTF.getText() == null || userNameTF.getText().length() == 0) errMsg += "Brak wype³nionego pola Nazwa u¿ytkownika\n";
		if (passwordPF.getText() == null || passwordPF.getText().length() == 0)  errMsg += "Brak wype³nionego pola Has³o\n";
		if (repeatPasswordPF.getText() == null || repeatPasswordPF.getText().length() == 0)  errMsg += "Brak wype³nionego pola Powtórz has³o\n";
		if (!(passwordPF.getText().equals(repeatPasswordPF.getText()))) errMsg += "Podane has³a siê ró¿ni¹!\n";
		if (nameTF.getText() == null || nameTF.getText().length() == 0 ) errMsg += "Brak wype³nionego pola Imiê\n";
		if (surnameTF.getText() == null || surnameTF.getText().length() == 0) errMsg += "Brak wype³nionego pola Nazwisko\n";
		
		if(errMsg.length() == 0) return true;
		else {
			
			UtilityClass.showAlert(AlertType.ERROR, "Niepoprawne pola", "Proszê poprawiæ b³êdy w nastêpuj¹cych polach", errMsg, mainApp.getDialogStage()).showAndWait();
			return false;
		}
	}
	

}
