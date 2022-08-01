import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class start extends  JFrame {
    private JPanel start;
    private JTextField name;
    private JTextField dname;
    private JTextField port;
    private JButton startButton;
    private JLabel error;
    public static List<users> Online_users = new ArrayList<>();
    public static List<users> NewOnline_users = new ArrayList<>();
    public static users me;
    public start(){
        this.setTitle("Start");
        this.setVisible(true);
        this.setSize(400,250);
        add(start);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    //Send my info to tracker
                    String Uname = name.getText();
                    String Udname = dname.getText();
                    String Uport = port.getText();
                    if (Uname.equals("") || Udname.equals("")){
                        error.setText("Name and/or Domain name can't be empty");
                    }else {
                        me = new users(Uname,Uport,Integer.parseInt(Uport));
                        DatagramSocket CS = new DatagramSocket();
                        InetAddress Adress = InetAddress.getByName("localhost");

                        byte[] sendData = new byte[1024];
                        String s = Uname + " " + Udname + " " + Uport;
                        sendData = s.getBytes();
                        DatagramPacket SendPacket = new DatagramPacket(sendData, sendData.length, Adress, 9999);
                        CS.send(SendPacket);
                        CS.close();

                        //get All users
                        CS = new DatagramSocket();


                        sendData = new byte[1024];
                        s = "$#Send%All*^&";
                        sendData = s.getBytes();
                        SendPacket = new DatagramPacket(sendData, sendData.length, Adress, 9999);
                        CS.send(SendPacket);
                        CS.close();

                        //Peer Server
                        (new Thread(){
                            @Override
                            public void run(){
                                try{
                                    DatagramSocket clientSocket = new DatagramSocket(me.getPort());
                                    while (true) {
                                        byte[] receiveData = new byte[1024];
                                        DatagramPacket Paket = new DatagramPacket(receiveData, receiveData.length);
                                        //Receive Data From peers
                                        clientSocket.receive(Paket);
                                        String Data = new String(Paket.getData());
                                        Data.trim();
                                        if (Data.trim().contains("S&e#n@d*F_@!")){
                                            String []split = Data.trim().split("_@!",4);
                                            String fname = split[2];
                                            chattApp.chat.setText(chattApp.chat.getText() +"\n"+ split[0]+fname);

                                            String OutData = split[3];

                                            String path= new File("").getAbsolutePath();
                                            File dir = new File(path.concat("\\src\\Downloads\\"+me.getName()));
                                            dir.mkdir();
                                            path = path.concat("\\src\\Downloads\\"+me.getName()+"\\"+fname);
                                            File f =new File(path);
                                            System.out.println(path);

                                            if (f.createNewFile() ) {
                                                FileWriter fw = new FileWriter(path);
                                                BufferedWriter out = new BufferedWriter(fw);
                                                out.write(OutData);
                                                out.close();
                                                fw.close();

                                            } else {
                                                FileWriter fw = new FileWriter(path);
                                                fw.write(OutData);
                                                fw.close();
                                            }
                                        }
                                        else {
                                            System.out.println(Data.trim());
                                            chattApp.chat.setText(chattApp.chat.getText() +"\n"+Data.trim());


                                        }

                                    }

                                }catch (Exception exp){
                                    exp.printStackTrace();
                                }
                            }

                        }).start();



                        Main.start.setVisible(false);
                        Main.chatApp.setVisible(true);
                        Main.chatApp.setTitle("P2P Chat App ["+me.getName()+"]");

                    }

                }catch (Exception exp){
                    exp.printStackTrace();
                }

            }
        });
    }

}


