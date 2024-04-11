package GameTimer;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import katz.Start;
import javafx.scene.input.KeyCode;


public class Game extends AnimationTimer {
	private final static Image background = new Image("/img/bg.png",Game.MAP_SIZE,Game.MAP_SIZE,false,false);
	private final static Image TOPLEFT_CIRCLE = new Image ("/img/border.png", 200, 200, false, false);
	public final static int MAP_SIZE = 2400;
	public final static int WINDOW_SIZE = 800;
	public final static int GAMESTATUS_WIDTH = 200;

	private Random r = new Random();
	private Scene scene;
	private GraphicsContext gc;

	private Camera cam;
	private UserCat user;
	private ArrayList<Food> fishes = new ArrayList<Food>();
	//for enemies
	private ArrayList<Enemies> enemies = new ArrayList<Enemies>();
	private ArrayList<Thread> enemyThreads = new ArrayList<Thread>();
	public final static String[] ENEMY_COLOR = {"blue","cyan","green","mint","orange","pink","purple","red","yellow"};
	public final static String[] ENEMY_COLOR_STRING = {
			"cats/cat_blue.gif",
			"cats/cat_cyn.gif",
			"cats/cat_grn.gif",
			"cats/cat_mint.gif",
			"cats/cat_org.gif",
			"cats/cat_pnk.gif",
			"cats/cat_purp.gif",
			"cats/cat_red.gif",
			"cats/cat_ylw.gif"};
	private Image blue = new Image("cats/cat_blue.gif",UserCat.START_SIZE,UserCat.START_SIZE,false,false);
	private Image cyan = new Image("cats/cat_cyn.gif",UserCat.START_SIZE,UserCat.START_SIZE,false,false);
	private Image green = new Image("cats/cat_grn.gif",UserCat.START_SIZE,UserCat.START_SIZE,false,false);
	private Image mint = new Image("cats/cat_mint.gif",UserCat.START_SIZE,UserCat.START_SIZE,false,false);
	private Image orange = new Image("cats/cat_org.gif",UserCat.START_SIZE,UserCat.START_SIZE,false,false);
	private Image pink = new Image("cats/cat_pnk.gif",UserCat.START_SIZE,UserCat.START_SIZE,false,false);
	private Image purple = new Image("cats/cat_purp.gif",UserCat.START_SIZE,UserCat.START_SIZE,false,false);
	private Image red = new Image("cats/cat_red.gif",UserCat.START_SIZE,UserCat.START_SIZE,false,false);
	private Image yellow = new Image("cats/cat_ylw.gif",UserCat.START_SIZE,UserCat.START_SIZE,false,false);
	private Enemies toRemove; //to remove dead enemies

	//for gamestatus
	private Group gameStatsLabel = new Group();
	private Label gamestats = new Label("");
	private ImageView topCircleView = new ImageView();
	private String statusUpdate;
	private long start;

	//for Gameover
	private VBox vbox = new VBox();

	//for music
	private MediaPlayer bgmPlayer;
	private MediaPlayer sfxPlayer;


	//for powerup
	private Image yarnImg = new Image("img/yarnball.png",PowerUp.SIZE,PowerUp.SIZE,false,false);
	private Image ratImg = new Image("img/rat.png",PowerUp.SIZE,PowerUp.SIZE,false,false);

	private static Timer respawnPU = new Timer();
	private static Timer disappearPU = new Timer();
	private static ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();

	public Game() {
		Canvas canvas = new Canvas(Start.WINDOW_WIDTH, Start.WINDOW_WIDTH);

		this.user = new UserCat(this, "gray", "cats/cat_user.gif");
		this.cam = new Camera(0,0);

		//background methods
		this.playBGM();	//TODO: UNCOMMENT
		this.generateFood();
		this.generateEnemy();
		this.spawnEnemy();
		this.runEnemies();
		this.generatePowerUp();
		this.createButtonVbox();

		//1 line below executes the disappear every 5 sec, respawn every 10 seconds
		Game.disappearPU.schedule(new Disappear(), 15000, 10000); //starts at 15 seconds para di magcollide ang interval ni disappear at respawn
		Game.respawnPU.schedule(new Respawn(), 10000, 10000);

		//for game status
		this.redrawLabel(System.nanoTime());
		this.drawLabelCircle();
		gameStatsLabel.getChildren().addAll(topCircleView, this.gamestats);

		//draw bg img
		this.gc = canvas.getGraphicsContext2D();
		gc.drawImage(background, 0, 0);

		Group root = new Group();
		root.getChildren().addAll(canvas, gameStatsLabel, vbox);
		this.scene = new Scene(root, Start.WINDOW_WIDTH, Start.WINDOW_WIDTH);

		this.prepareActionHandlers();
		this.start(); //executes the game
		this.start = System.currentTimeMillis();
	}

