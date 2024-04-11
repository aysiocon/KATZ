package katz;

import GameTimer.Game;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import katz.About;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Start{
	private Stage stage;
	private Scene splashScene;		// the splash scene
	private Group root;
	private Canvas canvas;			// the canvas where the animation happens

	public final static int WINDOW_WIDTH = 800;
	public final static int WINDOW_HEIGHT = 800;

	private MediaPlayer sfxPlayer;
	private MediaPlayer bgmPlayer;

	public Start(){
		this.canvas = new Canvas( Start.WINDOW_WIDTH, Start.WINDOW_HEIGHT );
		this.root = new Group();
        this.root.getChildren().add( this.canvas );
        this.playBGM(); //TODO: UNCOMMENT
	}

	public void setStage(Stage stage)
	{
		this.stage = stage;
		stage.getIcons().add(new Image("img/logo.png"));
		stage.setTitle("KATZ");
		this.initSplash(stage);

		stage.setScene(this.splashScene);
		stage.setResizable(false);
		stage.show();
	}

	private void initSplash(Stage stage) //root
	{
		StackPane root = new StackPane();
		root.getChildren().addAll(this.createCanvas(), this.createVBox());
		this.splashScene = new Scene(root);
	}

	private Canvas createCanvas() //create background
	{
		Canvas canvas = new Canvas(Start.WINDOW_WIDTH, Start.WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		Image bg = new Image("img/yellowframe.png");
		gc.drawImage(bg, 0, 0);
		return canvas;
	}

	private VBox createVBox()
	{
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(0));
		vbox.setSpacing(0);

		final ImageView title = new ImageView(new Image("img/screen.gif"));
		Button newGame = new Button("");
		Button tutorial = new Button("");
		Button about = new Button("");
		Button back = new Button("");

		this.setButtonDesign(newGame, new ImageView("img/newgame_btn.png"));
		this.setButtonDesign(tutorial, new ImageView("img/tutorial_btn.png"));
		this.setButtonDesign(about, new ImageView("img/about_btn.png"));
		this.setButtonDesign(back, new ImageView("img/back_btn.png"));

		vbox.getChildren().addAll(title, newGame, tutorial, about);

		//back button functionalities
		back.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent arg0)
			{
				displayStart();
			}
		}
		);
		//new game functionalities
		newGame.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent arg0){
				displayGame();
			}
		});
		//about functionalities
		about.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent arg0)
			{
				displayAbout(back);
			}
		}
		);

		//tutorial functionalities
		tutorial.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent arg0)
			{
				displayTutorial(back);
			}
		}
		);
		return vbox;
	}


	private void setButtonDesign(Button button, ImageView imageview)
	{
		button.setStyle("-fx-background-color: transparent");
		button.setGraphic(imageview);
	}


	private void displayStart()
	{
		this.playSound();
		PauseTransition transition = new PauseTransition(Duration.seconds(1));
		transition.play();

		transition.setOnFinished(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				stage.setScene(splashScene);
			}
		});
	}

	protected void displayGame()
	{
		this.stopBGM(); //TODO: UNCOMMENT
		this.playSound();
		PauseTransition transition = new PauseTransition(Duration.seconds(1));
		transition.play();

		transition.setOnFinished(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent arg0){
				Game game = new Game();
				stage.setScene(game.getScene());
			}
		});
	}

	private void displayAbout(Button back)
	{
		this.playSound();
		PauseTransition transition = new PauseTransition(Duration.seconds(1));
		transition.play();

		transition.setOnFinished(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				About about= new About(back);
				stage.setScene(about.getScene());
			}
		});
	}

	private void displayTutorial(Button back)
	{
		this.playSound();
		PauseTransition transition = new PauseTransition(Duration.seconds(1));
		transition.play();

		transition.setOnFinished(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				Tutorial tutorial= new Tutorial(back);
				stage.setScene(tutorial.getScene());
			}
		});
	}

	private void playSound()
	{
		String source = "/sound/buttonsfx.mp3";
		Media sfx = new Media(getClass().getResource(source).toExternalForm());
		sfxPlayer = new MediaPlayer(sfx);
		sfxPlayer.play();
	}

	private void playBGM()
	{
		String source = "/sound/sceneSound.mp3";
		Media bgm = new Media(getClass().getResource(source).toExternalForm());
		bgmPlayer = new MediaPlayer(bgm);
		bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		bgmPlayer.play();
	}

	private void stopBGM()
	{
		if(bgmPlayer != null)
		{
			bgmPlayer.stop();
		}
	}

	public Stage getStage()
	{
		return this.stage;
	}




}
