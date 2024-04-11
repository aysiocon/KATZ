package katz;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tutorial {

	private Scene scene;
	private ImageView imgView;
	private Image elements = new Image("/img/tutorialElements.gif");

	public Tutorial(Button button)
	{
		Canvas canvas = new Canvas(Start.WINDOW_WIDTH, Start.WINDOW_WIDTH);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		// TODO: Add background to the canvas
		Image background = new Image("/img/tutorialBG.png",Start.WINDOW_WIDTH,Start.WINDOW_WIDTH,false,false);
		gc.drawImage(background, 0, 0);

		// TODO: Create a back button
		Button back = button;
		back.setLayoutY(590);
		back.setLayoutX(190);

		imgView = new ImageView();
		imgView.setImage(this.elements);
		imgView.setLayoutX(65);
		imgView.setLayoutY(190);


		Group root = new Group();
		root.getChildren().addAll(canvas, back, imgView);

		this.scene = new Scene(root, Start.WINDOW_WIDTH, Start.WINDOW_WIDTH);
	}

	Scene getScene(){
		return this.scene;
	}


}
