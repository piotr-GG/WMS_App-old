package app.view.deliveryPanes;

import app.model.Delivery;
import app.model.DeliveryDetails;
import app.model.Delivery.DeliveryStatus;
import app.utility.UtilityClass;
import app.view.TakeTheProductOverviewController;
import app.view.addNewDialogs.DeliveryDetailsDialogController;
import app.view.addNewDialogs.EmployeeDialogController;
import app.view.addNewDialogs.NewSupplierDialogController;
import app.view.addNewDialogs.ProductDetailedInfoDialogController;
import app.view.addNewDialogs.SupplierDetailsDialogController;
import app.view.inheritance.ActionController;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import javafx.util.Duration;

public class TakeDeliveryPaneController extends ActionController {

	/*
	 * Take delivery part
	 * 
	 * 
	 */
	
	//deliveries to take is the main table in the tab.
	//DTT suffix marks the columns belonging to this table
	@FXML
	private TableView<Delivery> deliveriesToTakeTable;
	@FXML
	private TableColumn<Delivery, Integer> deliveryIDColumn_DTT;
	@FXML
	private TableColumn<Delivery, String> deliveryArrivalDateColumn_DTT;
	@FXML
	private TableColumn<Delivery, Integer> supplierIDColumn_DTT;
	@FXML
	private TableColumn<Delivery, String> supplierCompanyNameColumn_DTT;
	@FXML
	private TableColumn<Delivery, String> deliveryDriverNameColumn_DTT;
	@FXML
	private TableColumn<Delivery, Integer> palletCountColumn_DTT;
	
	private ObservableList<Delivery> deliveriesToTakeData;
	
	//delivery details table 
	//DDT suffix marks the columns belonging to delivery details table
	
	@FXML
	private TableView<DeliveryDetails> deliveryDetailsTable;
	@FXML
	private TableColumn<DeliveryDetails, Integer > productIDColumn_DDT;
	@FXML
	private TableColumn<DeliveryDetails, String> productNameColumn_DDT;
	@FXML
	private TableColumn<DeliveryDetails, Integer> deliveredQuantityColumn_DDT;
	@FXML
	private TableColumn<DeliveryDetails, Integer> quantityTakenColumn_DDT;
	@FXML
	private TableColumn<DeliveryDetails, Number> quantityToBeTakenColumn_DTT;
	@FXML
	private TableColumn<DeliveryDetails, Integer> palletCountColumn_DDT;
	
	private ObservableList<DeliveryDetails> deliveryDetailsData;
	
	@FXML
	private void initialize(){
		
		initializeDeliveriesToTakeTable();
	}
	
	@FXML
	private void handleGetDeliveriesClick(){
		deliveriesToTakeData.clear();
		deliveriesToTakeData.addAll(mainApp.getController().getDatabase().getDeliveriesWithDeliveryStatus(DeliveryStatus.Arrived.toString()));
	}
	
	@FXML
	private void handleTakeTheProductClick(){
		Delivery d = deliveriesToTakeTable.getSelectionModel().getSelectedItem();
		if(d != null){
			DeliveryDetails dd = deliveryDetailsTable.getSelectionModel().getSelectedItem();
			if(dd != null){
				boolean isProductFullyTaken = showTakeProductOverview(dd, d.getDeliveryID());
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczonego produktu", "Proszê zaznaczyæ produkt do przyjêcia!", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczonej dostawy", "Proszê zaznaczyæ dostawê do przyjêcia produktu!", currentStage).showAndWait();
		}
	}	
	
