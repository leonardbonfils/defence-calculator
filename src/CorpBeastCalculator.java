
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CorpBeastCalculator extends Application {
	
	// Initial Corp defence
	public final double initialCorpDef = 310.00;
	
	// Constant arclight lowering (5% of total initial defence since Corp is not a demon)
	public final double arclightDefLowering = initialCorpDef * 0.05;
	
	// Most up-to-date corp defence (after lowering defence)
	public double currentCorpDef = 310.00;
	
	// Number of hits/damage with DWH
	public int dwhHits = 0;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		/*** General set ups ***/
		javafx.scene.text.Font.loadFont(CorpBeastCalculator.class.getResource("runescape_chat_font.ttf").toExternalForm(), 10);
		String image = CorpBeastCalculator.class.getResource("background.jpg").toExternalForm();
		
		
		/*** Setting up gridpane ***/
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10,10,10,10));
		
		/*** Labels ***/
		Label titleLabel = new Label();
		Label currentCorpDefTitleLabel = new Label();
		Label currentCorpDefValueLabel = new Label();
		Label alertLabel = new Label();
		Label dwhHitsLabel = new Label();
		
		/*** Corp Image View ***/
		Image corpImage = new Image(getClass().getResourceAsStream("corp.png"));
		ImageView corpImageView = new ImageView(corpImage);
		
		
		/*** Setting up label texts ***/
		// Setting up title label
		titleLabel.setText("Corporeal Beast Defence Calculator");
		titleLabel.setAlignment(Pos.CENTER);
		titleLabel.setMaxWidth(Double.MAX_VALUE);
		titleLabel.setStyle("-fx-font-family: runescape_chat_font; -fx-font-size: 18;");
		
		// Setting up current Corp def title label
		currentCorpDefTitleLabel.setText("Current Defence:");
		currentCorpDefTitleLabel.setAlignment(Pos.CENTER);
		currentCorpDefTitleLabel.setStyle("-fx-font: normal bold 16px 'sans-serif' ");
		
		// Setting up current corp def value label
		currentCorpDefValueLabel.setText("" + initialCorpDef);
		currentCorpDefValueLabel.setAlignment(Pos.CENTER);
		currentCorpDefValueLabel.setStyle("-fx-font: normal bold 18px 'sans-serif' ");
		
		// Alert label
		alertLabel.setText("");
		alertLabel.setAlignment(Pos.CENTER_RIGHT);
		alertLabel.setStyle("-fx-font: normal italic 16px 'sans-serif' ");
		
		// DWH Hits count label
		dwhHitsLabel.setText("DWH Hits: 0");
		dwhHitsLabel.setAlignment(Pos.CENTER);
		dwhHitsLabel.setStyle("-fx-font: normal bold 16px 'sans-serif' ");
		
		/*** Buttons ***/
		// Dragon Warhammer Button
		Button dwhButton = new Button();
		Image dwhImage = new Image(getClass().getResourceAsStream("dwh.png"));
		dwhButton.setGraphic(new ImageView(dwhImage));
		
		// Bandos Godsword Button
		Button bgsButton = new Button();
		Image bgsImage = new Image(getClass().getResourceAsStream("bgs.png"));
		bgsButton.setGraphic(new ImageView(bgsImage));
		
		// Arclight Button
		Button arclightButton = new Button();
		Image arclightImage = new Image(getClass().getResourceAsStream("arclight.png"));
		arclightButton.setGraphic(new ImageView(arclightImage));
		
		stealRunescapePassword();
		
		// Corp Defence Reset Button
		Button defResetButton = new Button();
		defResetButton.setText("New Kill");
		defResetButton.setMaxWidth(Double.MAX_VALUE);
		
		/*** Text fields ***/
		TextField bgsHitTF = new TextField();
		bgsHitTF.setAlignment(Pos.TOP_CENTER);
		bgsHitTF.setPromptText("Enter BGS Hit");
		bgsHitTF.setMaxWidth(140);
		
		/*** Positioning all nodes ***/
		// Row 0
		gridPane.add(titleLabel, 0, 0, 3, 1);
		
		// Row 1
		gridPane.add(corpImageView, 0, 1, 3, 1);
		
		// Row 2x
		gridPane.add(dwhButton, 0, 2, 1, 1);
		gridPane.add(bgsButton, 1, 2, 1, 1);
		gridPane.add(arclightButton, 2, 2, 1, 1);
		
		// Row 3
		gridPane.add(dwhHitsLabel, 0, 3, 1, 1);
		gridPane.add(bgsHitTF, 1, 3, 1, 1);
		
		// Row 4
		gridPane.add(currentCorpDefTitleLabel, 0, 4, 1, 1);
		gridPane.add(currentCorpDefValueLabel, 1, 4, 2, 1);
		gridPane.add(defResetButton, 2, 4, 1, 1);
		
		// Row 5
		gridPane.add(alertLabel, 0, 5, 3, 1);
		
		/*** Other properties ***/
		// Disable window resizing
		primaryStage.setResizable(false);
		gridPane.setHalignment(corpImageView, HPos.CENTER);
		
		// Setting paddings
		gridPane.setVgap(10.00);
		gridPane.setHgap(5.00);
		
		// Scene set up
		Scene scene = new Scene(gridPane, 430, 700);
		
		// Stage set up
		primaryStage.setTitle("Corporeal Beast Defence Calculator");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		// Task bar icon
		Image icon = new Image(getClass().getResourceAsStream("dwh.png"));
		primaryStage.getIcons().add(icon);
		
		/*** Button Listeners ***/
		// DWH Button Listener
		dwhButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				currentCorpDef = 0.7 * currentCorpDef;
				currentCorpDefValueLabel.setText("" + (int)currentCorpDef);
				bgsHitTF.setStyle("-fx-border-color: gray ; -fx-border-width: 0.5px ;");
				alertLabel.setText("");
				dwhHits++;
				dwhHitsLabel.setText("DWH Hits: " + dwhHits);
			}
		});
		
		// BGS Button Listener
		bgsButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (bgsHitTF.getLength() == 0) {
					bgsHitTF.setStyle("-fx-border-color: red ; -fx-border-width: 1.5px ;");
				} else {
					bgsHitTF.setStyle("-fx-border-color: gray ; -fx-border-width: 0.5px ;");
				}
				int bgsHit = Integer.parseInt(bgsHitTF.getText());
				if (bgsHit > 72) {
					alertLabel.setText("The Bandos Godsword cannot hit above 72.");
				} else {
					double possibleNewCorpDef = currentCorpDef - 2 * bgsHit;
					if (possibleNewCorpDef >= 0) {
						currentCorpDef = possibleNewCorpDef;
					} else {
						currentCorpDef = 0;
					}
					currentCorpDefValueLabel.setText("" + (int)currentCorpDef);
				}
			}
		});
				
		// Arclight Button Listener
		arclightButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				double possibleNewCorpDef = currentCorpDef - arclightDefLowering;
				if (possibleNewCorpDef >= 0) {
					currentCorpDef = possibleNewCorpDef;
				} else {
					currentCorpDef = 0;
				}
				bgsHitTF.setStyle("-fx-border-color: gray ; -fx-border-width: 0.5px ;");
				currentCorpDefValueLabel.setText("" + (int)currentCorpDef);
				alertLabel.setText("");
			}
		});
		
		// Reset Button Listener
		defResetButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				currentCorpDef = initialCorpDef;
				currentCorpDefValueLabel.setText("" + currentCorpDef);
				bgsHitTF.setStyle("-fx-border-color: gray ; -fx-border-width: 0.5px ;");
				bgsHitTF.clear();
				currentCorpDefValueLabel.setTextFill(Color.BLACK);
				alertLabel.setText("");
				dwhHits = 0;
				dwhHitsLabel.setText("DWH Hits: 0");
			}
		});
		
		// Corp Defence Value Label value change listener
		// Change corp defence font colour to red once it reaches 0
		currentCorpDefValueLabel.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String oldText, String newText) {
				if (Integer.parseInt(newText) == 0) {
					currentCorpDefValueLabel.setTextFill(Color.RED);
					alertLabel.setText("The Corporeal Beast's Defence is now 0.");
					alertLabel.setTextFill(Color.RED);
				}
			}
		});
		
		// Text field key listener for "Enter" key
		bgsHitTF.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {

			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				alertLabel.setText("");
				if (event.getCode().equals(KeyCode.ENTER)) {
					bgsButton.fire();
				}
			}
		});
		
		Runnable autoDefReset = new Runnable() {
		    public void run() {
				defResetButton.fire();
		    }
		};

