package app.view;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;

import com.mysql.jdbc.UpdatableResultSet;

import app.model.DeliveryDetails;
import app.model.Product;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.converter.NumberStringConverter;

public class TakeTheProductOverviewController extends DialogBasedController {

	@FXML
	private TextField deliveryIDField;
	@FXML 
	private TextField productNameField;
	@FXML
	private TextField quantityToBeDistributedField;
	@FXML
	private VBox locationVBOX;
	
	private boolean productFullyTaken = false;
	
	
	private DeliveryDetails deliveryDetail;
	private IntegerProperty quantityToBeDistributed;
	private int deliveryID;
	
	private ObservableList<QueryInfoContainer> queriesToBeExecuted;
	
	
	
	
	@FXML
	private void initialize(){
		queriesToBeExecuted = FXCollections.observableArrayList();
		
	}
	
	
	
	
	@FXML
	private void handleAddNewLocation(){
		Optional<String> input = UtilityClass.showTextInputDialog("Podaj wartoœæ", "Podaj lokalizacjê", "Proszê podaæ lokalizacjê do umieszczenia produktu: ", getCurrentStage());
		if(input.isPresent()){
			String locationID = input.get();
			boolean isLocationExisting = mainApp.getController().getDatabase().checkIfLocationExists(locationID);
			if(isLocationExisting){
				int productID = deliveryDetail.getProduct().getProductID();
				int quantityStoredThere = mainApp.getController().getDatabase().checkIfLocationHasProduct(locationID, productID );
				input = UtilityClass.showTextInputDialog("Podaj iloœæ", "Podaj iloœæ produktu", "Proszê podaæ iloœæ produktu do umieszczenia w lokalizacji: ", getCurrentStage());
				if(input.isPresent()){
					boolean isParsable = UtilityClass.isParsable(input.get());
					if(isParsable){
						int quantity = Integer.parseInt(input.get());
						if(quantity > 0 && quantity <= quantityToBeDistributed.get()){
							
							int indexOfLocationOccurence = isProductAlreadyAddedToLocation(locationID.trim());
							
							if(indexOfLocationOccurence != -1){
								
								QueryInfoContainer qic = queriesToBeExecuted.get(indexOfLocationOccurence);
								int quantityFromQic = qic.getQuantity();
								int quantityToAdd = quantityFromQic + quantity;
								qic.setQuantity(quantityToAdd);
								
								
								locationVBOX.getChildren().remove(qic.getLocationInfoVBox());
								qic.setLocationInfoVBox(addLocationInfoVBox(locationID, quantityToAdd));
								locationVBOX.getChildren().add(qic.getLocationInfoVBox());
							} else {
								
								QueryInfoContainer qic = new QueryInfoContainer(addLocationInfoVBox(locationID, quantity), locationID, quantity);
								queriesToBeExecuted.add(qic);
								locationVBOX.getChildren().add(qic.getLocationInfoVBox());
								
							}
							
							int newQuantity = quantityToBeDistributed.get() - quantity;
							quantityToBeDistributed.set(newQuantity);
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d wartoœci", "\nPodana iloœæ jest mniejsza od zera lub przewy¿sza iloœæ produktu do przyjêcia\n", getCurrentStage()).showAndWait();
						}
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d wartoœci", "Proszê podaæ LICZBÊ CA£KOWTIT¥ okreœlaj¹c¹ iloœæ produktu do przyjêcia", getCurrentStage()).showAndWait();
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Proszê podaæ iloœæ produktu do przyjêcia", getCurrentStage()).showAndWait();
				}
				

			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak takiej lokalizacji", "Brak lokalizacji o podanym ID", getCurrentStage()).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Proszê podaæ wartoœæ tekstow¹ w formacie X-X-X okreœlaj¹c¹ identyfikator lokalizacji!", getCurrentStage()).showAndWait();
		}
	}
	

	
	public void setDeliveryDetail(DeliveryDetails deliveryDetail){
		this.deliveryDetail = deliveryDetail;
	}
	
	
	
	
	public void setDeliveryID(int deliveryID){
		this.deliveryID = deliveryID;
	}
	

	
	
	public void initializeData(){
		
		quantityToBeDistributed = new SimpleIntegerProperty();
		
		deliveryIDField.textProperty().bind(new SimpleStringProperty(String.valueOf(deliveryID)));
		productNameField.textProperty().bind(deliveryDetail.getProduct().getProductNameProperty());
		
		StringProperty quantityString = new SimpleStringProperty();

		Bindings.bindBidirectional(quantityString, quantityToBeDistributed, new NumberStringConverter());
		int quantityRemaining = deliveryDetail.getQuantity() - deliveryDetail.getQuantityTaken();
		quantityToBeDistributed.set(quantityRemaining);
		quantityToBeDistributedField.textProperty().bind(quantityString);
	}
	
	
	
	
	public boolean isProductFullyTaken(){
		return this.productFullyTaken;
	}
	
	
	
	private int isProductAlreadyAddedToLocation(String locationID){
		int index = -1;
		for(QueryInfoContainer qic : queriesToBeExecuted){
			if(locationID.equals(qic.getLocationID())){
				index = queriesToBeExecuted.indexOf(qic);
			}
		}
		return index;
	}
	
	
	
	
	
	private VBox addLocationInfoVBox(String locationID, int quantity){
		
		VBox productLocationInfo = new VBox();
		productLocationInfo.setPadding(new Insets(10));
		productLocationInfo.setSpacing(8);

		Label locationIDLabel = new Label("ID lokalizacji: " + locationID);
		locationIDLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
		Label quantityLabel = new Label("Iloœæ: " + String.valueOf(quantity));
		quantityLabel.setFont(Font.font("System", FontWeight.LIGHT, 13));
		productLocationInfo.getChildren().addAll(locationIDLabel, quantityLabel);
		
		productLocationInfo.setPrefWidth(225);
		productLocationInfo.setPrefHeight(68);
		productLocationInfo.setStyle("-fx-border-color: black");
		
		return productLocationInfo;
	}
	
	
	
	
	
	
	@FXML
	private void handleOKClick(){
		if(!queriesToBeExecuted.isEmpty()){
			Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "PotwierdŸ", "PotwierdŸ przyjêcie produktu", "Proszê potwierdziæ operacjê przyjêcia produktu na magazyn", getCurrentStage()).showAndWait();
			if(result.get() == ButtonType.OK){
				
				for(QueryInfoContainer qic : queriesToBeExecuted){
					int productID = deliveryDetail.getProduct().getProductID();
					int quantityStored = mainApp.getController().getDatabase().checkIfLocationHasProduct(qic.getLocationID(), productID );
					if(quantityStored == -1){
						mainApp.getController().getDatabase().insertProductIntoLocation(qic.getLocationID(), productID, qic.getQuantity());
					} else {
						mainApp.getController().getDatabase().updateLocationDetailsRecord(qic.getLocationID(), productID, qic.getQuantity() + quantityStored);
					}
				}
				
				int quantityTakenSum = getQuantityTakenSumFromArrayList();
				Product product = deliveryDetail.getProduct();
				product.setUnitsInStock(product.getUnitsInStock() + quantityTakenSum);
				mainApp.getController().getDatabase().updateUnitsOnStockValue(product, quantityTakenSum);
				
				boolean successfullyUpdated = mainApp.getController().getDatabase().updateQuantityTakenField(deliveryDetail, deliveryID, quantityTakenSum);
				if(successfullyUpdated){
					int blabla = deliveryDetail.getQuantityTaken() + quantityTakenSum;
					deliveryDetail.setQuantityTaken(blabla);
					UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Operacja zakoñczona pomyœlnie", "Produkt zosta³ przyjêty do magazynu", getCurrentStage()).showAndWait();
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "Komunikat", "Operacja zakoñczona niepowodzeniem", "Podczas aktualizacji dostawy wyst¹pi³ problem!", getCurrentStage()).showAndWait();
				}
				
				getCurrentStage().close();

			}
		} else {
			getCurrentStage().close();
		}
	}
	
	
	
	
	
	
	@FXML
	private void handleGetBackClick(){
		if(!queriesToBeExecuted.isEmpty()) {
			Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Anuluj", "Potwierdzenie", "Wszystkie zmiany dokonane w oknie przyjêcia dostawy zostan¹ utracone. \nCzy chcesz wyjœæ? ", getCurrentStage()).showAndWait();
			if(result.get() == ButtonType.OK){
				getCurrentStage().close();
			}
		} else {
			getCurrentStage().close();
		}
	}
	
	
	
	
	
	
	private int getQuantityTakenSumFromArrayList(){
		int sum = 0;
		for(QueryInfoContainer qic : queriesToBeExecuted){
			sum += qic.getQuantity();
		}
		
		return sum;
	}
	
	
	
	
	
	/*
	 * Inner class used to store info about queries to be executed on 'exit' clock
	 */
	public class QueryInfoContainer{
		private VBox locationInfoVBox;
		private String locationID;
		private int quantity;
		
		public QueryInfoContainer(VBox locationInfoVBox, String locationID, int quantity){
			this.locationInfoVBox = locationInfoVBox;
			this.locationID = locationID;
			this.quantity = quantity;
		}
		
		public VBox getLocationInfoVBox(){
			return this.locationInfoVBox;
		}
		
		public String getLocationID(){
			return this.locationID;
		}
		
		public int getQuantity(){
			return this.quantity;
		}
		
		public void setLocationInfoVBox(VBox locationInfoVBox){
			this.locationInfoVBox = locationInfoVBox;
		}
		
		public void setLocationID(String locationID){
			this.locationID = locationID;
		}
		
		public void setQuantity(int quantity){
			this.quantity = quantity;
		}
	}
}
