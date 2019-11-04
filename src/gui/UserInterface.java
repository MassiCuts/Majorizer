package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class UserInterface extends Application{
	@Override
	public void start(Stage primaryStage)	{
		try	{
			BorderPane root = new BorderPane();
			root.getStyleClass().add("goldgradient");
			
			VBox login = new VBox();
			login.getStyleClass().add("roundedRect");
			
			login.setMaxSize(400, 300);
			
			VBox titleBox = new VBox();
			Label title = new Label("Majorizer");
			titleBox.getChildren().add(title);
			title.getStyleClass().add("standardfont");
			titleBox.setAlignment(Pos.CENTER);
			titleBox.setPadding(new Insets(10, 50, 10, 50));
			
			GridPane loginGrid = new GridPane();
			
			TextField username = new TextField();
			username.setPromptText("Username");
			username.getStyleClass().add("lightgraytheme");
			username.getStyleClass().add("fx-border-color: #000000");		//some sort of border??
			TextField password = new PasswordField();
			password.setPromptText("Password");
			password.getStyleClass().add("lightgraytheme");
			
			
			Button loginButton = new Button();
			loginButton.setShape(new Circle(2));
			ImageView loginButtonMark = new ImageView();
			Polyline check = new Polyline();
			check.getPoints().addAll(new Double[]	{200.0, 50.0, 400.0, 50.0});
			loginButton.setGraphic(loginButtonMark);
			loginButton.getStyleClass().add("savebuttontheme");
			loginButton.setDefaultButton(true);
			
			VBox buttonBox = new VBox();
			buttonBox.getChildren().add(loginButton);
			buttonBox.setAlignment(Pos.CENTER_RIGHT);
			buttonBox.setPadding(new Insets(5));
			
			loginGrid.add(new Label("Username"), 0, 1);
			loginGrid.add(username, 1, 1);
			loginGrid.add(new Label("Password"), 0, 2);
			loginGrid.add(password, 1, 2);
			loginGrid.add(buttonBox, 1, 3);
			loginGrid.setAlignment(Pos.CENTER);
			
			loginGrid.setPadding(new Insets(20, 20, 20, 20));
			loginGrid.setHgap(5);
			
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
	    	Image logo = new Image("https://www.clarkson.edu/themes/custom/cu_2019/logo.png");
	    	logoView.setImage(logo);
	    	logoBox.getChildren().add(logoView);
	    	logoBox.setMaxHeight(100);
	    	logoBox.setPadding(new Insets(20));
	    	
	    	
			root.setCenter(login);
			root.setTop(logoBox);
			
			
			root.setMargin(login, new Insets(150));
						
			Scene scene = new Scene(root,800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Majorizer");
			primaryStage.getIcons().add(new Image("https://www.clarkson.edu/themes/custom/cu_2019/favicon.ico"));
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
