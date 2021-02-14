package app.view.menuItemDialogs;



import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DevelopersDialogController extends DialogBasedController {


	@FXML
	private AnchorPane mainPane;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private VBox vBox;
	@FXML
	private TextField durationField;
	
	private Timeline task;
	
	@FXML
	private void initialize(){
		durationField.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.length() != 0){
					if(!UtilityClass.isParsable(newValue)){
						if(oldValue.length() != 0 ) durationField.setText(oldValue);
						else durationField.setText("0");
					}
				}
				
			}
			
		});
	}
	@FXML
	private void handleReturnClick(){
		getCurrentStage().close();
	}
	
	@FXML
	private void handlePlayClick(){
		if(durationField.getText().length() != 0 && UtilityClass.isParsable(durationField.getText())){
			int duration = Integer.parseInt(durationField.getText());
			if(duration > 0){
				task = new Timeline(
						new KeyFrame(Duration.ZERO, 
								new KeyValue(scrollPane.vvalueProperty(), scrollPane.getVmin())),
						new KeyFrame(Duration.seconds(duration), 
								new KeyValue(scrollPane.vvalueProperty(), scrollPane.getVmax()))
						);
				task.playFromStart();
				
			} else{
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Czas trwania nie mo¿e byæ liczb¹ ujemn¹!", getCurrentStage()).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna wartoœæ", "Brak podanego czasu trwania lub podana wartoœæ nie jest liczb¹ ca³kowit¹!", getCurrentStage()).showAndWait();
		}
	}
		
}
