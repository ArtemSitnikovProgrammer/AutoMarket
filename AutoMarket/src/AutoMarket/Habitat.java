package AutoMarket;



import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.Timer;

public class Habitat {

    static ArrayList<Auto> Arr = new ArrayList();
    static HashMap<Integer,Integer> Time_Life = new HashMap<>();
    static TreeSet<Integer> ID = new TreeSet<>();
    static Iterator it;

    public static boolean On=true, blok=true, time = true, unblokDialog=false;
    public static int Car = 0, Truck = 0, ALL = 0;
    public static long N1 = 4;
    public static long N2 = 2;
    public static float P1 = 1.0f;
    public static float P2 = 1.0f;
    public static int CarPriority = 5;
    public static int TruckPriority = 5;
    public static long stopTime = 0, pause_time=0 ; // системное время конца
    public static long m_lastTime = 0;   // Время окончания симуляции
    public static long m_startTime=0;
    public static int life_Time_Car = 20;
    public static int life_Time_Truck = 20;
    public static int ObjectID;

    public Timer timer1;
    public static MoveTruck moveTruck;
    public static MoveCar moveCar;

    public static MyFrame myFrame = new MyFrame(0,"");
    public JDialog infoDialog = new JDialog();


    public Habitat(int NumberThisClient, String boxitems)
    {
        try {
            ReadFileTXT();
        } catch (IOException e) {
            e.printStackTrace();
        }


        myFrame = new MyFrame(NumberThisClient,boxitems);
        myFrame.setVisible(true);
        Listener listener = new Listener(this);
    }

    public void StartSimul(){
        On=true;

        if (Arr.isEmpty()==false) Arr.clear();
        if (ID.isEmpty()==false) ID.clear();
        if (Time_Life.isEmpty()==false) Time_Life.clear();
        //считывание начальных условий с полей на фрейме и обнуление счетчиков
        ALL=0;
        Car=0;
        Truck=0;
        pause_time=0;

        N1 = Long.parseLong(myFrame.tn1.getText());
        N2 = Long.parseLong(myFrame.tn2.getText());

        life_Time_Car = Integer.parseInt(myFrame.life1.getText());
        life_Time_Truck = Integer.parseInt(myFrame.life2.getText());

        P1 = (float)myFrame.comboBoxCar.getSelectedItem();
        P2 = (float)myFrame.comboBoxTruck.getSelectedItem();

        if(myFrame.comboBoxPriorityCar.getSelectedItem()=="Высокий") CarPriority = Thread.MAX_PRIORITY;
        if(myFrame.comboBoxPriorityCar.getSelectedItem()=="Средний") CarPriority = Thread.NORM_PRIORITY;
        if(myFrame.comboBoxPriorityCar.getSelectedItem()=="Низкий") CarPriority = Thread.MIN_PRIORITY;

        if(myFrame.comboBoxPriorityTruck.getSelectedItem()=="Высокий") TruckPriority = Thread.MAX_PRIORITY;
        if(myFrame.comboBoxPriorityTruck.getSelectedItem()=="Средний") TruckPriority = Thread.NORM_PRIORITY;
        if(myFrame.comboBoxPriorityTruck.getSelectedItem()=="Низкий") TruckPriority = Thread.MIN_PRIORITY;

        // запуск основной функции симуляции
        Init();
    }
    public void PauseSimul(){
        moveCar.goingCar=false;
        moveTruck.goingTruck=false;
        timer1.cancel();

        if(unblokDialog) {
            JDialog dialog = null;
            pause_time = m_lastTime;
            if (dialog == null) // в первый раз
                dialog = myFrame.MyDialog("Модальное", true, this);
            dialog.setVisible(true); // отобразить диалог
        }else { StopSimul(); blok=false;}
    }
    public void InfoDialog(){
        moveCar.goingCar=false;
        moveTruck.goingTruck=false;
        timer1.cancel();

        pause_time = Habitat.m_lastTime;
        infoDialog = myFrame.MyinfoDialog("Статистика", true,this);
        infoDialog.setVisible(true); // отобразить диалог
    }
    public void StopSimul(){
        timer1.cancel();
        moveTruck.goingTruck = false;
        moveCar.goingCar = false;

        myFrame.StartButton.setEnabled(true);
        myFrame.startItem.setEnabled(true);
        On=false;
        myFrame.repaint();

    }
    public void TimePanal(){
        if (time) {
            time=false;
            myFrame.rButtonOF.setSelected(true);
        }
        else {
            time = true;
            myFrame.rButtonON.setSelected(true);
        }
    }
    public void TimeTask(){
        stopTime = System.currentTimeMillis();
        if(pause_time == 0)m_lastTime = stopTime - m_startTime;
        else {
            m_lastTime = pause_time + stopTime - m_startTime;
        }
    }
    public void RandomID(){
        for (int i=0;i<1000;i++) {
            boolean tre=true;
            ObjectID = (int) (Math.random() * 10000);
            for (Integer key : Time_Life.keySet()) {
                if (ObjectID == key) {
                    tre=false;
                    break;
                }
            }
            if(tre)break;
        }
    }
    public synchronized void OtpravkaObject(){
        String nameAuto = (String) myFrame.comboBoxObmenAuto.getSelectedItem();
        String numClient = (String) myFrame.comboBoxlistClient.getSelectedItem();

        ArrayList<Auto> Arr_Auto = new ArrayList();

        for (int i = 0; i < Arr.size(); i++) {
            if (Arr.get(i).getNameAuto().equals(nameAuto)) {
                Arr_Auto.add(Arr.get(i));

                ID.remove(Arr.get(i).ObjectID);
                Time_Life.remove(Arr.get(i).ObjectID);
                Arr.remove(i);
                i--;
            }
        }
        Client.setData(numClient, nameAuto, Arr_Auto);
    }

