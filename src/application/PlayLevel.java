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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class PlayLevel {

	static Group[][] textures;
	static String[][] types;
	static int hMotion = 0;
	static boolean canJump = true;
	static boolean jumping = false;
	static double yJumpStart = 0;
	public static Scene start(Stage primaryStage, Scene back, Scene menu, Level level) throws IOException{
		Pane root = new Pane();
		Scene scene = new Scene(root, 1200, 600);
		File[] sprites = new File(System.getProperty("user.dir") + "/Sprites").listFiles();
		textures = new Group[20][10];
		types = new String[20][10];
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 10; j++){
				if(level.level[i][j] != null){
					textures[i][j] = new Group();
					for(File f : sprites){
						if(f.getName().substring(0, f.getName().length() - 4).equals(level.level[i][j])){
							BufferedReader br = new BufferedReader(new FileReader(f));
							types[i][j] = br.readLine();
							for(int k = 0; k < 70; k++){
								for(int l = 0; l < 70; l++){
									Rectangle r = new Rectangle(6./7, 6./7);
									r.setFill(Color.valueOf(br.readLine()));
									r.setLayoutX(k * (6./7));
									r.setLayoutY(l * (6./7));
									textures[i][j].getChildren().add(r);
								}
							}
						}
					}
					textures[i][j].setTranslateX(i * 60);
					textures[i][j].setTranslateY(j * 60);
					root.getChildren().add(textures[i][j]);
				}
			}
		}
		
		Timeline animation = new Timeline(new KeyFrame(Duration.millis(33), e -> {
			for(int i = 0; i < 20; i++){
				for(int j = 0; j < 10; j++){
					if(types[i][j] != null){
						if(types[i][j].equals("Character")){
							textures[i][j].setTranslateX(textures[i][j].getTranslateX() + hMotion * 9);
							boolean falling = true;
							for(int k = 0; k < 20; k++){
								for(int l = 0; l < 10; l++){
									if(!(i == k && j == l) && types[k][l] != null && (types[k][l].equals("Enemy") || types[k][l].equals("Obstacle") || types[k][l].equals("Goal")) 
											&& !((textures[i][j].getTranslateX() >= textures[k][l].getTranslateX() + 60) 
											|| (textures[k][l].getTranslateX() >= textures[i][j].getTranslateX() + 60)
											|| (textures[i][j].getTranslateY() >= textures[k][l].getTranslateY() + 60)
											|| (textures[k][l].getTranslateY() >= textures[i][j].getTranslateY() + 60))) {
										if(types[k][l].equals("Goal"))
											JOptionPane.showConfirmDialog(null, "YOU WIN!!!");
										Platform.exit();

									}	
									if(types[k][l] != null && types[k][l].equals("Platform") && textures[i][j].getTranslateY() + 66 >= textures[k][l].getTranslateY()
											&& ((textures[i][j].getTranslateX() >= textures[k][l].getTranslateX() && textures[i][j].getTranslateX() <= textures[k][l].getTranslateX() + 60)
											|| (textures[i][j].getTranslateX() + 60 >= textures[k][l].getTranslateX() && textures[i][j].getTranslateX() + 60 <= textures[k][l].getTranslateX() + 60))){
										falling = false;
										canJump = true;
									}
									if(jumping && yJumpStart == 0){
										yJumpStart = textures[i][j].getTranslateY();
									}
									if(jumping && yJumpStart - textures[i][j].getTranslateY() < 150){
										textures[i][j].setTranslateY(textures[i][j].getTranslateY() - 15);
										falling = false;
										canJump = false;
									}
								}
							}
							if(falling)
								textures[i][j].setTranslateY(textures[i][j].getTranslateY() + 7);
						}
					}
				}
			}
		}));
		
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent e) {
				switch(e.getCode()){
				case LEFT: hMotion = -1; break;
				case RIGHT: hMotion = 1; break;
				case UP: if(canJump){ jumping = true; yJumpStart = 0;} break;
				default: break;
				}
			}
			
		});
				
		scene.setOnKeyReleased(e -> {
			switch(e.getCode()){
			case LEFT: if(hMotion == -1) hMotion = 0; break;
			case RIGHT: if(hMotion == 1) hMotion = 0; break;
			case UP: jumping = false; break;
			}
		});

		return scene;
	}

}