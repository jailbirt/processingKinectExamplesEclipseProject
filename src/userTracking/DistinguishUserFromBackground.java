package userTracking;
import SimpleOpenNI.*;
import processing.core.*;
import processing.opengl.*;

public class DistinguishUserFromBackground extends PApplet {
	 protected SimpleOpenNI kinect;
	 private boolean background, mirror;
	PImage userImage;
	int userID;
	int[] userMap; // Array de enteros de users.
	PImage rgbImage;
	
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
	// user-tracking callbacks!
		public void onNewUser(int userId) {
		  println("onNewUser - userId: " + userId);
		  println("  start pose detection");
			kinect.startPoseDetection("Psi", userId);
		}
		public void onLostUser(int userId)
		{
		  println("onLostUser - userId: " + userId);
		}
		public void onStartCalibration(int userId)
		{
		  println("onStartCalibration - userId: " + userId);
		}

		public void onEndCalibration(int userId, boolean successful) {
		  println("onEndCalibration - userId: " + userId + ", successfull: " + successful);
			if (successful) {
				println(" User calibrated !!!");
				kinect.startTrackingSkeleton(userId);
			} else {
				println(" Failed to calibrate user !!!");
				kinect.startPoseDetection("Psi", userId);
			}
		}

		public void onStartPose(String pose, int userId) {
		  println("onStartPose - userId: " + userId + ", pose: " + pose);
		  println(" stop pose detection");
			kinect.stopPoseDetection(userId);
			kinect.requestCalibrationSkeleton(userId, true);
		}

		// Para que salga windowed 
		 public void keyPressed(){
		        if(key == 'b') background=inverseBoolean(background);   
		        if(key == 'm') mirror=inverseBoolean(mirror); 
		 }
			// UTIL
			public boolean inverseBoolean(boolean b) {
				if (b == true)
					return false;
				else
					return true;
			}
}