package app.view;

import java.io.IOException;

import app.MainApp;
import app.model.Employee;
import app.model.Picking;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import app.view.whAttendantDialogs.CheckOrderDialogController;
import app.view.whAttendantDialogs.GetOrdersDialogController;
import app.view.whAttendantDialogs.MoveTheStockDialogController;
import app.view.whAttendantDialogs.OrderInProgressDialogController;
import app.view.whAttendantDialogs.ShipTheShipmentDialogController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WarehouseAttendantPanelController extends DialogBasedController {
	
	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private RadioButton checkOrdersRB;
	@FXML
	private RadioButton myOrderInProgressRB;
	@FXML
	private RadioButton orderControlRB;
	@FXML
	private RadioButton shipmentReadyRB;
	@FXML
	private RadioButton moveTheCargoRB;
	@FXML
	private RadioButton shipTheShipmentRB;

	
	private ToggleGroup radioGroup;
	
	@FXML
	private void initialize(){
		radioGroup = new ToggleGroup();
		checkOrdersRB.setToggleGroup(radioGroup);
		myOrderInProgressRB.setToggleGroup(radioGroup);
		orderControlRB.setToggleGroup(radioGroup);
		shipmentReadyRB.setToggleGroup(radioGroup);
		moveTheCargoRB.setToggleGroup(radioGroup);
		shipTheShipmentRB.setToggleGroup(radioGroup);


	}
	
	public void initializeData(){
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
	private void handleExecuteClick(){
		if(checkOrdersRB.isSelected()) showGetOrdersDialog();
		if(myOrderInProgressRB.isSelected()) showOrderInProgessDialog();
		if(orderControlRB.isSelected()) showCheckOrderDialog();
		if(shipmentReadyRB.isSelected()) showGetShipmentReadyDialog();
		if(moveTheCargoRB.isSelected()) showMoveTheStockDialog();
		if(shipTheShipmentRB.isSelected()) showShipTheShipmentDialog();

		
			
	}
	
	@FXML
	private void handleReturnClick(){
			getCurrentStage().close();
	}
	
	private void showGetOrdersDialog(){
		GetOrdersDialogController controller = (GetOrdersDialogController) showDialogFactory("view/whAttendantDialogs/GetOrdersDialog.fxml");
		controller.getOrderData();
	}
	
	private void showGetShipmentReadyDialog(){
		 showDialogFactory("view/whAttendantDialogs/GetShipmentReadyDialog.fxml");

	}
	
	private void showMoveTheStockDialog(){
		MoveTheStockDialogController controller = (MoveTheStockDialogController) showDialogFactory("view/whAttendantDialogs/MoveTheStockDialog.fxml");
	}
	
	private void showCheckOrderDialog(){
		CheckOrderDialogController controller = (CheckOrderDialogController) showDialogFactory("view/whAttendantDialogs/CheckOrderDialog.fxml");
		controller.getOrderData();
	}
	
	private void showOrderInProgessDialog(){
		Employee employeeLogged = mainApp.getController().getLoggedUser().getEmployee();
		Picking pickingInProgress = mainApp.getController().getDatabase().getPickingInProgress(employeeLogged);
		if(pickingInProgress != null){
			OrderInProgressDialogController controller = (OrderInProgressDialogController) showDialogFactory("view/whAttendantDialogs/OrderInProgressDialog.fxml");
			controller.setPicking(pickingInProgress);
		} else {
			UtilityClass.showAlert(AlertType.WARNING, "Brak zamówienia", "Brak zamówienia w toku", "Nie znaleziono zamówienia w toku przypisanego do u¿ytkownika.", getCurrentStage()).showAndWait();
		}
	}
	
	private void showShipTheShipmentDialog(){
		ShipTheShipmentDialogController controller = (ShipTheShipmentDialogController) showDialogFactory("view/whAttendantDialogs/ShipTheShipmentDialog.fxml");
	}
	
	private DialogBasedController showDialogFactory(String loaderLocation){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(loaderLocation));
			AnchorPane pane = (AnchorPane) loader.load();
			
			Scene scene = new Scene(pane);
			dialogStage = new Stage();
			dialogStage.setScene(scene);
			dialogStage.setTitle("SprawdŸ zamówienia");
			dialogStage.getIcons().add(mainApp.getProgramIcon());

			//Nadanie dostêpu kontrolerowi do aplikacji
			DialogBasedController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setCurrentStage(dialogStage);
			controller.setOwnerStage(ownerStage);
			controller.setDefaultIcon();
			
			dialogStage.initOwner(ownerStage);
			dialogStage.initModality(Modality.NONE);
			dialogStage.show();
			
			return controller;
			
		} catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
}
