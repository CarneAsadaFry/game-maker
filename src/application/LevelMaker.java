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


public class LevelMaker {

	public static void onComboBoxActive(ComboBox<Sprite> characters, ComboBox<Sprite> platforms, ComboBox<Sprite> enemies, ComboBox<Sprite> obstacles, ComboBox<Sprite> goals){
		characters.setOnAction(e -> {
			platforms.valueProperty().set(null);
			enemies.valueProperty().set(null);
			obstacles.valueProperty().set(null);
			goals.valueProperty().set(null);
			selected = characters.getValue();
		});
		platforms.setOnAction(e -> {
			characters.valueProperty().set(null);
			enemies.valueProperty().set(null);
			obstacles.valueProperty().set(null);
			goals.valueProperty().set(null);
			selected = platforms.getValue();
		});
		enemies.setOnAction(e -> {
			characters.valueProperty().set(null);
			platforms.valueProperty().set(null);
			obstacles.valueProperty().set(null);
			goals.valueProperty().set(null);
			selected = enemies.getValue();
		});
		obstacles.setOnAction(e -> {
			characters.valueProperty().set(null);
			platforms.valueProperty().set(null);
			enemies.valueProperty().set(null);
			goals.valueProperty().set(null);
			selected = obstacles.getValue();
		});
		goals.setOnAction(e -> {
			characters.valueProperty().set(null);
			platforms.valueProperty().set(null);
			enemies.valueProperty().set(null);
			obstacles.valueProperty().set(null);
			selected = goals.getValue();
		});
	}
	public static boolean dontExit = false;
	public static String[][] saveLevel = new String[20][10];
	
