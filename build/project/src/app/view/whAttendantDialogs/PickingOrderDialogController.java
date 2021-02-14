package app.view.whAttendantDialogs;

import app.model.Order;
import app.model.Picking;
import app.model.Order.OrderStatus;
import app.model.Picking.PickingDetails;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

public class PickingOrderDialogController extends DialogBasedController {

	private ToggleGroup buttonGroup;
	@FXML
	private VBox productsPane;
	
	private Picking picking;
	
	private boolean pickingCompleted = false;
	
	@FXML
	private void initialize(){
		buttonGroup = new ToggleGroup();
	}
	
	@FXML
	private void handlePickProductClick(){
		Toggle selectedToggle = buttonGroup.getSelectedToggle();
		if(selectedToggle != null){
			PickingDetails pd = (PickingDetails) selectedToggle.getUserData();
			if(pd != null){
				showPickTheProductDialog(pd);
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak informacji", "Brak informacji o produkcie!", getCurrentStage()).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczenia", "Proszê zaznaczyæ produkt do skompletowania!", getCurrentStage()).showAndWait();
		}
	}
	
	private void showPickTheProductDialog(PickingDetails pd){
		PickTheProductDialogController controller = (PickTheProductDialogController) UtilityClass.showDialogFactory("view/whAttendantDialogs/PickTheProductDialog.fxml", dialogStage, mainApp, getCurrentStage(), "Kompletuj produkt");
		controller.setPickingDetails(pd);
		controller.setPicking(picking);
		controller.getCurrentStage().showAndWait();
		
		if(controller.isProductCompletelyPicked()){
			productsPane.getChildren().remove((ToggleButton) buttonGroup.getSelectedToggle());
		}
		
		if(checkIfPickingIsCompleted()) {
			boolean successfullyUpdated = mainApp.getController().getDatabase().changeOrderStatus(picking.getOrder().getOrderID(), OrderStatus.Carried_Out.toString());
			successfullyUpdated = mainApp.getController().getDatabase().deletePickingRecord(picking);
			if(successfullyUpdated) UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Zamówienie zosta³o skompletowane!", "Zamówienie zosta³o ca³kowicie skompletowane!", getCurrentStage()).showAndWait();
			else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d!", "Podczas aktualizacji statusu zamówienia wyst¹pi³ b³¹d!", getCurrentStage()).showAndWait();
			pickingCompleted = true;
			getCurrentStage().close();
		}
	}
	
	
	
	private void createToggleButton(PickingDetails pd){
		if(pd.getQuantityToBePicked() != 0){
			ToggleButton pickingDetailButton = new ToggleButton();
			VBox detailsInfo = new VBox();
			detailsInfo.setPadding(new Insets(10));
			detailsInfo.setSpacing(8);
			
			Label productLabel = new Label ("Nazwa: ");
			Label productNameLabel = new Label(pd.getProduct().getProductName());
			HBox productHBox = new HBox();
			productHBox.setSpacing(10);
			productHBox.getChildren().addAll(productLabel, productNameLabel);
			
			Label quantityLabel = new Label ("Iloœæ: ");
			Label quantityToBePickedLabel = new Label();
			HBox quantityHBox = new HBox();
			quantityHBox.setSpacing(10);
			quantityHBox.getChildren().addAll(quantityLabel, quantityToBePickedLabel);
			
			StringProperty quantityProperty = new SimpleStringProperty(String.valueOf(pd.getQuantityToBePicked()));
			Bindings.bindBidirectional(quantityProperty, pd.getQuantityToBePickedProperty(), new NumberStringConverter()) ;
			quantityToBePickedLabel.textProperty().bind(quantityProperty);
			
			detailsInfo.getChildren().addAll(productHBox, quantityHBox);
			
			pickingDetailButton.setUserData(pd);
			pickingDetailButton.setPrefWidth(270);
			pickingDetailButton.setPrefHeight(68);
			pickingDetailButton.setGraphic(detailsInfo);
			pickingDetailButton.setToggleGroup(buttonGroup);
			pickingDetailButton.getStyleClass().add("orderAvailable");
			productsPane.getChildren().add(pickingDetailButton);
		}
	}
	
	
	@FXML
	private void handleReturnClick(){
		this.getCurrentStage().close();
	}
	
	public void setPicking(Picking picking){
		this.picking = picking;
		if(picking != null) {
			for(PickingDetails pd : picking.getPickingList()){
				createToggleButton(pd);
			}
		}
	}
	
	private boolean checkIfPickingIsCompleted(){
		for(PickingDetails pd : picking.getPickingList())
			if(pd.getQuantityToBePicked() != 0) return false;
		
		return true;
	}
	
	public boolean isPickingCompleted(){
		return this.pickingCompleted;
	}
	
}
