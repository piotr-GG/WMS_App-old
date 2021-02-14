package app.view;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.apache.xmlbeans.impl.common.ValidatorListener.Event;

import com.mysql.jdbc.authentication.Sha256PasswordPlugin;

import app.MainApp;
import app.model.Delivery;
import app.model.Delivery.DeliveryStatus;
import app.utility.UtilityClass;
import app.view.addNewDialogs.DeliveryDetailsDialogController;
import app.view.addNewDialogs.EmployeeDialogController;
import app.view.addNewDialogs.NewSupplierDialogController;
import app.view.addNewDialogs.ProductDetailedInfoDialogController;
import app.view.addNewDialogs.SupplierDetailsDialogController;
import app.view.inheritance.ActionController;
import app.model.DeliveryDetails;
import app.model.Employee;
import app.model.OrderDetails;
import app.model.Product;
import app.model.Supplier;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.util.Duration;

public class DeliveryOverviewController extends ActionController {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private TabPane deliveryPane;
	@FXML
	private Tab queryTab;
	@FXML
	private Tab resultTab;
	@FXML
	private Tab placeDeliveryOrderTab;
	@FXML
	private Tab takeDeliveryTab;
	
	@FXML
	private Label showDeliveryByIDLabel;
	@FXML
	private Label showDeliveriesOfSupplierBoxLabel;
	@FXML
	private Label showDeliveriesOfSupplierFieldLabel;
	@FXML
	private Label showDeliveriesWithValueBoxLabel;
	@FXML
	private Label showDeliveriesWithValueFieldLabel;
	@FXML
	private Label showTheMostImportantSuppliersLabel;
	@FXML
	private Label showDeliveriesWithDateLabel;

	/*
	 * Query part
	 */
	@FXML
	private RadioButton showDeliveryByIDRadioButton;
	@FXML
	private RadioButton showDeliveriesOfSupplierRadioButton;
	@FXML
	private RadioButton showDeliveriesWithValueRadioButton;
	@FXML
	private RadioButton showTheMostImportantSuppliersRadioButton;
	@FXML
	private RadioButton showDeliveriesWithDateRadioButton;
	
	@FXML
	private TextField showDeliveryByIDField;
	@FXML
	private ComboBox <String> showDeliveriesOfSupplierBox;
	@FXML
	private TextField showDeliveriesOfSupplierField;
	@FXML
	private ComboBox <String> showDeliveriesWithValueBox;
	@FXML
	private TextField showDeliveriesWithValueField;
	@FXML
	private ComboBox<String> showTheMostImportantSuppliersBox;
	@FXML
	private DatePicker showDeliveriesWithDateDatePicker;
	
	private ToggleGroup radioButtonGroup;
	
	/*
	 * Result part
	 * 
	 */
	
	@FXML
	private TableView<Delivery> deliveryTable;
	@FXML
	private TableColumn<Delivery, Integer> deliveryIDColumn;
	@FXML
	private TableColumn<Delivery, String> deliveryDateColumn;
	@FXML
	private TableColumn<Delivery, Integer> supplierIDColumn;
	@FXML
	private TableColumn<Delivery, String> supplierNameColumn;
	@FXML
	private TableColumn<Delivery, String> driverNameColumn;
	@FXML
	private TableColumn<Delivery, Integer> palletsCountColumn;
	@FXML
	private TableColumn<Delivery, String> employeeColumn;
	
	private ObservableList<Delivery> deliveryData;
	private ObservableList<Node> nodeList;
	
