package GameTimer;

import katz.Start;

public class Camera {
	private double offX, offY, dY, dX;

	public Camera(double x, double y)
	{
		this.offX = x;
		this.offY = y;
	}

	public void focusUser(UserCat user)
	{
		this.offX = user.getXPos() - Start.WINDOW_WIDTH/2 + user.getImage().getWidth()/2;
		this.offY = user.getYPos() - Start.WINDOW_WIDTH/2 + user.getImage().getHeight()/2;
		checkIfEdge(); //check if lagpas na
	}

	public void move()
	{
		this.offX += this.dX;
		this.offY += this.dY;
		checkIfEdge(); //check if lagpas na
	}

	public void checkIfEdge()
	{
		if(this.offX < 0)
		{
			this.offX = 0;
		}
		else if(this.offX > Game.MAP_SIZE-Start.WINDOW_WIDTH)
		{
			this.offX = Game.MAP_SIZE-Start.WINDOW_WIDTH;
		}

		if(this.offY < 0)
		{
			this.offY=0;
		}
		else if(this.offY > Game.MAP_SIZE-Start.WINDOW_WIDTH)
		{
			this.offY = Game.MAP_SIZE-Start.WINDOW_WIDTH;
		}
	}

	//getters and setters
	public double getdY() {
		return dY;
	}

	public void setdY(double dY) {
		this.dY = dY;
	}

	public double getdX() {
		return dX;
	}

	public void setdX(double dX) {
		this.dX = dX;
	}

	public double getOffX() {
		return offX;
	}

	public void setOffX(double offX) {
		this.offX = offX;
	}

	public double getOffY() {
		return offY;
	}

	public void setOffY(double offY) {
		this.offY = offY;
	}


}
