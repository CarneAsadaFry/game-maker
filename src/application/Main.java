package application;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		VBox root = new VBox(100);
		root.setPadding(new Insets(100, 000, 000, 000));
		root.setAlignment(Pos.TOP_CENTER);
		Scene scene = new Scene(root, 1200, 600);
		
		Label title = new Label("Cool Game Maker");
		title.setFont(Font.font("Haettenschweiler", 64));
		Button load = new Button("Load a game");
		load.setFont(Font.font("Agency FB", 36));
		Button make = new Button("Make a game");
		make.setFont(Font.font("Agency FB", 36));
		
		load.setOnAction(e -> {
			primaryStage.setScene(LevelLoader.start(primaryStage, scene));
		});
		
		make.setOnAction(e -> {
			primaryStage.setScene(LevelMaker.start(primaryStage, scene));
		});
		
		root.getChildren().addAll(title, load, make);
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) throws FileNotFoundException, IOException{
		File sprDir = new File(System.getProperty("user.dir"), "Sprites");
    	sprDir.mkdir();
    	if(!sprDir.exists()){
			OutputStream stream = new FileOutputStream(sprDir);
			stream.close();
    	}
    	File lvlDir = new File(System.getProperty("user.dir"), "Levels");
    	lvlDir.mkdir();
    	if(!lvlDir.exists()){
			OutputStream stream = new FileOutputStream(lvlDir);
			stream.close();
    	}
		launch(args);
	}
}
