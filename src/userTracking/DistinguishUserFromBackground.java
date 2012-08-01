package userTracking;
import SimpleOpenNI.*;
import processing.core.*;
import processing.opengl.*;

public class DistinguishUserFromBackground extends PApplet {
	protected SimpleOpenNI kinect;
	PImage userImage;
	int userID;
	int[] userMap; // Array de enteros de users.
	PImage rgbImage;
	
	//Esto es para redefinir el frame ( solo si existe ) ;)
	public void init(){
        if(frame!=null){
          frame.removeNotify();//make the frame displayable
          frame.setResizable(true); //resizable
          frame.setSize(1024,800); //el tamaño del container.
          frame.setUndecorated(false);
          frame.setTitle("PApplet!");
          println("frame is at "+frame.getLocation());
          frame.addNotify();
          
        }
        super.init();
  }
	public void setup() {
		size(640, 480, OPENGL);
		kinect = new SimpleOpenNI(this,SimpleOpenNI.RUN_MODE_MULTI_THREADED);
		kinect.enableDepth();
		kinect.enableUser(SimpleOpenNI.SKEL_PROFILE_NONE); //1 No necesito los nodos.
	}
	
	public void draw() {
		background(0); // 2 
		kinect.update();
		// if we have detected any users
		if (kinect.getNumberOfUsers() > 0) { // 3
			// find out which pixels have users in them
			userMap = kinect.getUsersPixels(SimpleOpenNI.USERS_ALL); // 4 captura los pixels de los users.
			// populate the pixels array
			// from the sketch's current contents
			loadPixels(); // 5 como pushMatrix pero con pixels, solo cambia los que estan en cada user.
			for (int i = 0; i < userMap.length; i++) { // 6 cada pixel en user Map
				// if the current pixel is on a user
				if (userMap[i] != 0) {
					// make it green
					pixels[i] = color(0, 255, 0); // 7 llena a pixels
				}
			}
			// display the changed pixel array 
			updatePixels(); // 8
		}
	}

	public void onNewUser(int uID) {
		userID = uID;
		println("tracking");
	}
	
}