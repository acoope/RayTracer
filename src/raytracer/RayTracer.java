package raytracer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import utils.*;

public class RayTracer extends JComponent {

    public Point3D eye;
    public ArrayList<Light> lights;
    public ArrayList<Object3D> objects;
    public int[][] pixelRGB = null;
    public int[][] grassRGB = null;
    public BufferedImage img;
    
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
        
        loadImage("/Users/alissa/NetBeansProjects/RayTracer/src/raytracer/resources/images.jpg");
        
        lights = new ArrayList<Light>();
        objects = new ArrayList<Object3D>();

        //Create Canvas
        Canvas canvas = new Canvas();

        //Set where eye is
        eye = new Point3D(0d, 0d, 0d);

        //Add light(s)
        //lights.add(new Light(new Point3D(0, 20, 20), new RTColor(100,100,100)));
        //lights.add(new Light(new Point3D(-20, 20, 0), new RTColor(0,0,255)));
        lights.add(new Light(new Point3D(20, 50, 70), new RTColor(0,255,0)));
        lights.add(new Light(new Point3D(-25, 20, 70), new RTColor(255,0,0)));
        //lights.add(new Light(new Point3D(-40, 30, 2), new RTColor(0,255,0)));
        
        //Add object(s)
        //objects.add(new Sphere3D(new Point3D(10, 5, 100), 30));
        //objects.add(new Sphere3D(new Point3D(-20, 10, 100), 20));
        objects.add(new Sphere3D(new Point3D(-5, 16, 70), 7, new Material(new RTColor(0,255,255)),0.5));
        objects.add(new Plane3D(new Point3D(0,-10,0), new Point3D(0,1,0),new Material(new RTColor(0,255,255)),0));
        
        //Create rays going from eye(camera) through screen(depth)
        double screen = 5;
        int screenWidth = this.getWidth();
        int screenHeight = this.getHeight();
        
        //create a ray from eye to every point going through screen
        for (int i = 0; i < screenHeight; i++) {
            for (int j = 0; j < screenWidth; j++) {
              
                double x = (i - screenWidth / 2) * screen / screenWidth;
                double y = ((screenHeight / 2) - j) * screen / screenHeight;

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
            //System.out.println("i=" + i + " ,currentRayHit= " + currentRayHit.distance);
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
            
            RTColor pointColor = new RTColor(Color.BLACK);
            
            int totalRed = 0;
            int totalGreen = 0;
            int totalBlue = 0;

            //Find light and eye vector for each light
            for (int k = 0; k < lights.size(); k++) {
                Light light = lights.get(k);

                Point3D lightVector = light.position.subtract(p);
                Point3D eyeVector = eye.subtract(p);

                //Figure out if there is a shadow
                Ray3D LR = new Ray3D(p, lightVector);
                 
                RayHit lightHit = closestRayHit(objects, LR);
                 
                if (lightHit.distance < lightVector.length() - Object3D.epsilon) { 
                   continue; 
                }
                

                //Draw object color based on where ray hits it
                double diffuseIntensity = light.diffuse(lightVector, hit.normal);
                double specularIntensity = light.specular(lightVector, eyeVector, hit.normal, 100);
                
                double intensity = diffuseIntensity + specularIntensity;
               
                totalRed += hit.obj.mat.getColor().getRed() * light.color.getRed() * intensity;
                totalGreen += hit.obj.mat.getColor().getGreen() * light.color.getGreen() * intensity;
                totalBlue += hit.obj.mat.getColor().getBlue() * light.color.getBlue() * intensity;

                if(totalRed > 255) totalRed = 255;
                if(totalGreen > 255) totalGreen = 255;
                if(totalBlue > 255) totalBlue = 255;
                       
                pointColor = new RTColor(totalRed, totalGreen, totalBlue);
            }
            
            //Calculate reflection ray
            Ray3D reflectionRay = reflection(ray,hit.normal);
            
            RTColor reflectColor = getPointColor(reflectionRay, depth-1);
            //System.out.println("obj color= " + pointColor.getRed() + " , reflect color= " + reflectColor.getRed());
            
            pointColor = (pointColor.scaleColor(1-hit.obj.reflectiveCoeff)).addColor(reflectColor.scaleColor(hit.obj.reflectiveCoeff));
            
            if(hit.obj.getClass().equals(Plane3D.class)){
                pointColor = textureMapping(p,.1);
            }
            
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
    
    public void loadImage(String fileName) {
        try {
            File file = new File(fileName);
            img = ImageIO.read(file);  
            pixelRGB = new int[img.getWidth()][img.getHeight()];  
                    
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    pixelRGB[i][j] = img.getRGB(i, j);
                }
            }
        } catch (IOException ex) {
            System.out.println("Couldn't read image into RGB at each pixel");
        }

    }
    
    public RTColor textureMapping(Point3D point, double pixelSize){
        
        //calculate i
        int tempi = new Double(point.x / pixelSize).intValue();
        int width = img.getWidth();
        
        while (tempi < 0){
            tempi = tempi + width;
        }
        
        int i = tempi % width;
        
                
        //calculate j
        int tempj = new Double(point.z / pixelSize).intValue();
        int height = img.getHeight();
        while (tempj < 0){
            tempj = tempj + height;
        }
        
        int j = tempj % height;
        
        //look up RGB value in array
        int rgb = pixelRGB[i][j];
        
        //find each of red, green, and blue values
        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = (rgb) & 0xff;
                
        //return pixel color
        return new RTColor(red,green,blue);
        
    }
}
