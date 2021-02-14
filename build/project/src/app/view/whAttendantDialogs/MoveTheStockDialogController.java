package app.view.whAttendantDialogs;

import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class MoveTheStockDialogController extends DialogBasedController{

	@FXML
	private TextField productIDField;
	@FXML
	private TextField oldLocationField;
	@FXML
	private TextField newLocationField;
	@FXML
	private TextField quantityField;
	
	@FXML
	private void handleMoveClick(){
		if(isInputValid()) 
		{
			String oldLocation = oldLocationField.getText();
			String newLocation = newLocationField.getText();
				
			int productID = Integer.parseInt(productIDField.getText());
					
			int quantity = Integer.parseInt(quantityField.getText());
			String errorString = "";
			boolean validateData = mainApp.getController().getDatabase().checkIfLocationExists(oldLocation);
			if(!validateData) errorString += "Wskazana lokalizacja w polu \"Lokalizacja\" nie istnieje\n";
						
			validateData = mainApp.getController().getDatabase().checkIfLocationExists(newLocation);
			if(!validateData) errorString += "Wskazana lokalizacja w polu \"Nowa lokalizacja\" nie istnieje\n";
						
			validateData = mainApp.getController().getDatabase().checkIfProductExists(productID);
			if(!validateData) errorString += "Podany produkt nie istnieje w programie";
						
			String queryResult = mainApp.getController().getDatabase().checkIfMovingWareIsDoable(oldLocationField.getText(), productID, quantity );
						
			if(queryResult.length() == 0){
			boolean isSuccessful = mainApp.getController().getDatabase().moveTheStock(oldLocationField.getText(),newLocationField.getText(), productID, quantity);
				if(isSuccessful) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Komunikat");
					alert.setHeaderText("Operacja zako�czona powodzeniem");
					alert.setContentText("Towar zosta� pomy�lnie przesuni�ty do nowej lokalizacji");
					alert.showAndWait();
								
					productIDField.setText("");
					oldLocationField.setText("");
					newLocationField.setText("");
					quantityField.setText("");
								
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Komunikat");
					alert.setHeaderText("B��d przy przesuwaniu towaru");
					alert.setContentText("Podczas procedury przesuwania towaru wyst�pi� nieoczekiwany b��d");
					alert.showAndWait();
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Nieprawid�owe warto�ci");
				alert.setHeaderText("Napotkano na nast�puj�ce b��dy");
				alert.setContentText(queryResult);
				alert.showAndWait();
			}
			if(errorString.length() != 0){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Nieprawid�owe warto�ci");
				alert.setHeaderText("Napotkano na nast�puj�ce b��dy");
				alert.setContentText(errorString);
				alert.showAndWait();
				}
			} 

	}
	@FXML
	private void handleReturnClick(){
		getCurrentStage().close();
	}
	
	private boolean isInputValid(){
		String errorString = "";
		if(productIDField.getText().isEmpty()) errorString += "Pole ID Produktu jest niewype�nione\n";
		else if (!UtilityClass.isParsable(productIDField.getText())) errorString += "ID produktu ma by� liczb� ca�kowit�!\n";
		
		if(oldLocationField.getText().isEmpty()) errorString += "Pole Lokalizacja jest niewype�nione\n";
		if(newLocationField.getText().isEmpty()) errorString +=  "Pole Nowa lokalizacja jest niewype�nione\n";
		if(oldLocationField.getText().equals(newLocationField.getText())) errorString += "Podane lokalizacje s� takie same\n";
		if(quantityField.getText().isEmpty()) errorString +=  "Pole ilo�� jest niewype�nione\n";
		else if (!UtilityClass.isParsable(quantityField.getText())) errorString += "Ilo�� produktu ma by� liczb� ca�kowit�!\n";
		
		if(errorString.length() == 0) return true;
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Nieprawid�owe warto�ci");
			alert.setHeaderText("Napotkano na nast�puj�ce b��dy");
			alert.setContentText(errorString);
			alert.showAndWait();
			return false;
		}
	}

}
