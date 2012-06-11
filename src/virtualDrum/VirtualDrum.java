package virtualDrum;
 import processing.opengl.*;
import processing.core.*;
import SimpleOpenNI.*;
import ddf.minim.*;

public class VirtualDrum extends PApplet {
SimpleOpenNI kinect;
float rotation = 0;

float s = 1;
DrumKit drum ;

public void setup()
{
    size(1024, 768, OPENGL);
    kinect = new SimpleOpenNI(this);
    kinect.enableDepth();

    drum = new DrumKit( new Minim(this),this ) ; //le paso VirtualDrum al draw de hotpoint, necesita el parent
}

public void draw()
{
    background(0);
    kinect.update();
    translate(width/2, height/2, 0);
    rotateX(radians(180));
    rotateY(radians(180));
    translate(0, 0, -500);

    translate(0, 0, s*-1000);
    scale(s);
    stroke(255);

    PVector[] depthPoints = kinect.depthMapRealWorld();

    for (int i = 0; i < depthPoints.length; i+=10) {
        PVector currentPoint = depthPoints[i];
        // each hotpoint should verify if it includes the currentPoint
        drum.check(currentPoint);
        point(currentPoint.x, currentPoint.y, currentPoint.z);
    }

    drum.play();
}

public void stop() {
    // make sure to close
    // both AudioPlayer objects
    drum.stop();
    super.stop();
}

public void keyPressed() {
    if (keyCode == 38) {
        s = s + 0.01f;
    }
    if (keyCode == 40) {
        s = s - 0.01f;
    }
}

}
