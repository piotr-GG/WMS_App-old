package app.view;

import java.io.IOException;
import app.MainApp;
import app.model.Location;
import app.model.Product;
import app.utility.UtilityClass;
import app.view.addNewDialogs.LocationDetailsDialogController;
import app.view.addNewDialogs.NewLocationDialogController;
import app.view.inheritance.ActionController;
import app.view.whAttendantDialogs.MoveTheStockDialogController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.BigIntegerStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;


public class WarehouseManagementOverviewController extends ActionController{

	@FXML
	private AnchorPane mainPane;
	@FXML
	private TabPane tabPane;
	@FXML
	private RadioButton showLocationByIDRadioButton;
	@FXML
	private TextField showLocationByIDField;
	@FXML
	private Label showLocationByIDFieldLabel;
	
	@FXML
	private RadioButton showAllLocationsRadioButton;
	@FXML
	private ComboBox<String> showAllLocationsBox;
	@FXML
	private Label showAllLocationsBoxLabel;
	@FXML
	private TextField showAllLocationsField;
	@FXML
	private Label showAllLocationsFieldLabel;
	@FXML
	private RadioButton showEmptyLocationsRadioButton;

	@FXML
	private TableView<Location> locationTable;
	@FXML
	private TableColumn<Location, String> locationIDColumn;
	@FXML
	private TableColumn<Location, Integer> acceptableLoadColumn;
	@FXML
	private TableColumn<Location, Integer> acceptableVolumeColumn;
	
	private ObservableList<Location> locationData;
	private ToggleGroup radioButtonGroup;
	
	@FXML
	private TextField locationIDField;
	@FXML
	private TextField acceptableLoadField;
	@FXML
	private TextField acceptableVolumeField;
	@FXML
	private VBox productStoredVBOX;
	