	public void initializeData(){
		
		
		
		UtilityClass.LabelledControlFactory(showDeliveryByIDRadioButton, new Label[] { showDeliveryByIDLabel });
		UtilityClass.LabelledControlFactory(showDeliveriesOfSupplierRadioButton, new Label[]{showDeliveriesOfSupplierBoxLabel, showDeliveriesOfSupplierFieldLabel});
		UtilityClass.LabelledControlFactory(showDeliveriesWithValueRadioButton, new Label[] {showDeliveriesWithValueBoxLabel, showDeliveriesWithValueFieldLabel});
		UtilityClass.LabelledControlFactory(showTheMostImportantSuppliersRadioButton, new Label[]{showTheMostImportantSuppliersLabel});
		UtilityClass.LabelledControlFactory(showDeliveriesWithDateRadioButton, new Label[]{showDeliveriesWithDateLabel});
		
		showTheMostImportantSuppliersBox.getItems().addAll(mainApp.getController().getDatabase().getCategoryNames());
		
		radioButtonGroup = new ToggleGroup();
		showDeliveryByIDRadioButton.setToggleGroup(radioButtonGroup);
		showDeliveriesOfSupplierRadioButton.setToggleGroup(radioButtonGroup);
		showDeliveriesWithValueRadioButton.setToggleGroup(radioButtonGroup);
		showTheMostImportantSuppliersRadioButton.setToggleGroup(radioButtonGroup);
		showDeliveriesWithDateRadioButton.setToggleGroup(radioButtonGroup);
		
		showDeliveriesWithValueBox.getItems().addAll("ID produktu", "Nazwa produktu");
		
		showDeliveriesOfSupplierBox.getItems().addAll("ID dostawcy", "Nazwa dostawcy");
		
		nodeList = FXCollections.observableArrayList(showDeliveryByIDField, showDeliveriesOfSupplierBox, showDeliveriesOfSupplierField, showDeliveriesWithValueBox, showDeliveriesWithValueField, showTheMostImportantSuppliersBox, showDeliveriesWithDateDatePicker);
		initializeDeliveryTable();
		updateCSSStyles();
		mainApp.getController().getStyleSheetPathProperty().addListener(changeListener -> {
			updateCSSStyles();
		});
		
		loadTabContent("view/deliveryPanes/PlaceDeliveryPane.fxml", placeDeliveryOrderTab);
		loadTabContent("view/deliveryPanes/TakeDeliveryPane.fxml", takeDeliveryTab);
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
			e.printStackTrace();
		}
	}
	
	private void updateCSSStyles(){
		mainPane.getStylesheets().clear();
		mainPane.getStylesheets().add(mainApp.getController().getStyleSheetPath());
	}
	
	@FXML
	private void handleExecuteClick(){
		for(Node n : nodeList){
			n.getStyleClass().remove("wrongInputEffect");
		}
		
		if(showDeliveryByIDRadioButton.isSelected()) showDeliveryByID();
		if(showDeliveriesOfSupplierRadioButton.isSelected()) showDeliveriesOfSupplier();
		if(showDeliveriesWithDateRadioButton.isSelected()) showDeliveriesWithDate();
		if(showTheMostImportantSuppliersRadioButton.isSelected()) showMostImportantSuppliersOfCategory();
		if(showDeliveriesWithValueRadioButton.isSelected()) showDeliveriesWithValue();
	}
	
	private void showDeliveryByID(){
		String deliveryIDText = showDeliveryByIDField.getText();
		if(deliveryIDText.length() != 0){
			boolean isParsable = UtilityClass.isParsable(deliveryIDText);
			if(isParsable){
				int deliveryID = Integer.parseInt(deliveryIDText);
				Delivery delivery = mainApp.getController().getDatabase().getDeliveryByID(deliveryID);
				if(delivery != null){
					UtilityClass.showAlert(AlertType.INFORMATION, "Poka¿ dostawê", "Zapytanie zwróci³o wynik", "Zapytanie zosta³o wykonane pomyœlnie!", currentStage).showAndWait();
					deliveryData.clear();
					deliveryData.add(delivery);
					deliveryPane.getSelectionModel().select(1);
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "Brak wyników", "Brak wyników zapytania", "Zapytanie nie zwróci³o rezultatów", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Z³a wartoœæ!", "ID dostawy ma byæ liczb¹ ca³kowit¹!", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Proszê podaæ ID dostawy do wyœwietlenia!", currentStage).showAndWait();
			showDeliveryByIDField.getStyleClass().add("wrongInputEffect");
		}
	}
	
	private void showDeliveriesOfSupplier(){
		String selectedItem = showDeliveriesOfSupplierBox.getSelectionModel().getSelectedItem();
		String supplierFieldText = showDeliveriesOfSupplierField.getText();
		
		if(selectedItem != null &&selectedItem.length() != 0 && supplierFieldText.length() != 0){
			if(selectedItem.equals("ID dostawcy")){
				boolean isParsable = UtilityClass.isParsable(supplierFieldText);
				if(isParsable){
					int supplierID = Integer.parseInt(supplierFieldText);
					ObservableList<Delivery> result = mainApp.getController().getDatabase().getDeliveriesBySupplierID(supplierID);
					getResultShown(result);
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Z³a wartoœæ!", "ID dostawcy ma byæ liczb¹ ca³kowit¹!", currentStage).showAndWait();
				}
			} else {
				ObservableList<Delivery> result = mainApp.getController().getDatabase().getDeliveriesBySupplierName(supplierFieldText);
				getResultShown(result);
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak warunku", "Proszê podaæ warunek dla zapytania oraz jego wartoœæ!", currentStage).showAndWait();
			showDeliveriesOfSupplierBox.getStyleClass().add("wrongInputEffect");
			showDeliveriesOfSupplierField.getStyleClass().add("wrongInputEffect");
		}
	}
	

	
	private void showDeliveriesWithDate(){
		LocalDate date = showDeliveriesWithDateDatePicker.getValue();
		if(date != null){
			ObservableList<Delivery> result = mainApp.getController().getDatabase().getDeliveriesWithArrivalDate(date.toString());
			getResultShown(result);
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak podanej daty", "Proszê podaæ datê dostawy!", currentStage).showAndWait();
			showDeliveriesWithDateDatePicker.getStyleClass().add("wrongInputEffect");
		}
	}
	
	private void showMostImportantSuppliersOfCategory(){
		String selectedItem = showTheMostImportantSuppliersBox.getSelectionModel().getSelectedItem();
		if(selectedItem != null && selectedItem.length() != 0){
			ObservableList<Delivery> result = mainApp.getController().getDatabase().getMostImportantSuppliersOfCategory(selectedItem);
			getResultShown(result);
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak kategorii", "Proszê podaæ kategoriê!", currentStage).showAndWait();
			showTheMostImportantSuppliersBox.getStyleClass().add("wrongInputEffect");
		}
	}
	
	private void showDeliveriesWithValue(){
		String comboBoxItem = showDeliveriesWithValueBox.getSelectionModel().getSelectedItem();
		String valueText = showDeliveriesWithValueField.getText();
		if(comboBoxItem != null && comboBoxItem.length() != 0 && valueText.length() != 0){
			if(comboBoxItem.equals("ID produktu")){
				boolean isParsable = UtilityClass.isParsable(valueText);
				if(isParsable){
					int productID = Integer.parseInt(valueText);
					ObservableList<Delivery> result = mainApp.getController().getDatabase().getDeliveriesOfProductByID(productID);
					getResultShown(result);
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Z³a wartoœæ!", "ID produktu ma byæ liczb¹ ca³kowit¹!", currentStage).showAndWait();
				}	
			} else {
				ObservableList<Delivery> result = mainApp.getController().getDatabase().getDeliveriesOfProductByName(valueText);
				getResultShown(result);
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak warunku", "Proszê podaæ warunek dla zapytania oraz jego wartoœæ!", currentStage).showAndWait();
			showDeliveriesWithValueBox.getStyleClass().add("wrongInputEffect");
			showDeliveriesWithValueField.getStyleClass().add("wrongInputEffect");
		}
	}
	
	private void getResultShown(ObservableList<Delivery> result){
		if(result != null && !result.isEmpty()){
			UtilityClass.showAlert(AlertType.INFORMATION, "Poka¿ dostawy", "Zapytanie zwróci³o wynik", "Zapytanie zosta³o wykonane pomyœlnie!", currentStage).showAndWait();
			deliveryData.clear();
			deliveryData.addAll(result);
			deliveryPane.getSelectionModel().select(1);
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Brak wyników", "Brak wyników zapytania", "Zapytanie nie zwróci³o rezultatów", currentStage).showAndWait();
		}
	}
	
	/*
	 * Result pane functions
	 * 
	 * 
	 */
	
	private void initializeDeliveryTable(){
		deliveryIDColumn.setCellValueFactory(cellData->cellData.getValue().getDeliveryIDProperty().asObject());
		deliveryDateColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getRequiredDeliveryDate().toString()));
		supplierIDColumn.setCellValueFactory(cellData->cellData.getValue().getSupplier().getSupplierIDProperty().asObject());
		supplierNameColumn.setCellValueFactory(cellData->cellData.getValue().getSupplier().getCompanyNameProperty());
		driverNameColumn.setCellValueFactory(cellData->cellData.getValue().getDriverNameProperty());
		palletsCountColumn.setCellValueFactory(cellData->cellData.getValue().getPalletCountProperty().asObject());
		employeeColumn.setCellValueFactory(cellData->cellData.getValue().getEmployee().getFullNameProperty());
		
		
		supplierNameColumn.setCellFactory(new Callback<TableColumn<Delivery, String>, TableCell<Delivery, String>>() {
		    public TableCell<Delivery, String> call(TableColumn<Delivery, String> col) {
		        final TableCell<Delivery, String> cell = new TableCell<>();
		        cell.textProperty().bind(cell.itemProperty()); 
		        MenuItem showSupplier = new MenuItem("Poka¿ szczegó³y dostawcy");
		        showSupplier.setOnAction(new EventHandler<ActionEvent>(){
		        	
					public void handle(ActionEvent arg0) {
						SupplierDetailsDialogController controller =  (SupplierDetailsDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/SupplierDetailsDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y dostawcy");
						Delivery item = (Delivery) cell.getTableRow().getItem();
						controller.setSupplier(item.getSupplier());
						controller.getCurrentStage().showAndWait();
					}
		        	
		        });
		        
		        cell.setContextMenu(new ContextMenu(showSupplier));
		        return cell ;
		    }
		});
		
		
		employeeColumn.setCellFactory(new Callback<TableColumn<Delivery, String>, TableCell<Delivery, String>>() {
		    public TableCell<Delivery, String> call(TableColumn<Delivery, String> col) {
		        final TableCell<Delivery, String> cell = new TableCell<>();
		        cell.textProperty().bind(cell.itemProperty()); 
		        MenuItem showEmployee = new MenuItem("Poka¿ szczegó³y pracownika");
		        showEmployee.setOnAction(new EventHandler<ActionEvent>(){
		        	public void handle(ActionEvent e){
		    			EmployeeDialogController controller = (EmployeeDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/EmployeeDialog.fxml", dialogStage, mainApp, ownerStage, "Szczegó³y pracownika");
		    			Delivery item = (Delivery) cell.getTableRow().getItem();
		    			controller.setEmployee(item.getEmployee());
		    			controller.getCurrentStage().show();
		        	}
		        });
		        
		        cell.setContextMenu(new ContextMenu(showEmployee));
		        return cell ;
		    }
		});
		
    	deliveryTable.setRowFactory(new Callback<TableView<Delivery>, TableRow<Delivery>>(){

			@Override
			public TableRow<Delivery> call(TableView<Delivery> param) {
				final TableRow<Delivery> row = new TableRow<>();
				final ContextMenu rowMenu = new ContextMenu();
				final MenuItem showSupplier = new MenuItem("Poka¿ szczegó³y dostawcy");
				final MenuItem showEmployee = new MenuItem("Poka¿ szczegó³y pracownika");
				final MenuItem showDeliveryDetails = new MenuItem("Poka¿ szczegó³y dostawy");
				showSupplier.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent arg0) {
						SupplierDetailsDialogController controller =  (SupplierDetailsDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/SupplierDetailsDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y dostawcy");
						Delivery item = row.getItem();
						controller.setSupplier(item.getSupplier());
						controller.getCurrentStage().showAndWait();
					}
		        });
				
				showEmployee.setOnAction(new EventHandler<ActionEvent>(){
		        	public void handle(ActionEvent e){
		    			EmployeeDialogController controller = (EmployeeDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/EmployeeDialog.fxml", dialogStage, mainApp, ownerStage, "Szczegó³y pracownika");
		    			Delivery item = (Delivery) row.getItem();
		    			controller.setEmployee(item.getEmployee());
		    			controller.getCurrentStage().show();
		        	}
		        });
				
				showDeliveryDetails.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent arg0) {
						DeliveryDetailsDialogController controller = (DeliveryDetailsDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/DeliveryDetailsDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y dostawy");
						controller.setDelivery(row.getItem());
						controller.getCurrentStage().showAndWait();
					}
				});
				
				rowMenu.getItems().addAll(showSupplier, showEmployee, showDeliveryDetails);
				row.setContextMenu(rowMenu);
				
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
						.then(rowMenu)
						.otherwise((ContextMenu) null));
				return row;
			}
    		
    	});
		
		
		deliveryData = FXCollections.observableArrayList();
		deliveryTable.setItems(deliveryData);
	}
	
	@FXML
	private void handleGetDeliveryDetailsClick(){
		Delivery item = deliveryTable.getSelectionModel().getSelectedItem();
		if(item != null){
			DeliveryDetailsDialogController controller = (DeliveryDetailsDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/DeliveryDetailsDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y dostawy");
			controller.setDelivery(item);
			controller.getCurrentStage().showAndWait();
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczenia", "Proszê zaznaczyæ najpierw dostawê, a potem klikaæ!!111!!", currentStage).showAndWait();
		}
	}
	

	
	
	
	

	


}


