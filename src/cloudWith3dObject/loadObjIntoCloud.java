package cloudWith3dObject;
import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.*;
import saito.objloader.*;
import SimpleOpenNI.*;

public class loadObjIntoCloud extends PApplet {
    
	private SimpleOpenNI kinect;
	private OBJModel model;
    private float rotateX;
    private float rotateY;
    private float s=1;
    boolean drawLines = false;
    
    public void setup() {
    	 size(1024, 768, OPENGL);
    	 kinect = new SimpleOpenNI(this);
    	 kinect.enableDepth();
    	 // load the model file
    	 // use triangles as the basic geometry
    	 model = new OBJModel(this, "data/Brick.obj", "relative", TRIANGLES); //3
    	 //model.enableDebug();//Que onda?
    	 model.enableTexture();
    	 // tell the model to translate itself
    	 // to be centered at 0,0
    	 model.translateToCenter(); // 1 arranca centrado.
    	 noStroke();
    	}

    public void draw() {
    	background(255);
    	kinect.update();
    	
    	translate(width/2, height/2, -1000); //2
    	rotateX(radians(180));
    	translate(0, 0, 1400);
    	rotateY(radians(map(mouseX, 0, width, -180, 180)));
    	translate(0, 0, s*-1000);
    	scale(s);

    	
    	// turn on the lights
    	lights();//3
    	noStroke();;
    	// isolate model transformations
    	pushMatrix();
    	  // adjust for default orientation
    	  // of the model
    	  //rotateX(rotateY);
    	  //rotateY(rotateX);
    	  //Esto es porque los programas de edicion manejan otros ejes.
    	  rotateY(radians(-90)); //4
    	  rotateX(radians(-180));
    	  //rotateZ(radians(90));
    	  model.draw();
    	popMatrix();
    	stroke(0);

    	PVector[] depthPoints = kinect.depthMapRealWorld();
    	for (int i = 0; i < depthPoints.length; i+=10) {
    		PVector currentPoint = depthPoints[i];
    		point(currentPoint.x, currentPoint.y, currentPoint.z);
    	}

    }
    	public void mouseDragged() { //Captura movimientos mouse
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
    	    /*if(drawLines){
    	    	model.shapeMode(LINES);
    	    	stroke(0);
    	    	} else {
    	    	model.shapeMode(TRIANGLES);
    	    	noStroke();
    	    	}
    	    	drawLines = !drawLines;
*/
    	}
}

