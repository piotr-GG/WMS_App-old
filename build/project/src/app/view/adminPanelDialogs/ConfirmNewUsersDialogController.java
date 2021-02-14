package app.view.adminPanelDialogs;

import java.util.Optional;

import app.model.DeliveryDetails;
import app.model.User;
import app.utility.UtilityClass;
import app.view.MyProfileOverviewController;
import app.view.inheritance.DialogBasedController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

public class ConfirmNewUsersDialogController extends DialogBasedController {

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
		
		MenuItem showDetails = new MenuItem("Poka� szczeg�y");
		showDetails.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent t) {
		    	if(!userData.isEmpty()){
		        User item = userData.get(userTable.getSelectionModel().getSelectedIndex());
		        if (item != null){ 
		            showUserDetails(item);
		        }
		    }
		  }
		});
		
		userTable.setContextMenu(new ContextMenu(showDetails));
	}
	
	
	public void showUsersToBeConfirmed(){
		ObservableList<User> result = mainApp.getController().getDatabase().getUsersToBeConfirmed();
		userData.addAll(result);
	}
	
	private void showUserDetails(User user){
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
	private void handleConfirmUser(){
		User user = userTable.getSelectionModel().getSelectedItem();
		if(user != null){
			Optional<ButtonType> input = UtilityClass.showAlert(AlertType.CONFIRMATION, "Komunikat", "Potwierdzenie potwierdzenia" , "Prosz� potwierdzi� zamiar potwierdzenia konta u�ytkownika", getCurrentStage()).showAndWait();
			if(input.get() == ButtonType.OK){
				boolean successfullyExecuted = mainApp.getController().getDatabase().confirmUser(user.getUserID());
				if(successfullyExecuted){
					UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Polecenie zako�czone pomy�lnie", "U�ytkownik zosta� pomy�lnie potwierdzony!", getCurrentStage()).showAndWait();
					userData.remove(user);
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "B��d", "Podczas procedury potwierdzenia u�ytkownika wyst�pi� b��d!", getCurrentStage()).showAndWait();
				}
			} 
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Brak zaznaczenia", "Prosz� zaznaczy� u�ytkownika do potwierdzenia", getCurrentStage()).showAndWait();
		}
	}
	
	@FXML
	private void handleRejectUser(){
		User user = userTable.getSelectionModel().getSelectedItem();
		if(user != null){
			Optional<ButtonType> input = UtilityClass.showAlert(AlertType.CONFIRMATION, "Komunikat", "Potwierdzenie potwierdzenia" , "Prosz� potwierdzi� zamiar potwierdzenia konta u�ytkownika", getCurrentStage()).showAndWait();
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
			UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Brak zaznaczenia", "Prosz� zaznaczy� u�ytkownika do odrzucenia", getCurrentStage()).showAndWait();
		}
	}
	
	@FXML
	private void handleGetBackClick(){
		getCurrentStage().close();
	}
	
}
