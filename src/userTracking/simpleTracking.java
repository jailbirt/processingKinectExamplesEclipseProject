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
	private boolean background,mirror;
	
	
	//Esto es para redefinir el frame ( solo si existe ) ;)
	public void init(){
        if(frame!=null){
          frame.removeNotify();//make the frame displayable
          frame.setResizable(true); //resizable
          frame.setSize(1024,800); //el tamaño del container.
          frame.setUndecorated(false);
          println("frame is at "+frame.getLocation());
          frame.addNotify();
          
        }
        super.init();
  }

	public void setup() {
		background=mirror=true;
		kinect = new SimpleOpenNI(this,SimpleOpenNI.RUN_MODE_MULTI_THREADED);
		kinect.enableDepth();
		// turn on user tracking
		kinect.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
        kinect.setMirror(true);
		skell=new skelleton(this,kinect);
        sphere=new sphere(this,kinect);
    //no lo uso por ahora    particles=new Visual2dParticles(this,kinect,jointDistance);
        jointDistance= new jointDistance(this,kinect);
        size(640,480,OPENGL);
		stroke(255,0,0);
        strokeWeight(5);
        textSize(20);
        
	}

	public void draw() {
	  
		// update the cam
		kinect.update();
		if (background == true) {
		   PImage depth = kinect.depthImage();
		   image(depth, 0, 0); } 
		else {
		      background(255); // quiero que el background no sea el de kinect sino negro.
		}
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
		//		skell.drawJoints(userId);
		//Estaria bueno hacerlo andar...particles.drawParticles();
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
	        if(key == 'b') background=inverseBoolean(background);   
	        if(key == 'm') mirror=inverseBoolean(mirror); 
	 }
	 
	 public boolean inverseBoolean (boolean b) {
	   if (b == true)
		   return false;
	   else 
		   return true;
	 }
	 
	
}
