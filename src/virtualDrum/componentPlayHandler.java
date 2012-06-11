package virtualDrum;
import processing.core.PApplet;
import processing.core.PVector;
import ddf.minim.AudioSnippet;
import ddf.minim.Minim;


public class componentPlayHandler extends PApplet {
	// Percussion Instrument class
 
	    AudioSnippet audio;
	    hotPoint trigger;

	    componentPlayHandler(Minim minim, String afile, PVector pos, int size, PApplet parent)
	    {
	        audio = minim.loadSnippet( afile );
	        trigger = new hotPoint(pos.x, pos.y, pos.z, size, parent);
	    }

	    public void checkHit ()
	    {
	        if( trigger.isHit() )
	        {
	            if( audio.isPlaying() )
	                audio.rewind();

	            audio.play();
	        }
	    }

	    public void checkPlaying ()
	    {
	        if( !audio.isPlaying() )
	        {
	            audio.rewind();
	        }
	    }
	}

