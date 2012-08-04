package userTracking;

import SimpleOpenNI.*;
import processing.core.*;


//La responsabilidad de esta clase es dibujar los puntos del esqueleto.

public class skelleton extends PApplet {
	private PApplet parent;
	private SimpleOpenNI kinect;
	
	public skelleton (PApplet parentO, SimpleOpenNI parentKinectO) {
		parent=parentO;
		kinect=parentKinectO;
	}
	
	public void drawSkeleton(int userId)
	{
		parent.stroke(0);
		parent.strokeWeight(5);
		this.drawKinectApiLines(userId); //los propios de la api, de donde a donde dibujar.
		parent.noStroke();
		parent.fill(255, 0, 0);
//		this.drawJoints(userId);         //aca dibujo circulo para cada punto, como refere
	}
	
	public void drawKinectApiLines (int userId) {
		//si quiero usar proyeccion en lugar de  this drawlimb es drawlimb a secas.
		this.drawLimb(userId, SimpleOpenNI.SKEL_HEAD,
				SimpleOpenNI.SKEL_NECK);// 1 Es parte de la api, por eso directamente dibujo las lineas que conectan.
		this.drawLimb(userId, SimpleOpenNI.SKEL_NECK,
				SimpleOpenNI.SKEL_LEFT_SHOULDER);
		this.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER,
				SimpleOpenNI.SKEL_LEFT_ELBOW);
		this.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW,
				SimpleOpenNI.SKEL_LEFT_HAND);
		this.drawLimb(userId, SimpleOpenNI.SKEL_NECK,
				SimpleOpenNI.SKEL_RIGHT_SHOULDER);
		this.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER,
				SimpleOpenNI.SKEL_RIGHT_ELBOW);
		this.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW,
				SimpleOpenNI.SKEL_RIGHT_HAND);
		this.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER,
				SimpleOpenNI.SKEL_TORSO);
		this.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER,
				SimpleOpenNI.SKEL_TORSO);
		this.drawLimb(userId, SimpleOpenNI.SKEL_TORSO,
				SimpleOpenNI.SKEL_LEFT_HIP);
		this.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP,
				SimpleOpenNI.SKEL_LEFT_KNEE);
		this.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE,
				SimpleOpenNI.SKEL_LEFT_FOOT);
		this.drawLimb(userId, SimpleOpenNI.SKEL_TORSO,
				SimpleOpenNI.SKEL_RIGHT_HIP);
		this.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP,
				SimpleOpenNI.SKEL_RIGHT_KNEE);
		this.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE,
				SimpleOpenNI.SKEL_RIGHT_FOOT);
		this.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP,
				SimpleOpenNI.SKEL_LEFT_HIP);
		
		// prueba de torso sea cuadrado ;)
		this.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER,
				SimpleOpenNI.SKEL_LEFT_HIP); 
		this.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER,
				SimpleOpenNI.SKEL_RIGHT_HIP);

	}
  //defino el propio drawLimb. este no usa proyexión sino que los arma en 3d.
	void drawLimb (int userId,int jointType1,int jointType2)
    {
    	PVector jointPos1 = new PVector();
    	PVector jointPos2 = new PVector();
    	float confidence;
    	confidence = kinect.getJointPositionSkeleton(userId,jointType1,jointPos1);
    	confidence += kinect.getJointPositionSkeleton(userId,jointType2,jointPos2);
    	parent.stroke(100);
    	parent.strokeWeight(5);
    	if(confidence > 1){ //la suma de los dos confidence, tiene que ser > a 0.5, que +0.5 = 1
    	    parent.line(jointPos1.x, jointPos1.y,jointPos1.z, jointPos2.x,jointPos2.y, jointPos2.z);
    	}
    }

	
	void drawJoints (int userId) 
	{
		drawJoint(userId, SimpleOpenNI.SKEL_HEAD); // 2 Aca dibujo usando processing cada uno de los puntos.
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
	
	void drawJoint(int userId, int jointID) { // 3 Crea el vector, lo pasa al kinect, se popula con la información 
		PVector joint = new PVector();
		// 4
		float confidence = kinect.getJointPositionSkeleton(userId, jointID,
				joint);
		if (confidence < 0.5f) {
			return; // 5 Se muestra segun la confianza, recordar que 1 es mucho.
		}

		PVector convertedJoint = new PVector();
		kinect.convertRealWorldToProjective(joint, convertedJoint);
		parent.ellipse(convertedJoint.x, convertedJoint.y, 5, 5);
	}

	public void draw3dPoint(PVector position, PMatrix3D orientation) {
		// TODO Auto-generated method stub
		 // move to the position of the TORSO, in an isolated sistema de coordenadas.
		   parent.translate(position.x, position.y, position.z);
		   // adopt the TORSO’s orientation
		   // to be our coordinate system
		   parent.applyMatrix(orientation);//3
		   // draw x-axis in red
		   parent.stroke(255, 0, 0); //4
		   parent.strokeWeight(3);
		   parent.line(0, 0, 0, 150, 0, 0);// 0,0,0 origen y 150, 0, 0 destino de x,y,z
		   // draw y-axis in blue
		   parent.stroke(0, 255, 0);
		   parent.line(0, 0, 0, 0, 150, 0);
		   // draw z-axis in green
		   parent.stroke(0, 0, 255);
		   parent.line(0, 0, 0, 0, 0, 150);
	}
	/* creo que la matrix queda por lo que pasa arriba.. 
	public void render3dObj(PVector position, PMatrix3D orientation, objectFromObj obj) {
		// TODO Auto-generated method stub
		 // move to the position of the TORSO, in an isolated sistema de coordenadas.
		   parent.translate(position.x, position.y, position.z);
		   // adopt the TORSO’s orientation
		   // to be our coordinate system
		   parent.applyMatrix(orientation);//3
	}
	*/

}
