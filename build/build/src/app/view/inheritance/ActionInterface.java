package app.view.inheritance;


import app.MainApp;
import javafx.scene.Node;
import javafx.stage.Stage;

public interface ActionInterface {

	public void setMainApp(MainApp mainApp);
	public void setPreviousPane(Node previousPane);
	public void setCurrentStage(Stage currentStage);
 
}
