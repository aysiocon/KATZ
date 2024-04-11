package main;

import javafx.application.Application;
import javafx.stage.Stage;
import katz.Start;

public class App extends Application
{

    public static void main(String[] args)
    {
        launch(args);

    }

    @Override
    public void start(Stage stage)
    {
       Start startgame = new Start();
       startgame.setStage(stage);
    }


}