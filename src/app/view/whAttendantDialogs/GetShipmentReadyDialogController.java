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
							UtilityClass.showAlert(AlertType.INFORMATION, "Potwierdzenie gotowo�ci do wysy�ki", "Pomy�lnie zaktualizowano status", "Zam�wienie o ID " + String.valueOf(orderID) + " jest gotowe do wysy�ki", getCurrentStage()).showAndWait();
						} else {
							UtilityClass.showAlert(AlertType.ERROR, "B��d", "Nieoczekiwany b��d", "Podczas aktualizacji statusu wyst�pi� niespodziewany b��d", getCurrentStage()).showAndWait();
						}
						getCurrentStage().close();
						
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B��d", "Nie mo�na kontynuowa�", "Zam�wienie musi posiada� status \"W trakcie kontroli\", aby mo�liwe by�o potwierdzenie gotowo�ci do wysy�ki", getCurrentStage()).showAndWait();
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak zam�wienia", "Zam�wienie o podanym ID nie istnieje", getCurrentStage()).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Prosz� poda� liczb� ca�kowit� b�d�c� identyfikatorem zam�wienia", getCurrentStage()).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak podanej warto�ci", "Prosz� poda� identyfikator zam�wienia", getCurrentStage()).showAndWait();
		}
	}
	@FXML
	private void handleReturnClick(){
		getCurrentStage().close();
	}
}
