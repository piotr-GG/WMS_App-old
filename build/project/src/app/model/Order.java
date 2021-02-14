package app.model;

import java.time.LocalDate;

import app.model.Employee.Position;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Order extends GeographicalData {
	
	private IntegerProperty orderID;
	private Customer customer;
	private Employee insertEmployee;
	private LocalDate orderDate;
	private LocalDate requiredDate;
	private DoubleProperty freight;
	private StringProperty shipName;

	private ObservableList<OrderDetails> orderDetails;
	private OrderStatus orderStatus;
	private Employee pickEmployee;
	/**
	 * Default constructor
	 */
	
	public Order(int orderID, Customer customer, Employee insertEmployee, LocalDate orderDate, LocalDate requiredDate, LocalDate shippedDate, Shipper shipper, double freight, String shipName, 
			String shipAddress, String shipCity, String shipPostalCode, String shipCountry, ObservableList<OrderDetails> orderDetails, String orderStatus){
		super(shipAddress, shipCity, shipPostalCode, shipCountry);
		this.orderID = new SimpleIntegerProperty(orderID);
		this.customer = customer;
		this.insertEmployee = insertEmployee;
		this.orderDate = orderDate;
		this.requiredDate = requiredDate;
		this.freight = new SimpleDoubleProperty(freight);
		this.shipName = new SimpleStringProperty(shipName);
		this.orderDetails = orderDetails;
		
        try{
        this.orderStatus = Enum.valueOf(OrderStatus.class, orderStatus);
        }
        catch(IllegalArgumentException e){
            System.err.println("Wyst¹pi³ b³¹d przypisania statusu do zamówienia!");
        }
	}
	
	/**
	 * Constructor used to initialize new order instance, which is then filled in NewOrderDialog window
	 * @param OrderID
	 * @param customer
	 * @param employee
	 * @param orderDate
	 * @param orderStatus
	 */
	public Order(Customer customer, Employee insertEmployee, LocalDate orderDate, String orderStatus, ObservableList<OrderDetails> orderDetails){
		super();
		
		this.orderID = new SimpleIntegerProperty();
		this.requiredDate = LocalDate.now();
		this.freight = new SimpleDoubleProperty();
		this.shipName = new SimpleStringProperty();
		this.customer = customer;
		this.insertEmployee = insertEmployee;
		this.orderDate = orderDate;
        try{
        this.orderStatus = Enum.valueOf(OrderStatus.class, orderStatus);
        }
        catch(IllegalArgumentException e){
            System.err.println("Wyst¹pi³ b³¹d przypisania statusu do zamówienia!");
        }
        
        this.orderDetails = orderDetails;
        this.pickEmployee = null;
	}

	/**
	 * Getters - properties
	 */
	
	public IntegerProperty getOrderIDProperty() {
		return orderID;
	}

	public DoubleProperty getFreightProperty() {
		return freight;
	}

	public StringProperty getShipNameProperty() {
		return shipName;
	}




	/**
	 * Getters - values
	 */
	
	public int getOrderID() {
		return orderID.get();
	}


	public Customer getCustomer() {
		return customer;
	}

	public Employee getInsertEmployee() {
		return insertEmployee;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public LocalDate getRequiredDate() {
		return requiredDate;
	}
	
	public double getFreight() {
		return freight.get();
	}

	public String getShipName() {
		return shipName.get();
	}


	
	public OrderStatus getOrderStatus(){
		return this.orderStatus;
	}
	
	public Employee getPickEmployee() {
		return pickEmployee;
	}
	
	public ObservableList<OrderDetails> getOrderDetails() {
		return orderDetails;
	}


	/**
	 * Setters - values
	 */
	
	public void setOrderID(int orderID){
		this.orderID.set(orderID);
	}
	
	public void setCustomer(Customer customer){
		this.customer = customer;
	}
	
	public void setInsertEmployee(Employee insertEmployee){
		this.insertEmployee = insertEmployee;
	}
	
	public void setOrderDate(LocalDate orderDate){
		this.orderDate = orderDate;
	}
	
	public void setRequiredDate(LocalDate requiredDate){
		this.requiredDate = requiredDate;
	}
	
	public void setFreight(double freight){
		this.freight.set(freight);
	}
	
	public void setShipName(String shipName){
		this.shipName.set(shipName);
	}

	public void setOrderDetails(ObservableList<OrderDetails> orderDetails){
		this.orderDetails = orderDetails;
	}
	
	public void setPickEmployee(Employee pickEmployee) {
		this.pickEmployee = pickEmployee;
	}

	public enum OrderStatus{
		Inserted ("Inserted", "Wprowadzone"), Accepted ("Accepted", "Przyjête"), Rejected ("Rejected", "Odrzucone"), Pick_Suggestions_Issued ("Pick suggestions issued", "Wydano sugestie pobrania"), In_Progress ("In progress", "W trakcie realizacji"), 
		Carried_Out ("Carried out", "Zrealizowane"), Checked ("Checked", "Skontrolowane"), Shipment_Ready ("Shipment ready", "Gotowe do wysy³ki"), Awaits_Shipment ("Awaits_Shipment", "Oczekuje na wysy³kê"), Shipped ("Shipped", "Wys³ane"); 
		
		private String extendedVersion;
		private String polishVersion;
		
		private OrderStatus(String extendedVersion, String polishVersion){
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
