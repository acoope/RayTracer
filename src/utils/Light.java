package utils;

public class Light {

    public Point3D position;

    public Light(Point3D p) {
        this.position = p;
    }

    public double diffuse(Point3D lightVector, Point3D normal) {
        double diffuseIntensity = normal.dot(lightVector);
        if (diffuseIntensity < 0 || diffuseIntensity > 255) return 0.0; else return (diffuseIntensity);
    }

    public double specular(Point3D lightVector, Point3D eyeVector, Point3D normal, int hardness) {

        Point3D u = lightVector.normalize();
        Point3D e = eyeVector.normalize(); 
        Point3D w = u.add(e).normalize(); 
        
        if (u.dot(normal) > 0 || u.dot(normal) > 255) return Math.pow(w.dot(normal), hardness); else return 0.0;
        
    }
}
