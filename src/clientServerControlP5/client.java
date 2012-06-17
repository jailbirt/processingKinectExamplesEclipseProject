package clientServerControlP5;
import processing.net.*;
import processing.core.*;

public class client extends PApplet {
	Client c;
	String input;
	String[] data;

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
	  //split del string obtenido. java hace algo raro...
        data =  (split(input, ' ')); // Split values into an array

	    //
	    // Draw line using received coords
	    stroke(0);
	    line(parseInt(data[0]), parseInt(data[1]), parseInt(data[2]),parseInt(data[3]));
	  }
	}
}
