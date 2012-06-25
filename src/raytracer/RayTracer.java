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
        //lights.add(new Light(new Point3D(10, 20, 1)));
        lights.add(new Light(new Point3D(10, 20, 100)));
        //lights.add(new Light(new Point3D(4, 3, 2)));
   
        //Add object(s)
        objects.add(new Sphere3D(new Point3D(175, 240, 1), 10));
        //objects.add(new Sphere3D(new Point3D(15, 5, 10), 1));

        //Create rays going from eye(camera) through screen(depth)
        double screen = 5;

        //create a ray from eye to every point going through screen
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {

                Ray3D ray = new Ray3D(eye, new Point3D(i, j, screen));

                //Find the closest object the ray intersects with
                RayHit hit = closestRayHit(objects, ray);

                //find the intersection point
                Point3D p = ray.atTime(hit.distance);

                //Find light and eye vector for each light
                for (int k = 0; k < lights.size(); k++) {
                    Light light = lights.get(k);

                    Point3D lightVector = light.position.subtract(p);
                    Point3D eyeVector = eye.subtract(p);

                    Ray3D LR = new Ray3D(p, lightVector);

                    RayHit lightHit = closestRayHit(objects, LR);

                    if (lightHit.distance < lightVector.length() - Object3D.epsilon) {
                        continue;
                    }

                    Color pointColor = Color.WHITE;

                    //Draw object color based on where ray hits it
                    if (hit.distance == Double.POSITIVE_INFINITY) {
                        pointColor = new Color(255, 255, 255); //White

                    } else {
                        //Set ambient light (background color)
                        pointColor = new Color(0, 0, 0); //Black

                        double diffuseIntensity = light.diffuse(lightVector, hit.normal);
                        double specularIntensity = light.specular(lightVector, eyeVector, hit.normal, 1);

                        double rgbDouble = diffuseIntensity + specularIntensity;
                        int rgb = new Double(diffuseIntensity + specularIntensity).intValue();
                        //int rgb = new Double(diffuseIntensity).intValue();
                        
                        System.out.println("intensity: " + diffuseIntensity + " , " + specularIntensity + " = " + rgb);
                        pointColor = new Color(rgb, rgb, rgb);
                    }

                    g.setColor(pointColor);
                    
                    int xValue = new Double(p.x).intValue();
                    int yValue = new Double(p.y).intValue();
                    //g.drawLine(xValue, yValue,xValue, yValue);
                    g.drawLine(i, j, i, j);
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
