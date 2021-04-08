package AutoMarket;

import javax.swing.*;
import java.awt.event.*;


public class Listener {
    public Listener(Habitat habitat){

        ActionListener playPotokTruck = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    habitat.moveTruck.myresume();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Поток запущен");
                }
            }
        };
        ActionListener pausePotokTruck = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    habitat.moveTruck.mysuspend();

                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"Поток уже ожидает");
                }
            }
        };
        ActionListener playPotokCar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    habitat.moveCar.myresume();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Поток запущен");
                }
            }
        };
        ActionListener pausePotokCar = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    habitat.moveCar.mysuspend();

                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"Поток уже ожидает");
                }
            }
        };

        ActionListener start = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    habitat.StartSimul();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Некорректно заданы значения");
                }
            }
        };
        ActionListener stop = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    habitat.StopSimul();
                    //habitat.PauseSimul();
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"Симуляция не запущена!!!");
                }
            }
        };

        ActionListener info = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    habitat.InfoDialog();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Ошибка");
                }
            }
        };

        ActionListener consolListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MyFrame.MyConsolDialog consolDialog = new MyFrame.MyConsolDialog("Консоль", false, habitat);
                    MoveCar.pipedConnection();
                    MoveTruck.pipedConnection();
                    consolDialog.start();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Ошибка");
                }
            }
        };

        ActionListener obmenListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    habitat.OtpravkaObject();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Ошибка");
                }
            }
        };

        ActionListener saveAllListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.WriteFileSer();

            }
        };

        ActionListener openListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.StopSimul();
                habitat.StartSimul();
                habitat.ReadFileSer();
            }
        };


        //Обработчик событий мыши, переключает фокус на панель Параметров
        habitat.myFrame.panelParametr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                habitat.myFrame.panelParametr.requestFocus();
            }
        });
        //Обработчик событий клавиатуры на панели Параметров
        habitat.myFrame.panelParametr.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                System.out.println(e);
                int keyCode = e.getKeyCode();
                switch(keyCode){
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                    case KeyEvent.VK_B: //start
                        try {
                            if(habitat.blok) habitat.StartSimul();

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,"Некорректно заданы значения");
                        }
                        break;
                    case KeyEvent.VK_E: //stop
                        habitat.PauseSimul();
                        break;
                    case KeyEvent.VK_T:
                        habitat.TimePanal();
                        break;
                    default:
                        System.out.println("Incorrect command");
                        break;
                }
            }
        });


        //добавление слушателей на кнопки панели параметров
        habitat.myFrame.playTruckButton.addActionListener(playPotokTruck);
        habitat.myFrame.pauseTruckButton.addActionListener(pausePotokTruck);
        habitat.myFrame.playCarButton.addActionListener(playPotokCar);
        habitat.myFrame.pauseCarButton.addActionListener(pausePotokCar);
        habitat.myFrame.StartButton.addActionListener(start);
        habitat.myFrame.StopButton.addActionListener(stop);
        habitat.myFrame.infoButton.addActionListener(info);
        habitat.myFrame.consolButton.addActionListener(consolListener);
        habitat.myFrame.obmenButton.addActionListener(obmenListener);

        //добавление cлушателей в меню/File
        habitat.myFrame.startItem.addActionListener(start);
        habitat.myFrame.stopItem.addActionListener(stop);
        habitat.myFrame.saveAllItem.addActionListener(saveAllListener);
        habitat.myFrame.openItem.addActionListener(openListener);


        // слушатель радиокнопки Показывать
        habitat.myFrame.rButtonOF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.time=false;
            }
        });
        // слушатель радиокнопки Не показывать
        habitat.myFrame.rButtonON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.time=true;
            }
        });

        // слушатель радиокнопки Показывать диалог
        habitat.myFrame.rButtonDialogOF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.unblokDialog=false;
            }
        });
        // слушатель радиокнопки Не показывать диалог
        habitat.myFrame.rButtonDialogON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habitat.unblokDialog=true;
            }
        });

        //слушатель кнопки exit в меню/File
        habitat.myFrame.exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


    }
}
