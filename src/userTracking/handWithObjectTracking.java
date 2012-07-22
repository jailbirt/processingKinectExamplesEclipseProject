package userTracking;
import SimpleOpenNI.*;
import processing.core.*;
import saito.objloader.*;


public class handWithObjectTracking extends PApplet {
	SimpleOpenNI kinect;
	float objscale ;
    OBJModel model1; 
    OBJModel model2; 
    controlWindow  controlPanel;
	
	public void setup() {
		 size(1024, 768, OPENGL);
	    kinect = new SimpleOpenNI(this);
	    kinect.enableDepth();
	    kinect.enableRGB();
	    // turn on user tracking
	    kinect.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
	    
	  //        model2 = new OBJModel(this, "data/kinect.obj", "relative", TRIANGLES); 
	  //        model2.translateToCenter();

	  //    model1 = new OBJModel(this, "data/tennisracquet.obj", "relative", POINTS); 
	  //      model1.translateToCenter();

	      model1 = new OBJModel(this, "data/Ball.obj", "relative", TRIANGLES);
	      model1.translateToCenter();
	    //  controlPanel = new controlPanel((PApplet)this);
	    controlPanel = new controlWindow(this);
	}
	public void draw() {
	    background(255);
        kinect.update();
	    PImage depth = kinect.depthImage();
	    image(depth, 0, 0);
	    //PImage rgb= kinect.rgbImage();
	    //image(rgb,0,0);
	    // make a vector of ints to store the list of users
	    controlPanel.draw();
	    IntVector userList = new IntVector();
	    // write the list of detected users
	    // into our vector
	    kinect.getUsers(userList);
	    // if we found any users
	    if (userList.size() > 0) {
	        // get the first user
	        int userId = userList.get(0);
	        // if we’re successfully calibrated
	        if ( kinect.isTrackingSkeleton(userId)) {
	            // make a vector to store the left hand
	            PVector rightHand = new PVector();
	            // put the position of the left hand into that vector
	            float confidence = kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HAND, rightHand);
	            
	            // convert the detected hand position
	            // to "projective" coordinates
	            // that will match the depth image
	            PVector convertedRightHand = new PVector();
	            kinect.convertRealWorldToProjective(rightHand, convertedRightHand);
	            this.displayObject(convertedRightHand, model1);


	           // convertedRightHand = new PVector();
	           // kinect.convertRealWorldToProjective(rightHand, convertedRightHand);

	           //que pasho? displayObject(convertedRightHand, model2);
	            
	        }
	        
	       
	    }
	}

	void displayObject(PVector position,OBJModel model )
	{
	  if(position.z > 600  && position.z < 2500){
		  pushMatrix();
	            fill(255,0,0);
	            noStroke();

	            translate(position.x, position.y,0);
	            rotateX(radians(90));
	            rotateZ(radians(180));
	            lights();

	            float escale = 0;
	            if(position.z > 600  && position.z < 1300) { escale = map(position.z,600,1300,0.75f,0.45f);  }
	            else if(position.z >= 1300  && position.z < 2500) { escale = map(position.z,1300,2000,0.45f,0.275f);  }

	      scale(escale);
	      model.draw();                          
	      //rect(-50, -100, 100, 200);
	    popMatrix();
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
}

