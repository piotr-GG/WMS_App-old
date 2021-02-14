package app.view.orderPanes;

import app.model.Order;
import app.model.OrderDetails;
import app.utility.UtilityClass;
import app.view.addNewDialogs.OrderDetailsInfoDialogController;
import app.view.inheritance.ActionController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

public class PickSuggestionsPaneController extends ActionController {

	
	/*
	 * Pick suggestions variables
	 */
	
	@FXML
	private TableView<Order> pickSuggTableView;
	@FXML
	private TableColumn <Order, Integer> pickSuggOrderIDColumn;
	@FXML
	private TableColumn <Order, String> pickSuggCustomerNameColumn;
	@FXML
	private TableColumn <Order, Double> pickSuggFreightColumn;
	@FXML
	private TableColumn <Order, String> pickSuggsuggestedShipDataColumn;
	@FXML
	private TableColumn <Order, String> pickSuggShipmentAddressColum;
	@FXML
	private GridPane pickSuggestionsGridPane;
	
	private ObservableList<OrderDetails> orderDetailsData;
    private ObservableList<Order> orderData;
    

	/*
	 * Pick suggestions event handlers
	 */
	
	@FXML
	private void issuePickSuggestions(){
		Order order = pickSuggTableView.getSelectionModel().getSelectedItem();
		boolean successfullyUpdated = changeOrderStatus(order, "Pick_Suggestions_Issued");
		if(successfullyUpdated) pickSuggTableView.getItems().remove(order);
	}
	
	@FXML
	private void getOrdersPickSuggestionsPane(){
		getOrdersWithPickSuggestionStatus();
	}
	
	private void getOrdersWithPickSuggestionStatus(){
		initializePickSuggestionTable();
		//Get orders whose status is "Accepted" which means that they are ready to have issued suggestions
		
		orderData = mainApp.getController().getDatabase().getOrdersByOrderStatus("Accepted");
		UtilityClass.showResultAlert(orderData.size(), currentStage);
		pickSuggTableView.setItems(orderData);
		
		pickSuggTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {
			@Override
			public void changed( ObservableValue observable, Order oldValue, Order newValue) {
				if(newValue != null) showPickSuggestions(newValue);
		}});
	}
	
	private void showPickSuggestions(Order order){
		pickSuggestionsGridPane.getChildren().clear();

		orderDetailsData = mainApp.getController().getDatabase().getOrderDetailsWithPickLocation(order.getOrderID());
		order.setOrderDetails(orderDetailsData);
		
		int row = pickSuggestionsGridPane.getRowConstraints().size();
		pickSuggestionsGridPane.setHgap(10);
		pickSuggestionsGridPane.setVgap(10);
		for(OrderDetails od : orderDetailsData){
			Label productLabel = new Label(od.getProduct().getProductName());
			Label quantityLabel = new Label(String.valueOf(od.getQuantity()));

			
			Label locationID = new Label(od.getProduct().getSuggestedLocation().getLocationID());
			
			
			pickSuggestionsGridPane.addRow(++row, productLabel, quantityLabel, locationID);
		}
	}
	
	private void initializePickSuggestionTable(){
		pickSuggOrderIDColumn.setCellValueFactory(cellData->cellData.getValue().getOrderIDProperty().asObject());
		pickSuggCustomerNameColumn.setCellValueFactory(cellData->cellData.getValue().getCustomer().getCompanyNameProperty());
		pickSuggFreightColumn.setCellValueFactory(cellData->cellData.getValue().getFreightProperty().asObject());
		pickSuggsuggestedShipDataColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getRequiredDate().toString()));
		pickSuggShipmentAddressColum.setCellValueFactory(cellData->cellData.getValue().getAddressProperty());
		
		MenuItem showOrderDetailsMenuItem = new MenuItem("Poka¿ szczegó³y");
		showOrderDetailsMenuItem.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				Order selectedItem = pickSuggTableView.getSelectionModel().getSelectedItem();
				if(selectedItem != null){
					OrderDetailsInfoDialogController controller = (OrderDetailsInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/OrderDetailsInfoDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y zamówienia");
					controller.setOrder(selectedItem);
					controller.getCurrentStage().show();
				}
			}
		});
		
		pickSuggTableView.setContextMenu(new ContextMenu(showOrderDetailsMenuItem));
	}
	
	private boolean changeOrderStatus(Order order, String orderStatus){
		if(order != null){
			boolean successfullyUpdated = mainApp.getController().getDatabase().changeOrderStatus(order.getOrderID(), orderStatus);
			if(successfullyUpdated){
				String contentText = "Status zamówienia o ID " + String.valueOf(order.getOrderID()) + " zosta³ pomyœlnie zaktualizowany";
				UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Aktualizacja statusu zamówienia", contentText, currentStage).showAndWait();
				return true;
				
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "B³¹d aktualizacji", "Takie zamówienia nie ma w bazie lub zosta³o przeniesione", currentStage).showAndWait();
				return false;
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "B³¹d zaznaczenia", "Brak zaznaczonego elementu w tabeli zamówieñ", currentStage).showAndWait();
			return false;
		}
	}
}
