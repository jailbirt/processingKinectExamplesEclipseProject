package userTracking;
import SimpleOpenNI.*;
import processing.core.*;

public class Visual2dParticles extends PApplet {

	private PApplet parent;
	protected SimpleOpenNI kinect;
	protected jointDistance parentJointsD;
	private int num; //how many particles we'll have in the system. More particles = slower sketch.
	Particle[] particle; //Initialize array of particles of length "num"
	

	public Visual2dParticles () {} //no va a a pasar, siempre va a recibir parametros.
	
	public Visual2dParticles (simpleTracking parentO, SimpleOpenNI parentKinectO, jointDistance parentJointsDO) {
		parent=parentO;
		kinect=parentKinectO;
		parentJointsD=parentJointsDO;
		num=2000;
		particle=new Particle[num];
		this.populateParticles();
	}
	
	public void setParticlesNumber(int number){
		num=number;
	}
	
	public void populateParticles () {
		//fill particle array with new Particle objects
		  for(int i=0; i<particle.length; i++){
		    particle[i] = new Particle(new PVector(random(0, width), random(0, height)), 2, 10, 10,parent);
		  }
	}
	
	public void drawParticles () {
		  //draw trails, trail length can be altered by making alpha value in fill() lower
		  parent.fill(255, 55);
		  parent.rect(0, 0, width, height);
		  //run all the particles in the array every frame
		  for(int i=0; i<particle.length; i++){
		    //particle[i].run(parentJointsD.getdifferenceVectorX(), parentJointsD.getdifferenceVectorY()); //run() method takes two arguments - x and y values to apply forces to
			particle[i].run(parent.mouseX, parent.mouseY); //run() method takes two arguments - x and y values to apply forces to  
		    //System.out.println("Mouse X:"+parent.mouseX+" Y:"+parent.mouseY+"Vector X"+parentJointsD.getdifferenceVectorX()+
		   // 		"Vector Y:"+parentJointsD.getdifferenceVectorY());
		  
		
	      }
     }
}
class Particle extends Visual2dParticles {
	  /*
	    PVector is a class in Processing that makes it easier to store
	    values, and make calculations based on these values. It can make
	    applying forces to objects much easier and more efficient!
	  */
	  PVector loc; //location vector
	  PVector vel; //velocity vector
	  PVector acc; //acceleration vector
	  int sz;  //size of particle
	  float gravity;  //gravity variable
	  float mass;  //mass variable
	  int velocityLimit = 5;  //the maximum velocity a particle can travel at
	  float d;  //distance variable between particle and the target co-ordinates
	  private PApplet parent; //pruebo si por aca funciona.
	  
	  //CONSTRUCTOR
	  public Particle(PVector _loc, int _sz, float _gravity, float _mass,PApplet pa){
	    loc = _loc.get();  //when calling loc, return current location of the selected particle
	    vel = new PVector(0, 0);  //set vel and acc vectors to 0 as default
	    acc = new PVector(0, 0);
	    sz = _sz;
	    gravity = _gravity;
	    mass = _mass;
	    parent = pa;
	  }
	   
	   
	  //method to render the particle. control how it looks here!
	  public void display(){
	 
	    parent.ellipseMode(CENTER);
	    parent.fill(d, 0, 255);
	    parent.ellipse(loc.x, loc.y, sz, sz);

	  }
	   
	  //math for attraction and repulsion forces
	  //tx and ty are the co-ordinates attraction/repulsion will be applied to
	  void forces(float tx, float ty){
	    PVector targetLoc = new PVector(tx, ty);  //creating new vector for attractive/repulsive x and y values
	    PVector dir = PVector.sub(loc, targetLoc);  //calculate the direction between a particle and targetLoc
	    d = dir.mag();  //calculate how far away the particle is from targetLoc
	    dir.normalize();  //convert the measurement to a unit vector
	     
	    //calculate the strength of the force by factoring in a gravitational constant and the mass of a particle
	    //multiply by distance^2
	//    float force = (gravity*mass) / (d*d);
	     
	    if(parent.mousePressed){
	      dir.mult(1);
	    }
	    //else multiply the direction by -1 to switch the direction the particle travels in (attraction)
	    else{
	      dir.mult(-1);
	    }

	    //apply directional vector
	    applyForce(dir);
	  }
	   
	  //method to apply a force vector to the particle
	  void applyForce(PVector force){
	    force.div(mass);
	    acc.add(force);
	  }
	   
	  //method to update the location of the particle, and keep its velocity within a set limit
	  void update(){
	    vel.add(acc);
	    vel.limit(velocityLimit);
	    loc.add(vel);
	    acc.mult(0);
	  }
	   
	  //method to bounce particles of canvas edges
	  void boundsCanvas(){
	    if(loc.y > height || loc.y < 0){
	      vel.y *= -1;
	    }
	    if(loc.x > width || loc.x < 0){
	      vel.x *= -1;
	    }
	  }
	   
	  //main method that combines all previous methods, and takes two arguments
	  //tx and ty are inherited from forces(), and set the attractive/repulsive co-ords
	  void run(float tx, float ty){
	    forces(tx, ty);
	    display();
	    boundsCanvas();
	    update();
	  }
}
