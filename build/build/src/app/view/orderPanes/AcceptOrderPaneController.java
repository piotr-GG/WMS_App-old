package app.view.orderPanes;

import app.model.Order;
import app.utility.UtilityClass;
import app.view.addNewDialogs.OrderDetailsInfoDialogController;
import app.view.inheritance.ActionController;
import app.view.whAttendantDialogs.OrderDetailsDialogController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;

public class AcceptOrderPaneController extends ActionController {

	@FXML
	private TableView<Order> accpOrdTable;
	@FXML
	private TableColumn <Order, Integer> accpOrdOrderIDColumn;
	@FXML
	private TableColumn <Order, String> accpOrdCustomerNameColumn;
	@FXML
	private TableColumn <Order, Double> accpOrdFreightColumn;
	@FXML
	private TableColumn <Order, String> accpOrdsuggestedShipDataColumn;
	@FXML
	private TableColumn <Order, String> accpOrdShipmentAddressColum;
	@FXML
	private RadioButton accpOrdShowOrderByIDRB;
	@FXML
	private TextField accpOrdShowOrderByIDField;
	
	private ObservableList<Order> orderData;

	private void initializeAcceptOrderTable(){
		orderData = FXCollections.observableArrayList();
		accpOrdTable.setItems(orderData);
		accpOrdOrderIDColumn.setCellValueFactory(cellData->cellData.getValue().getOrderIDProperty().asObject());
		accpOrdCustomerNameColumn.setCellValueFactory(cellData->cellData.getValue().getCustomer().getCompanyNameProperty());
		accpOrdFreightColumn.setCellValueFactory(cellData->cellData.getValue().getFreightProperty().asObject());
		accpOrdsuggestedShipDataColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getRequiredDate().toString()));
		accpOrdShipmentAddressColum.setCellValueFactory(cellData->cellData.getValue().getAddressProperty());
		
		MenuItem showOrderDetailsMenuItem = new MenuItem("Poka¿ szczegó³y");
		showOrderDetailsMenuItem.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				Order selectedItem = accpOrdTable.getSelectionModel().getSelectedItem();
				if(selectedItem != null){
					OrderDetailsInfoDialogController controller = (OrderDetailsInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/OrderDetailsInfoDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y zamówienia");
					controller.setOrder(selectedItem);
					controller.getCurrentStage().show();
				}
			}
		});
		
		accpOrdTable.setContextMenu(new ContextMenu(showOrderDetailsMenuItem));
	}
	

	@FXML
	private void getOrdersAcceptOrderPane(){
		getOrdersWithInsertedStatus();
	}
	@FXML
	private void acceptOrder(){
		Order order = accpOrdTable.getSelectionModel().getSelectedItem();
		boolean successfullyUpdated = changeOrderStatus(order, "Accepted");
		if(successfullyUpdated) accpOrdTable.getItems().remove(order);
	}
	
	@FXML
	private void rejectOrder(){
		Order order = accpOrdTable.getSelectionModel().getSelectedItem();
		boolean successfullyUpdated = changeOrderStatus(order, "Rejected");
		if(successfullyUpdated) accpOrdTable.getItems().remove(order);
	}
	

	private void getOrdersWithInsertedStatus(){
		initializeAcceptOrderTable();
		orderData = mainApp.getController().getDatabase().getOrdersByOrderStatus("Inserted");
		UtilityClass.showResultAlert(orderData.size(), currentStage);
		accpOrdTable.setItems(orderData);
		
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
