
package utils;

import java.awt.Color;

public class Material {
    public static Material defaultMat = new Material(); 
    double hardness = 70d; 
    Color color = Color.WHITE;
    
    public Color getColor(){
        return this.color;
    }
}
