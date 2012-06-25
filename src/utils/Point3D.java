
package utils;

/**	A Point3D is a triple of doubles that can represent a point or a vector. **/ 

public class Point3D {	
    public double x=0.0, y=0.0, z=0.0, w=1.0;

    public Point3D(double x, double y, double z) { 
        this.x = x;
        this.y = y; 
        this.z = z;
    }
    
    public Point3D(Point3D m){ 
        this(m.x,m.y,m.z);
    } 
    
    public Point3D subtract(Point3D q) { 
        return new Point3D(x-q.x, y-q.y,z-q.z);
    }
    
    public Point3D add(Point3D q) {
        return new Point3D(x+q.x, y+q.y,z+q.z);	
    } 
    
    public Point3D scale(double a) {
        return new Point3D(x*a, y*a, z*a);
    }
    
    public Point3D translate(double x_dist, double y_dist, double z_dist) {
        return new Point3D (x + x_dist, y + y_dist, z + z_dist); 
    } 
    
    public double norm(Point3D q) { 
        return q.dot(q);
    }
    
    public double length(Point3D q) { 
        return Math.sqrt(q.dot(q));
    }
    
    public double dot(Point3D q){ 
        return x*q.x + y*q.y + z*q.z;
    }
    
    public Point3D cross(Point3D q){
        return new Point3D(y*q.z - q.y*z, q.x*z - x*q.z, x*q.y - q.x*y); 
    }
    
    public Point3D normalize() { 
        return this.scale(1/this.length());
    }
    
    public double length() { 
        return Math.sqrt((x*x) + (y*y) + (z*z));
    }
}