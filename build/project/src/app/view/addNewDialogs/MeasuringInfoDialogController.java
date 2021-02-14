package app.view.addNewDialogs;

import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MeasuringInfoDialogController extends DialogBasedController {

	@FXML
	private ImageView image;

	@FXML
	private void intialize(){
		
	}
	
	@FXML
	private void handle1Button(){
		image.setImage(new Image("file:src/app/view/images/meat/1.png"));
	}
	
	@FXML
	private void handle2Button(){
		image.setImage(new Image("file:src/app/view/images/meat/2.png"));
	}
	
	@FXML
	private void handle3Button(){
		image.setImage(new Image("file:src/app/view/images/meat/3.png"));
	}
	
	@FXML
	private void handle4Button(){
		image.setImage(new Image("file:src/app/view/images/meat/4.png"));
	}
	
	@FXML
	private void handle5Button(){
		image.setImage(new Image("file:src/app/view/images/meat/5.png"));
	}
}
