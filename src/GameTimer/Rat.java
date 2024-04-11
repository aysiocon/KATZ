package GameTimer;

import javafx.scene.image.Image;


public class Rat extends PowerUp{

	public Rat(double xPos, double yPos, Image image, Game game, UserCat user) {
		super("RAT", xPos, yPos, image, game, user);
	}


	@Override
	void checkCollision(MovingSprite cat) {

		if(this.collidesWith(cat)){
			UserPowerUpTimer put = new UserPowerUpTimer( cat, "rat");
			put.start();
			this.setVisible(false);
			this.respawn();
		}
	}
}
