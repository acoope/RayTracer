
package utils;

import java.awt.Color;

public class Material {
    double hardness = 70d; 
    RTColor color = new RTColor(Color.WHITE);
    
    
    public Material(RTColor c){
         this.color = c;
    }
    
    public RTColor getColor(){
        return this.color;
    }
}
