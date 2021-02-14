package app.view;

import app.MainApp;
import app.model.Employee;
import app.model.Permission;
import app.model.User;
import app.utility.UtilityClass;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RegisterFoundEmployeeController {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private TableView<Employee> employeeTabView;
	@FXML
	private TableColumn<Employee, String> firstNameCol;
	@FXML
	private TableColumn<Employee, String> lastNameCol;
	@FXML
	private TableColumn<Employee, String> birthDateCol;
	
	@FXML
	private Button addEmployeeButton;
	
	private MainApp mainApp;
	private Stage dialogStage;
	private Stage ownerStage;
	
	
	private boolean okClicked = false;
	private ObservableList<Employee> employeeList;
	private User user;
	
	
	/**
	 * Sets the source of data for the table
	 * @param dialogData
	 */
	public void setEmployeeList(ObservableList<Employee> dialogData){
		this.employeeList = FXCollections.observableArrayList();
		this.employeeList.addAll(dialogData);
		insertDataIntoTabView();
		employeeTabView.setItems(employeeList);
	}
	
	/**
	 * Inserts data into the table showing employees with name and last name matching the required string
	 */
	public void insertDataIntoTabView(){
		firstNameCol.setCellValueFactory(cellData->cellData.getValue().getFirstNameProperty());
		lastNameCol.setCellValueFactory(cellData->cellData.getValue().getLastNameProperty());
		birthDateCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getBirthDate().toString()));
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
	
	
	public void setDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}
	
	public void setOwnerStage(Stage ownerStage){
		this.ownerStage = ownerStage;
	}
	
	
	
	public void setUser(User user){
		this.user = user;
	}
	
	@FXML
	private void handleAddEmployeeClick(){
		Employee selectedEmployee = employeeTabView.getSelectionModel().getSelectedItem();
		
		if(selectedEmployee != null){
			user.setEmployee(selectedEmployee);
			user.setPermission(Permission.getDefaultPermission());
			
			mainApp.getController().getDatabase().insertUserRecord(user);
			
			okClicked = true;
			
			dialogStage.close();
			ownerStage.close();
			
			//show information alert
			String contentText = "Poprawnie przypisano pracownika do u¿ytkownika.\n\n"
					+ "Konto u¿ytkownika przed zakoñczeniem rejestracji musi zostaæ potwierdzone przez administratora. Stosowna informacja odnoœnie procesu"
					+ "rejestracji zostanie przes³ana po wydaniu decyzji w sprawie utworzenia konta przez administratora.";
			UtilityClass.showAlert(AlertType.INFORMATION, "Przypisanie pracownika", null, contentText, dialogStage).showAndWait();
			mainApp.showRootOverview();
		} else {
			//Nothing selected
			UtilityClass.showAlert(AlertType.WARNING, "B³¹d zaznaczenia", "Nie wybrano pracownika", "Proszê wybraæ pracownika do przypisania w oknie tabeli", dialogStage).showAndWait();
		}
	}
	
	/**
	 * Informs if the user clicked OK
	 * @return
	 */
	
	public boolean isOkClicked(){
		return okClicked;
	}
	
	
}
