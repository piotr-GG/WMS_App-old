package app.view;

import java.io.IOException;

import app.MainApp;
import app.model.Category;
import app.model.Location;
import app.model.Product;
import app.model.Supplier;
import app.utility.UtilityClass;
import app.view.addNewDialogs.NewCategoryDialogController;
import app.view.addNewDialogs.NewLocationDialogController;
import app.view.addNewDialogs.NewProductDialogController;
import app.view.addNewDialogs.NewSupplierDialogController;
import app.view.inheritance.ActionController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductsQueryOverviewController extends ActionController {

	/**
	 * Variables associated with controller itself
	 */
	
	private Stage detailedInfoDialog;
	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private TabPane tabbedPane;
	
	@FXML
	private Tab queryTab;
	@FXML
	private Tab resultTab;
	
	@FXML
	private RadioButton productInfoRB;
	@FXML
	private ComboBox<String> productInfoCBox;
	@FXML
	private Label productInfoBoxLabel;
	@FXML
	private TextField productInfoField;
	@FXML
	private Label productInfoFieldLabel;
	
	@FXML
	private RadioButton suppliersOfProductRB;
	@FXML
	private ComboBox<String> suppliersOfProductCBox;
	@FXML
	private Label suppliersOfProductBoxLabel;
	@FXML
	private TextField suppliersOfProductField;
	@FXML
	private Label suppliersOfProductFieldLabel;
	
	@FXML
	private ComboBox<String> getCategorizedProductsCBox;
	@FXML
	private Label getCategorizedProductsBoxLabel;
	@FXML
	private RadioButton getCategorizedProductsRB;

	@FXML
	private RadioButton getProductsBelowMinimalStockRB;

	@FXML
	private RadioButton getLocationsOfProductRB;
	@FXML
	private ComboBox<String> getLocationsOfProductCBox;
	@FXML
	private Label getLocationsOfProductBoxLabel;
	@FXML
	private TextField getLocationsOfProductField;
	@FXML
	private Label getLocationsOfProductFieldLabel;
	
	@FXML
	private Button executeButton;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableView resultTableView;
	@FXML
	private Button editButton;
	@FXML
	private Label queryLabel;
	@FXML
	private Label searchProductLabel;
	@FXML
	private GridPane detailsPane;
	
	private ToggleGroup queryGroup;
	
	private ObservableList<Product> productData;
	private ObservableList<Category> categoryData;
	private ObservableList<Location> locationData;
	private ObservableList<Supplier> supplierData;
	
	
	@FXML
	private void initialize(){
		
		detailsPane.setHgap(10);
		detailsPane.setVgap(10);
		detailsPane.getStyleClass().add("detailsGPane");
		
		
		productData = FXCollections.observableArrayList();
		categoryData = FXCollections.observableArrayList();
		locationData = FXCollections.observableArrayList();
		supplierData = FXCollections.observableArrayList();
		
		queryGroup = new ToggleGroup();
		
		/*
		 * Search for product query controls
		 */
		productInfoCBox.getItems().addAll("wg nazwy", "wg ID");
		productInfoCBox.getSelectionModel().select(0);
		productInfoRB.setToggleGroup(queryGroup);
		/*
		 * Get all suppliers of particular product controls
		 */
		suppliersOfProductCBox.getItems().addAll("wg nazwy", "wg ID");
		suppliersOfProductCBox.getSelectionModel().select(0);
		suppliersOfProductRB.setToggleGroup(queryGroup);
		/*
		 * Get all products by categories controls
		 */
		getCategorizedProductsRB.setToggleGroup(queryGroup);
		/*
		 * Get products with stock below the minimal value controls
		 */
		getProductsBelowMinimalStockRB.setToggleGroup(queryGroup);
		/*
		 * Get all locations of product controls
		 */
		getLocationsOfProductRB.setToggleGroup(queryGroup);
		getLocationsOfProductCBox.getItems().addAll("wg nazwy", "wg ID");
		getLocationsOfProductCBox.getSelectionModel().select(0);
		
		MenuItem showEditRecord = new MenuItem("Edytuj rekord");
		showEditRecord.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				handleEditClick();
			}
		});
		
		resultTableView.setContextMenu(new ContextMenu(showEditRecord));
		resultTableView.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER){
					handleEditClick();
				}
				
			}
		});
		
		UtilityClass.LabelledControlFactory(productInfoRB, new Label[]{productInfoBoxLabel, productInfoFieldLabel});
		UtilityClass.LabelledControlFactory(suppliersOfProductRB, new Label[]{suppliersOfProductBoxLabel, suppliersOfProductFieldLabel});
		UtilityClass.LabelledControlFactory(getCategorizedProductsRB, new Label[]{getCategorizedProductsBoxLabel});
		UtilityClass.LabelledControlFactory(getLocationsOfProductRB, new Label[]{getLocationsOfProductBoxLabel, getLocationsOfProductFieldLabel});
	}
		
	public void initializeData(){
		updateCSSStyles();
		mainApp.getController().getStyleSheetPathProperty().addListener(changeListener -> {
			updateCSSStyles();
		});
	}
	
	private void updateCSSStyles(){
		mainPane.getStylesheets().clear();
		mainPane.getStylesheets().add(mainApp.getController().getStyleSheetPath());
	}
	
    @FXML
    private void handleExecuteQueryClick(){
    	
    	boolean queryHasResults = false;
    	
    	if (!resultTableView.getColumns().isEmpty()) {
    		//clear the columns that are no longer in need
			resultTableView.getColumns().clear();

		}
    	
    	if(!detailsPane.getChildren().isEmpty()){
    		//clear the details before executing new query
    		detailsPane.getChildren().clear();
    	}
    	
    	if(productInfoRB.isSelected()) queryHasResults = showProductByNameOrID();
    	if(getCategorizedProductsRB.isSelected()) queryHasResults = showCategorizedProducts();
    	if(getProductsBelowMinimalStockRB.isSelected()) queryHasResults = showBelowMinProducts();
    	if(getLocationsOfProductRB.isSelected()) queryHasResults = showLocationsOfProduct();
    	if(suppliersOfProductRB.isSelected()) queryHasResults = showSuppliersOfProduct();
     	
    	if(queryHasResults){
    		tabbedPane.getSelectionModel().select(1);
    		setQueryLabel();
    	}
    		
    				
   }
    

    private boolean showProductByNameOrID(){
    	boolean queryHasResults = false;
    	productData.clear();
    	
    	//Search for a product by its name or ID
    	
    	Product result = null;

    	String productInfoText = productInfoField.getText();
    	if(productInfoText.length() != 0) {
    		if(productInfoCBox.getSelectionModel().getSelectedItem().equals("wg nazwy")){
    			result = mainApp.getController().getDatabase().getProductByName(productInfoField.getText());
    		} else if (productInfoCBox.getSelectionModel().getSelectedItem().equals("wg ID")){
    			boolean isParsable = UtilityClass.isParsable(productInfoText);
    			if(isParsable){
    				int productID = Integer.parseInt(productInfoText);
    				result = mainApp.getController().getDatabase().getProductByID(productID);
    			} else {
    				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ ID zamówienia", "Proszê podaæ liczbê ca³kowit¹ odpowiadaj¹c¹ identyfikatorowi zamówienia", currentStage).showAndWait();
    				return false;
    			}
    		}
    	
    		if(result != null){
    			productData.add(result);
    		}
    	
    		//Check if the query has returned some results and show appropriate alert
    		queryHasResults = checkQueryResults(productData.size());
    	
    		//Show product data
    		setProductTableView();
    	} else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak podanej wartoœci", "Proszê podaæ wartoœæ w odpowiednim polu tekstowym!", currentStage).showAndWait();
    		
    	return queryHasResults;
    }
    
    private boolean showCategorizedProducts(){
    	boolean queryHasResults = false;
    	
    	productData.clear();
    	ObservableList<Product> result = mainApp.getController().getDatabase().getCategorizedProducts(getCategorizedProductsCBox.getSelectionModel().getSelectedItem().toString());
    	
    	//Check if the query has returned some results and show appropriate alert
    	if(result != null) productData.addAll(result);
    	
		queryHasResults = checkQueryResults(productData.size());
		//Show product data
		setProductTableView();
		return queryHasResults;
    }
    
    /**
     * Get list of products whose unit on stock quantity is below minimal value
     * @return
     */
    
    private boolean showBelowMinProducts(){
       	boolean queryHasResults = false;
    	
       	productData.clear();
    	//Get product Data
    	ObservableList<Product> result = mainApp.getController().getDatabase().getProductsBelowMinStockValue();
    	//Check if the query has returned some results and show appropriate alert
    	if(result != null) productData.addAll(result);
    	queryHasResults = checkQueryResults(result.size());
    	
    	//Show product data
    	setProductTableView();
    	return queryHasResults;
    }
    

    /**
     * Get all locations of a product 
     * @return
     */

	private boolean showLocationsOfProduct (){
    	
    	boolean queryHasResults = false;
    	locationData.clear();
    	ObservableList<Location> result = FXCollections.observableArrayList();
    	//Get location data
    	if(getLocationsOfProductCBox.getSelectionModel().getSelectedItem().equals("wg nazwy")){
    		String productName = getLocationsOfProductField.getText();
    		if(productName.length() == 0) {
    			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak nazwy produktu", "Proszê podaæ nazwê produktu!", currentStage).showAndWait();
    			return false;
    		}
    		else result = mainApp.getController().getDatabase().getProductLocationsByName(productName);
    	}
    	else if (getLocationsOfProductCBox.getSelectionModel().getSelectedItem().equals("wg ID")) {
    		boolean isParsable = UtilityClass.isParsable(getLocationsOfProductField.getText());
    		if(isParsable){
    			int productID = Integer.parseInt(getLocationsOfProductField.getText());
    			result = mainApp.getController().getDatabase().getProductLocationsByID(productID);
    		} else {
    			UtilityClass.showAlert(AlertType.ERROR, "B³êdna wartoœæ!", "B³êdna wartoœæ ID produktu", "Proszê podaæ liczbê ca³kowit¹ bêd¹c¹ identyfikatorem produktu!", currentStage).showAndWait();
    			return false;
    		}
    		
    	}

    	queryHasResults = checkQueryResults(result.size());
    	if(queryHasResults) {
    		locationData.addAll(result);
    		searchProductLabel.setText("Poszukiwany produkt: " + locationData.get(0).getProductsStored().get(0).getProductName() );
    	}
    	setLocationTableView();
    	return queryHasResults;
    }
    

    /**
     * Get all suppliers of a product
     * @return
     */
    private boolean showSuppliersOfProduct(){
    	boolean queryHasResults = false;
    	
    	supplierData.clear();
    	ObservableList<Supplier> result = FXCollections.observableArrayList();
    	//Get supplier data 
    	String productFieldText = suppliersOfProductField.getText();
    	if (suppliersOfProductCBox.getSelectionModel().getSelectedItem().equals("wg nazwy")){
    	
    		if(productFieldText.length() != 0)  result = mainApp.getController().getDatabase().getSuppliersByProductName(productFieldText);
    		else {
    			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak nazwy produktu", "Proszê podaæ nazwê produktu!",currentStage).showAndWait();
    			return false;
    		}
    		
    	} else if (suppliersOfProductCBox.getSelectionModel().getSelectedItem().equals("wg ID")){
    		boolean isParsable = UtilityClass.isParsable(productFieldText);
    		if(isParsable) {
    			int productID = Integer.parseInt(productFieldText);
    			result = mainApp.getController().getDatabase().getSuppliersByProductID(productID);
    		} else {
    			UtilityClass.showAlert(AlertType.ERROR, "B³êdna wartoœæ!", "B³êdna wartoœæ ID produktu", "Proszê podaæ liczbê ca³kowit¹ bêd¹c¹ identyfikatorem produktu!", currentStage).showAndWait();
    			return false;
    		}
    	}
    	
    	queryHasResults = checkQueryResults(result.size());
    	if(queryHasResults) supplierData.addAll(result);
    	
    	setSupplierTableView();
    	return queryHasResults;
    }
 
    /**
     * Checks if the query has returned results and shows appropriate alert
     * @return
     */
    private boolean checkQueryResults(int arraySize){
    	
    	boolean queryHasResults = false;
    	if(arraySize != 0 ){
			//Query has been executed properly and returned some results
			queryHasResults = true;
			UtilityClass.showAlert(AlertType.INFORMATION, "Operacja zakoñczona powodzeniem", "Pomyœlnie wykonano zapytanie", "Liczba zwróconych wyników zapytania: "+ arraySize, currentStage).showAndWait();
		} else {
			//Query has returned no results 
			queryHasResults = false;
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wyników zapytania", "Zapytanie nie zwróci³o ¿adnych wyników" , currentStage).showAndWait();
		}
    	return queryHasResults;
    }
    
	public void setCategoryInfo(){
		//get data for combo box
		getCategorizedProductsCBox.getItems().clear();
		getCategorizedProductsCBox.getItems().addAll(mainApp.getController().getDatabase().getCategoryNames());
		getCategorizedProductsCBox.getSelectionModel().select(0);
	}
	
    private void setQueryLabel(){
		RadioButton selectedRB = (RadioButton) queryGroup.getSelectedToggle();
		queryLabel.setText(selectedRB.getText());
    }
    
    /**
     * Show new edit dialog
     */
    @FXML
    private void handleEditClick(){
    	Object object = resultTableView.getSelectionModel().getSelectedItem();
    	if(object != null){
    	
	    	if(object instanceof Supplier){
	    		showEditSupplierDialog((Supplier) object);
	    		showDetailedSupplierInfo((Supplier) object);
	    	}
	    	if(object instanceof Product){
	    		showEditProductDialog((Product) object);
	    		showDetailedProductInfo((Product) object);
	    	}
	    	if(object instanceof Location){
	    		showEditLocationDialog((Location) object); 
	    		showDetailedLocationInfo((Location) object);
	    	} 
    	
    	} else {
    		UtilityClass.showAlert(AlertType.WARNING, "B³¹d", "Brak zaznaczenia", "Proszê zaznaczyæ odpowiedni rekord do wyœwietlenia szczegó³ów!", currentStage).showAndWait();
    	}
    }
    
    @FXML
    private void handleDetailsClick(){
    	
    }
    
    private void showEditLocationDialog(Location location){
    	NewLocationDialogController controller = (NewLocationDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewLocationDialog.fxml", dialogStage, mainApp, currentStage, "Edytuj lokacjê");
    	controller.setEditLocationMode(true);
    	controller.setEditLocation(location);
    	controller.getCurrentStage().showAndWait();
    }
    
    private void showEditSupplierDialog(Supplier supplier){
		NewSupplierDialogController controller = (NewSupplierDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewSupplierDialog.fxml", dialogStage, mainApp, currentStage, "Dodaj dostawcê");
		controller.setNewSupplierMode(false);
		controller.setSupplier(supplier);
		controller.fillTheFields();
		controller.getCurrentStage().showAndWait();
    }
    
    private void showEditProductDialog(Product product){
    	NewProductDialogController controller = (NewProductDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewProductDialog.fxml", dialogStage, mainApp, currentStage, "Edytuj produkt");
    	controller.editProductMode(product);
    	controller.getCurrentStage().showAndWait();
    }
    /**
     * Puts all the product data into the Grid Pane object so that user can access detailed info about a product
     * @param prod
     */
    private void showDetailedProductInfo(Object prod){
    	if(prod instanceof Product && prod != null){
    		Product p = (Product) prod;
    	

    		detailsPane.getChildren().clear();
    		int size = detailsPane.getRowConstraints().size();
    		Label productIDLabel = new Label("ID");
    		productIDLabel.getStyleClass().add("detailsGPaneLabel");
    		Label productIDValue = new Label(String.valueOf(p.getProductID()));
    		detailsPane.addRow(size++, productIDLabel, productIDValue);
    		
    		Label productNameLabel = new Label("Nazwa produktu");
    		productNameLabel.getStyleClass().add("detailsGPaneLabel");
    		Label productNameValue = new Label(p.getProductName());
    		detailsPane.addRow(size++, productNameLabel, productNameValue);
    		
    		Label categoryLabel = new Label("Kategoria");
    		categoryLabel.getStyleClass().add("detailsGPaneLabel");
    		Label categoryValue = new Label(p.getCategory().getCategoryName());
    		detailsPane.addRow(size++, categoryLabel, categoryValue);
    		
    		Label quantityPerUnitLabel = new Label("Ilosæ jednostkowa");
    		quantityPerUnitLabel.getStyleClass().add("detailsGPaneLabel");
    		Label quantityPerUnitValue = new Label(p.getQuantityPerUnit());
    		detailsPane.addRow(size++, quantityPerUnitLabel, quantityPerUnitValue);
    		
    		Label unitPriceLabel = new Label("Cena jednostkowa");
    		unitPriceLabel.getStyleClass().add("detailsGPaneLabel");
    		Label unitPriceValue = new Label(String.valueOf(p.getUnitPrice()));
    		detailsPane.addRow(size++, unitPriceLabel, unitPriceValue);
    		
    		Label unitsOnStockLabel = new Label("Iloœæ sztuk na magazynie");
    		unitsOnStockLabel.getStyleClass().add("detailsGPaneLabel");
    		Label unitsOnStockValue = new Label(String.valueOf(p.getUnitsInStock()));
    		detailsPane.addRow(size++, unitsOnStockLabel, unitsOnStockValue);
    		
    		Label minQuantityOnStockLabel = new Label("Minimalna dopuszczalna iloœæ");
    		minQuantityOnStockLabel.getStyleClass().add("detailsGPaneLabel");
    		Label minQuantityOnStockValue = new Label(String.valueOf(p.getReorderLevel()));
    		detailsPane.addRow(size++, minQuantityOnStockLabel, minQuantityOnStockValue);
    	
    	}
    }
    
    /**
     * Puts all the location data into the Grid Pane object so that user can access detailed info about a location
     * @param lol
     */
    private void showDetailedLocationInfo(Object lol){
    	
    	if(lol != null && lol instanceof Location){
    		Location l = (Location) lol;
    		detailsPane.getChildren().clear();
    		detailsPane.setHgap(10);
    		detailsPane.setVgap(10);
    		
    		int size = detailsPane.getRowConstraints().size();
    		
    		Label locationIDLabel = new Label("ID lokacji");
    		locationIDLabel.getStyleClass().add("detailsGPaneLabel");
    		Label locationIDValue = new Label(l.getLocationID());
    		detailsPane.addRow(size++, locationIDLabel, locationIDValue);
    		
    		Label acceptableLoadLabel = new Label("Dopuszczalne obci¹¿enie [kg]");
    		acceptableLoadLabel.getStyleClass().add("detailsGPaneLabel");
    		Label acceptableLoadValue = new Label(String.valueOf(l.getAcceptableLoad()));
    		detailsPane.addRow(size++, acceptableLoadLabel, acceptableLoadValue);
    		
    		Label acceptableVolumeLabel = new Label("Dopuszczalna sk³adowana objêtoœæ [m3]");
    		acceptableVolumeLabel.getStyleClass().add("detailsGPaneLabel");
    		Label acceptableVolumeValue = new Label(String.valueOf(l.getAcceptableVolume()));
    		size++;
    		detailsPane.addRow(size++, acceptableVolumeLabel, acceptableVolumeValue);
    		
    		Label productLabel = new Label("Produkty sk³adowane w lokalizacji:");
    		productLabel.getStyleClass().add("detailsGPaneLabel");
    		size += 2;
    		detailsPane.addRow(size++, productLabel);
    		
    		for(Product p : l.getProductsStored()){
    			Label productNameValue = new Label("Produkt: " + p.getProductName());
    			Label productQuantityValue = new Label("Iloœæ: " + p.getUnitsInStock());
    			detailsPane.addRow(size++, productNameValue, productQuantityValue);
    		}
    		

    	}
    }
    
    /**
     * Puts all the supplier data into the Grid Pane object so that user can access detailed info about a supplier
     * @param lol
     */
    private void showDetailedSupplierInfo(Object sup){
    	if(sup != null && sup instanceof Supplier){
    		Supplier s = (Supplier) sup;
    		
    		detailsPane.getChildren().clear();
    		detailsPane.setHgap(10);
    		detailsPane.setVgap(10);
    		int size = detailsPane.getRowConstraints().size();
    		
    		Label supplierIDLabel = new Label("ID dostawcy");
    		supplierIDLabel.getStyleClass().add("detailsGPaneLabel");
    		Label supplierIDValue = new Label(String.valueOf(s.getSupplierID()));
    		detailsPane.addRow(size++, supplierIDLabel, supplierIDValue);
    		
    		Label companyNameLabel = new Label("Nazwa firmy");
    		companyNameLabel.getStyleClass().add("detailsGPaneLabel");
    		Label companyNameValue = new Label(s.getCompanyName());
    		detailsPane.addRow(size++, companyNameLabel, companyNameValue);
    		
    		Label representativeNameLabel = new Label("Przedstawiciel firmy");
    		representativeNameLabel.getStyleClass().add("detailsGPaneLabel");
    		Label representativeNameValue = new Label(s.getRepresentative());
    		detailsPane.addRow(size++, representativeNameLabel, representativeNameValue);
    		
    		
    		Label addressLabel = new Label("Adres");
    		addressLabel.getStyleClass().add("detailsGPaneLabel");
    		Label addressValue = new Label(s.getAddress());
    		detailsPane.addRow(size++, addressLabel, addressValue);
    		
    		Label cityLabel = new Label("Miasto");
    		cityLabel.getStyleClass().add("detailsGPaneLabel");
    		Label cityValue = new Label(s.getCity());
    		detailsPane.addRow(size++, cityLabel, cityValue);
    		 	
    		Label postalCodeLabel = new Label("Kod pocztowy");
    		postalCodeLabel.getStyleClass().add("detailsGPaneLabel");
    		Label postalCodeValue = new Label(s.getPostalCode());
    		detailsPane.addRow(size++, postalCodeLabel, postalCodeValue);
    		
    		Label countryLabel = new Label("Kraj");
    		countryLabel.getStyleClass().add("detailsGPaneLabel");
    		Label countryValue = new Label(s.getCountry());
    		detailsPane.addRow(size++, countryLabel, countryValue);
    		
    		Label phoneNumberLabel = new Label("Telefon");
    		phoneNumberLabel.getStyleClass().add("detailsGPaneLabel");
    		Label phoneNumberValue = new Label(s.getPhoneNumber());
    		detailsPane.addRow(size++, phoneNumberLabel, phoneNumberValue);
    		
    		Label otherLabel = new Label("Inne");
    		otherLabel.getStyleClass().add("detailsGPaneLabel");
    		Label otherValue = new Label(s.getOther());
    		detailsPane.addRow(size++, otherLabel, otherValue);
    		

    	}
    }
    
	@FXML
	private void handleAddNewProductClick(){
		NewProductDialogController controller = (NewProductDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewProductDialog.fxml", dialogStage, mainApp, currentStage, "Dodaj produkt");
		controller.newProductMode();
		controller.getCurrentStage().setResizable(false);
		controller.getCurrentStage().showAndWait();
		
	}
	
	@FXML
	private void handleAddNewCategoryClick(){
		NewCategoryDialogController controller = (NewCategoryDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewCategoryDialog.fxml", dialogStage, mainApp, currentStage, "Dodaj kategoriê");
		controller.getCurrentStage().showAndWait();
	}
	
	@FXML
	private void handleRefreshClick(){
		setCategoryInfo();
	}
	
	@FXML
	private void handleAddNewSupplierClick(){
		NewSupplierDialogController controller = (NewSupplierDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewSupplierDialog.fxml", dialogStage, mainApp, currentStage, "Dodaj dostawcê");
		controller.setNewSupplierMode(true);
		controller.getCurrentStage().showAndWait();
				
	}
	
	   
    private void setSupplierTableView(){
    	TableColumn <Supplier, String> companyName = new TableColumn<>("Nazwa firmy");
    	TableColumn <Supplier, String> address = new TableColumn<>("Adres");
    	TableColumn <Supplier, String> city = new TableColumn<>("Miasto");
    	TableColumn <Supplier, String> country = new TableColumn<>("Kraj");
    	TableColumn <Supplier, String> phoneNumber = new TableColumn<>("Telefon");
    	
    	companyName.setCellValueFactory(cellData->cellData.getValue().getCompanyNameProperty());
    	address.setCellValueFactory(cellData->cellData.getValue().getAddressProperty());
    	city.setCellValueFactory(cellData->cellData.getValue().getCityProperty());
    	country.setCellValueFactory(cellData->cellData.getValue().getCountryProperty());
    	phoneNumber.setCellValueFactory(cellData->cellData.getValue().getPhoneNumberProperty());
    	
    	resultTableView.getColumns().addAll(companyName, address, city, country, phoneNumber);
    	resultTableView.setItems(supplierData);
    		
    	resultTableView.getSelectionModel().selectedItemProperty().addListener(
    			(observable, oldValue, newValue) -> showDetailedSupplierInfo(newValue));
    }
    
    
    /**
     * Sets the proper columns for products to be shown
     */
    
    @SuppressWarnings("unchecked")
	private void setProductTableView(){
		TableColumn <Product, String> productName = new TableColumn<>("Nazwa");
		TableColumn <Product, String> quantityPerUnit = new TableColumn <> ("Iloœæ jednostkowa");
		TableColumn <Product, Double> unitPrice = new TableColumn <> ("Cena jedn."); 
		TableColumn <Product, Integer> unitsInStock = new TableColumn <> ("Iloœæ sztuk na magazynie"); 
 
		productName.setCellValueFactory(cellData->cellData.getValue().getProductNameProperty());
		quantityPerUnit.setCellValueFactory(cellData->cellData.getValue().getQuantityPerUnitProperty());
		unitPrice.setCellValueFactory(cellData->cellData.getValue().getUnitPriceProperty().asObject());
		unitsInStock.setCellValueFactory(cellData->cellData.getValue().getUnitsInStockProperty().asObject());

		resultTableView.setItems(productData);
		resultTableView.getColumns().addAll( productName, quantityPerUnit, unitPrice, unitsInStock);
		
		//show details about the product being currently selected
		resultTableView.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) ->showDetailedProductInfo(newValue));
    }
    
	private void setLocationTableView(){
    	TableColumn <Location, String> locationID = new TableColumn<>("ID lokacji");
    	TableColumn <Location, Integer> acceptableLoad = new TableColumn <>("Dopuszczalne obci¹¿enie");
    	TableColumn <Location, Integer> acceptableVolume = new TableColumn <>("Maksymalna objêtoœæ");
    	
    	locationID.setCellValueFactory(cellData->cellData.getValue().getLocationIDProperty());
    	acceptableLoad.setCellValueFactory(cellData->cellData.getValue().getAcceptableLoadProperty().asObject());
    	acceptableVolume.setCellValueFactory(cellData->cellData.getValue().getAcceptableVolumeProperty().asObject());
    	

    	resultTableView.setItems(locationData);
    	resultTableView.getColumns().clear();
    	resultTableView.getColumns().addAll(locationID, acceptableLoad, acceptableVolume);

    	resultTableView.getSelectionModel().selectedItemProperty().addListener(
    			(observable, oldValue, newValue) -> showDetailedLocationInfo(newValue));
	}
 }
