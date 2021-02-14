package app.view.adminPanelDialogs;

import java.io.IOException;
import java.util.Optional;

import app.MainApp;
import app.model.Employee;
import app.model.Permission;
import app.model.User;
import app.utility.UtilityClass;
import app.view.MyProfileOverviewController;
import app.view.inheritance.ActionController;
import app.view.inheritance.DialogBasedController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserTableOverviewController extends DialogBasedController {

	@FXML
	private TableView<User> userTable;
	@FXML
	private TableColumn <User, Integer> userID;
	@FXML
	private TableColumn <User, String> login;
	@FXML
	private TableColumn <User, String> password;
	@FXML
	private TableColumn <User, String> userFirstName;
	@FXML
	private TableColumn <User, String> userLastName;
	@FXML
	private TableColumn <User, String> departmentNameColumn;
	
	private ObservableList<User> userData;
	
	@FXML
	private void initialize(){
		userData = FXCollections.observableArrayList();
		initializeUserTable();
	}
	
	private void initializeUserTable(){
		userID.setCellValueFactory(cellData->cellData.getValue().getUserIDProperty().asObject());
		login.setCellValueFactory(cellData->cellData.getValue().getLoginProperty());
		password.setCellValueFactory(cellData->cellData.getValue().getPasswordProperty());
		userFirstName.setCellValueFactory(cellData->cellData.getValue().getEmployee().getFirstNameProperty());
		userLastName.setCellValueFactory(cellData->cellData.getValue().getEmployee().getLastNameProperty());
		departmentNameColumn.setCellValueFactory(cellData->cellData.getValue().getEmployee().getDepartment().getDepartmentNameProperty());
		
		userTable.setItems(userData);
	}
	
	
	/**
	 * Shows all the users in the database with option to edit their data
	 */
	public void showAllUsers(){
		ObservableList<User> result = FXCollections.observableArrayList();
		try {
			result = mainApp.getController().getDatabase().getUsers(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		userData.addAll(result);
	}
	
	@FXML
	private void handleShowDetailsClick(){
		User user = userTable.getSelectionModel().getSelectedItem();
		if(user != null){
			
			MyProfileOverviewController controller = (MyProfileOverviewController) UtilityClass.showDialogFactory("view/MyProfileOverview.fxml", dialogStage, mainApp, ownerStage, "Dane u�ytkownika");
			controller.getCurrentStage().initModality(Modality.WINDOW_MODAL);
			controller.setUser(user);
			controller.getCurrentStage().show();
			
		} else {
			UtilityClass.showAlert(AlertType.WARNING, "B��d", "Brak zaznaczenia", "Prosz� zaznaczy� u�ytkownika" , getCurrentStage()).showAndWait();
		}
	}
	
	@FXML
	private void handleDeleteUserClick(){
		User user = userTable.getSelectionModel().getSelectedItem();
		if(user != null){
			
			if(!user.equalsOtherUser(mainApp.getController().getLoggedUser())){
				Optional<ButtonType> input = UtilityClass.showAlert(AlertType.CONFIRMATION, "Komunikat", "Potwierdzenie" , "Prosz� potwierdzi� zamiar usuni�cia konta u�ytkownika", getCurrentStage()).showAndWait();
				if(input.get() == ButtonType.OK){
					boolean successfullyExecuted = mainApp.getController().getDatabase().deleteUser(user.getUserID());
					if(successfullyExecuted){
						UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Polecenie zako�czone pomy�lnie", "U�ytkownik zosta� pomy�lnie usni�ty!", getCurrentStage()).showAndWait();
						userData.remove(user);
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "B��d", "Podczas procedury usuni�cia u�ytkownika wyst�pi� b��d!", getCurrentStage()).showAndWait();
					}
				} 
			} else {
				UtilityClass.showAlert(AlertType.WARNING, "B��d", "Nie mo�na kontynuowa�", "Nie mo�na usun�� w�asnego konta u�ytkownika z tego poziomu" , getCurrentStage()).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.WARNING, "B��d", "Brak zaznaczenia", "Prosz� zaznaczy� u�ytkownika" , getCurrentStage()).showAndWait();
		}
	}
	
	@FXML
	private void handleOKClick(){
		getCurrentStage().close();
	}
	

	
}
