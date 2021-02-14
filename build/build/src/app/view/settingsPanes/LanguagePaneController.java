package app.view.settingsPanes;

import java.util.Locale;
import java.util.ResourceBundle;

import app.utility.UTF8Control;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import app.view.inheritance.LanguageInterface;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class LanguagePaneController extends DialogBasedController implements LanguageInterface{

	@FXML
	private Label languageLabel;
	@FXML
	private Label chooseYourLanguageLabel;
	@FXML
	private Button applyButton;
	@FXML
	private Button cancelButton;
	
	@FXML
	private ComboBox<LanguageData> languageBox;
	
	private ObservableList<LanguageData> languageList;
	private ResourceBundle languagePack;
	
	public void initializeData(){
		languageList = FXCollections.observableArrayList();
		languageList.addAll(new LanguageData("Polski", Locale.ROOT, "POL"), new LanguageData("Angielski", Locale.US, "ENG"), new LanguageData("Niemiecki", Locale.GERMANY, "GER") , new LanguageData("Hiszpañski", mainApp.getController().getEspLocale(), "SPA"));
		languageBox.getItems().addAll(languageList);
		
		updateDisplay();
	}
	
	@FXML
	private void handleApplyClick(){
		LanguageData item = languageBox.getSelectionModel().getSelectedItem();
		if(item != null) {
			if(!mainApp.getController().getCurrentLocale().equals(item.getLocaleAssociated())){
				mainApp.getController().setCurrentLocale(item.getLocaleAssociated());
				updateDisplay();
				
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczenia", "Proszê zaznaczyæ jêzyk do ustawienia!", getCurrentStage()).showAndWait();
		}
	}
	
	@FXML
	private void handleCancelClick(){
		ownerStage.close();
	}

	@Override
	public void updateDisplay() {
		languagePack = ResourceBundle.getBundle("app.view.language.LanguagePane", mainApp.getController().getCurrentLocale(), new UTF8Control());
		
		languageLabel.setText(languagePack.getString("language"));
		chooseYourLanguageLabel.setText(languagePack.getString("chooseYourLanguage"));
		applyButton.setText(languagePack.getString("apply"));
		cancelButton.setText(languagePack.getString("cancel"));
		
		for(LanguageData ld : languageList) {
			ld.updateLanguageNames();
		}
		
		
		int indexSelected = languageBox.getSelectionModel().getSelectedIndex();
		languageBox.getItems().clear();
		languageBox.getItems().addAll(languageList);
		languageBox.getSelectionModel().select(indexSelected);

		
	}

	public class LanguageData{
		private StringProperty languageName;
		private Locale localeAssociated;
		private String languageTag;
		
		public LanguageData(String languageName, Locale localeAssociated, String languageTag){
			this.languageName = new SimpleStringProperty(languageName);
			this.localeAssociated = localeAssociated;
			this.languageTag = languageTag;
		}
		
		
		public String toString(){
			return languageName.get();
			
		}
		
		public String getLanguageName(){
			return this.languageName.get();
		}
		
		public Locale getLocaleAssociated(){
			return this.localeAssociated;
		}
		
		public void updateLanguageNames(){
			languageName.set(languagePack.getString(languageTag));
		}
	}

	@Override
	public void initializeLanguageDisplay() {
		// TODO Auto-generated method stub
		
	}
}
