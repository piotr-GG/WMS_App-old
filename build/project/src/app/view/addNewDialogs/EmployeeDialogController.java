package app.view.addNewDialogs;

import app.model.Employee;
import app.view.inheritance.DialogBasedController;
import javafx.beans.binding.StringExpression;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
 
public class EmployeeDialogController extends DialogBasedController {

	@FXML
	private TextField nameTF;
	@FXML
	private TextField surnameTF;
	@FXML
	private ComboBox<String> postitionCBox;
	@FXML
	private TextField departmentTF;
	@FXML
	private DatePicker birthDateDP;
	@FXML
	private DatePicker hireDateDP;
	@FXML
	private TextField addressTF;
	@FXML
	private TextField cityTF;
	@FXML
	private TextField postalCodeTF;
	@FXML
	private TextField countryTF;
	@FXML
	private TextField phoneNumberTF;
	@FXML
	private TextField emailTF;
	@FXML
	private Label nameLabel;
	
	private Employee employee;
	
	public void setEmployee(Employee employee){
		this.employee = employee;
		if(employee != null) fillTheFields();
	}
	
	public void fillTheFields(){
		//Label
		StringExpression expression = (employee.getFirstNameProperty().concat(" ")).concat(employee.getLastNameProperty()); //Concatenates first name string property with "" string and then with last name string property
		nameLabel.textProperty().bind(expression);

		//ComboBox
		if(employee.getPosition().equals("Administrator")){
			//no posibility for administrator to change his position
			postitionCBox.getItems().add(employee.getPosition());
			postitionCBox.getSelectionModel().select("Administrator");
		} else {
			//no posibility for other users to become admin
			postitionCBox.getItems().addAll(employee.getPositionNotString().valuesExtended());
			postitionCBox.getItems().remove("Administrator");
			
			postitionCBox.getSelectionModel().select(employee.getExtendedPosition());
		}
		
		
		//Employee part
		
		nameTF.setText(employee.getFirstName());
		surnameTF.setText(employee.getLastName());
		departmentTF.setText(employee.getDepartment().getDepartmentName());
		birthDateDP.setValue(employee.getBirthDate());
		hireDateDP.setValue(employee.getHireDate());
		addressTF.setText(employee.getAddress());
		cityTF.setText(employee.getCity());
		postalCodeTF.setText(employee.getPostalCode());
		countryTF.setText(employee.getCountry());
		phoneNumberTF.setText(employee.getPhoneNumber());
		emailTF.setText(employee.getEmail());
	}
	
	@FXML
	private void handleOKClick(){
		getCurrentStage().close();
	}
}
