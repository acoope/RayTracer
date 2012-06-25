
package utils;

/** Record properties of intersection of a ray and an object **/ 

public class RayHit {
    public static RayHit NO_HIT = new RayHit(Double.POSITIVE_INFINITY,null, null);
    public double distance; // distance along ray to the first intersection 
    public Point3D normal; // normal of the object at the intersection point 
    public Object3D obj; // innermost primitive object that this ray hits...
    
    public RayHit(double d,Point3D n, Object3D obj) { 
        this.distance=d;
        this.normal=n;
        this.obj = obj;	
    }
    
}
