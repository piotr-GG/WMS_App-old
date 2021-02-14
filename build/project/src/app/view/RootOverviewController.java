package app.view;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import app.MainApp;
import app.utility.UTF8Control;
import app.view.inheritance.LanguageInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RootOverviewController implements LanguageInterface {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private Label loginLabel;
	@FXML
	private Label registerLabel;
	@FXML
	private Label settingsLabel;
	@FXML
	private Label chooseLanguageLabel;
	@FXML
	private Label helloTextLabel;
	@FXML
	private Label loginBelowTextLabel;
	@FXML
	private Label registerBelowTextLabel;
	@FXML
	private Label footerLabel;
	
	private MainApp mainApp;
    private ResourceBundle languagePack;
    private Stage dialogStage;

	
	
	/**
	 * Funkcja, która ustawia g³ówn¹ aplikacjê
	 */
	
	@SuppressWarnings("unchecked")
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
		
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
	private void handleZalogujClick(){
		mainApp.showLoginOverview();
		
	}
	
	@FXML
	private void handleRegisterClick(){
		mainApp.showRegisterOverview();
	}
	
	//Choose current locale
	
	public void setCurrentLocale(Locale locale){
		mainApp.getController().setCurrentLocale(locale);
	}
	

	
	@FXML
	private void handlePOLClick(){
		setCurrentLocale(Locale.ROOT);
	}
	
	@FXML
	private void handleENGClick(){
		setCurrentLocale(Locale.US);
	}
	
	@FXML
	private void handleGERClick(){
		setCurrentLocale(Locale.GERMANY);
		
	}
	
	@FXML
	private void handleESPClick(){
		setCurrentLocale(mainApp.getController().getEspLocale());
	}
	
	@FXML
	private void handleSettingsClick(){
		showSettingsOverview();
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

			
			//Nadanie dostêpu kontrolerowi do aplikacji
			SettingsDialogOverviewController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setOwnerStage(mainApp.getPrimaryStage());
			controller.setCurrentStage(dialogStage);
			controller.setDefaultIcon();
			controller.initializeSettings();
			controller.initializeLanguageDisplay();
			
			
			dialogStage.initOwner(mainApp.getPrimaryStage());
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.show();
	}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	//Updates labels
	
	public void updateDisplay(){
		languagePack = ResourceBundle.getBundle("app.view.language.DataOverview", mainApp.getController().getCurrentLocale(), new UTF8Control());
		loginLabel.setText(languagePack.getString("login"));
		registerLabel.setText(languagePack.getString("register"));
		settingsLabel.setText(languagePack.getString("settings"));
		chooseLanguageLabel.setText(languagePack.getString("chooseLanguage"));
		helloTextLabel.setText(languagePack.getString("helloText"));
		loginBelowTextLabel.setText(languagePack.getString("loginBelowText"));
		registerBelowTextLabel.setText(languagePack.getString("registerBelowText"));
		footerLabel.setText(languagePack.getString("footer"));

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
