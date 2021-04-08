package AutoMarket;

import java.io.Serializable;

public abstract class Auto implements IBehaviour, Serializable {
    int x,y, widthPain, heightPain;
    public boolean edy=false;
    int ObjectID;
    long life_Time;

    Auto(int width, int height, int ObjectID, long life_Time){
        widthPain = width;
        heightPain = height;
        x = (int) (Math.random() * (width-100));
        y = (int) (Math.random() * (height-100));

        this.ObjectID = ObjectID;
        this.life_Time = life_Time;
    }

    public void setx(int x) {
        this.x=x;
    }

    public void sety(int y) {
        this.y=y;
    }

    @Override
    public int getx() {
        return x;
    }

    @Override
    public int gety() {
        return y;
    }


    @Override
    public void move() {

    }

}
