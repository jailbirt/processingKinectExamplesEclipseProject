package userTracking;

import SimpleOpenNI.*;
import processing.core.*;

public class simpleTracking extends PApplet {
	SimpleOpenNI kinect;

	public void setup() {
		size(640, 480);
		kinect = new SimpleOpenNI(this,SimpleOpenNI.RUN_MODE_MULTI_THREADED);
		kinect.enableDepth();
		// turn on user tracking
		kinect.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
    background(0);
	}

	public void draw() {
	  // update the cam
		kinect.update();
		PImage depth = kinect.depthImage();
		image(depth, 0, 0);
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
				// make a vector to store the left hand, el kinect esta enfrentado.
				PVector rightHand = new PVector();
				// put the position of the left hand into that vector
				// 1 es hiper confiable, 0 es nada confiable.
				// confidence es lo que reporta como retorno del metodo.
				float confidence = kinect.getJointPositionSkeleton(userId,
						SimpleOpenNI.SKEL_LEFT_HAND, rightHand);
				// convert the detected hand position
				// to "projective" coordinates
				// that will match the depth image
				PVector convertedRightHand = new PVector();
				//Proyecto en 2d ( saco Z ) lo que veo en 3d.
				// como si siguiera con el dedo algo que pasa del otro lado del vidrio.
				kinect.convertRealWorldToProjective(rightHand,
						convertedRightHand);
				// and display it
				fill(255, 0, 0);
				//uso z para 'escalar' el circulo.
				float ellipseSize = map(convertedRightHand.z, 700, 2500, 50,1);
		        //700 es el numero màs chico y 2500 el màs grande que puedo ver de z.
				if ( confidence > 0.7 ){ //si es un valor aceptable dibujo, sino no ( para que no dibuje basura).
				 println("La confianza es de "+confidence);
				 ellipse(convertedRightHand.x, convertedRightHand.y,
				 ellipseSize, ellipseSize);
				}		

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

}
