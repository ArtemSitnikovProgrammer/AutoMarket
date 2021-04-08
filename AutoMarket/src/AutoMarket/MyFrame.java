package AutoMarket;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Timer;

import static java.awt.event.KeyEvent.VK_ENTER;


public class MyFrame extends JFrame {

    //Панель симуляции
    Image Back;
    Font font = new Font("Courier", Font.ITALIC, 50), font1 = new Font("Monaco", Font.ITALIC, 50), font2 = new Font("Arial", Font.ITALIC, 50);
    Font fontArea1 = new Font("Courier", Font.ITALIC, 40);
    Font fontLabl = new Font("Courier", Font.ITALIC, 10);
    MyPanel panel = new MyPanel();

    //Панель Меню
    JMenuBar panelMenu=new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem startItem = new JMenuItem("Start");
    JMenuItem stopItem = new JMenuItem("Stop");
    JMenuItem saveAllItem = new JMenuItem("Save All");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem exitItem = new JMenuItem("Exit");

    //Панель Параметров
    JPanel panelParametr=new JPanel();
    JButton StartButton, StopButton, infoButton, playCarButton, playTruckButton, pauseCarButton, pauseTruckButton, consolButton, obmenButton;
    JLabel N1Label, N2Label, P1Label, P2Label, carlabel, trucklabel, life1Label, life2Label,priorityLabel,priority2Label;
    JTextField tn1, tn2, tp1, tp2,life1, life2;
    JRadioButton rButtonON, rButtonOF,rButtonDialogOF, rButtonDialogON ;
    JSlider sliderCar, sliderTruck;
    JComboBox comboBoxTruck, comboBoxCar, comboBoxPriorityCar, comboBoxPriorityTruck, comboBoxObmenAuto, comboBoxlistClient;


