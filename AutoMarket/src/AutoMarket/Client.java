package AutoMarket;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static AutoMarket.Habitat.m_lastTime;

public class Client {
    static int NumberThisClient;
    static ObjectInputStream inStream;
    static ObjectOutputStream outStream;
    //static InputStreamReader in;
    //static OutputStreamWriter out;

    static Socket fromserver = null;
    static Habitat habitat;

    public Client()
    {
        System.out.println("Welcome to Client side");
        String S = "localhost";
        String boxitems=null;

        //запрашивать в строку количесвто клиентов
        System.out.println("Connecting to... " + S);

        try
        {
            fromserver = new Socket(S, 7777);

            //in = new InputStreamReader(fromserver.getInputStream());
            //out = new OutputStreamWriter(fromserver.getOutputStream());

            inStream = new ObjectInputStream(fromserver.getInputStream());
            outStream = new ObjectOutputStream(fromserver.getOutputStream());
            outStream.flush();

            boxitems = (String) inStream.readObject();
            System.out.println("списоок: "+boxitems);
            NumberThisClient = (int) inStream.readObject();
            System.out.println("номер: "+NumberThisClient);

            //при подключении должен отправить заявку на измение боксов
            outStream.writeObject("list");
            outStream.writeObject("add");
            outStream.writeObject(NumberThisClient);

        } catch (IOException ex)
        {
            System.out.println("Соединение не установлено");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        habitat = new Habitat(NumberThisClient,boxitems);

        Thread t1 = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    getData();
                }
            }
        });
        t1.start();
    }

    //-----------------------------------------------
   static void meGoClose()
    {
        try
        {
            outStream.writeObject("list");
            outStream.writeObject("remove");
            outStream.writeObject(NumberThisClient);

            inStream.close();
            outStream.close();
            fromserver.close();
        } catch (IOException ex)
        {
        }
    }

   static void setData(String numClient, String nameAuto, ArrayList<Auto> Arr_Auto) {


        try {
            outStream.writeObject("zapros_na_obmen");
            outStream.writeObject(NumberThisClient);
            outStream.writeObject(Integer.parseInt(numClient));
            outStream.writeObject(nameAuto);

            outStream.writeObject(Arr_Auto);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void getData()
    {
        try
        {
            String flag = (String)inStream.readObject();

            if ("list".equals(flag))
            {
                String operationWithBox = (String)inStream.readObject();

                if ("add".equals(operationWithBox)) //добавить
                {
                    String numWhichAdd = (String)inStream.readObject();
                    System.out.println("Бокс: " + numWhichAdd);
                    habitat.myFrame.comboBoxlistClient.addItem(numWhichAdd);
                } else  //удалить
                {
                    String numWhichRemove = (String)inStream.readObject();
                    habitat.myFrame.comboBoxlistClient.removeItem(numWhichRemove);
                }
            } else
            {

                if("zapros_na_obmen".equals(flag)){

                    ArrayList<Auto> Arr_Auto_set = new ArrayList();
                    ArrayList<Auto> Arr_Auto_get = new ArrayList();

                    int numClient_sender = (int) inStream.readObject();
                    String nameAuto = (String)inStream.readObject();

                    try {
                        Arr_Auto_get =  (ArrayList<Auto>) inStream.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    outStream.writeObject("otwet_na_obmen");
                    outStream.writeObject(numClient_sender);

                    synchronized (Habitat.Arr) {
                        for (int i = 0; i < Habitat.Arr.size(); i++) {
                            if (Habitat.Arr.get(i).getNameAuto().equals(nameAuto)) {
                                Arr_Auto_set.add(Habitat.Arr.get(i));
                                Habitat.ID.remove(Habitat.Arr.get(i).ObjectID);
                                Habitat.Time_Life.remove(Habitat.Arr.get(i).ObjectID);
                                Habitat.Arr.remove(i);
                                i--;
                            }
                        }

                        habitat.PoluchenieObject(Arr_Auto_get);//добавление принятых объектов в коллекцию

                    }
                    outStream.writeObject(Arr_Auto_set);
                }

                if("otwet_na_obmen".equals(flag)){

                    ArrayList<Auto> Arr_Auto_get = new ArrayList();

                    try {
                        Arr_Auto_get = (ArrayList<Auto>) inStream.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    habitat.PoluchenieObject(Arr_Auto_get);//добавление принятых объектов в коллекцию
                    System.out.println("Ответ получен ");
                }

            }
        } catch (IOException ex)
        {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
