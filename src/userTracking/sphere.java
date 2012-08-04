package userTracking;
import SimpleOpenNI.*;
import processing.core.*;


/**
 * @author jailbirt
 *La responsabilidad de esta clase es dibujar una esfera en la mano izquierda.
 */
public class sphere extends PApplet {
	private PApplet parent;
	private SimpleOpenNI kinect;
	
	public sphere (PApplet parentO, SimpleOpenNI parentKinectO) {
		parent=parentO;
		kinect=parentKinectO;
	}
    public void drawRightHandSphere(int userId) {
    	// make a vector to store the left hand, el kinect esta enfrentado.
		PVector rightHand = new PVector();
		// put the position of the left hand into that vector
		// confidence es lo que reporta como retorno del metodo.
		// 1 es hiper confiable, 0 es nada confiable.
		float confidence = kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HAND, rightHand);
		// convert the detected hand position
		// to "projective" coordinates
		// that will match the depth image
		PVector convertedRightHand = new PVector();
		//Proyecto en 2d ( saco Z ) lo que veo en 3d.
		// como si siguiera con el dedo algo que pasa del otro lado del vidrio.
		kinect.convertRealWorldToProjective(rightHand, convertedRightHand);
		// and display it
		parent.fill(255, 0, 0);
		//uso z para 'escalar' el circulo.
		float ellipseSize = map(convertedRightHand.z, 700, 2500, 50,1);
        //700 es el numero màs chico y 2500 el màs grande que puedo ver de z.
		if ( confidence > 0.9f ){ //si es un valor aceptable dibujo, sino no ( para que no dibuje basura).
		 println("La confianza es de "+confidence);
		 parent.ellipse(convertedRightHand.x, convertedRightHand.y,
		 ellipseSize, ellipseSize);
		}		
    }
}
