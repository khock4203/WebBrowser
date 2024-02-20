package com.example.browserprogram;// IMPORTS
// These are some classes that may be useful for completing the project.
// You may have to add others.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.stage.Stage;
import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;

/**
 * @author Kevin Hock
 * CS1131, L01, Week 9 In-Class program
 * Web Browser
 */
public class BrowserProgram extends Application {
	// INSTANCE VARIABLES
	// These variables are included to get you started.
	public static final String DEFAULT_WEB_SITE = "https://www.google.com";
	private Stage stage = null;
	private BorderPane borderPane = null;
	private WebView view = null;
	private WebEngine webEngine = null;
	private TextField statusbar = null;
	private HBox toolbar = null;
	private Button backButton=null;
	private Button forwardButton=null;
	private TextField urlField=null;
	private Button helpButton=null;

	Pane helpPane = new Pane ();

	// HELPER METHODS
	/**
	 * Retrieves the value of a command line argument specified by the index.
	 *
	 * @param index - position of the argument in the args list.
	 * @return The value of the command line argument.
	 */
	private String getParameter( int index ) {
		Parameters params = getParameters();
		List<String> parameters = params.getRaw();
		return !parameters.isEmpty() ? parameters.get(index) : "";
	}

	/**
	 * Creates a WebView which handles mouse and some keyboard events, and
	 * manages scrolling automatically, so there's no need to put it into a ScrollPane.
	 * The associated WebEngine is created automatically at construction time.
	 *
	 * @return browser - a WebView container for the WebEngine.
	 */
	private WebView makeHtmlView( ) {
		view = new WebView();
		webEngine = view.getEngine();
		return view;
	}

	/**
	 * Generates the status bar layout and text field.
	 *
	 * @return statusbarPane - the HBox layout that contains the statusbar.
	 */
	private HBox makeStatusBar( ) {
		HBox statusbarPane = new HBox();
		statusbarPane.setPadding(new Insets(5, 4, 5, 4));
		statusbarPane.setSpacing(10);
		statusbarPane.setStyle("-fx-background-color: #9999ff;");
		statusbar = new TextField();
		HBox.setHgrow(statusbar, Priority.ALWAYS);
		statusbarPane.getChildren().addAll(statusbar);
		return statusbarPane;
	}

	private HBox makeToolbar() {
		toolbar = new HBox();
		toolbar.setPadding(new Insets(5, 4, 5, 4));
		toolbar.setSpacing(10);
		toolbar.setStyle("-fx-background-color: #9999ff;");
		backButton = new Button ("<-");
		forwardButton = new Button ("->");
		urlField = new TextField();
		helpButton = new Button("?");
		toolbar.getChildren().addAll(backButton, forwardButton, urlField, helpButton);
		return toolbar;
	}

	// REQUIRED METHODS
	/**
	 * The main entry point for all JavaFX applications. The start method is
	 * called after the init method has returned, and after the system is ready
	 * for the application to begin running.
	 *
	 * NOTE: This method is called on the JavaFX Application Thread.
	 *
	 * @param primaryStage - the primary stage for this application, onto which
	 * the application scene can be set.
	 */
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		borderPane = new BorderPane();

		borderPane.setTop(makeToolbar());
		borderPane.setCenter(makeHtmlView());
		borderPane.setBottom(makeStatusBar());

		webEngine.load(getParameter(0).isEmpty() ? DEFAULT_WEB_SITE: getParameter(0));
		webEngine.getLoadWorker().stateProperty().addListener(
				new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> observableValue, State state, State newState) {
						if (newState == State.SUCCEEDED) {
							stage.setTitle(webEngine.getTitle());
						}
					}
			});

		webEngine.setOnStatusChanged(event -> statusbar.setText(event.getData()));

		backButton.setOnAction( actionEvent -> {
			webEngine.getHistory().go(-1);
		}
		);

		forwardButton.setOnAction( actionEvent -> {
					webEngine.getHistory().go(1);
				}
		);

		helpButton.setOnAction( actionEvent -> {
			Scene scene = new Scene( helpPane, 500, 500 );
			Text text = new Text(50,50,"This is my browser!");
			text.setStroke(Color.rgb(0, 0, 0));
			helpPane.getChildren().add(text);
			Text text2 = new Text(50,70,"You can put any URL in the text box to jump to that website.");
			text2.setStroke(Color.rgb(0, 0, 0));
			helpPane.getChildren().add(text2);
			Text text3 = new Text(50,90,"The Forward and Back buttons can take you to previous websites.");
			text3.setStroke(Color.rgb(0, 0, 0));
			helpPane.getChildren().add(text3);
			stage.setTitle("Help Page");
			stage.setScene(scene);
			stage.show();
				}
		);

		urlField.setOnAction(event -> webEngine.load(urlField.getText()));

		Scene scene = new Scene(borderPane, 1600, 1000);
		stage.setScene(scene);
		stage.setTitle("Kevin's Web Browser");
		stage.show();
	}

	/**
	 * The main( ) method is ignored in JavaFX applications.
	 * main( ) serves only as fallback in case the application is launched
	 * as a regular Java application, e.g., in IDEs with limited FX
	 * support.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

		launch(args);
	}
}