    public synchronized void PoluchenieObject(ArrayList<Auto> Arr_Auto_get){

        for (int i = 0; i < Arr_Auto_get.size(); i++) {
            RandomID();
            Arr_Auto_get.get(i).ObjectID = ObjectID;

            Arr.add(Arr_Auto_get.get(i));
            ID.add(Arr_Auto_get.get(i).ObjectID);
            Time_Life.put(Arr_Auto_get.get(i).ObjectID,(int) m_lastTime / 1000);
        }
    }

    public void Init()     {
        //создаём новые объекты таймер и потоки
        timer1 = new Timer();
        moveTruck = new MoveTruck("Potok Truck");
        moveCar = new MoveCar("Potok Car");

        //блокируем кнопки Start
        myFrame.StartButton.setEnabled(false);
        myFrame.startItem.setEnabled(false);
        blok=false;

        m_startTime=System.currentTimeMillis();// запоминаем системное время


        TimerTask task1 = new TimerTask() {
            public void run()
            {
                TimeTask();
                synchronized (Arr) {
                    if (Math.random() <= P1) {
                        RandomID();
                        Car Cr = new Car(myFrame.panel.getWidth(), myFrame.panel.getHeight(), ObjectID, life_Time_Car);
                        ID.add(Cr.ObjectID);
                        Arr.add(Cr);
                        Time_Life.put(Cr.ObjectID, (int) m_lastTime / 1000);
                        Car++;
                        ALL++;
                    }
                }

            }
        };
         TimerTask task2 = new TimerTask() {
            public void run() {
                TimeTask();
                synchronized (Arr) {
                    if (Math.random() <= P2) {
                        RandomID();
                        Truck Tr = new Truck(myFrame.panel.getWidth(), myFrame.panel.getHeight(), ObjectID, life_Time_Truck);
                        ID.add(Tr.ObjectID);
                        Arr.add(Tr);
                        Time_Life.put(Tr.ObjectID, (int) m_lastTime / 1000);
                        Truck++;
                        ALL++;
                    }
                }
            }
        };

        TimerTask deleteObject = new TimerTask() {
            public void run()
            {
                TimeTask();
                synchronized (Arr) {
                    for (int i = 0; i < Arr.size(); i++) {
                        if (m_lastTime / 1000 - Time_Life.get(Arr.get(i).ObjectID) >= Arr.get(i).life_Time) {
                            ID.remove(Arr.get(i).ObjectID);
                            Time_Life.remove(Arr.get(i).ObjectID);
                            Arr.remove(i);
                            i--;
                        }
                    }
                }
            }
        };

        TimerTask otrisovka = new TimerTask() {
            public void run()
            {
                synchronized (Arr) {
                    myFrame.repaint();

                }
            }
        };

        //запускаем таймер
        timer1.schedule(task1, N1 * 1000, N1 * 1000);
        timer1.schedule(task2, N2 * 1000, N2 * 1000);
        timer1.schedule(deleteObject,1000,1000);
        timer1.schedule(otrisovka, 50,50);

        //присваиваем приоритеты потокам
        moveCar.setPriority(CarPriority);
        moveTruck.setPriority(TruckPriority);

        //запускаем потоки
        moveCar.start();
        moveTruck.start();


    }

