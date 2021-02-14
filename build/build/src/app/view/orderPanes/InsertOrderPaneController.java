package app.view.orderPanes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import app.MainApp;
import app.model.Customer;
import app.model.DeliveryDetails;
import app.model.Employee;
import app.model.Order;
import app.model.OrderDetails;
import app.model.Product;
import app.utility.UtilityClass;
import app.view.NewOrderDialogController;
import app.view.addNewDialogs.NewCustomerDialogController;
import app.view.addNewDialogs.ProductDetailedInfoDialogController;
import app.view.inheritance.ActionController;
import app.view.inheritance.DialogBasedController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class InsertOrderPaneController extends ActionController {

	@FXML
	private TableView<OrderDetails> insOrdTable;
	@FXML
	private TableColumn <OrderDetails, String> insOrdProductColumn;
	@FXML
	private TableColumn <OrderDetails, Integer> insOrdQuantityColumn;
	@FXML
	private TableColumn <OrderDetails, Double > insOrdUnitPriceColumn;
	@FXML
	private TableColumn <OrderDetails, Double> insOrdDiscountColumn;
	@FXML
	private TextField insOrdCustomerField;
	@FXML
	private TextField insOrdCustomerIDField;
	
	private Customer insertOrderCustomer;
	
	private ObservableList<OrderDetails> insOrdTableData;
	@FXML
	private void initialize(){
		initializeInsertOrderTable();
	}
	
    private void initializeInsertOrderTable(){

    	insOrdTableData = FXCollections.observableArrayList();
    	insOrdProductColumn.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductNameProperty());
    	insOrdQuantityColumn.setCellValueFactory(cellData->cellData.getValue().getQuantityProperty().asObject());
    	insOrdUnitPriceColumn.setCellValueFactory(cellData->cellData.getValue().getUnitPriceProperty().asObject());
    	insOrdDiscountColumn.setCellValueFactory(cellData->cellData.getValue().getDiscountProperty().asObject());
    	insOrdTable.setItems(insOrdTableData);
    	
    	MenuItem showProductDetails = new MenuItem("Poka¿ szczegó³y produktu");
    	
    	showProductDetails.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				OrderDetails od = insOrdTable.getSelectionModel().getSelectedItem() ;
				if(od != null){
					ProductDetailedInfoDialogController controller = (ProductDetailedInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ProductDetailedInfoDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y produktu");
					controller.setProduct(od.getProduct());
					controller.getCurrentStage().showAndWait();
				}
    	}});
    	
		insOrdTable.setRowFactory(new Callback<TableView<OrderDetails>, TableRow<OrderDetails>>(){
			
			public TableRow<OrderDetails> call(TableView<OrderDetails> paramP){
				
				 final TableRow<OrderDetails> row = new TableRow<>();
				 final ContextMenu productDetailsMenu = new ContextMenu();
				 final MenuItem showProductDetails = new MenuItem("Poka¿ szczegó³y produktu");
			    	
			     showProductDetails.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						OrderDetails od = insOrdTable.getSelectionModel().getSelectedItem();
						if(od != null){
							ProductDetailedInfoDialogController controller = (ProductDetailedInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ProductDetailedInfoDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y produktu");
							controller.setProduct(od.getProduct());
							controller.getCurrentStage().showAndWait();
						}
			    	}});
				   productDetailsMenu.getItems().addAll(showProductDetails);

				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
						.then(productDetailsMenu)
						.otherwise((ContextMenu) null));
				
				return row;
			}
		});
    	
    	
    	
    	
    }
    
	/*
	 * Insert order event handlers
	 */
	@FXML
	private void addProduct(){
		Optional<String> input = UtilityClass.showTextInputDialog("Komunikat", "WprowadŸ identyfikator produktu", "Proszê podaæ identyfikator produktu: ", currentStage);
		if(input.isPresent()){
			boolean isParsable = UtilityClass.isParsable(input.get());
			if(isParsable){
				int productID = Integer.parseInt(input.get());
				Product product = mainApp.getController().getDatabase().getProductByID(productID);
				if(insOrdTable.getItems().isEmpty()){
				if(product != null){
					addProductProcedure(product);
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak produktu", "W bazie danych nie istnieje produkt o podanym ID", currentStage).showAndWait();
				}
			 } else {
				 boolean containsProduct = containsProduct(insOrdTable.getItems(), product);
				 
				 if(containsProduct){
					 UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Produkt ju¿ dodany", "Produkt o podanym ID zosta³ ju¿ dodany do zamówienia", currentStage).showAndWait();
				 } else {
					 addProductProcedure(product);
				 }
			 }
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Proszê podaæ liczbê ca³kowit¹ odpowiadaj¹c¹ identyfikatorowi produktu", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Brak podanej wartoœci ID produktu", currentStage).showAndWait();
		}
	}	
	
	@FXML
	private void handleRemoveProduct(){
		if(!insOrdTableData.isEmpty()){
			OrderDetails item = insOrdTable.getSelectionModel().getSelectedItem();
			if(item != null){
				insOrdTableData.remove(item);
				UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Usuniêto produkt", "Produkt zosta³ usuniêty z zamówienia!", currentStage).showAndWait();
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczonego produktu", "Proszê zaznaczyæ produkt do usuniêcia!", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak produktów", "Brak produktów w tabeli do usuniêcia!", currentStage).showAndWait();
		}
	}
	
	private void addProductProcedure(Product product){
		UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Dodaj produkt", "Znaleziono produkt o podanym ID. W celu kontynuowania proszê klikn¹æ przycisk OK w oknie widoku produktu", currentStage).showAndWait();
		ProductDetailedInfoDialogController controller = (ProductDetailedInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ProductDetailedInfoDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y produktu");
		controller.setProduct(product);
		controller.getCurrentStage().showAndWait();
		if(controller.isOkClicked()){
			Optional<String> quantityInput = UtilityClass.showTextInputDialog("Komunikat", "WprowadŸ iloœæ", "Proszê podaæ iloœæ zamówionego produktu: ", currentStage);
			if(quantityInput.isPresent()){
				boolean isParsable = UtilityClass.isParsable(quantityInput.get());
				if(isParsable){
					int quantity = Integer.parseInt(quantityInput.get());
					if(quantity > 0){
						Optional<String> discountInput = UtilityClass.showTextInputDialog("Komunikat", "WprowadŸ wartoœæ rabatu", "Proszê podaæ wartoœæ rabatu na zamówiony produkt: ", currentStage);
						if(discountInput.isPresent()){
							isParsable = UtilityClass.isDoubleParsable(discountInput.get());
							if(isParsable){
								double discount = Double.parseDouble(discountInput.get());
								if(discount >= 0 && discount <= 1){
									OrderDetails od = new OrderDetails(product, quantity, product.getUnitPrice(), discount);
									insOrdTableData.add(od);
								} else {
									UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Wartoœæ rabatu musi byæ liczb¹ rzeczywist¹ zawart¹ w przedziale <0,1>", currentStage).showAndWait();
								}
							} else {
								UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Proszê podaæ liczbê rzeczywist¹ w przedziale <0,1> odpowiadaj¹c¹ rabatowi na produkt", currentStage).showAndWait();
							}
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak", "Brak podanej wartoœci", currentStage).showAndWait();
						}
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Proszê podaæ liczbê ca³kowit¹ wiêksz¹ od zera", currentStage).showAndWait();
						}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Proszê podaæ liczbê ca³kowit¹ odpowiadaj¹c¹ zamówionej iloœci produktu", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Proszê podaæ liczbê ca³kowit¹ odpowiadaj¹c¹ zamówionej iloœci produktu", currentStage).showAndWait();
			}
				
		}
	}
		
	

	
	@FXML
	private void addCustomer(){
		Optional<String> input = UtilityClass.showTextInputDialog("Komunikat", "WprowadŸ identyfikator klienta", "Podaj identyfikator klienta: ", currentStage);
		if(input.isPresent()){
		String customerID = input.get();
		if(!customerID.isEmpty()){
			insertOrderCustomer = mainApp.getController().getDatabase().getCustomerByID(customerID);
			if(insertOrderCustomer != null){
				String contentText = "\nPrzed dodaniem klienta proszê potwierdziæ przypisanie do zamówienia\n\nZnaleziony klient: " +  insertOrderCustomer.getCompanyName();
				Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Potwierdzenie", "PotwierdŸ klienta", contentText, currentStage).showAndWait();
				if(result.get() == ButtonType.OK){
					insOrdCustomerField.textProperty().bind(insertOrderCustomer.getCompanyNameProperty());

				} else {

				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Klient o podanym ID nie istnieje!", "Proszê podaæ prawid³ow¹ wartoœæ identyfikatora klienta", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Podana wartoœæ tekstowa jest pusta", currentStage).showAndWait();
		}
	  }
		
	}
	
	@FXML
	private void newCustomer(){
		NewCustomerDialogController controller = (NewCustomerDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewCustomerDialog.fxml", dialogStage, mainApp, ownerStage, "Dodaj nowego klienta");
		controller.getCurrentStage().showAndWait();
		if(controller.isCustomerAdded()){
			Customer customer = controller.getCustomer();
			insOrdCustomerField.setText(customer.getCompanyName());
		} 
		
		
	}
	
	@FXML
	private void insertOrder(){
		ObservableList<OrderDetails> orderDetails = insOrdTable.getItems();
		if(!orderDetails.isEmpty()){
			if(!insOrdCustomerField.getText().isEmpty()){
				Optional<ButtonType> wannaContinue = UtilityClass.showAlert(AlertType.CONFIRMATION, "Komunikat", "Kontynuuj polecenie", "\nCzy na pewno chcesz wprowadziæ zamówienie?\n", currentStage).showAndWait();
				if(wannaContinue.get() == ButtonType.OK){
					NewOrderDialogController controller = (NewOrderDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewOrderDialog.fxml", dialogStage, mainApp, currentStage, "WprowadŸ nowe zamówienie");
					
					
					Employee employee = mainApp.getController().getLoggedUser().getEmployee();
					LocalDate orderDate = LocalDate.now();
					String orderStatus = "Inserted";
					
					Order initializedOrder = new Order(insertOrderCustomer, employee, orderDate, orderStatus, orderDetails);
					
					controller.setOrder(initializedOrder);
					controller.fillTheFields(false);
					controller.getCurrentStage().showAndWait();
					if(controller.isNewOrderAdded()) {
						insOrdTable.getItems().clear();
						insOrdCustomerField.textProperty().unbind();
						insOrdCustomerField.setText("");
						insertOrderCustomer = null;
					}
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak przypisanego klienta", "\nPrzed wprowadzeniem zamówienia nale¿y przypisaæ klienta , ffs\n", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak przypisanych produktów", "\nPrzed wprowadzeniem zamówienia nale¿y wprowadziæ do tabeli co najmniej jeden produkt\n", currentStage).showAndWait();
		}
		

	}
	
	private boolean containsProduct(ObservableList<OrderDetails> productData, Product product){
		for(OrderDetails od : productData){
			Product p = od.getProduct();
			if(p.getProductID() == product.getProductID()) return true;
		}
		return false;
	}
}