	public static Sprite selected;
	public static Scene start(Stage primaryStage, Scene menu){
		File[] files = new File(System.getProperty("user.dir") + "/Sprites").listFiles();
		ArrayList<Sprite> characters = new ArrayList<>();
		ArrayList<Sprite> enemies = new ArrayList<>();
		ArrayList<Sprite> obstacles = new ArrayList<>();
		ArrayList<Sprite> platforms = new ArrayList<>();
		ArrayList<Sprite> goals = new ArrayList<>();

		for(File f : files){
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String name = f.getName().substring(0, f.getName().length() - 4);
				String type = br.readLine();
				String[] colors = new String[4900];
				for(int i = 0; i < 4900; i++)
					colors[i] = br.readLine();
				switch(type){
				case "Character": characters.add(new Sprite(name, colors)); break;
				case "Enemy": enemies.add(new Sprite(name, colors)); break;
				case "Obstacle": obstacles.add(new Sprite(name, colors)); break;
				case "Platform": platforms.add(new Sprite(name, colors)); break;
				case "Goal": goals.add(new Sprite(name, colors)); break;
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

		Pane root = new Pane();
		Scene scene = new Scene(root, 1200, 600);
		VBox spriteList = new VBox(20);
		GridPane level = new GridPane();
		Pane panel = new Pane();

		spriteList.setStyle("-fx-border-color: black");
		spriteList.setTranslateY(65);
		Button createSprite = new Button("Create a sprite");
		createSprite.setOnAction(e -> {
			primaryStage.setScene(SpriteMaker.start(primaryStage, menu));
		});

		Label labelChar = new Label("Characters");
		ComboBox<Sprite> characterSelect = new ComboBox<>();
		characterSelect.getItems().addAll(characters);
		Label labelEnem = new Label("Enemies");
		ComboBox<Sprite> enemySelect = new ComboBox<>();
		enemySelect.getItems().addAll(enemies);
		Label labelObst = new Label("Obstacles");
		ComboBox<Sprite> obstacleSelect = new ComboBox<>();
		obstacleSelect.getItems().addAll(obstacles);
		Label labelPlat = new Label("Platforms");
		ComboBox<Sprite> platformSelect = new ComboBox<>();
		platformSelect.getItems().addAll(platforms);
		Label labelGoal = new Label("Goals");
		ComboBox<Sprite> goalSelect = new ComboBox<>();
		goalSelect.getItems().addAll(goals);
		spriteList.getChildren().addAll(createSprite, labelChar, characterSelect, labelEnem, enemySelect, labelObst, obstacleSelect, labelPlat, platformSelect, labelGoal, goalSelect);

		onComboBoxActive(characterSelect, enemySelect, obstacleSelect, platformSelect, goalSelect);

		level.setStyle("-fx-border-color: black");
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 10; j++){
				Group temp = new Group();
				Rectangle t = new Rectangle(50, 50);
				t.setFill(Color.TRANSPARENT);
				t.setStroke(Color.BLACK);
				t.setStrokeWidth(.25);
				temp.getChildren().add(t);
				temp.setStyle("-fx-border-color: black");
				level.add(temp, i, j);
			}
		}
		level.setTranslateX(160);
		level.setTranslateY(65);

		level.requestFocus();

		level.setOnMouseClicked(e -> {
			if(selected != null){
				Group g = (Group)level.getChildren().get((int)(e.getX() / 51) * 10 + (int)(e.getY() / 51));
				saveLevel[(int)(e.getX() / 51)][(int)(e.getY() / 51)] = null;
				g.getChildren().clear();
				Rectangle t = new Rectangle(50, 50);
				t.setFill(Color.TRANSPARENT);
				t.setStroke(Color.BLACK);
				t.setStrokeWidth(.25);
				g.getChildren().add(t);
				if(e.getButton().equals(MouseButton.PRIMARY)){
					int counter = 0;
					for(int i = 0; i < 70; i++){
						for(int j = 0; j < 70; j++){
							Rectangle temp = new Rectangle(5./7, 5./7);
							saveLevel[(int)(e.getX() / 51)][(int)(e.getY() / 51)] = selected.name;
							temp.setFill(Color.valueOf(selected.image[counter++]));	
							temp.setLayoutX(i * 5./7);
							temp.setLayoutY(j * 5./7);
							g.getChildren().add(temp);
						}
					}
				}
			}
		});
		
		Button clear = new Button("Clear Level");
		clear.setTranslateX(160);
		clear.setTranslateY(20);
		Button save = new Button("Save Level");
		save.setTranslateX(420);
		save.setTranslateY(20);
		Button saveExit = new Button("Save Level and Exit");
		saveExit.setTranslateX(620);
		saveExit.setTranslateY(20);
		Button exit = new Button("Exit without Saving");
		exit.setTranslateX(860);
		exit.setTranslateY(20);

		panel.getChildren().addAll(clear, save, saveExit, exit);
		
		clear.setOnAction(e -> {
			for(int i = 0; i < 20; i++){
				for(int j = 0; j < 10; j++){
					Group g = (Group)level.getChildren().get(i * 10 + j);
					saveLevel[i][j] = null;
					g.getChildren().clear();
					Rectangle t = new Rectangle(50, 50);
					t.setFill(Color.TRANSPARENT);
					t.setStroke(Color.BLACK);
					t.setStrokeWidth(.25);
					g.getChildren().add(t);
				}
			}
		});
		
		save.setOnAction(e -> {
			String name = JOptionPane.showInputDialog("Name your level: ");
			if(name == null || name.equals("")){
				dontExit = true;
				return;
			}

			try{
				File file = new File(System.getProperty("user.dir") + "/Levels", name + ".lvl");
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				for(int i = 0; i < 20; i++){
					for(int j = 0; j < 10; j++){
						out.println(saveLevel[i][j]);
					}
				}
				out.close();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		});
		
		saveExit.setOnAction(e -> {
			dontExit = false;
			save.fire();
			if(!dontExit)
				exit.fire();
		});
		
		exit.setOnAction(e -> {
			primaryStage.setScene(menu);
		});
		
		root.getChildren().addAll(spriteList, level, panel);

		return scene;
	}

}
