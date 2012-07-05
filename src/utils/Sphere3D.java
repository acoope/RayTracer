
package utils;

public class Sphere3D extends Object3D {
    
    private Point3D center;
    private double radius;
    
    public Sphere3D(Point3D c, double r){
        super();
        this.center = c;
        this.radius = r;
    }

    @Override
    public RayHit rayIntersect(Ray3D ray) {
        Point3D point = ray.p;
        //Point3D dist = ray.d; 
        
        double t=-1;
        
        double A = ray.d.dot(ray.d);
        double B= 2*ray.d.dot(point.subtract(center));
        double C= point.subtract(center).dot(point.subtract(center)) - radius*radius; 
        double D = (B*B-4*A*C);
        
        boolean has_solution = (D>=0); 
        
        if (!(has_solution)) return RayHit.NO_HIT;
        
        double Dsqrt = Math.sqrt(D);
        double t1 = (-B-Dsqrt)/(2 *A);
        double t2=(-B+Dsqrt)/(2*A); // note t1<=t2 always 
        
        if (t1>= epsilon) t=t1; // ray hits the outside of the sphere 
        else if (t2 >= epsilon) t=t2; // ray starts inside the sphere 
        else return RayHit.NO_HIT; // sphere is behind the ray!
        
        Point3D p = ray.atTime(t); 
        Point3D n = p.subtract(center).normalize(); 
        return new RayHit(t,n,this);
      
    }
    
    
}
