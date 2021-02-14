package app.view.whAttendantDialogs;

import java.io.IOException;

import app.MainApp;
import app.model.Order;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GetOrdersDialogController extends DialogBasedController {

	private ToggleGroup buttonGroup;
	@FXML
	private VBox ordersPane;
	
	private ObservableList<Order> orders;
	
	@FXML
	private void initialize(){
		
	}
	
	public void getOrderData(){
		orders = mainApp.getController().getDatabase().getOrdersByOrderStatus("Pick_Suggestions_Issued");
		buttonGroup = new ToggleGroup();
		
		if(orders.isEmpty()){
			Label noOrdersAvailable = new Label("Brak zamówieñ");
			noOrdersAvailable.setFont(new Font("System", 18));
			ordersPane.getChildren().add(noOrdersAvailable);
		} else {
			for(Order o:orders){
				createToggleButton(o);
			}	
		}
	}
	
	private void createToggleButton(Order o){
		ToggleButton orderButton = new ToggleButton();
		VBox orderInfo = new VBox();
		orderInfo.setPadding(new Insets(10));
		orderInfo.setSpacing(8);
		
		Label shipNameLabel = new Label("#" + o.getOrderID() + "   " + o.getShipName());
		shipNameLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
		Label orderDateLabel = new Label(o.getOrderDate().toString());
		orderDateLabel.setFont(Font.font("System", FontPosture.REGULAR, 14));
		
		orderInfo.getChildren().addAll(shipNameLabel, orderDateLabel);
		orderButton.setUserData(o);
		
		orderButton.setPrefWidth(225);
		orderButton.setPrefHeight(68);
		orderButton.setGraphic(orderInfo);
		orderButton.setToggleGroup(buttonGroup);
		orderButton.getStyleClass().add("orderAvailable");
		ordersPane.getChildren().add(orderButton);
	}
	@FXML
	private void handleSneakPeekClick(){
		Toggle toggle = buttonGroup.getSelectedToggle();
		if(toggle != null){
			Order selectedOrder = (Order) toggle.getUserData();
			if(selectedOrder != null){
				try{
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainApp.class.getResource("view/whAttendantDialogs/OrderDetailsDialog.fxml"));
					AnchorPane pane = (AnchorPane) loader.load();
					
					Scene scene = new Scene(pane);
					dialogStage = new Stage();
					dialogStage.setScene(scene);
					dialogStage.setTitle("Podgl¹d zamówienia");
					
					//Nadanie dostêpu kontrolerowi do aplikacji
					OrderDetailsDialogController controller = loader.getController();
					controller.setMainApp(mainApp);
					controller.setCurrentStage(dialogStage);
					controller.setOwnerStage(ownerStage);
					controller.setOrder(selectedOrder);
					controller.setDefaultIcon();
					
					dialogStage.initOwner(ownerStage);
					dialogStage.initModality(Modality.NONE);
					dialogStage.showAndWait();
					
					if(controller.hasOrderBeenAssigned()) removeTheAssignedOrder(selectedOrder);
				} catch(IOException e) {
					e.printStackTrace();
				}
				
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zamówienia", "Nie znaleziono podanego zamówienia!", getCurrentStage()).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczonego elementu", "Proszê nacisn¹æ na zamówienie, które ma zostaæ wyœwietlone", getCurrentStage()).showAndWait();
	
			}
	}
	
	private void removeTheAssignedOrder(Order order){
		for(Toggle t : buttonGroup.getToggles()){
			if((Order) t.getUserData() == order) {
				ToggleButton orderToggle = (ToggleButton) t;
				ordersPane.getChildren().remove(orderToggle);
				return;
			}
		}
	}
	
	@FXML
	private void handleReturnClick(){
		this.getCurrentStage().close();
	}
}
