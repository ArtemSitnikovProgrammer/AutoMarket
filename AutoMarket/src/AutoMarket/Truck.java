package AutoMarket;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Truck extends Auto {
    transient static public Image Im;
    public int x2,y2;
    public String nameAuto;

    Truck(int width, int height, int ObjectID, long life_Time) {
        super(width, height,ObjectID,life_Time);

        x2 =(int) (Math.random() * (width/2-100));
        y2 = (int) (Math.random() * (height/2-100));


        try {
            Im = ImageIO.read(new File("image/Truck3.png"));
            nameAuto="Truck";
        } catch (IOException e) {
            System.out.println("Error!!");
        }
    }
    public String getNameAuto() {
        return nameAuto;
    }
    public int getX2() { return x2;}
    public int getY2() { return y2;}
    public Image getIm() { return Im;}

}