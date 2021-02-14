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
							UtilityClass.showAlert(AlertType.INFORMATION, "Aktualizacja statusu", "Pomyœlnie zaktualizowano status", "Status wysy³ki zosta³ pomyœlnie zaktualizowany", getCurrentStage()).showAndWait();
							getCurrentStage().close();
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d", "Podczas aktualizacji wyst¹pi³ b³¹d!", getCurrentStage()).showAndWait();
						}
						
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdny status wysy³ki", "Status wysy³ki nie pozwala na kontynuowanie procedury!", getCurrentStage()).showAndWait();
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zamówienia", "W bazie danych nie ma wysy³ki o podanym ID!", getCurrentStage()).showAndWait();
				}
 			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ ID", "Wartoœæ ID musi byæ liczb¹ ca³kowit¹!", getCurrentStage()).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Brak podanej wartoœci ID wysy³ki", getCurrentStage()).showAndWait();
		}
	}
	@FXML
	private void handleCancelClick(){
		getCurrentStage().close();
	}
}
