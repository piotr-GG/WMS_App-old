package app.external;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class JXLS {

	public static boolean createDemandDataQuantityXLSX(ObservableList<PieChart.Data> demandData, File newFile) throws IOException{
		

		File templateFile = new File("excel//DemandDataQuantityTemplate.xlsx");
		FileInputStream fis = new FileInputStream(templateFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		
		int counter = 1;
		for(PieChart.Data dd : demandData){
			data.put(String.valueOf(counter), new Object[] {dd.getName(), dd.getPieValue()});
			counter++;
		}
			
		Set<String> newRows = data.keySet();
		
		int rownum = mySheet.getLastRowNum();
		for(String key : newRows){
			Row row = mySheet.createRow(++rownum);
			Object [] objArr = data.get(key);
			int cellnum = 0;
			for(Object obj : objArr){
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
				{ 
					cell.setCellValue((String) obj); 
					}

				else if (obj instanceof Double) 
				{ 
					cell.setCellValue((Double) obj); 
					}
			}
			}

		int lastRow = mySheet.getLastRowNum();
		Row row = mySheet.createRow(lastRow + 4);
		Cell generatedTextCell = row.createCell(0);
		

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  d MMMM yyyy");
		generatedTextCell.setCellValue("Plik wygenerowany przez program ForkLift WMS w dniu : " + sdf.format(new GregorianCalendar().getTime()));

	
		FileOutputStream os = new FileOutputStream(newFile);
		myWorkBook.write(os);
		
		fis.close();
		os.close();
		myWorkBook.close();
		
		return true;
	}
	
	public static boolean createDemandDataIncomeXLSX(ObservableList<PieChart.Data> demandData, File newFile) throws IOException{
		

		File templateFile = new File("excel//DemandDataIncomeTemplate.xlsx");
		FileInputStream fis = new FileInputStream(templateFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		
		int counter = 1;
		for(PieChart.Data dd : demandData){
			data.put(String.valueOf(counter), new Object[] {dd.getName(), dd.getPieValue()});
			counter++;
		}
		Set<String> newRows = data.keySet();
		
		int rownum = mySheet.getLastRowNum();
		for(String key : newRows){
			Row row = mySheet.createRow(++rownum);
			Object [] objArr = data.get(key);
			int cellnum = 0;
			for(Object obj : objArr){
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
				{ 
					cell.setCellValue((String) obj); 
					}

				else if (obj instanceof Double) 
				{ 
					cell.setCellValue((Double) obj); 
					}
			}
			}

		int lastRow = mySheet.getLastRowNum();
		Row row = mySheet.createRow(lastRow + 4);
		Cell generatedTextCell = row.createCell(0);
		

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  d MMMM yyyy");
		generatedTextCell.setCellValue("Plik wygenerowany przez program ForkLift WMS w dniu : " + sdf.format(new GregorianCalendar().getTime()));

	
		FileOutputStream os = new FileOutputStream(newFile);
		myWorkBook.write(os);
		
		fis.close();
		os.close();
		myWorkBook.close();
		
		return true;
	}
	
	public static boolean createDemandDataCommonXLSX(ObservableList<XYChart.Series<String, Number>> demandData, File newFile) throws IOException{
		

		File templateFile = new File("excel//DemandDataCommonTemplate.xlsx");
		FileInputStream fis = new FileInputStream(templateFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		
		XYChart.Series<String, Number>  series1 = demandData.get(0);
		XYChart.Series<String, Number>  series2 = demandData.get(1);
		
		int counter = 1;
		for(int i = 0; i < series1.getData().size(); i++){
			data.put(String.valueOf(counter), new Object[] {series1.getData().get(i).getXValue(), series1.getData().get(i).getYValue(), series2.getData().get(i).getYValue()});
			counter++;
		}

		Set<String> newRows = data.keySet();
		
		int rownum = mySheet.getLastRowNum();
		for(String key : newRows){
			Row row = mySheet.createRow(++rownum);
			Object [] objArr = data.get(key);
			int cellnum = 0;
			for(Object obj : objArr){
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
				{ 
					cell.setCellValue((String) obj); 
					}

				else if (obj instanceof Long) 
				{ 
					cell.setCellValue((long) obj); 
					}
			}
			}

		int lastRow = mySheet.getLastRowNum();
		Row row = mySheet.createRow(lastRow + 4);
		Cell generatedTextCell = row.createCell(0);
		

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  d MMMM yyyy");
		generatedTextCell.setCellValue("Plik wygenerowany przez program ForkLift WMS w dniu : " + sdf.format(new GregorianCalendar().getTime()));

	
		FileOutputStream os = new FileOutputStream(newFile);
		myWorkBook.write(os);
		
		fis.close();
		os.close();
		myWorkBook.close();
		
		return true;
	}
	
	
	public static boolean createIncomesInTimeXLSX(XYChart.Series<String, Number> incomesData, File newFile) throws IOException{
		

		File templateFile = new File("excel//IncomesOTTemplate.xlsx");
		FileInputStream fis = new FileInputStream(templateFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		
		int counter = 1;
		for(XYChart.Data<String, Number> d : incomesData.getData()){
			data.put(String.valueOf(counter), new Object[] {d.getXValue(), d.getYValue()});
			counter++;
		}
		Set<String> newRows = data.keySet();
		
		int rownum = mySheet.getLastRowNum();
		for(String key : newRows){
			Row row = mySheet.createRow(++rownum);
			Object [] objArr = data.get(key);
			int cellnum = 0;
			for(Object obj : objArr){
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
				{ 
					cell.setCellValue((String) obj); 
					}

				else if (obj instanceof Long) 
				{ 
					cell.setCellValue((Long) obj); 
					}
			}
			}

		int lastRow = mySheet.getLastRowNum();
		Row row = mySheet.createRow(lastRow + 4);
		Cell generatedTextCell = row.createCell(0);
		

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  d MMMM yyyy");
		generatedTextCell.setCellValue("Plik wygenerowany przez program ForkLift WMS w dniu : " + sdf.format(new GregorianCalendar().getTime()));

	
		FileOutputStream os = new FileOutputStream(newFile);
		myWorkBook.write(os);
		
		fis.close();
		os.close();
		myWorkBook.close();
		
		return true;
	}
	
	public static boolean createMostValuableCustomersXLSX(XYChart.Series<String, Number> mostValuableCustomersData, File newFile) throws IOException{
		
		File templateFile = new File("excel//MostValCustomersTemplate.xlsx");
		FileInputStream fis = new FileInputStream(templateFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		
		int counter = 1;
		for(XYChart.Data<String, Number> d : mostValuableCustomersData.getData()){
			data.put(String.valueOf(counter), new Object[] {d.getXValue(), d.getYValue()});
			counter++;
		}
		Set<String> newRows = data.keySet();
		
		int rownum = mySheet.getLastRowNum();
		for(String key : newRows){
			Row row = mySheet.createRow(++rownum);
			Object [] objArr = data.get(key);
			int cellnum = 0;
			for(Object obj : objArr){
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
				{ 
					cell.setCellValue((String) obj); 
					}

				else if (obj instanceof Long) 
				{ 
					cell.setCellValue((Long) obj);
					}
			}
			}

		int lastRow = mySheet.getLastRowNum();
		Row row = mySheet.createRow(lastRow + 4);
		Cell generatedTextCell = row.createCell(0);
		

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  d MMMM yyyy");
		generatedTextCell.setCellValue("Plik wygenerowany przez program ForkLift WMS w dniu : " + sdf.format(new GregorianCalendar().getTime()));

	
		FileOutputStream os = new FileOutputStream(newFile);
		myWorkBook.write(os);
		
		fis.close();
		os.close();
		myWorkBook.close();
		
		return true;
	}
	
	public static boolean createDeliveryValuesOverTimeXLSX(XYChart.Series<String, Number> deliveryValuesData, File newFile) throws IOException {
		File templateFile = new File("excel//DeliveryValuesOTTemplate.xlsx");
		FileInputStream fis = new FileInputStream(templateFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		
		int counter = 1;
		for(XYChart.Data<String, Number> d : deliveryValuesData.getData()){
			data.put(String.valueOf(counter), new Object[] {d.getXValue(), d.getYValue()});
			counter++;
		}
		Set<String> newRows = data.keySet();
		
		int rownum = mySheet.getLastRowNum();
		for(String key : newRows){
			Row row = mySheet.createRow(++rownum);
			Object [] objArr = data.get(key);
			int cellnum = 0;
			for(Object obj : objArr){
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
				{ 
					cell.setCellValue((String) obj); 
					}

				else if (obj instanceof Long) 
				{ 
					cell.setCellValue((Long) obj);
					}
			}
			}

		int lastRow = mySheet.getLastRowNum();
		Row row = mySheet.createRow(lastRow + 4);
		Cell generatedTextCell = row.createCell(0);
		

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  d MMMM yyyy");
		generatedTextCell.setCellValue("Plik wygenerowany przez program ForkLift WMS w dniu : " + sdf.format(new GregorianCalendar().getTime()));

	
		FileOutputStream os = new FileOutputStream(newFile);
		myWorkBook.write(os);
		
		fis.close();
		os.close();
		myWorkBook.close();
		
		return true;
	}
	
	public static boolean createCategoryIncomesXLSX(ObservableList<PieChart.Data> categoryIncomesData, File newFile) throws IOException {
		File templateFile = new File("excel//CategoryIncomesTemplate.xlsx");
		FileInputStream fis = new FileInputStream(templateFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		
		int counter = 1;
		for(PieChart.Data d : categoryIncomesData){
			data.put(String.valueOf(counter), new Object[] {d.getName(), d.getPieValue()});
			counter++;
		}
		Set<String> newRows = data.keySet();
		
		int rownum = mySheet.getLastRowNum();
		for(String key : newRows){
			Row row = mySheet.createRow(++rownum);
			Object [] objArr = data.get(key);
			int cellnum = 0;
			for(Object obj : objArr){
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
				{ 
					cell.setCellValue((String) obj); 
					}

				else if (obj instanceof Double) 
				{ 
					cell.setCellValue((Double) obj);
					}
			}
			}

		int lastRow = mySheet.getLastRowNum();
		Row row = mySheet.createRow(lastRow + 4);
		Cell generatedTextCell = row.createCell(0);
		

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  d MMMM yyyy");
		generatedTextCell.setCellValue("Plik wygenerowany przez program ForkLift WMS w dniu : " + sdf.format(new GregorianCalendar().getTime()));

	
		FileOutputStream os = new FileOutputStream(newFile);
		myWorkBook.write(os);
		
		fis.close();
		os.close();
		myWorkBook.close();
		
		return true;
	}
	
	public static boolean createQuantityDistributionXLSX(ObservableList<PieChart.Data> quantityDistributionData, File newFile, String locationID) throws IOException {
		File templateFile = new File("excel//QuantityDistributionTemplate.xlsx");
		FileInputStream fis = new FileInputStream(templateFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		
		int counter = 1;
		for(PieChart.Data d : quantityDistributionData){
			data.put(String.valueOf(counter), new Object[] {d.getName(), d.getPieValue()});
			counter++;
		}
		Set<String> newRows = data.keySet();
		
		Row row = mySheet.createRow(0);
		Cell textCell = row.createCell(0);
		textCell.setCellValue("Dane dot. rozk³adu iloœciowego w lokacji " + locationID);
		CellStyle cs = myWorkBook.createCellStyle();
		
		Font font = myWorkBook.createFont();
		font.setFontHeightInPoints((short)14);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		cs.setFont(font);
		    
		textCell.setCellStyle(cs);
		
		int rownum = 1;
		for(String key : newRows){
			row = mySheet.createRow(rownum++);
			Object [] objArr = data.get(key);
			int cellnum = 0;
			for(Object obj : objArr){
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
				{ 
					cell.setCellValue((String) obj); 
					}

				else if (obj instanceof Double) 
				{ 
					cell.setCellValue((Double) obj);
					}
				else if (obj instanceof Long){
					cell.setCellValue((Long) obj);
				}
			}
			}

		int lastRow = mySheet.getLastRowNum();
		row = mySheet.createRow(lastRow + 4);
		Cell generatedTextCell = row.createCell(0);
		

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  d MMMM yyyy");
		generatedTextCell.setCellValue("Plik wygenerowany przez program ForkLift WMS w dniu : " + sdf.format(new GregorianCalendar().getTime()));

	
		FileOutputStream os = new FileOutputStream(newFile);
		myWorkBook.write(os);
		
		fis.close();
		os.close();
		myWorkBook.close();
		
		return true;
	}
	
	public static boolean linearSmoothing(XYChart.Series<Integer, Number> linearSmoothingData) throws IOException{
		File templateFile = new File("excel//Linear smoothing.xlsx");
		FileInputStream fis = new FileInputStream(templateFile);
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		
		int counter = 1;
		for(XYChart.Data<Integer, Number> d : linearSmoothingData.getData()){
			data.put(String.valueOf(counter), new Object[] {d.getXValue(), d.getYValue()});
			counter++;
		}
		Set<String> newRows = data.keySet();
		
		int rownum = mySheet.getLastRowNum();
		for(String key : newRows){
			Row row = mySheet.createRow(rownum++);
			Object [] objArr = data.get(key);
			int cellnum = 0;
			for(Object obj : objArr){
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof Integer)
				{ 
					cell.setCellValue((Integer) obj); 
					}

				else if (obj instanceof Double) 
				{ 
					cell.setCellValue((Double) obj);
					}
			}
			}

		
		
		

		int size = linearSmoothingData.getData().size() + 2;
		
		mySheet.setArrayFormula("LINEST(B3:B" + size+ ",A3:A" + size  + ")", CellRangeAddress.valueOf("C1:D1"));

		File outputFile = new File("excel//calc.xlsx");
		FileOutputStream os = new FileOutputStream(outputFile);
		
		myWorkBook.write(os);
		
		mySheet.getRow(1).getCell(2).setCellFormula("VALUE(C1)");
		mySheet.getRow(1).getCell(3).setCellFormula("VALUE(D1)");
		
		os = new FileOutputStream(outputFile);
		myWorkBook.write(os);
		
		
		fis.close();
		os.close();
		myWorkBook.close();

		return true;
	}
	

	

}
