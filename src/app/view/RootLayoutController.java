package app.view;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;

import app.MainApp;
import app.model.Employee;
import app.model.Permission;
import app.model.User;
import app.utility.UTF8Control;
import app.utility.UtilityClass;
import app.view.inheritance.LanguageInterface;
import app.view.menuItemDialogs.AboutDialogController;
import app.view.menuItemDialogs.DevelopersDialogController;
import app.view.menuItemDialogs.IntroductoryMovieDialogController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class RootLayoutController implements LanguageInterface {

	private Stage ownerStage;
	private Stage dialogStage;
	private MainApp mainApp;
	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private Menu fileMenu;
	@FXML
	private MenuItem closeMenuItem;
	@FXML
	private Menu optionsMenu;
	@FXML
	private Menu languageMenu;
	@FXML
	private RadioMenuItem polishRMI;
	@FXML
	private RadioMenuItem englishRMI;
	@FXML
	private RadioMenuItem germanRMI;
	@FXML
	private RadioMenuItem spanishRMI;
	@FXML
	private MenuItem settingsMenuItem;
	@FXML
	private Menu helpMenu;
	@FXML
	private MenuItem aboutMenuItem;
	@FXML
	private MenuItem developersMenuItem;
	@FXML
	private MenuItem introductoryMovieMenuItem;
	
	private ToggleGroup languageGroup;
	
	@FXML
	private Label settingsLabel;
	@FXML
	private Label chooseYourLanguageLabel;
	@FXML
	private Button logoutButton;
	@FXML
	private Button myProfileButton;
	@FXML
	private Label userLabel;
	@FXML
	private ImageView userIcon;
	@FXML
	private ImageView adminPanelAccessImage;
	@FXML
	private Label adminPanelAccessLabel;
	
	private ResourceBundle languagePack;
	
	private StringProperty youAreLoggedProperty;

	@FXML
	private void initialize(){
		languageGroup = new ToggleGroup();
		polishRMI.setToggleGroup(languageGroup);
		englishRMI.setToggleGroup(languageGroup);
		germanRMI.setToggleGroup(languageGroup);
		spanishRMI.setToggleGroup(languageGroup);
		
		settingsMenuItem.setOnAction(eventHandler -> showSettingsOverview());
		closeMenuItem.setOnAction(event -> {
		    	 mainApp.getPrimaryStage().fireEvent(new WindowEvent(mainApp.getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST));
		         event.consume();
		});
		
		aboutMenuItem.setOnAction(eventHandler -> showAboutDialog());
		developersMenuItem.setOnAction(eventHandler -> showDevelopersDialog());
		introductoryMovieMenuItem.setOnAction(eventHandler -> showIntroductoryMovieDialog());
	}


	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		
		initializeActionButton();
		updateDisplay();
		setCurrentUserText();
		
		updateCSSStyles();
		mainApp.getController().getStyleSheetPathProperty().addListener(changeListener -> {
			updateCSSStyles();
		});
	}
	
	private void updateCSSStyles(){
		mainPane.getStylesheets().clear();
		mainPane.getStylesheets().add(mainApp.getController().getStyleSheetPath());
	}
	
	@FXML
	private void handleLogoutClick(){
		if(mainApp.getController().logout()){
			//show an alert and go to the first scene
			Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Wylogowanie", "Potwierdzenie", "Czy chcesz wylogowa� si� z programu?", ownerStage).showAndWait();
		    if(result.get() == ButtonType.OK){	
		    	//user clicked OK
		    	UtilityClass.showAlert(AlertType.INFORMATION, "Wylogowanie", "Wylogowanie", "Pomy�lnie wylogowano z programu", ownerStage).showAndWait();
		 	    } 
		    //close stage that currently uses root layout and show the entering screen
		    if(ownerStage != null) ownerStage.close();
		     
			mainApp.showRootOverview();
			mainApp.getPrimaryStage().show();
			
		} else {
			//exit program if user has not been logged out properly
			UtilityClass.showAlert(AlertType.ERROR, "Wylogowanie", "Wylogowanie", "U�ytkownik nie zosta� poprawnie wylogowany z programu", ownerStage).showAndWait();
			System.exit(0);
		}
	}
	
	@FXML
	private void handleSettingsClick(){
		showSettingsOverview();
	}
	
	@FXML
	private void handleMyProfileClick(){
		showMyProfileDialog(mainApp.getController().getLoggedUser());
	}
	
	
	
	/**
	 * Shows new dialogStage with user data 
	 * @param user
	 * @return
	 */
	private boolean showMyProfileDialog(User user){
		MyProfileOverviewController controller = (MyProfileOverviewController) UtilityClass.showDialogFactory("view/MyProfileOverview.fxml", dialogStage, mainApp, mainApp.getPrimaryStage(), "M�j profil");
		controller.setUser(user);
		controller.getCurrentStage().setTitle("M�j profil");
		controller.getCurrentStage().showAndWait();
		return controller.isOkClicked();
		
	}
	
	@FXML
	private void handlePOLClick(){
		mainApp.getController().setCurrentLocale(Locale.ROOT);
	}
	
	@FXML
	private void handleENGClick(){
		mainApp.getController().setCurrentLocale(Locale.US);
	}
	
	@FXML
	private void handleGERClick(){
		mainApp.getController().setCurrentLocale(Locale.GERMANY);
		
	}
	@FXML
	private void handleESPClick(){
		mainApp.getController().setCurrentLocale(mainApp.getController().getEspLocale());
	}
	
	/**
	 * Sets text of userLabel according to user currently logged in the program
	 */
	public void setCurrentUserText(){
		boolean logged = mainApp.getController().getLoginStatus();
		if(logged){
			User loggedUser = mainApp.getController().getLoggedUser();
			Employee loggedEmployee = mainApp.getController().getLoggedUser().getEmployee();
			if(loggedEmployee != null){
				youAreLoggedProperty = new SimpleStringProperty(languagePack.getString("youAreLogged"));
				StringExpression expression = youAreLoggedProperty.concat (" ").concat(loggedEmployee.getFirstNameProperty().concat(" ").concat(loggedEmployee.getLastNameProperty())); //Concatenates first name string property with "" string and then with last name string property
				userLabel.textProperty().bind(expression);  //bind the concatenated expression with user label
				
				if(loggedUser.getUserImage() != null){
					Bindings.bindBidirectional(this.userIcon.imageProperty(), loggedUser.getUserImage());
				}
			
			} 
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Brak przypisanego pracownika");
				alert.setHeaderText(null);
				alert.setContentText("Brak przypisanego pracownika do konta u�ytkownika. Koniec programu.");
				alert.showAndWait();
				System.exit(0);
			}
		} 
	
		
		
	}
	
	/**
	 * Sets stage that is currently in use and stores it to close the stage when user logs out
	 * @param ownerStage
	 */
	public void setOwnerStage(Stage ownerStage){
		this.ownerStage = ownerStage;
	}
	
	private void initializeActionButton(){
		Permission p = mainApp.getController().getLoggedUser().getPermission();
		
		if(p.isHasAdminRights()){
			adminPanelAccessImage.setVisible(true);
			adminPanelAccessImage.setOnMouseEntered(e -> adminPanelAccessLabel.setVisible(true));
			adminPanelAccessImage.setOnMouseExited(e -> adminPanelAccessLabel.setVisible(false));
			adminPanelAccessImage.setOnMouseClicked(e -> showAdminPanel());
			
		} else {
			adminPanelAccessImage.setVisible(false);
			adminPanelAccessImage.setOnMouseClicked(null);
		}
				
	}
	
	/**
	 * Shows new frame for admin actions
	 */
	private void showAdminPanel(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/AdminPanelOverview.fxml"));
			AnchorPane adminPanel = (AnchorPane) loader.load();
			
			Scene scene = new Scene(adminPanel);
			dialogStage = new Stage();
			dialogStage.setTitle("Panel administratora");
			dialogStage.setScene(scene);
			

			//Nadanie dost�pu kontrolerowi do aplikacji
			AdminPanelController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setOwnerStage(ownerStage);
			controller.setCurrentStage(dialogStage);
			controller.setDefaultIcon();
			
			
			dialogStage.initOwner(ownerStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.show();
	}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	

	private void showAboutDialog(){
		AboutDialogController controller = (AboutDialogController) UtilityClass.showDialogFactory("view/menuItemDialogs/AboutDialog.fxml", dialogStage, mainApp, ownerStage, "O nas");
		controller.getCurrentStage().showAndWait();
	}
	
	private void showDevelopersDialog(){
		DevelopersDialogController controller = (DevelopersDialogController) UtilityClass.showDialogFactory("view/menuItemDialogs/DevelopersDialog.fxml", dialogStage, mainApp, ownerStage, "Tw�rcy");
		controller.getCurrentStage().showAndWait();
	}
	
	private void showIntroductoryMovieDialog(){
		IntroductoryMovieDialogController controller = (IntroductoryMovieDialogController) UtilityClass.showDialogFactory("view/menuItemDialogs/IntroductoryMovieDialog.fxml", dialogStage, mainApp, ownerStage, "Film wprowadzaj�cy");
		controller.initializeListener();
		controller.getCurrentStage().showAndWait();
	}
	
	private void showSettingsOverview(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SettingsDialogOverview.fxml"));
			BorderPane settingsPanel = (BorderPane) loader.load();
			
			Scene scene = new Scene(settingsPanel);
			dialogStage = new Stage();
			dialogStage.setTitle("Ustawienia");
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);

			//Nadanie dost�pu kontrolerowi do aplikacji
			SettingsDialogOverviewController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setOwnerStage(ownerStage);
			controller.setCurrentStage(dialogStage);
			controller.setDefaultIcon();
			controller.initializeSettings();
			controller.initializeLanguageDisplay();
			
			
			dialogStage.initOwner(ownerStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.show();
	}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void updateDisplay() {
		languagePack = ResourceBundle.getBundle("app.view.language.RootLayoutController", mainApp.getController().getCurrentLocale(), new UTF8Control());
		settingsLabel.setText(languagePack.getString("settings"));
		chooseYourLanguageLabel.setText(languagePack.getString("chooseYourLanguage"));
		myProfileButton.setText(languagePack.getString("myProfile"));
		logoutButton.setText(languagePack.getString("logout"));
		if (youAreLoggedProperty != null) youAreLoggedProperty.set(languagePack.getString("youAreLogged"));
		fileMenu.setText(languagePack.getString("fileMenu"));
		closeMenuItem.setText(languagePack.getString("closeMenu"));
		optionsMenu.setText(languagePack.getString("optionsMenu"));
		languageMenu.setText(languagePack.getString("languageMenu"));
		polishRMI.setText(languagePack.getString("polishRMI"));
		englishRMI.setText(languagePack.getString("englishRMI"));
		germanRMI.setText(languagePack.getString("germanRMI"));
		spanishRMI.setText(languagePack.getString("spanishRMI"));
		settingsMenuItem.setText(languagePack.getString("settingsMenuItem"));
		helpMenu.setText(languagePack.getString("helpMenu"));
		aboutMenuItem.setText(languagePack.getString("aboutMenuItem"));
		developersMenuItem.setText(languagePack.getString("developersMenuItem"));
		introductoryMovieMenuItem.setText(languagePack.getString("introductoryMovie"));
		
	}


	@Override
	public void initializeLanguageDisplay() {
		mainApp.getController().getLocaleProperty().addListener(new ChangeListener<Locale>(){

			@Override
			public void changed(ObservableValue<? extends Locale> observable, Locale oldValue, Locale newValue) {
				updateDisplay();
			}
		});
		
	}
	
	
}
