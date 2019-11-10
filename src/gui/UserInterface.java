package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import utils.ResourceLoader;

public class UserInterface extends Application{
	public BorderPane loginScreen()	{
		BorderPane root = new BorderPane();
		try	{
			root.getStyleClass().add("goldgradient");
			
			VBox login = new VBox();
			login.getStyleClass().add("roundedRect");
			
			login.setMaxSize(400, 300);
			
			VBox titleBox = new VBox();
			Label title = new Label("Majorizer");
			titleBox.getChildren().add(title);
			title.getStyleClass().add("titlefont");
			titleBox.setAlignment(Pos.CENTER);
			titleBox.setPadding(new Insets(10, 50, 10, 50));
			
			GridPane loginGrid = new GridPane();
			
			TextField username = new TextField();
			username.setPromptText("Username");
			username.getStyleClass().add("lightgraytheme");
			username.getStyleClass().add("boxoutline");
			TextField password = new PasswordField();
			password.setPromptText("Password");
			password.getStyleClass().add("lightgraytheme");
			password.getStyleClass().add("boxoutline");
			Label ulabel = new Label("Username");
			Label plabel = new Label("Password");
			ulabel.getStyleClass().add("lightgraytheme");
			plabel.getStyleClass().add("lightgraytheme");
			
			
			
			Button loginButton = new Button();
			loginButton.setShape(new Circle(2));
			ImageView loginButtonMark = new ImageView(ResourceLoader.getImage("checkmark30.png"));
			loginButton.setGraphic(loginButtonMark);
			loginButtonMark.setFitWidth(25);
			loginButtonMark.setFitHeight(25);
			loginButton.getStyleClass().add("savebuttontheme");
			loginButton.setDefaultButton(true);
			
			VBox buttonBox = new VBox();
			buttonBox.getChildren().add(loginButton);
			buttonBox.setAlignment(Pos.CENTER_RIGHT);
			buttonBox.setPadding(new Insets(5));
			
			loginGrid.add(ulabel, 0, 1);
			loginGrid.add(username, 1, 1);
			loginGrid.add(plabel, 0, 2);
			loginGrid.add(password, 1, 2);
			loginGrid.add(buttonBox, 1, 3);
			loginGrid.setAlignment(Pos.CENTER);
			
			loginGrid.setPadding(new Insets(20, 20, 20, 20));
			loginGrid.setHgap(5);
			loginGrid.setVgap(1);
			
			login.getChildren().add(titleBox);
			login.getChildren().add(loginGrid);
		
			
	    	 Platform.runLater(() -> {
	    	        if (!username.isFocused()) {
	    	            username.requestFocus();
	    	        }
	    	    });
	    	 
	    	
	    	loginButton.setOnAction(new EventHandler<ActionEvent>()	{
	    		@Override
	    		public void handle(ActionEvent ae)	{
	    			System.out.println(username.getText() + '\n' + password.getText());
	    		}
	    	});
	    	
	    	
	    	VBox logoBox = new VBox();
	    	ImageView logoView = new ImageView();
	    	Image logo = ResourceLoader.getImage("logoGreen2.png");
	    	logoView.setPreserveRatio(true);
	    	logoView.setFitWidth(600);
	    	logoView.setImage(logo);
	    	logoBox.getChildren().add(logoView);
	    	logoBox.setMaxHeight(100);
	    	logoBox.setPadding(new Insets(20));
	    	
			root.setCenter(login);
			root.setTop(logoBox);
			
			
			root.setMargin(login, new Insets(150));
			
		}	catch( IOException ioe)	{
			ioe.printStackTrace();
		}
		return root;
	}
	
	public BorderPane studentView()	{
		BorderPane root = new BorderPane();
		try	{
			root.getStyleClass().add("lightgraytheme");
			
			
		}	catch( Exception e )	{
			e.printStackTrace();
		}
		
		return root;
	}
	
	@Override
	public void start(Stage primaryStage)	{
		try	{
			BorderPane root = new BorderPane();
			//Login Screen
//			root = loginScreen();
			
			//Student View
//			root = studentView();
			
			Scene scene = new Scene(root, 1000, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Majorizer");
			primaryStage.getIcons().add(ResourceLoader.getImage("favicon.png"));
			primaryStage.show();
			primaryStage.setMaximized(true);
		}	catch(Exception e)	{
			e.printStackTrace();
		}
	}
		
	public static void main(String[] args) {
		launch(args);
	}
}