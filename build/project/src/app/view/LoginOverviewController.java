package app.view;

import java.util.Locale;
import java.util.ResourceBundle;

import app.MainApp;
import app.model.Permission;
import app.model.User;
import app.utility.Controller;
import app.utility.UTF8Control;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import app.view.inheritance.LanguageInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginOverviewController extends DialogBasedController implements LanguageInterface {

	@FXML
	private AnchorPane mainPane;
	@FXML 
	private Button loginBTN;
	@FXML
	private Label loginPanelLabel;
	@FXML
	private Label userNameLabel;
	@FXML
	private Label passwordLabel;
	@FXML
	private TextField userNameTF;
	@FXML
	private PasswordField passwordPF;
	
	private ResourceBundle languagePack;
		
	public void initializeCSSListener(){
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
	private void handleLoginBTNClick(){
	  if(!userNameTF.getText().isEmpty() && !passwordPF.getText().isEmpty()){
		String login = userNameTF.getText();
		String password = passwordPF.getText();
		
		boolean loggedSuccessfully = login(login, password);
		
		if(loggedSuccessfully){
			if(mainApp.getController().getLoggedUser().getPermission().isBlockedAccount()){  
				//check if user account is blocked
				Permission.showBlockedAccountAlert();
				} 
			else {
			    UtilityClass.showAlert(AlertType.INFORMATION, languagePack.getString("titleLoggedSuccessfully"), languagePack.getString("headerLoggedSuccessfully"), languagePack.getString("contentLoggedSuccessfully"), getCurrentStage()).showAndWait();
			    mainApp.showUserActionOverview();
		   }
		 }
	  }

	}
	
	public boolean login(String login, String password){
		
		boolean isUserExisting = mainApp.getController().getDatabase().checkIfUserExits(login);
		if(isUserExisting){
			User loggedUser = mainApp.getController().getDatabase().loginProcedure(login, password);
			if(loggedUser != null){
				mainApp.getController().setLoggedUser(loggedUser);
				mainApp.getController().setLoginStatus(true);
				return true;
			} else {
				UtilityClass.showAlert(AlertType.ERROR, languagePack.getString("titleProcError"), languagePack.getString("headerProcError"), languagePack.getString("contentProcError"), getCurrentStage()).showAndWait();
				return false;
			}
 		} else {
			UtilityClass.showAlert(AlertType.ERROR, languagePack.getString("titleNoUser"), languagePack.getString("headerNoUser"), languagePack.getString("contentNoUser"), getCurrentStage()).showAndWait();
			return false;
		}
		
	}

	@Override
	public void updateDisplay() {
		languagePack = ResourceBundle.getBundle("app.view.language.LoginOverview", mainApp.getController().getCurrentLocale(), new UTF8Control());
		loginBTN.setText(languagePack.getString("loginBTN"));
		loginPanelLabel.setText(languagePack.getString("loginPanelLabel"));
		userNameLabel.setText(languagePack.getString("userNameLabel"));
		passwordLabel.setText(languagePack.getString("passwordLabel"));
	}

	@Override
	public void initializeLanguageDisplay() {
		updateDisplay();
		mainApp.getController().getLocaleProperty().addListener(new ChangeListener<Locale>(){
			@Override
			public void changed(ObservableValue<? extends Locale> observable, Locale oldValue, Locale newValue) {
				updateDisplay();
			}
		});
	}
	
}
