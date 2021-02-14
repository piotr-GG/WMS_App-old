package app.view.orderPanes;

import java.time.LocalDate;
import java.util.Optional;

import app.model.Customer;
import app.model.Order;
import app.model.Shipment;
import app.model.ShipmentDetails;
import app.model.Shipper;
import app.model.Order.OrderStatus;
import app.utility.UtilityClass;
import app.view.addNewDialogs.OrderDetailsInfoDialogController;
import app.view.inheritance.ActionController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

public class CreateShipmentPaneController extends ActionController {

	@FXML
	private TableView<Order> createShipTableView;
	@FXML
	private TableColumn <Order, Integer> createShipOrderID;
	@FXML
	private TableColumn <Order, String> createShipCustomerNameColumn;
	@FXML
	private TableColumn <Order, Double> createShipFreightColumn;
	@FXML
	private TableColumn <Order, String> createShipSuggestedShipDataColumn;
	@FXML
	private TableColumn <Order, String> createShipShipmentAddressColumn;
	@FXML
	private GridPane createShipDetailsPane;
	@FXML
	private Label createShipFreightLabel;
	@FXML
	private Label createShipVolumeLabel;
	@FXML
	private Label createShipPalletLabel;
	@FXML
	private Label createShipShipmentAddressLabel;
	@FXML
	private Label createShipShipmentCityLabel;
	@FXML
	private Label createShipShipmentPostalCodeLabel;
	@FXML
	private Label createShipShipmentCountryLabel;
	
	@FXML
	private ComboBox<String> createShipShipperBox;
	@FXML
	private TextField commonCustomerNameField;
	
	private ObservableList<Order> createShipTableData;
	
	boolean isCreateShipTableInitiliazed = false;
	
	private SimpleStringProperty commonShipmentAddress;
	private SimpleStringProperty commonShipmentCity;
	private SimpleStringProperty commonPostalCode;
	private SimpleStringProperty commonCountry;
	private Customer commonCustomer;

	@FXML
	private void initialize(){
		initializeCreateShipmentTable();
	}
	
