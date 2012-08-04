package userTracking;

import SimpleOpenNI.*;
import processing.core.*;
import processing.opengl.*;
import saito.objloader.*;

public class tresDTracking extends PApplet {
	protected SimpleOpenNI kinect;
	// protected simpleTracking thisO;//y singleton ? y syncronized ?
	private skelleton skell;// la resp. de la subClase skelleton es dibujarlo.
	private boolean background, mirror;
	PImage colorImage;
	boolean gotImage;
	objectFromObj handOBJ;
	objectFromObj torsoOBJ;

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
		background = mirror = false;

		handOBJ = new objectFromObj(this, "kinect.obj");
		torsoOBJ = new objectFromObj(this, "torso.obj");

		kinect = new SimpleOpenNI(this, SimpleOpenNI.RUN_MODE_MULTI_THREADED); // MULTI_THREAD
																				// openni
																				// y
																				// nite
																				// .
		kinect.enableDepth();
		// turn on user tracking
		kinect.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
		kinect.setMirror(true);
		skell = new skelleton(this, kinect);

		size(1024, 800, OPENGL);
		stroke(255, 0, 0);
		strokeWeight(5);
		textSize(20);

	}

	public void draw() {

		// update the cam
		kinect.update();
		if (background == true) {
			PImage depth = kinect.depthImage();
			image(depth, 0, 0);
		} else {
			background(255); // background blanco.
		}
		translate(width / 2, height / 2, 0);
		rotateX(radians(180));

		scale(0.9f);

		// make a dinamic vector of ints to store the list of users
		// Salva la data con un orden fijo ( distinto a PVector que tiene x,y,z
		// values )
		IntVector userList = new IntVector();
		// write the list of detected users
		// into our vector
		kinect.getUsers(userList);
		if (userList.size() > 0) {// if we found any users
			// get the first user
			int userId = userList.get(0);
			// if we’re successfully calibrated
			if (kinect.isTrackingSkeleton(userId)) {
				PVector position = new PVector();
				kinect.getJointPositionSkeleton(userId,SimpleOpenNI.SKEL_TORSO, position);

				PMatrix3D orientation = new PMatrix3D(); // 1
				float confidence = kinect.getJointOrientationSkeleton(userId,
						SimpleOpenNI.SKEL_TORSO, orientation); // 2 Agrego data de orientación a la matrix orientation.
				// Trabajo denuevo sobre las manos.
				// Tomo la posicion de ambas.
				PVector leftHand = new PVector();
				kinect.getJointPositionSkeleton(userId,
						SimpleOpenNI.SKEL_LEFT_HAND, leftHand); // 5
				PVector rightHand = new PVector();
				kinect.getJointPositionSkeleton(userId,
						SimpleOpenNI.SKEL_RIGHT_HAND, rightHand); // 6
				// subtract right from left hand to turn leftHand into a vector
				// representing the difference between them
				leftHand.sub(rightHand);
				// convert leftHand to a unit vector// 7 dejo el vector unitario para la direción.Para calcular desp -9
				// model is rotated so "up" is the x-axis
				PVector modelOrientation = new PVector(1, 0, 0);// 8
				// teniendo en cuenta que es 'largo' y no 'alto', seteando x lo pongo 'horizontal'.
				// calculate angle and axis the dot product tells us the angle between the vectors
				float angle = acos(modelOrientation.dot(leftHand)); // 9 acos es arcoseno.Necesario para rotar.
				// and the cross product tells us the axis against which this angle is defined
				PVector axis = modelOrientation.cross(leftHand);
				// dibujo el esqueleto.
				skell.drawKinectApiLines(userId);
				pushMatrix();
				    skell.draw3dPoint(position, orientation);
				    scale(100);
				    lights();
				    stroke(175);
				    strokeWeight(1);
				    fill(255);
				    torsoOBJ.draw();
				popMatrix();

				pushMatrix(); // 11
				    lights();
				    stroke(175);
				    strokeWeight(1);
				    fill(250);
				    translate(rightHand.x, rightHand.y, rightHand.z); // 12 preparo todo dentro de la matrix
				    // siempre estoy siguiendo la mano derecha.
				    // rotate angle amount around all axis, mantiene alineado los
				    // sistemas de coordenadas.
				    rotate(angle, axis.x, axis.y, axis.z); // 13
				    handOBJ.draw(); // 14
				popMatrix();

				skell.drawJoints(userId);
				// draw x-axis in red
				stroke(255, 0, 0);

			}
		}
	}

	// user-tracking callbacks!
	public void onNewUser(int userId) {
		println("onNewUser - userId: " + userId);
		println("  start pose detection");
		kinect.startPoseDetection("Psi", userId);
	}

	public void onLostUser(int userId) {
		println("onLostUser - userId: " + userId);
	}

	public void onStartCalibration(int userId) {
		println("onStartCalibration - userId: " + userId);
	}

	public void onEndCalibration(int userId, boolean successful) {
		println("onEndCalibration - userId: " + userId + ", successfull: "
				+ successful);
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
	public void keyPressed() {
		if (key == 'b')
			background = inverseBoolean(background);
		if (key == 'm')
			mirror = inverseBoolean(mirror);
	}

	// UTIL
	public boolean inverseBoolean(boolean b) {
		if (b == true)
			return false;
		else
			return true;
	}

}
