package GameTimer;

import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Enemies extends MovingSprite implements Runnable {
	private String color;
	private Game game;
	private boolean isAlive;
	private double speed;
	private double size;
	private final static String[] MOVEMENT = {"none","left", "right", "up", "down"};
	private String currentMove;
	private String imgLoc;
	private double time;


	public Enemies(String color, Game game,double xPos, double yPos, Image img, GraphicsContext gc, String imgLoc) {
		super(xPos, yPos, img, color, true, 3, true, false, imgLoc);
		this.color = color;
		this.game = game;
		this.size = UserCat.START_SIZE;
		this.speed = 120/this.size;
		this.isAlive = true;
		this.imgLoc = imgLoc;
	}

	public void run(){

		while(this.isAlive == true){
			Random r = new Random();
			int sec = r.nextInt(10);

			Random m = new Random();
			int move = m.nextInt(Enemies.MOVEMENT.length);
			move(move);
			this.time = sec*2;
			countDown();

		}
	}

	void move(int move){
		if(Enemies.MOVEMENT[move] == "left"){
			this.currentMove = "left";
		}else if(Enemies.MOVEMENT[move] == "right"){
			this.currentMove = "right";
		}else if(Enemies.MOVEMENT[move] == "up"){
			this.currentMove = "up";
		}else if(Enemies.MOVEMENT[move] == "down"){
			this.currentMove = "down";
		}
	}

	void countDown(){
		while(this.time != 0){
			try{
				Thread.sleep(100);
				this.time -= 0.5;
				movement();
			} catch(InterruptedException e){}
		}
		this.currentMove = "none";
	}

	void movement(){
			if(this.currentMove == "left"){
				this.xPos -=this.getSpeed();
			}else if(this.currentMove == "right"){
				this.xPos += this.getSpeed();

			}else if(this.currentMove == "up"){
				this.yPos -= this.getSpeed();

			}else if(this.currentMove == "down"){
    			this.yPos += this.getSpeed();

			}
	}



	public String getColor(){
		return this.color;
	}

	@Override
	public void render(GraphicsContext gc){
		checkIfEdge();
		gc.drawImage(this.getImage(), this.xPos - game.getCam().getOffX(), this.yPos - game.getCam().getOffY());
	}

	@Override
	void checkCollision(MovingSprite spritecat) {
		if(this.collidesWith(spritecat) && spritecat.getIsAlive() && !spritecat.getIsImmune())
		{
			//if mas malaki si pusa na kalaban
			if(this.size > spritecat.getImage().getWidth())
			{
				spritecat.setIsAlive(false);

					System.out.println(this.color + " killed "+spritecat.getColor() + " at " + this.getXPos() +", "+ this.getYPos());
					this.eatEnemy(true, spritecat);


			}
			else if(this.size < spritecat.getImage().getWidth())
			{
				System.out.println(spritecat.getColor() + " killed "+ this.getColor() + " at " + this.getXPos() +", "+ this.getYPos());
				this.setIsAlive(false);

				//IF SI USER ANG KUMAIN
				if(spritecat.getClass().getName().equals("GameTimer.UserCat"))
				{
					spritecat.eatEnemy(true, this);
				}
			}

		}
	}

	@Override
	void eatFood(boolean eaten) {
		System.out.println(this.color + " ate a fish!");
		this.size += Food.SIZE_INCREASE;
		this.img = new Image (this.imgLoc, this.size, this.size, false, false);
		this.loadImage(this.img);
	}

	public double getSpeed(){
		return this.speed;
	}

	@Override
	void eatEnemy(boolean eatenEnemy, MovingSprite sprite) {
		this.size += sprite.getImage().getHeight();
		this.img = new Image (this.imgLoc, this.size, this.size, false, false);
		this.loadImage(this.img);
	}



}
