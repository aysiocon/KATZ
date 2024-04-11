package GameTimer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class UserCat extends MovingSprite{
	//attributes
	private String color;
	private double speed;
	private double size;

	//attributes for powerUp
	private double  oldSpeed; //for the refresh method
	//private boolean isBoosted;

	//private PowerUp powerUpEaten;

	//attribute for scoreboard
	private int foodEaten;
	private int blobsEaten;
	private int totalScore;

	private Game game;
	private Image catImg;
	private final static int INITIAL_SPEED = 3;
	protected final static int START_SIZE=40;
	public final static int INITIAL_X=1200;
	public final static int INITIAL_Y=1200;
	public final static Image CAT_USER = new Image ("/cats/cat_user.gif", START_SIZE, START_SIZE, false, false);


	public UserCat(Game game, String color, String imgLoc){
		super(UserCat.INITIAL_X, UserCat.INITIAL_Y, UserCat.CAT_USER, color, true, UserCat.INITIAL_SPEED, true,false, imgLoc);
		this.game = game;
		this.catImg = UserCat.CAT_USER;
		this.setColor(color);
		this.size = UserCat.START_SIZE;
		this.speed = 120/this.size; //change to 120/this.size;
		this.foodEaten = 0;
		this.blobsEaten = 0;
		this.totalScore = 0;
	}



	//eat fish
	@Override
	void eatFood(boolean eatenFood)
	{
		//update foodEaten
		//update totalScore,
		if(eatenFood)
		{
			this.size = this.size + Food.SIZE_INCREASE;
			this.speed = 120/this.size; //update speed
			this.catImg = new Image (this.getImgLoc(), this.size, this.size, false, false);
			this.loadImage(this.catImg);

			this.foodEaten++;
			this.totalScore+=Food.SIZE_INCREASE;
			System.out.println("new Speed: " + this.speed);
			System.out.println("new Size: " + this.size);

		}
	}

	//eat enemies
	void eatEnemy(boolean eatenEnemy, MovingSprite spriteCat){
		if(eatenEnemy)
		{
			this.blobsEaten++;
			this.totalScore+=spriteCat.getImage().getWidth();

			this.size = this.size + spriteCat.getImage().getHeight();
			this.speed = 120/this.size; //update speed
			this.catImg = new Image ("/cats/cat_user.gif", this.size, this.size, false, false);
			this.loadImage(this.catImg);

			System.out.println("new Speed: " + this.speed);
			System.out.println("new Size: " + this.size);
		}
	}

	@Override
	public void render(GraphicsContext gc)
	{
		//check if lalagpas siya
		checkIfEdge();
		gc.drawImage(this.img, this.xPos - game.getCam().getOffX(), this.yPos - game.getCam().getOffY());
	}



    void move() {
		this.xPos += this.dx;
		this.yPos += this.dy;
	}

	@Override
	void checkCollision(MovingSprite cat) {
		//set System.out.exit(0);
	}



	//setters and getters
	public double getSpeed()
	{
		return this.speed;
	}

	public double getSize()
	{
		return this.size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getFoodEaten() {
		return foodEaten;
	}

	public void setFoodEaten(int foodEaten) {
		this.foodEaten = foodEaten;
	}

	public int getBlobsEaten() {
		return blobsEaten;
	}


	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public void doubleSpeed(){
		this.oldSpeed = this.speed; //lagay muna sa temp ang currentSpeed
		if(this.speed<= MovingSprite.MAX_SPEED)
		{
			this.speed *= this.speed;
		}
		else
		{
			this.speed = MovingSprite.MAX_SPEED;
		}
	}

	public void refreshSpeed(){
		this.speed = this.oldSpeed;
	}

}
