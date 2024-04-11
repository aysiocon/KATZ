package GameTimer;

import javafx.scene.image.Image;

public class UserPowerUpTimer extends Thread{

	private MovingSprite cat;
	private String type;
	private int time;
	private final static int POWERUP_COUNTDOWN = 5;

	public UserPowerUpTimer(MovingSprite cat, String type){
		this.cat =  cat;
		this.type = type;
		this.time = UserPowerUpTimer.POWERUP_COUNTDOWN;
	}

	void countDown(){
		//update attributes depending on the power up
		if(this.type == "yarn"){
			System.out.println(this.cat.getColor() + " has speed boost!");
			this.cat.doubleSpeed();
		}else if(this.type == "rat"){
			System.out.println(this.cat.getColor() + " has immunity!");
			this.cat.setIsImmune(true);
			this.cat.changeImage(new Image("cats/immunity.gif",this.cat.getSize(),this.cat.getSize(),false,false));
		}

		//timer
		while(this.time != 0){
			try {
				Thread.sleep(1000);
				time--;
			} catch (InterruptedException e) {}
		}

		//revert attributes after 5 seconds
		if(this.type == "yarn"){
			System.out.println(this.cat.getColor() + " has no speed boost!");
			this.cat.refreshSpeed();
		}else if(this.type == "rat"){
			System.out.println(this.cat.getColor() + " has no immunity!");
			this.cat.changeImage(new Image(this.cat.getImgLoc(),this.cat.getSize(),this.cat.getSize(),false,false));
			this.cat.setIsImmune(false);
		}


	}

	public void run(){
		this.countDown();

	}
}
