package clientServerControlP5;
import processing.net.*;
import processing.core.*;

public class server extends PApplet {
	Server s;
	Client c;
	String input;
	int data[];

	public void setup() 
	{
	  size(450, 255);
	  background(204);
	  stroke(0);
	  frameRate(5); // Slow it down a little
	  s = new Server(this, 12345); // Start a simple server on a port
	}

	public void draw() 
	{
	  if (mousePressed == true) {
	    // Draw our line
	    stroke(255);
	    line(pmouseX, pmouseY, mouseX, mouseY);
	    // Send mouse coords to other person
	    s.write(pmouseX + " " + pmouseY + " " + mouseX + " " + mouseY + "\n");
	  }
	  // Receive data from client
	  c = s.available();
	  if (c != null) {
	    input = c.readString();
	    input = input.substring(0, input.indexOf("\n")); // Only up to the newline
	    
	    //split del string obtenido. java hace algo raro...
	    //     data = int(split(input, ' ')); // Split values into an array

	    String[] tempStr = split(input, ' ');
	    for (int i=0;i< tempStr.length;i++) {	
	        data[i] = Integer.parseInt(tempStr[i]); // Split values into an array
	    }
	    //
	    
	        // Draw line using received coords
	    stroke(0);
	    line(data[0], data[1], data[2], data[3]);
	  }
	}
}