	@Override
	public void handle(long currentNanoTime)
    {
		this.getCam().focusUser(this.user);
		this.redrawBGImg();
        this.renderSprites();
        this.checkFoodCollision();
        this.checkEnemyCollision();
        this.checkPowerUpCollision();
        this.checkIfUserAlive();
        this.checkEnemyAlive();
        this.redrawLabel(System.nanoTime());
    }


	private void redrawBGImg()
	{
		this.gc.clearRect(0, 0, Start.WINDOW_WIDTH, Start.WINDOW_WIDTH);
		gc.drawImage(background, 0 - (int) this.cam.getOffX(), 0 - (int)this.cam.getOffY());
	}

	public static String formatInterval(final long interval)
	{
//	    final long hr = TimeUnit.MILLISECONDS.toHours(interval);
	    final long min = TimeUnit.MILLISECONDS.toMinutes(interval) %60;
	    final long sec = TimeUnit.MILLISECONDS.toSeconds(interval) %60;
        return String.format("%02d:%02d", min, sec );
	}


	private void redrawLabel(long currentTime)
	{
		this.statusUpdate = "Food eaten: " + user.getFoodEaten() + "\nKatz eaten: "+ user.getBlobsEaten() + "\nTotal Score: " + user.getTotalScore() + "\nTime: "+formatInterval((System.currentTimeMillis() - this.start));
		this.gamestats.setText(this.statusUpdate);

		//styling
		this.gamestats.setFont(Font.loadFont(Game.class.getResourceAsStream("/fonts/RetroGaming.ttf"), 16)); //load Font
		this.gamestats.setPadding(new Insets(5));
		this.gamestats.setMinWidth(Game.GAMESTATUS_WIDTH);
		this.gamestats.setMaxWidth(Game.GAMESTATUS_WIDTH);
		this.gamestats.setWrapText(false);
		this.gamestats.setTranslateY(70);
		this.gamestats.setTranslateX(605);
		this.gamestats.setTextAlignment(TextAlignment.LEFT);
	}

	private void drawLabelCircle()
	{
		this.topCircleView.setImage(TOPLEFT_CIRCLE);
		this.topCircleView.setX(Start.WINDOW_WIDTH-210);
		this.topCircleView.setY(20);
	}

	void renderSprites()
	{
		//draw cat
		this.user.render(this.gc);
		this.user.move();
		this.cam.move();

		//render fishes, enemies, rats and yarn
		this.renderFood();
		this.renderEnemy();
		this.renderPowerUp();
	}

