package application;

public class Level {
	String name;
	String[][] level;
	
	Level(String name, String[][] level){
		this.name = name;
		this.level = new String[20][10];
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 10; j++){
				this.level[i][j] = level[i][j];
			}
		}
	}
	
	public String toString(){
		return name;
	}
}
