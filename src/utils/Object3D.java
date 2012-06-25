
package utils;

public abstract class Object3D {

    public static final double epsilon = 0.00001; 
    public Material insideMat = Material.defaultMat, 
                    outsideMat= Material.defaultMat;
    
    /** rayIntersect(r) returns the intersection of the object with a ray as a RayHit object **/ 
    public abstract RayHit rayIntersect (Ray3D ray);
}