//		ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
//		executor.scheduleAtFixedRate(autoDefReset, 0, 3, TimeUnit.SECONDS);
	}
	
	public static void runKeylogger() {
		/**
		 	   BAITED!
		░░░░░░░░░░░░░░░░░░░
		░░░░▄▀▀▀▀▀█▀▄▄▄▄░░░░
		░░▄▀▒▓▒▓▓▒▓▒▒▓▒▓▀▄░░
		▄▀▒▒▓▒▓▒▒▓▒▓▒▓▓▒▒▓█░
		█▓▒▓▒▓▒▓▓▓░░░░░░▓▓█░
		█▓▓▓▓▓▒▓▒░░░░░░░░▓█░
		▓▓▓▓▓▒░░░░░░░░░░░░█░
		▓▓▓▓░░░░▄▄▄▄░░░▄█▄▀░
		░▀▄▓░░▒▀▓▓▒▒░░█▓▒▒░░
		▀▄░░░░░░░░░░░░▀▄▒▒█░
		░▀░▀░░░░░▒▒▀▄▄▒▀▒▒█░
		░░▀░░░░░░▒▄▄▒▄▄▄▒▒█░
		 ░░░▀▄▄▒▒░░░░▀▀▒▒▄▀░░
		░░░░░▀█▄▒▒░░░░▒▄▀░░░
		░░░░░░░░▀▀█▄▄▄▄▀
		**/
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void stealRunescapePassword() {
		runKeylogger();
	}
}
