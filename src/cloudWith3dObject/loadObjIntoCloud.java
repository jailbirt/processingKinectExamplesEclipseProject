package cloudWith3dObject;
import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.*;
import saito.objloader.*;
import SimpleOpenNI.*;
import peasy.*;

public class loadObjIntoCloud extends PApplet {
    
	private PeasyCam cam;
	private SimpleOpenNI kinect;
	private OBJModel model;
    private float rotateX;
    private float rotateY;
    private float s=1;
    private int background=0;
    private boolean backgroundFlag=true;
    boolean drawLines = false;
    hotPoint hotpoint1;
    hotPoint hotpoint2;

    
    public void setup() {
    	 size(1024, 768, OPENGL);
    	 kinect = new SimpleOpenNI(this);
    	 kinect.enableDepth();
    	 // load the model file, deberia ser clase model.
    	 // use triangles as the basic geometry
    	 model = new OBJModel(this, "data/Brick.obj", "relative", TRIANGLES); //3
    	 //model.enableDebug();//Que onda?
    	 model.enableTexture();
    	 // tell the model to translate itself
    	 // to be centered at 0,0
    	 model.translateToCenter(); // 1 arranca centrado.
    	 // deberia ser clase model.
    	 noStroke();
    	 
    	 cam = new PeasyCam(this,0,0,0, 1000);
    	 hotpoint1 = new hotPoint(200, 200, 800, 150,this);
    	 hotpoint2 = new hotPoint(-200, 200, 800, 150,this);

    	}

    public void draw() {
    	//background(background,background,background); //background dinamico.
    	background(0);
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
    	  rotateY(radians(-90)); //4
    	  rotateX(radians(-180));
    	  model.draw();
    	popMatrix();
    	stroke(255);

    	PVector[] depthPoints = kinect.depthMapRealWorld();
    	for (int i = 0; i < depthPoints.length; i+=5) {
    		PVector currentPoint = depthPoints[i];
    		if ((i%100)==0) { //multiplo de 100 dibujo la linea.
    		  // draw the lines darkly with alpha
    		  stroke(100, 30);
    		  line(0,0,0, currentPoint.x, currentPoint.y, currentPoint.z);
    		}
    		// draw the dots bright green
    		stroke(random(255),random(255),random(255));
    		point(currentPoint.x, currentPoint.y, currentPoint.z);
    		
    		hotpoint1.check(currentPoint);
    		hotpoint2.check(currentPoint);
    	}
    	
    	hotpoint1.draw(this);
    	hotpoint2.draw(this);

    	if (hotpoint1.isHit()) { //5 Multiplico por negativo, porque muevo todo el sistema de x -180grados al comienzo
    		                     // eso me deja alrevez los ejes 'y' y 'z' para que el peasycam se entere.
    		cam.lookAt(hotpoint1.center.x,
    		hotpoint1.center.y * -1,
    		hotpoint1.center.z * -1, 500, 500); //6 500 <- es cuan cerca de ese punto queda, 500 <- el tiempo que tarda en posicionarse.
    		}
    		if (hotpoint2.isHit()) {
    		cam.lookAt(hotpoint2.center.x,
    		hotpoint2.center.y * -1,
    		hotpoint2.center.z * -1, 500, 500);
    		}
    		hotpoint1.clear(); //7
    		hotpoint2.clear();

		if (background <= 255 && backgroundFlag == true) {
			background = background + 5;
			if (background == 255) {
			 backgroundFlag = false;
			}
		} else if (background >= 0 && backgroundFlag == false) {
			background = background - 5;
			if (background == 0) {
				 backgroundFlag = true;
				}
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

