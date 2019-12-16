package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import framework.Course;
import framework.Curriculum;
import framework.DatabaseManager;
import framework.Majorizer;
import framework.Request;
import framework.Student;
import framework.User;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.ResourceLoader;
import utils.Single;

public class UserInterface extends Application{

	public static final int NO_SEMESTER_SELECTED = -1;
	public static Image GREEN_PLUS_IMAGE;
	public static Image MINUS_512;
	
	static {
		try {
			// loading all images:
			GREEN_PLUS_IMAGE = ResourceLoader.getImage("NEWgreenPlus.png");
			MINUS_512 = ResourceLoader.getImage("minus-512.png");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	
	private Scene scene = null;
	private final GridPane semesters[] = new GridPane[8];
	private int selectedSemester;
	private BorderPane searchBorderPane;
	private boolean isSeachingCurriculumsVisible = false;
	private Button addCourseToSemesterButton;
	
	private GridPane requestsTab = new GridPane();
	private GridPane currentSelectedSemesterTab = newActionGrid();
	private GridPane currentSelectedSemesterPane = new GridPane();
	private GridPane curriculumTab = new GridPane();
	private GridPane currentCurriculumsPane = new GridPane();
	private Dimension screenSize;
	boolean isAuthenticated = false;
	
	
	public void updateUI()	{
		
		Node root = null;

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
			scene = new Scene((Parent) root);
		else
			scene.setRoot((Parent) root);
		
		try {
			String path = ResourceLoader.getCSSPath("application.css");
			scene.getStylesheets().add(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getName()	{
		return Majorizer.getStudent().getFirstName() + ' ' + Majorizer.getStudent().getLastName();
	}
	
	public String getNameAdvisor() {
		return Majorizer.getAdvisor().getFirstName() + ' ' + Majorizer.getAdvisor().getLastName();
	}

	public String getStudentID()	{
		return Majorizer.getStudent().getUniversityID();
	}
	
	public void setFadeIn(Node node, int timeMili) {
		setFadeIn(node, 0, timeMili);
	}
	
	public void setFadeIn(Node node, double startOpacity, int timeMili) {
		if (!node.isVisible()) {
			FadeTransition fadeIn = new FadeTransition(Duration.millis(timeMili));
			fadeIn.setNode(node);
			fadeIn.setFromValue(startOpacity);
			fadeIn.setToValue(1.0);
			fadeIn.setCycleCount(1);
			fadeIn.setAutoReverse(false);
			node.setVisible(true);
	        fadeIn.playFromStart();
	    }
	}
	
	public void setFadeOut(Node node, int timeMili) {
		if (node.isVisible()) {
			FadeTransition fadeIn = new FadeTransition(Duration.millis(timeMili));
			fadeIn.setNode(node);
			fadeIn.setFromValue(1.0);
			fadeIn.setToValue(0);
			fadeIn.setCycleCount(1);
			fadeIn.setAutoReverse(false);
			node.setVisible(true);
	        fadeIn.playFromStart();
	    }
	}
	
	public void newSemester(final int semesterNumber, final String css)	{
		GridPane semester = new GridPane();
		semester.setMinSize((screenSize.getWidth()*(2/3.0))/8.0, screenSize.getHeight()/5.0);
		semester.getStyleClass().add(css);
		semester.setStyle("-fx-border-color: black; -fx-border-width: 1px; outline: solid;");
		semester.setOnMouseClicked((me) -> {
			setFadeIn(currentSelectedSemesterPane, 300);
			selectSemester(semesterNumber);
		});
		this.semesters[semesterNumber] = semester;
	}
	
	public void selectSemester(int semesterNumber)	{
		if(!searchBorderPane.getChildren().isEmpty() && !isSeachingCurriculumsVisible) {
			Node current = searchBorderPane.getCenter();
			setFadeOut(current, 150);
			searchBorderPane.getChildren().remove(0);
		}
		int currentSemester = Majorizer.getStudentCurrentSemesterIndex();
		if(semesterNumber >= currentSemester) {
			addCourseToSemesterButton.setVisible(true);
			addCourseToSemesterButton.setManaged(true);
		} else {
			addCourseToSemesterButton.setVisible(false);
			addCourseToSemesterButton.setManaged(false);
		}
		
		selectedSemester = semesterNumber;
		currentSelectedSemesterTab.getChildren().clear();
		for(int semester = 0; semester <= 7; ++semester)	{
			if(semester == semesterNumber)	{
				this.semesters[semester].setStyle("-fx-border-color: yellow; -fx-border-width: 3px; outline: solid;");
				populateSemesterShown(semester);
			}
			else {
				this.semesters[semester].setStyle("-fx-border-color: black; -fx-border-width: 1px; outline: solid;");
			}
		}
	}

	public void addRequest(Request request) {
		Student student = request.getStudent();
		Curriculum curriculum = request.getCurriculum();
		int curriculumID = curriculum.getCurriculumID();
		
		String firstName = student.getFirstName();
		String lastName = student.getLastName();
		String fullName = firstName + ' ' + lastName;
		
		String curriculumName = curriculum.getName();
		
		Label studentLabel = new Label(fullName);
		Label actionLabel;
		if(request.isAdding() == true) {
			actionLabel = new Label("[REQUEST ADD]");
			actionLabel.setTextFill(Color.DARKGREEN);
		} else {
			actionLabel = new Label("[REQUEST REMOVE]");
			actionLabel.setTextFill(Color.DARKRED);
		}
		Label curriculumLabel = new Label(curriculumName);
		Button removeButton = newRemoveButton();
		Button addButton = newAddButton();
		HBox hbox1 = new HBox(actionLabel, removeButton, addButton);
		hbox1.setSpacing(5);
//		HBox hbox2 = new HBox(studentLabel);
		VBox vbox = new VBox(hbox1, curriculumLabel, studentLabel);
		vbox.setStyle("-fx-border-color: gray; -fx-border-width: 1px; outline: solid;");
		vbox.setPadding(new Insets(2, 5, 2, 5));
		
		addButton.setOnAction((ae) -> {
			requestsTab.getChildren().remove(vbox);
			DatabaseManager.removeRequest(request);
			Student fetchedStudent = DatabaseManager.getStudent(student.getUserID());
			ArrayList<Integer> curriculumIDs = fetchedStudent.getAcademicPlan().getDegreeIDs();
			if(request.isAdding()) {
				if(!curriculumIDs.contains(curriculumID))
					curriculumIDs.add(curriculumID);
			} else {
				Iterator<Integer> iterator = curriculumIDs.iterator();
				while(iterator.hasNext()) {
					int currentID = iterator.next();
					if(currentID == curriculumID) {
						iterator.remove();
						break;
					}
				}
			}
			Majorizer.saveStudent(fetchedStudent);
			
			if(Majorizer.checkIfCurrentStudent(student.getUserID())) {
				Majorizer.hardRemoveRequest(request);
				Student currentStudent = Majorizer.getStudent();
				ArrayList<Integer> currentCurriculumIDs = currentStudent.getAcademicPlan().getDegreeIDs();
				if(request.isAdding()) {
					if(!currentCurriculumIDs.contains(curriculumID))
						currentCurriculumIDs.add(curriculumID);
				} else {
					Iterator<Integer> iterator = currentCurriculumIDs.iterator();
					while(iterator.hasNext()) {
						int currentID = iterator.next();
						if(currentID == curriculumID) {
							iterator.remove();
							break;
						}
					}
				}
				populateMajors();
			}
		});
		removeButton.setOnAction((ae) -> {
			requestsTab.getChildren().remove(vbox);
			DatabaseManager.removeRequest(request);
			if(Majorizer.checkIfCurrentStudent(student.getUserID())) {
				Majorizer.hardRemoveRequest(request);
				populateMajors();
			}
		});
		
		
		int requestSize = requestsTab.getChildren().size();
		
		requestsTab.add(vbox, 0, requestSize);
	}
	
	public void populateMajors() {
		curriculumTab.getChildren().clear();
		ArrayList<Integer> curriculae = Majorizer.getStudent().getAcademicPlan().getDegreeIDs();
		for(int curriculumIdx = 0; curriculumIdx < curriculae.size(); ++curriculumIdx)	{
			int curriculumID = curriculae.get(curriculumIdx);
			Curriculum curriculum = DatabaseManager.getCurriculum(curriculumID);
			addCurriculum(curriculum);
		}
		for(Request request : Majorizer.getStudentSaveRequestsIterable())	{
			Curriculum curriculum = DatabaseManager.getCurriculum(request.getCurriculumID());
			addCurriculumRequest(curriculum, request.isAdding());
		}
	}
	
	public void populateSemesterShown(int semester)	{
		ArrayList<Integer> currentSemCourses = getCurrentSemesterCourses();
		
		for(int courseIdx = 0; courseIdx < currentSemCourses.size(); ++courseIdx)	{
			final int courseID = currentSemCourses.get(courseIdx);
			Course course = DatabaseManager.getCourse(courseID);
			addCourseToCurrentSemester(course);
		}
	}
	
	private synchronized void addCurriculumRequest(Curriculum c, boolean adding) {
		String curriculumName = c.getName();
		final int curriculumID = c.getCurriculumID();

		final Label cLabel = new Label(curriculumName);
		final Label rLabel;
		if(adding) {
			rLabel = new Label("[REQUEST ADD]");
			rLabel.setTextFill(Color.DARKGREEN);
		} else {
			rLabel = new Label("[REQUEST REMOVE]");
			rLabel.setTextFill(Color.DARKRED);
		}
		
		final HBox box = new HBox(cLabel, rLabel);
		
		final ObservableList<Node> nodes = curriculumTab.getChildren();
		int numElements = nodes.size() / 2;
		curriculumTab.add(box, 0, numElements);
		final Button removeCurriculumButton = newRemoveButton();
		curriculumTab.add(removeCurriculumButton, 1, numElements);

		removeCurriculumButton.setOnMouseClicked((me) -> {
			Majorizer.removeRequest(curriculumID, adding);
			nodes.remove(box);
			nodes.remove(removeCurriculumButton);
		});
	}
	
	private synchronized void addCurriculum(Curriculum c) {
		String curriculumName = c.getName();
		final int curriculumID = c.getCurriculumID();

		final Label cLabel = new Label(curriculumName);
		
		final ObservableList<Node> nodes = curriculumTab.getChildren();
		int numElements = nodes.size() / 2;
		curriculumTab.add(cLabel, 0, numElements);
		final Button removeCurriculumButton = newRemoveButton();
		curriculumTab.add(removeCurriculumButton, 1, numElements);

		removeCurriculumButton.setOnMouseClicked((me) -> {
			if(Majorizer.getUser().isUserIsStudent()) {
				if(!Majorizer.checkIfRequestExists(curriculumID, false)) {
					Majorizer.addRequest(curriculumID, false);
					addCurriculumRequest(c, false);
				}
			} else {
				ArrayList<Integer> curriculums = Majorizer.getStudent().getAcademicPlan().getDegreeIDs();
				Iterator<Integer> iterator = curriculums.iterator();
				while(iterator.hasNext()) {
					Integer id = iterator.next();
					if(id == curriculumID) {
						iterator.remove();
						break;
					}
				}
				nodes.remove(cLabel);
				nodes.remove(removeCurriculumButton);
			}
		});
	}
	
	
	private synchronized void addCourseToCurrentSemester(Course c) {
		if(selectedSemester == NO_SEMESTER_SELECTED)
			return;
		
		String courseName = c.getCourseName();
		String courseCode = c.getCourseCode();
		final int courseID = c.getCourseID();
		
		int currentSemester = Majorizer.getStudentCurrentSemesterIndex();

		final Label cLabel = new Label(courseCode + '\t' + courseName);
		
		final ObservableList<Node> nodes = currentSelectedSemesterTab.getChildren();
		
		if(selectedSemester < currentSemester) {
			int numElements = nodes.size();
			currentSelectedSemesterTab.add(cLabel, 0, numElements);
		} else {
			int numElements = nodes.size() / 2;
			currentSelectedSemesterTab.add(cLabel, 0, numElements);
			final Button removeCourseButton = newRemoveButton();
			currentSelectedSemesterTab.add(removeCourseButton, 1, numElements);

			removeCourseButton.setOnMouseClicked((me) -> {
				Iterator<Integer> iterator = getCurrentSemesterCourses().iterator();
				while(iterator.hasNext()) {
					Integer id = iterator.next();
					if(id == courseID) {
						iterator.remove();
						break;
					}
				}
				nodes.remove(cLabel);
				nodes.remove(removeCourseButton);
			});
		}
	}
	
	private ArrayList<Integer> getCurrentSemesterCourses() {
		if(selectedSemester == NO_SEMESTER_SELECTED)
			return new ArrayList<>();
		Map<String, ArrayList<Integer>> courses = Majorizer.getStudent().getAcademicPlan().getSelectedCourseIDs();
		String selectedSemesterString = Majorizer.getStudentCurrentSemesterString(selectedSemester);
		ArrayList<Integer> semCourses = courses.get(selectedSemesterString);
		if(semCourses == null) {
			semCourses = new ArrayList<Integer>();
			courses.put(selectedSemesterString, semCourses);
		}
		return semCourses;
	}
	

//REUSABLE ELEMENTS===========================
	public Button newRemoveButton()	{
		Button removeButton = new Button();
		removeButton.setShape(new Circle(2));
		ImageView redMinusMark = null;
		
		redMinusMark = new ImageView(MINUS_512);
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
		greenPlusMark = new ImageView(GREEN_PLUS_IMAGE);
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
			login.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.DARKGOLDENROD, 3, 0, -3, -3));
			
			
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
	    	        if (!username.isFocused())
	    	            username.requestFocus();
	    	    });
	    	 
	    	
	    	loginButton.setOnAction((ae) -> {
	    		User user = Majorizer.authenticate(username.getText(), password.getText());
	    		Majorizer.setUser(user);
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
			
			GridPane.setMargin(login, new Insets(100));

		}	catch( IOException ioe)	{
			ioe.printStackTrace();
		}
		return loginScreen;
	}
	
	public Parent studentView()	{
		
		BorderPane studentScreen = new BorderPane();
		ScrollPane root = new ScrollPane();
		root.setContent(studentScreen);
		root.setFitToHeight(true);
		root.setFitToWidth(true);
		
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
				System.exit(0);
			});
			
			VBox logoutButtonBox = new VBox();
			logoutButtonBox.getChildren().add(logoutButton);
			logoutButtonBox.setAlignment(Pos.TOP_RIGHT);
			logoutButtonBox.setPadding(new Insets(5));
			logoutButtonBox.getStyleClass().add("logoutbuttontheme");
			
			topPane.add(name, 0, 0);
			topPane.add(studentID, 0, 1);
			topPane.add(logoutButtonBox, 1, 0);
			GridPane.setMargin(logoutButtonBox, new Insets(20, 0, 0, 0));
			topPane.getColumnConstraints().add(constraints);
			
			//Schedule Pane
			GridPane schedulePane = new GridPane();
			
			//Header label for Schedule Pane
			Label header = newStyledLabel("Course Schedule", "fonttitle");
			schedulePane.add(header, 0, 0);
			
			//Schedule
			GridPane scheduleBox = new GridPane();
			for(int columnIndex = 0; columnIndex <= 7; ++columnIndex)	{
				if(columnIndex < Majorizer.getStudentCurrentSemesterIndex())
					newSemester(columnIndex, "pastSem");
				else if(columnIndex == Majorizer.getStudentCurrentSemesterIndex())
					newSemester(columnIndex, "currentSem");
				else if(columnIndex > Majorizer.getStudentCurrentSemesterIndex())
					newSemester(columnIndex, "futureSem");
				
				scheduleBox.add(this.semesters[columnIndex], columnIndex, 1);
			}
			schedulePane.add(scheduleBox, 0, 1);
			
			final double tabWidth = 1/2.0;
			
			actionPane.setPrefWidth((screenSize.getWidth()*(2/3.0)));
//			actionPane.getColumnConstraints().add(constraints);
			
			//Majors and Minors Pane
			currentCurriculumsPane = new GridPane();
			currentCurriculumsPane.setMaxWidth(screenSize.getWidth()*(2/3.0)*tabWidth-50.0);
			currentCurriculumsPane.setMinWidth(screenSize.getWidth()*(2/3.0)*tabWidth-50.0);
			
			//Header label for Majors and Minors
			Label curriculumHeader = newStyledLabel("Majors and Minors", "fontmed");
			
			//Green Plus Button for Majors and Minors
			Button addCurriculumButton = newAddButton();
			addCurriculumButton.setOnAction((ae) -> {
				if(searchBorderPane.getChildren().isEmpty()) {
					Node current = mkAddCurriculumsPane(constraints);
					searchBorderPane.setCenter(current);
					setFadeIn(current, 150);
				} else {
					if(!isSeachingCurriculumsVisible) {
						Node prev = searchBorderPane.getCenter();
						setFadeOut(prev, 150);
						Node current = mkAddCurriculumsPane(constraints);
						searchBorderPane.setCenter(current);
						setFadeIn(current, .8f, 150);
					}
				}
				isSeachingCurriculumsVisible = true;
			});
			
			curriculumTab = newActionGrid();
			ScrollPane curriculumScroll = newActionScroll();
			curriculumScroll.setContent(curriculumTab);
			curriculumScroll.setMinViewportWidth(curriculumTab.getWidth());
			
			currentCurriculumsPane.add(curriculumHeader, 0, 0);
			currentCurriculumsPane.add(addCurriculumButton, 1, 0);
			currentCurriculumsPane.add(curriculumScroll, 0, 1);
			
			populateMajors();
			
			actionPane.add(currentCurriculumsPane, 0, 0);
			
			//Current Selected Semester Pane
			currentSelectedSemesterPane = new GridPane();
			currentSelectedSemesterPane.setMaxWidth(actionPane.getPrefWidth()*(2/3.0)*tabWidth);
			currentSelectedSemesterPane.setMinWidth(actionPane.getPrefWidth()*(2/3.0)*tabWidth);
			
			//Header label for Current Selected Semester
			Label headerForCurrentSelectedSemester = newStyledLabel("Current Selected Semester", "fontmed");
			
			//Green Plus Button for Current Selected Semester
			addCourseToSemesterButton = newAddButton();
			addCourseToSemesterButton.setOnAction((ae) -> {
				if(searchBorderPane.getChildren().isEmpty()) {
					Node current = mkAddCoursesPane(constraints);
					searchBorderPane.setCenter(current);
					setFadeIn(current, 150);
				} else {
					if(isSeachingCurriculumsVisible) {
						Node prev = searchBorderPane.getCenter();
						setFadeOut(prev, 150);
						Node current = mkAddCoursesPane(constraints);
						searchBorderPane.setCenter(current);
						setFadeIn(current, 150);
					}
				}
				isSeachingCurriculumsVisible = false;
			});
			
			ScrollPane currentSelectedSemesterScroll = newActionScroll();
			currentSelectedSemesterScroll.setContent(currentSelectedSemesterTab);
//			currentSelectedSemesterScroll.setMinViewportWidth(currentSelectedSemesterTab.getMinWidth());
			currentSelectedSemesterScroll.setMaxWidth(actionPane.getPrefWidth()*tabWidth);
			currentSelectedSemesterScroll.setMinWidth(actionPane.getPrefWidth()*tabWidth);

			currentSelectedSemesterPane.add(headerForCurrentSelectedSemester, 0, 0);
			currentSelectedSemesterPane.add(addCourseToSemesterButton, 1, 0);
			currentSelectedSemesterPane.add(currentSelectedSemesterScroll, 0, 1);
			currentSelectedSemesterPane.setVisible(false);
			
			actionPane.add(currentSelectedSemesterPane, 1, 0);
			actionPane.setHgap(10);
			
			
			searchBorderPane = new BorderPane();			
			
			//Check Button
			Button checkButton = new Button();
			checkButton.setShape(new Circle(2));
			ImageView checkButtonMark = new ImageView(ResourceLoader.getImage("checkmark30.png"));
			checkButton.setGraphic(checkButtonMark);
			checkButtonMark.setFitWidth(40);
			checkButtonMark.setFitHeight(40);
			checkButton.getStyleClass().add("checkbuttontheme");
			checkButton.setAlignment(Pos.BOTTOM_RIGHT);
			
			checkButton.setOnMouseClicked((me) -> {
				Majorizer.saveCurrentStudent();
			});
			
			VBox checkButtonBox = new VBox();
			checkButtonBox.getChildren().add(checkButton);
			checkButtonBox.setAlignment(Pos.BOTTOM_RIGHT);
			checkButtonBox.setPadding(new Insets(5));

			searchPane.setAlignment(Pos.BOTTOM_CENTER);
			searchPane.setVgap(5);
			searchPane.setHgap(20);
			
			searchPane.add(searchBorderPane, 0, 0);
			searchPane.add(checkButtonBox, 1, 0);
			
			boolean gridLinesOn = false;
			if(gridLinesOn)	{
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
		
		return root;
	}

	
	public GridPane mkAddCurriculumsPane(ColumnConstraints constraints) {
		GridPane addCurriculumsTab = newActionGrid();
		ScrollPane addCurriculumsScroll = newActionScroll();
		
		//Search Header Pane
		GridPane searchHeaderPane = new GridPane();
		searchHeaderPane.getColumnConstraints().add(constraints);
		
		//Search Header
		Label headerForAddCurriculums = newStyledLabel("Add Curriculums", "fontmed");
		
		//Search Field
		TextField searchField = new TextField();
		searchField.setPromptText("search");
		searchField.getStyleClass().add("windows");
		searchField.setAlignment(Pos.CENTER_RIGHT);
		
		searchField.setOnKeyTyped((ke) -> {
			
			// set request to another thread "searchThread"
			searchThread.setSearchRequest(()-> {
				if(searchField.getText().equals(null) || searchField.getText().equals(""))	{
					// nothing to search, clear previous search results from addCoursesTab
					
					// GUI changes must be on main thread
					final Single<Boolean> waitUntilDoneFlag = new Single<>(false);
					Platform.runLater(() -> {
						addCurriculumsTab.getChildren().clear();
						waitUntilDoneFlag.single = true;
					});
					waitUntilDone(waitUntilDoneFlag);
					
					return; // no need to do anything further, end lambda functionality here
				}
				
				final ArrayList<Curriculum> searchedCurriculums = DatabaseManager.searchCurriculum(searchField.getText());
				Iterator<Curriculum> checkIter = searchedCurriculums.iterator();
				ArrayList<Integer> studentCurrentCurriculumload = Majorizer.getStudent().getAcademicPlan().getDegreeIDs();
				if(studentCurrentCurriculumload != null) {
					while(checkIter.hasNext()) {
						Curriculum checkCurriculum = checkIter.next();
						if(studentCurrentCurriculumload.contains(checkCurriculum.getCurriculumID())) {
							checkIter.remove();
						} else {
							if(Majorizer.checkIfRequestExists(checkCurriculum.getCurriculumID(), true))
								checkIter.remove();
						}
					}
				}
				// End of search
				
				// GUI changes must be on main thread

				final Single<Boolean> waitUntilDoneFlag = new Single<>(false);
				Platform.setImplicitExit(false);
				Platform.runLater(() -> {
					addCurriculumsTab.getChildren().clear();
					for(int searchIndex = 0; searchIndex < searchedCurriculums.size(); ++searchIndex)	{
						final Curriculum searchedCurriculum = searchedCurriculums.get(searchIndex);
						final int curriculumID = searchedCurriculum.getCurriculumID();
						Label searchedLabel = new Label(searchedCurriculum.getName());
						addCurriculumsTab.add(searchedLabel, 0, searchIndex);
						
						Button addSearchButton = newAddButton();
						addSearchButton.setOnMouseClicked((me) -> {
							if(Majorizer.getUser().isUserIsStudent()) {
								if(!Majorizer.checkIfRequestExists(curriculumID, true)) {
									Majorizer.addRequest(curriculumID, true);
									addCurriculumRequest(searchedCurriculum, true);
								}
							} else {
								Iterator<Curriculum> iterator = searchedCurriculums.iterator();
								while(iterator.hasNext()) {
									Curriculum currentCurriculum = iterator.next();
									if(currentCurriculum.getCurriculumID() == searchedCurriculum.getCurriculumID()) {
										studentCurrentCurriculumload.add(searchedCurriculum.getCurriculumID());
										iterator.remove();
										break;
									}
								}
								addCurriculum(searchedCurriculum);
							}
							addCurriculumsTab.getChildren().remove(searchedLabel);
							addCurriculumsTab.getChildren().remove(addSearchButton);
						});
						addCurriculumsTab.add(addSearchButton, 1, searchIndex);
					}
					waitUntilDoneFlag.single = true;
				});
				waitUntilDone(waitUntilDoneFlag);
			});
		});
		
		final double tabWidthTwo = 3/4.0;
		addCurriculumsTab.getStyleClass().add("windows");
		addCurriculumsTab.setMinSize(screenSize.getWidth()*(2/3.0)*tabWidthTwo, screenSize.getHeight()*(1/5.0));
		addCurriculumsTab.setMaxSize(screenSize.getWidth()*(2/3.0)*tabWidthTwo, screenSize.getHeight()*(1/5.0));
		addCurriculumsScroll.setMinSize(screenSize.getWidth()*(2/3.0)*tabWidthTwo, screenSize.getHeight()*(1/5.0));
		addCurriculumsScroll.setMaxSize(screenSize.getWidth()*(2/3.0)*tabWidthTwo, screenSize.getHeight()*(1/5.0));
		addCurriculumsScroll.setContent(addCurriculumsTab);
		
		
		searchHeaderPane.add(headerForAddCurriculums, 0, 0);
		searchHeaderPane.add(searchField, 1, 0);
		
		GridPane searchCoursesPane = new GridPane();
		
		searchCoursesPane.add(searchHeaderPane, 0, 0);
		searchCoursesPane.add(addCurriculumsScroll, 0, 1);
		searchCoursesPane.setVisible(false);
		
		return searchCoursesPane;
	}
	
	
	public GridPane mkAddCoursesPane(ColumnConstraints constraints) {
		GridPane addCoursesTab = newActionGrid();
		ScrollPane addCoursesScroll = newActionScroll();
		
		//Search Header Pane
		GridPane searchHeaderPane = new GridPane();
		searchHeaderPane.getColumnConstraints().add(constraints);
		
		//Search Header
		Label headerForAddCourses = newStyledLabel("Add Courses", "fontmed");
		
		//Search Field
		TextField searchField = new TextField();
		searchField.setPromptText("search");
		searchField.getStyleClass().add("windows");
		searchField.setAlignment(Pos.CENTER_RIGHT);
		
		searchField.setOnKeyTyped((ke) -> {
			
			// set request to another thread "searchThread"
			searchThread.setSearchRequest(()-> {
				if(searchField.getText().equals(null) || searchField.getText().equals(""))	{
					// nothing to search, clear previous search results from addCoursesTab
					
					// GUI changes must be on main thread
					final Single<Boolean> waitUntilDoneFlag = new Single<>(false);
					Platform.runLater(() -> {
						addCoursesTab.getChildren().clear();
						waitUntilDoneFlag.single = true;
					});
					waitUntilDone(waitUntilDoneFlag);
					
					return; // no need to do anything further, end lambda functionality here
				}
				
				// Search (Takes forever)
				String searchSemester = Majorizer.getStudentCurrentSemesterString(selectedSemester);
				final ArrayList<Course> searchedCourses = DatabaseManager.searchCourse(searchField.getText(), searchSemester);
				Iterator<Course> checkIter = searchedCourses.iterator();
				ArrayList<Integer> studentCurrentCourseload = Majorizer.getStudent().getAcademicPlan().getSelectedCourseIDs().get(searchSemester);
				if(studentCurrentCourseload != null) {
					while(checkIter.hasNext()) {
						Course checkCourse = checkIter.next();
						if(studentCurrentCourseload.contains(checkCourse.getCourseID()))
							checkIter.remove();
					}
				}
				// End of search
				
				// GUI changes must be on main thread

				final Single<Boolean> waitUntilDoneFlag = new Single<>(false);
				Platform.setImplicitExit(false);
				Platform.runLater(() -> {
					addCoursesTab.getChildren().clear();
					for(int searchIndex = 0; searchIndex < searchedCourses.size(); ++searchIndex)	{
						final Course searchedCourse = searchedCourses.get(searchIndex);
						Label searchedLabel = new Label(searchedCourse.getCourseCode() + '\t' + searchedCourse.getCourseName());
						addCoursesTab.add(searchedLabel, 0, searchIndex);
						
						Button addSearchButton = newAddButton();
						addSearchButton.setOnMouseClicked((me) -> {
							Iterator<Course> iterator = searchedCourses.iterator();
							while(iterator.hasNext()) {
								Course currentCourse = iterator.next();
								if(currentCourse.getCourseID() == searchedCourse.getCourseID()) {
									studentCurrentCourseload.add(searchedCourse.getCourseID());
									iterator.remove();
									break;
								}
							}
							addCoursesTab.getChildren().remove(searchedLabel);
							addCoursesTab.getChildren().remove(addSearchButton);
							addCourseToCurrentSemester(searchedCourse);
						});
						addCoursesTab.add(addSearchButton, 1, searchIndex);
					}
					waitUntilDoneFlag.single = true;
				});
				waitUntilDone(waitUntilDoneFlag);
			});
		});
		
		final double tabWidthTwo = 3/4.0;
		addCoursesTab.getStyleClass().add("windows");
		addCoursesTab.setMinSize(screenSize.getWidth()*(2/3.0)*tabWidthTwo, screenSize.getHeight()*(1/5.0));
		addCoursesTab.setMaxSize(screenSize.getWidth()*(2/3.0)*tabWidthTwo, screenSize.getHeight()*(1/5.0));
		addCoursesScroll.setMinSize(screenSize.getWidth()*(2/3.0)*tabWidthTwo, screenSize.getHeight()*(1/5.0));
		addCoursesScroll.setMaxSize(screenSize.getWidth()*(2/3.0)*tabWidthTwo, screenSize.getHeight()*(1/5.0));
		addCoursesScroll.setContent(addCoursesTab);
		
		
		searchHeaderPane.add(headerForAddCourses, 0, 0);
		searchHeaderPane.add(searchField, 1, 0);
		
		GridPane searchCoursesPane = new GridPane();
		
		searchCoursesPane.add(searchHeaderPane, 0, 0);
		searchCoursesPane.add(addCoursesScroll, 0, 1);
		searchCoursesPane.setVisible(false);
		
		return searchCoursesPane;
	}
	
	public Parent advisorView()	{
		
		
		BorderPane advisorSide = new BorderPane();
		BorderPane studentSide = new BorderPane();
		SplitPane splitStudentAdvisor = new SplitPane(advisorSide, studentSide);
		splitStudentAdvisor.setDividerPositions(.2f);
		ScrollPane root = new ScrollPane();
		root.setContent(splitStudentAdvisor);
		root.setFitToHeight(true);
		root.setFitToWidth(true);
		
		
		advisorSide.getStyleClass().add("lightgraytheme");
		advisorSide.setPadding(new Insets(20));
		
		ColumnConstraints constraints = new ColumnConstraints();
		constraints.setHgrow(Priority.ALWAYS);
		
		GridPane orgPane = new GridPane();
		orgPane.getColumnConstraints().add(constraints);

		Label name = new Label();
		name.setText(getNameAdvisor());
		System.out.println(getNameAdvisor());
		
		name.getStyleClass().add("fonttitle");
		
		//studentList Pane
		GridPane studentListPane = new GridPane();
		studentListPane.setPrefWidth(200);
		
		//Header label for studentList
		Label studentListHeader = newStyledLabel("Students", "fontmed");
		
		//Tab for studentList
		GridPane studentListTab = newActionGrid();
		
		//Scroll for studentList
		ScrollPane studentListScroll = newActionScroll();
		studentListScroll.setContent(studentListTab);
		studentListScroll.setMinViewportWidth(200);
		
		studentListPane.add(studentListHeader, 0, 0);
		studentListPane.add(studentListScroll, 0, 1);
		
		ArrayList<Student> students = Majorizer.getAdvisees();
		for(int studentIdx = 0; studentIdx < students.size(); ++studentIdx)	{
			Student student = students.get(studentIdx); 
			String firstName = student.getFirstName();
			String lastName = student.getLastName();
			
			String fullName = firstName + ' ' + lastName;
			
			Label studentNames = new Label(fullName);
			studentListTab.add(studentNames, 0, studentIdx);
			
			studentNames.setOnMousePressed((me) -> {
				Student fetchedStudent = DatabaseManager.getStudent(student.getUserID());
				Majorizer.setStudent(fetchedStudent);
				Node studentView = studentView();
				studentView.setVisible(false);
				studentSide.setCenter(studentView);
				setFadeIn(studentView, 300);
			});
		}
		
		//Requests Pane
		GridPane requestsPane = new GridPane();
		requestsPane.setMaxWidth(200);
		
		//Header label for studentList
		Label requestsHeader = newStyledLabel("List of Requests", "fontmed");
		
		//Tab for requestsList
		requestsTab = newActionGrid();
		
		//Scroll for studentList
		ScrollPane requestsScroll = newActionScroll();
		requestsScroll.setContent(requestsTab);
		requestsScroll.setMinViewportWidth(300);
		
		requestsPane.add(requestsHeader, 0, 0);
		requestsPane.add(requestsScroll, 0, 1);
		
		ArrayList<Request> requests = Majorizer.getRequests();
		for(int requestIdx = 0; requestIdx < requests.size(); ++requestIdx) {
			Request request = requests.get(requestIdx);
			addRequest(request);
		}
		
		orgPane.add(name, 0, 0);
		orgPane.add(studentListPane, 0, 1);
		orgPane.add(requestsPane, 0, 2);
		
		advisorSide.setCenter(orgPane);
		
		return root;
	}

	
	// used for search thread
	private SearchThread searchThread;
	private boolean windowOpen = true; // true when program starts up
	private static final int SEARCH_WAIT_TIME_MILI = 100;
	private static final Object REQUEST_LOCK = new Object();
	
	
	// used for search thread
	private class SearchThread extends Thread {
		private Runnable searchRunnable;
		
		@Override
		public void run() {
			while (windowOpen) {
				if(searchRunnable != null) {
					Runnable toExecute;
					synchronized (REQUEST_LOCK) {
						toExecute = searchRunnable;
						searchRunnable = null;
					}
					
					toExecute.run();
				}
				try {
					Thread.sleep(SEARCH_WAIT_TIME_MILI);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void setSearchRequest(Runnable run) {
			synchronized (REQUEST_LOCK) {
				this.searchRunnable = run;
			}
		}
	}
	
	public static void waitUntilDone(Single<Boolean> flagSingle) {
		while(!flagSingle.single) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ie) {}
		}
	}
	
	
	@Override
	public void start(Stage primaryStage)	{
		try	{		
			updateUI();
			
			searchThread = new SearchThread();
			searchThread.setPriority(1);
			searchThread.start();   // thread will be running in the background waiting for search requests
			primaryStage.setOnCloseRequest((e) -> {
				windowOpen = false; // used to exit out of while loop of search thread
			});
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Majorizer");
			primaryStage.getIcons().add(ResourceLoader.getImage("favicon.png"));
			primaryStage.show();
			primaryStage.setMaximized(true);
		}	catch(Exception e)	{
			e.printStackTrace();
		}
	}
		
	public static void callLaunch(String[] args) {
		launch(args);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
