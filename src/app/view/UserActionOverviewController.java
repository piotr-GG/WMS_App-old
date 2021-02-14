package app.view;

import java.awt.Button;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.ResourceBundle;

import app.MainApp;
import app.utility.UTF8Control;
import app.view.inheritance.LanguageInterface;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UserActionOverviewController implements LanguageInterface {

	
	@FXML
	private GridPane mainPane;
	private MainApp mainApp;
	@FXML
	private Label userPanelLabel;
	@FXML
	private Label userPanelTextLabel;
	@FXML
	private Label pdfGeneratorLabel;
	@FXML
	private Label productsLabel;
	@FXML
	private Label shippingLabel;
	@FXML
	private Label communicationLabel;
	@FXML
	private Label orderLabel;
	@FXML
	private Label whAttendantPanelLabel;
	@FXML
	private Label locationLabel;
	@FXML
	private Label deliveriesLabel;
	@FXML
	private Label chartsLabel;
	
	@FXML
	private Tooltip pdfGeneratorToolTip;
	@FXML
	private Tooltip communicationTooltip;
	@FXML
	private Tooltip locationTooltip;
	@FXML
	private Tooltip productsTooltip;
	@FXML
	private Tooltip orderTooltip;
	@FXML
	private Tooltip deliveryTooltip;
	@FXML
	private Tooltip shippingTooltip;
	@FXML
	private Tooltip whAttendantPanelTooltip;
	@FXML
	private Tooltip chartsTooltip;

	@FXML
	private ImageView pdfGeneratorIcon;
	@FXML
	private ImageView productsIcon;
	@FXML
	private ImageView shippingIcon;
	@FXML
	private ImageView communicationIcon;
	@FXML
	private ImageView ordersIcon;
	@FXML
	private ImageView whAttendantIcon;
	@FXML
	private ImageView locationIcon;
	@FXML
	private ImageView deliveriesIcon;
	@FXML
	private ImageView chartsIcon;
	
	private ObservableList<ImageView> iconsData;
	private ObservableList<Label> labelData;
	private boolean smallerImages = false;
	
    private ResourceBundle languagePack;
	
    private AnchorPane pdfGeneratorPane = null;
    private AnchorPane userInteractionPane = null;
    private AnchorPane drawChartsPane = null;
    private AnchorPane productsQueryPane = null;
    private AnchorPane ordersPane = null;
    private AnchorPane warehouseManagementPane = null;
    private AnchorPane deliveriesPane = null;
    private AnchorPane shippingPane = null;
    
	@FXML
	private void initialize(){
		UserActionOverviewController.hackTooltipStartTiming(pdfGeneratorToolTip);
		UserActionOverviewController.hackTooltipStartTiming(communicationTooltip);
		UserActionOverviewController.hackTooltipStartTiming(locationTooltip);
		UserActionOverviewController.hackTooltipStartTiming(productsTooltip);
		UserActionOverviewController.hackTooltipStartTiming(orderTooltip);
		UserActionOverviewController.hackTooltipStartTiming(deliveryTooltip);
		UserActionOverviewController.hackTooltipStartTiming(shippingTooltip);
		UserActionOverviewController.hackTooltipStartTiming(whAttendantPanelTooltip);
		UserActionOverviewController.hackTooltipStartTiming(chartsTooltip);
		
		iconsData = FXCollections.observableArrayList();
		iconsData.addAll(pdfGeneratorIcon, productsIcon, shippingIcon, communicationIcon, ordersIcon, whAttendantIcon, locationIcon, deliveriesIcon, chartsIcon);
		labelData = FXCollections.observableArrayList();
		labelData.addAll(pdfGeneratorLabel, productsLabel, shippingLabel, communicationLabel, orderLabel, whAttendantPanelLabel, locationLabel, deliveriesLabel, chartsLabel);
		
		//Listener that scales the ImageView instances and the Labels if the height and width of the frame is less that certain value
		mainPane.layoutBoundsProperty().addListener(new ChangeListener<Bounds>(){
			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				if(newValue.getWidth() < 1400 && newValue.getHeight() < 850 && smallerImages != true){
					iconsData.forEach((imageView) -> {
						imageView.setFitHeight(100);
						imageView.setFitWidth(100);
					});
					labelData.forEach((label) -> label.setFont(Font.font("System", FontWeight.BOLD, 12))); 
					smallerImages = true;
				} else if (smallerImages == true && newValue.getWidth() > 1300 && newValue.getHeight() > 850){
					iconsData.forEach((imageView) -> {
						imageView.setFitHeight(150);
						imageView.setFitWidth(150);
					});
					labelData.forEach((label) -> label.setFont(Font.font("System", FontWeight.BOLD, 16))); 
					smallerImages = false;
				}
			}
		});
		
		
	}
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;

		updateCSSStyles();
		mainApp.getController().getStyleSheetPathProperty().addListener(changeListener -> {
			updateCSSStyles();
		});
		

		
		
		
		mainApp.getRootLayout().requestLayout();
	}

	private void updateCSSStyles(){
		mainPane.getStylesheets().clear();
		mainPane.getStylesheets().add(mainApp.getController().getStyleSheetPath());
	}
	
	/**
	 * Shows new window with query options
	 */
	
	public void showPDFGeneratorOverview(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PDFGeneratorOverview.fxml"));
			pdfGeneratorPane = (AnchorPane) loader.load();
			
			PDFGeneratorOverviewController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setCurrentStage(mainApp.getPrimaryStage());
			controller.setPreviousPane(mainApp.getRootLayout().getCenter());
			controller.initializeData();
			mainApp.getRootLayout().setCenter(pdfGeneratorPane);
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}

	
	/**
	 * Shows new window dedicated for communication between users
	 */
	
	public void showUserInteractionOverview(){
		try{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/UserInteractionOverview.fxml"));
		userInteractionPane = (AnchorPane) loader.load();
		
		UserInteractionController controller = loader.getController();
		controller.setMainApp(mainApp);
		controller.setCurrentStage(mainApp.getPrimaryStage());
		controller.setPreviousPane(mainApp.getRootLayout().getCenter());
		controller.initializeData();
		controller.getMessages();
		mainApp.getRootLayout().setCenter(userInteractionPane);
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows new window with drawing chart options
	 */
	
	public void showDrawChartsOverview(){
		try{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/DrawChartsOverview.fxml"));
		drawChartsPane = (AnchorPane) loader.load();
		
		DrawChartsOverviewController controller = loader.getController();
		controller.setMainApp(mainApp);
		controller.setCurrentStage(mainApp.getPrimaryStage());
		controller.setPreviousPane(mainApp.getRootLayout().getCenter());
		controller.initializeData();
		mainApp.getRootLayout().setCenter(drawChartsPane);
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * Shows product query window
	 */
	public void showProductsQueryOverview(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ProductsQueryOverview.fxml"));
			productsQueryPane = (AnchorPane) loader.load();
			
	        ProductsQueryOverviewController controller = loader.getController();
	        
			controller.setMainApp(mainApp);
			controller.setCurrentStage(mainApp.getPrimaryStage());
			controller.setPreviousPane(mainApp.getRootLayout().getCenter());
			controller.initializeData();
			mainApp.getRootLayout().setCenter(productsQueryPane);
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * Shows window with order informations
	 */
	public void showOrdersOverview(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/OrdersOverview.fxml"));
			ordersPane = (AnchorPane) loader.load();
			
	        OrdersOverviewController controller = loader.getController();
	        controller.setMainApp(mainApp);
			controller.setCurrentStage(mainApp.getPrimaryStage());
			controller.setPreviousPane(mainApp.getRootLayout().getCenter());
			controller.initializeData();
			mainApp.getRootLayout().setCenter(ordersPane);
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows window with warehouse management options
	 */
	public void showWarehouseManagementOverview(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/WarehouseManagementOverview.fxml"));
			warehouseManagementPane = (AnchorPane) loader.load();
			
	        WarehouseManagementOverviewController controller = loader.getController();
	        controller.setMainApp(mainApp);
			controller.setCurrentStage(mainApp.getPrimaryStage());
			controller.setPreviousPane(mainApp.getRootLayout().getCenter());
			controller.initializeData();
			mainApp.getRootLayout().setCenter(warehouseManagementPane);		
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows new frame for warehouse attendant actions
	 */
	private void showWarehouseAttendantPanel(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/WarehouseAttendantPanel.fxml"));
			AnchorPane attendantPanel = (AnchorPane) loader.load();
			
			Scene scene = new Scene(attendantPanel);
			Stage stage = new Stage();
			stage.setScene(scene);
	
			stage.initOwner(mainApp.getPrimaryStage());
	        stage.setTitle("ForkLift WMS Warehouse Management System");
	        stage.getIcons().add(new Image("file:src/app/view/images/mini icons/icon.png"));
			
			WarehouseAttendantPanelController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setOwnerStage(mainApp.getPrimaryStage());
			controller.setCurrentStage(stage);
			controller.initializeData();
			
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();	
			
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void showShippingOverview(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ShippingOverview.fxml"));
			shippingPane = (AnchorPane) loader.load();

			ShippingOverviewController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setCurrentStage(mainApp.getPrimaryStage());
			controller.setPreviousPane(mainApp.getRootLayout().getCenter());
			controller.initializeData();
			mainApp.getRootLayout().setCenter(shippingPane);		
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void showDeliveryOverview(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/DeliveryOverview.fxml"));
			deliveriesPane = (AnchorPane) loader.load();
			
			DeliveryOverviewController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setCurrentStage(mainApp.getPrimaryStage());
			controller.setPreviousPane(mainApp.getRootLayout().getCenter());
			controller.initializeData();
			mainApp.getRootLayout().setCenter(deliveriesPane);
		} 
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void updateDisplay(){
		languagePack = ResourceBundle.getBundle("app.view.language.UserActionOverview", mainApp.getController().getCurrentLocale(), new UTF8Control());
		userPanelLabel.setText(languagePack.getString("userPanel"));
		userPanelTextLabel.setText(languagePack.getString("userPanelText"));
		pdfGeneratorLabel.setText(languagePack.getString("pdfGenerator"));
		productsLabel.setText(languagePack.getString("products"));
		shippingLabel.setText(languagePack.getString("shipping"));
		communicationLabel.setText(languagePack.getString("communication"));
		orderLabel.setText(languagePack.getString("order"));
		whAttendantPanelLabel.setText(languagePack.getString("whAttendantPanel"));
		locationLabel.setText(languagePack.getString("location"));
		deliveriesLabel.setText(languagePack.getString("deliveries"));
		chartsLabel.setText(languagePack.getString("charts"));
	}
	
	/**
	 * Sets custom delay for every tooltip 
	 * Method created by Igor Luzhanov , copied from http://stackoverflow.com/questions/26854301/control-javafx-tooltip-delay
	 * @param tooltip
	 */
	
	public static void hackTooltipStartTiming(Tooltip tooltip) {
	    try {
	        Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
	        fieldBehavior.setAccessible(true);
	        Object objBehavior = fieldBehavior.get(tooltip);

	        Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
	        fieldTimer.setAccessible(true);
	        Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

	        objTimer.getKeyFrames().clear();
	        objTimer.getKeyFrames().add(new KeyFrame(new Duration(250)));
	    } catch (Exception e) {
	        e.printStackTrace();
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
	
	/*
	 * Event Handlers
	 */
	@FXML
	private void handlePDFGeneratorClick(){
		showPDFGeneratorOverview();
	}
	
	@FXML
	private void handleUserInteractionClick(){
		showUserInteractionOverview();
	}
	
	@FXML
	private void handleDrawChartsClick(){
		showDrawChartsOverview();
	}
	
	@FXML
	private void handleProductQueriesClick(){
		showProductsQueryOverview();
	}
	
	@FXML
	private void handleOrdersClick(){
		showOrdersOverview();
	}
	
	@FXML
	private void handleWarehouseManagementClick(){
		showWarehouseManagementOverview();
	}
	
	@FXML
	private void handleWarehouseAttendantClick(){
		showWarehouseAttendantPanel();
	}
	
	@FXML
	private void handleShippingClick(){
		showShippingOverview();
	}
	
	@FXML
	private void handleDeliveryClick(){
		showDeliveryOverview();
	}

}
