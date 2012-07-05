package utils;

public class Light {

    public Point3D position;
    public RTColor color;

    public Light(Point3D p, RTColor c) {
        this.position = p;
        this.color = c;
    }

    public double diffuse(Point3D lightVector, Point3D normal) {
  
        double diffuseIntensity = normal.normalize().dot(lightVector.normalize());       
        if (diffuseIntensity < 0) return 0; else if(diffuseIntensity > 1) return 1; else return (diffuseIntensity);
    }

    public double specular(Point3D lightVector, Point3D eyeVector, Point3D normal, int hardness) {

        Point3D u = lightVector.normalize();
        Point3D e = eyeVector.normalize(); 
        Point3D w = u.add(e).normalize(); 
        
        double specularIntensity = Math.pow(w.dot(normal), hardness);
        
        if (u.dot(normal) < 0) return 0; else return specularIntensity;
    }
}
