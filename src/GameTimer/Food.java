package GameTimer;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Food extends StaticSprite{
	private Random r = new Random();
	private int id;
	private Game game;
	public final static int SIZE = 20;
	public final static int SIZE_INCREASE = 10;
	public final static int MAX_FOOD = 50;

	public final static Image foodImg = new Image("img/food.png",Food.SIZE, Food.SIZE, false, false);

	public Food(Game game, int id, double xPos, double yPos){
		super(xPos, yPos, Food.foodImg);
		this.id=id;
		this.game = game;
		this.width = Food.SIZE;
		this.height = Food.SIZE;
	}

	@Override
	public void render(GraphicsContext g)
	{
		g.drawImage(this.getImage(), this.xPos - game.getCam().getOffX(), this.yPos - game.getCam().getOffY());
	}

	@Override
	void checkCollision(MovingSprite cat) {
		if(this.collidesWith(cat))
		{
			//spawn in new position
			System.out.println("Fish#" + this.id + " spawns in " + this.xPos + ", " + this.yPos);
			this.xPos = r.nextInt(Game.MAP_SIZE);
			this.yPos = r.nextInt(Game.MAP_SIZE);

			cat.eatFood(true);

		}

	}


}
