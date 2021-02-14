package app.view;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import app.model.Employee.Position;
import app.utility.UTF8Control;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import app.view.inheritance.LanguageInterface;
import app.view.settingsPanes.EnterprisePaneController;
import app.view.settingsPanes.LanguagePaneController;
import app.view.settingsPanes.LookAndFeelPaneController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class SettingsDialogOverviewController extends DialogBasedController implements LanguageInterface {
	

	@FXML
	private Label pleaseClickLinksLabel;
	@FXML
	private TreeView<String> settingsTree;
	@FXML
	private BorderPane settingsPane;
	
	private TreeItem<String> root;
	private TreeItem<String> languageItem;
	private TreeItem<String> enterpriseItem;
	private TreeItem<String> lookAndFeelItem;
	
	private AnchorPane languagePane = null;
	private AnchorPane enterprisePane = null;
	private AnchorPane lookAndFeelPane = null;
	
	private int paneCurrentlyUsed;
	
	private static final int LANGUAGE_PANE = 1;
	private static final int ENTERPRISE_PANE = 2;
	private static final int LOOK_AND_FEEL_PANE = 3;
	
	private ResourceBundle languagePack;
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	
	@FXML
	private void initialize(){
		
		final Node rootIcon = new ImageView("file:src/app/view/images/settings/settings.png");
		final Node languageIcon = new ImageView("file:src/app/view/images/settings/language.png");
		final Node enterpriseIcon = new ImageView("file:src/app/view/images/settings/enterprise.png");
		final Node lookAndFeelIcon = new ImageView("file:src/app/view/images/settings/lookandfeel.png");
		
		root = new TreeItem<String>("Ustawienia", rootIcon);
		languageItem = new TreeItem<String>("Jêzyk", languageIcon);
		enterpriseItem = new TreeItem<String>("Firma", enterpriseIcon);
		lookAndFeelItem = new TreeItem<String>("Wygl¹d", lookAndFeelIcon);

		settingsTree.getSelectionModel().selectedItemProperty().addListener((ChangeListener<TreeItem>) (arg0, oldItem, newItem) -> {
			if(newItem != null){
				handleClick(newItem);
			}
			
		});
		root.getChildren().setAll(enterpriseItem, languageItem, lookAndFeelItem);
		settingsTree.setRoot(root);

	}
	
	public void initializeSettings(){
		updateCSSStyles();
		mainApp.getController().getStyleSheetPathProperty().addListener(changeListener -> {
			updateCSSStyles();
		});
	}
	
	private void updateCSSStyles(){
		settingsPane.getStylesheets().clear();
		settingsPane.getStylesheets().add(mainApp.getController().getStyleSheetPath());
	}
	
	private void handleClick(TreeItem<String> item){
		if(item != null){
			if(item.equals(languageItem)) setLanguagePane();
			if(item.equals(enterpriseItem)){
				if(mainApp.getController().getLoginStatus() == true){
					if(mainApp.getController().getLoggedUser().getPermission().isHasAdminRights()){
						setEnterprisePane();
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "Brak uprawnieñ", "Dostêp zabroniony", "Tylko administrator mo¿e wyœwietlaæ i edytowaæ dane firmy!", getCurrentStage()).showAndWait();
					}
				}  else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Proszê siê zalogowaæ!", "Dostêp mo¿liwy tylko dla zalogowanych u¿ytkowników!", getCurrentStage()).showAndWait();
				}
				
			}
			if(item.equals(lookAndFeelItem)) setLookAndFeelPane();
		}
	}
	
	private void setLanguagePane(){
		if(languagePane == null){
			loadLanguagePane();
			settingsPane.setRight(languagePane);
		} else {
			if(paneCurrentlyUsed != LANGUAGE_PANE){
				settingsPane.setRight(languagePane);
				paneCurrentlyUsed = LANGUAGE_PANE;
			}
		}
	}
	
	private void setEnterprisePane(){
		if(enterprisePane == null){
			loadEnterprisePane();
			settingsPane.setRight(enterprisePane);
		} else {
			if(paneCurrentlyUsed != ENTERPRISE_PANE){
				settingsPane.setRight(enterprisePane);
				paneCurrentlyUsed = ENTERPRISE_PANE;
			}
		}
	}
	
	private void setLookAndFeelPane(){
		if(lookAndFeelPane == null){
			loadLookAndFeelPane();
			settingsPane.setRight(lookAndFeelPane);
		} else {
			if(paneCurrentlyUsed != LOOK_AND_FEEL_PANE){
				settingsPane.setRight(lookAndFeelPane);
				paneCurrentlyUsed = LOOK_AND_FEEL_PANE;
			}
		}
	}
	

	private void loadLanguagePane(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(mainApp.getClass().getResource("view/settingsPanes/LanguagePane.fxml"));
			languagePane  = (AnchorPane) loader.load();
			
			LanguagePaneController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.initializeData();
			controller.setOwnerStage(getCurrentStage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadEnterprisePane(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(mainApp.getClass().getResource("view/settingsPanes/EnterprisePane.fxml"));
			enterprisePane  = (AnchorPane) loader.load();
			
			EnterprisePaneController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setOwnerStage(getCurrentStage());
			controller.fillTheFields();
			controller.initializeLanguageDisplay();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadLookAndFeelPane(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(mainApp.getClass().getResource("view/settingsPanes/LookAndFeelPane.fxml"));
			lookAndFeelPane  = (AnchorPane) loader.load();
			
			LookAndFeelPaneController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setOwnerStage(getCurrentStage());
			controller.initializeData();
			controller.initializeLanguageDisplay();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleReturnClick(){
		getCurrentStage().close();
	}

	@Override
	public void updateDisplay() {
		languagePack = ResourceBundle.getBundle("app.view.language.SettingsDialog", mainApp.getController().getCurrentLocale(), new UTF8Control());
		pleaseClickLinksLabel.setText(languagePack.getString("pleaseClickLinks"));
		root.setValue(languagePack.getString("root"));
		languageItem.setValue(languagePack.getString("language"));
		enterpriseItem.setValue(languagePack.getString("enterprise"));
		lookAndFeelItem.setValue(languagePack.getString("lookAndFeel"));
		
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