	@FXML
	private void initialize(){
		radioButtonGroup = new ToggleGroup();
		
		showLocationByIDRadioButton.setToggleGroup(radioButtonGroup);
		
		showAllLocationsRadioButton.setToggleGroup(radioButtonGroup);
		showAllLocationsBox.setItems(FXCollections.observableArrayList("regale", "przêœle", "poziomie"));
		
		showEmptyLocationsRadioButton.setToggleGroup(radioButtonGroup);
		
		initializeLocationTable();
		
		UtilityClass.LabelledControlFactory(showLocationByIDRadioButton, new Label[]{showLocationByIDFieldLabel});
		UtilityClass.LabelledControlFactory(showAllLocationsRadioButton, new Label[]{showAllLocationsBoxLabel, showAllLocationsFieldLabel});
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
	private void handleExecuteClick(){

		if(showLocationByIDRadioButton.isSelected()) getLocationByID();
		if(showAllLocationsRadioButton.isSelected()) getAllLocationsWith();
		if(showEmptyLocationsRadioButton.isSelected()) getEmptyLocations();
	}
	
	private void getLocationByID(){
		String locationID = showLocationByIDField.getText();
		if(locationID.length() != 0){
			Location location = mainApp.getController().getDatabase().getLocationByID(locationID);
			if(location != null){
				locationData.clear();
				locationData.add(location);
				UtilityClass.showAlert(AlertType.INFORMATION, "ZnajdŸ lokacjê", "Znaleziono lokacjê", "Lokacja o podanym ID zosta³a znaleziona!", currentStage).showAndWait();
				tabPane.getSelectionModel().select(1);
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak lokacji", "Nie znaleziono lokacji o podanym ID", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Pusta wartoœæ", "Proszê podaæ ID lokacji!", currentStage).showAndWait();
		}
	}
	
	private void getAllLocationsWith(){
		if(showAllLocationsBox.getSelectionModel().getSelectedItem() != null){
			if(showAllLocationsField.getText().length() != 0 && UtilityClass.isParsable(showAllLocationsField.getText())){
				int value = Integer.parseInt((showAllLocationsField.getText()));
				int condition = showAllLocationsBox.getSelectionModel().getSelectedIndex() + 1;
				ObservableList<Location> result = mainApp.getController().getDatabase().getLocationsWith(condition , value);
				if(!result.isEmpty()){
					locationData.clear();
					locationData.addAll(result);
					UtilityClass.showAlert(AlertType.INFORMATION, "ZnajdŸ lokacje", "Znaleziono lokacje", "Lokacje spe³niaj¹ce podane kryterium zosta³y znalezione", currentStage).showAndWait();
					tabPane.getSelectionModel().select(1);
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "Brak lokacji", "Brak takich lokacji", "Nie znaleziono lokacji spe³niaj¹cych podane kryterium", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak podanej wartoœci", "Proszê zaznaczyæ wartoœæ kryterium w odpowiednim polu!", currentStage).showAndWait();
			}
			
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczonego kryterium", "Proszê zaznaczyæ kryterium!", currentStage).showAndWait();
		}
	}
	
	private void getEmptyLocations(){
		ObservableList<Location> result = mainApp.getController().getDatabase().getEmptyLocations();
		if(!result.isEmpty()){
			locationData.clear();
			locationData.addAll(result);
			UtilityClass.showAlert(AlertType.INFORMATION, "ZnajdŸ puste lokacje", "Znaleziono lokacje", "Lokacje bez przypisanych produktów zosta³y znalezione", currentStage).showAndWait();
			tabPane.getSelectionModel().select(1);
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Brak lokacji", "Brak pustych lokacji", "Nie znaleziono lokacji bez przypisanych produktów", currentStage).showAndWait();
		}
	}

	@FXML
	private void handleAddNewLocation(){
		NewLocationDialogController controller = (NewLocationDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewLocationDialog.fxml", dialogStage, mainApp, currentStage, "Dodaj now¹ lokacjê");
		controller.initializeNewLocationData();
		controller.getCurrentStage().showAndWait();
	}
	
	@FXML
	private void handleAddProductIntoLocation(){
		MoveTheStockDialogController controller = (MoveTheStockDialogController) UtilityClass.showDialogFactory("view/whAttendantDialogs/MoveTheStockDialog.fxml", dialogStage, mainApp, currentStage, "Przypisz produkt");
		controller.getCurrentStage().showAndWait();
	}
	
	@FXML
	private void handleQuantityChart(){
		Location location = locationTable.getSelectionModel().getSelectedItem();
		if(location != null){
			if(location.getProductsStored() != null && !location.getProductsStored().isEmpty()){
				try{
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainApp.class.getResource("view/ChartResultDialog.fxml"));
					TabPane chartDialog = (TabPane) loader.load();
					
					Stage stage = new Stage();
					
			        stage.setTitle("Wykres");
			        stage.getIcons().add(new Image("file:src/app/view/images/mini icons/icon.png"));
			        
			        
			        ChartResultDialogController controller = loader.getController();
			        controller.setMainApp(mainApp);
			        controller.setDialogStage(stage);
	        		controller.showQuantityDistributionChart(location);
	        		controller.setChartShown(ChartResultDialogController.QUANTITY_DISTRIBUTION);
	        		
	        		Scene scene = new Scene(chartDialog);
	        		stage.setScene(scene);
	        		stage.show();
				} catch (IOException e){
					e.printStackTrace();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak produktów", "W podanej lokacji nie ma sk³adowanych produktów!", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Brak zaznaczenia", "Brak lokacji", "Proszê zaznaczyæ lokacjê!", currentStage).showAndWait();
		}
	}
		
	private void initializeLocationTable(){
		locationIDColumn.setCellValueFactory(cellData->cellData.getValue().getLocationIDProperty());
		acceptableLoadColumn.setCellValueFactory(cellData->cellData.getValue().getAcceptableLoadProperty().asObject());
		acceptableVolumeColumn.setCellValueFactory(cellData->cellData.getValue().getAcceptableVolumeProperty().asObject());
		
		locationData = FXCollections.observableArrayList();
		locationTable.setItems(locationData);
		
		locationTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Location>() {

			@Override
			public void changed(ObservableValue<? extends Location> observable, Location oldValue, Location newValue) {
				if(newValue != null) showLocationDetails(newValue);
				else clearLocationDetails();
			}
		} );
		
		locationTable.setRowFactory(new Callback<TableView<Location>, TableRow<Location>>(){

			@Override
			public TableRow<Location> call(TableView<Location> arg0) {
				final TableRow<Location> row = new TableRow<>();
				final ContextMenu rowMenu = new ContextMenu();
				final MenuItem showQuantityDistribution = new MenuItem("Poka¿ rozk³ad iloœciowy");
				final MenuItem showLocationDetails = new MenuItem("Poka¿ szczegó³y lokacji");
				final MenuItem editLocation = new MenuItem("Edytuj lokacjê");
				
				showQuantityDistribution.setOnAction(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent arg0) {
						handleQuantityChart();
					}
				});
				
				showLocationDetails.setOnAction(new EventHandler<ActionEvent>(){

					@Override
					public void handle(ActionEvent event) {
						LocationDetailsDialogController controller =  (LocationDetailsDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/LocationDetailsDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y lokacji");
				    	controller.setLocation(row.getItem());
				    	controller.getCurrentStage().showAndWait();
					}
				});
				
				editLocation.setOnAction(new EventHandler<ActionEvent>(){

					@Override
					public void handle(ActionEvent event) {
						NewLocationDialogController controller = (NewLocationDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewLocationDialog.fxml", dialogStage, mainApp, currentStage, "Edytuj lokacjê");
				    	controller.setEditLocationMode(true);
				    	controller.setEditLocation(row.getItem());
				    	controller.getCurrentStage().showAndWait();
						
					}
					
				});
				rowMenu.getItems().addAll(showQuantityDistribution, showLocationDetails, editLocation);
				row.setContextMenu(rowMenu);
				row.contextMenuProperty().bind(
						Bindings.when(Bindings.isNotNull(row.itemProperty()))
						.then(rowMenu)
						.otherwise((ContextMenu) null));
				return row;
			}
			
		});
	}
	
	private void showLocationDetails(Location location){
		if(location != null){
			
			StringProperty acceptableLoadProperty = new SimpleStringProperty();
			StringProperty acceptableVolumeProperty = new SimpleStringProperty();
			Bindings.bindBidirectional(acceptableLoadProperty, location.getAcceptableLoadProperty(), new NumberStringConverter());
			Bindings.bindBidirectional(acceptableVolumeProperty, location.getAcceptableVolumeProperty(), new NumberStringConverter());
			
			locationIDField.textProperty().bind(location.getLocationIDProperty());
			acceptableLoadField.textProperty().bind(acceptableLoadProperty);
			acceptableVolumeField.textProperty().bind(acceptableVolumeProperty);
			
			productStoredVBOX.getChildren().clear();
			
			if(location.getProductsStored() != null && !location.getProductsStored().isEmpty()){
				
				for(Product product : location.getProductsStored()){
					Label label = new Label(product.getProductName() + "  -  " + product.getUnitsInStock());
					label.setFont(Font.font("System", FontWeight.NORMAL, 14));
					productStoredVBOX.getChildren().add(label);
				}
		    } else {
		    	Label label = new Label("Brak produktów do wyœwietlenia!");
		    	label.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 16));
		    	productStoredVBOX.getChildren().add(label);
		    }
		}
	}
	
	private void clearLocationDetails(){
		if(locationIDField.textProperty().isBound()){
			locationIDField.textProperty().unbind();
			locationIDField.setText("");
			acceptableLoadField.textProperty().unbind();
			acceptableLoadField.setText("");
			acceptableVolumeField.textProperty().unbind();
			acceptableVolumeField.setText("");
		}
		productStoredVBOX.getChildren().clear();
	}
}


