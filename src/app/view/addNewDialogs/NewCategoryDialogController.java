package app.view.addNewDialogs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import app.model.Category;
import app.model.Product;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class NewCategoryDialogController extends DialogBasedController{
	
	@FXML
	private TextField categoryNameField;
	@FXML
	private TextArea categoryDescriptionArea;
	@FXML
	private ImageView categoryPhotoImageView;
	
	@FXML
	private TableView<Product> newProductsTable;
	@FXML
	private TableColumn<Product, String> productNameColumn;
	@FXML
	private TableColumn<Product, String> categoryNameColumn;
	@FXML
	private TableColumn<Product, String> quantityPerUnitColumn;
	@FXML
	private TableColumn<Product, Double> unitPriceColumn;
	
	private ObservableList<Product> newProductsData;
	
	private File categoryPhoto = null;
	
	@FXML
	private void initialize(){
		initializeNewProductsTable();
	}

	private void initializeNewProductsTable(){
		productNameColumn.setCellValueFactory(cellData->cellData.getValue().getProductNameProperty());
		categoryNameColumn.setCellValueFactory(cellData->cellData.getValue().getCategory().getCategoryNameProperty());
		quantityPerUnitColumn.setCellValueFactory(cellData->cellData.getValue().getQuantityPerUnitProperty());
		unitPriceColumn.setCellValueFactory(cellData->cellData.getValue().getUnitPriceProperty().asObject());
		
		newProductsData = FXCollections.observableArrayList();
		newProductsTable.setItems(newProductsData);
	}
	
	
	@FXML
	private void handleChooseCategoryPhoto(){
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Wgraj obrazek kategorii");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("plik graficzny", "*.jpg", "*.png", "*.gif" )
		);
		File file = fileChooser.showSaveDialog(dialogStage);
		
		if(file != null){
			try {
				categoryPhotoImageView.setImage(new Image(new FileInputStream(file)));
				categoryPhoto = file;
			} catch (FileNotFoundException e) {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak takiego pliku", "Taki plik nie istnieje lub nie jest plikiem graficznym", getCurrentStage()).showAndWait();
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	private void handleAddProduct(){
		NewProductDialogController controller = (NewProductDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewProductDialog.fxml", dialogStage, mainApp, getCurrentStage(), "Dodaj produkt");
		controller.setNewCategoryMode(true);
		controller.newProductMode();
		controller.getCurrentStage().setResizable(false);
		controller.getCurrentStage().showAndWait();
		Product product = controller.getProduct();
		if(product != null){
			newProductsData.add(product);
		}
	}
	
	@FXML
	private void handleAddCategory(){
		if(isInputValid()){
			if(newProductsData.isEmpty()){	
				Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Potwierdzenie", "Brak dodanych produktów", "Czy na pewno chcesz dodaæ kategoriê bez ¿adnych produktów?", getCurrentStage()).showAndWait();
				if(result.get() == ButtonType.OK){
					addCategory();
				}
			} else {
				Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Potwierdzenie", "Dodaj now¹ kategoriê", "Czy na pewno chcesz dodaæ now¹ kategoriê", getCurrentStage()).showAndWait();
				if(result.get() == ButtonType.OK){
					addCategory();
				}
			}
		}
	}
	
	private void addCategory(){
		int nextID = mainApp.getController().getDatabase().getNextCategoryID();
		
		Image categoryImage = null;
		
		if(categoryPhoto != null){
			try {
				categoryImage = new Image(new FileInputStream(categoryPhoto));
			} catch (FileNotFoundException e) {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d obrazka kategorii", "Podczas zapisywania obrazka kategorii wyst¹pi³ nieoczekiwany b³¹d", getCurrentStage()).showAndWait();
				e.printStackTrace();
			}
		}

		Category category = new Category(nextID, categoryNameField.getText(), categoryDescriptionArea.getText(), categoryImage);
		
		boolean successfullyInserted = mainApp.getController().getDatabase().insertCategoryRecord(category, categoryPhoto);
		if(!newProductsData.isEmpty()){
			int productNextID = mainApp.getController().getDatabase().getNextProductID();
			for(Product p : newProductsData) {
				p.setCategory(category);
				p.setProductID(productNextID++);
				mainApp.getController().getDatabase().insertProductRecord(p);
			}
		}
		
		if(successfullyInserted) UtilityClass.showAlert(AlertType.INFORMATION, "Nowa kategoria", "Pomyœlnie dodano kategoriê", "Nowa kategoria o nazwie " + category.getCategoryName() + " i ID: " + String.valueOf(category.getCategoryID() + " zosta³a pomyœlnie dodana"), getCurrentStage()).showAndWait();
		else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d przy dodawaniu kategorii", "Wyst¹pi³ b³¹d podczas dodawania nowej kategorii", getCurrentStage()).showAndWait();
		
		handleExit();
	}
	
	@FXML
	private void handleExit(){
		getCurrentStage().close();
	}
	
	private boolean isInputValid(){
		String errMsg = "";
		if(categoryNameField.getText().length() == 0){
			errMsg += "Pole nazwy kategorii jest puste!\n";
		}
		if(categoryDescriptionArea.getText().length() == 0){
			errMsg += "Pole opisu kategorii jest puste!\n";
		}
		
		if(errMsg.length() == 0){
			return true;
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Proszê poprawiæ b³êdy w podanych polach", errMsg, getCurrentStage()).showAndWait();
			return false;
		}
		
	}
	
}
