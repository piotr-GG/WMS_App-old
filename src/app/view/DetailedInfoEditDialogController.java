package app.view;

import app.MainApp;
import app.model.Employee;
import app.model.Location;
import app.model.Permission;
import app.model.Product;
import app.model.Supplier;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Dialog used to show and edit data depending on type of argument sent by specified function
 * @author Piotrek
 *
 */

public class DetailedInfoEditDialogController {

	@FXML
	private GridPane gPane;
	
	private Stage ownerStage;
	private Stage currentStage;
	private MainApp mainApp;
	
	
	private Permission permission;
	private Supplier supplier;
	private Location location;
	private Product product;
	private Employee employee;
	
	private String classCurrentlyUsed;
	
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}
	
	public void setOwnerStage(Stage ownerStage){
		this.ownerStage = ownerStage;
	}
	
	public void setCurrentStage(Stage currentStage){
		this.currentStage = currentStage;
	}
	

	private void showPermission(){
		
	}
	private void showSupplier(){
		
	}
	
	private void showLocation(){
		
	}
	
	private void showProduct(){
		
	}
	
	private void showEmployee(){
		
	}
	
	public void setPermission(Permission permission){
		this.permission = permission;
		classCurrentlyUsed = permission.getClass().getSimpleName();
		
		showPermission();
	}
	
	public void setSupplier(Supplier supplier){
		this.supplier = supplier;
		classCurrentlyUsed = supplier.getClass().getSimpleName();
		
		showSupplier();
	}
	
	public void setLocation(Location location){
		this.location = location;
		classCurrentlyUsed = location.getClass().getSimpleName();
		
		showLocation();
	}
	
	public void setProduct(Product product){
		this.product = product;
		classCurrentlyUsed = product.getClass().getSimpleName();
		
		System.out.println(classCurrentlyUsed);
		
		showProduct();
	}
	
	public void setEmployee(Employee employee){
		this.employee = employee;
		classCurrentlyUsed = employee.getClass().getSimpleName();
		
		showEmployee();
	}
	
	@FXML
	private void handleSaveChangesClick(){
		
	}
	
	@FXML
	private void handleCancelClick(){
		
	}
}
