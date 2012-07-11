package kinectSetup;
import java.util.ArrayList;
import java.util.Iterator;

import processing.core.*;
import SimpleOpenNI.* ;
import toxi.geom.*;
import userTracking.controlPanel;

public class kinect extends PApplet {
	public float   xmin = -1000;
	public float   xmax = 1000;
	public float   ymin = -1000;
	public float   ymax = 1000;
	public float   zmin = -1000;
	public float   zmax = 1000;
	public int     resolution = 2;
	public int     bandsResolution;
	public int     pixelsResolution;
	public int     bandsSeparation = 0;
	public int     pixelSize = resolution;
	public boolean drawBands = true;
	public boolean drawPixels = false;
	public boolean realColor = true;
	public boolean explode = false;
	public boolean disolve = false;
	public boolean follow =  false;
	public boolean sandEffect = false;
	public boolean drawSculpture = false;
	public boolean drawSphere = false;
	public boolean drawBalls = false;
	public boolean drawGrid = false;
	public boolean drawFloor = false;
	
	SimpleOpenNI context;

	float zoomF = 0.35f;
	float rotX  = PI;
	float rotY  = 0;

	kinectUtilities kinectUtilities = new kinectUtilities(this);
	controlPanel    controlPanel;
	
	Particles      par = new Particles (this);
	Bands          band;
	ConnectedLines connectedLine = new ConnectedLines (this);
	ArrayList<Particles>      particlesList = new ArrayList<Particles>();
	ArrayList<Bands>      bandsList = new ArrayList<Bands>();
	Spline3D       splinePoints = new Spline3D();
	ArrayList<Ball>      balls = new ArrayList<Ball>();
	Ball           sph = new Ball(new PVector(0,0,2000),new PVector(0,0,0),100,this);

	public void setup(){
	  //size(1440, 900,P3D)
	  size(1024,768,OPENGL,P3D); // P3D es a partir de la 2.0
	  frameRate(300);
	  perspective((float)(45/(PI/180)),(float)(width)/(float)(height),10.0f,150000.0f);                       
	  context = new SimpleOpenNI(this);
	  context.setMirror(true);
	  context.enableDepth();  
	  context.enableRGB();
	  context.alternativeViewPointDepthToImage();
	  context.enableHands();
	  context.enableGesture();
	  context.addGesture("RaiseHand");
	  context.update();
	  kinectUtilities.calculateLimits(context.depthMap(),context.depthMapRealWorld());
	// falta arreglar.  controlPanel = new controlPanel(this);
	}