	private void initializeCreateShipmentTable(){
		createShipOrderID.setCellValueFactory(cellData->cellData.getValue().getOrderIDProperty().asObject());
		createShipCustomerNameColumn.setCellValueFactory(cellData->cellData.getValue().getCustomer().getCompanyNameProperty());
		createShipFreightColumn.setCellValueFactory(cellData->cellData.getValue().getFreightProperty().asObject());
		createShipSuggestedShipDataColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getRequiredDate().toString()));
		createShipShipmentAddressColumn.setCellValueFactory(cellData->cellData.getValue().getAddressProperty());
		isCreateShipTableInitiliazed = true;
		
		createShipTableData = FXCollections.observableArrayList();
		createShipTableView.setItems(createShipTableData);
		initializeCommonProperties();
		
		MenuItem showOrderDetailsMenuItem = new MenuItem("Poka¿ szczegó³y");
		showOrderDetailsMenuItem.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				Order selectedItem = createShipTableView.getSelectionModel().getSelectedItem();
				if(selectedItem != null){
					OrderDetailsInfoDialogController controller = (OrderDetailsInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/OrderDetailsInfoDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y zamówienia");
					controller.setOrder(selectedItem);
					controller.getCurrentStage().show();
				}
			}
		});
		
		createShipTableView.setContextMenu(new ContextMenu(showOrderDetailsMenuItem));
	}

	private void initializeCommonProperties(){
    	commonShipmentAddress = new SimpleStringProperty("");
    	commonShipmentCity = new SimpleStringProperty("");
    	commonCountry = new SimpleStringProperty(""); 
    	commonPostalCode = new SimpleStringProperty("");
    	commonCustomer = new Customer();

		createShipShipmentAddressLabel.textProperty().bind(commonShipmentAddress);
		createShipShipmentCityLabel.textProperty().bind(commonShipmentCity);
		createShipShipmentCountryLabel.textProperty().bind(commonCountry);
		createShipShipmentPostalCodeLabel.textProperty().bind(commonPostalCode);

	}

	/*
	 * Create shipment event handlers
	 */
	
	@FXML
	private void addOrder(){
		Optional<String> result = UtilityClass.showTextInputDialog("Komunikat", "Podaj wartoœæ", "Proszê podaæ identyfikator zamówienia: ", currentStage);
		if(result.isPresent()){
			boolean isParsable = UtilityClass.isParsable(result.get());
			if(isParsable){
				int orderID = Integer.parseInt(result.get());
				Order order = mainApp.getController().getDatabase().getOrderByID(orderID);
				if(order != null && order.getOrderStatus().equals(OrderStatus.Shipment_Ready)){
					
					if(createShipShipperBox.getItems() == null || createShipShipperBox.getItems().isEmpty()) getShippers();
					
					if(createShipTableData.isEmpty()){
						commonShipmentAddress.set(order.getAddress());
						commonCountry.set(order.getCountry());
						commonShipmentCity.set(order.getCity());
						commonPostalCode.set(order.getPostalCode());
						commonCustomer = order.getCustomer();
						createShipTableData.add(order);
					}
					else {
						if(checkShipmentData(order.getAddress(), order.getCountry(), order.getCity(), order.getPostalCode(), order.getCustomer())){
							
							if(!containsOrder(createShipTableData, order)) createShipTableData.add(order); 
							else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Zamówienie jest ju¿ w tabeli", "\nProszê trochê pomyœleæ i nie dodawaæ drugi raz tego samego zamówienia do wysy³ki\n", currentStage).showAndWait();
							
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Adresy wysy³ki siê ró¿ni¹", "Wszystkie zamówienia wchodz¹ce w sk³ad wysy³ki musz¹ posiadaæ ten sam adres wysy³ki oraz tego samego klienta", currentStage).showAndWait();
						}
					}
					
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zamówienia", "W bazie danych nie istnieje zamówienie o podanym ID lub zamówienie nie jest gotowe do wysy³ki", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Proszê podaæ liczbê ca³kowit¹ odpowiadaj¹c¹ identyfikatorowi zamówienia", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Proszê podaæ liczbê ca³kowit¹ odpowiadaj¹c¹ identyfikatorowi zamówienia", currentStage).showAndWait();
		}
	}
	
	
	private boolean checkShipmentData(String address, String country, String city, String postalCode, Customer customer){
		if(!commonShipmentAddress.get().equals(address)) return false;
		if(!commonCountry.get().equals(country)) return false;
		if(!commonShipmentCity.get().equals(city)) return false;
		if(!commonPostalCode.get().equals(postalCode)) return false;
		if(!customer.areCustomersEqual(commonCustomer)) return false;
		return true;
	}
	
	@FXML
	private void createShipment(){
		ObservableList<Order> ordersIncluded = createShipTableView.getItems();
		if(!ordersIncluded.isEmpty()){
			String shipperName = createShipShipperBox.getSelectionModel().getSelectedItem();
			if(shipperName != null){
				
				int nextShipmentID = mainApp.getController().getDatabase().getNextShipmentID();
				Shipper shipper = mainApp.getController().getDatabase().getShipperByName(shipperName);
				int palletsCount = 5;
				String shippedAddress = commonShipmentAddress.get();
				String shippedCity = commonShipmentCity.get();
				String shippedPostalCode = commonPostalCode.get();
				String shippedCountry = commonCountry.get();
				ObservableList<ShipmentDetails>  shipmentDetails = FXCollections.observableArrayList();
				
				for(Order o : ordersIncluded){
					ShipmentDetails sd = new ShipmentDetails(nextShipmentID, o, 5);
					shipmentDetails.add(sd);
				}
				
				Shipment shipment = new Shipment(nextShipmentID, shipper, commonCustomer, palletsCount, shippedAddress, commonCustomer.getCompanyName(), shippedCity, shippedPostalCode, shippedCountry, LocalDate.now(), shipmentDetails, "Awaits_Shipment");
				boolean succesfullyExecuted = mainApp.getController().getDatabase().insertShipmentRecord(shipment);
				if(succesfullyExecuted) {
					String contentText = "Wysy³ka o identyfikatorze " + String.valueOf(nextShipmentID) + " zosta³a pomyœlnie utworzona.\nSpedytor realizuj¹cy wysy³kê: " + shipper.getCompanyName();
					for(ShipmentDetails sd : shipmentDetails){
						Order order = sd.getOrder();
						boolean successfullyUpdated = mainApp.getController().getDatabase().changeOrderStatus(order.getOrderID(), "Awaits_Shipment");
						if(!successfullyUpdated){
							UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d aktualizacji", "Zamówienie o ID " + String.valueOf(order.getOrderID()) + "nie mog³o zostaæ zaktualizowane", currentStage).showAndWait();
						} 
					}
					UtilityClass.showAlert(AlertType.CONFIRMATION, "Komunikat", "Nowa wysy³ka", contentText , currentStage).showAndWait();
					createShipTableView.getItems().clear();
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Nie mo¿na kontynuowaæ", "Wyst¹pi³ b³¹d podczas dodawania wysy³ki do bazy danych.", currentStage).showAndWait();
				}

			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak spedytora", "Proszê zaznaczyæ spedytora przypisanego do tej wysy³ki", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zamówieñ", "Proszê dodaæ zamówienia do tabeli wysy³ek w celu utworzenia nowej wysy³ki", currentStage).showAndWait();
		}
	}
	
	@FXML
	private void removeOrder(){
		Order order = createShipTableView.getSelectionModel().getSelectedItem();
		if(order != null){
			createShipTableData.remove(order);
			if(createShipTableData.size() == 0){
				initializeCommonProperties();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczonego zamówienia", "W celu usuniêcia zamówienia z tabeli wysy³ek proszê zaznaczyæ zamówienie", currentStage).showAndWait();
		}
	}

	private boolean containsOrder(ObservableList<Order> orderData, Order order){
		for(Order o : orderData){
			if(o.getOrderID() == order.getOrderID()) return true;
		} 
		return false;
	}
	
	public void getShippers(){
		createShipShipperBox.getItems().addAll(mainApp.getController().getDatabase().getShipperNames());
	}
}
