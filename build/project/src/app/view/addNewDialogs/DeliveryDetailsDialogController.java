package app.view.addNewDialogs;



import app.model.Delivery;
import app.model.DeliveryDetails;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.beans.binding.Bindings;
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

public class DeliveryDetailsDialogController extends DialogBasedController {

	@FXML
	private TextField deliveryIDField;
	@FXML
	private TextField supplierField;
	@FXML
	private TextField employeeField;
	@FXML
	private TextField deliveryDateField;
	@FXML
	private TextField arrivalDateField;
	@FXML
	private TextField palletsCountField;
	@FXML
	private TextField driverNameField;
	@FXML
	private TextField deliveryStatusField;
	
	@FXML
	private TableView<DeliveryDetails> deliveryDetailsTable;
	@FXML
	private TableColumn<DeliveryDetails, String> productNameColumn;
	@FXML
	private TableColumn<DeliveryDetails, Integer> quantityColumn;
	@FXML
	private TableColumn<DeliveryDetails, Integer> palletsCountColumn;
	
	private Delivery delivery;
	
	public void setDelivery(Delivery delivery){
		this.delivery = delivery;
		if(delivery != null) fillTheFields();
	}

	private void fillTheFields(){
		deliveryIDField.setText(String.valueOf(delivery.getDeliveryID()));
		supplierField.setText(delivery.getSupplier().getCompanyName());
		employeeField.setText(delivery.getEmployee().getFullNameProperty().get());
		deliveryDateField.setText(delivery.getRequiredDeliveryDate().toString());
		if(delivery.getDeliveryArrivalDate() != null) arrivalDateField.setText(delivery.getDeliveryArrivalDate().toString());
		else arrivalDateField.setText("Brak");
		
		palletsCountField.setText(String.valueOf(delivery.getPalletCount()));
		driverNameField.setText(delivery.getDriverName());
		deliveryStatusField.setText(delivery.getDeliveryStatus().getPolishVersion());
		
		initializeDeliveryDetailsTable();
	}
	
	private void initializeDeliveryDetailsTable(){
		productNameColumn.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductNameProperty());
		quantityColumn.setCellValueFactory(cellData->cellData.getValue().getQuantityProperty().asObject());
		palletsCountColumn.setCellValueFactory(cellData->cellData.getValue().getPalletCountProperty().asObject());
		
		deliveryDetailsTable.setRowFactory(new Callback<TableView<DeliveryDetails>, TableRow<DeliveryDetails>>(){
			@Override
			public TableRow<DeliveryDetails> call(TableView<DeliveryDetails> param) {
				final TableRow<DeliveryDetails> row = new TableRow<>();
				final ContextMenu rowMenu = new ContextMenu();
				final MenuItem showProductDetailsItem = new MenuItem("Poka¿ szczegó³y produktu");
				showProductDetailsItem.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						ProductDetailedInfoDialogController controller = (ProductDetailedInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ProductDetailedInfoDialog.fxml", dialogStage, mainApp, getCurrentStage(), "Szczegó³y produktu");
						controller.setProduct(row.getItem().getProduct());
						controller.getCurrentStage().showAndWait();
					}
				});
				rowMenu.getItems().add(showProductDetailsItem);
				row.setContextMenu(rowMenu);
				
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
								.then(rowMenu)
								.otherwise((ContextMenu) null));
				return row;
			}
			
			
		});
		deliveryDetailsTable.setItems(delivery.getDeliveryDetails());
		
		
	}
	@FXML
	private void handleOKClick(){
		getCurrentStage().close();
	}
}
