package AutoMarket;

import java.awt.*;

public interface IBehaviour {

    void move();

    void setx(int x);

    void sety(int y);

    int getx();

    int gety();

    Image getIm();
    int getX2();
    int getY2();
    String getNameAuto();
}
