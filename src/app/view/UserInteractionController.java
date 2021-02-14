package app.view;


import java.io.IOException;

import java.util.Optional;



import app.MainApp;
import app.model.Message;
import app.utility.UtilityClass;
import app.view.addNewDialogs.NewMessageOverviewController;
import app.view.inheritance.ActionController;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UserInteractionController extends ActionController {

	

	private ObservableList<Message> messageData;
	private ObservableList<MessagePane> messagePanes;
	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private HBox paginationHBox;
	@FXML
	private Button editButton;
	@FXML
    private Accordion groupACC;
	@FXML
	private ComboBox<String> howManyMsgComboBox;
	@FXML
	private ImageView binImageView;
	@FXML
	private Label binLabel;
	
	private ObservableList<Button> paginationButtons;
	
	private ParallelTransition parallelTransition;
    private int messagesRemovedCount;
	//Defines how many messages are to be shown by default
	private static int howManyMsgToShow = 20;
	private boolean isLayoutResized = false; 
	@FXML
    private void initialize(){
    	messagesRemovedCount = 0;
		paginationButtons = FXCollections.observableArrayList();
		messageData = FXCollections.observableArrayList();
    	messagePanes = FXCollections.observableArrayList();
    	
    	editButton.setDisable(true);
    	
    	howManyMsgComboBox.getItems().addAll(FXCollections.observableArrayList(
    			"5", "10", "15", "20"
    	));
    	
    	//default value
    	howManyMsgComboBox.getSelectionModel().select("20");
    	
    	
    	howManyMsgComboBox.valueProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				
				howManyMsgToShow = Integer.parseInt(howManyMsgComboBox.getValue().toString());
				if(mainApp.getRootLayout().getLayoutBounds().getHeight() < 800 && howManyMsgToShow >= 15){
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Nie mo¿na wyœwietliæ wiadomoœci", "Wysokoœæ ekranu jest za ma³a, aby pomieœciæ ¿¹dan¹ iloœæ wiadomoœci!", currentStage).showAndWait();
				} else {
					addPaginationButtons();
					isLayoutResized = false;
				}
				
				
			}
    		
        });
    	
    	binImageView.setOnDragOver(new EventHandler<DragEvent>(){

			@Override
			public void handle(DragEvent event) {
				if(event.getGestureSource() != binImageView){
					event.acceptTransferModes(TransferMode.ANY);
				}
				event.consume();
				
			}
			
		});
		binImageView.setOnDragEntered(new EventHandler<DragEvent>(){
			@Override
			public void handle(DragEvent event) {
					binImageView.setImage(new Image("file:src/app/view/images/bin_open.png"));
					binLabel.setVisible(true);
					event.consume();
			}
			
		});
		

		
		binImageView.setOnDragDropped(new EventHandler<DragEvent>(){
			@Override
			public void handle(DragEvent event) {
				binImageView.setImage(new Image("file:src/app/view/images/bin_closed.png"));
				binLabel.setVisible(false);
				Dragboard db = event.getDragboard();
				int messageID = Integer.parseInt(db.getString());
				Optional<ButtonType> result = UtilityClass.showAlert(AlertType.CONFIRMATION, "Usuñ wiadomoœæ", "Usuwanie wiadomoœci", "Czy na pewno chcesz usun¹æ tê wiadomoœæ?", currentStage).showAndWait();
				if(result.get() == ButtonType.OK) removeMessageByItsID(messageID);
				event.setDropCompleted(true);
				stopTheTransition(binImageView);
				event.consume();
				
			}
		});
		
		binImageView.setOnDragExited(new EventHandler<DragEvent>(){
			@Override
			public void handle(DragEvent event) {
				binImageView.setImage(new Image("file:src/app/view/images/bin_closed.png"));
				binLabel.setVisible(false);
				event.consume();
			}
		});
    }
    
	public void initializeData(){
		updateCSSStyles();
		mainApp.getController().getStyleSheetPathProperty().addListener(changeListener -> {
			updateCSSStyles();
		});
		
		Bounds rootLayoutBounds = mainApp.getRootLayout().getLayoutBounds();
		if(rootLayoutBounds.getHeight() < 800 ) {
			howManyMsgComboBox.getSelectionModel().select("10");
		}
		
		mainApp.getRootLayout().layoutBoundsProperty().addListener(new ChangeListener<Bounds>(){

			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				if(isLayoutResized == false && newValue.getHeight() < 800 && howManyMsgToShow >= 15){
					howManyMsgComboBox.getSelectionModel().select("10");
					isLayoutResized = true;
				}
			}
			
		});
	}
	
	private void updateCSSStyles(){
		mainPane.getStylesheets().clear();
		mainPane.getStylesheets().add(mainApp.getController().getStyleSheetPath());
	}
	
	/**
	 * Adds buttons into the hBox and an actionlistener to a button 
	 * @param countButtons
	 */
	 public void addPaginationButtons(){
		 	messagesRemovedCount = 0;
			//determinate how many messages there are in the database
	    	int count = mainApp.getController().getDatabase().getCountMessages();
	    	
	    	//round up the message count to create an integer number of buttons
	    	int countButtons = (int) Math.ceil(count/ (double) howManyMsgToShow);
	    	
	    	//clear the hbox before adding new buttons
	    	paginationHBox.getChildren().clear();
	    	paginationButtons.clear();
	    	for(int i = 1; i <= countButtons; i++){
	    		Button paginationButton = new Button(String.valueOf(i));
	    		
	    		paginationButton.setOnAction(new EventHandler<ActionEvent>(){
	    			public void handle(ActionEvent event){
	    				showXMessages(Integer.parseInt(paginationButton.getText()));
	    			}
	    		});
	    		
	    		paginationHBox.getChildren().add(paginationButton);
	    		paginationButtons.add(paginationButton);
	    	}
	    showXMessages(1);
	    
	    groupACC.expandedPaneProperty().addListener(new ChangeListener<TitledPane>(){

			@Override
			public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue,
					TitledPane newValue) {
				
				if(newValue != null){
				TitledPane tp = newValue;
				if(tp.isExpanded()){
					Message message = getMessageAssociatedWithPane(tp);	
					boolean usersAreEqual = message.getUser().equalsOtherUser(mainApp.getController().getLoggedUser());
					if(usersAreEqual) {
						editButton.setDisable(false);
					} else {
						editButton.setDisable(true);
					}
				} else {
					editButton.setDisable(true);
				} 
			} else {
				editButton.setDisable(true);
			}
				
			}
	    	
	    });
	 }
    
    
	 public void showXMessages(int count){
		 messageData.clear();
		 groupACC.getPanes().clear();
		 messageData.addAll(mainApp.getController().getDatabase().getSomeMessages(count, howManyMsgToShow));
		 for(Button b : paginationButtons){
			 b.getStyleClass().clear();
			 b.getStyleClass().add("normalButton");
		 }
		 
		 paginationButtons.get(count - 1).getStyleClass().add("paginationButton");
		 for(Message m : messageData){
			addSingleMessage(m);
		 }
	 }

	public void addSingleMessage(Message m){
		MessagePane mp = messagePaneFactory(m);
		groupACC.getPanes().add(mp.getMessagePane());
		messagePanes.add(new MessagePane(mp.getMessagePane(), m));
	}
	
	public void addMessageAtFirstIndex(Message m){
		showXMessages(1);
		
		@SuppressWarnings("unused")
		MessagePane mp = messagePaneFactory(m);		
	
		if(messagePanes.size() > howManyMsgToShow) removeMessageAtLastIndex();
		
	}
	
	private void removeMessageAtLastIndex(){
		groupACC.getPanes().remove(groupACC.getPanes().size() - 1);
		messagePanes.remove(messagePanes.size() - 1);
	}
	
	private void removeMessageByItsID(int messageID){
		for(MessagePane mp : messagePanes){
			if(mp.getMessage().getMessageID() == messageID){
				boolean successfullyDeleted = mainApp.getController().getDatabase().deleteMessage(messageID);
				if(successfullyDeleted){
					if(messagesRemovedCount < howManyMsgToShow)  showXMessages(1);
					else addPaginationButtons();
						
					UtilityClass.showAlert(AlertType.INFORMATION, "Komunikat", "Usuniêto wiadomoœæ", "Wiadomoœæ zosta³a pomyœlnie usuniêta", currentStage).showAndWait();
				} else {
					UtilityClass.showAlert(AlertType.ERROR, "B³¹d", "Wyst¹pi³ b³¹d", "Podczas usuwania wiadomoœci wyst¹pi³ b³¹d!", currentStage).showAndWait();
				}
				return;
			}
			
		}
		
		System.out.println("Nie wesz³o w funkcjê... Czyli nie ma jej ju¿");
	}
	
	private void removeMessage(Message m){
		for(Message element : messageData){
			if(element.equals(m)) {
				int index = messageData.indexOf(element);
				messageData.remove(index);

			}
			return;
		}
	}
	
	private void removeMessagePane(MessagePane mp){
		for(MessagePane element : messagePanes){
			if(mp.getMessage().equals(element.getMessage())){
				int index = messagePanes.indexOf(element);
				messagePanes.remove(index);
				return;
			}
		}
	}
	
	private void removeTitledPane(MessagePane mp){
		for(TitledPane tp : groupACC.getPanes()){
			if( ((Message) tp.getUserData()).equals(mp.getMessage())){
				int index = groupACC.getPanes().indexOf(tp);
				groupACC.getPanes().remove(index);
				return;
			}
		}
	}
	
	private MessagePane messagePaneFactory(Message m){
		GridPane gridPane = new GridPane();
		
		Label title = new Label();
		title.textProperty().bind(m.getTitleProperty());
		GridPane.setRowIndex(title, 0);
		GridPane.setColumnIndex(title, 0);
		Label content = new Label();
		content.textProperty().bind(m.getContentProperty());
		GridPane.setRowIndex(content, 1);
		GridPane.setColumnIndex(content, 0);
		Label date = new Label();
		date.textProperty().bind(new SimpleStringProperty("Data dodania: " + m.getDate().toString()));
		GridPane.setRowIndex(date, 3);
		GridPane.setColumnIndex(date, 0);
		Label author = new Label();
		StringExpression expression = (m.getUser().getEmployee().getFirstNameProperty().concat(" ")).concat(m.getUser().getEmployee().getLastNameProperty());
		author.textProperty().bind(expression);
		GridPane.setRowIndex(author, 2);
		GridPane.setColumnIndex(author, 0);
		
		//Customize text in labels
		title.setFont(javafx.scene.text.Font.font("Verdana", FontWeight.BOLD, 17));
		author.setFont(javafx.scene.text.Font.font("Verdana", FontWeight.BOLD, 12));
		content.setFont(javafx.scene.text.Font.font("Verdana", FontWeight.MEDIUM, 14));
		
		title.setPadding(new Insets(15, 0, 30, 0));
		author.setPadding(new Insets(15,0,0,30));
		date.setPadding(new Insets(2,15,0,30));
		
		//Constraints for rows
		RowConstraints row1 = new RowConstraints();
        row1.setMaxHeight(50);
        row1.setMinHeight(35);
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        row3.setMaxHeight(50);
        row3.setMinHeight(35);

        gridPane.getRowConstraints().addAll(row1, row2, row3);
		gridPane.getChildren().addAll(date, content, author,title);
		
		TitledPane tp = new TitledPane("", gridPane);
		tp.getStyleClass().add("messages");
		
		
		Label userNameLabel = new Label();
		userNameLabel.textProperty().bind((m.getUser().getEmployee().getFirstNameProperty().concat(" ")).concat(m.getUser().getEmployee().getLastNameProperty()));
		userNameLabel.setFont(javafx.scene.text.Font.font("System", FontWeight.NORMAL, 14));
		userNameLabel.setMinWidth(700);

		
		Label titleLabel = new Label();
		titleLabel.textProperty().bind(m.getTitleProperty());
		titleLabel.setFont(javafx.scene.text.Font.font("System", FontWeight.LIGHT, 12));
		titleLabel.setMinWidth(600);
		
		Label dateLabel = new Label();
		dateLabel.textProperty().bind(new SimpleStringProperty(m.getDate().toString()));
		dateLabel.setFont(javafx.scene.text.Font.font("System", FontWeight.NORMAL, 12));
		
		
		userNameLabel.getStyleClass().add("messageLabel");
		titleLabel.getStyleClass().add("messageLabel");
		dateLabel.getStyleClass().add("messageLabel");
		
		Node node = new HBox(userNameLabel, titleLabel, dateLabel);
		
		tp.setUserData(m);
		tp.graphicProperty().set(node);
		tp.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		
		MessagePane mp = new MessagePane(tp, m);
		return mp;
	}
	 

	 
	public void getMessages(){
		 addPaginationButtons();
	}
	   
    @FXML
    private void handleNewClick(){
    	showNewMessageOverview(null);
    }
    
    @FXML
    private void newEditClick(){
    	if(groupACC.getExpandedPane() != null){
    	
    	TitledPane expPane = groupACC.getExpandedPane();
    	
    	int messageID = getMessageAssociatedWithPane(expPane).getMessageID();	
    	Message message = mainApp.getController().getMessageByID(messageID, messageData);
    	
    	if(message != null){
    		//if a message is provided show new window with EDIT options
    		showNewMessageOverview(message);
    	} else {
    		//no message has been found, show an alert to inform the user that an error occurred
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Brak takiej wiadomoœci");
			alert.setContentText("Brak takiej wiadomoœci w programie");
			alert.showAndWait();
    	}
    	
    	} else {
    		//no message selected, show an alert  
    		Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("B³¹d");
			alert.setHeaderText("Brak zaznaczonej wiadomoœci");
			alert.setContentText("Proszê zaznaczyæ wiadomoœæ przeznaczon¹ do edycji");
			alert.showAndWait();
    	}
    }
    
    private void showNewMessageOverview(Message message){
    	//User clicked "new" button
    	try{
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(MainApp.class.getResource("view/addNewDialogs/NewMessageOverview.fxml"));
    	AnchorPane messagePane = (AnchorPane) loader.load();
    	
    	dialogStage = new Stage();
    	dialogStage.setTitle("Opcje wiadomoœci");
    	dialogStage.initOwner(currentStage);
    	dialogStage.getIcons().add(mainApp.getProgramIcon());
    	dialogStage.initModality(Modality.WINDOW_MODAL);
    	
    	Scene scene = new Scene(messagePane);
    	dialogStage.setScene(scene);
    	
    	NewMessageOverviewController controller = loader.getController();
    	controller.setMainApp(mainApp);
    	controller.setCurrentStage(dialogStage);
    	controller.setMessage(message);
    	dialogStage.showAndWait();
    	
    	if(controller.getMessage() != null){
    		Message m = controller.getMessage();
    		addMessageAtFirstIndex(m);
    		
    	}
    	
    	}
    	catch(IOException e){
    		e.printStackTrace();
    	}
    	
    }
    
    private void parallelTransition(Node node){
		node.setRotate(0);
		RotateTransition rotateTransition = 
		           new RotateTransition(Duration.millis(1000), node);
		rotateTransition.setByAngle(360f);
		rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
		rotateTransition.setAutoReverse(false);
		
		 ScaleTransition scaleTransition = 
		            new ScaleTransition(Duration.millis(2000), node);
		scaleTransition.setFromX(0.8);
		scaleTransition.setFromY(0.8);
		scaleTransition.setToX(1.35);
		scaleTransition.setToY(1.35);
		scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
		scaleTransition.setAutoReverse(true);
		FadeTransition ft = new FadeTransition(Duration.millis(3500), node);
		ft.setCycleCount(FadeTransition.INDEFINITE);
		ft.setAutoReverse(true);
		 
		ft.setFromValue(1.0);
		ft.setToValue(0.15);
		
		
		parallelTransition = new ParallelTransition();
	    parallelTransition.getChildren().addAll(
	                ft, rotateTransition, scaleTransition
	        );
	    parallelTransition.setCycleCount(ParallelTransition.INDEFINITE);
	    parallelTransition.play();	
	}
    
    private void stopTheTransition(Node node){
    	node.setRotate(0);
    	node.setOpacity(1.0);
    	node.setScaleX(1);
    	node.setScaleY(1);
    	parallelTransition.stop();
    }
  
    /**
     * Inner class used for storing the titled panes and messages
     * @author Piotrek
     *
     */
    public class MessagePane {
		private TitledPane messagePane;
    	private Message message;
    	
    	public MessagePane(TitledPane messagePane, Message message){
    		this.messagePane = messagePane;
    		this.message = message;
    		
    		this.messagePane.setOnDragDetected(new EventHandler<MouseEvent>(){

				@Override
				public void handle(MouseEvent event) {
					if(mainApp.getController().getLoggedUser().equalsOtherUser(message.getUser())){
						Dragboard db = messagePane.startDragAndDrop(TransferMode.ANY);
						ClipboardContent content = new ClipboardContent();
						content.putString(String.valueOf(message.getMessageID()));
						db.setContent(content);
						parallelTransition(binImageView);
						event.consume();
					} 
				}
            	
            });
    		
    		this.messagePane.setOnDragDone(new EventHandler<DragEvent>(){

				@Override
				public void handle(DragEvent arg0) {
					stopTheTransition(binImageView);
					
				}
    			
    		});
    		
    		
    	}
    	
    	public TitledPane getMessagePane(){
    		return this.messagePane;
    	}
    	
    	public Message getMessage(){
    		return this.message;
    	}
    	
 
    }
    
    /**
     * Finds the message associated with given TitledPane
     * @param Pane
     * @return
     */
   	public Message getMessageAssociatedWithPane(TitledPane Pane){
		for(MessagePane m : messagePanes){
			if(m.getMessagePane() == Pane) return m.getMessage();
		}
		return null;
	}
   	
   	public ObservableList<Message> getMessageData(){
   		return this.messageData;
   	}
   	
   	public ObservableList<MessagePane> getMessagePanes(){
   		return this.messagePanes;
   	}
 
}