package userTracking;

import processing.core.PApplet;
import controlP5.ControlP5;
import controlP5.ControlWindow;
import controlP5.Knob;

public class controlPanel extends PApplet {
  
 // public PApplet parent;
  public handWithObjectTracking parent;
  int myColorBackground = color(0,0,0);
  int knobValue = 100;
  int deltaX = 20;
  int deltaY = 50;
  int stepX = 93;
  int stepY = 30;
  
  ControlP5 controlP5;
  ControlWindow cw;
  
  Knob myKnobA;
  Knob myKnobB;

  public controlPanel(handWithObjectTracking PAparent) {
	    parent=PAparent;
	    
	    controlP5 = new ControlP5(PAparent);
	    cw = controlP5.addControlWindow("Kinect control",0,0,700,400);  
	
	    //Lo siguiente, lo encontrarias en setup
		 
	    myKnobA = controlP5.addKnob("knob")
	                 .setRange(0,255)
	                 .setValue(50)
	                 .setPosition(100,70)
	                 .setRadius(50)
	                 .setViewStyle(Knob.ARC)
	                 .setDragDirection(Knob.VERTICAL)
	                 ;
	                       
	    myKnobB = controlP5.addKnob("knobValue")
	                 .setRange(0,255)
	                 .setValue(220)
	                 .setPosition(100,210)
	                 .setRadius(50)
	                 .setNumberOfTickMarks(10)
	                 .setTickMarkLength(4)
	                 .snapToTickMarks(true)
	                 .setColorForeground(color(255))
	                 .setColorBackground(color(0, 160, 100))
	                 .setColorActive(color(255,255,0))
	                 .setDragDirection(Knob.HORIZONTAL)
	                 ;

  }

  public void draw() {
    parent.background(myColorBackground);
    parent.fill(knobValue);
    parent.rect(0,height/2,width,height/2);
    parent.fill(0,100);
    parent.rect(80,40,140,320);
  }


  public void knob(int theValue) {
    myColorBackground = color(theValue);
    println("a knob event. setting background to "+theValue);
  }


  public void keyPressed() {
    switch(key) {
      case('1'):myKnobA.setValue(180);break;
      case('2'):myKnobB.setConstrained(false).hideTickMarks().snapToTickMarks(false);break;
      case('3'):myKnobA.shuffle();myKnobB.shuffle();break;
    }
    
  }

}
