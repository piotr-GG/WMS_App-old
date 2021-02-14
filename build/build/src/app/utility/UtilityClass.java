package app.utility;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import app.MainApp;
import app.view.inheritance.DialogBasedController;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UtilityClass {
	
	public static boolean isParsable(String input){
	    boolean parsable = true;
	    try{
	        Integer.parseInt(input);
	    }catch(NumberFormatException e){
	        parsable = false;
	    }
	    return parsable;
	}
	
	public static boolean isDoubleParsable(String input){
	    boolean parsable = true;
	    try{
	    	Double.parseDouble(input);
	    }catch(NumberFormatException e){
	        parsable = false;
	    }
	    return parsable;
	}
	
	public static boolean isLongParsable(String input){
		boolean parsable = true;
		try{
			Long.parseLong(input);
		} catch (NumberFormatException e){
			parsable = false;
		}
		return parsable;
	}
	
	public static Alert showAlert(AlertType type, String title, String headerText, String contentText, Stage owner){
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.initOwner(owner);
		return alert;
	}
	
	public static Optional<String> showTextInputDialog(String title, String headerText, String contentText, Stage owner){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);
		dialog.initOwner(owner);
		Optional<String> result = dialog.showAndWait();
		return result;
	}
	
	public static DialogBasedController showDialogFactory(String loaderLocation, Stage dialogStage, MainApp mainApp, Stage ownerStage, String stageTitle){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(loaderLocation));
			AnchorPane pane = (AnchorPane) loader.load();
			
			Scene scene = new Scene(pane);
			dialogStage = new Stage();
			dialogStage.setScene(scene);
			dialogStage.setTitle(stageTitle);
			dialogStage.getIcons().add(mainApp.getProgramIcon());

			//Nadanie dostêpu kontrolerowi do aplikacji
			DialogBasedController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setCurrentStage(dialogStage);
			controller.setOwnerStage(ownerStage);
			controller.setDefaultIcon();
			
			dialogStage.initOwner(ownerStage);
			dialogStage.initModality(Modality.NONE);

			return controller;
			
		} catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean containsListOfChars(String text){
		if(text != null || text.length() != 0){
		char[] listOfChars = {'+', '=', ';', ':', '?' , '<' , ',' , '>' , '`' ,'*', '&', '^', '%', '$', '#', '!'};
		for(int i = 0; i < text.length(); i++){
			char c = text.charAt(i);
			for(int j = 0; j < listOfChars.length; j++){
				if(c == listOfChars[j]) return true;
			}
		  }
		}
		return false;
	}
	
	public static boolean isValidLocationID(String locationID){
		int count = 0;
		char [] array = locationID.toCharArray();
		for(char c : array){
			if (c == '-') count++;
		}
		
		if(count == 2) return true;
		else return false;
	}
	
	public static File chooseTheFile(FileChooser.ExtensionFilter ext, Stage owner){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Eksportuj dane");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(
			ext
		);
		File file = fileChooser.showSaveDialog(owner);
		return file;
	}
	
	public static void showResultAlert(int arraySize, Stage stage){
		AlertType type = null;
		if(arraySize > 0){
			type = AlertType.INFORMATION;
		} else {
			type = AlertType.WARNING;
		}
		
		showAlert(type, "Wynik zapytania", "Rezultat", "Zapytanie zwróci³o " + String.valueOf(arraySize) + " wyników", stage).showAndWait();
	}
	
	public static void LabelledControlFactory(RadioButton controlItem, Label[] labelArray){
		controlItem.selectedProperty().addListener((changeListener) ->{
		for(Label controlLabel : labelArray){
			FadeTransition ft = new FadeTransition(Duration.millis(500), controlLabel);
			ft.setCycleCount(1);
			if(controlItem.isSelected()) {
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				controlLabel.setDisable(false);
			} else {
				ft.setFromValue(1.0);
				ft.setToValue(0.0);
				controlLabel.setDisable(true);
			}
			ft.play();	
		}
	});
		
		
	for(Label controlLabel : labelArray) controlLabel.setOpacity(0.0);
}
	
	
}
