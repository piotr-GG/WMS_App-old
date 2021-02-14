package app.view.addNewDialogs;

import java.util.Optional;

import app.MainApp;
import app.model.Category;
import app.model.Product;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewProductDialogController extends DialogBasedController {
	
	//Product data part
	@FXML
	private TextField productIDField;
	@FXML 
	private TextField productNameField;
	@FXML
	private TextField quantityPerUnitField;
	@FXML
	private TextField unitPriceField;
	@FXML
	private TextField unitsOnStockField;
	@FXML
	private TextField reorderLevelField;
	@FXML
	private ComboBox<Boolean> discontinuedBox;
	@FXML
	private ComboBox<String> categoryBox;
	@FXML
	private Button executeButton;
	@FXML
	private Label dialogLabel;
	
	//Product details part
	
	@FXML
	private TextField weightField;
	@FXML
	private TextField volumeField;
	@FXML
	private TextField lengthField;
	@FXML
	private TextField widthField;
	@FXML
	private TextField heightField;
	@FXML
	private ComboBox<String> suggestedContainerBox;
	@FXML
	private TextArea organolepticExaminationArea;
	
	
	private Product product = null;
	
	private boolean newCategoryMode = false;
	
	private boolean newProductMode = true;
	

	private void initializeData(){
		productIDField.setDisable(true);
		discontinuedBox.getItems().addAll(Boolean.TRUE, Boolean.FALSE);
		suggestedContainerBox.getItems().addAll("Paleta", "Pude�ko", "Ch�odnia");
	}
	

	public void newProductMode(){
		newProductMode = true;
		initializeData();
		if(newCategoryMode) setNewCategoryData();
		else setNewProductData();
	}
	
	public void editProductMode(Product product){
		newProductMode = false;
		if(product != null){
			this.product = product;
			initializeData();
			setEditProductData();
		}
	}
	
	/**
	 * EDIT PRODUCT MODE
	 */
	private void setEditProductData(){
		
		executeButton.setText("Zapisz zmiany");
		dialogLabel.setText("Edytuj produkt");
		
		productIDField.setText(String.valueOf(product.getProductID()));
		productNameField.setText(product.getProductName());
		quantityPerUnitField.setText(product.getQuantityPerUnit());
		unitPriceField.setText(String.valueOf(product.getUnitPrice()));
		unitsOnStockField.setText(String.valueOf(product.getUnitsInStock()));
		reorderLevelField.setText(String.valueOf(product.getReorderLevel()));
		discontinuedBox.getSelectionModel().select(Boolean.valueOf(product.getDiscontinued()));
		categoryBox.getItems().addAll(mainApp.getController().getDatabase().getCategoryNames());
		categoryBox.getSelectionModel().select(product.getCategory().getCategoryName());
	}
	
	/**
	 * NEW CATEGORY MODE
	 */
	private void setNewCategoryData(){
		categoryBox.getItems().clear();
		categoryBox.getItems().add("Nowa kategoria");
		categoryBox.getSelectionModel().selectFirst();
		categoryBox.setDisable(true);
		productIDField.setText("0");
	}
	
	/**
	 * NEW PRODUCT MODE
	 */
	
	private void setNewProductData(){
		int nextProductID = mainApp.getController().getDatabase().getNextProductID();
		categoryBox.getItems().addAll(mainApp.getController().getDatabase().getCategoryNames());
		productIDField.setText(String.valueOf(nextProductID));
	}
	
	/**
	 * Used only by NewCategoryDialog in order to stop inserting new product of new category into DB 
	 */
	
	public void setNewCategoryMode(boolean newCategoryMode){
		this.newCategoryMode = newCategoryMode;
		
		
	}
	
	public Product getProduct(){
		return this.product;
	}
	
	@FXML
	private void handleExecute(){
		boolean isInputValid = isInputValid();
		if(isInputValid){
			if(newProductMode) addNewProductProcedure();
			else editProductProcedure();
		}
	}
	
	private void addNewProductProcedure(){
		int productID = Integer.parseInt(productIDField.getText());
		String productName = productNameField.getText();
		
		String categoryName = categoryBox.getSelectionModel().getSelectedItem();

		
		String quantityPerUnit = quantityPerUnitField.getText();
		double unitPrice = Double.parseDouble(unitPriceField.getText());
		int unitsOnStock = Integer.parseInt(unitsOnStockField.getText());
		int reorderLevel = Integer.parseInt(reorderLevelField.getText());
		boolean discontinued = discontinuedBox.getValue().booleanValue();
		
		
		Category category = null;
		
		if(newCategoryMode == false){
			category = mainApp.getController().getDatabase().getCategoryByName(categoryName);
		} else {
			category = new Category("-----");
		}
		
		this.product = new Product(productID, productName, category, quantityPerUnit, unitPrice, unitsOnStock, reorderLevel, discontinued);
		
		if(newCategoryMode == false){
			boolean successfullyInserted = mainApp.getController().getDatabase().insertProductRecord(product);
			if(successfullyInserted) UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Dodano nowy produkt", "Nowy produkt zosta� pomy�lnie dodany", getCurrentStage()).showAndWait();
			else UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Wyst�pi� b��d", "Podczas dodawania produktu wyst�pi� b��d", getCurrentStage()).showAndWait();
		} 

		getCurrentStage().close();
	}

	private void editProductProcedure(){
		
		int productID = product.getProductID();
		String productName = productNameField.getText();	
		String categoryName = categoryBox.getSelectionModel().getSelectedItem();
		String quantityPerUnit = quantityPerUnitField.getText();
		double unitPrice = Double.parseDouble(unitPriceField.getText());
		int unitsOnStock = Integer.parseInt(unitsOnStockField.getText());
		int reorderLevel = Integer.parseInt(reorderLevelField.getText());
		boolean discontinued = discontinuedBox.getValue().booleanValue();
		Category category = mainApp.getController().getDatabase().getCategoryByName(categoryName);
		
		Product newProduct = new Product(productID, productName, category, quantityPerUnit, unitPrice, unitsOnStock, reorderLevel, discontinued);
		boolean isProductEdited = !product.areProductsEqual(newProduct);
		if(isProductEdited){
			Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Edytuj produkt", "Potwierd� polecenie", "Czy na pewno chcesz zapisa� zmiany w produkcie?", getCurrentStage()).showAndWait();
			if(result.get() == ButtonType.OK) {
				boolean successfullyUpdated = mainApp.getController().getDatabase().updateProductRecord(newProduct);
				if(successfullyUpdated){
					UtilityClass.showAlert(AlertType.INFORMATION, "Edytuj produkt", "Pomy�lnie wykonano polecenie", "Zmiany zosta�y pomy�lnie zapisane!", getCurrentStage()).showAndWait();
					product.setProductName(productName);
					product.setCategory(category);
					product.setQuantityPerUnit(quantityPerUnit);
					product.setUnitPrice(unitPrice);
					product.setUnitsInStock(unitsOnStock);
					product.setReorderLevel(reorderLevel);
					product.setDiscontinued(discontinued);
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "Edytuj produkt", "Wyst�pi� b��d", "Podczas zapisywania zmian wyst�pi� b��d!", getCurrentStage()).showAndWait();
				}
				getCurrentStage().close();
			}

		} 
		
	}
	

	
	@FXML
	private void handleCancel(){
		getCurrentStage().close();
	}
	
	
	
	private boolean isInputValid(){
		String errorString = "";
		if(productIDField.getText().isEmpty() || !UtilityClass.isParsable(productIDField.getText())){
			errorString += "Brak podanego ID produktu lub podane ID nie jest liczb� ca�kowit�\n";
		} 
		if(productNameField.getText().isEmpty()){
			errorString += "Brak podanej nazwy produktu\n";
		}
		if(quantityPerUnitField.getText().isEmpty()){
			errorString += "Brak podanej ilo�ci jednostkowej\n";
		}
		if(unitPriceField.getText().isEmpty() || !UtilityClass.isDoubleParsable(unitPriceField.getText())){
			errorString += "Brak podanej ceny lub podana cena nie jest liczb� rzeczywist�\n";
		}
		if(unitsOnStockField.getText().isEmpty() || !UtilityClass.isParsable(unitsOnStockField.getText())){
			errorString += "Brak podanego stanu magazynowego lub podana liczba nie jest liczb� ca�kowit�\n";
		}
		if(reorderLevelField.getText().isEmpty() || !UtilityClass.isParsable(reorderLevelField.getText())){
			errorString += "Brak podanego minimalnego stanu dopusczalnego lub podana liczba nie jest liczb� ca�kowit�\n";
		}
		if(discontinuedBox.getSelectionModel().getSelectedItem() == null){
			errorString += "Brak zaznaczonego pola Wycofany\n";
		}
		if(categoryBox.getSelectionModel().getSelectedItem() == null){
			errorString += "Brak zaznaczonej kategorii produktu\n";
		}
		if(weightField.getText().isEmpty() || !UtilityClass.isDoubleParsable(weightField.getText())){
			errorString += "Brak podanej wagi produktu lub podana warto�� nie jest liczb� rzeczywist�\n";
		}
		if(volumeField.getText().isEmpty() || !UtilityClass.isDoubleParsable(volumeField.getText())){
			errorString += "Brak podanej obj�to�ci produktu lub podana warto�� nie jest liczb� rzeczywist�\n";
		}
		if(lengthField.getText().isEmpty() || !UtilityClass.isDoubleParsable(lengthField.getText())){
			errorString += "Brak podanej d�ugo�ci produktu lub podana warto�� nie jest liczb� rzeczywist�\n";			
		}
		if(widthField.getText().isEmpty() || !UtilityClass.isDoubleParsable(widthField.getText())){
			errorString += "Brak podanej szeroko�ci produktu lub podana warto�� nie jest liczb� rzeczywist�\n";
		}
		if(heightField.getText().isEmpty() || !UtilityClass.isDoubleParsable(heightField.getText())){
			errorString += "Brak podanej wysoko�ci produktu lub podana warto�� nie jest liczb� rzeczywist�\n";
		}
		if(suggestedContainerBox.getSelectionModel().getSelectedItem() == null){
			errorString += "Brak zaznaczonego pola Sugerowane opakowanie transportowe\n";
		}
		if(organolepticExaminationArea.getText().isEmpty()){
			errorString += "Brak wype�nionego pola Badanie organoleptyczne\n";
		}
		if(errorString.length() == 0){
			return true;
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Prosz� poprawi� poni�sze b��dy we wprowadzonych warto�ciach", errorString, getCurrentStage()).showAndWait();
			return false;
		}
	}
	
	@FXML
	private void seeMoreAboutMeasuringClick(){
		/*
		MeasuringInfoDialogController controller = (MeasuringInfoDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/MeasuringInfoDialog.fxml", dialogStage, mainApp, getCurrentStage(), "Pomiary produkt�w");
		controller.getCurrentStage().setResizable(false);
		controller.getCurrentStage().setTitle("Badanie organoleptyczne - pomoc");
		controller.getCurrentStage().showAndWait();	
		*/
	}
	
}
