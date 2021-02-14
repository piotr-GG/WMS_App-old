package app.view.whAttendantDialogs;

import app.model.Employee;
import app.model.Order;
import app.model.Order.OrderStatus;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import app.model.OrderDetails;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class OrderDetailsDialogController extends DialogBasedController {

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
	
	//Order to be shown
	private Order order;
	private ObservableList<OrderDetails> orderData;
	
	private boolean orderHasBeenAssigned = false;
	
	@FXML
	private void initialize(){
		
	}
	
	public void setOrder(Order order){
		this.order = order;
		orderData = mainApp.getController().getDatabase().getOrderDetails(order.getOrderID());
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
		
	}
	
	@FXML
	private void handleAcceptOrderClick(){
		Employee employee = mainApp.getController().getLoggedUser().getEmployee();
		boolean checkIfEmployeeHasAlreadyPicking = mainApp.getController().getDatabase().checkIfEmployeeHasPicking(employee);
		if(checkIfEmployeeHasAlreadyPicking) UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Nie mo¿na kontynuowaæ", "Do profilu pracownika jest ju¿ przypisane zamówienie do kompletacji!", getCurrentStage()).showAndWait();
		else {
			boolean successfullyInserted = mainApp.getController().getDatabase().insertPickingRecord(employee, order);
			successfullyInserted = mainApp.getController().getDatabase().changeOrderStatus(order.getOrderID(), OrderStatus.In_Progress.toString());
			successfullyInserted = mainApp.getController().getDatabase().setPickEmployee(employee, order);
			order.setPickEmployee(employee);
			if(successfullyInserted){
				UtilityClass.showAlert(AlertType.INFORMATION, "Informacja", "Pomyœlnie przypisano zamówienie", "Zamówienie zosta³o przypisane do Pañskiego konta!", getCurrentStage()).showAndWait();
				orderHasBeenAssigned = true;
			}
			else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ nieoczekiwany b³¹d", "Podczas przypisywania zamówienia wyst¹pi³ nieoczekiwany b³¹d!", getCurrentStage());
			
			getCurrentStage().close();
		}
	}
	
	public boolean hasOrderBeenAssigned() {
		return this.orderHasBeenAssigned;
	}
	
	@FXML
	private void handleReturnClick(){
		getCurrentStage().close();
	}
}
