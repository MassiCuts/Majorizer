package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import framework.AcademicPlan;
import framework.Advisor;
import framework.Course;
import framework.DatabaseManager;
import framework.Majorizer;
import framework.Student;
import framework.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import utils.ResourceLoader;

public class UserInterface extends Application{

	private Scene scene = null;
	private final GridPane semesters[] = new GridPane[8];
	Dimension screenSize;
	boolean isAuthenticated = false;
	
	public void updateUI()	{
		
		BorderPane root = null;

		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//Login Screen
		if(!isAuthenticated)
			root = loginScreen();
		else	{
			if(Majorizer.getUser().isUserIsStudent())
				root = studentView();
			else
				root = advisorView();
		}
		
		if(scene == null)
			scene = new Scene(root);
		else
			scene.setRoot(root);
		
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	}
	
	
	public String getName()	{
		return Majorizer.getUser().getFirstName() + ' ' + Majorizer.getUser().getLastName();
	}

	public String getStudentID()	{
		return Majorizer.getUser().getUniversityID();
	}

	public void addToCurrentSelectedSemester(Student student, Course course)	{
		String courseName = course.getCourseName();
	}

	public void loginPressed(ActionEvent action) {
		System.out.println("Here");
	}
	
	public void newSemester(final int semesterNumber, final String css)	{
		GridPane semester = new GridPane();
		semester.setMinSize((screenSize.getWidth()*(2/3.0))/8.0, screenSize.getHeight()/5.0);
		semester.getStyleClass().add(css);
		semester.setStyle("-fx-border-color: black; -fx-border-width: 1px; outline: solid;");
		semester.setOnMouseClicked((me) -> { 
			selectSemester(semesterNumber);
		});
		this.semesters[semesterNumber] = semester;
	}
	
	public void selectSemester(int semesterNumber)	{
		for(int semester = 0; semester <= 7; ++semester)	{
			if(semester == semesterNumber)
				this.semesters[semester].setStyle("-fx-border-color: yellow; -fx-border-width: 30px; outline: solid;");
			else
				this.semesters[semester].setStyle("-fx-border-color: black; -fx-border-width: 1px; outline: solid;");
		}
	}

	//REUSABLE ELEMENTS===========================
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
	
