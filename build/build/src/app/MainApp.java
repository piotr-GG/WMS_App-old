package app;

import java.awt.Event;
import java.io.IOException;
import java.util.Optional;
import app.model.Employee;
import app.model.EnterpriseData;
import app.model.User;
import app.utility.Controller;
import app.utility.UtilityClass;
import app.view.RootOverviewController;
import app.view.EnterpriseDataDialogController;
import app.view.LoginOverviewController;
import app.view.RegisterFoundEmployeeController;
import app.view.RegisterNewEmployeeController;
import app.view.RegisterOverviewController;
import app.view.RootLayoutController;
import app.view.UserActionOverviewController;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class MainApp extends Application {

	
	private Controller controller; //handles DB queries and interacts with data
	
    private Stage primaryStage;
    private Stage dialogStage;
    private BorderPane rootLayout;
    private Image programIcon;

    public MainApp(){
    	controller = new Controller();
    	programIcon = new Image("file:src/app/view/images/mini icons/icon.png");
    }
    

    @Override
    public void start(Stage primaryStage) {
    	
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ForkLift WMS Warehouse Management System");
        
        //Set the application icon
        this.primaryStage.getIcons().add(programIcon);

        //Sets event handler on closing the window
        this.primaryStage.setOnCloseRequest(confirmCloseRequest);
        
        showRootOverview();
        boolean isDataAdded = loadEnterpriseData();
        if(isDataAdded)  primaryStage.show();
        else System.exit(0);
    }

    /**
     * Initializes the root layout - a borderpane object that is used as a root for the windows that inherit from ActionController class and has some basic functionality implemented for handling user commands
     */
    
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            primaryStage.show(); 
			RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            controller.initializeLanguageDisplay();
                       
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Shows the root window in the application
     */
    
	public void showRootOverview(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootOverview.fxml"));
			AnchorPane dataOverview = (AnchorPane) loader.load();
		
			primaryStage.setMinHeight(720.0);
			primaryStage.setMinWidth(1280.0);
			
			Scene scene = new Scene(dataOverview);
			primaryStage.setScene(scene);

			RootOverviewController controller = loader.getController();
			controller.setMainApp(this);
			controller.initializeLanguageDisplay();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows a window that is used to handle login commands
	 */
	
	public void showLoginOverview(){
			dialogStage = new Stage();
			dialogStage.setTitle("Logowanie");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image("file:src/app/view/images/mini icons/login.png"));
			
			LoginOverviewController controller = (LoginOverviewController) UtilityClass.showDialogFactory("view/LoginOverview.fxml", dialogStage, this, primaryStage, "Logowanie");
			controller.initializeCSSListener();
			controller.initializeLanguageDisplay();
			controller.getCurrentStage().setTitle("Logowanie");
            controller.getCurrentStage().showAndWait();
	}
	
	/**
	 * Shows a window with basic actions & commands that logged user can execute
	 */
	
	public void showUserActionOverview(){
		try{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/UserActionOverview.fxml"));
		AnchorPane UserActionOverview = (AnchorPane) loader.load();

		primaryStage.close();
		initRootLayout();
		rootLayout.setCenter(UserActionOverview);

		//Parallel transition for changing the center of the Root Layout's border pane
		rootLayout.centerProperty().addListener(new ChangeListener<Node>(){

			@Override
			public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
				if(newValue != null){
					oldValue.setOpacity(1.0);
					
					FadeTransition ft = new FadeTransition(Duration.millis(800), newValue);
					ft.setCycleCount(1);
					ft.setFromValue(0);
					ft.setToValue(1.0);

					TranslateTransition translateTransition =
				            new TranslateTransition(Duration.millis(1300), newValue);
				    translateTransition.setFromX(800);
				    translateTransition.setToX(0);
				    translateTransition.setCycleCount(1);
 
					ScaleTransition scaleTransition = 
					            new ScaleTransition(Duration.millis(1300), newValue);
					scaleTransition.setFromX(0.6f);
					scaleTransition.setFromY(0.6f);
					scaleTransition.setToX(1f);
					scaleTransition.setToY(1f);
					scaleTransition.setCycleCount(1);
					
					        
					ParallelTransition parallelTransition = new ParallelTransition();
					parallelTransition.getChildren().addAll(
					     ft, scaleTransition, translateTransition);
					parallelTransition.play();
				}
			}
			
		});
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		
		primaryStage.setMaximized(true);
		primaryStage.setMaximized(false);
		
		
		primaryStage.show();
		
		UserActionOverviewController controller = loader.getController();
        controller.setMainApp(this);
        controller.initializeLanguageDisplay();
 
		}
		catch(IOException e){
			e.printStackTrace();
		}	
		
	}
	

	/**
	 * Shows new window required for registering new user
	 */
	public void showRegisterOverview(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RegisterOverview.fxml"));
			AnchorPane registerOverview = (AnchorPane) loader.load();
			
			dialogStage = new Stage();
			dialogStage.setTitle("Rejestracja");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			dialogStage.getIcons().add(new Image("file:src/app/view/images/mini icons/register.png"));
			
			Scene scene = new Scene(registerOverview);
			dialogStage.setScene(scene);
			
			RegisterOverviewController controller = loader.getController();
			controller.setMainApp(this);
			controller.setDialogStage(dialogStage);
			dialogStage.showAndWait();
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Is shown when the query for an employee by particular name and last name returned result which means that user by given name & last name already exists in the database
	 * @param dialogData
	 * @param user
	 * @param ownerStage
	 * @return boolean
	 */
	public boolean showRegisterFoundEmployeeDialog(ObservableList<Employee> dialogData, User user, Stage ownerStage){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(("view/RegisterFoundEmployeeView.fxml")));
			AnchorPane registerDialog = (AnchorPane) loader.load();
			
			dialogStage = new Stage();
			dialogStage.setTitle("Przypisz u¿ytkownika");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(registerDialog);
			dialogStage.setScene(scene);
			
			RegisterFoundEmployeeController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setOwnerStage(ownerStage);
			controller.setMainApp(this);
			controller.setEmployeeList(dialogData);
			controller.setUser(user);
			dialogStage.showAndWait();
			return controller.isOkClicked();
		}
		catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Is shown when the query for an employee by particular name and last name has returned no result , which means that a new employee instance has to be created
	 * @param user
	 * @param ownerStage
	 * @return boolean 
	 */
	public boolean showRegisterNewEmployeeView(User user, Stage ownerStage){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(("view/RegisterNewEmployeeView.fxml")));
			AnchorPane registerDialog = (AnchorPane) loader.load();
			
			dialogStage = new Stage();
			dialogStage.setTitle("Utwórz nowego pracownika");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(registerDialog);
			dialogStage.setScene(scene);
			
			RegisterNewEmployeeController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setOwnerStage(ownerStage);
			controller.setMainApp(this);
			controller.setUser(user);
			dialogStage.showAndWait();
			
			return true;
			
		}
		catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}

	public Stage getPrimaryStage(){
		return primaryStage;
	}
	
	public Stage getDialogStage(){
		return dialogStage;
	}
	
	public Controller getController(){
		return this.controller;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void setPrimaryStage(Stage primaryStage){
		this.primaryStage = primaryStage;
	}
	
	public Image getProgramIcon(){
		return this.programIcon;
	}
	

	public BorderPane getRootLayout(){
		return this.rootLayout;
	}
	
	/**
	 * EventHandler that is executed when the close window request is fired
	 */
	private EventHandler<WindowEvent> confirmCloseRequest = event -> {
		 Optional<ButtonType> selectedOption = UtilityClass.showAlert(AlertType.CONFIRMATION, "PotwierdŸ", "PotwierdŸ", "Czy na pewno chcesz wyjœæ z programu?", primaryStage).showAndWait();
	      if (!ButtonType.OK.equals(selectedOption.get())) {
	         event.consume();
	      }
    };
    
    /**
     * Is shown when the query for enterprise data stored in the DB has returned no result. 
     * @return
     */
	private boolean loadEnterpriseData() {
		EnterpriseData.initializeEnterpriseData();
		boolean isEnterpriseData = controller.getDatabase().getEnterpriseData();
		if(isEnterpriseData){
			return true;
		} else {
			EnterpriseDataDialogController controller = (EnterpriseDataDialogController) UtilityClass.showDialogFactory("view/EnterpriseDataDialog.fxml", dialogStage, this, primaryStage, "Dane przedsiêbiorstwa");
			controller.getCurrentStage().setResizable(false);
			
			//Set the event handler on close request so that user is prompted to confirm his "exit" command
			controller.getCurrentStage().setOnCloseRequest(event -> {
				Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Wyjœcie z programu", "Potwierdzenie", "Brak danych firmy uniemo¿liwia korzystanie z programu. Czy na pewno chcesz wyjœæ z programu?", dialogStage).showAndWait();
				if(!result.get().equals(ButtonType.OK)){
					event.consume();
				}
			});
			controller.getCurrentStage().showAndWait();
			if(controller.isCorrectlyAdded()) return true;
			else return false;
		}
	} 
	

}


