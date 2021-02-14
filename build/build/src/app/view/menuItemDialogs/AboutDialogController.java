package app.view.menuItemDialogs;

import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;

public class AboutDialogController extends DialogBasedController{

	
	
	
	
	
	@FXML
	private void handleReturnClick(){
		getCurrentStage().close();
	}
}
