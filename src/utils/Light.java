package utils;

public class Light {

    public Point3D position;

    public Light(Point3D p) {
        this.position = p;
    }

    public double diffuse(Point3D lightVector, Point3D normal) {
  
        double diffuseIntensity = 100*normal.normalize().dot(lightVector.normalize());
        if (diffuseIntensity < 0) return 0; else if(diffuseIntensity > 255) return 255; else return (diffuseIntensity);
    }

    public double specular(Point3D lightVector, Point3D eyeVector, Point3D normal, int hardness) {

        Point3D u = lightVector.normalize();
        Point3D e = eyeVector.normalize(); 
        double w = u.dot(e); 
        
        if (u.dot(normal) > 0) return 255 * Math.pow(w, hardness); else return 0;
        
    }
}
