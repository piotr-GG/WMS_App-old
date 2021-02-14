package app.view.adminPanelDialogs;

import app.view.inheritance.DialogBasedController;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class StatisticsDialogController extends DialogBasedController {
	
	@FXML
	private TreeTableView<String> treeTableView = new TreeTableView<String>();
	
	public StatisticsDialogController(){
		
 
	}
	
	@FXML
	private void initialize(){
		final TreeItem<String> customersItem = new TreeItem<>("Klienci");
		
		final TreeItem<String> customersCountItem = new TreeItem<>("Iloœæ klientów: 5");
		final TreeItem<String> customersMostValuableItem = new TreeItem<>("Najwa¿niejszy klient: " + "PIGAL");
		customersItem.getChildren().setAll(customersCountItem, customersMostValuableItem);
		
		final TreeItem<String> employeesItem = new TreeItem<>("Pracownicy");
		final TreeItem<String> employeesCountItem = new TreeItem<>("Iloœæ pracowników: 10");
		final TreeItem<String> employeesCustomerServiceCount = new TreeItem<>("Iloœæ pracowników dzia³u obs³ugi klienta: 5");
		final TreeItem<String> employeesWarehouseCount = new TreeItem<>("Iloœc pracowników dzia³u magazynowego: 3");
		final TreeItem<String> employeesCommerceCount = new TreeItem<>("Iloœæ pracowników dzia³u handlowego: 2");
		employeesItem.getChildren().setAll(employeesCustomerServiceCount, employeesWarehouseCount, employeesCommerceCount);
		
		final TreeItem<String> suppliersItem = new TreeItem<>("Dostawcy");
		final TreeItem<String> suppliersCountItem = new TreeItem<>("Iloœæ dostawców: 7");
		final TreeItem<String> suppliersMostValueableItem = new TreeItem<>("Najwa¿niejszy dostawca: Hubert Sutowski");
		suppliersItem.getChildren().setAll(suppliersCountItem,suppliersMostValueableItem);
		
		final TreeItem<String> usersItem = new TreeItem<>("U¿ytkownicy");
		final TreeItem<String> usersCountItem  =  new TreeItem<>("Iloœæ u¿ytkowników: 10");
		usersItem.getChildren().setAll(usersCountItem);
		
		final TreeItem<String> totalOrderValueItem = new TreeItem<>("Ca³kowita wartoœæ zamówieñ");
		final TreeItem<String> totalOrderValue = new TreeItem<>("100 235 z³");
		totalOrderValueItem.getChildren().setAll(totalOrderValue);
		
		final TreeItem<String> shipmentsItem = new TreeItem<>("Wysy³ki");
		final TreeItem<String> shipmentsValueItem = new TreeItem<>("Iloœæ wysy³ek: 10");
		shipmentsItem.getChildren().setAll(shipmentsValueItem);
		
		final TreeItem<String> root = new TreeItem<>("Statystyki bazy danych");
		root.setExpanded(true);
		root.getChildren().setAll(customersItem, employeesItem, suppliersItem, usersItem, totalOrderValueItem, shipmentsItem);
		TreeTableColumn<String, String> column = new TreeTableColumn<>("Statystyki");
		
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) -> 
        new ReadOnlyStringWrapper(p.getValue().getValue()));  
        
       
        treeTableView.getColumns().add(column);
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(true);
	}
	
	
	@FXML
	private void handleReturnClick(){
		this.getCurrentStage().close();
	}
}
