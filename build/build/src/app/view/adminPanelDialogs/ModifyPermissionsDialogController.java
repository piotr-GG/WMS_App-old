package app.view.adminPanelDialogs;

import java.io.IOException;

import app.model.Permission;
import app.model.User;
import app.utility.UtilityClass;
import app.view.MyProfileOverviewController;
import app.view.inheritance.DialogBasedController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;

public class ModifyPermissionsDialogController extends DialogBasedController  {


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
	private ComboBox<Boolean> blockedAccountBox;
	@FXML
	private ComboBox<Boolean> mayEditBox;
	@FXML
	private ComboBox<Boolean> mayInsertBox;
	@FXML
	private ComboBox<Boolean> mayExecuteQueriesBox;
	@FXML
	private ComboBox<Boolean> hasAdminRightsBox;
	
	@FXML
	private void initialize(){
		userData = FXCollections.observableArrayList();
		initializeUserTable();
		
		ObservableList<Boolean> booleanData = FXCollections.observableArrayList(Boolean.TRUE, Boolean.FALSE);
		
		blockedAccountBox.getItems().addAll(booleanData);
		mayEditBox.getItems().addAll(booleanData);
		mayInsertBox.getItems().addAll(booleanData);
		mayExecuteQueriesBox.getItems().addAll(booleanData);
		hasAdminRightsBox.getItems().addAll(booleanData);
	}
	
	private void initializeUserTable(){
		userID.setCellValueFactory(cellData->cellData.getValue().getUserIDProperty().asObject());
		login.setCellValueFactory(cellData->cellData.getValue().getLoginProperty());
		password.setCellValueFactory(cellData->cellData.getValue().getPasswordProperty());
		userFirstName.setCellValueFactory(cellData->cellData.getValue().getEmployee().getFirstNameProperty());
		userLastName.setCellValueFactory(cellData->cellData.getValue().getEmployee().getLastNameProperty());
		departmentNameColumn.setCellValueFactory(cellData->cellData.getValue().getEmployee().getDepartment().getDepartmentNameProperty());
		
		userTable.setItems(userData);
		
		MenuItem showDetails = new MenuItem("Poka¿ szczegó³y");
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
		
		userTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>(){

			@Override
			public void changed(ObservableValue<? extends User> arg0, User arg1, User arg2) {
				if(arg2 != null){
					showPermissions(arg2.getPermission());
				}
				
			}
			
		});
	}
	
	public void showAllUsers(){
		ObservableList<User> result = FXCollections.observableArrayList();
		try {
			result = mainApp.getController().getDatabase().getUsers(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		userData.addAll(result);
	}
	
	private void showUserDetails(User user){
		if(user != null){
			
			MyProfileOverviewController controller = (MyProfileOverviewController) UtilityClass.showDialogFactory("view/MyProfileOverview.fxml", dialogStage, mainApp, ownerStage, "Dane u¿ytkownika");
			controller.getCurrentStage().initModality(Modality.WINDOW_MODAL);
			controller.setUser(user);
			controller.getCurrentStage().show();
			
		} else {
			UtilityClass.showAlert(AlertType.WARNING, "B³¹d", "Brak zaznaczenia", "Proszê zaznaczyæ u¿ytkownika" , getCurrentStage()).showAndWait();
		}
	}
		
	
	private void showPermissions(Permission p){
		if( p != null){
			blockedAccountBox.getSelectionModel().select(Boolean.valueOf(p.isBlockedAccount()));
			mayEditBox.getSelectionModel().select(Boolean.valueOf(p.isMayEdit()));
			mayInsertBox.getSelectionModel().select(Boolean.valueOf(p.isMayInsertData()));
			mayExecuteQueriesBox.getSelectionModel().select(Boolean.valueOf(p.isMayExecuteQueries()));
			hasAdminRightsBox.getSelectionModel().select(Boolean.valueOf(p.isHasAdminRights()));
		} 
	}
	
	@FXML
	private void modifyClick(){
		User user = userTable.getSelectionModel().getSelectedItem();
		if(user != null){
			boolean blockedAccount = blockedAccountBox.getSelectionModel().getSelectedItem();
			boolean mayEdit = mayEditBox.getSelectionModel().getSelectedItem();
			boolean mayInsert = mayInsertBox.getSelectionModel().getSelectedItem();
			boolean mayExecute = mayExecuteQueriesBox.getSelectionModel().getSelectedItem();
			boolean hasAdminRights = hasAdminRightsBox.getSelectionModel().getSelectedItem();
			Permission newPermission = new Permission(blockedAccount, mayEdit, mayInsert, mayExecute, hasAdminRights);
			boolean areEqual = Permission.arePermissionInstancesEqual(user.getPermission(), newPermission);
			if(!areEqual){
				boolean successfullyUpdated = mainApp.getController().getDatabase().updatePermissions(user.getUserID(), newPermission);
				if(successfullyUpdated){
					UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Pomyœlnie zaktualizowano uprawnienia", "Uprawnienia zosta³y pomyœlnie zaktualizowane", getCurrentStage()).showAndWait();
					user.setPermission(newPermission);
					showPermissions(newPermission);
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Wyst¹pi³ b³¹d", "Podczas aktualizowania uprawnieñ wyst¹pi³ b³¹d", getCurrentStage()).showAndWait();
				}
			} 
		} else {
			UtilityClass.showAlert(AlertType.WARNING, "B³¹d", "Brak zaznaczenia", "Proszê zaznaczyæ u¿ytkownika" , getCurrentStage()).showAndWait();
		}
	}
	
	@FXML
	private void getBackClick(){
		getCurrentStage().close();
	}
}
