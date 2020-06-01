package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class LevelLoader {

	public static Scene start(Stage primaryStage, Scene menu){
		Pane root = new Pane();
		Scene scene = new Scene(root, 1200, 600);
		
		File[] files = new File(System.getProperty("user.dir") + "/Levels").listFiles();
		ArrayList<Level> levelList = new ArrayList<>();
		
		for(File f : files){
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String name = f.getName().substring(0, f.getName().length() - 4);
				String[][] level = new String[20][10];
				for(int i = 0; i < 20; i++)
					for(int j = 0; j < 10; j++)
						level[i][j] = br.readLine();
				levelList.add(new Level(name, level));
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

		Label loadLabel = new Label("Choose a Level!");
		loadLabel.setFont(Font.font("Agency FB", 128));
		loadLabel.setTranslateX(300);
		loadLabel.setTranslateY(50);
		ComboBox<Level> select = new ComboBox<>();
		select.getItems().addAll(levelList);
		select.setTranslateX(450);
		select.setTranslateY(240);
		Button play = new Button("Start Game");
		play.setFont(Font.font("Agency FB", 48));
		play.setTranslateX(650);
		play.setTranslateY(240);
		play.setOnAction(e -> {
			if(select.getValue() != null)
				try {
					primaryStage.setScene(PlayLevel.start(primaryStage, scene, menu, select.getValue()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		});
		Button exit = new Button("Return to main menu");
		exit.setFont(Font.font("Agency FB", 36));
		exit.setTranslateX(450);
		exit.setTranslateY(500);
		exit.setOnAction(e -> {
			primaryStage.setScene(menu);
		});
		root.getChildren().addAll(loadLabel, select, play, exit);
		return scene;
	}

}
