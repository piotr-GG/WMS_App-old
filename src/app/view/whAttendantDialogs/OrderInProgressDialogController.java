package app.view.whAttendantDialogs;

import app.model.Location;
import app.model.Order;
import app.model.OrderDetails;
import app.model.Picking;
import app.model.Picking.PickingDetails;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class OrderInProgressDialogController extends DialogBasedController {

	@FXML
	private TextField customerField;
	@FXML
	private TextField addressField;
	@FXML
	private TextField freightField;
	
	@FXML
	private TableView<OrderDetails> orderDetailsTable;
	@FXML
	private TableColumn<OrderDetails, String> productColumn;
	@FXML
	private TableColumn<OrderDetails, Integer> quantityColumn;
	@FXML
	private TableColumn<OrderDetails, String> suggestedLocationColumn;
	
	//Order to be shown
	private Picking picking;
	
	private Order order;
	private ObservableList<OrderDetails> orderData;
	
	
	public void setPicking(Picking picking){
		this.picking = picking;
		this.order = picking.getOrder();
		orderData = mainApp.getController().getDatabase().getOrderDetailsWithPickLocation(order.getOrderID());
		inputData();
	}
	
	private void inputData(){
		customerField.setText(order.getCustomer().getCompanyName());
		addressField.setText(order.getAddress());
		freightField.setText(String.valueOf(order.getFreight()));
		
		initializeColumns();
		orderDetailsTable.setItems(orderData);
	}
	
	private void initializeColumns(){
		productColumn.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductNameProperty());
		quantityColumn.setCellValueFactory(cellData->cellData.getValue().getQuantityProperty().asObject());
		suggestedLocationColumn.setCellValueFactory(cellData->cellData.getValue().getProduct().getSuggestedLocation().getLocationIDProperty());
		
	}
	
	@FXML
	private void handlePickClick(){
		PickingOrderDialogController controller = (PickingOrderDialogController) UtilityClass.showDialogFactory("view/whAttendantDialogs/PickingOrderDialog.fxml", dialogStage, mainApp, getCurrentStage(), "Kompletacja zamówienia");
		controller.setPicking(picking);
		controller.getCurrentStage().showAndWait();
		if(controller.isPickingCompleted()) getCurrentStage().close();
	}
	
	@FXML
	private void handleReturnClick(){
		getCurrentStage().close();
	}
}
