package app.view.inheritance;

import app.MainApp;
import javafx.stage.Stage;

/**
 * Class that is designed to be inherited by other controller classes which are mere dialog stages
 * @author Piotrek
 *
 */

public abstract class DialogBasedController implements DialogBasedInterface {

    protected MainApp mainApp;
    protected Stage ownerStage;
    private Stage currentStage;
    protected Stage dialogStage;
	
	
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;	
	}
	
	public void setOwnerStage(Stage ownerStage){
		this.ownerStage = ownerStage;
	}

	public void setCurrentStage(Stage currentStage){
		this.currentStage = currentStage;

	}
	
	public void setDefaultIcon(){
		currentStage.getIcons().add(mainApp.getProgramIcon());
	}
	
	public Stage getCurrentStage(){
		return this.currentStage;
	}
}
