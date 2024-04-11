package GameTimer;

import javafx.scene.image.Image;

public abstract class MovingSprite extends Sprite {

	public final static int MAX_SPEED = 4;

	private String color;
	protected double speed;
	protected double size;
	private String imgLoc;

	//attributes for powerUp
	private boolean isAlive;
	private boolean isImmune;

	//for refresh
	private double  oldSpeed; //for the refresh method

	public MovingSprite(double xPos, double yPos, Image image, String color, boolean isAlive, int speed, boolean canBeEaten, boolean isImmune, String imgLoc) {
		super(xPos, yPos, image);
		this.color = color;
		this.isAlive = isAlive;
		this.speed = 120/this.size;
		this.size = UserCat.START_SIZE;
		this.isImmune = isImmune;
		this.imgLoc = imgLoc;
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

	abstract void eatFood(boolean eaten);
	abstract void eatEnemy(boolean eatenEnemy, MovingSprite sprite);


	public boolean getIsAlive()
	{
		return this.isAlive;
	}

	public String getColor() {
		return color;
	}

	public double getSpeed(){
		return this.speed;
	}

	public boolean getIsImmune(){
		return this.isImmune;
	}

	public double getSize()
	{
		return this.size;
	}

	public String getImgLoc(){
		return imgLoc;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setIsAlive(boolean val)
	{
		this.isAlive = val;
	}

	//set immunities
	public void setIsImmune(boolean bool){
		this.isImmune = bool;
	}




}
