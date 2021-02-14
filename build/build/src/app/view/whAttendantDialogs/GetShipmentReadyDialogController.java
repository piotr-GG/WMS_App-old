package app.view.whAttendantDialogs;

import app.model.Order;
import app.model.Order.OrderStatus;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class GetShipmentReadyDialogController extends DialogBasedController {

	@FXML
	private TextField orderIDField;
	
	
	
	@FXML
	private void handleConfirmClick(){
		if(!orderIDField.getText().isEmpty()){
			String orderIDText = orderIDField.getText();
			boolean isParsable = UtilityClass.isParsable(orderIDText);
			if(isParsable){
				int orderID = Integer.parseInt(orderIDText);
				Order order = mainApp.getController().getDatabase().getOrderByID(orderID);
				if(order != null){
					OrderStatus orderStatus = order.getOrderStatus();
					if(orderStatus.equals(OrderStatus.Checked)){
						boolean successfullyUpdated = mainApp.getController().getDatabase().changeOrderStatus(order.getOrderID(), "Shipment_Ready");
						if(successfullyUpdated){
							UtilityClass.showAlert(AlertType.INFORMATION, "Potwierdzenie gotowoœci do wysy³ki", "Pomyœlnie zaktualizowano status", "Zamówienie o ID " + String.valueOf(orderID) + " jest gotowe do wysy³ki", getCurrentStage()).showAndWait();
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Nieoczekiwany b³¹d", "Podczas aktualizacji statusu wyst¹pi³ niespodziewany b³¹d", getCurrentStage()).showAndWait();
						}
						getCurrentStage().close();
						
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Nie mo¿na kontynuowaæ", "Zamówienie musi posiadaæ status \"W trakcie kontroli\", aby mo¿liwe by³o potwierdzenie gotowoœci do wysy³ki", getCurrentStage()).showAndWait();
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zamówienia", "Zamówienie o podanym ID nie istnieje", getCurrentStage()).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Proszê podaæ liczbê ca³kowit¹ bêd¹c¹ identyfikatorem zamówienia", getCurrentStage()).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak podanej wartoœci", "Proszê podaæ identyfikator zamówienia", getCurrentStage()).showAndWait();
		}
	}
	@FXML
	private void handleReturnClick(){
		getCurrentStage().close();
	}
}
