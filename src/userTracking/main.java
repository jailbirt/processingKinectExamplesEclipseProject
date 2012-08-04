package userTracking;
import processing.core.PApplet;

public class main {
 
    static public void main(final String args[]) {
    	// should have pakage.obj
    	int display=1;
    	//String mainSketch="DistinguishUserFromBackground";
      	//String mainSketch="tresDTracking";
    	String mainSketch="multicam";
    	System.out.print("Initializing on display..."+display);
    	PApplet.main(new String[] {"--bgcolor=#ECE9D8","--present","--present-stop-color=#000000",
    		        	"--display="+display,"userTracking."+mainSketch });
    }
}