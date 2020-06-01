package application;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class SpriteMaker {

	public static boolean dontExit = false;

	public static Scene start(Stage primaryStage, Scene back) {
		Pane root = new Pane();
		Scene scene = new Scene(root, 1200, 600);
		Pane controls = new Pane();
		StackPane canvas = new StackPane();
		Pane brush = new Pane();
		GridPane sprite = new GridPane();
		for(int i = 0; i < 70; i++){
			for(int j = 0; j < 70; j++){
				Rectangle r = new Rectangle(3, 3);
				r.setFill(Color.TRANSPARENT);
				sprite.add(r, i, j);
			}
		}
		Pane hitbox = new Pane();
		canvas.getChildren().addAll(sprite, hitbox, brush);
		canvas.setStyle("-fx-border-color: black");
		canvas.setTranslateX(605);
		canvas.setTranslateY(232);
		canvas.setMaxSize(canvas.getWidth(), canvas.getHeight());

		Label rgbLabel = new Label("Brush Color");
		rgbLabel.setTranslateX(100);
		rgbLabel.setTranslateY(50);
		Label rgbDesc = new Label("R\n\nG\n\nB");
		rgbDesc.setTranslateX(88);
		rgbDesc.setTranslateY(65);
		Slider R = new Slider(0, 1, 0);
		R.setTranslateX(100);
		R.setTranslateY(70);
		Slider G = new Slider(0, 1, 0);
		G.setTranslateX(100);
		G.setTranslateY(100);
		Slider B = new Slider(0, 1, 0);
		B.setTranslateX(100);
		B.setTranslateY(130);
		Rectangle rgbSample = new Rectangle(40, 40);
		rgbSample.setStroke(Color.BLACK);
		rgbSample.setTranslateX(270);
		rgbSample.setTranslateY(88);
		R.setOnMouseDragged(e -> {
			rgbSample.setFill(Color.color(R.getValue(), G.getValue(), B.getValue()));
		});
		G.setOnMouseDragged(e -> {
			rgbSample.setFill(Color.color(R.getValue(), G.getValue(), B.getValue()));
		});
		B.setOnMouseDragged(e -> {
			rgbSample.setFill(Color.color(R.getValue(), G.getValue(), B.getValue()));
		});

		Label sizeLabel = new Label("Brush Radius");
		sizeLabel.setTranslateX(100);
		sizeLabel.setTranslateY(170);
		TextField size = new TextField("10");
		size.setTranslateX(100);
		size.setTranslateY(190);
		size.setMaxWidth(40);
		Slider sizeSlider = new Slider(0, 100, 10);
		size.setOnKeyReleased(e -> {
			if(size.getText().length() > 0 && Integer.parseInt(size.getText()) > 100){
				size.setText("100");
			}
			if(size.getText().length() == 0)
				sizeSlider.setValue(0);
			else
				sizeSlider.setValue(Integer.parseInt(size.getText()));
		});
		sizeSlider.setOnMouseDragged(e -> {
			size.setText((int)sizeSlider.getValue() + "");
		});
		sizeSlider.setTranslateX(150);
		sizeSlider.setTranslateY(190);

		ToggleGroup drawMode = new ToggleGroup();
		RadioButton rb1 = new RadioButton("Brush");
		rb1.setToggleGroup(drawMode);
		rb1.setSelected(true);
		rb1.setTranslateX(100);
		rb1.setTranslateY(250);
		RadioButton rb2 = new RadioButton("Paint Bucket");
		rb2.setToggleGroup(drawMode);
		rb2.setTranslateX(100);
		rb2.setTranslateY(270);
		RadioButton rb3 = new RadioButton("Eraser");
		rb3.setToggleGroup(drawMode);
		rb3.setTranslateX(100);
		rb3.setTranslateY(290);

		Button clear = new Button("Clear Sprite");
		clear.setTranslateX(100);
		clear.setTranslateY(330);
		clear.setOnAction(e -> {
			for(int i = 0; i < 70; i++){
				for(int j = 0; j < 70; j++){
					((Rectangle)sprite.getChildren().get(i * 70 + j)).setFill(Color.TRANSPARENT);
				}
			}
		});

		Button save = new Button("Save Sprite");
		save.setTranslateX(100);
		save.setTranslateY(430);
		Button saveExit = new Button("Save Sprite and Exit");
		saveExit.setTranslateX(100);
		saveExit.setTranslateY(470);
		Button exit = new Button("Exit without Saving");
		exit.setTranslateX(100);
		exit.setTranslateY(510);

		controls.getChildren().addAll(rgbLabel, rgbDesc, R, G, B, rgbSample, sizeLabel, size, sizeSlider, rb1, rb2, rb3, clear, save, saveExit, exit);

		canvas.requestFocus();
		canvas.setOnMouseMoved(e -> {
			if(!drawMode.getSelectedToggle().equals(rb2)){
				scene.setCursor(Cursor.NONE);
				brush.getChildren().clear();;
				if(e.getSceneX() <= 605 || e.getSceneX() >= 815 || e.getSceneY() <= 232 || e.getSceneY() >= 442)
					return;
				Circle c = new Circle(e.getX(), e.getY(), Integer.parseInt(size.getText()));
				c.setFill(Color.TRANSPARENT);
				c.setStroke(Color.BLACK);
				brush.getChildren().addAll(c);
			}
		});

		canvas.setOnMouseDragged(e -> {
			if(!drawMode.getSelectedToggle().equals(rb2)){
				scene.setCursor(Cursor.NONE);
				brush.getChildren().clear();
				Circle c = new Circle(e.getX(), e.getY(), Integer.parseInt(size.getText()));
				c.setFill(Color.TRANSPARENT);
				c.setStroke(Color.BLACK);
				brush.getChildren().add(c);
				for(int i = 0; i < 70; i++){
					for(int j = 0; j < 70; j++){
						if((i * 3 - e.getX()) * (i * 3 - e.getX()) + (j * 3 - e.getY()) * (j * 3 - e.getY()) <= Integer.parseInt(size.getText()) * Integer.parseInt(size.getText())){
							if(drawMode.getSelectedToggle().equals(rb1))
								((Rectangle)sprite.getChildren().get(i * 70 + j)).setFill(Color.color(R.getValue(), G.getValue(), B.getValue()));
							else
								((Rectangle)sprite.getChildren().get(i * 70 + j)).setFill(Color.TRANSPARENT);
						}
					}
				}
			}
		});

		canvas.setOnMouseClicked(e -> {
			if(!drawMode.getSelectedToggle().equals(rb2)){
				for(int i = 0; i < 70; i++){
					for(int j = 0; j < 70; j++){
						if((i * 3 - e.getX()) * (i * 3 - e.getX()) + (j * 3 - e.getY()) * (j * 3 - e.getY()) <= Integer.parseInt(size.getText()) * Integer.parseInt(size.getText())){
							if(drawMode.getSelectedToggle().equals(rb1))
								((Rectangle)sprite.getChildren().get(i * 70 + j)).setFill(Color.color(R.getValue(), G.getValue(), B.getValue()));
							else
								((Rectangle)sprite.getChildren().get(i * 70 + j)).setFill(Color.TRANSPARENT);
						}
					}
				}
			}else{
				Stack<Point2D> s = new Stack<Point2D>();
				s.push(new Point2D(e.getX() / 3, e.getY() / 3));
				int[] X = {-1, 0, 0, 1};
				int[] Y = {0, -1, 1, 0};
				Rectangle match = new Rectangle();
				match.setFill(((Rectangle)sprite.getChildren().get((int)(e.getX() / 3) * 70 + (int)(e.getY() / 3))).getFill());
				if(match.getFill().equals(Color.color(R.getValue(), G.getValue(), B.getValue())))
					return;
				while(!s.isEmpty()){
					Point2D p = s.pop();
					((Rectangle)sprite.getChildren().get((int)p.getX() * 70 + (int)p.getY())).setFill(Color.color(R.getValue(), G.getValue(), B.getValue()));
					for(int i = 0; i < 4; i++){
						int nx = (int)p.getX() + X[i];
						int ny = (int)p.getY() + Y[i];
						if(nx >= 0 && nx < 70 && ny >= 0 && ny < 70 && ((Rectangle)sprite.getChildren().get(nx * 70 + ny)).getFill().equals(match.getFill()))
							s.push(new Point2D(nx, ny));
					}
				}
			}
		});
		canvas.setOnMouseExited(e -> {
			scene.setCursor(Cursor.DEFAULT);
			brush.getChildren().clear();
		});

		save.setOnAction(e -> {
			String name = JOptionPane.showInputDialog("Name your sprite: ");
			if(name == null || name.equals("")){
				dontExit = true;
				return;
			}

			String[] values = {"Character", "Enemy", "Obstacle", "Platform", "Goal"};
			Object selected = JOptionPane.showInputDialog(null, "Choose a type: ", "Selection", JOptionPane.DEFAULT_OPTION, null, values, "0");
			if(selected == null){
				dontExit = true;
				return;
			}

			try{
				File file = new File(System.getProperty("user.dir") + "/Sprites", name + ".spr");
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				out.println(selected);
				for(int i = 0; i < 70; i++){
					for(int j = 0; j < 70; j++){
						out.println(((Rectangle)sprite.getChildren().get(i * 70 + j)).getFill());
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
			primaryStage.setScene(LevelMaker.start(primaryStage, back));
		});

		root.getChildren().addAll(controls, canvas);

		return scene;
	}
}
