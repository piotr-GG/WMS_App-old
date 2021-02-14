package app.view.adminPanelDialogs;

import java.time.LocalDate;
import java.util.Optional;

import com.itextpdf.text.pdf.AcroFields.Item;

import app.model.Delivery;
import app.model.DeliveryDetails;
import app.utility.UtilityClass;
import app.view.inheritance.ActionController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class SimulationsOverviewController extends ActionController {

	@FXML
	private TableView<Delivery> deliveryTable;
	@FXML
	private TableColumn<Delivery, Integer> deliveryIDColumn;
	@FXML
	private TableColumn<Delivery, String> deliveryArrivalDateColumn;
	@FXML
	private TableColumn<Delivery, Integer> supplierIDColumn;
	@FXML
	private TableColumn<Delivery, String> supplierCompanyNameColumn;
	@FXML
	private TableColumn<Delivery, String> driverNameColumn;
	@FXML
	private TableColumn<Delivery, Integer> palletCountColumn;
	
	private ObservableList<Delivery> deliveryTableData;
	
	@FXML
	private TableView<DeliveryDetails> deliveryDetailsTable;
	@FXML
	private TableColumn<DeliveryDetails, Integer> productIDColumn;
	@FXML
	private TableColumn<DeliveryDetails, String> productNameColumn;
	@FXML
	private TableColumn<DeliveryDetails, Integer> deliveredQuantityColumn;
	@FXML
	private TableColumn<DeliveryDetails,String> palletCountColumn_DDT;
	
	private ObservableList<DeliveryDetails> deliveryDetailsTableData;
	
	@FXML
	private DatePicker arrivalDateDatePicker;
	@FXML
	private TextField driverNameTF;
	@FXML
	private TextField palletCountTF;
	
	@FXML
	private void exitClick(){
		currentStage.close();
	}
	
	@FXML
	private void addDataClick(){
		Delivery delivery = deliveryTable.getSelectionModel().getSelectedItem();
		if(delivery != null){
			LocalDate arrivalDate = arrivalDateDatePicker.getValue();
			if(arrivalDate != null){
				if(!arrivalDate.isBefore(LocalDate.now())){	
					if(arrivalDate.isBefore(delivery.getRequiredDeliveryDate())){
						Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Potwierdzenie", "Data przybycia", "Podana data przybycia jest wcze�niejsza ni� data wymagana. Czy mimo to kontynuowa�?", currentStage).showAndWait();
						if(result.get() != ButtonType.OK){
							return;
						}
					}
					if(driverNameTF.getText() != null && !driverNameTF.getText().isEmpty()) addProductProcedure(delivery, arrivalDate);
					else UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak danych kierowcy", "Prosz� wprowadzi� dane kierowcy", currentStage).showAndWait();
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna data", "Data dostawy nie mo�e by� wcze�niej ni� w dzisiejsza data!", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak daty przybycia", "Prosz� wprowadzi� dat� przybycia dostawy do magazynu", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak dostawy", "Prosz� wybra� odpowiedni� dostaw�", currentStage).showAndWait();
		}
	}
	
	private void addProductProcedure(Delivery delivery, LocalDate arrivalDate){
		String driverName = driverNameTF.getText();
		
		int palletCount = Integer.parseInt(palletCountTF.getText());
		if(palletCount > 0){
			if(hasEveryDeliveryDetailPalletCount(delivery.getDeliveryDetails())){
				delivery.setDeliveryArrivalDate(arrivalDate);
				delivery.setDeliveryStatus("Arrived");
				delivery.setDriverName(driverName);
				delivery.setPalletCount(palletCount);
				delivery.setDeliveryDetails(deliveryDetailsTableData);
				boolean successfullyUpdated = mainApp.getController().getDatabase().updateDeliveryRecord(delivery);
				if(successfullyUpdated) {
					UtilityClass.showAlert(AlertType.INFORMATION, "Komuniakt", "Pomy�lnie zaktualizowano rekord", "Zlecenie dostawy zosta�o pomy�lnie zaktualizowane!", currentStage).showAndWait();
					clearData();
					deliveryDetailsTableData.remove(delivery.getDeliveryDetails());
					deliveryTableData.remove(delivery);
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��d aktualizacji", "Podczas aktualizacji wyst�pi� nieoczekiwany b��d!", currentStage).showAndWait();
				}
			} else {
				UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak ilo�ci palet", "W co najmniej jednym z produkt�w nie ma przydzielonej liczby palet!", currentStage).showAndWait();
			}
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "B��d", "Brak ilo�ci palet", "Prosz� wprowadzi� odpowiedni� ilo�� palet", currentStage).showAndWait();
		}
	} 

	
	private boolean hasEveryDeliveryDetailPalletCount(ObservableList<DeliveryDetails> dd){
		for(DeliveryDetails del : dd){
			if(del.getPalletCount() == 0) return false;
		}
		return true;
	}
	
	private void clearData(){
		arrivalDateDatePicker.setValue(null);
		driverNameTF.setText("");
		palletCountTF.setText("");
	}

	public void getDeliveries(){
		initializeDeliveryTable();
		
		ObservableList<Delivery> result = mainApp.getController().getDatabase().getDeliveriesWithDeliveryStatus("Order_Is_Placed");
		if(result.isEmpty()) UtilityClass.showAlert(AlertType.WARNING, "Komunikat", "Brak zlece� dostawy", "W bazie danych nie znaleziono zlece� dostawy oczekuj�cych na realizacj� przez dostawc�", currentStage).showAndWait();
		else {
			deliveryTableData.addAll(result);
			deliveryTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Delivery>() {

				@Override
				public void changed(ObservableValue<? extends Delivery> observable, Delivery oldValue,
						Delivery newValue) {
					if(newValue != null) showDeliveryDetails(newValue.getDeliveryDetails());
					
				}
			});
		}
	}
	
	private void initializeDeliveryTable(){
		deliveryIDColumn.setCellValueFactory(cellData->cellData.getValue().getDeliveryIDProperty().asObject());
		deliveryArrivalDateColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getDeliveryArrivalDate().toString()));
		supplierIDColumn.setCellValueFactory(cellData->cellData.getValue().getSupplier().getSupplierIDProperty().asObject());
		supplierCompanyNameColumn.setCellValueFactory(cellData->cellData.getValue().getSupplier().getCompanyNameProperty());
		driverNameColumn.setCellValueFactory(cellData->cellData.getValue().getDriverNameProperty());
		palletCountColumn.setCellValueFactory(cellData->cellData.getValue().getPalletCountProperty().asObject());
		
		deliveryTableData = FXCollections.observableArrayList();
		deliveryTable.setItems(deliveryTableData);
		
		initializeDeliveryDetailsTable();
		
		deliveryTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Delivery>() {

			@Override
			public void changed(ObservableValue<? extends Delivery> observable, Delivery oldValue, Delivery newValue) {
				if(newValue != null) showDetailsAboutDelivery(newValue);
			}
		});
	}
	
	private void initializeDeliveryDetailsTable(){
		productIDColumn.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductIDProperty().asObject());
		productNameColumn.setCellValueFactory(cellData->cellData.getValue().getProduct().getProductNameProperty());
		deliveredQuantityColumn.setCellValueFactory(cellData->cellData.getValue().getQuantityProperty().asObject());;
		palletCountColumn_DDT.setCellValueFactory(cellData->new SimpleStringProperty(String.valueOf(cellData.getValue().getPalletCount())));
		
		palletCountColumn_DDT.setCellFactory(TextFieldTableCell.forTableColumn());
		palletCountColumn_DDT.setOnEditCommit(new EventHandler<CellEditEvent<DeliveryDetails, String>>(){
			@Override
			public void handle(CellEditEvent<DeliveryDetails, String> event) {
				boolean isParsable = UtilityClass.isParsable(event.getNewValue());
				if(isParsable){
					int oldPalletCount = Integer.parseInt(event.getOldValue());
					int palletCount = Integer.parseInt(event.getNewValue());
					if(palletCount > 0){
						event.getTableView().getItems().get(event.getTablePosition().getRow()).setPalletCount(palletCount);
					
						int countPallets = Integer.parseInt(palletCountTF.getText());
						countPallets = countPallets - oldPalletCount + palletCount;
						palletCountTF.setText(String.valueOf(countPallets));
						
						
					} else {
						UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Prosz� poda� liczb� ca�kowit� wi�ksz� od zera!", currentStage).showAndWait();
						event.getTableView().getItems().get(event.getTablePosition().getRow()).setPalletCount(0);
					}
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B��d", "B��dna warto��", "Prosz� poda� liczb� ca�kowit�", currentStage).showAndWait();
					event.getTableView().getItems().get(event.getTablePosition().getRow()).setPalletCount(0);
				}
				
				
			}
			
		});
		
		
		deliveryDetailsTableData = FXCollections.observableArrayList();
		deliveryDetailsTable.setItems(deliveryDetailsTableData);	
	}
	
	private void showDeliveryDetails(ObservableList<DeliveryDetails> dd){
		deliveryDetailsTableData.clear();
		deliveryDetailsTableData.addAll(dd);
	}
	
	private void showDetailsAboutDelivery(Delivery d){
		arrivalDateDatePicker.setValue(d.getDeliveryArrivalDate());
		driverNameTF.setText(d.getDriverName());;
		
		int countPallets = 0;
		ObservableList<DeliveryDetails> deliveryDetails = d.getDeliveryDetails();
		for(DeliveryDetails dd : deliveryDetails){
			countPallets += dd.getPalletCount();
		}
		
		
		palletCountTF.setText(String.valueOf(countPallets));
		
	}
	
	
}
