package raytracer;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import utils.*;

public class RayTracer extends JComponent {

    public Point3D eye;
    public ArrayList<Light> lights;
    public ArrayList<Object3D> objects;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        JFrame mainFrame = new JFrame("Ray Tracer");
        mainFrame.getContentPane().add(new RayTracer());
        mainFrame.setSize(399, 399);
        mainFrame.setVisible(true);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        lights = new ArrayList<Light>();
        objects = new ArrayList<Object3D>();

        //Create Canvas
        Canvas canvas = new Canvas();

        //Set where eye is
        eye = new Point3D(0d, 0d, 0d);

        //Add light(s)
        //lights.add(new Light(new Point3D(0, 20, 20), new RTColor(100,100,100)));
        lights.add(new Light(new Point3D(0, 20, 20), new RTColor(0,0,255)));
        lights.add(new Light(new Point3D(-50, -50, 50), new RTColor(255,0,0)));
        lights.add(new Light(new Point3D(-40, 30, 2), new RTColor(0,255,0)));
        
        //Add object(s)
        objects.add(new Sphere3D(new Point3D(10, 5, 100), 30));
        objects.add(new Sphere3D(new Point3D(-20, -10, 100), 20));
        objects.add(new Sphere3D(new Point3D(-30, 16, 70), 7));
        
        //Create rays going from eye(camera) through screen(depth)
        double screen = 5;
        int screenWidth = this.getWidth();
        int screenHeight = this.getHeight();
        
        //create a ray from eye to every point going through screen
        for (int i = 0; i < screenHeight; i++) {
            for (int j = 0; j < screenWidth; j++) {
              
                double x = (j - screenWidth / 2) * screen / screenWidth;
                double y = (i - screenHeight / 2) * screen / screenHeight;

                Ray3D ray = new Ray3D(eye, new Point3D(x, y, screen));
                
                RTColor pColor = getPointColor(ray, 3);
                
                if(pColor.draw){

                    g.setColor(pColor.getColor());
                
                    g.drawLine(i, j, i + 1, j + 1);
                }

            }

        }

    }

    public RayHit closestRayHit(ArrayList<Object3D> objects, Ray3D ray) {
        RayHit closestHit = RayHit.NO_HIT;

        for (int i = 0; i < objects.size(); i++) {
            RayHit currentRayHit = objects.get(i).rayIntersect(ray);
            if (currentRayHit.distance < closestHit.distance) {
                closestHit = currentRayHit;
            }
        }

        return closestHit;
    }

    public RTColor getPointColor(Ray3D ray, int depth) {
        if(depth < 0 ){
            return new RTColor(0,0,0);
        }
        
        //Find the closest object the ray intersects with
        RayHit hit = closestRayHit(objects, ray);

        if (hit.distance != Double.POSITIVE_INFINITY) { 
            //find the intersection point
            Point3D p = ray.atTime(hit.distance);
            
            RTColor pointColor = new RTColor(Color.WHITE);
            
            int totalRed = 0;
            int totalGreen = 0;
            int totalBlue = 0;

            //Find light and eye vector for each light
            for (int k = 0; k < lights.size(); k++) {
                Light light = lights.get(k);

                Point3D lightVector = light.position.subtract(p);
                Point3D eyeVector = eye.subtract(p);

                /*
                Ray3D LR = new Ray3D(p, lightVector);
                 
                RayHit lightHit = closestRayHit(objects, LR);
                 
                if (lightHit.distance < lightVector.length() - Object3D.epsilon) { 
                   continue; 
                }
                */

                //Draw object color based on where ray hits it
                double diffuseIntensity = light.diffuse(lightVector, hit.normal);
                double specularIntensity = light.specular(lightVector, eyeVector, hit.normal, 100);
                
                double intensity = diffuseIntensity + specularIntensity;
               
                totalRed += light.color.getRed() * intensity;
                totalGreen += light.color.getGreen() * intensity;
                totalBlue += light.color.getBlue() * intensity;

                if(totalRed > 255) totalRed = 255;
                if(totalGreen > 255) totalGreen = 255;
                if(totalBlue > 255) totalBlue = 255;
                
                pointColor = new RTColor(totalRed, totalGreen, totalBlue);
            }
            
            //Calculate reflection ray
            Ray3D reflectionRay = reflection(ray,hit.normal);
            
            RTColor reflectColor = getPointColor(reflectionRay, depth-1);
            //System.out.println("obj color= " + pointColor.getRed() + " , reflect color= " + reflectColor.getRed());
            
            pointColor = pointColor.addColor(reflectColor);
            
            return pointColor;
        } else {
        
            return new RTColor(new Color(0,0,0),false);
        }

    }
    
    public Ray3D reflection(Ray3D r, Point3D normal ){
        Point3D rayDist = r.d;
        double dn = rayDist.dot(normal);
        Point3D Dprojection = normal.scale(dn).scale(2);
        
        Point3D reflectionDist = rayDist.subtract(Dprojection);
        
        Ray3D reflectRay = new Ray3D(r.p,reflectionDist);
        
        return reflectRay;     
    }
}
