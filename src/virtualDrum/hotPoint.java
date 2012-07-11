package virtualDrum;
import processing.core.*;

public class hotPoint extends PApplet {
    PApplet parent;
	PVector center;
    //Paja de hacer el objeto color
	int fillColor;
	//
	int size;
    int pointsIncluded;
    int maxPoints;
    boolean wasJustHit;
    int threshold;
    

    public hotPoint(float centerX, float centerY, float centerZ, int boxSize, PApplet parentSketch) {
        center = new PVector(centerX, centerY, centerZ);
        size = boxSize;
        pointsIncluded = 0;
        maxPoints = 1000;
        threshold = 0;
        parent = parentSketch;
        setColor(parent.random(255), parent.random(255), parent.random(255));
    }

    void setThreshold( int newThreshold ){
        threshold = newThreshold;
    }

    void setMaxPoints(int newMaxPoints) {
        maxPoints = newMaxPoints;
    }

    void setColor(float r, float g, float b){
        fillColor=parent.color(r,g,b);
    }

    boolean check(PVector point) {
        boolean result = false;
        if (point.x > center.x - size/2 && point.x < center.x + size/2)
        {
            if (point.y > center.y - size/2 && point.y < center.y + size/2)
            {
                if (point.z > center.z - size/2 && point.z < center.z + size/2)
                {
                    result = true;
                    pointsIncluded++;
                }
            }
        }
        return result;
    }

    public void draw(VirtualDrum parent)
    { 
        parent.pushMatrix();//isolate coordinate system manipulation
        parent.translate(center.x, center.y, center.z);
        parent.fill(parent.red(fillColor),parent.green(fillColor),parent.blue(fillColor), 255 * percentIncluded());
        parent.stroke(parent.red(fillColor),parent.green(fillColor),parent.blue(fillColor), 255);
        parent.box(size);
        parent.popMatrix();
    }

    float percentIncluded() {
        return map(pointsIncluded, 0, maxPoints, 0, 1);
    }

    boolean currentlyHit() {
        return (pointsIncluded > threshold);
    }

    boolean isHit() {
        return currentlyHit() && !wasJustHit;
    }

    void clear() {
        wasJustHit = currentlyHit();
        pointsIncluded = 0;
    }
}
