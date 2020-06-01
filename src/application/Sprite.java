package application;

public class Sprite {
	String name;
	String type;
	String[] image;

	Sprite(String name, String[] colors){
		this.name = name;
		image = new String[4900];
		for(int i = 0; i < 4900; i++){
			image[i] = colors[i];
		}
	}

	public String toString(){
		return name;
	}
}
