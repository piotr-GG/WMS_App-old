package app.view.whAttendantDialogs;

import app.model.Shipment;
import app.model.Shipment.ShipmentStatus;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class ShipTheShipmentDialogController extends DialogBasedController {
	
	@FXML
	private TextField shipmentIDField;
	
	
	@FXML
	private void handleShipClick(){
		String shipmentIDText = shipmentIDField.getText();
		if(!shipmentIDText.isEmpty()){
			boolean isParsable = UtilityClass.isParsable(shipmentIDText);
			if(isParsable){
				Shipment result = mainApp.getController().getDatabase().getShipmentByID(Integer.parseInt(shipmentIDText));
				if(result != null){
					if(result.getShipmentStatus().equals(ShipmentStatus.Awaits_Shipment)){
						result.setShipmentStatus("Shipped");
						boolean successfullyUpdated = mainApp.getController().getDatabase().updateShipmentStatus(result, "Shipped");
						if(successfullyUpdated){
							UtilityClass.showAlert(AlertType.INFORMATION, "Aktualizacja statusu", "Pomy�lnie zaktualizowano status", "Status wysy�ki zosta� pomy�lnie zaktualizowany", getCurrentStage()).showAndWait();
							getCurrentStage().close();
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B��d", "Wyst�pi� b��d", "Podczas aktualizacji wyst�pi� b��d!", getCurrentStage()).showAndWait();
						}
						
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dny status wysy�ki", "Status wysy�ki nie pozwala na kontynuowanie procedury!", getCurrentStage()).showAndWait();
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak zam�wienia", "W bazie danych nie ma wysy�ki o podanym ID!", getCurrentStage()).showAndWait();
				}
 			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto�� ID", "Warto�� ID musi by� liczb� ca�kowit�!", getCurrentStage()).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak warto�ci", "Brak podanej warto�ci ID wysy�ki", getCurrentStage()).showAndWait();
		}
	}
	@FXML
	private void handleCancelClick(){
		getCurrentStage().close();
	}
}
