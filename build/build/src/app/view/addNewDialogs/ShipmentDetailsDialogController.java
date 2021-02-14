package app.view.addNewDialogs;

import app.model.Shipment;
import app.model.ShipmentDetails;
import app.view.inheritance.DialogBasedController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ShipmentDetailsDialogController extends DialogBasedController {
	
	@FXML
	private TextField shipmentIDField;
	@FXML
	private TextField shipperField;
	@FXML
	private TextField customerField;
	@FXML
	private TextField fullAddressPart1Field;
	@FXML
	private TextField fullAddressPart2Field;
	@FXML
	private TextField shippedDateField;
	@FXML
	private TextField shippedToNameField;
	@FXML
	private TextField shipmentStatusField;
	
	@FXML
	private TableView <ShipmentDetails> shipDetailsTable;
	@FXML
	private TableColumn <ShipmentDetails, Integer> orderIDColumn;
	@FXML
	private TableColumn <ShipmentDetails, Integer> palletsCountColumn;
	@FXML
	private TableColumn <ShipmentDetails, String> orderDateColumn;
	@FXML
	private TableColumn <ShipmentDetails, String> requiredDateColumn;
	
	private ObservableList<ShipmentDetails> shipDetailsData;
	
	private Shipment shipment;
	
	@FXML
	private void initialize(){
		initializeShipDetailsTable();
	}
	
	private void initializeShipDetailsTable(){
		orderIDColumn.setCellValueFactory(cellData->cellData.getValue().getOrder().getOrderIDProperty().asObject());
		palletsCountColumn.setCellValueFactory(cellData->cellData.getValue().getPalletsCountProperty().asObject());
		orderDateColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getOrder().getOrderDate().toString()));
		requiredDateColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getOrder().getRequiredDate().toString()));
		
		shipDetailsData = FXCollections.observableArrayList();
		shipDetailsTable.setItems(shipDetailsData);
	}
	
	public void setShipment(Shipment shipment){
		this.shipment = shipment;
	}
	
	public void fillTheFields(){
		shipmentIDField.setText(String.valueOf(shipment.getShipmentID()));
		shipperField.setText(shipment.getShipper().getCompanyName());
		customerField.setText(shipment.getCustomer().getCompanyName());
		
		String fullAddressText = shipment.getAddress() + " , " + shipment.getCity();
		fullAddressPart1Field.setText(fullAddressText);
		
		fullAddressText = shipment.getPostalCode() + " , " + shipment.getCountry();
		fullAddressPart2Field.setText(fullAddressText);
		shippedDateField.setText(shipment.getShippedDate().toString());
		shippedToNameField.setText(shipment.getShippedName());
		shipmentStatusField.setText(shipment.getShipmentStatus().getPolishVersion());
		
		if(shipment.getShipmentDetails() != null) shipDetailsData.addAll(shipment.getShipmentDetails());
		
	}
	
	@FXML
	private void handleOKClick(){
		getCurrentStage().close();
	}
}
