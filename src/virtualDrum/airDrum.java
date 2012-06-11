package virtualDrum;
 import processing.opengl.*;
import processing.core.*;
import SimpleOpenNI.*;
import ddf.minim.*;


public class airDrum extends PApplet{
        // minim objects
        Minim minim;
        AudioSnippet player;
        SimpleOpenNI kinect;
        float rotation = 0;
        // used for edge detection
        boolean wasJustInBox = false;// 1 Edge detection
        int boxSize = 150;
        PVector boxCenter = new PVector(0, 0, 600);
        double s = 1;
        public void setup() {
                size(1024, 768, OPENGL);
                kinect = new SimpleOpenNI(this);
                kinect.enableDepth();
                // initialize Minim
                // and AudioPlayer
                minim = new Minim(this);
                player = minim.loadSnippet("sounds/LEX_HEAVY_Kick_LV2.wav");
        }
        public void draw() {
                background(0);
                kinect.update();
                translate(width/2, height/2, -1000);
                rotateX(radians(180));
                translate(0, 0, 1400);
                rotateY(radians(map(mouseX, 0, width, -180, 180)));
                translate(0, 0, (float)s*-1000);
                scale((float)s);
                stroke(255);
                PVector[] depthPoints = kinect.depthMapRealWorld();
                int depthPointsInBox = 0;
                for (int i = 0; i < depthPoints.length; i+=10) {
                        PVector currentPoint = depthPoints[i];
                        // The nested if statements inside of our loop 2
                        // These ones Verify that your points are
                        // inside the box.
                        if (currentPoint.x > boxCenter.x - boxSize/2
                                        && currentPoint.x < boxCenter.x + boxSize/2)
                        {
                                if (currentPoint.y > boxCenter.y - boxSize/2
                                                && currentPoint.y < boxCenter.y + boxSize/2)
                                {
                                        if (currentPoint.z > boxCenter.z - boxSize/2
                                                        && currentPoint.z < boxCenter.z + boxSize/2)
                                        {
                                                depthPointsInBox++; // 3 Increments transparency color.
                                        }
                                }
                        }
                         
                        point(currentPoint.x, currentPoint.y, currentPoint.z);
                }
                //3
                // set the box color's transparency
                // 0 is transparent, 1000 points is fully opaque red
                float boxAlpha = map(depthPointsInBox, 0, 1000, 0, 255); 
                // edge detection
                // are we in the box this time
                boolean isInBox = (depthPointsInBox > 0); //2
                        // if we just moved in from outside
                        // start it playing
                        if (isInBox && !wasJustInBox) { //3
                                player.play();
                        }
                // if it's played all the way through
                // pause and rewind
                if (!player.isPlaying()) {// 4
                        player.rewind();
                        player.pause();
                }
                // save current status
                // for next time
                wasJustInBox = isInBox;

                translate(boxCenter.x, boxCenter.y, boxCenter.z);
                // the fourth argument to fill() is "alpha"
                // it determines the color's opacity
                // we set it based on the number of points
                fill(255, 0, 0, boxAlpha);
                stroke(255, 0, 0);
                box(boxSize);

        }
        public void stop()
        {
                player.close();
                minim.stop();
                super.stop();
        }
        // use keys to control zoom
        // up-arrow zooms in
        // down arrow zooms out
        // s gets passed to scale() in draw()
        public void keyPressed() {
                if (keyCode == 38) {
                        s = s + 0.01;
                }
                if (keyCode == 40) {
                        s = s - 0.01;
                }
        }
}



