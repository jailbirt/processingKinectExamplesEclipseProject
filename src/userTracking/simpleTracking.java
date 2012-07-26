package userTracking;

import SimpleOpenNI.*;
import processing.core.*;

public class simpleTracking extends PApplet {
	protected SimpleOpenNI kinect;
//	protected simpleTracking thisO;//y singleton ?  y syncronized ?
    private skelleton skell;// la resp. de la subClase skelleton es dibujarlo.
	private sphere sphere; //resp. de la subClase es dibujar la esfera.
	private Visual2dParticles particles; //resp. de la subclase es dibujar las Particulas!
	private jointDistance jointDistance;
	private int width;
	private int height;
	
	public simpleTracking () {
	    width=640;
	    height=480;
	}
	
	public void setup() {
		
		kinect = new SimpleOpenNI(this,SimpleOpenNI.RUN_MODE_MULTI_THREADED);
		kinect.enableDepth();
		// turn on user tracking
		kinect.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
        background(0);
        skell=new skelleton(this,kinect);
        sphere=new sphere(this,kinect);
        particles=new Visual2dParticles(this,kinect,jointDistance);
        jointDistance= new jointDistance(this,kinect);
        size(width,height);
		stroke(255,0,0);
        strokeWeight(5);
        textSize(20);
        
	}

	public void draw() {
	  // update the cam
		kinect.update();
	//	PImage depth = kinect.depthImage();
		//image(depth, 0, 0);
		background(0); // quiero que el background no sea el de kinect sino negro.
		// make a vector of ints to store the list of users
        // Salva la data con un orden fijo ( distinto a PVector que tiene x,y,z values )
        // Puede crecer dinamicamente.
		IntVector userList = new IntVector();
		// write the list of detected users
		// into our vector
		kinect.getUsers(userList);
		// if we found any users
		if (userList.size() > 0) {
			// get the first user
			int userId = userList.get(0);
			// if we’re successfully calibrated
			if (kinect.isTrackingSkeleton(userId)) {				
			    // dibujo de esfera en la mano izquierda
			    sphere.drawRightHandSphere(userId);
			    jointDistance.calculateJointsDistance(userId); //calcula la distancia.
			    jointDistance.drawLinebetweenNodes(userId);
                // dibujo el esqueleto.
				skell.drawKinectApiLines(userId);
				skell.drawJoints(userId);
				particles.drawParticles();
			}
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
	        if(key == 'f') exitFullscreen();
	    }
	  private void exitFullscreen() {
	        frame.setBounds(0,0,width,height);
	        setBounds((screenWidth - width) / 4,(screenHeight - height) / 4,width, height);
	        frame.setLocation((screenWidth - width) / 4,(screenHeight - height) / 4);
	        setLocation((screenWidth - width) / 4,(screenHeight - height) / 4);
	    }
}
