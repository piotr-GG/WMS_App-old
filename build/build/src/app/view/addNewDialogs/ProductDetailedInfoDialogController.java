package app.view.addNewDialogs;

import app.model.Product;
import app.view.inheritance.DialogBasedController;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class ProductDetailedInfoDialogController extends DialogBasedController {

	@FXML
	private Label categoryLabel;
	@FXML
	private TextField productIDField;
	@FXML
	private TextField productNameField;
	@FXML
	private TextField quantityPerUnitField;
	@FXML
	private TextField priceField;
	@FXML
	private TextField unitsOnStockField;
	@FXML
	private TextField reorderLevelField;
	@FXML
	private ComboBox<Boolean> discontinuedBox;
	
	private Product product;
	
	private boolean okClicked = false;
	@FXML
	private TreeTableView<String> productDetailsTable;
	
	public void setProduct(Product product){
	 this.product = product;
	 setProductInfo();
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	private void initialize(){
		final TreeItem<String> weightColumn = new TreeItem<>("Waga");
		final TreeItem<String> weightValue = new TreeItem<>("15 kg");
		weightColumn.getChildren().setAll(weightValue);
		
		final TreeItem<String> volumeColumn = new TreeItem<>("Objêtoœæ");
		final TreeItem<String> volumeValue = new TreeItem<>("50 cm^3");
		volumeColumn.getChildren().setAll(volumeValue);
		
		final TreeItem<String> dimensionsColumn = new TreeItem<>("Wymiary");
		final TreeItem<String> lengthColumn = new TreeItem<>("D³ugoœæ");
		final TreeItem<String> lengthValue = new TreeItem<>("65 cm");
		lengthColumn.getChildren().setAll(lengthValue);
		
		final TreeItem<String> widthColumn = new TreeItem<>("Szerokoœæ");
		final TreeItem<String> widthValue = new TreeItem<>("50 cm");
		widthColumn.getChildren().setAll(widthValue);
		
		final TreeItem<String> heightColumn = new TreeItem<>("Wysokoœæ");
		final TreeItem<String> heightValue = new TreeItem<>("50 cm");
		heightColumn.getChildren().setAll(heightValue);
		
		dimensionsColumn.getChildren().setAll(lengthColumn, widthColumn, heightColumn);
		
		final TreeItem<String> suggestedContainerColumn = new TreeItem<String>("Proponowane opakowanie transportowe");
		final TreeItem<String> suggestedContainerValue = new TreeItem<String>("Paleta");
		suggestedContainerColumn.getChildren().setAll(suggestedContainerValue);
		
		final TreeItem<String> root = new TreeItem<>("Dane szczegó³owe produktu");
		root.setExpanded(true);
		root.getChildren().setAll(weightColumn, volumeColumn,dimensionsColumn, suggestedContainerColumn);
		TreeTableColumn<String, String> column = new TreeTableColumn<>("Szczegó³y");	
		column.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) -> 
        new ReadOnlyStringWrapper(p.getValue().getValue()));  
		
		productDetailsTable.getColumns().add(column);
		productDetailsTable.setRoot(root);
		productDetailsTable.setShowRoot(true);
	
	}
	
	private void setProductInfo(){
		categoryLabel.textProperty().bind(product.getCategory().getCategoryNameProperty());
		productIDField.textProperty().bind(product.getProductIDProperty().asString());;
		productNameField.textProperty().bind(product.getProductNameProperty());
		quantityPerUnitField.textProperty().bind(product.getQuantityPerUnitProperty());
		priceField.textProperty().bind(product.getUnitPriceProperty().asString());
		unitsOnStockField.textProperty().bind(product.getUnitsInStockProperty().asString());
		reorderLevelField.textProperty().bind(product.getReorderLevelProperty().asString());
		
		discontinuedBox.getItems().add(product.getDiscontinuedProperty().getValue());
		discontinuedBox.getSelectionModel().selectFirst();
	}
	
	@FXML
	private void okClick(){
		getCurrentStage().close();
		okClicked = true;
	}
	
	public boolean isOkClicked(){
		return this.okClicked;
	}
		
	
	
}
