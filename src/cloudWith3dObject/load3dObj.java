package cloudWith3dObject;
import processing.core.PApplet;
import processing.opengl.*;
import saito.objloader.*; //1

public class load3dObj extends PApplet {
	
	private OBJModel model; //2
    private float rotateX;
    private float rotateY;
    private float s;
    boolean drawLines = false;
    
    public void setup() {
    	 size(640, 480, OPENGL);
    	 // load the model file
    	 // use triangles as the basic geometry
    	 model = new OBJModel(this, "data/Brick.obj", "relative", TRIANGLES); //3
    	 model.enableDebug();//Que onda?
    	 model.enableTexture();
    	 
    	 // tell the model to translate itself
    	 // to be centered at 0,0
    	 model.translateToCenter(); //4
    	 noStroke();
    	}

    public void draw() { //5
    	background(255);
    	// turn on the lights
    	lights(); //6
    	translate(width/2, height/2, 10); //7
    	rotateX(rotateY);// 8
    	rotateY(rotateX);
    	// pruebo zoom translate(0, 0, -500);
    	// translate(0, 0, s*-1000);
        // scale(s);
    	
    	// tell the model to draw itself
    	model.draw(); //9
    	}
    	public void mouseDragged() {
    	rotateX += (mouseX - pmouseX) * 0.01;
    	rotateY -= (mouseY - pmouseY) * 0.01;
    	}

    	public void keyPressed() {
    	    if (keyCode == 38) {
    	        s = s + 0.01f;
    	    }
    	    if (keyCode == 40) {
    	        s = s - 0.01f;
    	    }
    	    if(drawLines){
    	    	model.shapeMode(LINES);
    	    	stroke(0);
    	    	} else {
    	    	model.shapeMode(TRIANGLES);
    	    	noStroke();
    	    	}
    	    	drawLines = !drawLines;

    	}
}