    public MyFrame(int NumberThisClient, String boxitems) {
        super("AutoMarket №" + NumberThisClient);
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //размеры панелей
        panelParametr.setPreferredSize(new Dimension(350, getHeight()));

        try {
            Back = ImageIO.read(new File("image/fon.png"));
        } catch (IOException e) {
            System.out.println("Error");
        }

        MyPanel panelTimer = new MyPanel(){
            @Override
            public void paintComponent(Graphics g) {
                if (Habitat.time) {
                    g.setColor(Color.RED);
                    g.setFont(new Font("Courier", Font.TYPE1_FONT, 50));
                    g.drawString("" + Habitat.m_lastTime / 1000, 15, 40);
                }
            }
        };

        panelTimer.setPreferredSize(new Dimension(50, 50));
        //инициализация всех объектов фрейма
        StartButton = new JButton("Start");
        StopButton = new JButton("Stop");
        infoButton = new JButton("Состояние");
        playCarButton = new JButton();
        playTruckButton = new JButton();
        consolButton = new JButton("Консоль");
        obmenButton = new JButton("Обмен с клиентом");
        try {
            Image imgPlay = ImageIO.read(new File("image/Play2mini.png"));
            playCarButton.setIcon(new ImageIcon(imgPlay));
            playTruckButton.setIcon(new ImageIcon(imgPlay));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        pauseCarButton = new JButton();
        pauseTruckButton = new JButton();
        try {
            Image imgPause = ImageIO.read(new File("image/Pause2mini.png"));
            pauseCarButton.setIcon(new ImageIcon(imgPause));
            pauseTruckButton.setIcon(new ImageIcon(imgPause));
        } catch (Exception ex) {
            System.out.println(ex);
        }

        N1Label = new JLabel("Время появления");N1Label.setFont(fontLabl);
        N2Label = new JLabel("Время появления");N2Label.setFont(fontLabl);
        P1Label = new JLabel("Вероятность появления");P1Label.setFont(fontLabl);
        P2Label = new JLabel("Вероятность появления");P2Label.setFont(fontLabl);
        life1Label = new JLabel("Время жизни");life1Label.setFont(fontLabl);
        life2Label = new JLabel("Время жизни");life2Label.setFont(fontLabl);
        priorityLabel = new JLabel("Приоритет");priorityLabel.setFont(fontLabl);
        priority2Label = new JLabel("Приоритет");priority2Label.setFont(fontLabl);
        carlabel = new JLabel( new ImageIcon("image/car1.png"));
        trucklabel = new JLabel( new ImageIcon("image/truck1.png"));
        tn1 = new JTextField(Long.toString(Habitat.N1),5);
        tn2 = new JTextField(Long.toString(Habitat.N2),5);
        tp1 = new JTextField(Float.toString(Habitat.P1),5);
        tp2 = new JTextField(Float.toString(Habitat.P2),5);
        life1 = new JTextField(Long.toString(Habitat.life_Time_Car),5);
        life2 = new JTextField(Long.toString(Habitat.life_Time_Truck),5);
        rButtonON = new JRadioButton("Показывать", true); //rButtonON.setFont(fontLabl);
        rButtonOF = new JRadioButton("Не показывать"); //rButtonOF.setFont(fontLabl);
        rButtonDialogOF = new JRadioButton("Не показывать",true); //rButtonDialogOF.setFont(fontLabl);
        rButtonDialogON = new JRadioButton("Показывать"); //rButtonDialogON.setFont(fontLabl);

        //JComboBox для Car
        Float[] mod = {0.0f, 0.1f,0.2f,0.3f,0.4f,0.5f,0.6f,0.7f,0.8f,0.9f,1f};
        comboBoxCar = new JComboBox(mod);
        comboBoxCar.setSelectedItem(Habitat.P1);

        //JComboBox для Truck
        comboBoxTruck = new JComboBox(mod);
        comboBoxTruck.setSelectedItem(Habitat.P2);

        //JComboBox PriorityCar
        String[] modPriority ={"Высокий","Средний","Низкий"};
        comboBoxPriorityCar = new JComboBox(modPriority);
        if(Habitat.CarPriority == Thread.MAX_PRIORITY) comboBoxPriorityCar.setSelectedItem(modPriority[0]);
        if(Habitat.CarPriority == Thread.NORM_PRIORITY) comboBoxPriorityCar.setSelectedItem(modPriority[1]);
        if(Habitat.CarPriority == Thread.MIN_PRIORITY) comboBoxPriorityCar.setSelectedItem(modPriority[2]);



        //JComboBox PriorityTruck
        comboBoxPriorityTruck = new JComboBox(modPriority);
        if(Habitat.TruckPriority == Thread.MAX_PRIORITY) comboBoxPriorityTruck.setSelectedItem(modPriority[0]);
        if(Habitat.TruckPriority == Thread.NORM_PRIORITY) comboBoxPriorityTruck.setSelectedItem(modPriority[1]);
        if(Habitat.TruckPriority == Thread.MIN_PRIORITY) comboBoxPriorityTruck.setSelectedItem(modPriority[2]);

        String[] modList = boxitems.split(" ");
        comboBoxlistClient = new JComboBox(modList);
        String[] modObmen ={"Car","Truck"};
        comboBoxObmenAuto = new JComboBox(modObmen);

        //создание бокса для параметров Car
        Box box = Box.createVerticalBox();
        box.setBorder(new TitledBorder("Параметры Car"));
      //  box.add(carlabel);
        box.add(N1Label);
        box.add(tn1);
        box.add(life1Label);
        box.add(life1);
        box.add(Box.createVerticalStrut(5));
        box.add(P1Label);
        box.add(comboBoxCar);
        box.add(priorityLabel);
        box.add(comboBoxPriorityCar);
        box.add(playCarButton);
        box.add(pauseCarButton);

        //создание бокса для параметров Truck
        Box box2 = Box.createVerticalBox();
        box2.setBorder(new TitledBorder("Параметры Truck"));
        //box2.add(trucklabel);
        box2.add(N2Label);
        box2.add(tn2);
        box2.add(life2Label);
        box2.add(life2);
        box2.add(Box.createVerticalStrut(5));
        box2.add(P2Label);
        box2.add(comboBoxTruck);
        box2.add(priority2Label);
        box2.add(comboBoxPriorityTruck);
        box2.add(playTruckButton);
        box2.add(pauseTruckButton);

        // создание box для таймера
        Box box3 = Box.createVerticalBox();
        ButtonGroup bg = new ButtonGroup(); // создаем группу взаимного исключения
        bg.add(rButtonON);
        bg.add(rButtonOF); // сделали радиокнопки взаимоисключающими
        box3.add(rButtonON);
        box3.add(rButtonOF); // добавили радиокнопки на панель box3
        box3.add(panelTimer);
        box3.setBorder(new TitledBorder("Время симуляции"));

        // создание box для РАзрешения диалога
        Box box4 = Box.createVerticalBox();
        ButtonGroup group = new ButtonGroup(); // создаем группу взаимного исключения
        group.add(rButtonDialogON);
        group.add(rButtonDialogOF); // сделали радиокнопки взаимоисключающими
        box4.add(rButtonDialogON);
        box4.add(rButtonDialogOF); // добавили радиокнопки на панель box3
        box4.setBorder(new TitledBorder("Диалог"));


        //Добавление элементов в панель параметров
        panelParametr.add(box);// Car
        panelParametr.add(box2);//Truck
        panelParametr.add(StartButton);
        panelParametr.add(StopButton);
        panelParametr.add(infoButton);
        panelParametr.add(consolButton);
        panelParametr.add(box3);//Timer
        panelParametr.add(box4);//Dialog
        panelParametr.add(obmenButton);
        panelParametr.add(comboBoxlistClient);
        panelParametr.add(comboBoxObmenAuto);



        //добавление элементов в меню
        panelMenu.add(fileMenu);
            fileMenu.add(startItem);
            fileMenu.addSeparator();
            fileMenu.add(stopItem);
            fileMenu.addSeparator();
            fileMenu.add(saveAllItem);
            fileMenu.addSeparator();
            fileMenu.add(openItem);
            fileMenu.addSeparator();
            fileMenu.add(exitItem);

        //Добавление панелей в Фрейм
        setJMenuBar(panelMenu);
        add(panel, BorderLayout.CENTER);
        add(panelParametr, BorderLayout.EAST);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    Habitat.WriteFileTXT();

                    Client.meGoClose();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    //Диалоговое окно
    public JDialog MyDialog(String title, boolean modal, Habitat habitat)
    {
        JDialog dialog = new JDialog(this, title, modal);
        JPanel panel = new JPanel();
        JPanel paneArea = new JPanel();
        JTextArea area1 = new JTextArea();
        JButton ok = new JButton("ОК");
        JButton otmena = new JButton("Отмена");


        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300, 300);

        //задаем параметры TextArea
        area1.setFont(fontArea1);
        area1.setText("Time: " + Habitat.m_lastTime / 1000 +
                "\nCar: " + Habitat.Car +
                "\nTruck: " + Habitat.Truck +
                "\nSumm: " + Habitat.ALL);
        area1.setEnabled(false);

        paneArea.add(area1);
        panel.add(ok);
        panel.add(otmena);
        dialog.add(paneArea, BorderLayout.CENTER);
        dialog.add(panel, BorderLayout.SOUTH);

        // Обработчики кнопок ОК и Отмена.
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                habitat.StopSimul();
                dialog.setVisible(false);
            }
        });

        otmena.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                habitat.Init();
                dialog.setVisible(false);

            }
        });
        return dialog;
    }

    public JDialog MyinfoDialog(String title, boolean modal, Habitat habitat)
    {

        JDialog dialog = new JDialog(this, title, modal);
        JPanel panel = new JPanel();
        JButton ok = new JButton("ОК");



        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(300, 300);

        MyPanel panelinfo = new MyPanel(){
            @Override
            public void paintComponent(Graphics g) {
                    for (int i = 0; i < Habitat.Arr.size(); i++) {
                        g.drawString("" + Habitat.Arr.get(i).getNameAuto() +"    "+ Habitat.Arr.get(i).ObjectID+"     "+Habitat.Time_Life.get(Habitat.Arr.get(i).ObjectID), 0, i*20+10);


                }
            }
        };

        JScrollPane jsp = new JScrollPane(panelinfo, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.add(ok);
        dialog.add(jsp, BorderLayout.CENTER);
        dialog.add(panel,BorderLayout.SOUTH);
        jsp.setVisible(true);

        // Обработчики кнопок ОК
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dialog.setVisible(false);
                habitat.Init();
            }
        });

        return dialog;
    }

    public static class MyConsolDialog extends Thread{


        public static JTextArea consolTextArea = new JTextArea();
        String textOperation = new String("Введите операцию(Остановить/Продолжить): ");
        String textStop = new String("Остановить");
        String textStart = new String ("Продолжить");

        private static PipedWriter pwCar;
        private static PipedWriter pwTruck;

        MyConsolDialog(String title, boolean modal, Habitat habitat){

            JDialog dialog = new JDialog(habitat.myFrame, title, modal);
            JScrollPane jsp = new JScrollPane(consolTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            dialog.setSize(500, 300);
            dialog.add(jsp, BorderLayout.CENTER);
            jsp.setVisible(true);
            dialog.setVisible(true);
            consolTextArea.setText(textOperation);
            pwCar = new PipedWriter();
            pwTruck = new PipedWriter();
        }

        public static PipedWriter getStreamCar()  { return pwCar;}
        public static PipedWriter getStreamTruck()  { return pwTruck;}
        @Override
        public void run() {

            consolTextArea.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent k)
                {
                    if (k.getKeyCode() == VK_ENTER) {

                        String[] stringMas = consolTextArea.getText().split(" ");
                        String comanda = new String(stringMas[stringMas.length - 1]);

                        if (comanda.startsWith(textStart)== true) {
                            consolTextArea.append("\nПоток движения ЗАПУЩЕН: )\n" + textOperation);
                            consolTextArea.setCaretPosition(consolTextArea.getDocument().getLength());
                            try     {
                                pwCar.write(1);
                                pwTruck.write(1);
                                System.out.println("Передали: " + textStart);
                            }
                            catch(Exception e)     {
                                System.err.println("From MyConsolDialog.run(): " + e) ;
                            }
                        }

                        if (comanda.startsWith(textStop)== true) {
                            consolTextArea.append("\nПоток движения ОСТАНОВЛЕН: )\n" + textOperation);
                            consolTextArea.setCaretPosition(consolTextArea.getDocument().getLength());
                            try     {
                                pwCar.write(0);
                                pwTruck.write(0);
                                System.out.println("Передали: " + textStop);
                            }
                            catch(Exception e)     {
                                System.err.println("From MyConsolDialog.run(): " + e) ;
                            }
                        }
                        if (comanda.startsWith(textStart)== false && comanda.startsWith(textStop)== false){
                            consolTextArea.append("\nНекорректная команда. Введите 'Продолжить' или 'Остановить': ");
                            try     {
                                pwCar.write(2);
                                pwTruck.write(2);
                                System.out.println("Передали: Ошибка!!!");
                            }
                            catch(Exception e)     {
                                System.err.println("From MyConsolDialog.run(): " + e) ;
                            }
                        }
                    }
                }
            });
        }

    }

    // отрисовка панели
    public class MyPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {

            if (Habitat.On) {
                g.drawImage(Back, 0, 0, panel.getWidth(),panel.getHeight(),null);
                g.setColor(Color.GREEN);
                g.fillRect(getWidth() / 2 - 5, 0, 10, getHeight());
                g.fillRect(0, getHeight() / 2 - 5, getWidth(), 10);
                g.setColor(Color.BLACK);
                g.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
                g.drawLine(0, getHeight() / 2, getWidth(), getHeight()/2);
                for (int i = 0; i < Habitat.Arr.size(); i++) {
                    g.drawImage(Habitat.Arr.get(i).getIm(), Habitat.Arr.get(i).x, Habitat.Arr.get(i).y, this);
                }
            } else {

                g.drawImage(Back, 0, 0, panel.getWidth(),panel.getHeight(),null);
                g.setColor(Color.WHITE);
                g.fillRect(getWidth() / 2 - 100, getHeight() / 2 - 100, 250, 200);
                g.setColor(Color.BLACK);
                g.setFont(font);
                g.drawString("Time: " + Habitat.m_lastTime / 1000, getWidth() / 2-100, getHeight() / 2 - 40);
                g.setFont(font1);
                g.setColor(Color.ORANGE);
                g.drawString("Car: " + Habitat.Car, getWidth() / 2-100, getHeight() / 2);
                g.setFont(font2);
                g.setColor(Color.BLUE);
                g.drawString("Truck: " + Habitat.Truck, getWidth() / 2-100, getHeight() / 2 + 40);
                g.setColor(Color.RED);
                g.setFont(font);
                g.drawString("Summ: " + (Habitat.ALL), getWidth() / 2-100, getHeight() / 2 + 80);
            }

        }

    }
}