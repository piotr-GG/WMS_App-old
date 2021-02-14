package app.view.inheritance;


import java.util.ResourceBundle;

import app.MainApp;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public abstract class ActionController implements ActionInterface {

	/**
	 * Variables associated with showing the stage etc.
	 */
	
    protected MainApp mainApp;
    protected Node previousPane;
    
    protected Stage ownerStage;
    protected Stage currentStage;
    protected Stage dialogStage;
	
    protected ResourceBundle languagePack;
	
    public void setPreviousPane(Node previousPane){
    	this.previousPane = previousPane;
    }
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;	
	}
	
	public void setCurrentStage(Stage currentStage){
		this.currentStage = currentStage;
	}
	
	@FXML
	private void handleArrowClick(){
		mainApp.getRootLayout().setCenter(previousPane);
	}
	
}
