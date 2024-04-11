package GameTimer;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Sprite {
	protected Image img;
	protected double xPos, yPos, dx, dy;
	protected boolean visible;
	protected double width;
	protected double height;


    public Sprite(double xPos, double yPos, Image image){
		this.xPos = xPos;
		this.yPos = yPos;
		this.loadImage(image);
		this.visible = true;
		this.img = image;
	}




	protected void loadImage(Image image){
		try{
			this.img = image;
	        this.setSize();
		} catch(Exception e)	{
			e.printStackTrace();
		}
	}

	public void changeImage(Image img){
		this.img = img;
	}

	private void setSize(){
		this.width = this.img.getWidth();
		this.height = this.img.getHeight();
	}

    public void render(GraphicsContext gc){
    	gc.drawImage( this.img, this.xPos, this.yPos);
    }

    //collision detector general
	private Rectangle2D getBounds(){
		return new Rectangle2D(this.xPos, this.yPos, this.width, this.height);
	}

	protected boolean collidesWith(Sprite rect2)
	{
		Rectangle2D rectangle1 = this.getBounds();
		Rectangle2D rectangle2 = rect2.getBounds();

//		System.out.println("intersects? "+ rectangle1.intersects(rectangle2));
		return rectangle1.intersects(rectangle2);
	}

	//para di lumagpas sa edge
	public void checkIfEdge()
	{
		//System.out.println("xpos: " + this.xPos); //tracker for debugging purposes
		if(this.xPos < 0 )
		{
			this.xPos = 0;
		}
		else if(this.xPos > Game.MAP_SIZE-this.img.getWidth())
		{
			this.xPos = Game.MAP_SIZE-this.img.getWidth();
		}
		if(this.yPos < 0)
		{
			this.yPos = 0;
		}
		else if(this.yPos > Game.MAP_SIZE-this.img.getWidth())
		{
			this.yPos = Game.MAP_SIZE-this.img.getWidth();
		}
	}

    //getters
	public Image getImage(){
		return this.img;
	}

	public double getXPos(){
		return this.xPos;
	}

	public double getYPos(){
		return this.yPos;
	}

	public boolean getIsVisible() {
		return visible;
	}

	//setters
	public void setDX(double val){
		this.dx = val;

	}

	public void setDY(double val){
		this.dy = val;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	//abstracts
	abstract void checkCollision(MovingSprite cat);



}


