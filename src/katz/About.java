package katz;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class About {
	private Scene scene;

	public About(Button button){		//constructor
		Canvas canvas = new Canvas(Start.WINDOW_WIDTH, Start.WINDOW_WIDTH);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		// TODO: Add background to the canvas
		Image background = new Image("/img/about.png",Start.WINDOW_WIDTH,Start.WINDOW_WIDTH,false,false);
		gc.drawImage(background, 0, 0);

		// TODO: Create an back button
		Button back = button;
		back.setLayoutY(590);
		back.setLayoutX(190);

		Group root = new Group();
		root.getChildren().addAll(canvas, back);

		this.scene = new Scene(root, Start.WINDOW_WIDTH, Start.WINDOW_WIDTH);

	}

	Scene getScene(){
		return this.scene;
	}



}
