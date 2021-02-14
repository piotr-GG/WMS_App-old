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
		
		if(acceptableLoadField.getText().length() == 0) errMsg += "Brak warto�ci dopuszczalnego obci��enia\n";
		else if(!UtilityClass.isParsable(acceptableLoadField.getText())) errMsg += "Warto�� w polu dopuszczalnego obci��enia nie jest liczb� ca�kowit�\n";
		
		if(acceptableVolumeField.getText().length() == 0) errMsg += "Brak warto�ci maksymalnej pojemno�ci\n";
		else if(!UtilityClass.isParsable(acceptableVolumeField.getText())) errMsg += "Warto�� w polu maksymalnej pojemno�ci nie jest liczb� ca�kowit�\n";

		if(errMsg.length() == 0) return true;
		else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Napotkano poni�sze b��dy:", errMsg, getCurrentStage()).showAndWait();
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
			if(successfullyUpdated) UtilityClass.showAlert(AlertType.INFORMATION, "Edytuj lokacj�", "Pomy�lnie zapisano zmiany", "Zmiany w lokacji zosta�y pomy�lnie zapisane", getCurrentStage()).showAndWait();
			else UtilityClass.showAlert(AlertType.ERROR, "Edytuj lokacj�", "Wyst�pi� nieoczekiwany b��d", "Podczas zapisywania wyst�pi� nieoczekiwany b��d!", getCurrentStage()).showAndWait();
				
			getCurrentStage().close();
		} 
		
		
		
	}
	
	private void newLocationProcedure(){
		String locationID = locationIDField.getText();
		boolean locationAlreadyExists = mainApp.getController().getDatabase().checkIfLocationExists(locationID);
		if(locationAlreadyExists){
			UtilityClass.showAlert(AlertType.WARNING, "B��d", "Lokacja ju� istnieje", "Lokacja o podanym ID ju� istnieje!", getCurrentStage()).showAndWait();
		} else {
			int acceptableLoad = Integer.parseInt(acceptableLoadField.getText());
			int acceptableVolume = Integer.parseInt(acceptableVolumeField.getText());

			Location location = new Location(locationID, acceptableLoad, acceptableVolume, null);
			
			boolean successfullyInserted = mainApp.getController().getDatabase().insertLocationRecord(location);
			if(successfullyInserted) UtilityClass.showAlert(AlertType.INFORMATION, "Dodaj lokacj�", "Pomy�lnie dodano lokacj�", "Lokacja o ID " + locationID + " zosta�a pomy�lnie dodana!", getCurrentStage()).showAndWait();
			else UtilityClass.showAlert(AlertType.ERROR, "Dodaj lokacj�", "Wyst�pi� b��d", "Podczas dodawania lokacji wyst�pi� nieoczekiwany b��d!", getCurrentStage()).showAndWait();
			
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
		dialogLabel.setText("Edytuj lokacj�");
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
