package app.view;

import app.MainApp;
import app.model.Permission;
import app.model.User;
import app.view.inheritance.DialogBasedController;
import javafx.beans.binding.StringExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PermissionOverviewController extends DialogBasedController {


	@FXML
	private ImageView blockedAccountImageView;
	@FXML
	private ImageView mayEditImageView;
	@FXML
	private ImageView mayInsertDataImageView;
	@FXML
	private ImageView mayExecuteQueriesImageView;
	@FXML
	private ImageView hasAdminRightsImageView;

	private Permission permission;

	@FXML
	private void initialize(){
		
	}
	
	@FXML
	private void handleReturnClick(){
		getCurrentStage().close();
	}
	public void setPermission(Permission permission){
		this.permission = permission;
		setLabels();
	}
	

	
	private void setLabels(){
		if(permission.isBlockedAccount()) {
			blockedAccountImageView.setImage(new Image("file:src/app/view/images/dialog icons/check.png"));
		} else {
			blockedAccountImageView.setImage(new Image("file:src/app/view/images/dialog icons/cross.png"));
		}
		
		if(permission.isMayEdit()){
			mayEditImageView.setImage(new Image("file:src/app/view/images/dialog icons/check.png"));
		} else {
			mayEditImageView.setImage(new Image("file:src/app/view/images/dialog icons/cross.png"));
		}
		
		if(permission.isMayInsertData()){
			mayInsertDataImageView.setImage(new Image("file:src/app/view/images/dialog icons/check.png"));
		} else {
			mayInsertDataImageView.setImage(new Image("file:src/app/view/images/dialog icons/cross.png"));
		}
		
		if(permission.isMayExecuteQueries()){
			mayExecuteQueriesImageView.setImage(new Image("file:src/app/view/images/dialog icons/check.png"));
		} else {
			mayExecuteQueriesImageView.setImage(new Image("file:src/app/view/images/dialog icons/cross.png"));
		}
		
		if(permission.isHasAdminRights()){
			hasAdminRightsImageView.setImage(new Image("file:src/app/view/images/dialog icons/check.png"));
		} else {
			hasAdminRightsImageView.setImage(new Image("file:src/app/view/images/dialog icons/cross.png"));
		}
		
		
	}
	

}