    //catch key press
	private void prepareActionHandlers() {

		this.scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                moveUser(code);
			}
		});

		this.scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		            public void handle(KeyEvent e){
		            	KeyCode code = e.getCode();
		                stop(code);
		                stopCam(code);
		            }
		        });

    }

    //gets called in handle
    private void moveUser(KeyCode key)
    {
    	//move user and the camera follows
		if(key==KeyCode.W)
		{
			this.user.setDY(-user.getSpeed());
			this.cam.setdY(-user.getSpeed());
		}

		if(key==KeyCode.A)
		{
			this.user.setDX(-user.getSpeed());
			this.cam.setdX(-user.getSpeed());
		}

		if(key==KeyCode.S){
			this.user.setDY(user.getSpeed());
			this.cam.setdY(user.getSpeed());
		}
		if(key==KeyCode.D)
		{
			this.user.setDX(user.getSpeed());
			this.cam.setdX(user.getSpeed());
		}

		//for debugging purposes but can also stay
		if(key==KeyCode.ESCAPE){
			this.user.setIsAlive(false);
		}

	}

	private void stop(KeyCode ke)
	{
		this.user.setDX(0);
		this.user.setDY(0);
	}

	private void stopCam(KeyCode ke)
	{
		this.cam.setdX(0);
		this.cam.setdY(0);
	}

	//generate food
    private void generateFood()
    {
        for(int i = 0; i < Food.MAX_FOOD; i++)
        {
            fishes.add(new Food(this, i, r.nextInt(Game.MAP_SIZE), r.nextInt(Game.MAP_SIZE)));
        }
    }

    private void renderFood()
    {
    	for(Food fish: fishes)
    	{
    		fish.render(this.gc);
    	}
    }


    private void checkFoodCollision()
    {
    	for(Food fish: fishes)
    	{
    		fish.checkCollision(user);
    	}

    	for (Enemies enemyCats : enemies)
    	{
    		for(Food fish: fishes)
        	{
        		fish.checkCollision(enemyCats);
        	}

    	}
    }


    private void checkPowerUpCollision()
    {
    	//check rat
    	Game.powerUps.get(0).checkCollision(this.user);
    	for (Enemies enemyCats : enemies)
    	{
    		Game.powerUps.get(0).checkCollision(enemyCats);	//check later nalang kung pwede ba maboost ang kalaban
    	}

    	//check yarn
    	Game.powerUps.get(1).checkCollision(this.user);
    	for (Enemies enemyCats : enemies)
    	{
    		Game.powerUps.get(1).checkCollision(enemyCats);
    	}
    }


    //for enemy
    private void checkEnemyCollision()
    {
    	//user vs enemies
    	for (Enemies enemyCats : enemies)
    	{
    			enemyCats.checkCollision(this.user);
    	}

    	//enemies vs enemies
    	for (Enemies enemyCats1 : enemies) //check if nagcolide ang dalawang pusa
    	{
    		for (Enemies enemyCats2 : enemies)
    		{
    			if(enemyCats1!=enemyCats2)
    			{
    				enemyCats1.checkCollision(enemyCats2);
    			}
    		}
    	}

    }



    private void generateEnemy(){
    	for(int i = 0; i < 9; i++){
    		switch(Game.ENEMY_COLOR[i]){
    		case "blue": enemies.add(new Enemies(Game.ENEMY_COLOR[i],this, r.nextInt(Game.MAP_SIZE),r.nextInt(Game.MAP_SIZE),this.blue,this.gc, ENEMY_COLOR_STRING[i]));break;
    		case "cyan": enemies.add(new Enemies(Game.ENEMY_COLOR[i],this, r.nextInt(Game.MAP_SIZE),r.nextInt(Game.MAP_SIZE),this.cyan,this.gc, ENEMY_COLOR_STRING[i]));break;
    		case "green": enemies.add(new Enemies(Game.ENEMY_COLOR[i],this, r.nextInt(Game.MAP_SIZE),r.nextInt(Game.MAP_SIZE),this.green,this.gc, ENEMY_COLOR_STRING[i]));break;
    		case "mint": enemies.add(new Enemies(Game.ENEMY_COLOR[i],this,r.nextInt(Game.MAP_SIZE),r.nextInt(Game.MAP_SIZE),this.mint,this.gc, ENEMY_COLOR_STRING[i]));break;
    		case "orange": enemies.add(new Enemies(Game.ENEMY_COLOR[i],this,r.nextInt(Game.MAP_SIZE),r.nextInt(Game.MAP_SIZE),this.orange,this.gc, ENEMY_COLOR_STRING[i]));break;
    		case "pink": enemies.add(new Enemies(Game.ENEMY_COLOR[i],this,r.nextInt(Game.MAP_SIZE),r.nextInt(Game.MAP_SIZE),this.pink,this.gc, ENEMY_COLOR_STRING[i]));break;
    		case "purple": enemies.add(new Enemies(Game.ENEMY_COLOR[i],this,r.nextInt(Game.MAP_SIZE),r.nextInt(Game.MAP_SIZE),this.purple,this.gc, ENEMY_COLOR_STRING[i]));break;
    		case "red": enemies.add(new Enemies(Game.ENEMY_COLOR[i],this,r.nextInt(Game.MAP_SIZE),r.nextInt(Game.MAP_SIZE),this.red,this.gc, ENEMY_COLOR_STRING[i]));break;
    		case "yellow": enemies.add(new Enemies(Game.ENEMY_COLOR[i],this,r.nextInt(Game.MAP_SIZE),r.nextInt(Game.MAP_SIZE),this.yellow,this.gc, ENEMY_COLOR_STRING[i]));break;
    		}
		}
    }

    private void spawnEnemy(){
    	for(int i = 0; i < enemies.size(); i++){
    		enemyThreads.add(new Thread(enemies.get(i)));
    	}
    }

    private void runEnemies(){
    	for(Thread thread : enemyThreads)
    	{
    		thread.start();
    	}
    }

    private void renderEnemy()
    {
    	for(Enemies enemy : enemies)
    	{
    		enemy.render(this.gc);
    	}
    }

    private void checkEnemyAlive()
    {
    	int i=0;
    	for(Enemies enemyCats : enemies)
    	{
    		if(enemyCats.getIsVisible())
    		{
    			if(!enemyCats.getIsAlive())
    			{
    				enemyThreads.get(i).interrupt();
    				enemyCats.setVisible(false);
    				enemyThreads.remove(enemyThreads.get(i));
    				toRemove = enemyCats;
    			}
    		}
    		i++;
    	}
    	removeDeadCats();
    }

    private void removeDeadCats()
    {
    	enemies.remove(toRemove);
    }


    //for drawing game over scene
    private void checkIfUserAlive()
    {
    	if(!this.user.getIsAlive())
    	{
    		if(bgmPlayer != null)
    		{
    			bgmPlayer.stop();
    		}
    		this.playSFX();
    		this.stop();
    		this.drawGameOver();
    	}
    }



    private void generatePowerUp(){
    	Yarn yarn = new Yarn(r.nextInt((int) (UserCat.INITIAL_X+400)-(UserCat.INITIAL_X))+UserCat.INITIAL_X,
				r.nextInt((int) (UserCat.INITIAL_Y+400)-(UserCat.INITIAL_Y))+UserCat.INITIAL_Y,this.yarnImg,this,
				this.user);
    	Rat rat = new Rat(r.nextInt((int) (UserCat.INITIAL_X+400)-(UserCat.INITIAL_X))+UserCat.INITIAL_X,
				r.nextInt((int) (UserCat.INITIAL_Y+400)-(UserCat.INITIAL_Y))+UserCat.INITIAL_Y,this.ratImg,this,
				this.user);

    	Game.powerUps.add(yarn);
    	Game.powerUps.add(rat);
    }

    private void renderPowerUp(){
    	for(PowerUp pup : Game.powerUps){
    		pup.render(this.gc);
    	}
    }

    private void createButtonVbox()
	{
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(0));
		vbox.setSpacing(0);

		Button exit = new Button("");
		exit.setStyle("-fx-background-color: transparent");
		Image exitbtnIMG = new Image("img/exit_btn.png", 200, 60, false, false);
		exit.setGraphic(new ImageView(exitbtnIMG));

		vbox.getChildren().addAll(exit);
		vbox.setVisible(false);

    	this.vbox.setLayoutX(Start.WINDOW_HEIGHT/3);
    	this.vbox.setLayoutY(Start.WINDOW_HEIGHT-250);


		//functionalities
    	exit.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent arg0)
			{
				System.exit(0);
			}
		}
		);

	}


    private void drawGameOver()
    {
    	System.out.println("GAME OVER!");
    	Game.respawnPU.cancel(); //terminates respawn of PU
    	Game.disappearPU.cancel();
    	//redraw label to show
    	this.gameStatsLabel.setVisible(false);
    	this.vbox.setVisible(true);

    	this.gc.fillRect(0, 0, Start.WINDOW_WIDTH, Start.WINDOW_HEIGHT);
    	//heading
    	this.gc.setFont(Font.loadFont(Game.class.getResourceAsStream("/fonts/RetroGaming.ttf"), 100));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText("GAME OVER!", 60, Start.WINDOW_HEIGHT/3);
		//body
		this.gc.setFont(Font.loadFont(Game.class.getResourceAsStream("/fonts/RetroGaming.ttf"), 30));
		this.gc.setFill(Color.YELLOW);
		this.gc.fillText(this.statusUpdate, Start.WINDOW_HEIGHT/3, Start.WINDOW_HEIGHT/2);
    }

    //music
    private void playBGM()
    {
    	String source = "/sound/gameScene.mp3";
		Media bgm = new Media(getClass().getResource(source).toExternalForm());
		bgmPlayer = new MediaPlayer(bgm);
		bgmPlayer.setOnEndOfMedia(new Runnable() {
		public void run() {
			bgmPlayer.seek(Duration.ZERO);
			}
		});
		bgmPlayer.play();
    }

	private void playSFX()
	{
		String source = "/sound/buttonsfx.mp3";
		Media sfx = new Media(getClass().getResource(source).toExternalForm());
		sfxPlayer = new MediaPlayer(sfx);
		sfxPlayer.play();
	}

    //getter
	public Scene getScene() {
		return this.scene;
	}

	public Camera getCam() {
		return cam;
	}

	public static ArrayList<PowerUp> getPowerUps() {
		return powerUps;
	}

	public static Timer getDisappearPU() {
		return disappearPU;
	}

}
