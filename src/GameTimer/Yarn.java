package GameTimer;

import javafx.scene.image.Image;


public class Yarn extends PowerUp{

	public Yarn(double xPos, double yPos, Image image, Game game, UserCat user) {
		super("YARN", xPos, yPos, image, game, user);
	}



	@Override
	void checkCollision(MovingSprite cat) {
		if(this.collidesWith(cat)){
			UserPowerUpTimer cd = new UserPowerUpTimer(cat, "yarn");
			cd.start();
			this.setVisible(false);
			this.respawn();
		}
	}




}
