package app.view;



import java.text.DecimalFormat;


import app.model.Shipper;
import app.model.ShippingInfo;
import app.model.User;
import app.utility.UtilityClass;
import app.view.addNewDialogs.NewShipperDialogController;
import app.view.addNewDialogs.ShipmentDetailsDialogController;
import app.view.inheritance.ActionController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ShippingOverviewController extends ActionController {
	
	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private TableView<ShippingInfo> shippingInfoTable;
	@FXML
	private TableColumn <ShippingInfo, Integer> shipmentIDColumn;
	@FXML
	private TableColumn <ShippingInfo, String> arrivalDateColumn;
	@FXML
	private TableColumn <ShippingInfo, Integer> distanceCoveredColumn;
	@FXML
	private TableColumn <ShippingInfo, Double> shipmentDurationColumn;
	
	private ObservableList<ShippingInfo> shippingInfoData;
	
	@FXML
	private VBox techVelocityVBox;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Label computingProgressLabel;
	@FXML
	private Label techVelocityValueLabel;
	
	
	private Timeline task;
	
	@FXML
	private void initialize(){
		techVelocityVBox.setVisible(false);
		initializeTask();
		initializeShippingInfoTable();
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
	private void handleAddShipper(){
		NewShipperDialogController controller = (NewShipperDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewShipperDialog.fxml", dialogStage, mainApp, currentStage, "Dodaj spedytora");
		controller.setShipper(null);
		controller.getCurrentStage().showAndWait();
	}
	
	@FXML
	private void handleShowShipment(){
		showShipment();
	}
	
	@FXML
	private void handleGetShippingInfoData(){
		ObservableList<ShippingInfo> result = mainApp.getController().getDatabase().getShippingInfos();
		if(result != null && !result.isEmpty()){
			shippingInfoData.clear();
			shippingInfoData.addAll(result);
			UtilityClass.showAlert(AlertType.INFORMATION, "Pobierz informacje", "Zapytanie zwróci³o wyniki", "Znaleziono " + shippingInfoData.size() + " informacji spedycyjnych!", currentStage).showAndWait();
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Brak wyników", "Brak rekordów do wyœwietlenia", "Zapytanie nie zwróci³o ¿adnych wyników!", currentStage).showAndWait();
		}
	}
	
	@FXML
	private void handleComputeTechVelocity(){
		ShippingInfo info = shippingInfoTable.getSelectionModel().getSelectedItem();
		if(info != null){
			computingProgressLabel.setText("Obliczanie prêdkoœci technicznej...");
			
			techVelocityValueLabel.setText("0.0");
			progressBar.progressProperty().addListener(new ChangeListener<Number>() {
				  
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					
				  	double value = (double) Math.round(task.getCurrentTime().toSeconds() * 50d)/50;
					if(value == 0.0) computingProgressLabel.setText("Obliczanie prêdkoœci technicznej...");
					if(value == 5.0) {
						computingProgressLabel.setText("Obliczono!");
						double techVelocity = Math.round(info.getDistanceCovered() / info.getShipmentDuration());
						techVelocityValueLabel.setText(String.valueOf(techVelocity));
					}
					
				}
			});
			
			task.playFromStart();
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Brak zaznaczenia", "Brak zaznaczonego elementu", "Proszê zaznaczyæ element, dla którego ma zostaæ obliczona prêdkoœc techniczna!", currentStage).showAndWait();
		}
	}
	
	
	
	private void initializeTask(){
	    task = new Timeline(
	            new KeyFrame(
	                    Duration.ZERO,       
	                    new KeyValue(progressBar.progressProperty(), 0)
	            ),
	            new KeyFrame(
	                    Duration.seconds(5), 
	                    new KeyValue(progressBar.progressProperty(), 1)
	            )
	        );
	}
	
	private void initializeShippingInfoTable(){
		shipmentIDColumn.setCellValueFactory(cellData->cellData.getValue().getShipment().getShipmentIDProperty().asObject());
		arrivalDateColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getArrivalDate().toString()));
		distanceCoveredColumn.setCellValueFactory(cellData->cellData.getValue().getDistanceCoveredProperty().asObject());
		shipmentDurationColumn.setCellValueFactory(cellData->cellData.getValue().getShipmentDurationProperty().asObject());
		shippingInfoData = FXCollections.observableArrayList();
		shippingInfoTable.setItems(shippingInfoData);
		
		shippingInfoTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ShippingInfo>(){

			@Override
			public void changed(ObservableValue<? extends ShippingInfo> observable, ShippingInfo oldValue,
					ShippingInfo newValue) {
				if(newValue != null) techVelocityVBox.setVisible(true);
				else techVelocityVBox.setVisible(false);		
			}
			
		});
		
		MenuItem showShipmentMenuItem = new MenuItem("Poka¿ szczegó³y wysy³ki");
		MenuItem showShipperMenuItem = new MenuItem("Poka¿ szczegó³y spedytora");
		
		

		showShipmentMenuItem.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent t) {
		    	showShipment();
		  }
		});
		
		showShipperMenuItem.setOnAction(new javafx.event.EventHandler<ActionEvent>(){
			public void handle(ActionEvent t){
		    	if(!shippingInfoData.isEmpty()){
		    		ShippingInfo si =  shippingInfoTable.getSelectionModel().getSelectedItem();
		    		if(si != null){
		    			Shipper shipper = si.getShipment().getShipper();
		    			NewShipperDialogController controller = (NewShipperDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/NewShipperDialog.fxml", dialogStage, mainApp, currentStage, "Dodaj spedytora");
		    			controller.setShipper(shipper);
		    			controller.getCurrentStage().showAndWait();
		    		}
		    }
			}
		});
		
		shippingInfoTable.setContextMenu(new ContextMenu(showShipmentMenuItem, showShipperMenuItem));
	}
	
		private void showShipment(){
	    	if(!shippingInfoData.isEmpty()){
	    		ShippingInfo si =  shippingInfoTable.getSelectionModel().getSelectedItem();
	    		if(si != null){
	    			ShipmentDetailsDialogController controller = (ShipmentDetailsDialogController) UtilityClass.showDialogFactory("view/addNewDialogs/ShipmentDetailsDialog.fxml", dialogStage, mainApp, currentStage, "Szczegó³y wysy³ki");
	    			controller.setShipment(si.getShipment());
	    			controller.fillTheFields();
	    			controller.getCurrentStage().showAndWait();
	    		} else {
					UtilityClass.showAlert(AlertType.ERROR, "Brak zaznaczenia", "Brak zaznaczonego elementu", "Proszê zaznaczyæ element, który ma zostaæ wyœwietlony!", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "Brak rekordów", "Brak danych do wyœwietlenia", "Nie mo¿na kontynuowaæ, poniewa¿ w tabeli nie ma ¿adnych danych do wyœwietlenia!", currentStage).showAndWait();
			}
	        
		}

}
