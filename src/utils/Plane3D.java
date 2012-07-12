
package utils;

public class Plane3D extends Object3D {
    
    private Point3D center;
    private Point3D normal;
    
    public Plane3D(Point3D c, Point3D n){
        super();
        this.center = c;
        this.normal = n.normalize();
    }
    
     public Plane3D(Point3D c, Point3D n, Material m, double coeff){
        super();
        this.center = c;
        this.normal = n.normalize();
        this.reflectiveCoeff = coeff;
        this.mat = m;
    }
    
    @Override
    public RayHit rayIntersect(Ray3D ray) {
        Point3D point = ray.p;
        
        double PCN = normal.dot(center.subtract(point));
        double DN = normal.dot(ray.d);
        
        if(DN == 0) return RayHit.NO_HIT;
 
        double t = PCN/DN;
        //System.out.println("time =" + t);
        
        if(t > epsilon){
            Point3D p = ray.atTime(t); 
            return new RayHit(point.subtract(p).length(),normal,this);
            
        } else {
            return RayHit.NO_HIT;
        }
        
    }
    
}
