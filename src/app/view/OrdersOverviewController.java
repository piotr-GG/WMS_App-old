package app.view;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import app.MainApp;
import app.model.Customer;
import app.model.Employee;
import app.model.Order;
import app.model.Order.OrderStatus;
import app.utility.UtilityClass;
import app.model.OrderDetails;
import app.model.Permission;
import app.model.Product;
import app.model.Shipment;
import app.model.ShipmentDetails;
import app.model.Shipper;
import app.model.User;
import app.view.addNewDialogs.CustomerDetailsDialogController;
import app.view.addNewDialogs.NewShipperDialogController;
import app.view.addNewDialogs.OrderDetailsInfoDialogController;
import app.view.addNewDialogs.ProductDetailedInfoDialogController;
import app.view.addNewDialogs.ShipmentDetailsDialogController;
import app.view.inheritance.ActionController;
import app.view.orderPanes.AcceptOrderPaneController;
import app.view.orderPanes.CreateShipmentPaneController;
import app.view.orderPanes.InsertOrderPaneController;
import app.view.orderPanes.PickSuggestionsPaneController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class OrdersOverviewController extends ActionController {

		@FXML
		private AnchorPane mainPane;
	    @FXML
	    private Tab insertOrderTab;
	    @FXML
	    private Tab acceptOrderTab;
	    @FXML
	    private Tab suggestionOrderTab;
	    @FXML
	    private Tab createNewShipmentTab;
		/*
		 * Query variables
		 */
		@FXML
		private RadioButton showOrderByIDRadioButton;
		@FXML
		private RadioButton showOrdersOfCustomerRadioButton;
		@FXML
		private RadioButton showOrdersWithStatusRadioButton;
		@FXML
		private RadioButton showOrdersWithDateRadioButton;
		@FXML
		private RadioButton showShipmentByIDRadioButton;
		@FXML
		private RadioButton showAllShipmentOfCustomerRadioButton;
		@FXML
		private RadioButton showShipmentsWithDateRadioButton;
		
		@FXML
		private TextField showOrderByIDField;
		@FXML
		private Label showOrderByIDLabel;
		@FXML
		private ComboBox<String> showOrdersOfCustomerBox;
		@FXML
		private Label showOrdersOfCustomerBoxLabel;
		@FXML
		private TextField showOrdersOfCustomerField;
		@FXML
		private Label showOrdersOfCustomerFieldLabel;
		@FXML
		private ComboBox<String> showOrdersWithStatusBox;
		@FXML
		private Label showOrdersWithStatusLabel;
		@FXML
		private DatePicker showOrdersWithDateDatePicker;
		@FXML
		private Label showOrdersWithDateLabel;
		@FXML
		private TextField showShipmentByIDField;
		@FXML
		private Label showShipmentByIDLabel;
		@FXML
		private ComboBox<String> showAllShipmentsOfCustomerBox;
		@FXML
		private Label showAllShipmentsOfCustomerBoxLabel;
		@FXML
		private TextField showAllShipmentsOfCustomerField;
		@FXML
		private Label showAllShipmentsOfCustomersFieldLabel;
		@FXML
		private DatePicker showShipmentsWithDateDatepicker;
		@FXML
		private Label showShipmentsWithDateLabel;
		@FXML
		private Button executeButton;
		
		private ToggleGroup queryGroup;
		
		/*
		 * Results - order part variables
		 */
		@FXML
		private TableView<Order> resultTable;
		@FXML
		private TableColumn<Order, Integer> orderIDColumn;
		@FXML
		private TableColumn<Order, String> customerIDColumn;
		@FXML
		private TableColumn<Order, String> customerNameColumn;
		@FXML
		private TableColumn<Order, String> orderDateColumn;
		@FXML
		private TableColumn<Order, String> requiredDateColumn;
		@FXML
		private TableColumn<Order, String> orderStatusColumn;
		
		@FXML
		private TableView<OrderDetails> resultDetailsTable;
		@FXML
		private TableColumn<OrderDetails, Integer> resultProductIDColumn;
		@FXML
		private TableColumn<OrderDetails, String> resultProductNameColumn;
		@FXML
		private TableColumn<OrderDetails, Integer> resultQuantityColumn;
		@FXML
		private TableColumn<OrderDetails, Double> resultUnitPriceColumn;
		@FXML
		private TableColumn<OrderDetails, Double> resultDiscountColumn;
		
		@FXML
		private TextField shipmentReceiverField;
		@FXML
		private TextField shipmentAddressField;
		@FXML
		private TextField shipmentCityField;
		@FXML
		private TextField shipmentPostalCodeField;
		@FXML
		private TextField shipmentCountryField;
	
		ObservableList<Order> orderResultData;
		ObservableList<OrderDetails> orderDetailsResultData;
		
		
		
		
		@FXML
		private TabPane orderPane;
	    @FXML
		private Tab queriesTab;
	    @FXML
		private Tab orderResultsTab;
	    @FXML
	    private Tab shipmentResultsTab;



	    @FXML
	    private void initialize(){
	    	queryGroup = new ToggleGroup();
			showOrderByIDRadioButton.setToggleGroup(queryGroup);
			showOrdersOfCustomerRadioButton.setToggleGroup(queryGroup);
			showOrdersWithStatusRadioButton.setToggleGroup(queryGroup);
			showOrdersWithDateRadioButton.setToggleGroup(queryGroup);
			showShipmentByIDRadioButton.setToggleGroup(queryGroup);
			showAllShipmentOfCustomerRadioButton.setToggleGroup(queryGroup);
			showShipmentsWithDateRadioButton.setToggleGroup(queryGroup);

	    	initializeOrderResultTable();
	    	showOrdersOfCustomerBox.getItems().addAll("ID klienta", "Nazwa firmy klienta");
			showOrdersWithStatusBox.getItems().addAll(OrderStatus.polishValues());
			
			initializeShipmentResultTable();
	    	showAllShipmentsOfCustomerBox.getItems().addAll("ID klienta", "Nazwa firmy klienta");

	    	UtilityClass.LabelledControlFactory(showOrderByIDRadioButton, new Label[]{showOrderByIDLabel});
	    	UtilityClass.LabelledControlFactory(showOrdersOfCustomerRadioButton, new Label[]{showOrdersOfCustomerBoxLabel, showOrdersOfCustomerFieldLabel});
	    	UtilityClass.LabelledControlFactory(showOrdersWithStatusRadioButton, new Label[]{showOrdersWithStatusLabel});
	    	UtilityClass.LabelledControlFactory(showOrdersWithDateRadioButton, new Label[]{showOrdersWithDateLabel});
	    	UtilityClass.LabelledControlFactory(showShipmentByIDRadioButton, new Label[]{showShipmentByIDLabel});
	    	UtilityClass.LabelledControlFactory(showAllShipmentOfCustomerRadioButton, new Label[]{showAllShipmentsOfCustomerBoxLabel, showAllShipmentsOfCustomersFieldLabel});
	    	UtilityClass.LabelledControlFactory(showShipmentsWithDateRadioButton, new Label[]{showShipmentsWithDateLabel});
	    	

	    }
	    
		public void initializeData(){
			updateCSSStyles();
			mainApp.getController().getStyleSheetPathProperty().addListener(changeListener -> {
				updateCSSStyles();
			});
			
			loadTabContent("view/orderPanes/InsertOrderPane.fxml", insertOrderTab);
			loadTabContent("view/orderPanes/AcceptOrderPane.fxml", acceptOrderTab);
			loadTabContent("view/orderPanes/PickSuggestionsPane.fxml", suggestionOrderTab);
			loadTabContent("view/orderPanes/CreateShipmentPane.fxml", createNewShipmentTab);
		}
		
		private void loadTabContent(String filePath, Tab tab){
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(filePath));
			try {
				AnchorPane pane = (AnchorPane) loader.load();
				ActionController controller = loader.getController();
		        controller.setMainApp(mainApp);
				controller.setCurrentStage(mainApp.getPrimaryStage());
				tab.setContent(pane);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void updateCSSStyles(){
			mainPane.getStylesheets().clear();
			mainPane.getStylesheets().add(mainApp.getController().getStyleSheetPath());
		}
		
	    /*
	     * QUERIES PART
	     * 
	     * 
	     * 
	     * 
	     */
	    
	    /**
	     * Sets every field and combobox etc. to empty value
	     */
	    private void clearTheFields(){
	    	for (Toggle toggle : queryGroup.getToggles()){
	    		toggle.setSelected(false);
	    	}

	    	showOrderByIDField.setText("");
	    	showOrdersOfCustomerBox.getSelectionModel().clearSelection();
	    	showOrdersOfCustomerField.setText("");
	    	showOrdersWithStatusBox.getSelectionModel().clearSelection();
	    	showOrdersWithDateDatePicker.setValue(null);
	    	showShipmentByIDField.setText("");
	    	showAllShipmentsOfCustomerBox.getSelectionModel().clearSelection();
	    	showAllShipmentsOfCustomerField.setText("");
	    	showShipmentsWithDateDatepicker.setValue(null);
	    	
	    }
	    
	    /*
	     *ORDER RESULT PART 
	     * 
	     * 
	     * 
	     */
	    
	    private void initializeOrderResultTable(){
	    	orderResultData = FXCollections.observableArrayList();
	    	
	    	orderIDColumn.setCellValueFactory(cellData->cellData.getValue().getOrderIDProperty().asObject());
			customerIDColumn.setCellValueFactory(cellData->cellData.getValue().getCustomer().getCustomerIDProperty());
			customerNameColumn.setCellValueFactory(cellData->cellData.getValue().getCustomer().getCompanyNameProperty());
			orderDateColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getOrderDate().toString()));
			requiredDateColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getRequiredDate().toString()));
			orderStatusColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getOrderStatus().toString()));
			resultTable.setItems(orderResultData);
			
			MenuItem showCustomerDetails = new MenuItem("Poka¿ klienta");
			showCustomerDetails.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					Order o = resultTable.getSelectionModel().getSelectedItem();
					if(o != null){
						Customer c = o.getCustomer();
						CustomerDetailsDialogController controller = (CustomerDetailsDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/CustomerDetailsDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y klienta");
						controller.setCustomer(c);
						controller.getCurrentStage().showAndWait();
					}
					
				}
			});
			
			
			MenuItem showOtherOrdersOfCustomerMenuItem = new MenuItem("Poka¿ inne zamówienia tego dostawcy");
			showOtherOrdersOfCustomerMenuItem.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					Order o = resultTable.getSelectionModel().getSelectedItem();
					if(o != null){
						getAllOrdersOfCustomer(o.getCustomer());
					}
					
				}
			});
			
			MenuItem showOrderDetails = new MenuItem("Poka¿ szczegó³y zamówienia");
			showOrderDetails.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					Order o = resultTable.getSelectionModel().getSelectedItem();
					if(o != null){
						OrderDetailsInfoDialogController controller = (OrderDetailsInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/OrderDetailsInfoDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y zamówienia");
						controller.setOrder(o);
						controller.getCurrentStage().show();
					}
				}
			});
			
			resultTable.setContextMenu(new ContextMenu(showCustomerDetails, showOtherOrdersOfCustomerMenuItem, showOrderDetails));
			initializeOrderDetailsTable();
	    }
	    
	    private void clearOrderTab(){
	    	orderResultData.clear();
	    	resultDetailsTable.setItems(null);
	    	shipmentReceiverField.setText("");
	    	shipmentAddressField.setText("");
	    	shipmentCityField.setText("");
	    	shipmentPostalCodeField.setText("");
	    	shipmentCountryField.setText("");
	    	
	    }
	    private void initializeOrderDetailsTable(){
	    	resultProductIDColumn.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductIDProperty().asObject());
	    	resultProductNameColumn.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductNameProperty());
	    	resultQuantityColumn.setCellValueFactory(cellData->cellData.getValue().getQuantityProperty().asObject());
	    	resultUnitPriceColumn.setCellValueFactory(cellData->cellData.getValue().getUnitPriceProperty().asObject());
	    	resultDiscountColumn.setCellValueFactory(cellData->cellData.getValue().getDiscountProperty().asObject());
	    	
	    	orderDetailsResultData = FXCollections.observableArrayList();
	    	resultDetailsTable.setItems(orderDetailsResultData);
	    	
			resultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {
				@Override
				public void changed(ObservableValue<? extends Order> observable, Order oldValue, Order newValue) {
					if(newValue != null){
						showOrderDetails(newValue);
					}
					
				}
			});
			
			resultDetailsTable.setOnMouseClicked(event -> {
				OrderDetails od = resultDetailsTable.getSelectionModel().getSelectedItem();
				if(event.getClickCount() == 2 && od != null){
					ProductDetailedInfoDialogController controller = (ProductDetailedInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ProductDetailedInfoDialog.fxml", dialogStage, mainApp, ownerStage, "Szczegó³y produktu");
					controller.setProduct(od.getProduct());
					controller.getCurrentStage().showAndWait();
				}
			});
			
	    	MenuItem showProductDetailsMenuItem = new MenuItem("Poka¿ szczegó³y produktu");
	    	showProductDetailsMenuItem.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) {
					if(resultDetailsTable.getSelectionModel().getSelectedItem() != null){
						ProductDetailedInfoDialogController controller = (ProductDetailedInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ProductDetailedInfoDialog.fxml", dialogStage, mainApp, ownerStage, "Szczegó³y produktu");
						controller.setProduct(resultDetailsTable.getSelectionModel().getSelectedItem().getProduct());
						controller.getCurrentStage().showAndWait();
					}
				}
	    	});
			
	    	resultDetailsTable.setContextMenu(new ContextMenu(showProductDetailsMenuItem));
	    }
	    
	    private void showOrderDetails(Order order){
	    	
	    	orderDetailsResultData = order.getOrderDetails();
	    	shipmentReceiverField.setText(order.getShipName());
	    	shipmentAddressField.setText(order.getAddress());
	    	shipmentCityField.setText(order.getCity());
	    	shipmentPostalCodeField.setText(order.getPostalCode());
	    	shipmentCountryField.setText(order.getCountry());
	    	
	    	resultDetailsTable.setItems(order.getOrderDetails());
	    }
	    
	    /*
	     * SHIPMENT RESULT PART
	     * 
	     * 
	     * 
	     * 
	     * 
	     */

		/*
		 * Result variables - shipment part
		 */
		
		//variables for shipment query results table
		@FXML
		private TableView<Shipment> shipResultTable;
		@FXML
		private TableColumn<Shipment, Integer> shipResultShipIDColumn;
		@FXML
		private TableColumn<Shipment, String> shipResultShipperNameColumn;
		@FXML
		private TableColumn<Shipment, Integer> shipResultShipperIDColumn;
		@FXML
		private TableColumn<Shipment, Integer> shipResultPalletsCountColumn;
		@FXML
		private TableColumn<Shipment, String> shipResultCustomerIDColumn;
		@FXML
		private TableColumn<Shipment, String> shipResultCustomerNameColumn;
		@FXML
		private TableColumn<Shipment, String> shipResultShippedDateColumn;
		@FXML
		private TableColumn<Shipment, String> shipResultShipmentStatusColumn;
		
		//variables for shipment details table
		@FXML
		private TableView<ShipmentDetails> shipDetailsTable;
		@FXML
		private TableColumn<ShipmentDetails, Integer> shipDetailsOrderIDColumn;
		@FXML
		private TableColumn<ShipmentDetails, String> shipDetailsOrderDateColumn;
		@FXML
		private TableColumn<ShipmentDetails, String> shipDetailsRequierdDateColumn;
		@FXML
		private TableColumn<ShipmentDetails, Integer> shipDetailsPalletsCountColumn;

		
		private ObservableList<Shipment> shipResultData;
		private ObservableList<ShipmentDetails> shipDetailsData;
		
		//used for showing shipment details in shipment table view
		@FXML
		private TextField shipmentCustomerNameTextField;
		@FXML 
		private TextField  shipmentAddressTextField;
		@FXML
		private TextField  shipmentCityTextField;
		@FXML
		private TextField  shipmentPostalCodeTextFied;
		@FXML
		private TextField  shipmentCountryTextField;
	    
		private void initializeShipmentResultTable(){
			shipResultShipIDColumn.setCellValueFactory(cellData->cellData.getValue().getShipmentIDProperty().asObject());
			shipResultShipperNameColumn.setCellValueFactory(cellData->cellData.getValue().getShipper().getCompanyNameProperty());
			shipResultShipperIDColumn.setCellValueFactory(cellData->cellData.getValue().getShipper().getShipperIDProperty().asObject());
			shipResultPalletsCountColumn.setCellValueFactory(cellData->cellData.getValue().getPalletsCountProperty().asObject());
			shipResultCustomerIDColumn.setCellValueFactory(cellData->cellData.getValue().getCustomer().getCustomerIDProperty());
			shipResultCustomerNameColumn.setCellValueFactory(cellData->cellData.getValue().getCustomer().getCompanyNameProperty());
			shipResultShippedDateColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getShippedDate().toString()));
			shipResultShipmentStatusColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getShipmentStatus().toString()));
			shipResultData = FXCollections.observableArrayList();
			shipResultTable.setItems(shipResultData);
			
			initializeShipmentDetailsTable();
			
			shipResultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Shipment>() {
				@Override
				public void changed(ObservableValue<? extends Shipment> observable, Shipment oldValue,
						Shipment newValue) {
					if(newValue != null) showShipmentDetails(newValue);
					
				}
			});
			
			MenuItem showShipperMenuItem = new MenuItem("Poka¿ spedytora");
			showShipperMenuItem.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					Shipment s = shipResultTable.getSelectionModel().getSelectedItem();
					if(s != null){
						Shipper shipper = s.getShipper();
		    			NewShipperDialogController controller = (NewShipperDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewShipperDialog.fxml", dialogStage, mainApp, currentStage, "Dodaj nowego spedytora");
		    			controller.setShipper(shipper);
		    			controller.getCurrentStage().showAndWait();
					}
				}
			});
			
			MenuItem showShipmentsOfCustomer = new MenuItem("Poka¿ inne wysy³ki klienta");
			showShipmentsOfCustomer.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) {
					Shipment s = shipResultTable.getSelectionModel().getSelectedItem();
					if(s != null){
						getAllShipmentsOfCustomer(s.getCustomer());
					}
				}
				
			});
			
			MenuItem showCustomerDetails = new MenuItem("Poka¿ klienta");
			showCustomerDetails.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					Shipment s = shipResultTable.getSelectionModel().getSelectedItem();
					if(s != null){
						Customer c = s.getCustomer();
						CustomerDetailsDialogController controller = (CustomerDetailsDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/CustomerDetailsDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y klienta");
						controller.setCustomer(c);
						controller.getCurrentStage().showAndWait();
					}
					
				}
			});
			
			MenuItem showShipmentDetails = new MenuItem("Poka¿ szczegó³y wysy³ki");
			showShipmentDetails.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					Shipment s = shipResultTable.getSelectionModel().getSelectedItem();
					if(s != null){
						ShipmentDetailsDialogController controller = (ShipmentDetailsDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ShipmentDetailsDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y wysy³ki");
		    			controller.setShipment(s);
		    			controller.fillTheFields();
		    			controller.getCurrentStage().showAndWait();
					}
				}
			});
			
			shipResultTable.setContextMenu(new ContextMenu(showShipperMenuItem, showCustomerDetails, showShipmentsOfCustomer, showShipmentDetails));
			
		}
		
		private void initializeShipmentDetailsTable(){
			shipDetailsOrderIDColumn.setCellValueFactory(cellData->cellData.getValue().getOrder().getOrderIDProperty().asObject());
			shipDetailsOrderDateColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getOrder().getOrderDate().toString()));
			shipDetailsRequierdDateColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getOrder().getRequiredDate().toString()));
			shipDetailsPalletsCountColumn.setCellValueFactory(cellData->cellData.getValue().getPalletsCountProperty().asObject());
			
			shipDetailsData = FXCollections.observableArrayList();
			shipDetailsTable.setItems(shipDetailsData);
			
			shipDetailsTable.setOnMouseClicked(event -> {
				if(event.getClickCount() == 2 && shipDetailsTable.getSelectionModel().getSelectedItem() != null){
					ShipmentDetails sd = shipDetailsTable.getSelectionModel().getSelectedItem();
					getOrderByID(sd.getOrder().getOrderID());
				}
			});
			
			MenuItem showOrderDetails = new MenuItem("Poka¿ szczegó³y zamówienia");
			showOrderDetails.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(ActionEvent event) {
					if(shipDetailsTable.getSelectionModel().getSelectedItem() != null){
						ShipmentDetails sd = shipDetailsTable.getSelectionModel().getSelectedItem();
						getOrderByID(sd.getOrder().getOrderID());
					}
					
				}
			});
			
			shipDetailsTable.setContextMenu(new ContextMenu(showOrderDetails));
		}
		
	    private void showShipmentDetails(Shipment shipment){
	    	shipDetailsData.clear();
	    	shipDetailsData.addAll(shipment.getShipmentDetails());
	    	
	    	shipmentCustomerNameTextField.setText(shipment.getCustomer().getCompanyName());
	    	shipmentAddressTextField.setText(shipment.getAddress());
	    	shipmentPostalCodeTextFied.setText(shipment.getPostalCode());
	    	shipmentCityTextField.setText(shipment.getCity());
	    	shipmentCountryTextField.setText(shipment.getCountry());
	    }
	    
	    private void clearShipmentData(){
	    	shipDetailsData.clear();
	    	shipResultData.clear();
	    	
	    	shipmentCustomerNameTextField.setText("");
	    	shipmentAddressTextField.setText("");
	    	shipmentPostalCodeTextFied.setText("");
	    	shipmentCityTextField.setText("");
	    	shipmentCountryTextField.setText("");
	    }
		

	    
	    
	    
	    
		@FXML
		private void executeQueryClick(){
			//Show order by its ID
			if(showOrderByIDRadioButton.isSelected()) {
				String orderByIDText = showOrderByIDField.getText();
				if(!orderByIDText.isEmpty()){
					if(UtilityClass.isParsable(orderByIDText)){
						int orderID = Integer.parseInt(showOrderByIDField.getText());
						getOrderByID(orderID);
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Proszê podaæ liczbê ca³kowit¹!", currentStage).showAndWait();						
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Brak identyfikatora zamówienia", currentStage).showAndWait();
				}
			}
			
			//Show all orders of customer by his ID or his company name
			if(showOrdersOfCustomerRadioButton.isSelected()){
				String comboBoxText = showOrdersOfCustomerBox.getSelectionModel().getSelectedItem();
				String textFieldText = showOrdersOfCustomerField.getText();
				if(!textFieldText.isEmpty()){
					ObservableList<Order> result = FXCollections.observableArrayList();
					if(comboBoxText.equals("ID klienta")){
						result = mainApp.getController().getDatabase().getOrderWithCustomerParameters("CustomerID", textFieldText);
					} else if (comboBoxText.equals("Nazwa firmy klienta")) {
						 result = mainApp.getController().getDatabase().getOrderWithCustomerParameters("CompanyName", textFieldText);
					}
					
					if(!result.isEmpty()){
						clearOrderTab();
						orderResultData.addAll(result);
						orderPane.getSelectionModel().select(orderResultsTab);
						UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Znaleziono zamówienia", "Zamówienia zosta³y znalezione", currentStage).showAndWait();
					} else {
						UtilityClass.showAlert(AlertType.WARNING, "Komunikat", "Brak rekordów", "Zapytanie nie zwróci³o ¿adnych zamówieñ", currentStage).showAndWait();
					}
				}
			}
			
			//show all the orders with given status
			if(showOrdersWithStatusRadioButton.isSelected()){

				
				String orderStatus = showOrdersWithStatusBox.getSelectionModel().getSelectedItem();
				if(!showOrdersWithStatusBox.getSelectionModel().isEmpty()){
					OrderStatus convertedOrderStatus = OrderStatus.convertIntoEnglishVersion(orderStatus);
					
					ObservableList<Order> result = FXCollections.observableArrayList();
					result = mainApp.getController().getDatabase().getOrdersByOrderStatus(convertedOrderStatus.toString());
					if(!result.isEmpty()){
						clearOrderTab();
						orderResultData.addAll(result);
						orderPane.getSelectionModel().select(orderResultsTab);
						UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Znaleziono zamówienia", "Zamówienia zosta³y znalezione", currentStage).showAndWait();
					} else {
						UtilityClass.showAlert(AlertType.WARNING, "Komunikat", "Brak rekordów", "Zapytanie nie zwróci³o ¿adnych zamówieñ", currentStage).showAndWait();
					
				}
			}
				else {
					UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Brak zaznaczonej wartoœci", "Proszê zaznaczyæ odpowiedni status zamówienia", currentStage).showAndWait();
				}
			
		}
			//show all the order with given date 
			if(showOrdersWithDateRadioButton.isSelected()){
				LocalDate orderDate = showOrdersWithDateDatePicker.getValue();
				if(orderDate != null){
					//adjust the existing date format to the one used by DatePickers
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					String orderString = orderDate.toString();
					orderDate = LocalDate.parse(orderString, formatter);
					
					ObservableList<Order> result = FXCollections.observableArrayList();
					result = mainApp.getController().getDatabase().getOrderWithDate("OrderDate", orderDate.toString());
					if(!result.isEmpty()){
						clearOrderTab();
						orderResultData.addAll(result);
						orderPane.getSelectionModel().select(orderResultsTab);
						UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Znaleziono zamówienia", "Zamówienia zosta³y znalezione", currentStage).showAndWait();
					} else {
						UtilityClass.showAlert(AlertType.WARNING, "Komunikat", "Brak rekordów", "Zapytanie nie zwróci³o ¿adnych zamówieñ", currentStage).showAndWait();
					
				}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Brak podanej daty zamówienia", "Proszê podaæ datê zamówienia, po której maj¹ byæ wyszukane zamówienia", currentStage).showAndWait();
				}
					
			}
			
			if(showShipmentByIDRadioButton.isSelected()){
				String shipmentIDText = showShipmentByIDField.getText();
				if(shipmentIDText.length() != 0){
					if(UtilityClass.isParsable(shipmentIDText)){
						
						clearShipmentData();
						int shipmentID = Integer.parseInt(shipmentIDText);
						Shipment shipment = mainApp.getController().getDatabase().getShipmentByID(shipmentID);
						if(shipment != null){
							UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Znaleziono wysy³ki", "\nWysy³ka o podanym ID zosta³a znaleziona\n", currentStage).showAndWait(); 
							shipResultData.add(shipment);
							orderPane.getSelectionModel().select(shipmentResultsTab);
						} else {
							UtilityClass.showAlert(AlertType.WARNING, "Komunikat", "Brak wysy³ek", "\nZapytanie nie zwróci³o ¿adnych wysy³ek o podanym identyfikatorze\n", currentStage).showAndWait();
						}
						
					} else {
						  UtilityClass.showAlert(AlertType.WARNING, "Komunikat", "B³êdna wartoœæ identyfikatora", "Proszê podaæ liczbê ca³kowit¹ bêd¹c¹ identyfikatorem zamówienia", currentStage).showAndWait();
						  
					}
					
				} else {
					UtilityClass.showAlert(AlertType.WARNING, "Komunikat", "Brak podanego identyfikatora", "\nProszê podaæ identyfikator zamówienia w przeznaczonym do tego polu\n", currentStage).showAndWait();
				}
			}	
	
			if(showAllShipmentOfCustomerRadioButton.isSelected()){
				String comboBoxText = showAllShipmentsOfCustomerBox.getSelectionModel().getSelectedItem();
				if(comboBoxText != null && !comboBoxText.isEmpty()){
					ObservableList<Shipment> queryResult = getAllShipmentsOfCustomer(comboBoxText);
					if(queryResult != null && !queryResult.isEmpty()){
						UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Znaleziono wysy³ki", "\nWysy³ki zosta³y znalezione\n", currentStage).showAndWait(); 
						clearShipmentData();
						shipResultData.addAll(queryResult);
						orderPane.getSelectionModel().select(shipmentResultsTab);
					} else {
						UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Brak rekordów", "\nZapytanie nie zwróci³o ¿adnych wysy³ek spe³niaj¹cych podane warunki\n", currentStage).showAndWait(); 
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Brak wartoœci", "/nProszê podaæ po jakim polu maj¹ byæ znalezione zamówienia klienta!/n", currentStage).showAndWait();
				}
			}
			
			
			
			
			
			if(showShipmentsWithDateRadioButton.isSelected()){
				LocalDate shippedDate = showShipmentsWithDateDatepicker.getValue();
				if(shippedDate != null){
					clearShipmentData();
					ObservableList<Shipment> result = mainApp.getController().getDatabase().getShipmentByShippedDate(shippedDate.toString());
					if(!result.isEmpty()){
						UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Znaleziono wysy³ki", "\nZapytanie zwróci³o " + String.valueOf(result.size()) + " rekordów", currentStage).showAndWait(); 
						shipResultData.addAll(result);
						orderPane.getSelectionModel().select(shipmentResultsTab);
					} else {
						UtilityClass.showAlert(AlertType.WARNING, "Komunikat", "Brak wysy³ek", "\nZapytanie nie zwróci³o ¿adnych wysy³ek o podanej dacie wysy³ki\n", currentStage).showAndWait();
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Brak wprowadzonej daty wysy³ki", "Proszê uzupe³niæ pole daty wysy³ki przed wykonaniem zapytania", currentStage).showAndWait();
				}
				
			}
			
		}
		

		
		
		/*
		 * Query execution functions
		 */
		
	    private void getOrderByID(int orderID){
			Order order = mainApp.getController().getDatabase().getOrderByID(orderID);
			if(order != null){
				clearOrderTab();
				orderResultData.add(order);
				orderPane.getSelectionModel().select(orderResultsTab);
				UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Znaleziono zamówienie", "Zamówienie o podanym identyfikatorze zosta³o znalezione", currentStage).showAndWait();
			} else {
				UtilityClass.showAlert(AlertType.WARNING, "Komunikat", "Brak zamówienia", "Brak zamówienia o podanym identyfikatorze" , currentStage).showAndWait();
			}
	    }
	    
	    private ObservableList<Shipment> getAllShipmentsOfCustomer(String parameterName){
	    	String textFieldValue = showAllShipmentsOfCustomerField.getText();
	    	if(!textFieldValue.isEmpty()){
	    		ObservableList<Shipment> shipmentsOfCustomer = FXCollections.observableArrayList();
		    	if(parameterName.equals("ID klienta")){
		    		shipmentsOfCustomer = mainApp.getController().getDatabase().getAllShipmentsOfCustomer("CustomerID", textFieldValue);
		    	} 
		    	
		    	if(parameterName.equals("Nazwa firmy klienta")){
		    		shipmentsOfCustomer = mainApp.getController().getDatabase().getAllShipmentsOfCustomer("CompanyName", textFieldValue);
		    	}
		    	
		    	return shipmentsOfCustomer;
		    	
	    	} else {
	    		UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Brak wartoœci", "Proszê podaæ wartoœæ w polu tekstowym odnoœnie klientów", currentStage).showAndWait();
	    		return null;
	    	}
	
	    }
	    
	    private void getAllOrdersOfCustomer(Customer c){
			ObservableList<Order> result = FXCollections.observableArrayList();
			result = mainApp.getController().getDatabase().getOrderWithCustomerParameters("CustomerID", c.getCustomerID());
			if(!result.isEmpty()){
				clearOrderTab();
				orderResultData.addAll(result);
				orderPane.getSelectionModel().select(orderResultsTab);
				UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Znaleziono zamówienia", "Zamówienia zosta³y znalezione", currentStage).showAndWait();
			} else {
				UtilityClass.showAlert(AlertType.WARNING, "Komunikat", "Brak rekordów", "Zapytanie nie zwróci³o ¿adnych zamówieñ", currentStage).showAndWait();
			}
	    }
	    
	    private void getAllShipmentsOfCustomer(Customer c){
	    	ObservableList<Shipment> queryResult= FXCollections.observableArrayList();
	    	queryResult = mainApp.getController().getDatabase().getAllShipmentsOfCustomer("CustomerID", c.getCustomerID());
			if(!queryResult.isEmpty()){
				UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Znaleziono wysy³ki", "\nWysy³ki zosta³y znalezione\n", currentStage).showAndWait(); 
				clearShipmentData();
				shipResultData.addAll(queryResult);
				orderPane.getSelectionModel().select(shipmentResultsTab);
			} else {
				UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Brak rekordów", "\nZapytanie nie zwróci³o ¿adnych wysy³ek spe³niaj¹cych podane warunki\n", currentStage).showAndWait(); 
			}
	    }
	    
		@FXML
		private void handleBinClick(){
			clearTheFields();
		}
		
		private void showDenyPermissionAlert(String departmentString){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Komunikat");
			alert.setHeaderText("Brak uprawnieñ");
			alert.setContentText("Dostêp do tej zak³adki jest mo¿liwy tylko dla pracowników " + departmentString );
			alert.showAndWait();
		}
			
	
}




