package app.view.addNewDialogs;

import app.model.Supplier;
import app.view.inheritance.DialogBasedController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SupplierDetailsDialogController extends DialogBasedController {
	
	@FXML
	private TextField supplierIDField;
	@FXML
	private TextField companyNameField;
	@FXML
	private TextField representativeField;
	@FXML
	private TextField addressField;
	@FXML
	private TextField cityField;
	@FXML
	private TextField postalCodeField;
	@FXML
	private TextField countryField;
	@FXML
	private TextField phoneNumberField;
	@FXML
	private TextArea otherArea;
	
	private Supplier supplier;
	
	public void setSupplier(Supplier supplier){
		if(supplier != null){
			this.supplier = supplier;
			fillTheFields();
		}
	}
	
	private void fillTheFields(){
		supplierIDField.setText(String.valueOf(supplier.getSupplierID()));
		companyNameField.setText(supplier.getCompanyName());
		representativeField.setText(supplier.getRepresentative());
		addressField.setText(supplier.getAddress());
		cityField.setText(supplier.getCity());
		postalCodeField.setText(supplier.getPostalCode());
		countryField.setText(supplier.getCountry());
		phoneNumberField.setText(supplier.getPhoneNumber());
		otherArea.setText(supplier.getOther());
	}
	
	@FXML
	private void handleOKClick(){
		getCurrentStage().close();
	}
}
