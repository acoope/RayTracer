
package utils;

import java.awt.Color;

public abstract class Object3D {

    public static final double epsilon = 0.00001; 
    public Material mat = new Material(new RTColor(Color.WHITE));
    public double reflectiveCoeff = 0;
    
    /** rayIntersect(r) returns the intersection of the object with a ray as a RayHit object **/ 
    public abstract RayHit rayIntersect (Ray3D ray);
}