	@FXML
	private void handleTakeTheDeliveryClick(){
		Delivery d = deliveriesToTakeTable.getSelectionModel().getSelectedItem();
		if(d != null){
			boolean isTheDeliveryReady = areTheProductsTaken(d.getDeliveryDetails());
			if(isTheDeliveryReady){
				d.setDeliveryStatus("Taken_By_Warehouse");
				boolean successfullyUpdated = mainApp.getController().getDatabase().updateDeliveryStatus(d);
				if(successfullyUpdated){
					deliveriesToTakeData.remove(d);
					UtilityClass.showAlert(AlertType.INFORMATION, "Sukces", "Pomyœlnie przyjêto dostawê", "Dostawa zosta³a przyjêta pomyœlnie", currentStage).showAndWait();
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d przyjêcia dostawy", "Podczas aktualizowania rekordu wyst¹pi³ b³¹d", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d przyjêcia dostawy", "Co najmniej jeden z produktów nie zosta³ przyjêty ca³kowicie!", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczonej dostawy", "Proszê zaznaczyæ dostawê do przyjêcia!", currentStage).showAndWait();
		}

	}
	
	private boolean areTheProductsTaken(ObservableList<DeliveryDetails> deliveryDetailsData){
		for(DeliveryDetails dd : deliveryDetailsData){
			if(dd.getQuantity() - dd.getQuantityTaken() != 0) return false;
		} 
		return true;
	}
	
	private boolean showTakeProductOverview(DeliveryDetails dd, int deliveryID){
		TakeTheProductOverviewController controller = (TakeTheProductOverviewController) UtilityClass.showDialogFactory("view/TakeTheProductOverview.fxml", dialogStage, mainApp, currentStage, "Przyjmij produkt");
		controller.setDeliveryDetail(dd);
		controller.setDeliveryID(deliveryID);
		controller.initializeData();
		controller.getCurrentStage().showAndWait();
		return controller.isProductFullyTaken();
		
	}
	private void initializeDeliveriesToTakeTable(){
		deliveryIDColumn_DTT.setCellValueFactory(cellData->cellData.getValue().getDeliveryIDProperty().asObject());;
		deliveryArrivalDateColumn_DTT.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getDeliveryArrivalDate().toString()));
		supplierIDColumn_DTT.setCellValueFactory(cellData->cellData.getValue().getSupplier().getSupplierIDProperty().asObject());
		supplierCompanyNameColumn_DTT.setCellValueFactory(cellData->cellData.getValue().getSupplier().getCompanyNameProperty());;
		deliveryDriverNameColumn_DTT.setCellValueFactory(cellData->cellData.getValue().getDriverNameProperty());
		palletCountColumn_DTT.setCellValueFactory(cellData->cellData.getValue().getPalletCountProperty().asObject());
		
		deliveriesToTakeData = FXCollections.observableArrayList();
		deliveriesToTakeTable.setItems(deliveriesToTakeData);
		
		initializeDeliveryDetailsTable();
		
		deliveriesToTakeTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Delivery>() {

			@Override
			public void changed(ObservableValue<? extends Delivery> observable, Delivery oldValue, Delivery newValue) {
				if(newValue != null) showDeliveryDetails(newValue.getDeliveryDetails());
				
			}
		});
		
		deliveriesToTakeTable.setRowFactory(new Callback<TableView<Delivery>, TableRow<Delivery>>(){

			@Override
			public TableRow<Delivery> call(TableView<Delivery> param) {
				final TableRow<Delivery> row = new TableRow<>();
				final ContextMenu rowMenu = new ContextMenu();
				final MenuItem showSupplier = new MenuItem("Poka¿ szczegó³y dostawcy");
				final MenuItem showDeliveryDetails = new MenuItem("Poka¿ szczegó³y dostawy");
				showSupplier.setOnAction(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent arg0) {
						SupplierDetailsDialogController controller =  (SupplierDetailsDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/SupplierDetailsDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y dostawcy");
						Delivery item = row.getItem();
						controller.setSupplier(item.getSupplier());
						controller.getCurrentStage().showAndWait();
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
				
				rowMenu.getItems().addAll(showSupplier, showDeliveryDetails);
				row.setContextMenu(rowMenu);
				
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
						.then(rowMenu)
						.otherwise((ContextMenu) null));
				return row;
			}
    		
    	});
	}
	private void initializeDeliveryDetailsTable(){
		productIDColumn_DDT.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductIDProperty().asObject());
		productNameColumn_DDT.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductNameProperty());
		deliveredQuantityColumn_DDT.setCellValueFactory(cellData->cellData.getValue().getQuantityProperty().asObject());;
		quantityTakenColumn_DDT.setCellValueFactory(cellData->cellData.getValue().getQuantityTakenProperty().asObject());
		
		
		
		quantityToBeTakenColumn_DTT.setCellValueFactory(cellData-> {
			DeliveryDetails data = cellData.getValue(); 
			return Bindings.createIntegerBinding(() -> {
				try{
					int quantityDelivered = data.getQuantity();
					int quantityTaken = data.getQuantityTaken();
					return quantityDelivered - quantityTaken;
				} catch(NumberFormatException nfe){
					nfe.printStackTrace();
					return null;
				}
			}, data.getQuantityProperty(), data.getQuantityTakenProperty());
		});
		
		palletCountColumn_DDT.setCellValueFactory(cellData->cellData.getValue().getPalletCountProperty().asObject());
		
		deliveryDetailsTable.setRowFactory(new Callback<TableView<DeliveryDetails>, TableRow<DeliveryDetails>>(){
			@Override
			public TableRow<DeliveryDetails> call(TableView<DeliveryDetails> arg0) {
				final TableRow<DeliveryDetails> row = new TableRow<>();
				final ContextMenu rowMenu = new ContextMenu();
				final MenuItem showProductDetails = new MenuItem("Poka¿ szczegó³y produktu");
				showProductDetails.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent arg0) {
						ProductDetailedInfoDialogController controller = (ProductDetailedInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ProductDetailedInfoDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y produktu");
						controller.setProduct(row.getItem().getProduct());
						controller.getCurrentStage().showAndWait();
					}
				});
				
				rowMenu.getItems().addAll(showProductDetails);
				row.setContextMenu(rowMenu);
				
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
						.then(rowMenu)
						.otherwise((ContextMenu) null));
				
				return row;
			}
		});
		deliveryDetailsData = FXCollections.observableArrayList();
		deliveryDetailsTable.setItems(deliveryDetailsData);
	}
	
	private void showDeliveryDetails(ObservableList<DeliveryDetails> dd){
		deliveryDetailsData.clear();
		deliveryDetailsData.addAll(dd);
	}
	

}
