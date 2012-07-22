package userTracking;

/**
 * ControlP5 ControlWindow
 * by andreas schlegel, 2012
 */

import processing.core.PApplet;
import controlP5.*;

public class controlWindow extends PApplet
{
ControlP5 cp5;
PApplet parent;
int myColorBackground = color(0, 0, 0);
int knobValue = 100;
ControlWindow myKnobA,controlWindow;
ControlWindow myKnobB;

public int sliderValue = 40;

public controlWindow(PApplet parentCasteado) {
	parent=parentCasteado;
}

public controlWindow() { //Si El metodo main, la llama a sí misma, then parent=this
	parent=this;
	parent.setup();
}


public void setup() {
  size(700, 400);
  smooth();
  noStroke();
  cp5 = new ControlP5(this);
  

  controlWindow = cp5.addControlWindow("controlP5window", 100, 100, 400, 200)
                     .hideCoordinates()
                     .setBackground(color(40));

  cp5.addSlider("sliderValue")
     .setRange(0, 255)
     .setPosition(40, 40)
     .setSize(200, 29)
     .setWindow(controlWindow);
  
 

  myKnobA = cp5.addKnob("knob")
               .setRange(0,255)
               .setValue(50)
               .setPosition(30,100)
               .setRadius(50)
               .setViewStyle(Knob.ARC)
               .setDragDirection(Knob.VERTICAL)
               .setWindow(controlWindow);
                
  myKnobB = cp5.addKnob("knobValue")
               .setRange(0,255)
               .setValue(220)
               .setPosition(180,100)
               .setRadius(50)
               .setNumberOfTickMarks(10)
               .setTickMarkLength(4)
               .snapToTickMarks(true)
               .setColorForeground(color(255))
               .setColorBackground(color(0, 160, 100))
               .setColorActive(color(255,255,0))
               .setDragDirection(Knob.HORIZONTAL)
               .setWindow(controlWindow);

}


public void draw() {
//  background(sliderValue);
  parent.background(myColorBackground);
  parent.fill(knobValue);
  parent.rect(0,height/2,width,height/2);
  parent.fill(0,100);
  parent.rect(80,40,140,320);

}

public void myTextfield(String theValue) {
  println(theValue);
}

public void myWindowTextfield(String theValue) {
  println("from controlWindow: "+theValue);
}


void knob(int theValue) {
  myColorBackground = color(theValue);
  println("a knob event. setting background to "+theValue);
}


public void keyPressed() {
  switch(key) {
//    case('1'):myKnobA.setValue(180);break;
//    case('2'):myKnobB.setConstrained(false).hideTickMarks().snapToTickMarks(false);break;
//    case('3'):myKnobA.shuffle();myKnobB.shuffle();break;
    case(','):controlWindow.hide();
    case('.'):controlWindow.show();
    case('d'):if (controlWindow.isUndecorated()) { controlWindow.setUndecorated(false);
               } else                            { controlWindow.setUndecorated(true);}
    case('t'):controlWindow.toggleUndecorated();
  }
      
}

static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "userTracking.controlWindow" });
}
}