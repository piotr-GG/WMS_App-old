package app.view.adminPanelDialogs;

import java.time.LocalDate;

import app.model.Order.OrderStatus;
import app.model.Shipment;
import app.model.ShippingInfo;
import app.model.Shipment.ShipmentStatus;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ShipmentSimulationDialogController extends DialogBasedController  {
	
	@FXML
	private TableView<Shipment> shipmentInfoTable;
	@FXML
	private TableColumn<Shipment, Integer> shipmentIDColumn;
	@FXML
	private TableColumn<Shipment, String> shipperNameColumn;
	@FXML
	private TableColumn<Shipment, Integer> shipperIDColumn;
	@FXML
	private TableColumn<Shipment, Integer> palletCountColumn;
	@FXML
	private TableColumn<Shipment, String> customerIDColumn;
	@FXML
	private TableColumn<Shipment, String> customerNameColumn;
	@FXML
	private TableColumn<Shipment, String> shipmentStatusColumn;
	
	private ObservableList<Shipment> shipmentData;
	
	@FXML
	private DatePicker arrivalDateDatePicker;
	@FXML
	private TextField distanceCoveredField;
	@FXML
	private TextField shipmentDurationField;

	@FXML
	private void initialize(){
		arrivalDateDatePicker.setValue(LocalDate.now());
		arrivalDateDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>(){
			@Override
			public void changed(ObservableValue<? extends LocalDate> arg0, LocalDate oldDate, LocalDate newDate) {
				if(newDate != null && newDate.isBefore(LocalDate.now())){
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ!", "Data przybycia musi byæ równa lub wiêksza od dzisiejszej daty!", getCurrentStage()).showAndWait();
					newDate = oldDate;
				}
			}
			
		});
	}
	
	public void getShipmentData(){
		initializeShipmentInfoTable();
		ObservableList <Shipment> result = mainApp.getController().getDatabase().getShipmentsWithStatus(ShipmentStatus.Shipped.toString());
		if(result != null && !result.isEmpty()){
			shipmentData.addAll(result);
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Symulacja wysy³ek", "Brak wysy³ek", "Nie znaleziono ¿adnych wysy³ek mo¿liwych do edycji!", getCurrentStage()).show();
		}
	}
	
	@FXML
	private void handleAddDataClick(){
		Shipment shipment = shipmentInfoTable.getSelectionModel().getSelectedItem();
		if(shipment != null){
			if(isInputValid()){
				
				LocalDate arrivalDate = arrivalDateDatePicker.getValue();
				int distanceCovered = Integer.parseInt(distanceCoveredField.getText());
				double shipmentDuration = Double.parseDouble(shipmentDurationField.getText());
				shipment.setShipmentStatus(ShipmentStatus.Arrived.toString());
				
				ShippingInfo si = new ShippingInfo(shipment, arrivalDate, distanceCovered, shipmentDuration);
				boolean successfullyInserted = mainApp.getController().getDatabase().insertShippingInfoRecord(si);
				
				if(successfullyInserted){
					
					boolean successfullyUpdated = mainApp.getController().getDatabase().updateShipmentStatus(shipment, OrderStatus.Shipped.toString());
					
					if(successfullyUpdated){
						UtilityClass.showAlert(AlertType.INFORMATION, "Sukces", "Pomyœlnie dodano rekord", "Rekord zosta³ pomyœlnie dodany!", getCurrentStage()).showAndWait();
						shipmentData.remove(shipment);
						
						clearTheFields();
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d", "Podczas aktualizacji statusu wysy³ki wyst¹pi³ nieoczekiwany b³¹d!", getCurrentStage()).showAndWait();
					}
					
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d", "Podczas dodawania rekordu wyst¹pi³ nieoczekiwany b³¹d!", getCurrentStage()).showAndWait();
				}
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczenia", "Proszê zaznaczyæ wysy³kê", getCurrentStage()).showAndWait();
		}
	}
	
	@FXML
	private void handleExitClick(){
		getCurrentStage().close();
	}
	
	private boolean isInputValid(){
		String errMsg = "";
		if(arrivalDateDatePicker.getValue() == null) errMsg += "Brak daty przybycia!\n";
		else if (arrivalDateDatePicker.getValue().isBefore(LocalDate.now())) errMsg += "Data przybycia musi byæ wiêksza lub równa dacie dzisiejszej!\n";
	
		if(distanceCoveredField.getText().length() == 0) errMsg += "Brak informacji o przebytej drodze!\n";
		else if (!UtilityClass.isParsable(distanceCoveredField.getText())) errMsg += "Przebyta droga musi byæ liczb¹ ca³kowit¹!\n";
		if(shipmentDurationField.getText().length() == 0) errMsg += "Brak informacji o czasie trwania przewozu\n";
		else if (!UtilityClass.isDoubleParsable(shipmentDurationField.getText())) errMsg += "Czas trwania przewozu musi byæ liczb¹!\n";
		
		if(errMsg.length() == 0) return true;
		else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Napotkano poni¿sze b³êdy:", errMsg, getCurrentStage()).showAndWait();
			return false;
		}
	}
	
	private void initializeShipmentInfoTable(){
		shipmentIDColumn.setCellValueFactory(cellData->cellData.getValue().getShipmentIDProperty().asObject());
		shipperNameColumn.setCellValueFactory(cellData->cellData.getValue().getShipper().getCompanyNameProperty());
		shipperIDColumn.setCellValueFactory(cellData->cellData.getValue().getShipper().getShipperIDProperty().asObject());
		palletCountColumn.setCellValueFactory(cellData->cellData.getValue().getPalletsCountProperty().asObject());
		customerIDColumn.setCellValueFactory(cellData->cellData.getValue().getCustomer().getCustomerIDProperty());
		customerNameColumn.setCellValueFactory(cellData->cellData.getValue().getCustomer().getCompanyNameProperty());
		shipmentStatusColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getShipmentStatus().toString()));
		
		shipmentData = FXCollections.observableArrayList();
		shipmentInfoTable.setItems(shipmentData);
	}
	
	private void clearTheFields(){
		arrivalDateDatePicker.setValue(LocalDate.now());
		distanceCoveredField.setText("");
		shipmentDurationField.setText("");
	}
}
