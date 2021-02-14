package app.view;

import java.io.IOException;

import app.MainApp;
import app.utility.UtilityClass;
import app.view.inheritance.ActionController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DrawChartsOverviewController extends ActionController {

	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private RadioButton demandChartRB;
	@FXML
	private ComboBox<String> demandCriterionBox;
	@FXML
	private ComboBox<String> productQuantityBox;
	
	
	@FXML
	private RadioButton incomesOverTimeRB;
	@FXML
	private ComboBox<String> timeSpanBox;
	
	@FXML
	private RadioButton mostValuableCustomersRB;
	@FXML
	private ComboBox<String> customerCountBox;
	
	@FXML
	private RadioButton deliveryValuesRB;
	@FXML
	private ComboBox<String> timeSpanDeliveryBox;
	
	@FXML
	private RadioButton categoryIncomesRB;
	
	@FXML
	private RadioButton linearSmoothingRB;
	@FXML
	private ComboBox<String> linearSmoothingTimeSpanBox;
	
	private ToggleGroup radioButtonsGroup;

	@FXML
	private void initialize(){
		
		radioButtonsGroup = new ToggleGroup();
		
		demandCriterionBox.getItems().addAll("wg iloœci", "wg przychodów", "podsumowanie");
		demandCriterionBox.getSelectionModel().select(0);
		

		timeSpanBox.getItems().addAll("7 dni", "1 miesi¹c", "3 miesi¹ce", "6 miesiêcy", "1 rok");
		timeSpanBox.getSelectionModel().select(1);
		
		productQuantityBox.getItems().addAll("5", "10", "15", "20");
		productQuantityBox.getSelectionModel().select(1);
		
		demandChartRB.setToggleGroup(radioButtonsGroup);
		demandChartRB.setUserData("ProductDemand");
		
		mostValuableCustomersRB.setToggleGroup(radioButtonsGroup);
		mostValuableCustomersRB.setUserData("MostValuableCustomers");
		customerCountBox.getItems().addAll("5", "10", "15", "20");
		customerCountBox.getSelectionModel().selectFirst();
		
		incomesOverTimeRB.setToggleGroup(radioButtonsGroup);
		incomesOverTimeRB.setUserData("IncomesOverTime");
		
		deliveryValuesRB.setToggleGroup(radioButtonsGroup);
		deliveryValuesRB.setUserData("DeliveryValuesOverTime");
		
		timeSpanDeliveryBox.getItems().addAll("7 dni", "1 miesi¹c", "3 miesi¹ce", "6 miesiêcy", "1 rok");
		timeSpanDeliveryBox.getSelectionModel().select(1);
		
		categoryIncomesRB.setToggleGroup(radioButtonsGroup);
		categoryIncomesRB.setUserData("CategoryIncomes");
		
		linearSmoothingRB.setToggleGroup(radioButtonsGroup);
		linearSmoothingRB.setUserData("LinearSmoothing");
		
		linearSmoothingTimeSpanBox.getItems().addAll("7 dni", "1 miesi¹c", "3 miesi¹ce", "6 miesiêcy", "1 rok");
		linearSmoothingTimeSpanBox.getSelectionModel().select(1);
	}
	
	public void initializeData(){
		updateCSSStyles();
		mainApp.getController().getStyleSheetPathProperty().addListener(changeListener -> {
			updateCSSStyles();
		});
	}
	
	private void updateCSSStyles(){
		mainPane.getStylesheets().clear();
		mainPane.getStylesheets().add(mainApp.getController().getStyleSheetPath());
	}
	
	
	/**
	 * Invokes the showChartDialogOverview and sends the 'command' string associated with selected radio button
	 */
	@FXML
	private void handleExecuteClick(){
		Toggle selectedToggle = radioButtonsGroup.getSelectedToggle();
		if(selectedToggle != null){
			String userData = selectedToggle.getUserData().toString();
			showChartDialogOverview(userData);
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zaznaczenia", "Proszê zaznaczyæ odpowiedni rodzaj wykresu!", currentStage).showAndWait();
		}
	}
	

	/**
	 * Loads new dialog stage and invokes a method depending on the 'command' string
	 * @param chartOption
	 */
	private void showChartDialogOverview(String chartOption){
		try{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/ChartResultDialog.fxml"));
		TabPane chartDialog = (TabPane) loader.load();
		
		Stage stage = new Stage();
		
        stage.setTitle("Wykres");
        stage.getIcons().add(new Image("file:src/app/view/images/mini icons/icon.png"));
        
        
        ChartResultDialogController controller = loader.getController();
        controller.setMainApp(mainApp);
        controller.setDialogStage(stage);
        
        switch(chartOption){
        	case "MostValuableCustomers": {
        		int customerCount = Integer.parseInt(customerCountBox.getSelectionModel().getSelectedItem());
        		controller.showMostValuableCustomersChart(customerCount);
        		controller.setChartShown(ChartResultDialogController.MOST_VALUABLE_CUSTOMERS);
        		break;
        		}
        	case "ProductDemand": {
        		controller.showProductDemandChart(demandCriterionBox.getSelectionModel().getSelectedIndex(), Integer.parseInt(productQuantityBox.getSelectionModel().getSelectedItem().toString()));
        		controller.setChartShown(demandCriterionBox.getSelectionModel().getSelectedIndex() + 1);
        		break;
        	}
        	case "IncomesOverTime" : {
        		String timeSpan = timeSpanBox.getSelectionModel().getSelectedItem();
        		controller.showIncomesOverTime(timeSpan);
        		controller.setChartShown(ChartResultDialogController.INCOMES_IN_TIME);
        		break;
        	}
        	case "DeliveryValuesOverTime" :{
        		String timeSpan = timeSpanDeliveryBox.getSelectionModel().getSelectedItem();
        		controller.showDeliveryValuesOverTime(timeSpan);
        		controller.setChartShown(ChartResultDialogController.DELIVERY_VALUES_OVER_TIME);
        		break;
        	}
        	case "CategoryIncomes" : {
        		controller.showCategoryIncomes();
        		controller.setChartShown(ChartResultDialogController.CATEGORY_INCOMES);
        		break;
        	}
        	case "LinearSmoothing":{
        		controller.showLinearSmoothingChart();
        		controller.setChartShown(ChartResultDialogController.LINEAR_SMOOTHING);
        		break;
        	}
        	default:{
        		UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d", "W programie wyst¹pi³ b³¹d, który nie pozwala na kontynuowanie, przepraszamy!", currentStage).showAndWait();
        		return;
        	}
        
        
        }

		Scene scene = new Scene(chartDialog);
		stage.setScene(scene);
		stage.show();
		
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	 
}
