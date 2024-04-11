package GameTimer;

import java.util.TimerTask;

public class Disappear extends TimerTask {

	 public void run(){
		 Game.getPowerUps().get(0).setVisible(false);
	 	 Game.getPowerUps().get(1).setVisible(false);

	 	 System.out.println(Game.getPowerUps().get(0).getClass() + " has disappeared!");
	 	 System.out.println(Game.getPowerUps().get(1).getClass() + " has disappeared!");
	 }
}
