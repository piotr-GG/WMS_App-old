package app.view.addNewDialogs;

import app.model.DeliveryDetails;
import app.model.Location;
import app.model.Product;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class LocationDetailsDialogController extends DialogBasedController {

	@FXML
	private TextField locationIDField;
	@FXML
	private TextField acceptableLoadField;
	@FXML
	private TextField acceptableVolumeField;
	@FXML
	private TableView<Product> productsStoredTable;
	@FXML
	private TableColumn<Product, String> productNameColumn;
	@FXML
	private TableColumn<Product, Integer> productIDColumn;
	@FXML
	private TableColumn<Product, Integer> quantityStoredColumn;
	
	private ObservableList<Product> productsStoredData;
	private Location location;
	
	private void initializeData(){
		locationIDField.setText(location.getLocationID());
		acceptableLoadField.setText(String.valueOf(location.getAcceptableLoad()));
		acceptableVolumeField.setText(String.valueOf(location.getAcceptableVolume()));
		
		productNameColumn.setCellValueFactory(cellData->cellData.getValue().getProductNameProperty());
		productIDColumn.setCellValueFactory(cellData->cellData.getValue().getProductIDProperty().asObject());
		quantityStoredColumn.setCellValueFactory(cellData->cellData.getValue().getUnitsInStockProperty().asObject());
		
		productsStoredData = FXCollections.observableArrayList();
		if(location.getProductsStored() != null) productsStoredData.addAll(location.getProductsStored());
		
		productsStoredTable.setItems(productsStoredData);
	}
	
	public void setLocation(Location location){
		this.location = location;
		if(location != null) initializeData();
	}
	
	@FXML
	private void handleOKClick(){
		getCurrentStage().close();
	}
	
}
