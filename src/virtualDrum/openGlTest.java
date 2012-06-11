package virtualDrum;

import processing.core.PApplet;
import processing.opengl.*;


public class openGlTest extends PApplet{


	public void setup() {
	  size(800, 600, OPENGL);
	  background(153);
	}

	public void draw() {
	  line(0, 0, 0, width, height, -200);
	}

}