	public Button newAddButton()	{
		Button addButton = new Button();
		addButton.setShape(new Circle(2));
		ImageView greenPlusMark = null;
		try {
			greenPlusMark = new ImageView(ResourceLoader.getImage("NEWgreenPlus.png"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		addButton.setGraphic(greenPlusMark);
		greenPlusMark.setFitWidth(30);
		greenPlusMark.setFitHeight(20);
		addButton.getStyleClass().add("greenbuttontheme");
		return addButton;
	}

	public GridPane newActionGrid()	{
		GridPane actionGrid = new GridPane();
		actionGrid.setHgap(5);
		actionGrid.setPadding(new Insets(5));
		actionGrid.setMinSize(180, 200);		//fields?
		return actionGrid;
	}
	
	public ScrollPane newActionScroll()	{
		ScrollPane actionScroll = new ScrollPane();
		actionScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		actionScroll.setPrefViewportHeight(200);
		actionScroll.getStyleClass().add("windows");
		return actionScroll;
	}
	
	public Label newStyledLabel(String text, String css)	{
		Label styledLabel = new Label(text);
		styledLabel.getStyleClass().add(css);
		return styledLabel;
	}
	
	public Button newLogButton(String text)	{
		Button logButton = new Button();
		logButton.setShape(new Rectangle());
		logButton.setText(text);
		logButton.getStyleClass().add("logoutbuttontheme");
		return logButton;
	}
	//===================================================================
	
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
			Label ulabel = newStyledLabel("Username", "lightgraytheme");
			Label plabel = newStyledLabel("Password", "lightgraytheme");
			
			//Login Button
			Button loginButton = newLogButton("Log In");
			
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
	    	 
	    	
	    	loginButton.setOnAction((ae) -> {
	    		Majorizer.setUser(Majorizer/*DatabaseManager*/.authenticate(username.getText(), password.getText()));
	    		if(Majorizer.getUser() != null)
	    			this.isAuthenticated = true;
	    		this.updateUI();
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
			
			ColumnConstraints constraints = new ColumnConstraints();
			constraints.setHgrow(Priority.ALWAYS);

			GridPane orgPane = new GridPane();
			GridPane topPane = new GridPane();
			GridPane actionPane = new GridPane();
			GridPane searchPane = new GridPane();
			
			Label name = new Label();
			name.setText(getName());
			name.getStyleClass().add("fonttitle");
			
			Label studentID = new Label();
			studentID.setText(getStudentID()); 		//Likewise ^^
			studentID.getStyleClass().add("IDfont");

			//Logout Button
			Button logoutButton = newLogButton("Log Out");
			logoutButton.setOnAction((ae) -> {
				Platform.exit();
			});
			
			VBox logoutButtonBox = new VBox();
			logoutButtonBox.getChildren().add(logoutButton);
			logoutButtonBox.setAlignment(Pos.TOP_RIGHT);
			logoutButtonBox.setPadding(new Insets(5));
			logoutButtonBox.getStyleClass().add("logoutbuttontheme");
			
			
			topPane.add(name, 0, 0);
			topPane.add(studentID, 0, 1);
			topPane.add(logoutButtonBox, 1, 0);
			topPane.setMargin(logoutButtonBox, new Insets(20, 0, 0, 0));
			topPane.getColumnConstraints().add(constraints);
			

			
			//Schedule Pane
			GridPane schedulePane = new GridPane();
			
			//Header label for Schedule Pane
			Label header = newStyledLabel("Course Schedule", "fonttitle");
			schedulePane.add(header, 0, 0);
			
			
			//Schedule
			GridPane scheduleBox = new GridPane();
			for(int columnIndex = 0; columnIndex <= 7; ++columnIndex)	{
				if(columnIndex < Majorizer.getStudentCurrentSemester())
					newSemester(columnIndex, "pastSem");
				else if(columnIndex == Majorizer.getStudentCurrentSemester())
					newSemester(columnIndex, "currentSem");
				else if(columnIndex > Majorizer.getStudentCurrentSemester())
					newSemester(columnIndex, "futureSem");
				
				scheduleBox.add(this.semesters[columnIndex], columnIndex, 1);
			}
			schedulePane.add(scheduleBox, 0, 1);
			
			
			actionPane.setPrefWidth((screenSize.getWidth()*(2/3.0)));
			actionPane.getColumnConstraints().add(constraints);

			
			//Majors and Minors Pane
			GridPane curriculumPane = new GridPane();
			curriculumPane.setMaxWidth(actionPane.getPrefWidth()*(1/3.0));
			
			//Header label for Majors and Minors
			Label curriculumHeader = newStyledLabel("Majors and Minors", "fontmed");
			
			//Green Plus Button for Majors and Minors
			Button addCurriculumButton = newAddButton();
			
			GridPane curriculumTab = newActionGrid();
			ScrollPane curriculumScroll = newActionScroll();
			curriculumScroll.setContent(curriculumTab);
			curriculumScroll.setMinViewportWidth(curriculumTab.getWidth());
			
			curriculumPane.add(curriculumHeader, 0, 0);
			curriculumPane.add(addCurriculumButton, 1, 0);
			curriculumPane.add(curriculumScroll, 0, 1);
			
			
			//SAMPLE DATA
			curriculumTab.add(new Label("Computer Science Major"), 0, 0);
			curriculumTab.add(new Label("Computer Engineering Major"), 0, 1);
			
			//Remove Major/Minor Buttons
			int numberOfMajMin = 2;																//TEMP
			Button removeMajorButton[] = new Button[numberOfMajMin];
			for(int rowIndex = 0; rowIndex < numberOfMajMin; ++rowIndex)	{
				removeMajorButton[rowIndex] = newRemoveButton();
				curriculumTab.add(removeMajorButton[rowIndex], 1, rowIndex);
			}
			
			
			actionPane.add(curriculumPane, 0, 0);
			
			//Current Selected Semester Pane
			GridPane currentSelectedSemesterPane = new GridPane();
			currentSelectedSemesterPane.setMaxWidth(actionPane.getPrefWidth()*(2/3.0));
//			currentSelectedSemesterPane.setPrefWidth(actionPane.getPrefWidth()*(2/3.0));
			
			//Header label for Current Selected Semester
			Label headerForCurrentSelectedSemester = newStyledLabel("Current Selected Semester", "fontmed");
			
			//Green Plus Button for Current Selected Semester
			Button addCourseButton = newAddButton();
			
			
			GridPane currentSelectedSemesterTab = newActionGrid();
			ScrollPane currentSelectedSemesterScroll = newActionScroll();
			currentSelectedSemesterScroll.setContent(currentSelectedSemesterTab);
			currentSelectedSemesterScroll.setMinViewportWidth(currentSelectedSemesterTab.getMinWidth());

			currentSelectedSemesterPane.add(headerForCurrentSelectedSemester, 0, 0);
			currentSelectedSemesterPane.add(addCourseButton, 1, 0);
			currentSelectedSemesterPane.add(currentSelectedSemesterScroll, 0, 1);
			
			actionPane.add(currentSelectedSemesterPane, 1, 0);
			actionPane.setHgap(10);
			
			//SAMPLE DATA
			currentSelectedSemesterTab.add(new Label("CS350	Software Design and Development"), 0, 0);
			currentSelectedSemesterTab.add(new Label("EE262	Introduction to Object Oriented Programming"), 0, 1);
			currentSelectedSemesterTab.add(new Label("EE341	Microelectronics"), 0, 2);
			currentSelectedSemesterTab.add(new Label("EE321	Signals and Systems"), 0, 3);
			currentSelectedSemesterTab.add(new Label("EE365	Advanced Digital Circuit Design"), 0, 4);
			currentSelectedSemesterTab.add(new Label("MA339	Applied Linear Algebra"), 0, 5);
			currentSelectedSemesterTab.add(new Label("CSXXX	CS Course 1"), 0, 6);
			currentSelectedSemesterTab.add(new Label("CSYYY	CS Course 2"), 0, 7);
			
			//Remove Course Buttons
			int numberOfCourses = 8;																//TEMP
			Button removeCourseButton[] = new Button[numberOfCourses];
			for(int rowIndex = 0; rowIndex < numberOfCourses; ++rowIndex)	{
				removeCourseButton[rowIndex] = newRemoveButton();
				currentSelectedSemesterTab.add(removeCourseButton[rowIndex], 1, rowIndex);
			}
			
			//Search Header Pane
			GridPane searchHeaderPane = new GridPane();
			searchHeaderPane.getColumnConstraints().add(constraints);
			
			//Search Header
			Label headerForAddCourses = newStyledLabel("Add Courses", "fontmed");
			
			//Search Field
			TextField searchField = new TextField();
			searchField.setPromptText("Course-ID");
			searchField.getStyleClass().add("windows");
			searchField.setAlignment(Pos.CENTER_RIGHT);
			
			GridPane addCoursesTab = new GridPane();

			addCoursesTab.getStyleClass().add("windows");
			addCoursesTab.setMinSize(60, 60);
			
			
			Button addCoursesButton = newAddButton();
			
			
			//Check Button
			Button checkButton = new Button();
			checkButton.setShape(new Circle(2));
			ImageView checkButtonMark = new ImageView(ResourceLoader.getImage("checkmark30.png"));
			checkButton.setGraphic(checkButtonMark);
			checkButtonMark.setFitWidth(40);
			checkButtonMark.setFitHeight(40);
			checkButton.getStyleClass().add("checkbuttontheme");
			checkButton.setAlignment(Pos.BOTTOM_RIGHT);
			
			VBox checkButtonBox = new VBox();
			checkButtonBox.getChildren().add(checkButton);
			checkButtonBox.setAlignment(Pos.BOTTOM_RIGHT);
			checkButtonBox.setPadding(new Insets(5));

			addCoursesTab.add(new Label("CS141	Introduction to Computer Science I"), 0, 0);
			addCoursesTab.add(addCoursesButton, 1, 0);
			
			searchHeaderPane.add(headerForAddCourses, 0, 0);
			searchHeaderPane.add(searchField, 1, 0);
			
			searchPane.setAlignment(Pos.BOTTOM_CENTER);
//			searchPane.getColumnConstraints().add(constraints);
			searchPane.setVgap(5);
			searchPane.setHgap(20);
			
			searchPane.add(searchHeaderPane, 0, 0);
			searchPane.add(addCoursesTab, 0, 1);
			searchPane.add(checkButtonBox, 1, 1);
			
			boolean gridLinesOn = true;
			if(gridLinesOn)	{
				searchHeaderPane.setGridLinesVisible(true);
				actionPane.setGridLinesVisible(true);
				searchPane.setGridLinesVisible(true);
				orgPane.setGridLinesVisible(true);
			}

			
			orgPane.add(topPane, 0, 0);
			orgPane.add(schedulePane, 0, 1);
			orgPane.add(actionPane, 0, 2);
			orgPane.add(searchPane, 0, 3);
			orgPane.setVgap(10);
			orgPane.setHgap(5);
			orgPane.setPadding(new Insets(10));			
			
			studentScreen.setCenter(orgPane);
		}	catch( Exception e )	{
			e.printStackTrace();
		}
		
		return studentScreen;
	}
	
	public BorderPane advisorView()	{

		BorderPane advisorScreen = new BorderPane();
		try	{
			if(false)						//TESTING ONLY - DELETE ONCE A FUNCTION IS ADDED THAT THROWS IOE
				throw new IOException();	//TODO: REMOVE
			
			BorderPane advisorInfoPane = new BorderPane();
			BorderPane studentInfoPane = new BorderPane();
			
			advisorInfoPane.getStyleClass().add("lightgraytheme");

			ColumnConstraints constraints = new ColumnConstraints();
			constraints.setHgrow(Priority.ALWAYS);

			//TESTING? -- Sets Student Side
			studentInfoPane = studentView();
			
			GridPane orgPane = new GridPane();
			
			Label name = new Label();
			name.setText("Sean Banerjee");		//getName());
			name.getStyleClass().add("fonttitle");
			
			//studentList Pane
			GridPane studentListPane = new GridPane();
			studentListPane.setMaxWidth(orgPane.getPrefWidth()*(1/3.0));
			
			//Header label for studentList
			Label studentListHeader = newStyledLabel("Students", "fontmed");
			
			//Tab for studentList
			GridPane studentListTab = newActionGrid();
			
			//Scroll for studentList
			ScrollPane studentListScroll = newActionScroll();
			studentListScroll.setContent(studentListTab);
			studentListScroll.setMinViewportWidth(studentListTab.getWidth());
			
			studentListPane.add(studentListHeader, 0, 0);
			studentListPane.add(studentListScroll, 0, 1);
			
			//SAMPLE DATA
			studentListTab.add(new Label("Lorenzo Villani"), 0, 0);
			
			orgPane.add(studentListPane, 0, 0);
			
			//Requests Pane
			GridPane requestsPane = new GridPane();
			requestsPane.setMaxWidth(orgPane.getPrefWidth()*(1/3.0));
			
			//Header label for studentList
			Label requestsHeader = newStyledLabel("List of Requests", "fontmed");
			
			//Tab for studentList
			GridPane requestsTab = newActionGrid();
			
			//Scroll for studentList
			ScrollPane requestsScroll = newActionScroll();
			requestsScroll.setContent(requestsTab);
			requestsScroll.setMinViewportWidth(requestsTab.getWidth());
			
			requestsPane.add(requestsHeader, 0, 0);
			requestsPane.add(requestsScroll, 0, 1);
			
			//SAMPLE DATA
			requestsTab.add(new Label("Heet Dave - Add Business Major"), 0, 0);
			
			//Remove Requests Buttons
			int numberOfRequests = 1;																//TEMP
			Button removeRequestsButton[] = new Button[numberOfRequests];
			for(int rowIndex = 0; rowIndex < numberOfRequests; ++rowIndex)	{
				removeRequestsButton[rowIndex] = newRemoveButton();
				requestsTab.add(removeRequestsButton[rowIndex], 1, rowIndex);
			}
			
			//Add Requests Buttons
			Button addRequestsButton[] = new Button[numberOfRequests];
			for(int rowIndex = 0; rowIndex < numberOfRequests; ++rowIndex)	{
				addRequestsButton[rowIndex] = newAddButton();
				requestsTab.add(addRequestsButton[rowIndex], 1, rowIndex);
			}
			
			orgPane.add(requestsPane, 1, 0);
			
			//TESTING
			orgPane.setGridLinesVisible(true);
			
			orgPane.getColumnConstraints().add(constraints);
			orgPane.add(name, 0, 0);
			orgPane.add(newStyledLabel("Students", "fontmed"), 0, 1);
//			orgPane.add(name, 0, 0);
//			orgPane.add(name, 0, 0);
//			orgPane.add(name, 0, 0);
			
			advisorInfoPane.getChildren().add(orgPane);
			advisorScreen.setLeft(advisorInfoPane);
			advisorScreen.setRight(studentInfoPane);
		}	catch( IOException ioe )	{
			ioe.printStackTrace();
		}
		return advisorScreen;
	}

	
	@Override
	public void start(Stage primaryStage)	{
		try	{		
			updateUI();

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
