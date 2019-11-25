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

	//TESTING ONLY
	public String getName()	{
		return "Lorenzo Villani";
	}
	//TESTING ONLY
	public String getStudentID()	{
		return "0755050";
	}
	//TESTING ONLY
	public int getCurrentSem()	{
		return 4;
	}
	
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
			title.getStyleClass().add("font");
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
			
			
			//Login Button
			Button loginButton = new Button();
			loginButton.setShape(new Rectangle());
			loginButton.setText("Login");
			loginButton.getStyleClass().add("logoutbuttontheme");
			
			loginButton.setDefaultButton(true);
			
			VBox loginButtonBox = new VBox();
			loginButtonBox.getChildren().add(loginButton);
			loginButtonBox.setAlignment(Pos.CENTER_RIGHT);
			loginButtonBox.setPadding(new Insets(5));
			loginButtonBox.getStyleClass().add("logoutbuttontheme");
			
			loginGrid.add(ulabel, 0, 1);
			loginGrid.add(username, 1, 1);
			loginGrid.add(plabel, 0, 2);
			loginGrid.add(password, 1, 2);
			loginGrid.add(loginButtonBox, 1, 3);
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
			
			
			root.setMargin(login, new Insets(100));
			
		}	catch( IOException ioe)	{
			ioe.printStackTrace();
		}
		return root;
	}
	
	public BorderPane studentView()	{
		BorderPane root = new BorderPane();
		try	{
			root.getStyleClass().add("lightgraytheme");
			
			//Pane for Organization
			GridPane orgPane = new GridPane();
			
			//Pre-Top Pane for Logout
			GridPane preTopPane = new GridPane();
			
			//Top Pane
			GridPane topPane = new GridPane();
			//Name
			Label name = new Label();
			name.setText(getName());				//This will get the name from Majorizer eventually
			name.getStyleClass().add("fonttitle");
			//Student ID
			Label studentID = new Label();
			studentID.setText(getStudentID()); 		//Likewise ^^
			studentID.getStyleClass().add("IDfont");
			//Integrate
			topPane.add(name, 0, 0);
			topPane.add(studentID, 0, 1);
			
			//Logout Button
			Button logoutButton = new Button();
			logoutButton.setShape(new Rectangle());
			//ImageView logoutButtonMark = new ImageView(ResourceLoader.getImage("logoutMark.png"));
			//logoutButton.setGraphic(logoutButtonMark);
			logoutButton.setText("Logout");
			logoutButton.getStyleClass().add("logoutbuttontheme");
			//logoutButton.setDefaultButton(true);
			
			VBox logoutButtonBox = new VBox();
			logoutButtonBox.getChildren().add(logoutButton);
			logoutButtonBox.setAlignment(Pos.TOP_RIGHT);
			logoutButtonBox.setPadding(new Insets(5));
			logoutButtonBox.getStyleClass().add("logoutbuttontheme");
						
			preTopPane.add(logoutButtonBox, 100, 0);
			
			//Schedule Pane
			GridPane schedulePane = new GridPane();
			
			//Header label for Schedule Pane
			Label header = new Label();
			header.setText("Course Schedule");
			header.getStyleClass().add("fonttitle");
			schedulePane.add(header, 0, 0);
			
			GridPane semester[] = new GridPane[8];
			for(int i = 0; i <= 7; ++i)	{
				semester[i] = new GridPane();
				if(i < getCurrentSem())
					semester[i].getStyleClass().add("pastSem");
				else if(i == getCurrentSem())
					semester[i].getStyleClass().add("currentSem");
				else if(i > getCurrentSem())
					semester[i].getStyleClass().add("futureSem");
				
				semester[i].setMinSize(100, 180);
			}
						
			
			for(int i = 0; i <= 7; ++i)	{
				schedulePane.add(semester[i], i, 1);
			}
									
			//Action Pane
			GridPane actionPane = new GridPane();
			
			//Left Arrow
			Button leftArrowButton = new Button();
			leftArrowButton.setShape(new Circle(2));
			ImageView leftArrowMark = new ImageView(ResourceLoader.getImage("leftArrow.png"));
			leftArrowButton.setGraphic(leftArrowMark);
			leftArrowMark.setFitWidth(20);
			leftArrowMark.setFitHeight(20);
			leftArrowButton.getStyleClass().add("arrowbuttontheme");
			
			VBox leftArrowButtonBox = new VBox();
			leftArrowButtonBox.getChildren().add(leftArrowButton);
			leftArrowButtonBox.setAlignment(Pos.CENTER_LEFT);
			leftArrowButtonBox.setPadding(new Insets(5));
			
			//Adding left arrow to the actionPane
			actionPane.add(leftArrowButtonBox, 0, 0);
			
			//Right Arrow
			Button rightArrowButton = new Button();
			rightArrowButton.setShape(new Circle(2));
			ImageView rightArrowMark = new ImageView(ResourceLoader.getImage("rightArrow.png"));
			rightArrowButton.setGraphic(rightArrowMark);
			rightArrowMark.setFitWidth(20);
			rightArrowMark.setFitHeight(20);
			rightArrowButton.getStyleClass().add("arrowbuttontheme");
			
			VBox rightArrowButtonBox = new VBox();
			rightArrowButtonBox.getChildren().add(rightArrowButton);
			rightArrowButtonBox.setAlignment(Pos.CENTER_LEFT);
			rightArrowButtonBox.setPadding(new Insets(5));
			
			//Adding right arrow to the actionPane
			actionPane.add(rightArrowButtonBox, 1, 0);
			
			//Windows Pane
			GridPane windowsPane = new GridPane();
			
			//Majors and Minors Pane
			GridPane majorsAndMinorsPane = new GridPane();
			
			//Header label for Majors and Minors
			Label headerForMajorsAndMinors = new Label();
			headerForMajorsAndMinors.setText("Majors and Minors");
			headerForMajorsAndMinors.getStyleClass().add("fontmed");
			majorsAndMinorsPane.add(headerForMajorsAndMinors, 0, 0);
			
			GridPane majorsAndMinorsTab = new GridPane();
			majorsAndMinorsTab.getStyleClass().add("windows");
			majorsAndMinorsTab.setMinSize(180, 260);		
			
			majorsAndMinorsPane.add(majorsAndMinorsTab, 0, 1);
			
			windowsPane.add(majorsAndMinorsPane, 0, 1);
		
			//Current Selected Semester Pane
			GridPane currentSelectedSemesterPane = new GridPane();
			
			//Header label for Majors and Minors
			Label headerForCurrentSelectedSemester = new Label();
			headerForCurrentSelectedSemester.setText("Current Selected Semester");
			headerForCurrentSelectedSemester.getStyleClass().add("fontmed");
			currentSelectedSemesterPane.add(headerForCurrentSelectedSemester, 0, 0);
			
			GridPane currentSelectedSemesterTab = new GridPane();
			currentSelectedSemesterTab.getStyleClass().add("windows");
			currentSelectedSemesterTab.setMinSize(180, 260);
			
			currentSelectedSemesterPane.add(currentSelectedSemesterTab, 0, 1);

			windowsPane.add(currentSelectedSemesterPane, 1, 1);
			
			//Electives Pane
			GridPane electivesPane = new GridPane();
			
			//Header label for Majors and Minors
			Label headerForElectives = new Label();
			headerForElectives.setText("Electives");
			headerForElectives.getStyleClass().add("fontmed");
			electivesPane.add(headerForElectives, 0, 0);
			
			GridPane electivesTab = new GridPane();
			electivesTab.getStyleClass().add("windows");
			electivesTab.setMinSize(180, 260);
			
			electivesPane.add(electivesTab, 0, 1);

			windowsPane.add(electivesPane, 2, 1);
			
			//Bottom Pane
			GridPane bottomPane = new GridPane();
			
			//Check Button
			Button checkButton = new Button();
			checkButton.setShape(new Circle(2));
			ImageView checkButtonMark = new ImageView(ResourceLoader.getImage("checkmark30.png"));
			checkButton.setGraphic(checkButtonMark);
			checkButtonMark.setFitWidth(40);
			checkButtonMark.setFitHeight(40);
			checkButton.getStyleClass().add("checkbuttontheme");
			//checkButton.setDefaultButton(true);
			
			VBox checkButtonBox = new VBox();
			checkButtonBox.getChildren().add(checkButton);
			checkButtonBox.setAlignment(Pos.CENTER_RIGHT);
			checkButtonBox.setPadding(new Insets(5));
			
			bottomPane.add(checkButtonBox, 0, 100);
			
			orgPane.add(preTopPane, 1000, 0);
			orgPane.add(topPane, 0, 0);
			orgPane.add(schedulePane, 0, 1);
			orgPane.add(actionPane, 0, 2);
			orgPane.add(windowsPane, 0, 10);
			orgPane.add(bottomPane, 1000, 5000);
						
			root.setTop(orgPane);

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
			root = studentView();
			
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
