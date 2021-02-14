package app.view;

import java.io.IOException;

import app.MainApp;
import app.utility.UtilityClass;
import app.view.adminPanelDialogs.ConfirmNewUsersDialogController;
import app.view.adminPanelDialogs.ModifyPermissionsDialogController;
import app.view.adminPanelDialogs.ShipmentSimulationDialogController;
import app.view.adminPanelDialogs.SimulationsOverviewController;
import app.view.adminPanelDialogs.StatisticsDialogController;
import app.view.adminPanelDialogs.UserTableOverviewController;
import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminPanelController extends DialogBasedController {

	private ToggleGroup rbsGroup;
	
	@FXML
	private RadioButton showUsersRB;
	@FXML 
	private RadioButton confirmRegistrationsRB;
	@FXML
	private RadioButton modifyPermissionsRB;
	@FXML
	private RadioButton statisticsRB;
	@FXML
	private RadioButton simulationsRB;
	@FXML
	private RadioButton shipmentSimRB;

	@FXML
	private void initialize(){
		rbsGroup = new ToggleGroup();
		showUsersRB.setToggleGroup(rbsGroup);
		confirmRegistrationsRB.setToggleGroup(rbsGroup);
		modifyPermissionsRB.setToggleGroup(rbsGroup);
		statisticsRB.setToggleGroup(rbsGroup);
		simulationsRB.setToggleGroup(rbsGroup);
		shipmentSimRB.setToggleGroup(rbsGroup);
		
	}

	@FXML
	private void handleExecuteClick(){
		if (showUsersRB.isSelected()) showUserTableOverview();
		if (confirmRegistrationsRB.isSelected()) showConfirmNewUsersDialog();
		if (modifyPermissionsRB.isSelected()) showModifyPermissionsDialog(); 
		if (statisticsRB.isSelected()) showStatisticsOverview(); 
		if(simulationsRB.isSelected()) showSimulationsOverview();
		if(shipmentSimRB.isSelected()) showShipmentSimDialog();
			
	}
	
	@FXML
	private void handleCancelClick(){
		getCurrentStage().close();
	}
	
	private void showConfirmNewUsersDialog(){
		ConfirmNewUsersDialogController controller = (ConfirmNewUsersDialogController) UtilityClass.showDialogFactory("view/adminPanelDialogs/ConfirmNewUsersDialog.fxml", dialogStage, mainApp, getCurrentStage(), "PotwierdŸ u¿ytkowników");
		controller.showUsersToBeConfirmed();
		controller.getCurrentStage().showAndWait();
	}
	
	private void showModifyPermissionsDialog(){
		ModifyPermissionsDialogController controller = (ModifyPermissionsDialogController) UtilityClass.showDialogFactory("view/adminPanelDialogs/ModifyPermissionsDialog.fxml", dialogStage, mainApp, getCurrentStage(), "Modyfikuj uprawnienia");
		controller.showAllUsers();
		controller.getCurrentStage().showAndWait();
	}
	
	
	private void showUserTableOverview(){
		UserTableOverviewController controller = (UserTableOverviewController) UtilityClass.showDialogFactory("view/adminPanelDialogs/UserTableOverview.fxml", dialogStage, mainApp, getCurrentStage(), "U¿ytkownicy");
		controller.showAllUsers();
		controller.getCurrentStage().showAndWait();
		
	}
		
	private void showStatisticsOverview(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/adminPanelDialogs/StatisticsDialogOverview.fxml"));
			AnchorPane statisticsScene = (AnchorPane) loader.load();
			
			Scene scene = new Scene(statisticsScene);
			dialogStage = new Stage();
			dialogStage.setScene(scene);
			dialogStage.setTitle("Statystyki programu ForkLift");
			
			//Nadanie dostêpu kontrolerowi do aplikacji
			StatisticsDialogController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setCurrentStage(dialogStage);
			controller.setOwnerStage(ownerStage);
			controller.setDefaultIcon();
			
			dialogStage.initOwner(ownerStage);
			dialogStage.initModality(Modality.NONE);
			dialogStage.show();
			
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void showSimulationsOverview(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/adminPanelDialogs/SimulationsOverview.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			Scene scene = new Scene(pane);
			dialogStage = new Stage();
			dialogStage.setScene(scene);
			dialogStage.setTitle("Symulacja pracy magazynu - ForkLift WMS");
			
			//Nadanie dostêpu kontrolerowi do aplikacji
			SimulationsOverviewController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setCurrentStage(dialogStage);
			controller.getDeliveries();
			
			dialogStage.initOwner(ownerStage);
			dialogStage.initModality(Modality.NONE);
			dialogStage.show();
			
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void showShipmentSimDialog(){
		ShipmentSimulationDialogController controller = (ShipmentSimulationDialogController) UtilityClass.showDialogFactory("view/adminPanelDialogs/ShipmentSimulationsDialog.fxml", dialogStage, mainApp, getCurrentStage(), "Symulacja magazynu - wysy³ki");
		controller.getShipmentData();
		controller.getCurrentStage().showAndWait();
		
	}
}
