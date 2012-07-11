package kinectSetup;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;


public class Bands extends PApplet {
	  kinect parent;
	  PVector[] map3D;
	  PImage rgbImg;
	  boolean[] consImg; 
	  PVector[] vel;
	  int xSize;
	  int ySize;
	  int nPixels;
	    
	  Bands(PVector[] tempMap3D, PImage tempRGBimg, boolean[] tempConsImg, int tempXsize, int tempYsize, kinect p){
	    map3D = tempMap3D;
	    rgbImg = tempRGBimg;
	    consImg = tempConsImg;
	    xSize = tempXsize;
	    ySize = tempYsize;
	    nPixels = consImg.length;
	    vel = new PVector[nPixels];
	    for(int i = 0; i < nPixels; i++){
	      vel[i] = new PVector(0,0,0);
	    }
	    parent = p ;
	  }
	  
	  void paint(int col, int gaps){
	    noStroke();
	    fill(col);
	    PVector p, lp;
	    int index, indexl;
	    boolean started = false;
		PVector prevPoint = new PVector(0,0,0);
	    float maxSep = 50;
	    println("ESTOY EN PAINT");
	    
	    for(int y = 0; y < ySize-1; y+=(1+gaps)){
	      for(int x = 0; x < xSize; x++){
	        index = x + y*xSize;
	        if(consImg[index]){
	          p = map3D[index];
	          if(!started){
	            beginShape(TRIANGLE_STRIP);
	            vertex(p.x,p.y,p.z);
	            prevPoint.set(p);
	            started = true;
	          }
	          else if(prevPoint.dist(p) < maxSep){  
	            vertex(p.x,p.y,p.z);         
	            prevPoint.set(p);
	          }
	          else{
	            endShape();
	            started = false;
	            x--;
	            continue;
	          }           
	          indexl = x + (y+1)*xSize;
	          lp = map3D[indexl];
	          if(consImg[indexl] && (p.dist(lp) < maxSep)){
	            vertex(lp.x,lp.y,lp.z);
	            if(x == xSize-1){
	              endShape();
	              started = false;
	            }
	          } 
	          else{
	            vertex(p.x,p.y,p.z);
	            endShape();
	            started = false;
	          }
	        }
	        else if(started){
	          endShape();
	          started = false;
	        }
	      }
	    }
	  }
	  
	  void paint(int gaps){
	    parent.noStroke();
	    PVector p, lp;
	    int index, indexl;
	    boolean started = false;
	    PVector prevPoint = new PVector(0,0,0);
	    float maxSep = 50;

	    for(int y = 0; y < ySize-1; y+=(1+gaps)){
	      for(int x = 0; x < xSize; x++){
	        index = x + y*xSize;
	        if(consImg[index]){
	          p = map3D[index];
	          parent.fill(rgbImg.pixels[index]);
	          if(!started){
	            parent.beginShape(TRIANGLE_STRIP);
	            parent.vertex(p.x,p.y,p.z);
	            prevPoint.set(p);
	            started = true;
	          }
	          else if(prevPoint.dist(p) < maxSep){  
	            parent.vertex(p.x,p.y,p.z);         
	            prevPoint.set(p);
	          }
	          else{
	            parent.endShape();
	            started = false;
	            x--;
	            continue;
	          }           
	          indexl = x + (y+1)*xSize;
	          lp = map3D[indexl];
	          if(consImg[indexl] && (p.dist(lp) < maxSep)){
	            parent.vertex(lp.x,lp.y,lp.z);
	            if(x == xSize-1){
	              parent.endShape();
	              started = false;
	            }
	          } 
	          else{
	            parent.vertex(p.x,p.y,p.z);
	            parent.endShape();
	            started = false;
	          }
	        }
	        else if(started){
	          parent.endShape();
	          started = false;
	        }
	      }
	    }
	  }
	  
	  void disolve(float yMin){
	    PVector g = new PVector(0,-15,0);

	    for(int i = 0; i < nPixels; i++){
	      if(consImg[i] && ((map3D[i].y + vel[i].y) > yMin)){
	        map3D[i].add(vel[i]);
	        vel[i].add(g);
	        //map3D[i].add(vel[i]);
	        //vel[i].set(new PVector(0,map(p.y,yMin,900,-40,-4),0));
	      }
	    }
	  }
	  
	  void explode(){
	    PVector explodeAcc = new PVector(0,0,-10);

	    for(int i = 0; i < nPixels; i++){
	      if(consImg[i]){
	        map3D[i].add(vel[i]);
	        vel[i].add(explodeAcc);
	      }
	    }
	  }
}