	public void draw(){
	  context.update();
	  int[] depthMap = context.depthMap();
	  PVector[] realWorldMap = context.depthMapRealWorld();
	  PImage rgbImage = context.rgbImage();

	  int[] resDepth = kinectUtilities.resizeDepth(depthMap,resolution);
	  PVector[]resMap3D = kinectUtilities.resizeMap3D(realWorldMap,resolution);
	  PImage resRGB = kinectUtilities.resizeRGB(rgbImage,resolution);
	  boolean[] constrainedImg = kinectUtilities.constrainImg(resDepth,resMap3D,xmin,xmax,ymin,ymax,zmin,zmax);
	  int resXsize = context.depthWidth()/resolution;
	  int resYsize = context.depthHeight()/resolution;
	  
	  background(10);
	  translate(width/2,height/2,0);
	  rotateX(rotX);
	  rotateY(rotY);
	  scale(zoomF);
	  translate(0,0,-1500);
	  
	  if(drawFloor){
	    drawFloor(color(150),xmin,xmax,ymin,ymax,zmin,zmax);
	  }

	  if(drawGrid){
	    drawGrid(color(255),xmin,xmax,ymin,ymax,zmin,zmax);
	  }
	  
	  if(!realColor){
	    directionalLight(255,255,255,0,(float)-0.2,1); 
	  }

	  if(drawBands){
	    if(!explode && !disolve){
	      band = new Bands(resMap3D,resRGB,constrainedImg,resXsize,resYsize,this);
	    }
	    else if(explode){
	      band.explode();
	    }
	    else if(disolve){
	      band.disolve(ymin);
	    }
	    
	    if(realColor){
	      band.paint(bandsSeparation);
	    }
	    else{
	      band.paint(color(50,50,255),bandsSeparation);
	    }
	    
	    if(follow){
	      bandsList.add(band);
	      if(bandsList.size() > 30){
	        Bands band1 = (Bands) bandsList.get(15);
	        Bands band2 = (Bands) bandsList.get(0);
	        if(realColor){
	          band1.paint(bandsSeparation);
	          band2.paint(bandsSeparation);
	        }
	        else{
	          band1.paint(color(255,50,50),bandsSeparation);
	          band2.paint(color(50,255,50),bandsSeparation);
	        }
	        bandsList.remove(0);
	      }
	    }
	    else{
	      bandsList.clear();
	    }
	  }

	  if(drawPixels){
	    par = new Particles(resMap3D,resRGB,constrainedImg);
	    //connectedLine = new ConnectedLines(resMap3D,constrainedImg,resXsize,resYsize);
	    if(realColor){
	      par.paint(pixelSize);
	      //connectedLine.paint(color(200));
	    }
	    else{
	      par.paint(pixelSize,color(200));
	      //connectedLine.paint(color(200));
	    }

	    if(follow){
	      particlesList.add(par);
	      if(sandEffect){
	        for(int i = 0; i < particlesList.size()-1; i++){
	          par = (Particles) particlesList.get(i);
	          par.paint(pixelSize,color(200));
	          par.update(ymin);
	        }
	        if(particlesList.size() > 30){
	          particlesList.remove(0);
	        }
	      } 
	      else{
	        if(particlesList.size() > 30){
	          Particles par1 = (Particles) particlesList.get(15);
	          Particles par2 = (Particles) particlesList.get(0);
	          if(realColor){
	            par1.paint(pixelSize);
	            par2.paint(pixelSize);
	          }
	          else{
	            par1.paint(pixelSize,color(200));
	            par2.paint(pixelSize,color(200));
	          }
	          particlesList.remove(0);
	        }
	      }
	    }
	    else{
	      particlesList.clear();
	    }
	  }

	  if(realColor){
	    directionalLight(255,255,255,0,(float) -0.2,1); 
	  }

	  if(drawSculpture){
	    if(splinePoints.getPointList().size() > 2){
	      splinePoints.setTightness((float) 0.25);
	      java.util.List vertices = splinePoints.computeVertices(4);
	      for(int i = 0; i < vertices.size()-2; i++){
	        Vec3D p1 = (Vec3D) vertices.get(i);
	        Vec3D p2 = (Vec3D) vertices.get(i+1);
	        Vec3D p3 = (Vec3D) vertices.get(i+2);
	        float rad1 = 60; // + 20*noise(float(i)*0.1);
	        float rad2 = 60; // + 20*noise((float(i)+0.5)*0.1);
	        float rad3 = 60; // + 20*noise(float(i+1)*0.1);
	        int col = color(255);
	        float frac = 0.2f;
	      
	        cilinder(p1,p2,rad1,rad2,col,frac);
	        connector(p1,p2,p3,rad2,rad3,col,frac);
	      }
	    }
	  }
	  
	  if(drawBalls){
	    for(Iterator i = balls.iterator(); i.hasNext(); ){
	      Ball b = (Ball) i.next();
	      b.paint(color(255));
	      b.update(new PVector(0,-15,0),1);
	      if(b.pos.y < ymin){
	        i.remove();
	      }
	    }  
	  }
	  
	  if(drawSphere){
	    int col = color(0,50+200*noise((float) (frameCount*0.05)),50+200*noise((float) (200+frameCount*0.05)));
	    sph.paint(col);
	    sph.contact(resMap3D,constrainedImg,resXsize);
	    sph.update(new PVector(0,0,0),0.5f);
	  }
	 
	}

	
	
	
//keypressed

public void keyPressed(){
  switch(keyCode){
    case LEFT:
      rotY += 0.1;
      break;
    case RIGHT:
      rotY -= 0.1;
      break;
    case UP:
      if(keyEvent.isShiftDown()){
        zoomF += 0.02;
      }  
      else{
        rotX += 0.1;
      }
      break;
    case DOWN:
      if(keyEvent.isShiftDown()){
        zoomF -= 0.02;
        if(zoomF < 0.04){
          zoomF = (float) 0.04;
        }
      }
      else{
        rotX -= 0.1;
      }
      break;
  }
}

//tranquilamente puede ser otra clase...
void cilinder(Vec3D p1, Vec3D p2, float rad1, float rad2, int col, float frac){
  int steps = 100;
  float ang = TWO_PI/(float)steps;
  noStroke();
  fill(col);

  Vec3D v = p2.sub(p1).normalize();
  float a1 = atan2(v.z,sqrt(sq(v.x)+sq(v.y)));
  float a2 = atan2(v.y,v.x);
  Vec3D per = new Vec3D(-cos(a2)*sin(a1),-sin(a2)*sin(a1),cos(a1));
  Vec3D per1 = per.scale(rad1);
  Vec3D per2 = per.scale(rad2);
  Vec3D step = p2.sub(p1).scale(frac);

  beginShape(QUAD_STRIP);
    for(int i = 0; i <= steps; i++) {
      vertex(p1.x+step.x+per1.x,p1.y+step.y+per1.y,p1.z+step.z+per1.z);
      vertex(p2.x-step.x+per2.x,p2.y-step.y+per2.y,p2.z-step.z+per2.z);
      per1.rotateAroundAxis(v,ang);
      per2.rotateAroundAxis(v,ang);
    }
  endShape();
}

void connector(Vec3D p1, Vec3D p2, Vec3D p3, float rad1, float rad2, int col, float frac){
  int steps = 100;
  float ang = TWO_PI/(float)steps;
  float angSecond = 0;
  noStroke();
  fill(col);

  Vec3D v1 = p2.sub(p1).normalize();
  float a1 = atan2(v1.z,sqrt(sq(v1.x)+sq(v1.y)));
  float a2 = atan2(v1.y,v1.x);
  Vec3D per1 = new Vec3D(-cos(a2)*sin(a1),-sin(a2)*sin(a1),cos(a1));
  per1.scaleSelf(rad1);
  Vec3D step1 = p2.sub(p1).scale(frac);

  Vec3D v2 = p3.sub(p2).normalize();
  a1 = atan2(v2.z,sqrt(sq(v2.x)+sq(v2.y)));
  a2 = atan2(v2.y,v2.x);
  Vec3D per2 = new Vec3D(-cos(a2)*sin(a1),-sin(a2)*sin(a1),cos(a1));
  per2.scaleSelf(rad2);
  Vec3D step2 = p3.sub(p2).scale(frac);

  float minDist = 100000;
  for(int i = 0; i <= steps; i++) {
    float d = per1.distanceTo(per2);
    if(d < minDist){
      minDist = d;
      angSecond = (float)i*ang;
    } 
    per2.rotateAroundAxis(v2,ang);
  }  
  
  per2 = new Vec3D(-cos(a2)*sin(a1),-sin(a2)*sin(a1),cos(a1));
  per2.scaleSelf(rad2);
  per2.rotateAroundAxis(v2,angSecond);
  
  beginShape(QUAD_STRIP);
    for(int i = 0; i <= steps; i++) {
      vertex(p2.x-step1.x+per1.x,p2.y-step1.y+per1.y,p2.z-step1.z+per1.z);
      vertex(p2.x+step2.x+per2.x,p2.y+step2.y+per2.y,p2.z+step2.z+per2.z);
      per1.rotateAroundAxis(v1,ang);
      per2.rotateAroundAxis(v2,ang);
    }
  endShape();
}

//Otra Clase Mano.

String lastGesture;
boolean track = false;

void onRecognizeGesture(String strGesture, PVector idPosition, PVector endPosition){
  println("onRecognizeGesture - strGesture: " + strGesture + ", idPosition: " + idPosition + ", endPosition:" + endPosition);
  lastGesture = strGesture;
  context.removeGesture(strGesture);
  context.startTrackingHands(endPosition);
}

void onCreateHands(int handId, PVector pos, float time){
  println("onCreateHands - handId: " + handId + ", pos: " + pos + ", time:" + time);
  if(drawSculpture && (splinePoints.getPointList().size() == 0)){
    track = true;
  }
}

void onUpdateHands(int handId, PVector pos, float time){
  if(track){
    splinePoints.add(new Vec3D(pos.x,pos.y,pos.z));
  }
  if(drawBalls){
    float velMag = random(50,80);
    float ang1 = random(0,TWO_PI);
    float ang2 = random(0,HALF_PI/3);
    PVector vel = new PVector(velMag*cos(ang1)*sin(ang2),velMag*cos(ang2),velMag*sin(ang1)*sin(ang2));
    balls.add(new Ball(PVector.add(pos,new PVector(0,50,0)),vel,30,this));
  }
}

void onDestroyHands(int handId, float time){
  println("onDestroyHandsCb - handId: " + handId + ", time:" + time);
  context.addGesture(lastGesture);
  track = false;
}

// clase DrawFloor
void drawFloor(int col, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax){
	  float deltaX = (xMax-xMin);
	  float deltaZ = (zMax-zMin);

	  noStroke();
	  fill(col); 
	  beginShape();
	    vertex((float)(xMin-0.1*deltaX),(float)yMin,(float)(zMin-0.1*deltaZ));
	    vertex((float)(xMin-0.1*deltaX),(float)(yMin),(float)(zMax+0.1*deltaZ));
	    vertex((float)(xMax+0.1*deltaX),(float)(yMin),(float)(zMax+0.1*deltaZ));
	    vertex((float)(xMax+0.1*deltaX),(float)yMin,(float)(zMin-0.1*deltaZ));
	  endShape(CLOSE);
	}

	void drawGrid(int col, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax){
	  int steps = 10;
	  float deltaX = (xMax-xMin)/steps;
	  float deltaZ = (zMax-zMin)/steps;

	  smooth();
	  strokeWeight(1);
	  stroke(col);
	  for(int i = 0; i <= steps; i++){
	    line(xMin+i*deltaX,yMin+10,zMin,xMin+i*deltaX,yMin+10,zMax);
	    line(xMin,yMin+10,zMin+i*deltaZ,xMax,yMin+10,zMin+i*deltaZ);
	  }
	  noStroke();
	  noSmooth();
	}


}
