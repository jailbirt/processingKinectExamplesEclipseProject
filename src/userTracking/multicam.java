package userTracking;
import SimpleOpenNI.*;
import processing.core.*;
import processing.opengl.*;
import saito.objloader.*;

public class multicam extends PApplet {
	private SimpleOpenNI cam1;
	private SimpleOpenNI cam2;

		// Esto es para redefinir el frame ( solo si existe ) ;)
		public void init() {
			if (frame != null) {
				frame.removeNotify();// make the frame displayable
				frame.setResizable(true); // resizable
				frame.setSize(1024, 800); // el tamaño del container.
				frame.setUndecorated(false);
				frame.setTitle("PApplet!");
				println("frame is at " + frame.getLocation());
				frame.addNotify();

			}
			super.init();
		}
		
		public void setup() {
			 size(640 * 2 + 10,480 * 2 + 10); 

			  // start OpenNI, loads the library
			  SimpleOpenNI.start();
			  
			  // print all the cams 
			  StrVector strList = new StrVector();
			  SimpleOpenNI.deviceNames(strList);
			  for(int i=0;i<strList.size();i++)
			    println(i + ":" + strList.get(i));

			  // init the cameras
			  cam1 = new SimpleOpenNI(0,this);
			  cam2 = new SimpleOpenNI(1,this);

			  cam2.enableDepth();
		//	  cam2.enableIR();
			  // set the camera generators
			  cam1.enableDepth();
		//	  cam1.enableIR();
			 

			 
			 
			  background(10,200,20);	
		}
		
		public void draw() {
			// update the cam
			  SimpleOpenNI.updateAll();
			  
			  // draw depthImageMap
			  image(cam2.depthImage(),0,0);
		//	  image(cam2.irImage(),0,480 + 10);
			  
			  image(cam1.depthImage(),640 + 10,0);
			//  image(cam1.irImage(),640 + 10,480 + 10);
		}
 }
