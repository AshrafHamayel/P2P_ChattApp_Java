import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class chattApp extends JFrame {
    private JPanel root;
    private JRadioButton publicChatRadioButton;
    private JPanel center;
    private JPanel south;
    private JTextField msg;
    private JButton send;
    private JButton sendFileButton;
    private JTextArea textArea1;
    private JComboBox peers;
    private JRadioButton privateChatRadioButton;
    public static JComboBox peer;
    public static JTextArea chat;
    public static List<String> chatS = new ArrayList<>();
    public static String PubliChat = new String("Public Char");

    public chattApp()  {
        this.setTitle("P2P Chat App");
        add(root);
        setSize(610,450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try{
                    DatagramSocket CS = new DatagramSocket();
                    InetAddress Adress = InetAddress.getByName("localhost");

                    byte[] sendData = new byte[1024];
                    String s = "!D@el$et%e_"+start.me.getPort();
                    sendData = s.getBytes();
                    DatagramPacket SendPacket = new DatagramPacket(sendData, sendData.length, Adress, 9999);
                    CS.send(SendPacket);
                    CS.close();
                    System.exit(0);


                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        peer =peers;
        chat=textArea1;

        //Update Peers
        (new Thread(){
            @Override
            public void run(){
                while (true){
                    try{
                        start.NewOnline_users.clear();
                        Thread.sleep(1000);
                        InetAddress Adress = InetAddress.getByName("localhost");
                        DatagramSocket CS = new DatagramSocket();

                        byte[] sendData = new byte[1024];
                        String s = "$#Send%All*^&";
                        sendData = s.getBytes();
                        DatagramPacket SendPacket = new DatagramPacket(sendData, sendData.length, Adress, 9999);
                        CS.send(SendPacket);

                        byte[] receiveData = new byte[1024];
                        DatagramPacket Paket = new DatagramPacket(receiveData, receiveData.length);
                        CS.receive(Paket);
                        String Data = new String(Paket.getData());
                        String split[] = Data.trim().split("#");

                        for (String user : split) {
                            String temp[] = user.split("-");
                            if(temp.length == 3){
                                users u = new users(temp[0].trim(), temp[1].trim(), Integer.parseInt(temp[2].trim()));
                                start.NewOnline_users.add(u);

                            }
                        }

                          int index =chattApp.peer.getSelectedIndex();
                            peer.removeAllItems();
                            start.Online_users.clear();
                            for (int i = 0; i < start.NewOnline_users.size(); i++){
                                if ( !start.me.equals(null) && start.NewOnline_users.get(i).getPort()!= start.me.getPort() ){
                                    peer.addItem(start.NewOnline_users.get(i));
                                    start.Online_users.add(start.NewOnline_users.get(i));
                                }

                            }
                            chattApp.peer.setSelectedIndex(index);

                    }catch (NullPointerException e){

                    }
                    catch (IllegalArgumentException ea){

                    }
                    catch (Exception exp){
                        exp.printStackTrace();
                    }
                }
            }

        }).start();



        // Send message
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //public chat
                try{

                    if(publicChatRadioButton.isSelected()){
                        chat.setText(chat.getText()+"\n"+"[me to EveryOne]: "+msg.getText());
                        DatagramSocket CS = new DatagramSocket();
                        InetAddress Adress = InetAddress.getByName("localhost");
                        for (users u:start.Online_users) {
                            if (u.getPort()!=start.me.getPort()){
                                byte[] sendData = new byte[1024];
                                String s ="["+start.me.getName()+" to EveryOne]: " +msg.getText();
                                sendData = s.getBytes();
                                DatagramPacket SendPacket = new DatagramPacket(sendData, sendData.length, Adress, u.getPort());
                                CS.send(SendPacket);

                            }
                            else  if (u.getPort() == start.me.getPort()){

                            }
                        }
                        msg.setText("");
                        CS.close();



                    }
                    //private chat
                    else if(privateChatRadioButton.isSelected()){

                        DatagramSocket CS = new DatagramSocket();
                        InetAddress Adress = InetAddress.getByName("localhost");

                        byte[] sendData = new byte[1024];
                        chat.setText(chat.getText()+"\n"+"[me to "+start.Online_users.get(peer.getSelectedIndex()).getName()+"]: "+msg.getText());
                        String s ="["+start.me.getName()+" to me]: " +msg.getText();
                        sendData = s.getBytes();
                        DatagramPacket SendPacket = new DatagramPacket(sendData, sendData.length, Adress, start.Online_users.get(peer.getSelectedIndex()).getPort());
                        CS.send(SendPacket);
                        CS.close();
                        msg.setText("");
                    }
                }catch (Exception exp){
                    exp.printStackTrace();
                }
            }
        });


        //Send File to Peers
        sendFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               try{
                   String path= new File("").getAbsolutePath();
                   JFileChooser fc=new JFileChooser();
                   FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                   fc.setFileFilter(filter);
                   fc.setCurrentDirectory(new File(""));
                   int result=fc.showOpenDialog(new JFrame());
                   String selectedFile="";
                   if (result == JFileChooser.APPROVE_OPTION) {
                       selectedFile = fc.getSelectedFile().getAbsolutePath();

                   }


                   //send to public chat
                   if (true){
                       if(publicChatRadioButton.isSelected()){


                           DatagramSocket CS = new DatagramSocket();
                           InetAddress Adress = InetAddress.getByName("localhost");
                           byte[] sendData = new byte[4096];
                           String s= new String("S&e#n@d*F_@!");


                           File myObj = new File(selectedFile);
                           System.out.println(selectedFile);

                           Scanner myReader = new Scanner(myObj);
                           s+=myObj.getName()+"_@!";

                           while (myReader.hasNextLine()) {
                               String data = myReader.nextLine();
                               s+=data+"\n";

                           }
                           myReader.close();


                           for (users u:start.Online_users) {
                               if (u.getPort()!=start.me.getPort()){
                                   String s1="["+start.me.getName()+" to EveryOne]:_@! " +s;
                                   sendData = s1.getBytes();
                                   DatagramPacket SendPacket = new DatagramPacket(sendData, sendData.length, Adress, u.getPort());
                                   CS.send(SendPacket);

                               }
                               else  if (u.getPort() == start.me.getPort()){


                               }
                           }
                           chat.setText(chat.getText()+"\n"+"[me to EveryOne]: "+fc.getSelectedFile().getName());
                           System.out.println("me port"+start.me.getPort());
//                       System.out.println(s);

                       }//send to private chat
                       else if (privateChatRadioButton.isSelected()){
                           DatagramSocket CS = new DatagramSocket();
                           InetAddress Adress = InetAddress.getByName("localhost");
                           byte[] sendData = new byte[4096];
                           String s= new String("S&e#n@d*F_@!");

                           File myObj = new File(selectedFile);
                           System.out.println(selectedFile);

                           Scanner myReader = new Scanner(myObj);
                           s+=myObj.getName()+"_@!";

                           while (myReader.hasNextLine()) {
                               String data = myReader.nextLine();
                               s+=data+"\n";
                           }
                           myReader.close();
                           s="["+start.me.getName()+" to me]: _@!"+s;
                           sendData = s.getBytes();
                           DatagramPacket SendPacket = new DatagramPacket(sendData, sendData.length, Adress, start.Online_users.get(peer.getSelectedIndex()).getPort());
                           CS.send(SendPacket);
                           CS.close();
                           chat.setText(chat.getText()+"\n"+"[me to "+start.Online_users.get(peer.getSelectedIndex()).getName()+"]: "+fc.getSelectedFile().getName());


                       }
                   }
               }catch (Exception exp){
                   exp.printStackTrace();
               }
            }
        });
    }
}
