package app.view.whAttendantDialogs;

import java.util.Optional;

import app.model.Order;
import app.model.Order.OrderStatus;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CheckOrderDialogController extends DialogBasedController {
	
	@FXML
	private VBox ordersVBox;
	
	private ToggleGroup buttonGroup;
	
	private ObservableList<Order> orders;
	
	@FXML
	private void initialize(){
		buttonGroup = new ToggleGroup();
	}
	
	public void getOrderData(){
		orders = mainApp.getController().getDatabase().getOrdersByOrderStatus("Carried_Out");
		buttonGroup = new ToggleGroup();
		
		if(orders.isEmpty()){
			Label noOrdersAvailable = new Label("Brak zamówieñ");
			noOrdersAvailable.setFont(new Font("System", 18));
			ordersVBox.getChildren().add(noOrdersAvailable);
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
		
		Label shipLabel = new Label("Wysy³ka:");
		shipLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
		Label shipNameLabel = new Label("#" + o.getOrderID() + "   " + o.getShipName());
		shipNameLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
		HBox shipHBox = new HBox();
		shipHBox.setSpacing(15);
		shipHBox.getChildren().addAll(shipLabel, shipNameLabel);
		
		Label orderLabel = new Label("Data zamówienia:");
		orderLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
		Label orderDateLabel = new Label(o.getOrderDate().toString());
		orderDateLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
		HBox orderHBox = new HBox();
		orderHBox.setSpacing(15);
		orderHBox.getChildren().addAll(orderLabel, orderDateLabel);
		
		orderInfo.getChildren().addAll(shipHBox, orderHBox);
		orderButton.setUserData(o);
		
		orderButton.setPrefWidth(225);
		orderButton.setPrefHeight(68);
		orderButton.setGraphic(orderInfo);
		orderButton.setToggleGroup(buttonGroup);
		orderButton.getStyleClass().add("orderAvailable");
		ordersVBox.getChildren().add(orderButton);
	}
	
	
	@FXML
	private void handleConfirmClick(){
		Toggle selectedToggle = buttonGroup.getSelectedToggle();
		if(selectedToggle != null){
			Order order = (Order) selectedToggle.getUserData();
			if(order != null){
				getOrderControlled(order);
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d" , "Wyst¹pi³ b³¹d", "Wyst¹pi³ nieoczekiwany b³¹d!", getCurrentStage()).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d" , "Brak zaznaczenia", "Proszê zaznaczyæ zamówienie do kontroli!", getCurrentStage()).showAndWait();
		}
	}
	
	@FXML
	private void handleReturnClick(){
		getCurrentStage().close();
	}
	
	private void getOrderControlled(Order order){
		Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Komunikat", "PotwierdŸ kontrolê", "Proszê potwierdziæ kontrolê zamówienia", getCurrentStage()).showAndWait();
		if(result.get() == ButtonType.OK) {
			boolean successfullyUpdated = mainApp.getController().getDatabase().changeOrderStatus(order.getOrderID(), OrderStatus.Checked.toString());
			if(successfullyUpdated) {
				UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Zamówienie skontrolowane", "Zamówienie zosta³o skontrolowane", getCurrentStage()).showAndWait();
				removeTheOrder(order);
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d" , "Wyst¹pi³ b³¹d", "Wyst¹pi³ nieoczekiwany b³¹d!", getCurrentStage()).showAndWait();
			}
		}
	}
	
	private void removeTheOrder(Order order){
		for(Toggle t : buttonGroup.getToggles()){
			if((Order) t.getUserData() == order) {
				ToggleButton orderToggle = (ToggleButton) t;
				ordersVBox.getChildren().remove(orderToggle);
				return;
			}
		}
	}
}
