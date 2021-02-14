package app.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.itextpdf.text.DocumentException;

import app.external.PDF;
import app.model.Delivery;
import app.model.Delivery.DeliveryStatus;
import app.model.Order;
import app.model.Shipment;
import app.model.Shipment.ShipmentStatus;
import app.utility.UtilityClass;
import app.view.inheritance.ActionController;
import app.view.inheritance.LanguageInterface;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

public class PDFGeneratorOverviewController extends ActionController implements LanguageInterface {
	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private RadioButton generateWayBillRB;
	@FXML
	private TextField generateWayBillField;
	@FXML
	private VBox generateWayBillVBox;
	@FXML
	private HBox generateWayBillOptionsVBox;
	@FXML
	private CheckBox mrnT1WayBillCheckBox;
	@FXML
	private CheckBox invoiceWayBillCheckBox;
	
	@FXML
	private RadioButton generateOrderInvoiceRB;
	@FXML
	private VBox generateOrderInvoiceVBox;
	@FXML
	private TextField generateOrderInvoiceField;
	
	@FXML
	private RadioButton generateDeliveryInvoiceRB;
	@FXML
	private VBox generateDeliveryInvoiceVBox;
	@FXML
	private TextField generateDeliveryInvoiceField;
	
	@FXML
	private RadioButton generateCMRRB;
	@FXML
	private VBox generateCMRVBox;
	@FXML
	private HBox generateCMROptionsVBox;
	@FXML
	private CheckBox mrnT1CMRCheckBox;
	@FXML
	private CheckBox invoiceCMRCheckBox;
	@FXML
	private TextField generateCMRField;
	
	@FXML
	private Button executeButton;
	
	private ToggleGroup radioButtonGroup;
	
