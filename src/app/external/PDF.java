package app.external;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import app.model.Customer;
import app.model.Delivery;
import app.model.DeliveryDetails;
import app.model.EnterpriseData;
import app.model.Order;
import app.model.OrderDetails;
import app.model.Shipment;
import app.model.ShipmentDetails;
import app.model.Supplier;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class PDF {

	private static Document createNewPDF(File newFile) throws FileNotFoundException, DocumentException{
		Document document = new Document();
		//Meta data
		document.addTitle("My PDF document");
		document.addSubject("Using iText");
		document.addKeywords("Java, PDF, iText");
		document.addAuthor("Piotr Ga�ecki");
		document.addCreator("Piotr Ga�ecki");
		
		PdfWriter.getInstance(document, new FileOutputStream(newFile));
		return document;
	}
	
	public static boolean createDemandDataQuantityPDF(ObservableList<PieChart.Data> demandData, File newFile) throws IOException, DocumentException{
		
		Document document = createNewPDF(newFile);
		document.open();
		

		BaseFont baseFont;
		baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);

		Font biggerFont = new Font(baseFont, 18);
		Font paragraphFont = new Font(baseFont, 12, Font.NORMAL);
		Font courierFont = new Font(footerFont, 8, Font.BOLD);
		
		Paragraph preface = new Paragraph();

		preface.add(new Paragraph("\nDane dot. popytu - kryterium ilo�ciowe\n", biggerFont));
		
		document.add(preface);

		
		Paragraph content = new Paragraph();
		content.add(new Chunk("\nPoni�ej przedstawione zosta�y w postaci tabeli dane dotycz�ce popytu wed�ug kryterium ilo�ci sprzedanych produkt�w.\n\n\n", paragraphFont));
		content.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(content);
		
		PdfPTable table = new PdfPTable(2);
		
		
		PdfPCell productHeader = new PdfPCell(new Phrase("Produkt", paragraphFont));
		productHeader.setBorder(Rectangle.BOTTOM);
		productHeader.setPaddingBottom(5.0F);
		
		PdfPCell valueHeader = new PdfPCell(new Phrase("Sprzedana ilo�� [szt]", paragraphFont));
		valueHeader.setBorder(Rectangle.BOTTOM);
		valueHeader.setPaddingBottom(5.0F);
		
		
		table.addCell(productHeader);
		table.addCell(valueHeader);
		
		table.setHeaderRows(1);
		table.getDefaultCell().setBorder(Rectangle.BOTTOM);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		for(PieChart.Data dd: demandData){
			PdfPCell cell1 = new PdfPCell(new Phrase(dd.getName(), paragraphFont));
			PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(dd.getPieValue()), paragraphFont));
			
			cell1.setBorder(Rectangle.NO_BORDER);
			cell2.setBorder(Rectangle.NO_BORDER);
			
			table.addCell(cell1);
			table.addCell(cell2);
		}
		
		document.add(table);
		document.add(new Chunk("\n\n\n"));
		Paragraph footer = new Paragraph("Dokument wygenerowany przez program ForkLift WMS", courierFont);
		
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		
		document.add(footer);
		document.close();
		
		return true;
	}

	public static boolean createDemandDataIncomePDF(ObservableList<PieChart.Data> demandData, File newFile) throws IOException, DocumentException{
		
		Document document = createNewPDF(newFile);
		document.open();
		

		BaseFont baseFont;
		baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);

		Font biggerFont = new Font(baseFont, 18);
		Font paragraphFont = new Font(baseFont, 12, Font.NORMAL);
		Font courierFont = new Font(footerFont, 8, Font.BOLD);
		
		Paragraph preface = new Paragraph();

		preface.add(new Paragraph("\nDane dot. popytu - kryterium wp�yw�w ze sprzeda�y\n", biggerFont));
		
		document.add(preface);

		
		Paragraph content = new Paragraph();
		content.add(new Chunk("\nPoni�ej przedstawione zosta�y w postaci tabeli dane dotycz�ce popytu wed�ug kryterium wp�yw�w ze sprzeda�y.\n\n\n", paragraphFont));
		content.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(content);
		
		PdfPTable table = new PdfPTable(2);
		
		
		PdfPCell productHeader = new PdfPCell(new Phrase("Produkt", paragraphFont));
		productHeader.setBorder(Rectangle.BOTTOM);
		productHeader.setPaddingBottom(5.0F);
		
		PdfPCell valueHeader = new PdfPCell(new Phrase("Przych�d [z�]", paragraphFont));
		valueHeader.setBorder(Rectangle.BOTTOM);
		valueHeader.setPaddingBottom(5.0F);
		
		
		table.addCell(productHeader);
		table.addCell(valueHeader);
		
		table.setHeaderRows(1);
		table.getDefaultCell().setBorder(Rectangle.BOTTOM);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		for(PieChart.Data dd: demandData){
			PdfPCell cell1 = new PdfPCell(new Phrase(dd.getName(), paragraphFont));
			PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(dd.getPieValue()), paragraphFont));
			
			cell1.setBorder(Rectangle.NO_BORDER);
			cell2.setBorder(Rectangle.NO_BORDER);
			
			table.addCell(cell1);
			table.addCell(cell2);
		}
		
		document.add(table);
		document.add(new Chunk("\n\n\n"));
		Paragraph footer = new Paragraph("Dokument wygenerowany przez program ForkLift WMS", courierFont);
		
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		
		document.add(footer);
		document.close();
		
		return true;
	}	
		
	public static boolean createDemandDataCommonPDF(ObservableList<XYChart.Series<String, Number>> demandData, File newFile) throws IOException, DocumentException{
		
		Document document = createNewPDF(newFile);
		document.open();
		

		BaseFont baseFont;
		baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);

		Font biggerFont = new Font(baseFont, 18);
		Font paragraphFont = new Font(baseFont, 12, Font.NORMAL);
		Font courierFont = new Font(footerFont, 8, Font.BOLD);
		
		Paragraph preface = new Paragraph();

		preface.add(new Paragraph("\nDane dot. popytu - warto�ci wsp�lne\n", biggerFont));
		
		document.add(preface);

		
		Paragraph content = new Paragraph();
		content.add(new Chunk("\nPoni�ej przedstawione zosta�y w postaci tabeli dane dotycz�ce popytu kryterium ilo�ciowego oraz wp�yw�w ze sprzeda�y. Nale�y zauwa�y�, �e warto�ci zosta�y podane w warto�ciach procentowych ca�kowitego udzia�u w ilo�ci sprzedanych produkt�w lub ca�kowitych wp�yw�w ze sprzeda�y.\n\n\n", paragraphFont));
		content.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(content);
		
		PdfPTable table = new PdfPTable(3);
		
		PdfPCell quantityHeader = new PdfPCell(new Phrase("Ilo�� [%]", paragraphFont));
		quantityHeader.setBorder(Rectangle.BOTTOM);
		quantityHeader.setPaddingBottom(5.0F);
		
		PdfPCell productHeader = new PdfPCell(new Phrase("Produkt", paragraphFont));
		productHeader.setBorder(Rectangle.BOTTOM);
		productHeader.setPaddingBottom(5.0F);
		
		PdfPCell valueHeader = new PdfPCell(new Phrase("Przychody [%]", paragraphFont));
		valueHeader.setBorder(Rectangle.BOTTOM);
		valueHeader.setPaddingBottom(5.0F);
		
		
		table.addCell(productHeader);
		table.addCell(quantityHeader);
		table.addCell(valueHeader);
		
		table.setHeaderRows(1);

		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		
		XYChart.Series<String, Number>  series1 = demandData.get(0);
		XYChart.Series<String, Number>  series2 = demandData.get(1);
		
		for(int i = 0; i < series1.getData().size(); i++){
			PdfPCell cell1 = new PdfPCell(new Phrase(series1.getData().get(i).getXValue(), paragraphFont));
			PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(series1.getData().get(i).getYValue()), paragraphFont));
			PdfPCell cell3 = new PdfPCell(new Phrase(String.valueOf(series2.getData().get(i).getYValue()), paragraphFont));
			
			cell1.setBorder(Rectangle.NO_BORDER);
			cell2.setBorder(Rectangle.NO_BORDER);
			cell3.setBorder(Rectangle.NO_BORDER);
			
			table.addCell(cell1);
			table.addCell(cell2);
			table.addCell(cell3);
		}
		
		document.add(table);
		document.add(new Chunk("\n\n\n"));
		Paragraph footer = new Paragraph("Dokument wygenerowany przez program ForkLift WMS", courierFont);
		
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		
		document.add(footer);
		document.close();
		
		return true;
	}	
		
	
	public static boolean createIncomesInTimePDF(XYChart.Series<String, Number> incomesData, File newFile) throws IOException, DocumentException{
		
		Document document = createNewPDF(newFile);
		document.open();
		

		BaseFont baseFont;
		baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);

		Font biggerFont = new Font(baseFont, 18);
		Font paragraphFont = new Font(baseFont, 12, Font.NORMAL);
		Font courierFont = new Font(footerFont, 8, Font.BOLD);
		
		Paragraph preface = new Paragraph();

		preface.add(new Paragraph("\nDane dot. przychod�w w dziedzinie czasu\n", biggerFont));
		
		document.add(preface);

		
		Paragraph content = new Paragraph();
		content.add(new Chunk("\nPoni�ej przedstawione zosta�y w postaci tabeli dane dotycz�ce przychod�w w dziedzinie czasu.\n\n\n", paragraphFont));
		content.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(content);
		
		PdfPTable table = new PdfPTable(2);
		
		
		PdfPCell productHeader = new PdfPCell(new Phrase("Data", paragraphFont));
		productHeader.setBorder(Rectangle.BOTTOM);
		productHeader.setPaddingBottom(5.0F);
		
		PdfPCell valueHeader = new PdfPCell(new Phrase("Przych�d [szt]", paragraphFont));
		valueHeader.setBorder(Rectangle.BOTTOM);
		valueHeader.setPaddingBottom(5.0F);
		
		
		table.addCell(productHeader);
		table.addCell(valueHeader);
		
		table.setHeaderRows(1);
		table.getDefaultCell().setBorder(Rectangle.BOTTOM);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		for(XYChart.Data<String, Number> data : incomesData.getData()){
			PdfPCell cell1 = new PdfPCell(new Phrase(data.getXValue(), paragraphFont));
			PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(data.getYValue()), paragraphFont));
			
			cell1.setBorder(Rectangle.NO_BORDER);
			cell2.setBorder(Rectangle.NO_BORDER);
			
			table.addCell(cell1);
			table.addCell(cell2);
		}
		
		document.add(table);
		document.add(new Chunk("\n\n\n"));
		Paragraph footer = new Paragraph("Dokument wygenerowany przez program ForkLift WMS", courierFont);
		
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		
		document.add(footer);
		document.close();
		
		return true;
	}	
		
	public static boolean createMostValuableCustomersPDF(XYChart.Series<String, Number> mostValuableCustomersData, File newFile) throws IOException, DocumentException{
		
		Document document = createNewPDF(newFile);
		document.open();
		

		BaseFont baseFont;
		baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);

		Font biggerFont = new Font(baseFont, 18);
		Font paragraphFont = new Font(baseFont, 12, Font.NORMAL);
		Font courierFont = new Font(footerFont, 8, Font.BOLD);
		
		Paragraph preface = new Paragraph();

		preface.add(new Paragraph("\nDane dot. klient�w z najwi�ksz� warto�ci� zam�wie�\n", biggerFont));
		
		document.add(preface);

		
		Paragraph content = new Paragraph();
		content.add(new Chunk("\nPoni�ej przedstawione zosta�y w postaci tabeli dane dot. klient�w z najwi�ksz� warto�ci� zam�wie�\n\n\n", paragraphFont));
		content.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(content);
		
		PdfPTable table = new PdfPTable(2);
		
		
		PdfPCell productHeader = new PdfPCell(new Phrase("Nazwa firmy klienta", paragraphFont));
		productHeader.setBorder(Rectangle.BOTTOM);
		productHeader.setPaddingBottom(5.0F);
		
		PdfPCell valueHeader = new PdfPCell(new Phrase("Warto�� zam�wie� [z�]", paragraphFont));
		valueHeader.setBorder(Rectangle.BOTTOM);
		valueHeader.setPaddingBottom(5.0F);
		
		
		table.addCell(productHeader);
		table.addCell(valueHeader);
		
		table.setHeaderRows(1);
		table.getDefaultCell().setBorder(Rectangle.BOTTOM);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		for(XYChart.Data<String, Number> data: mostValuableCustomersData.getData()){
			PdfPCell cell1 = new PdfPCell(new Phrase(data.getXValue(), paragraphFont));
			PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(data.getYValue()), paragraphFont));
			
			cell1.setBorder(Rectangle.NO_BORDER);
			cell2.setBorder(Rectangle.NO_BORDER);
			
			table.addCell(cell1);
			table.addCell(cell2);
		}
		
		document.add(table);
		document.add(new Chunk("\n\n\n"));
		Paragraph footer = new Paragraph("Dokument wygenerowany przez program ForkLift WMS", courierFont);
		
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		
		document.add(footer);
		document.close();
		
		return true;
	}	
	
	public static boolean createDeliveryValuesOverTimePDF(XYChart.Series<String, Number> deliveryValuesData, File newFile) throws IOException, DocumentException{
		Document document = createNewPDF(newFile);
		document.open();
		

		BaseFont baseFont;
		baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);

		Font biggerFont = new Font(baseFont, 18);
		Font paragraphFont = new Font(baseFont, 12, Font.NORMAL);
		Font courierFont = new Font(footerFont, 8, Font.BOLD);
		
		Paragraph preface = new Paragraph();

		preface.add(new Paragraph("\nDane dot. warto�ci dostaw w dziedzinie czasu\n", biggerFont));
		
		document.add(preface);

		
		Paragraph content = new Paragraph();
		content.add(new Chunk("\nPoni�ej przedstawione zosta�y w postaci tabeli dane dotycz�ce warto�ci dostaw do magazynu w dziedzinie czasu.\n\n\n", paragraphFont));
		content.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(content);
		
		PdfPTable table = new PdfPTable(2);
		
		
		PdfPCell productHeader = new PdfPCell(new Phrase("Data dostawy", paragraphFont));
		productHeader.setBorder(Rectangle.BOTTOM);
		productHeader.setPaddingBottom(5.0F);
		
		PdfPCell valueHeader = new PdfPCell(new Phrase("Warto�� [z�]", paragraphFont));
		valueHeader.setBorder(Rectangle.BOTTOM);
		valueHeader.setPaddingBottom(5.0F);
		
		
		table.addCell(productHeader);
		table.addCell(valueHeader);
		
		table.setHeaderRows(1);
		table.getDefaultCell().setBorder(Rectangle.BOTTOM);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		for(XYChart.Data<String, Number> data: deliveryValuesData.getData()){
			PdfPCell cell1 = new PdfPCell(new Phrase(data.getXValue(), paragraphFont));
			PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(data.getYValue()), paragraphFont));
			
			cell1.setBorder(Rectangle.NO_BORDER);
			cell2.setBorder(Rectangle.NO_BORDER);
			
			table.addCell(cell1);
			table.addCell(cell2);
		}
		
		document.add(table);
		document.add(new Chunk("\n\n\n"));
		Paragraph footer = new Paragraph("Dokument wygenerowany przez program ForkLift WMS", courierFont);
		
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		
		document.add(footer);
		document.close();
		
		return true;
	}
	
	public static boolean createCategoryIncomesPDF(ObservableList<PieChart.Data> categoryIncomesData , File newFile) throws IOException, DocumentException{
		Document document = createNewPDF(newFile);
		document.open();
		

		BaseFont baseFont;
		baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);

		Font biggerFont = new Font(baseFont, 18);
		Font paragraphFont = new Font(baseFont, 12, Font.NORMAL);
		Font courierFont = new Font(footerFont, 8, Font.BOLD);
		
		Paragraph preface = new Paragraph();

		preface.add(new Paragraph("\nDane dot. warto�ci zam�wie� wed�ug podzia�u na kategorie\n", biggerFont));
		
		document.add(preface);

		
		Paragraph content = new Paragraph();
		content.add(new Chunk("\nPoni�ej przedstawione zosta�y w postaci tabeli dane dotycz�ce warto�ci zam�wie� wed�ug podzia�u na kategorie.\n\n\n", paragraphFont));
		content.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(content);
		
		PdfPTable table = new PdfPTable(2);
		
		
		PdfPCell productHeader = new PdfPCell(new Phrase("Kategoria", paragraphFont));
		productHeader.setBorder(Rectangle.BOTTOM);
		productHeader.setPaddingBottom(5.0F);
		
		PdfPCell valueHeader = new PdfPCell(new Phrase("Warto�� zam�wie� [z�]", paragraphFont));
		valueHeader.setBorder(Rectangle.BOTTOM);
		valueHeader.setPaddingBottom(5.0F);
		
		
		table.addCell(productHeader);
		table.addCell(valueHeader);
		
		table.setHeaderRows(1);
		table.getDefaultCell().setBorder(Rectangle.BOTTOM);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		for(PieChart.Data data: categoryIncomesData){
			PdfPCell cell1 = new PdfPCell(new Phrase(data.getName(), paragraphFont));
			PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(data.getPieValue()), paragraphFont));
			
			cell1.setBorder(Rectangle.NO_BORDER);
			cell2.setBorder(Rectangle.NO_BORDER);
			
			table.addCell(cell1);
			table.addCell(cell2);
		}
		
		document.add(table);
		document.add(new Chunk("\n\n\n"));
		Paragraph footer = new Paragraph("Dokument wygenerowany przez program ForkLift WMS", courierFont);
		
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		
		document.add(footer);
		document.close();
		
		return true;
	}
	
	public static boolean createQuantityDistributionPDF(ObservableList<PieChart.Data> quantityDistributionData, File newFile, String locationID) throws IOException, DocumentException{
		Document document = createNewPDF(newFile);
		document.open();
		

		BaseFont baseFont;
		baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);

		Font biggerFont = new Font(baseFont, 18);
		Font paragraphFont = new Font(baseFont, 12, Font.NORMAL);
		Font courierFont = new Font(footerFont, 8, Font.BOLD);
		
		Paragraph preface = new Paragraph();

		preface.add(new Paragraph("\nDane dot. rozk�adu ilo�ciowego w lokacji " + locationID +  "\n", biggerFont));
		
		document.add(preface);

		
		Paragraph content = new Paragraph();
		content.add(new Chunk("\nPoni�ej przedstawione zosta�y w postaci tabeli dane dotycz�ce ilo�ci produkt�w sk�adowanych w lokacji " + locationID + " .\n\n\n", paragraphFont));
		content.setAlignment(Element.ALIGN_JUSTIFIED);
		document.add(content);
		
		PdfPTable table = new PdfPTable(2);
		
		
		PdfPCell productHeader = new PdfPCell(new Phrase("Produkt", paragraphFont));
		productHeader.setBorder(Rectangle.BOTTOM);
		productHeader.setPaddingBottom(5.0F);
		
		PdfPCell valueHeader = new PdfPCell(new Phrase("Ilo�� [szt]", paragraphFont));
		valueHeader.setBorder(Rectangle.BOTTOM);
		valueHeader.setPaddingBottom(5.0F);
		
		
		table.addCell(productHeader);
		table.addCell(valueHeader);
		
		table.setHeaderRows(1);
		table.getDefaultCell().setBorder(Rectangle.BOTTOM);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		for(PieChart.Data data: quantityDistributionData){
			PdfPCell cell1 = new PdfPCell(new Phrase(data.getName(), paragraphFont));
			PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(data.getPieValue()), paragraphFont));
			
			cell1.setBorder(Rectangle.NO_BORDER);
			cell2.setBorder(Rectangle.NO_BORDER);
			
			table.addCell(cell1);
			table.addCell(cell2);
		}
		
		document.add(table);
		document.add(new Chunk("\n\n\n"));
		Paragraph footer = new Paragraph("Dokument wygenerowany przez program ForkLift WMS", courierFont);
		
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		
		document.add(footer);
		document.close();
		
		return true;
	}
	
	public static boolean createWayBillPDF(Shipment shipment, File newFile, boolean mrnT1Selected, boolean invoiceSelected) throws DocumentException, IOException{
		
		if(shipment == null || newFile == null) return false;
		
		Document document = createNewPDF(newFile);
		document.open();
		
		BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		Font textFont = new Font(baseFont, 12, Font.BOLD);
		Font paragraphFont = new Font(baseFont, 8, Font.BOLDITALIC);
		
		Font primaryCellFont = new Font(baseFont, 7, Font.BOLDITALIC);
		Font secCellFont = new Font(baseFont, 7, Font.NORMAL);
		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);
		Font courierFont = new Font(footerFont, 8, Font.NORMAL);
		
		Paragraph preface = new Paragraph();
		preface.setFont(textFont);
		preface.add("\nLIST PRZEWOZOWY KRAJOWY NR " + shipment.getShipmentID() + "\n\n");
		document.add(preface);
		document.add(new Phrase("Ten list przewozowy stanowi jednocze�nie umow� na wykonywan� us�ug� transportow�, kt�ra podlega obowi�zuj�cym przepisom polskiego prawa przewozowego.\n"
				+ "Wszelkie rozszczenia z tytu�u szk�d maj�tkowych wyrz�dzonych podczas �wiadczenia niniejszego zlecenia s� pokrywane z aktualnej polisy OC przewo�nika.\n", secCellFont));
		
		
		PdfPTable mainTable = new PdfPTable(3);
		mainTable.setWidthPercentage(90F);
		mainTable.getDefaultCell().setPadding(2F);
		mainTable.getDefaultCell().setBorderWidth(0.4F);
		mainTable.setPaddingTop(20);
		PdfPCell firstCell;
		PdfPCell secCell;
		PdfPTable secondaryTable;

		Map<String, String[]> data = new LinkedHashMap<>();
		data.put("Za�adunek", new String[] {"\n\n"});
		data.put("Nadawca: (nazwisko lub nazwa, adres)", new String[] {EnterpriseData.getEnterpriseName(), EnterpriseData.getStreet(), EnterpriseData.getCity() + " , " + EnterpriseData.getCountry(), "NIP: " + EnterpriseData.getNip()});
		data.put("Odbiorca (nazwisko lub nazwa, adres):", new String[] {shipment.getShippedName(), shipment.getAddress() + " " + shipment.getCity(), shipment.getCountry() + " " + shipment.getPostalCode()});
		data.put("Miejsce i data za�adunku", new String[]{EnterpriseData.getCity() + " , " + EnterpriseData.getCountry(), shipment.getShippedDate().toString()});
		data.put("Miejsce przeznaczenia:", new String[]{shipment.getShippedName(), shipment.getAddress() + " " + shipment.getCity(), shipment.getCountry() + " " + shipment.getPostalCode()});
		
		String[] array = new String[]{"",""};
		if(mrnT1Selected && invoiceSelected) {
			array[0] = "MRN/T1";
			array[1] = "Invoice";
		} 
		else if (mrnT1Selected) array[0] = "MRN/T1"; 
		else if (invoiceSelected) array[0] = "Invoice";
		
		data.put("Za��czone dokumenty:", array);

		int size = shipment.getShipmentDetails().size();
		String[] products = new String[size];
		int i = 0;
		for(ShipmentDetails sd : shipment.getShipmentDetails()){
			products[i++] = "ID Zam�wienia: " + String.valueOf(sd.getOrder().getOrderID()) + " - palety: " + sd.getPalletsCount();
		}
		data.put("�adunek: ", products);
		data.put("Podpis i stempel nadawcy", new String[]{"\n\n"});
		data.put("Miejsce i data wystawienia:", new String[] {EnterpriseData.getEnterpriseName(), EnterpriseData.getStreet(), EnterpriseData.getCity() + "," + EnterpriseData.getCountry()});
		data.put("Transport", new String[] {"\n\n\n"});
		data.put("Przewo�nik:", new String[]{shipment.getShipper().getCompanyName()});
		data.put("Nr rej. samochodu/Nr rej. naczepy/Imi� i nazwisko kierowcy:", new String[] {"\n\n______________________________"});
		data.put("Zastrze�enia i uwagi przewo�nika", new String[]{"\n\n\n\n"});
		data.put("Czytelny podpis i stemple przewo�nika", new String[]{"\n\n\n\n"});
		data.put("Nale�no�� od nadawcy", new String[]{""});
		data.put("Czytelny podpis kierowcy po za�adunku", new String[]{"\n\n\n\n"});
		data.put("Roz�adunek", new String[] {"\n\n"});
		data.put("Zastrze�enia i uwagi odbiorcy", new String[] {"\n\n\n\n"});
		data.put("Przesy�k� otrzymano", new String[]{"Miejscowo��:\n", "Data:\n", "Podpis odbiorcy:\n"});
		data.put("Godzina", new String[]{"Przyjazdu:\n", "Zako�czenia roz�adunku:\n", "Odjazdu:\n"});
		
		
		Set<String> input = data.keySet();
		
		for(String key : input){
			Phrase title = new Phrase(key, paragraphFont); 
			firstCell = new PdfPCell();
			firstCell.addElement(title);
			firstCell.setPadding(3);
			firstCell.setPaddingTop(1);
			firstCell.setBorder(Rectangle.NO_BORDER);
			String[] stringArray = data.get(key);
			
			Phrase secCellPhrase = new Phrase();
			secCellPhrase.setFont(secCellFont);
			for(String s : stringArray){
				secCellPhrase.add(s);
				secCellPhrase.add("\n");
			}
			
			secCell = new PdfPCell(secCellPhrase);
			secCell.setPadding(3);
			secCell.setPaddingLeft(20);
			secCell.setBorder(Rectangle.NO_BORDER);
			secondaryTable = new PdfPTable(1);
			secondaryTable.addCell(firstCell);
			secondaryTable.addCell(secCell);
			if(!key.equals("�adunek: ") && !key.equals("Nale�no�� od nadawcy"))
			mainTable.addCell(secondaryTable);
			else if(key.equals("�adunek: ")) {
				
				PdfPTable someData = new PdfPTable(7);
				someData.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				someData.getDefaultCell().setPadding(0F);
				someData.setWidthPercentage(100F);
				someData.addCell(new Phrase("Cechy i numery",  primaryCellFont));
				someData.addCell(new Phrase("Ilo�� sztuk" ,primaryCellFont));
				someData.addCell(new Phrase("Opakowanie", primaryCellFont));
				someData.addCell(new Phrase("Rodzaj towaru", primaryCellFont));
				someData.addCell(new Phrase("Waga brutto [kg]" ,primaryCellFont));
				someData.addCell(new Phrase("Obj�to�� [m3]", primaryCellFont));
				someData.addCell(new Phrase("   wg za��cznika nr", primaryCellFont));

				for(i = 0 ; i < 21; i++) someData.addCell(new Phrase("\n\n\n"));
				
				PdfPTable additionalTable = new PdfPTable(4);
				additionalTable.setWidthPercentage(100F);
				additionalTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				additionalTable.addCell(new Phrase("Klasa", new Font(baseFont, 5, Font.NORMAL)));
				additionalTable.addCell(new Phrase("Liczba" ,new Font(baseFont, 5, Font.NORMAL)));
				additionalTable.addCell(new Phrase("Litera", new Font(baseFont, 5, Font.NORMAL)));
				additionalTable.addCell(new Phrase("(ADR*)", new Font(baseFont, 5, Font.NORMAL)));
				
				for(i = 0 ; i < 4 ; i++) additionalTable.addCell("\n");

				firstCell = new PdfPCell();
				firstCell.setColspan(7);
				firstCell.addElement(additionalTable);
				someData.addCell(firstCell);
				
				PdfPCell someCell = new PdfPCell();
				someCell.setColspan(2);
				someCell.setRowspan(3);
				someCell.addElement(someData);
				mainTable.addCell(someCell);
			} else if (key.equals("Nale�no�� od nadawcy")){

				PdfPTable someData = new PdfPTable(4);
				someData.getDefaultCell().setPadding(1F);
				someData.setWidthPercentage(100F);
				someData.addCell(new Phrase("Rodzaj nale�no�ci\n\n",  primaryCellFont));
				someData.addCell(new Phrase("Ci�ar\n\n" ,primaryCellFont));
				someData.addCell(new Phrase("Stawka\n\n", primaryCellFont));
				someData.addCell(new Phrase("Warto��\n\n", primaryCellFont));
				
				someData.addCell(new Phrase("Op�ata skarbowa", primaryCellFont));
				someData.addCell(new Phrase("Przewo�ne za km", primaryCellFont));
				for(i = 0 ; i < 11; i++) someData.addCell(new Phrase("\n"));
				PdfPCell someCell = new PdfPCell();
				someCell.setColspan(2);
				someCell.setRowspan(1);
				someCell.addElement(someData);
				mainTable.addCell(someCell);
			}
			
			
		}
		
		document.add(mainTable);
		
		Paragraph footer = new Paragraph("\nDokument wygenerowany przez program ForkLift WMS", courierFont);
		
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		
		document.add(footer);
		document.close();
		return true;
	}
	
	public static boolean createOrderInvoicePDF(Order o, File newFile) throws DocumentException, IOException{
		
		if(o == null || newFile == null) return false;
		
		Document document = createNewPDF(newFile);
		document.open();
		
		BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		Font textFont = new Font(baseFont, 14, Font.BOLD);
		Font paragraphFont = new Font(baseFont, 9, Font.BOLD);
		Font secCellFont = new Font(baseFont, 8, Font.NORMAL);
		Font odDetailsFont = new Font(baseFont, 9, Font.NORMAL);
		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);
		Font courierFont = new Font(footerFont, 8, Font.NORMAL);
		
		Paragraph preface = new Paragraph();
		preface.setFont(textFont);
		preface.add("\nFaktura VAT nr " + o.getOrderID() + "\n");
		document.add(preface);
		document.add(new Paragraph("ID zam�wienia: " + o.getOrderID() + "\n\n", paragraphFont));

		Map<String, String> sellerData = new LinkedHashMap<>();
		Map<String, String> customerData = new LinkedHashMap<>();
		
		
		sellerData.put("Nazwa firmy: ", EnterpriseData.getEnterpriseName());
		sellerData.put("Adres: ", EnterpriseData.getStreet());
		sellerData.put("Miasto: ", EnterpriseData.getCity());
		sellerData.put("Kod pocztowy: ", EnterpriseData.getPostalCode());
		sellerData.put("Kraj: ", EnterpriseData.getCountry());
		sellerData.put("Telefon: ", EnterpriseData.getPhoneNumber());
		sellerData.put("E-mail: ", EnterpriseData.getEmail());
		sellerData.put("NIP: ", EnterpriseData.getNip());
		sellerData.put("REGON: ", EnterpriseData.getRegon());
		
		Customer c = o.getCustomer();
		customerData.put("Nazwa firmy: ", c.getCompanyName());
		customerData.put("Adres: ", c.getAddress());
		customerData.put("Miasto: ", c.getCity());
		customerData.put("Kod pocztowy: ", c.getPostalCode());
		customerData.put("Telefon: ", c.getPhone());
		customerData.put("E-mail: ", c.getEmail());
		customerData.put("Przedstawiciel firmy: ", c.getRepresentative());
		
		PdfPTable customerTable = new PdfPTable(2);
		
		PdfPCell cell = new PdfPCell();
		Set<String> sellerSet = sellerData.keySet();
		cell.addElement(new Chunk("Sprzedawca", paragraphFont));
		for(String key : sellerSet){
			cell.addElement(new Chunk(key + "		" + sellerData.get(key), secCellFont));
			
		}
		cell.setBorder(Rectangle.NO_BORDER);
		customerTable.addCell(cell);
		
		
		cell = new PdfPCell();
		Set<String> customerSet = customerData.keySet();
		cell.addElement(new Chunk("Nabywca", paragraphFont));
		for(String key : customerSet){
			cell.addElement(new Chunk(key +"		" + customerData.get(key), secCellFont));
		}
		cell.setBorder(Rectangle.NO_BORDER);
		customerTable.addCell(cell);
		document.add(customerTable);
		document.add(new Chunk("\n\n"));
		
		PdfPTable orderDetailsTable = new PdfPTable(7);
		int i = 1;
		double sum = 0;
		
		orderDetailsTable.addCell(new Phrase("L.p", paragraphFont));
		orderDetailsTable.addCell(new Phrase("Produkt", paragraphFont));
		orderDetailsTable.addCell(new Phrase("Ilo�� jdn.", paragraphFont));
		orderDetailsTable.addCell(new Phrase("Ilo��", paragraphFont));
		orderDetailsTable.addCell(new Phrase("Cena jdn.", paragraphFont));
		orderDetailsTable.addCell(new Phrase("Rabat", paragraphFont));
		orderDetailsTable.addCell(new Phrase("Kwota", paragraphFont));
		orderDetailsTable.setHeaderRows(1);
		for(OrderDetails od : o.getOrderDetails()){
			orderDetailsTable.addCell(new Phrase(String.valueOf(i++), odDetailsFont));
			orderDetailsTable.addCell(new Phrase(od.getProduct().getProductName(), odDetailsFont));
			orderDetailsTable.addCell(new Phrase(od.getProduct().getQuantityPerUnit(), odDetailsFont));
			orderDetailsTable.addCell(new Phrase(String.valueOf(od.getQuantity()), odDetailsFont));
			orderDetailsTable.addCell(new Phrase(String.valueOf(od.getUnitPrice()), odDetailsFont));
			orderDetailsTable.addCell(new Phrase(String.valueOf(od.getDiscount() * 100) + "%", odDetailsFont));
			int odSum = (int) (od.getQuantity() * od.getUnitPrice() * (1 - od.getDiscount()));
			sum += odSum;
			orderDetailsTable.addCell(new Phrase(String.valueOf((double) odSum), odDetailsFont));
		}
		
		document.add(orderDetailsTable);
		document.add(new Chunk("\n\n"));

		PdfPTable vatTable = new PdfPTable(4);
		vatTable.addCell(new Phrase("Stawka VAT", paragraphFont));
		vatTable.addCell(new Phrase("Netto", paragraphFont));
		vatTable.addCell(new Phrase("VAT", paragraphFont));
		vatTable.addCell(new Phrase("Brutto", paragraphFont));
		vatTable.setHeaderRows(1);
		vatTable.addCell(new Phrase("22%", odDetailsFont));
		vatTable.addCell(new Phrase(String.valueOf(sum), odDetailsFont));
		vatTable.addCell(new Phrase(String.valueOf(sum * 0.22), odDetailsFont));
		vatTable.addCell(new Phrase(String.valueOf(sum * 1.22), odDetailsFont));
		
		document.add(vatTable);
		
		document.add(new Chunk("\nDo zap�aty : " + String.valueOf(sum * 1.22), paragraphFont));
		
		document.add(new Chunk("\nData zam�wienia: " + o.getOrderDate().toString() + "\n", odDetailsFont));
		document.add(new Chunk("Data wymagana: " + o.getRequiredDate().toString() + "\n", odDetailsFont));
		document.add(new Chunk("Osoba odpowiedzialna: " + o.getInsertEmployee().getFullNameProperty().get() + "\n", odDetailsFont));
		Paragraph footer = new Paragraph("\nDokument wygenerowany przez program ForkLift WMS", courierFont);
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		
		document.add(footer);
		
		document.close();
		return true;
	}
	
	public static boolean createDeliveryInvoicePDF(Delivery d, File newFile) throws DocumentException, IOException{
		
		if(d == null || newFile == null) return false;
		
		Document document = createNewPDF(newFile);
		document.open();
		
		BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		Font textFont = new Font(baseFont, 14, Font.BOLD);
		Font paragraphFont = new Font(baseFont, 9, Font.BOLD);
		Font secCellFont = new Font(baseFont, 8, Font.NORMAL);
		Font odDetailsFont = new Font(baseFont, 9, Font.NORMAL);
		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);
		Font courierFont = new Font(footerFont, 8, Font.NORMAL);
		
		Paragraph preface = new Paragraph();
		preface.setFont(textFont);
		preface.add("\nPrzyj�cie dostawy nr " + d.getDeliveryID() + "\n");
		document.add(preface);
		document.add(new Paragraph("ID dostawy: " + d.getDeliveryID() + "\n\n", paragraphFont));

		Map<String, String> ourEnterpriseData = new LinkedHashMap<>();
		Map<String, String> customerData = new LinkedHashMap<>();
		
		ourEnterpriseData.put("Nazwa firmy: ", EnterpriseData.getEnterpriseName());
		ourEnterpriseData.put("Adres: ", EnterpriseData.getStreet());
		ourEnterpriseData.put("Miasto: ", EnterpriseData.getCity());
		ourEnterpriseData.put("Kod pocztowy: ", EnterpriseData.getPostalCode());
		ourEnterpriseData.put("Kraj: ", EnterpriseData.getCountry());
		ourEnterpriseData.put("Telefon: ", EnterpriseData.getPhoneNumber());
		ourEnterpriseData.put("E-mail: ", EnterpriseData.getEmail());
		ourEnterpriseData.put("NIP: ",EnterpriseData.getNip());
		ourEnterpriseData.put("REGON: ", EnterpriseData.getRegon());
		
		Supplier s = d.getSupplier();
		customerData.put("Nazwa firmy: ", s.getCompanyName());
		customerData.put("Adres: ", s.getAddress());
		customerData.put("Miasto: ", s.getCity());
		customerData.put("Kod pocztowy: ", s.getPostalCode());
		customerData.put("Telefon: ", s.getPhoneNumber());
		customerData.put("Przedstawiciel firmy: ", s.getRepresentative());
		
		PdfPTable customerTable = new PdfPTable(2);
		
		PdfPCell cell = new PdfPCell();
		Set<String> enterpriseSet = ourEnterpriseData.keySet();
		cell.addElement(new Chunk("Przyjmuj�cy", paragraphFont));
		for(String key : enterpriseSet){
			cell.addElement(new Chunk(key + "		" + ourEnterpriseData.get(key), secCellFont));
			
		}
		cell.setBorder(Rectangle.NO_BORDER);
		customerTable.addCell(cell);
		
		
		cell = new PdfPCell();
		Set<String> customerSet = customerData.keySet();
		cell.addElement(new Chunk("Dostawca", paragraphFont));
		for(String key : customerSet){
			cell.addElement(new Chunk(key +"		" + customerData.get(key), secCellFont));
		}
		cell.setBorder(Rectangle.NO_BORDER);
		customerTable.addCell(cell);
		document.add(customerTable);
		document.add(new Chunk("\n\n"));
		
		PdfPTable deliveryDetailsTable = new PdfPTable(4);
		int i = 1;
		
		deliveryDetailsTable.addCell(new Phrase("L.p", paragraphFont));
		deliveryDetailsTable.addCell(new Phrase("Produkt", paragraphFont));
		deliveryDetailsTable.addCell(new Phrase("Ilo�� jdn.", paragraphFont));
		deliveryDetailsTable.addCell(new Phrase("Ilo��", paragraphFont));
		
		deliveryDetailsTable.setHeaderRows(1);
		for(DeliveryDetails dd : d.getDeliveryDetails()){
			deliveryDetailsTable.addCell(new Phrase(String.valueOf(i++), odDetailsFont));
			deliveryDetailsTable.addCell(new Phrase(dd.getProduct().getProductName(), odDetailsFont));
			deliveryDetailsTable.addCell(new Phrase(dd.getProduct().getQuantityPerUnit(), odDetailsFont));
			deliveryDetailsTable.addCell(new Phrase(String.valueOf(dd.getQuantity()), odDetailsFont));
			
		}
		
		document.add(deliveryDetailsTable);
		document.add(new Chunk("\n\n"));

		document.add(new Chunk("\nData zlecenia dostawy: " + d.getRequiredDeliveryDate().toString() + "\n", odDetailsFont));
		document.add(new Chunk("Data przybycia dostawy: " + d.getDeliveryArrivalDate().toString() + "\n", odDetailsFont));
		document.add(new Chunk("Osoba odpowiedzialna: " + d.getEmployee().getFullNameProperty().get() + "\n", odDetailsFont));
		Paragraph footer = new Paragraph("\nDokument wygenerowany przez program ForkLift WMS", courierFont);
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		
		document.add(footer);
		
		document.close();
		return true;
	}
	
	public static boolean createCMRPDF(Shipment s,  File newFile, boolean mrnT1Selected, boolean invoiceSelected) throws DocumentException, IOException{
		
		if(s == null || newFile == null) return false;
		
		Document document = new Document(PageSize.A4, 10, 10, 20, 5);
		//Meta data
		document.addTitle("My PDF document");
		document.addSubject("Using iText");
		document.addKeywords("Java, PDF, iText");
		document.addAuthor("Piotr Ga�ecki");
		document.addCreator("Piotr Ga�ecki");
		
		PdfWriter.getInstance(document, new FileOutputStream(newFile));
		document.open();
		
		BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		Font paragraphFont = new Font(baseFont, 10, Font.BOLD);

		
		BaseFont footerFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1250", BaseFont.EMBEDDED);
		Font courierFont = new Font(footerFont, 8, Font.NORMAL);
		
		document.add(new Chunk("Egzemplarz dla nadawcy/Copy for sender/Exemplar f�r den Absender", paragraphFont));


		
		LinkedHashMap<String, String[]> data = new LinkedHashMap<>();
		data.put("Nadawca (nazwisko lub nazwa, adres, kraj)\nAbsender (Name, Anschrift, Land)\nSender (name, address, country)", new String[]{EnterpriseData.getEnterpriseName(), EnterpriseData.getStreet() + " " + EnterpriseData.getCity() + " " + EnterpriseData.getPostalCode(), EnterpriseData.getCountry() });
		data.put("MI�DZYNARODOWY SAMOCHODOWY LIST PRZEWOZOWY\nINTERNATIONALER FRACHTBRIEF\nINTERNATIONAL CONSIGNMENT" , new String[]{"		                              CMR			","Niniejszy przew�z podlega postanowieniom konwencji o umowie mi�dzynarodowej przewozu drogowego towar�w (CMR) bez wzgl�du na jak�kolwiek przeciwn� klauzul�.",  "Diese Bef�rderung unterliegt trotz einer gegenteiligen Abmachung den Bestimmungen des �bereinkommens �ber den Bef�rderungsvertrag im internationalen Strasseng�terverkehr (CMR)", "This carriage is subject notwithstanding any clause to the contrary, to the Convention on the Contract for the international Carriage of goods by road (CMR)"});
		data.put("Odbiorca (nazwisko lub nazwa, adres, kraj)\nEmpf�nger (Name, Anschrift, Land)\nConsignee (name, address, country)", new String[]{s.getShippedName(), s.getAddress(),s.getCountry()});
		data.put("Miejsce przeznaczenia (miejscowo��, kraj)\nAuslieferungsort des Gutes (Ort, Land)\nPlace of delivery of the goods (place, country)", new String[]{s.getCity(), s.getCountry()});
		data.put("Miejsce i data za�adowania (miejscowo��, kraj, data)\nOrt und Tag der �bernahme das Gutes (Ort, Land, Datum)\nPlace and date taking over the goods (place, country, date)", new String[]  {EnterpriseData.getCity() + " " + s.getShippedDate().toString(), EnterpriseData.getCountry()});
		
		String[] array = new String[]{"",""};
		if(mrnT1Selected && invoiceSelected) {
			array[0] = "MRN/T1";
			array[1] = "Invoice";
		} 
		else if (mrnT1Selected) array[0] = "MRN/T1"; 
		else if (invoiceSelected) array[0] = "Invoice";
		
		
		data.put("Za��czone dokumenty\nBeigef�gte Dokumente\nDocuments attached", array);

		data.put("Cechy i numery\nKennzeichen und Nummern\nMarks and numbers", new String[]{"\n\n\n"});
		data.put("Numer statystyczny\nStatistiknummer\nStatistical number", new String[]{"\n\n\n"});
		data.put("Waga brutto w kg\nBruttogewicht in kg\nGroos weight in kg", new String[]{"\n\n\n"});
		data.put("Obj�to�� w m3\nUmfang m3\nVolume in m3", new String[] {"\n\n\n"});
		data.put("Instrukcje nadawcy\nAnweisungen des Absenders\nSender�s instructions", new String[]{"\n\n\n\n"});
		data.put("Postanowienia odno�nie przewo�nego\nFrechtzahlungsanweisungen\nInstruction as to payment for carriage\n\nPrzewo�ne zap�acone / frei / Carriage paid\nPrzewo�ne nieop�acone / Unfrei / Carriage forward", new String[]{"\n\n\n"});
		data.put("Zap�ata / R�ckerstattuung / Cash on deliver", new String[] {"\n\n\n"});
		data.put("Przewoznik (nazwisko lub nazwa, adres, kraj)\nFrachtf�hrer (Name, Anschrift, Land)\nCarrier (name, address, country)", new String[]{s.getShipper().getCompanyName(), s.getCountry(), "      \nNR REJ.:                                \\                                 "});
		data.put("Kolejni przewo�nicy (nazwisko lub nazwa, adres, kraj)\nNachfolgende Frachtf�hrer (Name, Anschrift, Land)\nSuccessive carriers (Name, address, country)", new String[]{"\n\n\n"});
		data.put("Zastrze�enia i uwagi przewo�nika\nVorbehalte und Bemerkungen der Frachtf�hrer\nCarrier�s reservations and observations", new String[]{"\n\n\n"});
		data.put("Postanowienia specjalne\nBesondere Vereinbarungen\nSpecial agreements", new String[]{"\n\n\n"});
		data.put("Do zap�acenia\nZu zahlen vom\nTo be paid by", new String[]{"\n\n\n"});
		data.put("Wystawiono w\nAusgefertigt in\nEstablished in", new String [] {EnterpriseData.getCity() + " " + LocalDate.now().toString()});
		data.put("Podpis i stempel nadawcy\nUnterschrift und Stempel des Absenders\nSignature and stamp of the sender", new String[]{EnterpriseData.getEnterpriseName(), EnterpriseData.getCity(), EnterpriseData.getPostalCode() + " " + EnterpriseData.getCity()});
		data.put("Podpis i stempel przewoznika\nUnterschrift und Stempel des Frachtf�hrers\nSignature and stamp of the carrier", new String[]{"\n\n\n"});
		data.put("Przesy�k� otrzymano/ Gut empfangen/ Goods received", new String[]{"\n\nMiejscowo��      dnia      ", "Ort      am      ","Place      on      \n\n", "Podpis i stempel odbiorcy\nUnterschrift und Stempel des Empf�ngers\nSignature and stamp of the consignee"});

		PdfPTable mainTable = getColoredTable(data, new BaseColor(199, 0, 0), new Font(baseFont, 6, Font.NORMAL, new BaseColor(199, 0, 0)), new Font(baseFont, 6, Font.BOLD , new BaseColor(199, 0, 0)));
		document.add(new Phrase("\n"));
		document.add(mainTable);
		
		Paragraph footer = new Paragraph("\nDokument wygenerowany przez program ForkLift WMS", courierFont);
		
		
		Date teraz = new GregorianCalendar().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm  dd MMMM yyyy");
		footer.add("\n" + sdf.format(teraz));
		document.add(footer);
		
		document.newPage();
		document.add(new Chunk("Egzemplarz dla odbiorcy/Copy for consignee/Exemplar f�r den Empf�nger\n", paragraphFont));
		document.add(new Phrase("\n"));
		PdfPTable consigneeTable = getColoredTable(data, new BaseColor(0, 0, 199),new Font(baseFont, 6, Font.NORMAL, new BaseColor(0, 0, 199)), new Font(baseFont, 6, Font.BOLD , new BaseColor(0, 0, 199)));
		consigneeTable.getDefaultCell().setBorderColor(new BaseColor(0, 0, 199));
		consigneeTable.getDefaultCell().setBorderWidth(0.5F);
		document.add(consigneeTable);
		document.add(footer);
		
		document.newPage();
		document.add(new Chunk("Egzemplarz dla przewo�nika/Copy for carrier/Exemplar f�r den Frachtf�hrer\n", paragraphFont));
		PdfPTable carrierTable = getColoredTable(data, new BaseColor(0, 199, 0), new Font(baseFont, 6, Font.NORMAL, new BaseColor(0, 199, 0)), new Font(baseFont, 6, Font.BOLD , new BaseColor(0, 199, 0)));
		document.add(new Phrase("\n"));
		document.add(carrierTable);
		document.add(footer);
		
		document.newPage();
		document.add(new Chunk("", paragraphFont));
		document.add(new Phrase("\n"));
		PdfPTable blackTable = getColoredTable(data, BaseColor.BLACK, new Font(baseFont, 6, Font.NORMAL, BaseColor.BLACK), new Font(baseFont, 6, Font.BOLD , BaseColor.BLACK));
		document.add(blackTable);
		document.add(footer);
		document.close();

		return true;
	}
	
	private static PdfPTable getColoredTable(LinkedHashMap<String, String[]> data, BaseColor tableColor, Font primaryCellFont, Font secCellFont) throws DocumentException, IOException{
		
		PdfPTable mainTable = new PdfPTable(2);
		PdfPCell firstCell;
		PdfPCell secCell;
		PdfPTable secondaryTable;
		
		mainTable.setWidthPercentage(90);
		PdfPTable toBePaidTable = new PdfPTable(4);
		toBePaidTable.addCell(new Phrase("Do zap�acenia\nZu zahlen vom:\nTo be paid by", secCellFont));
		toBePaidTable.addCell(new Phrase("Nadawca\nAbsender\nSender", secCellFont));
		toBePaidTable.addCell(new Phrase("Waluta / W�hrung\n/ Currency", secCellFont));
		toBePaidTable.addCell(new Phrase("Odbiorca\nEmpf�nger\nConsignee", secCellFont));
		toBePaidTable.addCell(new Phrase("Przewo�ne / Fracht\n/ Carriage charges", secCellFont));
		for(int i = 0; i < 3; i++) toBePaidTable.addCell(new Phrase(""));
		toBePaidTable.addCell(new Phrase("Bonifikaty / Erm�ssigungen\nDeductions", secCellFont));
		for(int i = 0; i < 3; i++) toBePaidTable.addCell(new Phrase(""));
		toBePaidTable.addCell(new Phrase("Saldo / Zuschl�ge /\nBalance", secCellFont));
		for(int i = 0; i < 3; i++) toBePaidTable.addCell(new Phrase(""));
		toBePaidTable.addCell(new Phrase("Dop�aty / Nebengeb�hren\n/ Supplem. charges", secCellFont));
		for(int i = 0; i < 3; i++) toBePaidTable.addCell(new Phrase(""));
		toBePaidTable.addCell(new Phrase("Koszty dodatkowe\n/ Sonstiges / Miscellaneous", secCellFont));
		for(int i = 0; i < 3; i++) toBePaidTable.addCell(new Phrase(""));
		toBePaidTable.addCell(new Phrase("Ubezpieczenie / Insurance / Versicherung", secCellFont));
		for(int i = 0; i < 3; i++) toBePaidTable.addCell(new Phrase(""));
		toBePaidTable.addCell(new Phrase("Razem / Gesamtsumme\n/ Total to be paid", secCellFont));
		for(int i = 0; i < 3; i++) toBePaidTable.addCell(new Phrase(""));
		
		BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250", BaseFont.EMBEDDED);
		
		PdfPTable someData = new PdfPTable(4);
		someData.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		someData.getDefaultCell().setPadding(0F);
		someData.addCell(new Phrase("Cechy i numery\nKennzeichen und Nummern\nMarks and Nos",  primaryCellFont));
		someData.addCell(new Phrase("Ilo�� sztuk\nAnzahl der Packst�cke\nNumber of packages" ,primaryCellFont));
		someData.addCell(new Phrase("Spos�b opakowania\nArt der Verpackung\nMethod of packing", primaryCellFont));
		someData.addCell(new Phrase("Rodzaj towaru\nBezeichnung des Gutes\nNature of the goods", primaryCellFont));
		for(int i = 0 ; i < 4; i++) someData.addCell(new Phrase("\n\n\n"));
		
		PdfPTable additionalTable = new PdfPTable(5);
		additionalTable.setWidthPercentage(100F);
		additionalTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		additionalTable.addCell(new Phrase("Numer UN\nNummer UN\nNumber UN", new Font(baseFont, 5, Font.NORMAL, tableColor)));
		additionalTable.addCell(new Phrase("Patrz punkt 9\nBez.s.Nr.9\nLook point 9" ,new Font(baseFont, 5, Font.NORMAL, tableColor)));
		additionalTable.addCell(new Phrase("Klasa\nKlasse\nClass", new Font(baseFont, 5, Font.NORMAL, tableColor)));
		additionalTable.addCell(new Phrase ("Grupa pakowania\nVerpackung gruppe\nThe packing group", new Font(baseFont, 5, Font.NORMAL, tableColor)));
		additionalTable.addCell(new Phrase("(ADR*)", new Font(baseFont, 5, Font.NORMAL, tableColor)));
		for(int i = 0 ; i < 5 ; i++) additionalTable.addCell("\n");

		firstCell = new PdfPCell();
		firstCell.setColspan(4);
		firstCell.addElement(additionalTable);
		someData.addCell(firstCell);


		
		mainTable.getDefaultCell().setBorderColor(tableColor);
		mainTable.getDefaultCell().setBorderWidth(0.5F);
		int counter = 1;
		Set<String> input = data.keySet();
		for(String key : input){
			Phrase title = new Phrase(6, String.valueOf(counter++) + ". " + key, primaryCellFont); 
			firstCell = new PdfPCell();
			firstCell.addElement(title);
			firstCell.setPadding(1);
			firstCell.setBorder(Rectangle.NO_BORDER);
			String[] stringArray = data.get(key);
			
			Phrase secCellPhrase = new Phrase();
			secCellPhrase.setFont(secCellFont);
			for(String str : stringArray){
				secCellPhrase.add(str);
				secCellPhrase.add("\n");
			}
			
			secCell = new PdfPCell(secCellPhrase);
			secCell.setPadding(1);
			secCell.setPaddingLeft(10);
			secCell.setBorder(Rectangle.NO_BORDER);
			secondaryTable = new PdfPTable(1);
			secondaryTable.addCell(firstCell);
			secondaryTable.addCell(secCell);
			if(!key.equals("Do zap�acenia\nZu zahlen vom\nTo be paid by") && !key.equals("Cechy i numery\nKennzeichen und Nummern\nMarks and numbers"))
			mainTable.addCell(secondaryTable); 
			else if (key.equals("Do zap�acenia\nZu zahlen vom\nTo be paid by")){
				firstCell = new PdfPCell();
				firstCell.setRowspan(3);
				firstCell.addElement(toBePaidTable);
				mainTable.addCell(firstCell);
				counter--;
			} else if (key.equals("Cechy i numery\nKennzeichen und Nummern\nMarks and numbers")){
				firstCell = new PdfPCell();
				firstCell.setRowspan(2);
				firstCell.addElement(someData);
				mainTable.addCell(firstCell);
				counter--;
			}
				
		}
		return mainTable;
	}

	
	
}
