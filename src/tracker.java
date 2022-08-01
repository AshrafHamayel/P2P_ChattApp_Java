import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.io.*;


public class tracker {

    public static void main(String[] args) throws Exception {
        System.out.println("Server Started");

        List<users> usersList = new ArrayList<>();

        DatagramSocket serverSocket = new DatagramSocket(9999);


        while (true){

            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            DatagramPacket Paket = new DatagramPacket(receiveData, receiveData.length);
            //Receive Data From peers
            serverSocket.receive(Paket);
            String Data = new String(Paket.getData());
            String response = new String("");
//            System.out.println("Data : "+Data);

            if(Data.trim().equals("$#EnD!@")){
                break;
            }

//            Send all active peers
            else if(Data.trim().equals("$#Send%All*^&")){
                String user = new String();
                for ( users u : usersList  ){
                    user +=(u.getName() +
                            "-"+u.getDnmae() +
                            "-" +u.getPort()+"#" );

                }

                InetAddress IPAddress = Paket.getAddress();
                sendData = user.getBytes();
                int port = Paket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);

            }
            //Delete a peer when closing
            else if(Data.trim().contains("!D@el$et%e_")){
                for ( int i = 0 ; i< usersList.size();i++ ){
                    if (usersList.get(i).getPort() == Integer.parseInt(Data.trim().split("_")[1])){
                        System.out.println("Delete "+usersList.get(i)+" Done ");
                        usersList.remove(i);
                        break;

                    }

                }
            }

            else {
                String split[] = Data.split(" ");
                users u = new users(split[0].trim(), split[1].trim(), Integer.parseInt(split[2].trim()));
                usersList.add(u);
            }









        }

        serverSocket.close();
    }


}

