package app.view.inheritance;

import app.MainApp;
import javafx.stage.Stage;

public interface DialogBasedInterface {

	public void setMainApp(MainApp mainApp);
	public void setOwnerStage(Stage ownerStage);
	public void setCurrentStage(Stage currentStage);
	
}
