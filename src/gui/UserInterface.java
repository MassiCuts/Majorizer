package gui;

import java.io.IOException;
import java.util.Optional;

import framework.Course;
import framework.Student;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import utils.ResourceLoader;

public class UserInterface extends Application{

	private Scene scene = null;
	
	public void updateUI()	{
		
		BorderPane root = null;
		
		
		//Login Screen
//		root = loginScreen();
		
		//Student View
		root = studentView();

		
		if(scene == null)
			scene = new Scene(root);
		else
			scene.setRoot(root);
	}
	
	
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

	public void addToCurrentSelectedSemester(Student student, Course course)	{
		String courseName = course.getCourseName();
	}

	public void loginPressed(ActionEvent action) {
		System.out.println("Here");
	}
	
	//REUSABLE ELEMENTS
	public Button newRemoveButton()	{
		Button removeButton = new Button();
		removeButton.setShape(new Circle(2));
		ImageView redMinusMark = null;
		try {
			redMinusMark = new ImageView(ResourceLoader.getImage("minus-512.png"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		removeButton.setGraphic(redMinusMark);
		redMinusMark.setFitWidth(10);
		redMinusMark.setFitHeight(25);
		removeButton.getStyleClass().add("redbuttontheme");

		return removeButton;
	}
	


	
	
	public BorderPane loginScreen()	{
		BorderPane loginScreen = new BorderPane();
		try	{
			loginScreen.getStyleClass().add("goldgradient");
			
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
			loginButtonBox.setAlignment(Pos.CENTER);
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
	    	 
	    	
	    	loginButton.setOnAction(this::loginPressed);
//	    			new EventHandler<ActionEvent>()	{
//	    		@Override
//	    		public void handle(ActionEvent ae)	{
//	    			System.out.println(username.getText() + '\n' + password.getText());
//	    		}
//	    	});
	    	
	    	
	    	VBox logoBox = new VBox();
	    	ImageView logoView = new ImageView();
	    	Image logo = ResourceLoader.getImage("logoGreen2.png");
	    	logoView.setPreserveRatio(true);
	    	logoView.setFitWidth(600);
	    	logoView.setImage(logo);
	    	logoBox.getChildren().add(logoView);
	    	logoBox.setMaxHeight(100);
	    	logoBox.setPadding(new Insets(20));
	    	
			loginScreen.setCenter(login);
			loginScreen.setTop(logoBox);
			
			loginScreen.setMargin(login, new Insets(100));

			
		}	catch( IOException ioe)	{
			ioe.printStackTrace();
		}
		return loginScreen;
	}
	
	public BorderPane studentView()	{
		BorderPane studentScreen = new BorderPane();
		try	{
			studentScreen.getStyleClass().add("lightgraytheme");
			
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
			logoutButton.setOnAction((ae) -> {
				Platform.exit();
			});
			
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
			for(int columnIndex = 0; columnIndex <= 7; ++columnIndex)	{
				semester[columnIndex] = new GridPane();
				if(columnIndex < getCurrentSem())
					semester[columnIndex].getStyleClass().add("pastSem");
				else if(columnIndex == getCurrentSem())
					semester[columnIndex].getStyleClass().add("currentSem");
				else if(columnIndex > getCurrentSem())
					semester[columnIndex].getStyleClass().add("futureSem");
				
				semester[columnIndex].setMinSize(100, 170);
				
				schedulePane.add(semester[columnIndex], columnIndex, 1);
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
			
			//Green Plus Button for Majors and Minors
			Button greenPlusForMajorsAndMinorsButton = new Button();
			greenPlusForMajorsAndMinorsButton.setShape(new Circle(2));
			ImageView greenPlusMark = new ImageView(ResourceLoader.getImage("NEWgreenPlus.png"));
			greenPlusForMajorsAndMinorsButton.setGraphic(greenPlusMark);
			greenPlusMark.setFitWidth(30);
			greenPlusMark.setFitHeight(20);
			greenPlusForMajorsAndMinorsButton.getStyleClass().add("greenbuttontheme");
			
			VBox greenPlusButtonForMajorsAndMinorsBox = new VBox();
			greenPlusButtonForMajorsAndMinorsBox.getChildren().add(greenPlusForMajorsAndMinorsButton);
			greenPlusButtonForMajorsAndMinorsBox.setAlignment(Pos.CENTER_LEFT);
			greenPlusButtonForMajorsAndMinorsBox.setPadding(new Insets(5));
			
			majorsAndMinorsPane.add(greenPlusButtonForMajorsAndMinorsBox, 1, 0);
			
			GridPane majorsAndMinorsTab = new GridPane();
//			majorsAndMinorsTab.setVgap(5);
			majorsAndMinorsTab.getStyleClass().add("windows");
			majorsAndMinorsTab.setMinSize(180, 200);		
			
			majorsAndMinorsPane.add(majorsAndMinorsTab, 0, 1);
			
			//SAMPLE DATA
			majorsAndMinorsTab.add(new Label("Computer Science Major"), 0, 0);
			majorsAndMinorsTab.add(new Label("Computer Engineering Major"), 0, 1);
			
			//Red Minus Button for Major#1
			Button redMinusForMajorButton = newRemoveButton();
			VBox redMinusButtonForMajorBox = new VBox();
			redMinusButtonForMajorBox.getChildren().add(redMinusForMajorButton);
			redMinusButtonForMajorBox.setAlignment(Pos.CENTER_RIGHT);
			redMinusButtonForMajorBox.setPadding(new Insets(5));
			
			majorsAndMinorsTab.add(redMinusButtonForMajorBox, 1, 0);
			
			//Red Minus Button for Major#2
			Button redMinusForMajor2Button = newRemoveButton();
			
			VBox redMinusButtonForMajor2Box = new VBox();
			redMinusButtonForMajor2Box.getChildren().add(redMinusForMajorButton);
			redMinusButtonForMajor2Box.setAlignment(Pos.CENTER_RIGHT);
			redMinusButtonForMajor2Box.setPadding(new Insets(5));
			
			
			
			majorsAndMinorsTab.add(redMinusButtonForMajor2Box, 1, 1);
			
			//----------------------
			//Slider for Majors And Minors Tab
			Slider majorsAndMinorsSlider = new Slider();
			majorsAndMinorsSlider.setOrientation(Orientation.VERTICAL);
			//WORKING HERE
			
			windowsPane.add(majorsAndMinorsPane, 0, 1);
			
			//Current Selected Semester Pane
			GridPane currentSelectedSemesterPane = new GridPane();
			
			//Header label for Current Selected Semester
			Label headerForCurrentSelectedSemester = new Label();
			headerForCurrentSelectedSemester.setText("Current Selected Semester");
			headerForCurrentSelectedSemester.getStyleClass().add("fontmed");
			currentSelectedSemesterPane.add(headerForCurrentSelectedSemester, 0, 0);
			
			//Green Plus Button for Current Selected Semester
			Button greenPlusForCurrentSelectedSemesterButton = new Button();
			greenPlusForCurrentSelectedSemesterButton.setShape(new Circle(2));
			ImageView greenPlusForCSSMark = new ImageView(ResourceLoader.getImage("NEWgreenPlus.png"));
			greenPlusForCurrentSelectedSemesterButton.setGraphic(greenPlusForCSSMark);
			greenPlusForCSSMark.setFitWidth(30);
			greenPlusForCSSMark.setFitHeight(20);
			greenPlusForCurrentSelectedSemesterButton.getStyleClass().add("greenbuttontheme");
			
			VBox greenPlusButtonForCurrentSelectedSemesterBox = new VBox();
			greenPlusButtonForCurrentSelectedSemesterBox.getChildren().add(greenPlusForCurrentSelectedSemesterButton);
			greenPlusButtonForCurrentSelectedSemesterBox.setAlignment(Pos.CENTER_LEFT);
			greenPlusButtonForCurrentSelectedSemesterBox.setPadding(new Insets(5));
			
			currentSelectedSemesterPane.add(greenPlusButtonForCurrentSelectedSemesterBox, 1, 0);
			
			GridPane currentSelectedSemesterTab = new GridPane();
			currentSelectedSemesterTab.setHgap(5);
			currentSelectedSemesterTab.setPadding(new Insets(5));
			currentSelectedSemesterTab.getStyleClass().add("windows");
			currentSelectedSemesterTab.setMinSize(180, 200);
			
			currentSelectedSemesterPane.add(currentSelectedSemesterTab, 0, 1);

			windowsPane.add(currentSelectedSemesterPane, 1, 1);
			
			//SAMPLE DATA
			
			currentSelectedSemesterTab.add(new Label("CS350	Software Design and Development"), 0, 0);
			currentSelectedSemesterTab.add(new Label("EE262	Introduction to Object Oriented Programming"), 0, 1);
			currentSelectedSemesterTab.add(new Label("EE341	Microelectronics"), 0, 2);
			currentSelectedSemesterTab.add(new Label("EE321	Signals and Systems"), 0, 3);
			currentSelectedSemesterTab.add(new Label("EE365	Advanced Digital Circuit Design"), 0, 4);
			currentSelectedSemesterTab.add(new Label("MA339	Applied Linear Algebra"), 0, 5);
			
			
			//Red Minus Buttons for Course
			int numberOfCourses = 6;																//TEMP
			Button removeCourseButton[] = new Button[numberOfCourses];
			for(int rowIndex = 0; rowIndex < numberOfCourses; ++rowIndex)	{
				removeCourseButton[rowIndex] = newRemoveButton();
				currentSelectedSemesterTab.add(removeCourseButton[rowIndex], 1, rowIndex);
			}
			
			
			
			//Electives Pane
			GridPane electivesPane = new GridPane();
			
			//Header label for Electives
			Label headerForElectives = new Label();
			headerForElectives.setText("Electives");
			headerForElectives.getStyleClass().add("fontmed");
			electivesPane.add(headerForElectives, 0, 0);
			
			GridPane electivesTab = new GridPane();
			electivesTab.getStyleClass().add("windows");
			electivesTab.setMinSize(180, 200);
			
			electivesTab.add(new Label("CS470	Deep Learning"), 0, 0);
			
			//Green Plus Button for ElectiveCourse
			Button greenPlusForElectiveCourseButton = new Button();
			greenPlusForElectiveCourseButton.setShape(new Circle(2));
			ImageView greenPlusForElectiveCourseMark = new ImageView(ResourceLoader.getImage("NEWgreenPlus.png"));
			greenPlusForElectiveCourseButton.setGraphic(greenPlusForElectiveCourseMark);
			greenPlusForElectiveCourseMark.setFitWidth(30);
			greenPlusForElectiveCourseMark.setFitHeight(20);
			greenPlusForElectiveCourseButton.getStyleClass().add("greenbuttontheme");
			
			VBox greenPlusButtonForElectiveCourseBox = new VBox();
			greenPlusButtonForElectiveCourseBox.getChildren().add(greenPlusForElectiveCourseButton);
			greenPlusButtonForElectiveCourseBox.setAlignment(Pos.CENTER_LEFT);
			greenPlusButtonForElectiveCourseBox.setPadding(new Insets(5));
			
			electivesTab.add(greenPlusButtonForElectiveCourseBox, 1, 0);
			
			electivesPane.add(electivesTab, 0, 1);

			windowsPane.add(electivesPane, 2, 1);
			
			
			//POST windows Pane for AddCourses and AddMajorOrMinor
			GridPane postWindowsPane = new GridPane();
			
			//Add Courses Pane
			GridPane addCoursesPane = new GridPane();
			
			//Header label for Add Courses
			Label headerForAddCourses = new Label();
			headerForAddCourses.setText("Add Courses");
			headerForAddCourses.getStyleClass().add("fontmed");
			addCoursesPane.add(headerForAddCourses, 0, 0);
			
			//Text box for Searching
			TextField searchField = new TextField();
			searchField.setPromptText("Course-ID");
			searchField.getStyleClass().add("lightgraytheme");
			//searchField.getStyleClass().add("boxoutline");
			
			addCoursesPane.add(searchField, 1, 0);
			
			GridPane addCoursesTab = new GridPane();
			addCoursesTab.getStyleClass().add("windows");
			addCoursesTab.setMinSize(60, 60);
			
			addCoursesTab.add(new Label("CS141	Introduction to Computer Science I"), 0, 0);
			
			//Green Plus Button for addCoursesTab
			Button greenPlusForAddCoursesButton = new Button();
			greenPlusForAddCoursesButton.setShape(new Circle(2));
			ImageView greenPlusForAddCoursesMark = new ImageView(ResourceLoader.getImage("NEWgreenPlus.png"));
			greenPlusForAddCoursesButton.setGraphic(greenPlusForAddCoursesMark);
			greenPlusForAddCoursesMark.setFitWidth(30);
			greenPlusForAddCoursesMark.setFitHeight(20);
			greenPlusForAddCoursesButton.getStyleClass().add("greenbuttontheme");
			
			VBox greenPlusButtonForAddCoursesBox = new VBox();
			greenPlusButtonForAddCoursesBox.getChildren().add(greenPlusForAddCoursesButton);
			greenPlusButtonForAddCoursesBox.setAlignment(Pos.CENTER_LEFT);
			greenPlusButtonForAddCoursesBox.setPadding(new Insets(5));
			
			addCoursesTab.add(greenPlusButtonForAddCoursesBox, 1, 0);
			
			addCoursesPane.add(addCoursesTab, 0, 1);
			
			postWindowsPane.add(addCoursesPane, 0, 0);
			
			
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
			orgPane.add(postWindowsPane, 0, 500);
			orgPane.add(bottomPane, 1000, 1000);
						
			studentScreen.setTop(orgPane);


		}	catch( Exception e )	{
			e.printStackTrace();
		}
		
		return studentScreen;
	}
	
	@Override
	public void start(Stage primaryStage)	{
		try	{		
		
			updateUI();

			
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
