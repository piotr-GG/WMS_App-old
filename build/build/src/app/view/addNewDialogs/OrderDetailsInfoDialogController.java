package app.view.addNewDialogs;

import app.model.Order;
import app.model.OrderDetails;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class OrderDetailsInfoDialogController extends DialogBasedController {

	@FXML
	private TextField customerField;
	@FXML
	private TextField addressField;
	@FXML
	private TextField freightField;
	@FXML
	private TextField orderStatusField;
	@FXML
	private DatePicker orderDatePicker;
	@FXML
	private DatePicker requiredDatePicker;
	
	@FXML
	private TableView<OrderDetails> orderDetailsTable;
	@FXML
	private TableColumn<OrderDetails, String> productColumn;
	@FXML
	private TableColumn<OrderDetails, Integer> quantityColumn;
	
	//Order to be shown
	private Order order;
	private ObservableList<OrderDetails> orderData;
	
	public void setOrder(Order order){
		this.order = order;
		orderData = mainApp.getController().getDatabase().getOrderDetails(order.getOrderID());
		inputData();
	}
	
	private void inputData(){
		customerField.setText(order.getCustomer().getCompanyName());
		addressField.setText(order.getAddress());
		freightField.setText(String.valueOf(order.getFreight()));
		orderStatusField.setText(order.getOrderStatus().getPolishVersion());
		orderDatePicker.setValue(order.getOrderDate());
		requiredDatePicker.setValue(order.getRequiredDate());
		
		initializeColumns();
		orderDetailsTable.setItems(orderData);
	}
	
	private void initializeColumns(){
		productColumn.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductNameProperty());
		quantityColumn.setCellValueFactory(cellData->cellData.getValue().getQuantityProperty().asObject());
		
		MenuItem showProductDetailsMenuItem = new MenuItem("Poka¿ szczegó³y produktu");
		showProductDetailsMenuItem.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				OrderDetails item = orderDetailsTable.getSelectionModel().getSelectedItem();
				if(item != null){
					ProductDetailedInfoDialogController controller = (ProductDetailedInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ProductDetailedInfoDialog.fxml", dialogStage, mainApp, getCurrentStage(), "Szczegó³y produktu");
					controller.setProduct(item.getProduct());
					controller.getCurrentStage().showAndWait();
				}	
			}
		});
		
		orderDetailsTable.setContextMenu(new ContextMenu(showProductDetailsMenuItem));
		
	}
	
	@FXML
	private void handleReturnClick(){
		getCurrentStage().close();
	}
}
