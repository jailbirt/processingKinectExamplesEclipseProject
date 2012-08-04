package userTracking;
import processing.core.*;
import saito.objloader.BoundingBox;
import saito.objloader.OBJModel;


//Clase que carga los objetos 3d.

public class objectFromObj extends PApplet {
    public OBJModel model;
    PApplet PAparent;
    
	public objectFromObj(PApplet parent, String objectName) {
	    PAparent = parent;
		model = new OBJModel(parent, objectName , "relative", TRIANGLES);
		model.translateToCenter();
        model.enableMaterial();
	    model.enableTexture();
	    // translate the model so its origin
	    // is at its left side
	    BoundingBox box = new BoundingBox(this, model); // 2 El rectangulo màs chico que encuadra al objeto.
	    model.translate(box.getMin()); //3 Pongo el origen de coordenadas del modelo al edge màs a la izq.
	
	}
	
	public void draw () {
		model.draw();
	}
}
