package GameTimer;
import java.util.Random;
import java.util.TimerTask;

public class Respawn extends TimerTask {
	private Random r = new Random();
	 public void run() {
		 int val = r.nextInt(2); //ran.choose between [0]rat or [1]yarn
		 Game.getPowerUps().get(val).setVisible(true);
		 Game.getPowerUps().get(val).respawn();
	 }
}
