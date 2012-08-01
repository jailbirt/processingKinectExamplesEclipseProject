package userTracking;
import SimpleOpenNI.*;
import processing.core.*;

public class jointDistance extends PApplet {
	private PApplet parent;
	private SimpleOpenNI kinect;
	private float magnitude;
	private PVector differenceVector;
	
	public jointDistance (PApplet parentO, SimpleOpenNI parentKinectO) {
		parent=parentO;
		kinect=parentKinectO;
		
	}
	
	public float getdifferenceVectorX(){
		return differenceVector.x;
	}
	public float getdifferenceVectorY(){
		return differenceVector.y;
	}
	public void calculateJointsDistance (int userId) {
		PVector leftHand = new PVector();
		PVector rightHand = new PVector();
		kinect.getJointPositionSkeleton(userId,SimpleOpenNI.SKEL_LEFT_HAND,leftHand);
		kinect.getJointPositionSkeleton(userId,
		SimpleOpenNI.SKEL_RIGHT_HAND,rightHand);
		// calculate difference by subtracting one vector from another //1
		differenceVector = PVector.sub(leftHand, rightHand); 
		// calculate the distance and direction of the difference vector.
		//hay que hacerlo antes de normalizar el vector, de lo contrario la magnitud será 1.
		magnitude = differenceVector.mag(); //2
		differenceVector.normalize(); //3 Unit vector, lo lleva a valores de 0 a 1.
		
	}
	
	public void drawLinebetweenNodes (int userId) {
		parent.stroke(differenceVector.x * 255,
				differenceVector.y * 255,
				differenceVector.z * 255);
		parent.strokeWeight(map(magnitude, 100, 1200, 1, 8));
		// draw a line between the two hands
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HAND,SimpleOpenNI.SKEL_RIGHT_HAND); //4
		// display
		parent.pushMatrix(); //5
		  //6 usa x,y,z para rellenar con colores según los ejes.
		  parent.fill(abs(differenceVector.x) * 255, abs(differenceVector.y) * 255,abs(differenceVector.z) * 255);
		  parent.text("m: " + magnitude, 10, 50); //7
		parent.popMatrix();
	 
	}
}
