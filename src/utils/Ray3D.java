
package utils;

/**	A Ray3D consists of a point and a (normalized) direction. **/ 

public class Ray3D{ 
    public Point3D p,d;
/**
    This represents a 3D ray with a specified origin point p and direction d.
    The direction of a ray is a normalized vector. 
**/
    
    public Ray3D(Point3D p, Point3D d) { 
        this.p = p;
        this.d = d.normalize(); 
        this.d.w=0; 
    }
    
/**	This returns the point along the ray t units from its origin p	**/ 
    public Point3D atTime(double t){
        return new Point3D((p.x+t*d.x), (p.y + t*d.y), (p.z+t*d.z));	
    }
}
