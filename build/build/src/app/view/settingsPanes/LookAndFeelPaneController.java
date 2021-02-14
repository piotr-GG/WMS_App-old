package app.view.settingsPanes;

import java.util.Locale;
import java.util.ResourceBundle;

import app.utility.UTF8Control;
import app.utility.UtilityClass;
import app.view.inheritance.DialogBasedController;
import app.view.inheritance.LanguageInterface;
import app.view.settingsPanes.LanguagePaneController.LanguageData;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class LookAndFeelPaneController extends DialogBasedController implements LanguageInterface {

	@FXML
	private Label lookAndFeelTitle;
	@FXML
	private Label chooseYourLAFLabel;
	@FXML
	private Button applyButton;
	@FXML
	private Button cancelButton;
	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private ComboBox<LookAndFeel> lookAndFeelBox;
	
	private ObservableList<LookAndFeel> lookAndFeelList;
	private ResourceBundle languagePack;
	
	public void initializeData(){
		lookAndFeelList = FXCollections.observableArrayList();
		lookAndFeelList.add(new LookAndFeel("Classic", "file:src/app/view/MyTheme.css"));
		lookAndFeelList.add(new LookAndFeel("Progressive", "file:src/app/view/SecondTheme.css"));
		lookAndFeelBox.setItems(lookAndFeelList);
		
		LookAndFeel currentlyUsedLAF = getCurrentlyUsedLookAndFeel();
		if(currentlyUsedLAF != null) lookAndFeelBox.getSelectionModel().select(currentlyUsedLAF);
		
		updateCSSStyles();
		mainApp.getController().getStyleSheetPathProperty().addListener(changeListener -> {
			updateCSSStyles();
		});
		
	}
	
	private void updateCSSStyles(){
		mainPane.getStylesheets().clear();
		mainPane.getStylesheets().add(mainApp.getController().getStyleSheetPath());
	}
	
	private LookAndFeel getCurrentlyUsedLookAndFeel(){
		for(LookAndFeel laf : lookAndFeelList) if(laf.getStyleSheetPathProperty().equals(mainApp.getController().getStyleSheetPathProperty())) return laf;
		return null;
	}
	

	
	@FXML
	private void handleApplyClick(){
		LookAndFeel selectedItem = lookAndFeelBox.getSelectionModel().getSelectedItem();
		if(selectedItem != null){
			if(!selectedItem.getStyleSheetPath().equals(mainApp.getController().getStyleSheetPath())){
				mainApp.getController().setStyleSheetPath(selectedItem.getStyleSheetPath());
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczonego motywu", "Proszê zaznaczyæ odpowiedni motyw", getCurrentStage()).showAndWait();
		}
	}
	
	@FXML
	private void handleCancelClick(){
		ownerStage.close();
	}

	@Override
	public void updateDisplay() {
		languagePack = ResourceBundle.getBundle("app.view.language.LookAndFeelPane", mainApp.getController().getCurrentLocale(), new UTF8Control());
		
		lookAndFeelTitle.setText(languagePack.getString("lookAndFeelTitle"));
		chooseYourLAFLabel.setText(languagePack.getString("chooseYourLAF"));
		applyButton.setText(languagePack.getString("apply"));
		cancelButton.setText(languagePack.getString("cancel"));
	}
	
	public class LookAndFeel{
		private StringProperty lookAndFeelName;
		private StringProperty styleSheetPath;
		
		public LookAndFeel(String lookAndFeelName, String styleSheetPath){
			this.lookAndFeelName = new SimpleStringProperty(lookAndFeelName);
			this.styleSheetPath = new SimpleStringProperty(styleSheetPath);
		}
		
		public StringProperty getLookAndFeelNameProperty(){
			return this.lookAndFeelName;
		}
		
		public StringProperty getStyleSheetPathProperty(){
			return this.styleSheetPath;
		}
		
		public String getStyleSheetPath(){
			return this.styleSheetPath.get();
		}
		
		public String toString(){
			return this.lookAndFeelName.get();
		}
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
