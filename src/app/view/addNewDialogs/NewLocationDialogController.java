package app.view.addNewDialogs;

import java.util.Optional;

import app.model.Location;
import app.model.Product;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class NewLocationDialogController extends DialogBasedController {

	@FXML
	private TextField locationIDField;
	@FXML
	private Spinner<Integer> storagePlaceField;
	@FXML
	private Spinner<Integer> spanField;
	@FXML
	private Spinner<Integer> levelField;
	@FXML
	private TextField acceptableLoadField;
	@FXML
	private TextField acceptableVolumeField;
	@FXML
	private Label dialogLabel;
	@FXML
	private Button executeButton;
	
	private SpinnerValueFactory<Integer> storagePlaceSVF;
	private SpinnerValueFactory<Integer> spanSVF;
	private SpinnerValueFactory<Integer> levelSVF;
	
	
	private Location editLocation = null;
	private boolean editLocationMode = false;

	@FXML
	private void initialize(){
		storagePlaceSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20);
		spanSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10);
		levelSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 6);
				
		storagePlaceField.setValueFactory(storagePlaceSVF);
		spanField.setValueFactory(spanSVF);
		levelField.setValueFactory(levelSVF);
	}
	
	public void initializeNewLocationData(){
		
		addLocationID(storagePlaceSVF.getValue() , spanSVF.getValue(), levelSVF.getValue());
		storagePlaceSVF.valueProperty().addListener(event -> addLocationID(storagePlaceSVF.getValue() , spanSVF.getValue(), levelSVF.getValue()));
		spanSVF.valueProperty().addListener(event -> addLocationID(storagePlaceSVF.getValue() , spanSVF.getValue(), levelSVF.getValue()));
		levelSVF.valueProperty().addListener(event -> addLocationID(storagePlaceSVF.getValue() , spanSVF.getValue(), levelSVF.getValue()));
		
	}
	
	private boolean isInputValid(){
		String errMsg = "";
		
		if(acceptableLoadField.getText().length() == 0) errMsg += "Brak wartoœci dopuszczalnego obci¹¿enia\n";
		else if(!UtilityClass.isParsable(acceptableLoadField.getText())) errMsg += "Wartoœæ w polu dopuszczalnego obci¹¿enia nie jest liczb¹ ca³kowit¹\n";
		
		if(acceptableVolumeField.getText().length() == 0) errMsg += "Brak wartoœci maksymalnej pojemnoœci\n";
		else if(!UtilityClass.isParsable(acceptableVolumeField.getText())) errMsg += "Wartoœæ w polu maksymalnej pojemnoœci nie jest liczb¹ ca³kowit¹\n";

		if(errMsg.length() == 0) return true;
		else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Napotkano poni¿sze b³êdy:", errMsg, getCurrentStage()).showAndWait();
			return false;
		}
	}

	@FXML
	private void handleExecute() {
		if(isInputValid()){
			if(editLocationMode){
				editLocationProcedure();
			} else {
				newLocationProcedure();
			}
		}
	}
	
	private void editLocationProcedure(){
		String locationID = editLocation.getLocationID();
		int acceptableLoad = Integer.parseInt(acceptableLoadField.getText());
		int acceptableVolume = Integer.parseInt(acceptableVolumeField.getText());
		
		Location newLocation = new Location(locationID, acceptableLoad, acceptableVolume, null);
		
		boolean areEqual = newLocation.areLocationsEqual(editLocation);
		
		if(!areEqual){
			
			editLocation.setAcceptableLoad(acceptableLoad);
			editLocation.setAcceptableVolume(acceptableVolume);
			
			boolean successfullyUpdated = mainApp.getController().getDatabase().updateLocationRecord(newLocation);
			if(successfullyUpdated) UtilityClass.showAlert(AlertType.INFORMATION, "Edytuj lokacjê", "Pomyœlnie zapisano zmiany", "Zmiany w lokacji zosta³y pomyœlnie zapisane", getCurrentStage()).showAndWait();
			else UtilityClass.showAlert(AlertType.ERROR, "Edytuj lokacjê", "Wyst¹pi³ nieoczekiwany b³¹d", "Podczas zapisywania wyst¹pi³ nieoczekiwany b³¹d!", getCurrentStage()).showAndWait();
				
			getCurrentStage().close();
		} 
		
		
		
	}
	
	private void newLocationProcedure(){
		String locationID = locationIDField.getText();
		boolean locationAlreadyExists = mainApp.getController().getDatabase().checkIfLocationExists(locationID);
		if(locationAlreadyExists){
			UtilityClass.showAlert(AlertType.WARNING, "B³¹d", "Lokacja ju¿ istnieje", "Lokacja o podanym ID ju¿ istnieje!", getCurrentStage()).showAndWait();
		} else {
			int acceptableLoad = Integer.parseInt(acceptableLoadField.getText());
			int acceptableVolume = Integer.parseInt(acceptableVolumeField.getText());

			Location location = new Location(locationID, acceptableLoad, acceptableVolume, null);
			
			boolean successfullyInserted = mainApp.getController().getDatabase().insertLocationRecord(location);
			if(successfullyInserted) UtilityClass.showAlert(AlertType.INFORMATION, "Dodaj lokacjê", "Pomyœlnie dodano lokacjê", "Lokacja o ID " + locationID + " zosta³a pomyœlnie dodana!", getCurrentStage()).showAndWait();
			else UtilityClass.showAlert(AlertType.ERROR, "Dodaj lokacjê", "Wyst¹pi³ b³¹d", "Podczas dodawania lokacji wyst¹pi³ nieoczekiwany b³¹d!", getCurrentStage()).showAndWait();
			
			getCurrentStage().close();
		}
		
	}
	
	private void addLocationID(int storagePlace, int span, int level){
		String locationIDText = String.valueOf(storagePlace) + "-" + String.valueOf(span) + "-" + String.valueOf(level);
		locationIDField.setText(locationIDText);
	}
	
	private void fillTheSpinners(){
		String locationID = editLocation.getLocationID();
		
		int index = 0;
		index = locationID.indexOf('-');
		String storagePlace = locationID.substring(0, index);
		

		int lastIndex = locationID.lastIndexOf('-');
		String span = locationID.substring(index + 1, lastIndex);

		
		String level = locationID.substring(lastIndex + 1);
	
		storagePlaceSVF.setValue(Integer.parseInt(storagePlace));
		spanSVF.setValue(Integer.parseInt(span));
		levelSVF.setValue(Integer.parseInt(level));

	}

	private void initializeEditLocationInfo(){
		dialogLabel.setText("Edytuj lokacjê");
		executeButton.setText("Edytuj");
		
		
		
		
		locationIDField.textProperty().bind(editLocation.getLocationIDProperty());
		storagePlaceField.setDisable(true);
		spanField.setDisable(true);
		levelField.setDisable(true);
		acceptableLoadField.setText(String.valueOf(editLocation.getAcceptableLoad()));
		acceptableVolumeField.setText(String.valueOf(editLocation.getAcceptableVolume()));
		
		fillTheSpinners();
	}

	public void setEditLocationMode(boolean editLocationMode){
		this.editLocationMode = editLocationMode;
	}
	
	public void setEditLocation(Location editLocation){
		this.editLocation = editLocation;
		if(editLocationMode) initializeEditLocationInfo();
	}
	
	@FXML
	private void handleCancel() {
		getCurrentStage().close();
	}
	
}
