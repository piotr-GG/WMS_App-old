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
    	
    	MenuItem showProductDetails = new MenuItem("Poka� szczeg�y produktu");
    	
    	showProductDetails.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				OrderDetails od = insOrdTable.getSelectionModel().getSelectedItem() ;
				if(od != null){
					ProductDetailedInfoDialogController controller = (ProductDetailedInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ProductDetailedInfoDialog.fxml", dialogStage, mainApp, currentStage, "Szczeg�y produktu");
					controller.setProduct(od.getProduct());
					controller.getCurrentStage().showAndWait();
				}
    	}});
    	
		insOrdTable.setRowFactory(new Callback<TableView<OrderDetails>, TableRow<OrderDetails>>(){
			
			public TableRow<OrderDetails> call(TableView<OrderDetails> paramP){
				
				 final TableRow<OrderDetails> row = new TableRow<>();
				 final ContextMenu productDetailsMenu = new ContextMenu();
				 final MenuItem showProductDetails = new MenuItem("Poka� szczeg�y produktu");
			    	
			     showProductDetails.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						OrderDetails od = insOrdTable.getSelectionModel().getSelectedItem();
						if(od != null){
							ProductDetailedInfoDialogController controller = (ProductDetailedInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ProductDetailedInfoDialog.fxml", dialogStage, mainApp, currentStage, "Szczeg�y produktu");
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
		Optional<String> input = UtilityClass.showTextInputDialog("Komunikat", "Wprowad� identyfikator produktu", "Prosz� poda� identyfikator produktu: ", currentStage);
		if(input.isPresent()){
			boolean isParsable = UtilityClass.isParsable(input.get());
			if(isParsable){
				int productID = Integer.parseInt(input.get());
				Product product = mainApp.getController().getDatabase().getProductByID(productID);
				if(insOrdTable.getItems().isEmpty()){
				if(product != null){
					addProductProcedure(product);
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak produktu", "W bazie danych nie istnieje produkt o podanym ID", currentStage).showAndWait();
				}
			 } else {
				 boolean containsProduct = containsProduct(insOrdTable.getItems(), product);
				 
				 if(containsProduct){
					 UtilityClass.showAlert(AlertType.ERROR, "B��d", "Produkt ju� dodany", "Produkt o podanym ID zosta� ju� dodany do zam�wienia", currentStage).showAndWait();
				 } else {
					 addProductProcedure(product);
				 }
			 }
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Prosz� poda� liczb� ca�kowit� odpowiadaj�c� identyfikatorowi produktu", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak warto�ci", "Brak podanej warto�ci ID produktu", currentStage).showAndWait();
		}
	}	
	
	@FXML
	private void handleRemoveProduct(){
		if(!insOrdTableData.isEmpty()){
			OrderDetails item = insOrdTable.getSelectionModel().getSelectedItem();
			if(item != null){
				insOrdTableData.remove(item);
				UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Usuni�to produkt", "Produkt zosta� usuni�ty z zam�wienia!", currentStage).showAndWait();
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak zaznaczonego produktu", "Prosz� zaznaczy� produkt do usuni�cia!", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak produkt�w", "Brak produkt�w w tabeli do usuni�cia!", currentStage).showAndWait();
		}
	}
	
	private void addProductProcedure(Product product){
		UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Dodaj produkt", "Znaleziono produkt o podanym ID. W celu kontynuowania prosz� klikn�� przycisk OK w oknie widoku produktu", currentStage).showAndWait();
		ProductDetailedInfoDialogController controller = (ProductDetailedInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ProductDetailedInfoDialog.fxml", dialogStage, mainApp, currentStage, "Szczeg�y produktu");
		controller.setProduct(product);
		controller.getCurrentStage().showAndWait();
		if(controller.isOkClicked()){
			Optional<String> quantityInput = UtilityClass.showTextInputDialog("Komunikat", "Wprowad� ilo��", "Prosz� poda� ilo�� zam�wionego produktu: ", currentStage);
			if(quantityInput.isPresent()){
				boolean isParsable = UtilityClass.isParsable(quantityInput.get());
				if(isParsable){
					int quantity = Integer.parseInt(quantityInput.get());
					if(quantity > 0){
						Optional<String> discountInput = UtilityClass.showTextInputDialog("Komunikat", "Wprowad� warto�� rabatu", "Prosz� poda� warto�� rabatu na zam�wiony produkt: ", currentStage);
						if(discountInput.isPresent()){
							isParsable = UtilityClass.isDoubleParsable(discountInput.get());
							if(isParsable){
								double discount = Double.parseDouble(discountInput.get());
								if(discount >= 0 && discount <= 1){
									OrderDetails od = new OrderDetails(product, quantity, product.getUnitPrice(), discount);
									insOrdTableData.add(od);
								} else {
									UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Warto�� rabatu musi by� liczb� rzeczywist� zawart� w przedziale <0,1>", currentStage).showAndWait();
								}
							} else {
								UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Prosz� poda� liczb� rzeczywist� w przedziale <0,1> odpowiadaj�c� rabatowi na produkt", currentStage).showAndWait();
							}
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak", "Brak podanej warto�ci", currentStage).showAndWait();
						}
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Prosz� poda� liczb� ca�kowit� wi�ksz� od zera", currentStage).showAndWait();
						}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Prosz� poda� liczb� ca�kowit� odpowiadaj�c� zam�wionej ilo�ci produktu", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Prosz� poda� liczb� ca�kowit� odpowiadaj�c� zam�wionej ilo�ci produktu", currentStage).showAndWait();
			}
				
		}
	}
		
	

	
	@FXML
	private void addCustomer(){
		Optional<String> input = UtilityClass.showTextInputDialog("Komunikat", "Wprowad� identyfikator klienta", "Podaj identyfikator klienta: ", currentStage);
		if(input.isPresent()){
		String customerID = input.get();
		if(!customerID.isEmpty()){
			insertOrderCustomer = mainApp.getController().getDatabase().getCustomerByID(customerID);
			if(insertOrderCustomer != null){
				String contentText = "\nPrzed dodaniem klienta prosz� potwierdzi� przypisanie do zam�wienia\n\nZnaleziony klient: " +  insertOrderCustomer.getCompanyName();
				Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Potwierdzenie", "Potwierd� klienta", contentText, currentStage).showAndWait();
				if(result.get() == ButtonType.OK){
					insOrdCustomerField.textProperty().bind(insertOrderCustomer.getCompanyNameProperty());

				} else {

				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Klient o podanym ID nie istnieje!", "Prosz� poda� prawid�ow� warto�� identyfikatora klienta", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Podana warto�� tekstowa jest pusta", currentStage).showAndWait();
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
				Optional<ButtonType> wannaContinue = UtilityClass.showAlert(AlertType.CONFIRMATION, "Komunikat", "Kontynuuj polecenie", "\nCzy na pewno chcesz wprowadzi� zam�wienie?\n", currentStage).showAndWait();
				if(wannaContinue.get() == ButtonType.OK){
					NewOrderDialogController controller = (NewOrderDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewOrderDialog.fxml", dialogStage, mainApp, currentStage, "Wprowad� nowe zam�wienie");
					
					
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
				UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak przypisanego klienta", "\nPrzed wprowadzeniem zam�wienia nale�y przypisa� klienta , ffs\n", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak przypisanych produkt�w", "\nPrzed wprowadzeniem zam�wienia nale�y wprowadzi� do tabeli co najmniej jeden produkt\n", currentStage).showAndWait();
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
