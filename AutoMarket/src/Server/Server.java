package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread
{

    static int numberClient;
    public static ArrayList<ClientEdit> List_Client = new ArrayList<ClientEdit>();

    ObjectInputStream inStream = null;
    //InputStreamReader in = null;


    static ServerSocket servers = null;
    static boolean boolCloseServer = false;
    Socket sKlient = null;

    class ClientEdit
    {
        Socket socket;
        boolean closed;
        ObjectOutputStream outStream = null;
      //  OutputStreamWriter out = null;

        public ClientEdit(Socket s)
        {
            socket = s;
            closed = false;

            try
            {
                //out = new OutputStreamWriter(s.getOutputStream());
                outStream = new ObjectOutputStream(s.getOutputStream());
                outStream.flush();


            } catch (IOException ex)
            {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public Server(Socket s)
    {
        sKlient = s;
        ClientEdit c = new ClientEdit(s);
        List_Client.add(c);
        try
        {


            //in = new InputStreamReader(s.getInputStream());
            inStream = new ObjectInputStream(s.getInputStream());

            String b=""; int bb;
            for(int j = 0;j<List_Client.size();j++)
            {
                if(List_Client.get(j).closed==false)
                {
                    bb=j+1;
                    b+=bb;
                    b+=" ";
                }
            }

            System.out.println("Список клиентов: " + b);


            c.outStream.writeObject(b);
            c.outStream.writeObject(numberClient);
            numberClient++;
        } catch (IOException e)
        {
            System.out.println("Соединение не установлено");
            System.out.println("ошибка: " + e);
        }

    }

    public static void main(String[] args)
    {
        System.out.println("Welcome to Server side");

        int k = 0;
        Socket fromclient = null;
        numberClient = 1;
        // create server socket

        try
        {
            servers = new ServerSocket(7777);
            while (k<9)
            {
                fromclient = servers.accept();
                System.out.println("Соединение с клиентом ");
                Server nst = new Server(fromclient);
                nst.start();
                k++;
            }
        } catch (Exception e)
        {
            System.out.println("Couldn't listen to port 7777");
            System.out.println("ошибка: " + e);
        }
    }

    public void run()
    {
        try
        {
            String flag, operationWithBox,  nameAuto;
            int numClient_recipient, numClient_sender;
            System.out.println("Wait for messages");

            while ((flag = (String)inStream.readObject()) != null)
            {
                if ("list".equals(flag))
                {
                    System.out.print("Обновление списка: ");
                    operationWithBox = (String)inStream.readObject();
                    if ("add".equals(operationWithBox))//добавляем клиент в бокс
                    {
                        System.out.println("Добавление");
                        numClient_recipient = (int) inStream.readObject();
                        for (int i = 0; i < (numClient_recipient - 1); i++)
                        {
                            if (List_Client.get(i).closed == false)
                            {

                                List_Client.get(i).outStream.writeObject("list");
                                List_Client.get(i).outStream.writeObject("add");
                                List_Client.get(i).outStream.writeObject(String.valueOf(numClient_recipient));
                            }
                        }
                    } else //удалить из бокса
                    {
                        System.out.println("Удаление");
                        numClient_recipient = (int) inStream.readObject();
                        List_Client.get(numClient_recipient - 1).closed=true;
                        for (int i = 0; i < List_Client.size(); i++)
                        {
                            if (List_Client.get(i).closed == false)
                            {

                                List_Client.get(i).outStream.writeObject("list");
                                List_Client.get(i).outStream.writeObject("remove");
                                List_Client.get(i).outStream.writeObject(String.valueOf(numClient_recipient));
                            }
                        }

                        inStream.close();
                        List_Client.get(numClient_recipient - 1).outStream.close();
                        List_Client.get(numClient_recipient - 1).socket.close();
                        break;
                    }
                } else //происходит обмен объектами
                {
                    if("zapros_na_obmen".equals(flag)){

                        System.out.println("Выполняется обмен");

                        //прием от клиента запроса на обмен
                        numClient_sender = (int) inStream.readObject();//получаем номер клиента отправителя
                        numClient_recipient = (int) inStream.readObject();//получаем номер клиента с которым будет обмен (получатель)
                        nameAuto = (String) inStream.readObject();//получаем имя объектов обмена

                        System.out.println(nameAuto+" Отправитель: " + numClient_sender +" Получатель: " + numClient_recipient);

                        //отправка запроса и передача данных второму клиенту
                        List_Client.get(numClient_recipient-1).outStream.writeObject("zapros_na_obmen");
                        List_Client.get(numClient_recipient-1).outStream.writeObject(numClient_sender);
                        List_Client.get(numClient_recipient-1).outStream.writeObject(nameAuto);


                        try {
                            List_Client.get(numClient_recipient-1).outStream.writeObject(inStream.readObject());

                            //Arr_Auto_one = (ArrayList<Object>) inStream.readObject();//получаем массив объектов от первого клиента
                            System.out.println("Массив первого клиента получен и отправлен второму!!!");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }else { //команда otwet_na_obmen

                        numClient_sender = (int) inStream.readObject();

                        //отправка ответа и передача данных первому клиенту
                        List_Client.get(numClient_sender-1).outStream.writeObject("otwet_na_obmen");

                        try {
                            List_Client.get(numClient_sender-1).outStream.writeObject(inStream.readObject());

                            //Arr_Auto_one = (ArrayList<Object>) inStream.readObject();//получаем массив объектов от первого клиента
                            System.out.println("Массив второго клиента получен и отправлен первому!!!");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Обмен закончен");
                    }

                }
            }
            for (int i = 0; i < List_Client.size(); i++)
            {
                if (List_Client.get(i).closed == false)
                {
                    boolCloseServer = false;
                    break;
                }
                boolCloseServer = true;
            }
            if (boolCloseServer == true)
            {
                servers.close();
            }
        } catch (IOException ex)
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
