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
						UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna data", "Data dostawy nie mo¿e byæ wczeœniej ni¿ dzieñ dzisiejszy", currentStage).showAndWait();
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
		
		MenuItem menuDel = new MenuItem("Usuñ rekord");
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
		Optional<String> input = UtilityClass.showTextInputDialog("Dodaj produkt", "Podaj ID produktu", "Proszê podaæ identyfikator produktu, który ma zostaæ dodany do dostawy: ", currentStage);
		if(input.isPresent()){
			boolean isParsable = UtilityClass.isParsable(input.get());
			if(isParsable){
				int productID = Integer.parseInt(input.get());
				Product product = mainApp.getController().getDatabase().getProductByID(productID);
				if(product != null){
					boolean containsProduct = containsProduct(productTableData, product);
					if(!containsProduct){
						input = UtilityClass.showTextInputDialog("Dodaj produkt", "Podaj zamawian¹ iloœæ produktu", "Podaj zamawian¹ iloœæ nastêpuj¹cego produktu " + product.getProductName() + "   ", currentStage);
						if(input.isPresent()){
							isParsable = UtilityClass.isParsable(input.get());
							if(isParsable){
								int quantity = Integer.parseInt(input.get());
								if(quantity > 0){
									DeliveryDetails dd = new DeliveryDetails(product, quantity);
									productTableData.add(dd);
									UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Pomyœlnie dodano produkt", "Produkt zosta³ pomyœlnie dodany do zlecenia dostawy", currentStage).showAndWait();
								} else {
									UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Proszê podaæ liczbê ca³kowit¹ wiêksz¹ od zera odpowiadaj¹cej zamawianej iloœci produktu", currentStage).showAndWait();
								}
							} else {
								UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Proszê podaæ liczbê ca³kowit¹ odpowiadaj¹c¹ zamawianej iloœci produktu", currentStage).showAndWait();
							}
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Brak podanej wartoœci zamawianej iloœci produktu", currentStage).showAndWait();
						}
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Produkt ju¿ dodany", "Produkt o podanym ID zosta³ ju¿ dodany do zamówienia", currentStage).showAndWait();
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak produktu", "W bazie danych nie istnieje produkt o podanym ID", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Proszê podaæ liczbê ca³kowit¹ odpowiadaj¹c¹ identyfikatorowi produktu", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Brak podanej wartoœci ID produktu", currentStage).showAndWait();
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
	        	UtilityClass.showAlert(AlertType.WARNING, "B³¹d", "Brak zaznaczonego elementu", "Proszê zaznaczyæ elementy do usuniêcia!", currentStage).showAndWait();
	        }
		} else {
			UtilityClass.showAlert(AlertType.WARNING, "B³¹d", "Brak elementów w tabeli", "W tabeli nie ma elementów do usuniêcia", currentStage).showAndWait();
		}
		
	}
	
	
	
	
	@FXML
	private void handleAddSupplierClick(){
		if(chosenSupplier == null){
		Optional<String> input = UtilityClass.showTextInputDialog("Dodaj dostawcê", "Podaj ID dostawcy", "Proszê podaæ ID dostawcy, który ma zostaæ przypisany do zlecenia", currentStage);
		if(input.isPresent()){
			boolean isParsable = UtilityClass.isParsable(input.get());
			if(isParsable){
				int supplierID = Integer.parseInt(input.get());
				Supplier supplier = mainApp.getController().getDatabase().getSupplierByID(supplierID);
				if(supplier != null){
					Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Potwierdzenie", "PotwierdŸ wybór dostawcy", "\nZnaleziono dostawcê o nazwie firmy: " + supplier.getCompanyName() + "\nPotwierdŸ wybór dostawcy", currentStage).showAndWait();
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
				UtilityClass.showAlert(AlertType.ERROR, "B³êdna wartoœæ", "B³êdna wartoœæ ID dostawcy", "Proszê podaæ ID dostawcy bêd¹ce liczb¹ ca³kowit¹", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Brak wartoœci", "Brak podanego ID dostawcy", "Proszê podaæ ID dostawcy", currentStage).showAndWait();
		}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Dostawca ju¿ przypisany", "\nPrzed zmian¹ dostawcy proszê usun¹æ aktualnego dostawcê za pomoc¹ odpowiedniego przycisku\n", currentStage).showAndWait();
		}
	}  
	@FXML
	private void handleRemoveSupplierClick(){
		if(chosenSupplier != null){
			clearSupplierData();
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak dostawcy", "\nBrak przypisanego dostawcy, którego mo¿naby usun¹æ\n", currentStage).showAndWait();
		}
	}
	
	@FXML
	private void handleInsertOrderClick(){
		if(!productTableData.isEmpty()){
			if(chosenSupplier != null){
				if(deliveryDatePicker.getValue() != null){
					Optional<String> input = UtilityClass.showTextInputDialog("Komunikat", "Podaj iloœæ palet", "Proszê podaæ planowan¹ iloœæ palet", currentStage);
					if(input.isPresent()){
						boolean isParsable = UtilityClass.isParsable(input.get());
						if(isParsable){
							int palletCount = Integer.parseInt(input.get());
							
							int nextID = mainApp.getController().getDatabase().getNextDeliveryID();
							Employee employee = mainApp.getController().getLoggedUser().getEmployee();
							LocalDate requiredDeliveryDate = deliveryDatePicker.getValue();
							Delivery delivery = new Delivery(nextID, chosenSupplier, employee, palletCount, productTableData, "Order_Is_Placed", requiredDeliveryDate);
							boolean successfullyInserted = mainApp.getController().getDatabase().insertDeliveryRecord(delivery);
							if(successfullyInserted) UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Pomyœlnie dodano dostawê", "Dostawa zosta³a dodana pomyœlnie\nID z³o¿onego zlecenia dostawy : " + String.valueOf(nextID), currentStage).showAndWait();
							else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d podczas dodawania rekordu", "Podczas dodawania rekordu wyst¹pi³ b³¹d", currentStage).showAndWait();
							clearPlaceDeliveryTab();
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Proszê podaæ liczbê ca³kowit¹ okreœlaj¹c¹ iloœæ palet", currentStage).showAndWait();
						}
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Proszê podaæ liczbê palet", currentStage).showAndWait();
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak daty dostawy", "Proszê dodaæ datê dostawy", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak dostawcy!", "Proszê dodaæ dostawcê obs³uguj¹cego tê dostawê", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak produktów!", "Proszê dodaæ produkty do zlecenia dostawy!", currentStage).showAndWait();
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