	@FXML
	private void initialize(){
		radioButtonGroup = new ToggleGroup();
		executeButton.setOpacity(0.0);
		executeButton.setDisable(true);
		
		generateWayBillVBox.setOpacity(0.0);
		generateWayBillOptionsVBox.setOpacity(0.0);
		generateWayBillOptionsVBox.setDisable(true);
		generateWayBillVBox.setDisable(true);
		generateWayBillRB.setToggleGroup(radioButtonGroup);
		generateWayBillRB.selectedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldState, Boolean newState) {
				if(newState == true) {
					playTransitionAnimation(generateWayBillVBox, false);
					playTransitionAnimation(generateWayBillOptionsVBox, false);
				}
				else {
					playTransitionAnimation(generateWayBillVBox, true );
					playTransitionAnimation(generateWayBillOptionsVBox, true);
				}
			}
		});
		
		generateWayBillField.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(!UtilityClass.isParsable(arg2) && arg2.length() != 0){
					if(arg1.length() != 0) generateWayBillField.setText(arg1);
					else generateWayBillField.setText("");
				}
			}
		});
		
		generateOrderInvoiceField.textProperty().addListener(new ChangeListener<String>(){
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(!UtilityClass.isParsable(arg2) && arg2.length() != 0){
					if(arg1.length() != 0) generateOrderInvoiceField.setText(arg1);
					else generateOrderInvoiceField.setText("");
				}
		}});
		
		generateDeliveryInvoiceField.textProperty().addListener(new ChangeListener<String>(){
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(!UtilityClass.isParsable(arg2) && arg2.length() != 0){
					if(arg1.length() != 0) generateDeliveryInvoiceField.setText(arg1);
					else generateDeliveryInvoiceField.setText("");
				}
		}});
		
		generateOrderInvoiceRB.setToggleGroup(radioButtonGroup);
		generateOrderInvoiceVBox.setOpacity(0.0);
		generateOrderInvoiceVBox.setDisable(true);
		generateOrderInvoiceRB.selectedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldState, Boolean newState) {
				if(newState == true) playTransitionAnimation(generateOrderInvoiceVBox, false);
				else playTransitionAnimation(generateOrderInvoiceVBox, true);
			}
		});
		
		generateDeliveryInvoiceRB.setToggleGroup(radioButtonGroup);
		generateDeliveryInvoiceVBox.setOpacity(0.0);
		generateDeliveryInvoiceVBox.setDisable(true);
		generateDeliveryInvoiceRB.selectedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldState, Boolean newState) {
				if(newState == true) playTransitionAnimation(generateDeliveryInvoiceVBox, false);
				else playTransitionAnimation(generateDeliveryInvoiceVBox, true);
			}
		});
		
		generateCMRRB.setToggleGroup(radioButtonGroup);
		generateCMRVBox.setOpacity(0.0);
		generateCMRVBox.setDisable(true);
		generateCMROptionsVBox.setOpacity(0.0);
		generateCMROptionsVBox.setDisable(true);
		generateCMRRB.selectedProperty().addListener(new ChangeListener<Boolean>(){
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldState, Boolean newState) {
				if(newState == true) {
					playTransitionAnimation(generateCMRVBox, false);
					playTransitionAnimation(generateCMROptionsVBox, false);
				}
				else {
					playTransitionAnimation(generateCMRVBox, true);
					playTransitionAnimation(generateCMROptionsVBox, true);
				}
			}
		});
		
		radioButtonGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
				if(radioButtonGroup.getSelectedToggle() != null) parallelTransition(executeButton, false);
				else parallelTransition(executeButton, true);
			}
		});
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
	
	
	private void playTransitionAnimation(Node node, boolean isSetToFade){
		FadeTransition ft = new FadeTransition(Duration.millis(700), node);
		ft.setCycleCount(1);
		if(isSetToFade){
			ft.setFromValue(1.0);
			ft.setToValue(0.0);
			node.setDisable(true);
		} else {
			ft.setFromValue(0.0);
			ft.setToValue(1.0);
			node.setDisable(false);
		}
		ft.play();	
	}
	
	private void parallelTransition(Node node, boolean isSetToFade){
		 node.setRotate(0);
		 RotateTransition rotateTransition = 
		            new RotateTransition(Duration.millis(700), node);
		 rotateTransition.setByAngle(360f);
		 rotateTransition.setCycleCount(1);
		 
		 FadeTransition ft = new FadeTransition(Duration.millis(700), node);
		 ft.setCycleCount(1);
		 if(isSetToFade){
				ft.setFromValue(1.0);
				ft.setToValue(0.0);
				node.setDisable(true);
		 } else {
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				node.setDisable(false);
		}
		ParallelTransition parallelTransition = new ParallelTransition();
	    parallelTransition.getChildren().addAll(
	                ft, rotateTransition
	        );
	    parallelTransition.setCycleCount(1);
	    parallelTransition.play();	
	}
	
	@FXML
	private void handleExecuteClick(){
		if(generateWayBillRB.isSelected()) generateWayBill();
		if(generateOrderInvoiceRB.isSelected()) generateOrderInvoice();
		if(generateDeliveryInvoiceRB.isSelected()) generateDeliveryInvoice();
		if(generateCMRRB.isSelected()) generateCMR();
	}
	
	private void generateWayBill(){
		String shipmentFieldText = generateWayBillField.getText();
		if(shipmentFieldText.length() != 0){
			int shipmentID = Integer.parseInt(shipmentFieldText);
			Shipment shipment = mainApp.getController().getDatabase().getShipmentByID(shipmentID);
			if(shipment != null){
				if(shipment.getShipmentStatus().equals(ShipmentStatus.Awaits_Shipment)){
					File file = UtilityClass.chooseTheFile(new FileChooser.ExtensionFilter("dokument PDF", "*.pdf" ), currentStage);
					if(file != null){
						boolean successfullyCreated;
						try {
							boolean mrnT1Selected = mrnT1WayBillCheckBox.isSelected();
							boolean invoiceSelected = invoiceWayBillCheckBox.isSelected();
							successfullyCreated = PDF.createWayBillPDF(shipment, file, mrnT1Selected, invoiceSelected);
						} catch (DocumentException | IOException e ) {
							successfullyCreated = false;
							e.printStackTrace();
						}
						if(successfullyCreated) UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Wygenerowano list przewozowy", "List przewozowy zosta³ pomyœlnie wygenerowany!", currentStage).showAndWait();
						else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d", "Podczas generowania listu przewozowego wyst¹pi³ b³¹d!", currentStage).showAndWait();
						generateWayBillField.setText("");
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d statusu", "Nie mo¿na ju¿ wygenerowaæ listu przewozowego do wys³anej wysy³ki!", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wysy³ki", "Nie ma takiej wysy³ki w bazie danych!", currentStage).showAndWait();
			}

		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Proszê podaæ ID wysy³ki!", currentStage).showAndWait();
		}
	}
	
	private void generateOrderInvoice(){
		String generateOrderInvoiceText = generateOrderInvoiceField.getText();
		if(generateOrderInvoiceText.length() != 0){
			int orderID = Integer.parseInt(generateOrderInvoiceText);
			Order order = mainApp.getController().getDatabase().getOrderByID(orderID);
			if(order != null){
				File file = UtilityClass.chooseTheFile(new FileChooser.ExtensionFilter("dokument PDF", "*.pdf" ), currentStage);
				if(file != null){
					boolean successfullyCreated;
					try {
						successfullyCreated = PDF.createOrderInvoicePDF(order, file);
					} catch (DocumentException | IOException e ) {
						successfullyCreated = false;
						e.printStackTrace();
					}
					if(successfullyCreated) UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Wygenerowano fakturê", "Faktura zosta³a pomyœlnie wygenerowana!", currentStage).showAndWait();
					else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d", "Podczas generowania faktury wyst¹pi³ b³¹d!", currentStage).showAndWait();
					generateOrderInvoiceField.setText("");
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak zamówienia", "Nie ma takiego zamówienia w bazie danych!", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Proszê podaæ ID zamówienia!", currentStage).showAndWait();
		}
	}
	
	private void generateDeliveryInvoice(){
		String generateDeliveryInvoiceText = generateDeliveryInvoiceField.getText();
		if(generateDeliveryInvoiceText.length() != 0){
			int deliveryID = Integer.parseInt(generateDeliveryInvoiceText);
			Delivery delivery = mainApp.getController().getDatabase().getDeliveryByID(deliveryID);
			if(delivery != null){
				if(delivery.getDeliveryStatus().equals(DeliveryStatus.Arrived)){
				File file = UtilityClass.chooseTheFile(new FileChooser.ExtensionFilter("dokument PDF", "*.pdf" ), currentStage);
				if(file != null){
					boolean successfullyCreated;
					try {
						successfullyCreated = PDF.createDeliveryInvoicePDF(delivery, file);
					} catch (DocumentException | IOException e ) {
						successfullyCreated = false;
						e.printStackTrace();
					}
					if(successfullyCreated) UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Wygenerowano fakturê", "Faktura zosta³a pomyœlnie wygenerowana!", currentStage).showAndWait();
					else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d", "Podczas generowania faktury wyst¹pi³ b³¹d!", currentStage).showAndWait();
					generateDeliveryInvoiceField.setText("");
				}
			  } else {
				  UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³êdna dostawa", "Dostawa o podanym ID jeszcze nie przyby³a do magazynu!", currentStage).showAndWait();
			}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak dostawy", "Nie ma takiej dostawy w bazie danych!", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Proszê podaæ ID wysy³ki!", currentStage).showAndWait();
		}
	}
	
	private void generateCMR(){
		String shipmentFieldText = generateCMRField.getText();
		if(shipmentFieldText.length() != 0){
			int shipmentID = Integer.parseInt(shipmentFieldText);
			Shipment shipment = mainApp.getController().getDatabase().getShipmentByID(shipmentID);
			if(shipment != null){
				if(shipment.getShipmentStatus().equals(ShipmentStatus.Awaits_Shipment)){
					File file = UtilityClass.chooseTheFile(new FileChooser.ExtensionFilter("dokument PDF", "*.pdf" ), currentStage);
					if(file != null){
						boolean successfullyCreated;
						try {
							boolean mrnT1Selected = mrnT1CMRCheckBox.isSelected();
							boolean invoiceSelected = invoiceCMRCheckBox.isSelected();
							successfullyCreated = PDF.createCMRPDF(shipment, file, mrnT1Selected, invoiceSelected);
						} catch (DocumentException | IOException e ) {
							successfullyCreated = false;
							e.printStackTrace();
						}
						if(successfullyCreated) UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Wygenerowano dokument CMR", "List przewozowy zosta³ pomyœlnie wygenerowany!", currentStage).showAndWait();
						else UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d", "Podczas generowania dokumentu CMR wyst¹pi³ b³¹d!", currentStage).showAndWait();
						generateCMRField.setText("");
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "B³¹d statusu", "Nie mo¿na ju¿ wygenerowaæ dokumentu CMR do wys³anej wysy³ki!", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wysy³ki", "Nie ma takiej wysy³ki w bazie danych!", currentStage).showAndWait();
			}

		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Brak wartoœci", "Proszê podaæ ID wysy³ki!", currentStage).showAndWait();
		}
	}

	@Override
	public void updateDisplay() {
		
	}

	@Override
	public void initializeLanguageDisplay() {
		
		
	}
	

}
