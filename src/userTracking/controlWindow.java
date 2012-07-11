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

int myColorBackground = color(0, 0, 0);

ControlWindow controlWindow;

public int sliderValue = 40;

public void setup() {
  size(700, 400);  

  cp5 = new ControlP5(this);

  controlWindow = cp5.addControlWindow("controlP5window", 100, 100, 400, 200)
    .hideCoordinates()
      .setBackground(color(40))
        ;

  cp5.addSlider("sliderValue")
    .setRange(0, 255)
      .setPosition(40, 40)
        .setSize(200, 29)
          .setWindow(controlWindow)
            ;
}


public void draw() {
  background(sliderValue);
}

public void myTextfield(String theValue) {
  println(theValue);
}

public void myWindowTextfield(String theValue) {
  println("from controlWindow: "+theValue);
}

public void keyPressed() {
  if (key==',') cp5.window("controlP5window").hide();
  if (key=='.') cp5.window("controlP5window").show();
  // controlWindow = controlP5.addControlWindow("controlP5window2",600,100,400,200);
  // controlP5.controller("sliderValue1").moveTo(controlWindow);

  if (key=='d') {
    if (controlWindow.isUndecorated()) {
      controlWindow.setUndecorated(false);
    } 
    else {
      controlWindow.setUndecorated(true);
    }
  }
  if (key=='t') {
    controlWindow.toggleUndecorated();
  }
  
}
static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "userTracking.controlWindow" });
}
}