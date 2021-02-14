package app.view.whAttendantDialogs;

import app.model.Picking;
import app.model.Picking.PickingDetails;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.converter.NumberStringConverter;

public class PickTheProductDialogController extends DialogBasedController {

	@FXML
	private TextField productNameField;
	@FXML
	private TextField remainingQuantityToBePickedField;
	@FXML
	private TextField locationIDField;
	@FXML
	private TextField quantityBeingPickedField;
	
	private Picking picking;
	private PickingDetails pickingDetails;
	
	private boolean productCompletelyPicked = false;
	
	@FXML
	private void initialize(){
		quantityBeingPickedField.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldString, String newString) {
				if(newString != null && UtilityClass.isParsable(newString)){
					int value = Integer.parseInt(newString);
					if(value > pickingDetails.getQuantityToBePicked()) {
						quantityBeingPickedField.setText(String.valueOf(pickingDetails.getQuantityToBePicked()));
					}
				}
				
			}
			
		});
	}
	@FXML
	private void handlePickClick(){
		if(isInputValid()){
			int quantityBeingPicked = Integer.parseInt(quantityBeingPickedField.getText());
			
			int oldQuantityToBePicked = pickingDetails.getQuantityToBePicked();
			int newQuantityToBePicked = oldQuantityToBePicked - quantityBeingPicked;
			
			String locationID = locationIDField.getText();
			String resultString = mainApp.getController().getDatabase().checkIfPickingIsPossible(locationID, pickingDetails.getProduct().getProductID(), quantityBeingPicked);
			
			if(resultString.length() == 0){
				pickingDetails.setQuantityToBePicked(newQuantityToBePicked);
				boolean successfullyUpdated = mainApp.getController().getDatabase().updatePickingDetailsRecord(picking.getPickingID(), pickingDetails);
				successfullyUpdated = mainApp.getController().getDatabase().getTheProductsFromLocation(locationID, pickingDetails.getProduct().getProductID(), quantityBeingPicked);
				if(successfullyUpdated){
					
					UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Przypisano produkt!", "Produkt zosta³ pomyœlnie przypisany do kompletowanego zamówienia!", getCurrentStage()).showAndWait();
					locationIDField.setText("");
					quantityBeingPickedField.setText("");
					
					if(newQuantityToBePicked == 0){
						UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Zakoñczono kompletacjê!", "Produkt zosta³ skompletowany", getCurrentStage()).showAndWait();
						productCompletelyPicked = true;
						getCurrentStage().close();
					}
					
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ nieoczekiwany b³¹d!", "Podczas aktualizowania kompletacji wyst¹pi³ b³¹d!", getCurrentStage());
				}
		  }
			else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Napotkano na b³¹d", resultString, getCurrentStage()).showAndWait();
			}
		}
	}
	
	@FXML
	private void handleCancelClick(){
		getCurrentStage().close();
	}
	
	private void fillTheFields(){
		productNameField.setText(pickingDetails.getProduct().getProductName());
		StringProperty remainingQuantityProperty = new SimpleStringProperty();
		Bindings.bindBidirectional(remainingQuantityProperty, pickingDetails.getQuantityToBePickedProperty(), new NumberStringConverter());
		remainingQuantityToBePickedField.textProperty().bind(remainingQuantityProperty);
	}
	
	private boolean isInputValid(){
		String errorMsg = "";
		if(locationIDField.getText().length() == 0 ) errorMsg += "Brak podanej lokacji do pobrania!\n";
		else if (UtilityClass.containsListOfChars(locationIDField.getText())) errorMsg += "ID lokacji zawiera nieprawid³owe znaki!\n";
		else if (!UtilityClass.isValidLocationID(locationIDField.getText())) errorMsg += "Podany tekst nie pasuje do formatu ID lokacji!\n";
		
		if(quantityBeingPickedField.getText().length() == 0) errorMsg += "Brak podanej iloœæ pobranej\n";
		else if (!UtilityClass.isParsable(quantityBeingPickedField.getText())) errorMsg += "Iloœæ pobrana musi byæ liczb¹ ca³kowit¹!\n";
		
		if(errorMsg.length() == 0) return true;
		else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Napotkano na poni¿sze b³êdy:", errorMsg, getCurrentStage()).showAndWait();
			return false;
		}
	}
	
	public void setPickingDetails(PickingDetails pickingDetails){
		this.pickingDetails = pickingDetails;
		if(pickingDetails != null) fillTheFields();
	}
	
	public void setPicking(Picking picking){
		this.picking = picking;
	}
	
	public boolean isProductCompletelyPicked(){
		return this.productCompletelyPicked;
	}
}
