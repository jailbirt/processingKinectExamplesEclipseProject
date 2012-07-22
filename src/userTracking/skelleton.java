package userTracking;

import SimpleOpenNI.*;
import processing.core.*;

public class skelleton extends simpleTracking {
	private PApplet parent;
	private SimpleOpenNI kinect;
	
	public skelleton (simpleTracking parentO, SimpleOpenNI parentKinectO) {
		parent=parentO;
		kinect=parentKinectO;
	}
	
	public void drawSkeleton(int userId)
	{
		parent.stroke(0);
		parent.strokeWeight(5);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_HEAD,
				SimpleOpenNI.SKEL_NECK);// 1
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_NECK,
				SimpleOpenNI.SKEL_LEFT_SHOULDER);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER,
				SimpleOpenNI.SKEL_LEFT_ELBOW);

		kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW,
				SimpleOpenNI.SKEL_LEFT_HAND);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_NECK,
				SimpleOpenNI.SKEL_RIGHT_SHOULDER);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER,
				SimpleOpenNI.SKEL_RIGHT_ELBOW);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW,
				SimpleOpenNI.SKEL_RIGHT_HAND);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER,
				SimpleOpenNI.SKEL_TORSO);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER,
				SimpleOpenNI.SKEL_TORSO);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_TORSO,
				SimpleOpenNI.SKEL_LEFT_HIP);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP,
				SimpleOpenNI.SKEL_LEFT_KNEE);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE,
				SimpleOpenNI.SKEL_LEFT_FOOT);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_TORSO,
				SimpleOpenNI.SKEL_RIGHT_HIP);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP,
				SimpleOpenNI.SKEL_RIGHT_KNEE);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE,
				SimpleOpenNI.SKEL_RIGHT_FOOT);
		kinect.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP,
				SimpleOpenNI.SKEL_LEFT_HIP);
		parent.noStroke();
		parent.fill(255, 0, 0);
		drawJoint(userId, SimpleOpenNI.SKEL_HEAD); // 2
		drawJoint(userId, SimpleOpenNI.SKEL_NECK);
		drawJoint(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER);
		drawJoint(userId, SimpleOpenNI.SKEL_LEFT_ELBOW);
		drawJoint(userId, SimpleOpenNI.SKEL_NECK);
		drawJoint(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
		drawJoint(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW);
		drawJoint(userId, SimpleOpenNI.SKEL_TORSO);
		drawJoint(userId, SimpleOpenNI.SKEL_LEFT_HIP);
		drawJoint(userId, SimpleOpenNI.SKEL_LEFT_KNEE);
		drawJoint(userId, SimpleOpenNI.SKEL_RIGHT_HIP);
		drawJoint(userId, SimpleOpenNI.SKEL_LEFT_FOOT);
		drawJoint(userId, SimpleOpenNI.SKEL_RIGHT_KNEE);
		drawJoint(userId, SimpleOpenNI.SKEL_LEFT_HIP);
		drawJoint(userId, SimpleOpenNI.SKEL_RIGHT_FOOT);
		drawJoint(userId, SimpleOpenNI.SKEL_RIGHT_HAND);
		drawJoint(userId, SimpleOpenNI.SKEL_LEFT_HAND);
	}

	void drawJoint(int userId, int jointID) { // 3
		PVector joint = new PVector();
		// 4
		float confidence = kinect.getJointPositionSkeleton(userId, jointID,
				joint);
		if (confidence < 0.5) {
			return; // 5
		}

		PVector convertedJoint = new PVector();
		kinect.convertRealWorldToProjective(joint, convertedJoint);
		parent.ellipse(convertedJoint.x, convertedJoint.y, 5, 5);
	}


}
