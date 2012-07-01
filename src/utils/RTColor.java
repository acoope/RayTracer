
package utils;

import java.awt.Color;

public class RTColor {
    public Color color;
    public boolean draw = true;
    
    public RTColor(int r, int g, int b) { 
        this.color = new Color(r,g,b);
        this.draw = true;
    }
    
    public RTColor(Color c){
        this.color = c;
        this.draw = true;
    }
    
    public RTColor(Color c, boolean draw){
        this.color = c;
        this.draw = false;
    }
    
    
    
    public Color getColor(){
        return this.color;
    }
    
    public Color addColor(Color c){
        int red = color.getRed() + c.getRed();
        int green = color.getGreen() + c.getGreen();
        int blue = color.getBlue() + c.getBlue();
        
        if(red > 255) red = 255;
        if(green > 255) green = 255;
        if(blue > 255) blue = 255;
        
        return new Color(red,green,blue);
    }
    
    public Color multiplyColors(Color c){
        int red = color.getRed() * c.getRed();
        int green = color.getGreen() * c.getGreen();
        int blue = color.getBlue() * c.getBlue();
        
        if(red > 255) red = 255;
        if(green > 255) green = 255;
        if(blue > 255) blue = 255;
        
        return new Color(red,green,blue);
    }
    
    public Color scaleColor(double intensity){
        int red = new Double(color.getRed() * intensity).intValue();
        int green = new Double(color.getGreen() * intensity).intValue();
        int blue = new Double(color.getBlue() * intensity).intValue();
        
        if(red > 255) red = 255;
        if(green > 255) green = 255;
        if(blue > 255) blue = 255;
        
        return new Color(red,green,blue);
        
    }
    
    public int getRed(){
        return this.color.getRed();
    }
    
    public int getGreen(){
        return this.color.getGreen();
    }
    
    public int getBlue(){
        return this.color.getBlue();
    }
}
