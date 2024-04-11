package GameTimer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.Random;
public class PowerUp extends StaticSprite{

	final static int SIZE = 20;
	protected Game game;
	private Random r = new Random();
	private UserCat user;
	private String name;

	public PowerUp(String name, double xPos, double yPos, Image image, Game game, UserCat user) {
		super(xPos, yPos, image);
		this.name = name;
		this.game = game;
		this.setUser(user);
		this.width = PowerUp.SIZE;
		this.height = PowerUp.SIZE;
		this.visible = false; //initially false
	}

	@Override
	void checkCollision(MovingSprite cat) {
		// do nothing, override
	}

	@Override
	public void render(GraphicsContext gc){
		if(this.visible)
		{
			gc.drawImage(this.getImage(), this.xPos - game.getCam().getOffX(), this.yPos - game.getCam().getOffY());
		}
		else
		{
			gc.drawImage(null, -1, -1);
		}

	}

	public void respawn(){
		System.out.println("user is at: " + this.getUser().getXPos());
		this.xPos = r.nextInt( (int)
				( (this.getUser().getXPos()+400)-(this.getUser().getXPos()-400)) 	//higherBound - lowerBound
				) + (this.getUser().getXPos()-400);							//plus lowerBound
		this.yPos = r.nextInt( (int)
				( (this.getUser().getYPos()+400)-(this.getUser().getYPos()-400)) 	//higherBound - lowerBound
				) + (this.getUser().getYPos()-400);

		checkIfEdge();
		System.out.println(this.name + " has respawned at: [" + this.xPos + ", "+ this.yPos +"]");
	}


	//getters and setters
	public double getPowerUpXPos(){
		return this.xPos;
	}

	public double getPowerUpYPos(){
		return this.yPos;
	}

	public void setEaten(boolean bool){
	}

	public UserCat getUser() {
		return user;
	}

	public void setUser(UserCat user) {
		this.user = user;
	}



}
