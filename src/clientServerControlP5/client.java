package clientServerControlP5;
import processing.net.*;
import processing.core.*;

public class client extends PApplet {
	Client c;
	String input;
	int data[];

	public void setup() 
	{
	  size(450, 255);
	  background(204);
	  stroke(0);
	  frameRate(5); // Slow it down a little
	  // Connect to the server's IP address and port
	  c = new Client(this, "127.0.0.1", 12345); // Replace with your server's IP and port
	}

	public void draw() 
	{
	  if (mousePressed == true) {
	    // Draw our line
	    stroke(255);
	    line(pmouseX, pmouseY, mouseX, mouseY);
	    // Send mouse coords to other person
	    c.write(pmouseX + " " + pmouseY + " " + mouseX + " " + mouseY + "\n");
	  }
	  // Receive data from server
	  if (c.available() > 0) {
	    input = c.readString();
	    input = input.substring(0, input.indexOf("\n")); // Only up to the newline
	    //split del string obtenido.
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
