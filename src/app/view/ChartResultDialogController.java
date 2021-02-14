package app.view;



import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import com.itextpdf.text.DocumentException;

import app.MainApp;
import app.external.JXLS;
import app.external.PDF;
import app.model.Location;
import app.model.Product;
import app.utility.UtilityClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChartResultDialogController {

	private MainApp mainApp;
	
	private XYChart.Series<String, Number> mostValuableCustomersData;
	private ObservableList<PieChart.Data> demandData;
	private ObservableList<XYChart.Series<String, Number>> demandDataCommon;
	private XYChart.Series<String, Number> incomeData;
	private XYChart.Series<String, Number> deliveryValuesData;
	private ObservableList<PieChart.Data> categoryIncomesData;
	private ObservableList<PieChart.Data> quantityDistributionData;
	
	private Location location;
	
	
	private XYChart.Series<Integer, Number> linearSmoothingData;
	

	
	private Stage dialogStage;
	
	@FXML
	private GridPane detailsGPane;
	@FXML
	private AnchorPane pane;
	@FXML
	private Label label;
	@FXML
	private Label pieValueLabel;
	
	@FXML
	private Label productLabel;
	@FXML
	private Label valueLabel;
	@FXML
	private Button exportPDF;
	@FXML
	private Button exportExcel;
	
	public static final int DEMAND_DATA_QUANTITY = 1;
	public static final int DEMAND_DATA_INCOME = 2;
	public static final int DEMAND_DATA_COMMON = 3;
	public static final int INCOMES_IN_TIME = 4;
	public static final int MOST_VALUABLE_CUSTOMERS = 5;
	public static final int SHIPMENTS_OVER_TIME = 6;
	public static final int DELIVERY_VALUES_OVER_TIME = 7;
	public static final int CATEGORY_INCOMES = 8;
	public static final int LINEAR_SMOOTHING = 9;
	public static final int QUANTITY_DISTRIBUTION = 10;
	
	/**
	 * chartShown variable holds the info about the type of chart that is being shown on the screen. It is used to differentiate the charts from each other in some methods
	 */
	private int chartShown;
	
	/**
	 * Sets the value of chartShown variable 
	 * @param chartShown
	 */
	public void setChartShown(int chartShown){
		this.chartShown = chartShown;
	}
	/**
	 * Sets the value of mainApp variable
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}
	
	public void setDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
		
		//This stage should not be resizable
		dialogStage.setResizable(false);
	}
	
	@FXML
	private void initialize(){

	}
	
	/**
	 * Handles action for "Return" button
	 */
	@FXML
	private void handleReturnClick(){
		this.dialogStage.close();
	}
	
	/**
	 * Draws sale incomes chart in the stage 
	 */
	public void showMostValuableCustomersChart(int customerCount){
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String, Number> chart = new BarChart<String, Number>(xAxis, yAxis);
		chart.setTitle("Wykres wp³ywów ze sprzeda¿y");
		xAxis.setLabel("Firmy");
		yAxis.setLabel("Wp³ywy [z³]");
		

		mostValuableCustomersData = mainApp.getController().getDatabase().getSaleIncomesData(customerCount);
		
		if(!mostValuableCustomersData.getData().isEmpty()){
			XYChart.Series<String, Number> series1 = mostValuableCustomersData;
			series1.setName("Wp³ywy");
			
			for(XYChart.Data<String, Number> data : series1.getData()){
				addNewDetailedInfo(data.getXValue(), String.valueOf(data.getYValue()));
			}
			
			chart.getData().add(series1);
			chart.prefHeightProperty().bind(pane.heightProperty());
			chart.prefWidthProperty().bind(pane.widthProperty());
			pane.getChildren().add(chart);
		}
	}
	
	/**
	 * Draws product demand chart depending on 'type' variable 
	 * @param type
	 * @param productQuantity
	 */
	
	@SuppressWarnings("unchecked")
	public void showProductDemandChart(int type, int productQuantity){
		
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		String chartTitle = "";
		if(type == 0){
			//quantity criterion 
		
			demandData = mainApp.getController().getDatabase().getDemandDataQuantity(productQuantity);
			valueLabel.setText(valueLabel.getText().concat(" [szt]"));
			chartTitle = "Wykres popytu - kryterium iloœciowe";

		} else if (type == 1){
			//income criterion
			demandData = mainApp.getController().getDatabase().getDemandDataSaleIncomes(productQuantity);
			valueLabel.setText(valueLabel.getText().concat(" [z³]"));
			chartTitle = "Wykres popytu - kryterium wp³ywów ze sprzeda¿y ";

		} else if (type ==2){
			
			label.setText("UWAGA. Podane dane s¹ okreœlone w wartoœciach procentowych!");
			
			final CategoryAxis xAxis = new CategoryAxis();
			xAxis.setTickLabelFont(javafx.scene.text.Font.font("Arial", FontWeight.MEDIUM, 11));
			final NumberAxis yAxis = new NumberAxis();
			final BarChart<String, Number> chart = new BarChart<String, Number>(xAxis, yAxis);
			chart.setTitle("Wykres wspólny popytu");
			xAxis.setLabel("Produkty");
			yAxis.setLabel("Popyt [%]");
			
			ObservableList<XYChart.Series<String, Number>> result = mainApp.getController().getDatabase().getDemandDataCommon(productQuantity);
			if(!result.isEmpty()){
				demandDataCommon = result;
				XYChart.Series<String, Number> series1 = result.get(0);
				series1.setName("Iloœæ");
				valueLabel.setText("Iloœæ | Przychód [%]");
				
				XYChart.Series<String, Number> series2 = result.get(1);
				series2.setName("Przychód");
				
				for(int i = 0; i < series1.getData().size(); i++){
					addNewDetailedInfo(series1.getData().get(i).getXValue(), String.valueOf(series1.getData().get(i).getYValue()) + " | " + String.valueOf(series2.getData().get(i).getYValue()));
				}
			
				chart.getData().addAll(series1, series2);
				chart.prefHeightProperty().bind(pane.heightProperty());
				chart.prefWidthProperty().bind(pane.widthProperty());
				pane.getChildren().add(chart);

			
			return;
			}
		}
		else {
			UtilityClass.showAlert(AlertType.ERROR, "B³¹d", null, "Wyst¹pi³ b³¹d podczas tworzenia wykresu: Nieprawid³owa wartoœæ?", dialogStage).showAndWait();
		}
	
		for(PieChart.Data data : demandData){
			addNewDetailedInfo(data.getName(), String.valueOf(data.getPieValue()));
		}
		pieChartData.addAll(demandData);

		final PieChart chart = new PieChart(pieChartData);
		chart.setTitle(chartTitle);
		

		
		
		chart.prefHeightProperty().bind(pane.heightProperty());
		chart.prefWidthProperty().bind(pane.widthProperty());
		pane.getChildren().add(chart);

		
	}
	
	/**
	 * Shows line chart with income over time data
	 */
	public void showIncomesOverTime(String timeSpanString){

		final NumberAxis xAxis = new NumberAxis();
		final CategoryAxis yAxis = new CategoryAxis();
		xAxis.setLabel("Przychody [z³]");
		final LineChart<String, Number> lineChart = new LineChart<String, Number>(yAxis,xAxis);
		

		LocalDate timeSpan = computeTimeSpan(timeSpanString);
		XYChart.Series<String, Number> series = mainApp.getController().getDatabase().getIncomesOverTimeData(timeSpan.toString());
		
		XYChart.Series<String, Number> list = getInitializedListOfDates(timeSpanString);
		
		if(!series.getData().isEmpty()){
			
			for(XYChart.Data<String, Number> data : list.getData()){
				for(XYChart.Data<String, Number> result : series.getData()){
					if(data.getXValue().equals(result.getXValue())){
						data.setYValue(result.getYValue());
					}
				}
			}
			
			list.setName("Przychody");
			incomeData = series;
			for(XYChart.Data<String,Number> data : series.getData()){
				addNewDetailedInfo(data.getXValue(), String.valueOf(data.getYValue()));
			}
		
			lineChart.getData().add(list);
			lineChart.setTitle("Wykres przychodów w czasie");
			lineChart.prefHeightProperty().bind(pane.heightProperty());
			lineChart.prefWidthProperty().bind(pane.widthProperty());
			pane.getChildren().add(lineChart);
		}
	}
	
	public void showDeliveryValuesOverTime(String timeSpanString){
		final NumberAxis xAxis = new NumberAxis();
		final CategoryAxis yAxis = new CategoryAxis();
		xAxis.setLabel("Wartoœæ [z³]");
		final LineChart<String, Number> lineChart = new LineChart<String, Number>(yAxis,xAxis);
		
		LocalDate timeSpan = computeTimeSpan(timeSpanString);
		XYChart.Series<String, Number> series = mainApp.getController().getDatabase().getDeliveryValuesOverTime(timeSpan.toString());
		
		XYChart.Series<String, Number> list = getInitializedListOfDates(timeSpanString);
		
		if(!series.getData().isEmpty()){
			
			for(XYChart.Data<String, Number> data : list.getData()){
				for(XYChart.Data<String, Number> result : series.getData()){
					if(data.getXValue().equals(result.getXValue())){
						data.setYValue(result.getYValue());
					}
				}
			}
			
			list.setName("Wartoœci dostaw");
			deliveryValuesData = series;
			for(XYChart.Data<String,Number> data : series.getData()){
				addNewDetailedInfo(data.getXValue(), String.valueOf(data.getYValue()));
			}
		
			lineChart.getData().add(list);
			lineChart.setTitle("Wykres wartoœci dostaw w czasie");
			lineChart.prefHeightProperty().bind(pane.heightProperty());
			lineChart.prefWidthProperty().bind(pane.widthProperty());
			pane.getChildren().add(lineChart);
		}
	}
	
	public void showCategoryIncomes(){
		ObservableList<PieChart.Data> result = mainApp.getController().getDatabase().getCategoryIncomesData();
		if(!result.isEmpty()){
			categoryIncomesData = result;
			final PieChart chart = new PieChart(categoryIncomesData);
			
			for(PieChart.Data data : categoryIncomesData){
				addNewDetailedInfo(data.getName(), String.valueOf(data.getPieValue()));
			}
			String chartTitle = "Wykres wp³ywów ze sprzeda¿y wg kategorii produktów";
			chart.setTitle(chartTitle);

			chart.prefHeightProperty().bind(pane.heightProperty());
			chart.prefWidthProperty().bind(pane.widthProperty());
			pane.getChildren().add(chart);
		}
	}
	
	private LocalDate computeTimeSpan(String timeSpan){
		switch(timeSpan){
			case "7 dni": return LocalDate.now().minusDays(7);
			case "1 miesi¹c": return LocalDate.now().minusDays(30);
			case "3 miesi¹ce" : return LocalDate.now().minusDays(90);
			case "6 miesiêcy" : return LocalDate.now().minusDays(180);
			case "1 rok" : return LocalDate.now().minusDays(365);
			default: return LocalDate.now();
		}
	}
	
	private XYChart.Series<String, Number> getInitializedListOfDates(String timeSpan){
		int dayNumber = 0;
		switch(timeSpan){
			case "7 dni":{
				dayNumber = 7;
				break;
			}
			case "1 miesi¹c":{
				dayNumber = 30;
				break;
			}
			case "3 miesi¹ce" :{
				dayNumber = 90;
				break;
			}
			case "6 miesiêcy" :{
				dayNumber = 180;
				break;
			}
			case "1 rok" :{
				dayNumber = 365;
				break;
			}

	     	}

		XYChart.Series<String, Number> data = new XYChart.Series<>();
		for(int i = dayNumber; i >= 0 ; i--){
			data.getData().add(new XYChart.Data<String, Number>(LocalDate.now().minusDays(i).toString(), 0));
		}
		return data;
	}
	
	
	public void showQuantityDistributionChart(Location location){
		this.location = location;
		ObservableList<Product> productsStored = location.getProductsStored();
		
		int sum = 0;
		if(productsStored != null){
			quantityDistributionData = FXCollections.observableArrayList();
			for(Product p : productsStored){
				PieChart.Data newData = new PieChart.Data(p.getProductName(), p.getUnitsInStock());
				quantityDistributionData.add(newData);
				
				addNewDetailedInfo(newData.getName(), String.valueOf(newData.getPieValue()));
				sum += p.getUnitsInStock();
			}

			final PieChart chart = new PieChart(quantityDistributionData);
			
			
			
			chart.setTitle("Rozk³ad iloœciowy w lokacji");

			chart.prefHeightProperty().bind(pane.heightProperty());
			chart.prefWidthProperty().bind(pane.widthProperty());
			pane.getChildren().add(chart);
			
			final int sum2 = sum;
			
			for(final PieChart.Data newData : chart.getData()){
				
				newData.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
					@Override
					public void handle(MouseEvent event) {
						int value = (int) (newData.getPieValue()/sum2 * 100);
						pieValueLabel.setText(String.valueOf(value) + "%");	
					}
				});
				
				newData.getNode().addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
					public void handle(MouseEvent event){
						pieValueLabel.setText("");
					}
				});
			}
			
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Pusta lokacja", "Lokacja jest pusta", "Lokacja o podanym ID jest pusta!", dialogStage).showAndWait();
		}
	} 
	
	
	public void showLinearSmoothingChart() {
		XYChart.Series<Integer, Number> result = mainApp.getController().getDatabase().getLinearSmoothingData();
	
		if(!result.getData().isEmpty()){
			
			linearSmoothingData = new XYChart.Series<Integer, Number>();
			int resultSize = result.getData().size();
			int maxWeek = result.getData().get(resultSize -1).getXValue();
			int minWeek = result.getData().get(0).getXValue();
			System.out.println(minWeek);
			System.out.println(maxWeek);
			
			for(int i = minWeek; i <= maxWeek; i++){
				linearSmoothingData.getData().add(new XYChart.Data<Integer, Number>(i, 0));
			}
			
			for(XYChart.Data<Integer, Number> data : result.getData()){
				for(int i = 0; i < linearSmoothingData.getData().size(); i++){
					if(linearSmoothingData.getData().get(i).getXValue() == data.getXValue()){
						linearSmoothingData.getData().get(i).setYValue(data.getYValue());
					}
				}
				
			}
			
			try {
				JXLS.linearSmoothing(linearSmoothingData);
			} catch (IOException e) {
				e.printStackTrace();
			}
				
	
		}
		
	}
	/**
	 * Adds new row to GridPane object which shows 'detailed' information about every product shown in the charts
	 * @param name
	 * @param value
	 */
	
	public void addNewDetailedInfo(String name, String value){
		
		Label nameLabel = new Label(name);
		GridPane.setRowIndex(nameLabel, detailsGPane.getRowConstraints().size());
		GridPane.setColumnIndex(nameLabel, 0);
		nameLabel.getStyleClass().add("detailsGPaneLabel");
		
		Label valueLabel = new Label(value);
		GridPane.setRowIndex(valueLabel, detailsGPane.getRowConstraints().size());
		GridPane.setColumnIndex(valueLabel, 1);

		detailsGPane.add(nameLabel, 0, detailsGPane.impl_getRowCount() );
		detailsGPane.add(valueLabel, 1, detailsGPane.impl_getRowCount() - 1);

	}
	
	/**
	 * Exports data into PDF file 
	 */
	@FXML
	private void handleExportPDF(){
		
		File file = chooseTheFile(new FileChooser.ExtensionFilter("dokument PDF", "*.pdf" ));
		if(file != null){
			try {
				boolean executedSuccessfully = false;
				
				//invoke a method depending on the chart type being shown
				switch(chartShown){
				case DEMAND_DATA_QUANTITY :{
					//demand data - quantity
					executedSuccessfully = PDF.createDemandDataQuantityPDF(demandData, file);
					break;
				}
				case DEMAND_DATA_INCOME: {
					//demand data - income
					executedSuccessfully = PDF.createDemandDataIncomePDF(demandData, file);
					break;
				}
				case DEMAND_DATA_COMMON:{
					//demand data - common
					executedSuccessfully = PDF.createDemandDataCommonPDF(demandDataCommon, file);
					break;
				}
				case INCOMES_IN_TIME:{
					executedSuccessfully = PDF.createIncomesInTimePDF(incomeData, file);
					break;
				}
				case MOST_VALUABLE_CUSTOMERS:{
					executedSuccessfully = PDF.createMostValuableCustomersPDF(mostValuableCustomersData, file);
					break;
				}
				case DELIVERY_VALUES_OVER_TIME:{
					executedSuccessfully = PDF.createDeliveryValuesOverTimePDF(deliveryValuesData, file);
					break;
				}
				case CATEGORY_INCOMES:{
					executedSuccessfully = PDF.createCategoryIncomesPDF(categoryIncomesData, file);
					break;
				}
				case QUANTITY_DISTRIBUTION:{
					executedSuccessfully = PDF.createQuantityDistributionPDF(quantityDistributionData, file, location.getLocationID());
					break;
				}
				
				}
				if(executedSuccessfully){
					//show an alert informing of success
					UtilityClass.showAlert(AlertType.INFORMATION, "Eksportuj dane", null, "Pomyœlnie wyeksportowano dane do arkusza PDF!", dialogStage).showAndWait();
				} else {
					//show an alert informing of failure
					UtilityClass.showAlert(AlertType.ERROR, "Eksportuj dane", null, "Wyst¹pi³ b³¹d podczas eksportowania danych do arkusza PDF!", dialogStage).showAndWait();
				}
				
			} catch (IOException e) 
			{
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d strumienia wejœcia - wyjœcia", e.getMessage(), dialogStage).showAndWait();
				
			} catch (DocumentException e) 
			{
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Podczas tworzenia dokumentu wyst¹pi³ b³¹d", e.getMessage(), dialogStage).showAndWait();
			}
			
		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Brak pliku", "Brak wybranego pliku", "Nie wybrano miejsca do zapisania pliku!", dialogStage).showAndWait();
		}
	}
	
	/**
	 * Exports data into Excel file
	 */
	@FXML
	private void handleExportExcel(){
		//choose the file
		File file = chooseTheFile(new FileChooser.ExtensionFilter("arkusz Excel 2007 - 2010", "*.xlsx" ));
		
		if(file != null){
			try {
				boolean executedSuccessfully = false;
				
				//invoke a method depending on the chart type being shown
				switch(chartShown){
				case DEMAND_DATA_QUANTITY :{
					//demand data - quantity
					executedSuccessfully = JXLS.createDemandDataQuantityXLSX(demandData, file);
					break;
				}
				case DEMAND_DATA_INCOME: {
					//demand data - income
					executedSuccessfully = JXLS.createDemandDataIncomeXLSX(demandData, file);
					break;
				}
				case DEMAND_DATA_COMMON:{
					//demand data - common
					executedSuccessfully = JXLS.createDemandDataCommonXLSX(demandDataCommon, file);
					break;
				}
				case INCOMES_IN_TIME:{
					executedSuccessfully = JXLS.createIncomesInTimeXLSX(incomeData, file);
					break;
				}
				case MOST_VALUABLE_CUSTOMERS:{
					executedSuccessfully = JXLS.createMostValuableCustomersXLSX(mostValuableCustomersData, file);
					break;
				}
				case DELIVERY_VALUES_OVER_TIME:{
					executedSuccessfully = JXLS.createDeliveryValuesOverTimeXLSX(deliveryValuesData, file);
					break;
				} 
				case CATEGORY_INCOMES:{
					executedSuccessfully = JXLS.createCategoryIncomesXLSX(categoryIncomesData, file);
					break;
				}
				case QUANTITY_DISTRIBUTION:{
					executedSuccessfully = JXLS.createQuantityDistributionXLSX(quantityDistributionData, file, location.getLocationID());
					break;
				}
				
				}
				if(executedSuccessfully){
					//show an alert informing of success
					UtilityClass.showAlert(AlertType.INFORMATION, "Eksportuj dane", null, "Pomyœlnie wyeksportowano dane do arkusza Excela!", dialogStage).showAndWait();
				} else {
					//show an alert informing of failure
					UtilityClass.showAlert(AlertType.ERROR, "Eksportuj dane", null, "Wyst¹pi³ b³¹d podczas eksportowania danych do arkusza Excela!", dialogStage).showAndWait();
				}
				
			} 
			catch (IOException e) {
				UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d strumienia wejœcia - wyjœcia", e.getMessage(), dialogStage).showAndWait();
			}

		} else {
			UtilityClass.showAlert(AlertType.ERROR, "Brak pliku", "Brak wybranego pliku", "Nie wybrano miejsca do zapisania pliku!", dialogStage).showAndWait();
		}
		
	}
	
	/**
	 * Shows a filechooser window with extension property being added by the parameter
	 * @param ext
	 * @return
	 */
	private File chooseTheFile(FileChooser.ExtensionFilter ext){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Eksportuj dane");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(
			ext
		);
		File file = fileChooser.showSaveDialog(dialogStage);
		return file;
	}
	
	

}
