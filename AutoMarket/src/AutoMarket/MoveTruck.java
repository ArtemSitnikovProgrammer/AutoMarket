package AutoMarket;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PipedReader;

import static java.awt.event.KeyEvent.VK_ENTER;

public class MoveTruck extends BaseAI {

    boolean goingTruck = true;
    boolean flag = false;
    String name;
    private static PipedReader pr;

    MoveTruck(String n){
        name = n;
        flag = false;
        MyFrame.MyConsolDialog.consolTextArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent k)
            {
                if (k.getKeyCode() == VK_ENTER) {
                    try {
                        int a;
                        a=pr.read();
                        System.out.println("Reading________: " + a);
                        if (a == 1) {
                            myresume();
                            System.out.println("Данные получены: ПРОДОЖАЕМ ДВИЖЕНИЕ");
                        }//продолжить
                        if (a == 0) {
                            System.out.println("Данные получены: ОСТАНАВЛИВАЕМ ДВИЖЕНИЕ");
                            mysuspend();
                        }//остановить
                        if (a == 2) {
                            System.out.println("Данные получены: Ошибка!!!");
                        }//остановить
                    } catch (IOException e) {
                        System.out.println("The job's finished.");
                        System.exit(0);
                    }
                }
            }
        });
    }

    PipedReader getStream(){ return pr; }

    public static void pipedConnection(){

        try   {  pr = new PipedReader(MyFrame.MyConsolDialog.getStreamTruck()); }
        catch(IOException e)        {
            System.err.println("From MoveCar(): " + e);
        }
    }

    void mysuspend()   {    flag = true; }

    synchronized  void myresume() {
        flag = false;
        notify();
    }

    @Override
    public void run() {
        while (goingTruck)
        {
            //System.out.println("Поток ");
            int x,x2,y,y2;
            float V,T, dx, dy,S;
            try
            {
                Thread.sleep(50);
                synchronized(this)  {
                    while(flag)  { wait();}
                }

            } catch (InterruptedException ex)
            {
            }
            synchronized (Habitat.Arr)
            {
                for (int i = 0; i < Habitat.Arr.size(); i++)
                {
                    //System.out.println("Движение");
                    if(Habitat.Arr.get(i).getClass() == Truck.class) {
                        if (Habitat.Arr.get(i).edy == true || Habitat.Arr.get(i).x >= Habitat.myFrame.panel.getWidth() / 2 -50 || Habitat.Arr.get(i).y >= Habitat.myFrame.panel.getHeight() / 2 - 50) {
                            V = 5;
                            Habitat.Arr.get(i).edy = true;
                            //System.out.println("Движение Truck");
                            x2 = Habitat.Arr.get(i).getX2();
                            y2 = Habitat.Arr.get(i).getY2();
                            x = Habitat.Arr.get(i).x;
                            y = Habitat.Arr.get(i).y;
                            //S = (float) Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
                            //T = S/V;
                            dx = (float)(x2 - x) / (float) Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
                            dy = (float)(y2 - y) / (float) Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
                            Habitat.Arr.get(i).x = x + (int)(dx * V);
                            Habitat.Arr.get(i).y = y + (int)(dy * V);

                            if (Habitat.Arr.get(i).x <= x2 || Habitat.Arr.get(i).y <= y2) {
                                Habitat.Arr.get(i).edy=false;
                            }

                        }
                    }
                }
            }
        }
    }

}