    public static void WriteFileTXT()throws IOException{

        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(
                    new FileOutputStream("include/fib.txt"));
            dos.writeLong(N1);
            dos.writeFloat(P1);
            dos.writeInt(life_Time_Car);
            if (myFrame.comboBoxPriorityCar.getSelectedItem() == "Высокий") CarPriority = Thread.MAX_PRIORITY;
            if (myFrame.comboBoxPriorityCar.getSelectedItem() == "Средний") CarPriority = Thread.NORM_PRIORITY;
            if (myFrame.comboBoxPriorityCar.getSelectedItem() == "Низкий") CarPriority = Thread.MIN_PRIORITY;
            dos.writeInt(CarPriority);

            dos.writeLong(N2);
            dos.writeFloat(P2);
            dos.writeInt(life_Time_Truck);
            if (myFrame.comboBoxPriorityTruck.getSelectedItem() == "Высокий") TruckPriority = Thread.MAX_PRIORITY;
            if (myFrame.comboBoxPriorityTruck.getSelectedItem() == "Средний") TruckPriority = Thread.NORM_PRIORITY;
            if (myFrame.comboBoxPriorityTruck.getSelectedItem() == "Низкий") TruckPriority = Thread.MIN_PRIORITY;
            dos.writeInt(TruckPriority);

        }catch (IOException e){

        }
        finally {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static void ReadFileTXT()throws IOException{
        DataInputStream dis = null;

            try    {
                dis = new DataInputStream(
                        new FileInputStream("include/fib.txt"));
                N1 = dis.readLong();
                System.out.print(N1 + "\n");
                P1 = dis.readFloat();
                System.out.print(P1 + "\n");
                life_Time_Car = dis.readInt();
                System.out.print(life_Time_Car + "\n");
                CarPriority = dis.readInt();
                System.out.print(CarPriority + "\n");

                N2 = dis.readLong();
                System.out.print(N2 + "\n");
                P2 = dis.readFloat();
                System.out.print(P2 + "\n");
                life_Time_Truck = dis.readInt();
                System.out.print(life_Time_Truck + "\n");
                TruckPriority = dis.readInt();
                System.out.print(TruckPriority + "\n");


            }
            catch(IOException e) {
                System.out.println("\nEnd of file");
            }
            finally {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
    public void WriteFileSer()
    {

        ObjectOutputStream oos=null;
        try
        {
            oos = new ObjectOutputStream(new FileOutputStream("include/date.out"));
            oos.writeInt(Car);
            oos.writeInt(Truck);
            oos.writeObject(Arr);

            System.out.println("Файл записан");
        } catch (FileNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null,"Wrong output File");
            System.out.println("Wrong output File");
        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null,"Ошибка");
        }
        finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public synchronized void ReadFileSer()
    {
        ID.clear();
        Time_Life.clear();
        ObjectInputStream ois  = null;
        try
        {
            ois = new ObjectInputStream(new FileInputStream("include/date.out"));
            //n = ois.readInt();
            Car = ois.readInt();
            Truck = ois.readInt();
            Arr = (ArrayList<Auto>) ois.readObject();
            ALL = Car + Truck;

            for (int i = 0; i < ALL; i++)
            {
                ID.add(Arr.get(i).ObjectID);
                Time_Life.put(Arr.get(i).ObjectID, (int) m_lastTime/1000);
            }

            System.out.println("Файл прочитан");

        } catch (FileNotFoundException ex)
        {
            System.out.println("Wrong input File");
        } catch (IOException ex)
        {
            System.out.println("Wrong input Object");
        } catch (ClassNotFoundException ex)
        {
            System.out.println("Empty File");
        }
        finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
