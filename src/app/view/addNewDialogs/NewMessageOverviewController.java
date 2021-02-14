package app.view.addNewDialogs;



import java.time.LocalDate;

import app.MainApp;
import app.model.Message;
import app.utility.UtilityClass;
import app.view.UserInteractionController;
import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewMessageOverviewController extends DialogBasedController {


	
	@FXML
	private TextField titleTF;
	@FXML
	private TextArea contentTA;
	@FXML
	private DatePicker dateDP;

	private Message message;
	
	public NewMessageOverviewController(){
		
	}
	
	
	@FXML
	private void initialize(){
		
	}
	
	public void setMessage(Message message){
		this.message = message;
		if(message != null) getData();
		else {
			titleTF.setPromptText("WprowadŸ tytu³");
			contentTA.setPromptText("WprowadŸ treœæ");
			dateDP.setValue(LocalDate.now());
		}
			
		
		
		
		 //Editing the date would make no sense
		 dateDP.setEditable(false);
		 dateDP.setDisable(true);
	}
	
	public void getData(){
		titleTF.setText(message.getTitle());
		contentTA.setText(message.getContent());
		dateDP.setValue(message.getDate());
		

	}
	
	@FXML
	private void handleAddEditClick(){
		//Save data....
		if(message != null){
			
			//save changes in program
			 message.setContent(contentTA.getText());
			 message.setTitle(titleTF.getText());
			 message.setDate(dateDP.getValue());
			 

			 
			 //save changes in database
			 if(mainApp.getController().getDatabase().updateMessageRecord(message)){
				 UtilityClass.showAlert(AlertType.INFORMATION, "Edytuj wiadomoœæ", "Operacja zakoñczona pomyœlnie", "Pomyœlnie zapisano zmiany w wiadomoœci!", getCurrentStage()).showAndWait();
			 } 
			 
			 
			 getCurrentStage().close();
		} 
		else 
		{
			//create new message 
			Message tempMsg = new Message(Message.getNextID(),mainApp.getController().getLoggedUser(), titleTF.getText(), contentTA.getText(), dateDP.getValue());
			//save new message in program
			
			//save new message in database
			if(mainApp.getController().getDatabase().insertMessageRecord(tempMsg)){
				UtilityClass.showAlert(AlertType.INFORMATION, "Nowa wiadomoœæ", "Operacja zakoñczona pomyœlnie", "Pomyœlnie utworzono now¹ wiadomoœæ!", getCurrentStage()).showAndWait();
				message = tempMsg;
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d", "Nie uda³o siê dodaæ wiadomoœci!", getCurrentStage()).showAndWait();
				message = null;
			}
			
			
			getCurrentStage().close();
			
		}
	}
	
	@FXML
	private void handleCancelClick(){
		getCurrentStage().close();
	}
	
	public Message getMessage(){
		return this.message;
	}
	
}
