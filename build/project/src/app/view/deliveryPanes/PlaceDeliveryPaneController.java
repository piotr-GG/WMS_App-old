package app.view.deliveryPanes;

import java.time.LocalDate;
import java.util.Optional;

import app.model.Delivery;
import app.model.DeliveryDetails;
import app.model.Employee;
import app.model.Product;
import app.model.Supplier;
import app.utility.UtilityClass;
import app.view.inheritance.ActionController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class PlaceDeliveryPaneController extends ActionController {


	
	/*
	 * Place delivery order part
	 * 
	 * 
	 * 
	 * 
	 */
	@FXML
	private AnchorPane dragNDropPane;
	@FXML
	private Label dragNDropLabel;
	@FXML
	private TableView<DeliveryDetails> productTable_PT;
	@FXML
	private TableColumn<DeliveryDetails, Integer> productIDColumn_PT;
	@FXML
	private TableColumn<DeliveryDetails, String> productNameColumn_PT;
	@FXML
	private TableColumn<DeliveryDetails, Integer> orderedQuantityColumn_PT;
	@FXML
	private TableColumn<DeliveryDetails, Double> unitPriceColumn_PT;
	
	@FXML
	private DatePicker deliveryDatePicker;
	@FXML
	private TextField supplierCompanyNameTF;
	@FXML
	private TextField supplierRepresentativeNameTF;
	@FXML
	private TextField supplierCityTF;
	@FXML
	private TextField supplierCountryTF;
	
	private Supplier chosenSupplier;
	private ObservableList<DeliveryDetails> productTableData;
	
	@FXML
	private void initialize(){
		dragNDropPane.setVisible(false);
		deliveryDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				if(newValue != null){
					if(newValue.isBefore(LocalDate.now())){
						UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna data", "Data dostawy nie mo�e by� wcze�niej ni� dzie� dzisiejszy", currentStage).showAndWait();
					}
				}
				
			}
		});
		initializeProductTable();
	}
	
	private void initializeProductTable(){
		
		productTable_PT.setRowFactory(new Callback<TableView<DeliveryDetails>, TableRow<DeliveryDetails>>(){
			public TableRow<DeliveryDetails> call(TableView<DeliveryDetails> paramP){
				return new TableRow<DeliveryDetails>(){
					protected void updateItem(DeliveryDetails paramT, boolean paramBoolean) {
                        this.setOnDragDetected(new EventHandler<MouseEvent>(){

							@Override
							public void handle(MouseEvent event) {
								Dragboard db = startDragAndDrop(TransferMode.ANY);
								ClipboardContent content = new ClipboardContent();
								content.putString(String.valueOf(paramT.getProduct().getProductID()));
								db.setContent(content);
								dragNDropPane.setVisible(true);
								event.consume();
							}
                        	
                        });

                        super.updateItem(paramT, paramBoolean);
					}
				};
			}
		});
		productIDColumn_PT.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductIDProperty().asObject());
		productNameColumn_PT.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductNameProperty());
		orderedQuantityColumn_PT.setCellValueFactory(cellData->cellData.getValue().getQuantityProperty().asObject());
		unitPriceColumn_PT.setCellValueFactory(cellData->cellData.getValue().getProduct().getUnitPriceProperty().asObject());
		
		productTableData = FXCollections.observableArrayList();
		productTable_PT.setItems(productTableData);
		
		dragNDropPane.setPickOnBounds(true);
		
		
		dragNDropPane.setOnDragOver(new EventHandler<DragEvent>(){

			@Override
			public void handle(DragEvent event) {
				if(event.getGestureSource() != dragNDropPane){
					event.acceptTransferModes(TransferMode.ANY);
				}
				event.consume();
				
			}
			
		});
		dragNDropPane.setOnDragEntered(new EventHandler<DragEvent>(){
			@Override
			public void handle(DragEvent event) {
					dragNDropPane.getStyleClass().add("dragOverHover");
					event.consume();
			}
			
		});
		

		
		dragNDropPane.setOnDragDropped(new EventHandler<DragEvent>(){
			@Override
			public void handle(DragEvent event) {
					dragNDropPane.getStyleClass().clear();
					dragNDropPane.setVisible(false);
					Dragboard db = event.getDragboard();
					int productID = Integer.parseInt(db.getString());
					for(DeliveryDetails dd : productTableData){
						if(dd.getProduct().getProductID() == productID) productTableData.remove(dd);
					}
					event.consume();
				
			}
		});
		
		dragNDropPane.setOnDragExited(new EventHandler<DragEvent>(){
			@Override
			public void handle(DragEvent event) {
				dragNDropPane.getStyleClass().clear();
				dragNDropPane.getStyleClass().add("dragOver");
				event.consume();
			}
		});
		
		MenuItem menuDel = new MenuItem("Usu� rekord");
		menuDel.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent t) {
		    	if(!productTableData.isEmpty()){
		        DeliveryDetails item = productTableData.get(productTable_PT.getSelectionModel().getSelectedIndex());
		        if (item != null){ 
		            productTableData.remove(item);
		        }
		    }
		  }
		});
		
		productTable_PT.setContextMenu(new ContextMenu(menuDel));
	}
	
	@FXML
	private void handleAddProductClick(){
		Optional<String> input = UtilityClass.showTextInputDialog("Dodaj produkt", "Podaj ID produktu", "Prosz� poda� identyfikator produktu, kt�ry ma zosta� dodany do dostawy: ", currentStage);
		if(input.isPresent()){
			boolean isParsable = UtilityClass.isParsable(input.get());
			if(isParsable){
				int productID = Integer.parseInt(input.get());
				Product product = mainApp.getController().getDatabase().getProductByID(productID);
				if(product != null){
					boolean containsProduct = containsProduct(productTableData, product);
					if(!containsProduct){
						input = UtilityClass.showTextInputDialog("Dodaj produkt", "Podaj zamawian� ilo�� produktu", "Podaj zamawian� ilo�� nast�puj�cego produktu " + product.getProductName() + "   ", currentStage);
						if(input.isPresent()){
							isParsable = UtilityClass.isParsable(input.get());
							if(isParsable){
								int quantity = Integer.parseInt(input.get());
								if(quantity > 0){
									DeliveryDetails dd = new DeliveryDetails(product, quantity);
									productTableData.add(dd);
									UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Pomy�lnie dodano produkt", "Produkt zosta� pomy�lnie dodany do zlecenia dostawy", currentStage).showAndWait();
								} else {
									UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Prosz� poda� liczb� ca�kowit� wi�ksz� od zera odpowiadaj�cej zamawianej ilo�ci produktu", currentStage).showAndWait();
								}
							} else {
								UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Prosz� poda� liczb� ca�kowit� odpowiadaj�c� zamawianej ilo�ci produktu", currentStage).showAndWait();
							}
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak warto�ci", "Brak podanej warto�ci zamawianej ilo�ci produktu", currentStage).showAndWait();
						}
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B��d", "Produkt ju� dodany", "Produkt o podanym ID zosta� ju� dodany do zam�wienia", currentStage).showAndWait();
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak produktu", "W bazie danych nie istnieje produkt o podanym ID", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Prosz� poda� liczb� ca�kowit� odpowiadaj�c� identyfikatorowi produktu", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak warto�ci", "Brak podanej warto�ci ID produktu", currentStage).showAndWait();
		}
	}
	
	private boolean containsProduct(ObservableList<DeliveryDetails> productData, Product product){
		for(DeliveryDetails od : productData){
			Product p = od.getProduct();
			if(p.getProductID() == product.getProductID()) return true;
		}
		return false;
	}
	
	
	@FXML
	private void handleRemoveProductClick(){
		if(!productTableData.isEmpty()){
		 DeliveryDetails item = productTable_PT.getSelectionModel().getSelectedItem();
	        if (item != null){ 
	            productTableData.remove(item);
	        } else {
	        	UtilityClass.showAlert(AlertType.WARNING, "B��d", "Brak zaznaczonego elementu", "Prosz� zaznaczy� elementy do usuni�cia!", currentStage).showAndWait();
	        }
		} else {
			UtilityClass.showAlert(AlertType.WARNING, "B��d", "Brak element�w w tabeli", "W tabeli nie ma element�w do usuni�cia", currentStage).showAndWait();
		}
		
	}
	
	
	
	
	@FXML
	private void handleAddSupplierClick(){
		if(chosenSupplier == null){
		Optional<String> input = UtilityClass.showTextInputDialog("Dodaj dostawc�", "Podaj ID dostawcy", "Prosz� poda� ID dostawcy, kt�ry ma zosta� przypisany do zlecenia", currentStage);
		if(input.isPresent()){
			boolean isParsable = UtilityClass.isParsable(input.get());
			if(isParsable){
				int supplierID = Integer.parseInt(input.get());
				Supplier supplier = mainApp.getController().getDatabase().getSupplierByID(supplierID);
				if(supplier != null){
					Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Potwierdzenie", "Potwierd� wyb�r dostawcy", "\nZnaleziono dostawc� o nazwie firmy: " + supplier.getCompanyName() + "\nPotwierd� wyb�r dostawcy", currentStage).showAndWait();
					if(result.get() == ButtonType.OK){
						chosenSupplier =  supplier;
						supplierCompanyNameTF.textProperty().bind(chosenSupplier.getCompanyNameProperty());
						supplierRepresentativeNameTF.textProperty().bind(chosenSupplier.getRepresentativeProperty());
						supplierCityTF.textProperty().bind(chosenSupplier.getCityProperty());
						supplierCountryTF.textProperty().bind(chosenSupplier.getCountryProperty());
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Brak dostawcy", "W bazie danych nie istnieje dostawca o podanym ID", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B��dna warto��", "B��dna warto�� ID dostawcy", "Prosz� poda� ID dostawcy b�d�ce liczb� ca�kowit�", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Brak warto�ci", "Brak podanego ID dostawcy", "Prosz� poda� ID dostawcy", currentStage).showAndWait();
		}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Dostawca ju� przypisany", "\nPrzed zmian� dostawcy prosz� usun�� aktualnego dostawc� za pomoc� odpowiedniego przycisku\n", currentStage).showAndWait();
		}
	}  
	@FXML
	private void handleRemoveSupplierClick(){
		if(chosenSupplier != null){
			clearSupplierData();
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak dostawcy", "\nBrak przypisanego dostawcy, kt�rego mo�naby usun��\n", currentStage).showAndWait();
		}
	}
	
	@FXML
	private void handleInsertOrderClick(){
		if(!productTableData.isEmpty()){
			if(chosenSupplier != null){
				if(deliveryDatePicker.getValue() != null){
					Optional<String> input = UtilityClass.showTextInputDialog("Komunikat", "Podaj ilo�� palet", "Prosz� poda� planowan� ilo�� palet", currentStage);
					if(input.isPresent()){
						boolean isParsable = UtilityClass.isParsable(input.get());
						if(isParsable){
							int palletCount = Integer.parseInt(input.get());
							
							int nextID = mainApp.getController().getDatabase().getNextDeliveryID();
							Employee employee = mainApp.getController().getLoggedUser().getEmployee();
							LocalDate requiredDeliveryDate = deliveryDatePicker.getValue();
							Delivery delivery = new Delivery(nextID, chosenSupplier, employee, palletCount, productTableData, "Order_Is_Placed", requiredDeliveryDate);
							boolean successfullyInserted = mainApp.getController().getDatabase().insertDeliveryRecord(delivery);
							if(successfullyInserted) UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Pomy�lnie dodano dostaw�", "Dostawa zosta�a dodana pomy�lnie\nID z�o�onego zlecenia dostawy : " + String.valueOf(nextID), currentStage).showAndWait();
							else UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��d podczas dodawania rekordu", "Podczas dodawania rekordu wyst�pi� b��d", currentStage).showAndWait();
							clearPlaceDeliveryTab();
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak warto�ci", "Prosz� poda� liczb� ca�kowit� okre�laj�c� ilo�� palet", currentStage).showAndWait();
						}
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak warto�ci", "Prosz� poda� liczb� palet", currentStage).showAndWait();
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak daty dostawy", "Prosz� doda� dat� dostawy", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak dostawcy!", "Prosz� doda� dostawc� obs�uguj�cego t� dostaw�", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak produkt�w!", "Prosz� doda� produkty do zlecenia dostawy!", currentStage).showAndWait();
		}
	}
	
	private void clearPlaceDeliveryTab(){
		productTableData.clear();
		clearSupplierData();
		deliveryDatePicker.setValue(null);
		
	}
	
	private void clearSupplierData(){
		if(chosenSupplier != null){
			supplierCompanyNameTF.textProperty().unbind();
			supplierRepresentativeNameTF.textProperty().unbind();
			supplierCityTF.textProperty().unbind();
			supplierCountryTF.textProperty().unbind();
			supplierCompanyNameTF.setText("");
			supplierRepresentativeNameTF.setText("");
			supplierCityTF.setText("");
			supplierCountryTF.setText("");
			chosenSupplier = null;
		}
	}
}
