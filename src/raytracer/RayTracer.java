package raytracer;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import utils.*;

public class RayTracer extends JComponent {

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

        ArrayList<Light> lights = new ArrayList<Light>();
        ArrayList<Object3D> objects = new ArrayList<Object3D>();

        //Create Canvas
        Canvas canvas = new Canvas();

        //Set where eye is
        Point3D eye = new Point3D(0d, 0d, 0d);

        //Add light(s)
        lights.add(new Light(new Point3D(0, 20, 80)));
        lights.add(new Light(new Point3D(-50, -50, 50)));
        lights.add(new Light(new Point3D(40, 30, 2)));
   
        //Add object(s)
        objects.add(new Sphere3D(new Point3D(10, 5, 100), 30));
        objects.add(new Sphere3D(new Point3D(-20, -10, 100), 20));

        //Create rays going from eye(camera) through screen(depth)
        double screen = 5;
        int screenWidth = this.getWidth();
        int screenHeight = this.getHeight();
        
        //create a ray from eye to every point going through screen
        for (int i = 0; i < screenHeight; i++) {
            for (int j = 0; j < screenWidth; j++) {
                
                double x = (j-screenWidth/2)*screen/screenWidth;
                double y = (i-screenHeight/2)*screen/screenHeight;
                        
                Ray3D ray = new Ray3D(eye, new Point3D(x, y, screen));

                //Find the closest object the ray intersects with
                RayHit hit = closestRayHit(objects, ray);

                //find the intersection point
                Point3D p = ray.atTime(hit.distance);
                
                Color pointColor = new Color(0,0,0);
                int totalRGB = 0;

                //Find light and eye vector for each light
                for (int k = 0; k < lights.size(); k++) {
                    Light light = lights.get(k);

                    Point3D lightVector = light.position.subtract(p);
                    Point3D eyeVector = eye.subtract(p);

                    //Ray3D LR = new Ray3D(p, lightVector);

                    //RayHit lightHit = closestRayHit(objects, LR);

                    //if (lightHit.distance < lightVector.length() - Object3D.epsilon) {
                        //continue;
                    //}

                    

                    //Draw object color based on where ray hits it
                    if (hit.distance != Double.POSITIVE_INFINITY) {

                        double diffuseIntensity = light.diffuse(lightVector, hit.normal);
                        //double specularIntensity = light.specular(lightVector, eyeVector, hit.normal, 1);

                        int rgb = new Double(diffuseIntensity).intValue(); //+ specularIntensity).intValue();
                        totalRGB += rgb;
                        
                        //System.out.println("totalRGB: " + totalRGB); //+ " , " + specularIntensity + " = " + rgb);
                    }
                    if(totalRGB > 255) totalRGB = 255;
                    pointColor = new Color(totalRGB, totalRGB, totalRGB);
                    g.setColor(pointColor);
                    
                    if (hit.distance != Double.POSITIVE_INFINITY) {
                        //System.out.println("i = " + i + " , j= " + j);
                        g.drawLine(i, j, i+1, j+1);
                    }
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
}
