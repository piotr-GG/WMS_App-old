package app.model;

import java.time.LocalDate;

import app.model.Order.OrderStatus;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Delivery {
	
	private IntegerProperty deliveryID;
	private Supplier supplier;
	private Employee employee;
	private IntegerProperty palletCount;
	private LocalDate requiredDeliveryDate;
	private LocalDate deliveryArrivalDate;
	private StringProperty driverName;
	private ObservableList<DeliveryDetails> deliveryDetails;
	private DeliveryStatus deliveryStatus;
	
	
	/**
	 * Constructor used to create NEW delivery order that is afterwards managed by supplier
	 * @param deliveryID
	 * @param supplier
	 * @param employee
	 * @param palletCount
	 * @param deliveryDetails
	 * @param deliveryStatus
	 * @param requiredDeliveryDate
	 * @param deliveryArrivalDate
	 * @param driverName
	 */
	public Delivery(int deliveryID, Supplier supplier, Employee employee, int palletCount, ObservableList<DeliveryDetails> deliveryDetails, String deliveryStatus, LocalDate requiredDeliveryDate){
		this.deliveryID = new SimpleIntegerProperty(deliveryID);
		this.supplier = supplier;
		this.employee = employee;
		this.palletCount = new SimpleIntegerProperty(palletCount);
		this.deliveryDetails = deliveryDetails;
		this.deliveryStatus = Enum.valueOf(DeliveryStatus.class, deliveryStatus);
		this.requiredDeliveryDate = requiredDeliveryDate;
		
		this.deliveryArrivalDate = null;
		this.driverName = new SimpleStringProperty();

	}
	
	/**
	 * Constructor used to create a delivery instance that has already arrived at warehouse and is waiting for being taken by warehouse
	 * @param deliveryID
	 * @param supplier
	 * @param employee
	 * @param palletCount
	 * @param deliveryDetails
	 * @param deliveryStatus
	 * @param requiredDeliveryDate
	 * @param deliveryArrivalDate
	 * @param driverName
	 */
	
	public Delivery(int deliveryID, Supplier supplier, Employee employee, int palletCount, ObservableList<DeliveryDetails> deliveryDetails, String deliveryStatus, LocalDate requiredDeliveryDate, LocalDate deliveryArrivalDate, String driverName){
		this(deliveryID, supplier, employee, palletCount, deliveryDetails, deliveryStatus, requiredDeliveryDate);
		this.deliveryArrivalDate = deliveryArrivalDate;
		this.driverName = new SimpleStringProperty(driverName);
	}
	
	/*
	 * Getters - properties
	 * 
	 */
	public IntegerProperty getDeliveryIDProperty(){
		return this.deliveryID;
	}
	

	public IntegerProperty getPalletCountProperty(){
		return this.palletCount;
	}
	
	public StringProperty getDriverNameProperty(){
		return this.driverName;
	}
	
	/*
	 * Getters - values
	 * 
	 * 
	 * 
	 * 
	 */
	
	public int getDeliveryID(){
		return this.deliveryID.get();
	}
	
	public int getPalletCount(){
		return this.palletCount.get();
	}
	
	public Supplier getSupplier(){
		return this.supplier;
	}
	
	public Employee getEmployee(){
		return this.employee;
	}
	
	public ObservableList<DeliveryDetails> getDeliveryDetails(){
		return this.deliveryDetails;
	}
	
	public DeliveryStatus getDeliveryStatus(){
		return this.deliveryStatus;
	}
	
	public LocalDate getRequiredDeliveryDate(){
		return this.requiredDeliveryDate;
	}
	
	public LocalDate getDeliveryArrivalDate(){
		return this.deliveryArrivalDate;
	}
	
	public String getDriverName(){
		return this.driverName.get();
	}
	
	/*
	 * Setters 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public void setDeliveryID(int deliveryID){
		this.deliveryID.set(deliveryID);
	}
	
	public void setPalletCount(int palletCount){
		this.palletCount.set(palletCount);
	}
	
	public void setSupplier(Supplier supplier){
		this.supplier = supplier;
	}
	
	public void setEmployee(Employee employee){
		this.employee = employee;
	}
	
	public void setDeliveryDetails(ObservableList<DeliveryDetails> deliveryDetails){
		this.deliveryDetails = deliveryDetails;
	}
	
	public void setRequiredDeliveryDate(LocalDate requiredDeliveryDate){
		this.requiredDeliveryDate = requiredDeliveryDate;
	}
	
	public void setDeliveryArrivalDate(LocalDate deliveryArrivalDate){
		this.deliveryArrivalDate = deliveryArrivalDate;
	}
	
	public void setDriverName(String driverName){
		this.driverName.set(driverName);
	}
	public void setDeliveryStatus(String deliveryStatus){
		this.deliveryStatus = Enum.valueOf(DeliveryStatus.class, deliveryStatus);
	}
	
	public enum DeliveryStatus{
		Order_Is_Placed("Order is placed", "Zamówienie z³o¿one"), Arrived("Arrived", "Nadesz³a"), Taken_By_Warehouse("Taken by warehouse", "Przyjêta przez magazyn");
		
		private String extendedVersion;
		private String polishVersion;
		
		private DeliveryStatus(String extendedVersion, String polishVersion){
			this.extendedVersion = extendedVersion;
			this.polishVersion = polishVersion;
		}
		
		
		public String getExtendedVersion(){
			return this.extendedVersion;
		}
		
		public String getPolishVersion(){
			return this.polishVersion;
		}
		
		//Gets extended versions of the OrderStatus enum class objects
		public static String[] valuesExtended(){
			OrderStatus[] values = OrderStatus.values();
			String[] extendedValues = new String[values.length]; 
			for(int i = 0; i < values.length; i++){
				extendedValues[i] = values[i].getExtendedVersion();
			}
			return extendedValues;
		}
		
		public static String[] polishValues(){
			OrderStatus[] values = OrderStatus.values();
			String[] polishValues = new String[values.length];
			for(int i = 0; i < values.length; i++){
				polishValues[i] = values[i].getPolishVersion();
			}
			return polishValues;
		}
		
		public static OrderStatus convertIntoEnglishVersion(String polishVersion){
			OrderStatus[] values = OrderStatus.values();
			for(OrderStatus os : values){
				if(os.getPolishVersion().equals(polishVersion)){
					return os;
				}
			}
			return null;
		}
		
	}
}
