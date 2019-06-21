package klasy_serwera;

import java.io.DataInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* Ta klasa jest "nici�" ��cz�c� klienta z serwerem, po kt�rej mo�na przesy�a� dane. Umo�liwia
 * przesy�anie danych pomi�dzy serwerem a klientem/klientami - "ni�" PO STRONIE SERWERA*/
public class ServerConnection extends Thread{
    Socket socket;
    Server server;
    DataInputStream din;
    DataOutputStream dout;
    //ServerSocket ss;
    boolean shouldRun = true;
    boolean playing = false;

    public ServerConnection(Socket socket, Server server){
        // Name of the thread: "ServerConnectionThread" <-- CO ROBI super()?
        super("ServerConnectionThread");
        this.socket = socket;
        this.server = server;
    }

    public void sendStringToClient(String text){
        try{
            dout.writeUTF(text);
            // flush() makes sure all of the data is out
            // of the buffer space
            dout.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public String receiveMessage(){
        try{
            String textIn = din.readUTF();
            return textIn;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }

    }

    // Metoda, kt�ra wysy�a otrzyman� wiadomo�� do wszystkich pod��czonych klient�w
    public void sendStringToAllClients(String text){
        // Starting from 0 iterate through all server connections
        for (int index = 0; index< server.connections.size(); index++){
            ServerConnection sc = server.connections.get(index);
            sc.sendStringToClient(text);
        }
    }

    // Overriding the Thread run() method
    public void run(){
        try{
            // An object for receiving data from the socket
            din = new DataInputStream(socket.getInputStream());
            // An object for sending data to the client socket (?)
            dout = new DataOutputStream(socket.getOutputStream());

            while(shouldRun){
                while(!playing == true){
                    if(server.connections.size() == 2){
                        playing = true;
                        break;
                    }

                    /*while (din.available() == 0){
                        try{
                            Thread.sleep(1);
                        }
                        catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }*/

                    // Read the message that has been sent by a client to the server..
                    //String textIn = din.readUTF();



                }
                /*while (din.available() == 0){
                    try{
                        Thread.sleep(1);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }

                }*/

                /*// Read the message that has been sent by a client to the server..
                String textIn = din.readUTF();
                // ..and send it to all connected clients
                sendStringToAllClients(textIn);*/

                if(playing == true){
                    server.connections.get(0).sendStringToClient("\nNapisz wiadomo��: ");
                    String message = server.connections.get(0).receiveMessage();
                    server.connections.get(1).sendStringToClient("Odpowied�: " + message);
                    server.connections.get(0).sendStringToClient("Czekaj na odpowied� od " + server.connections.get(1) +"...");

                    server.connections.get(1).sendStringToClient("\nNapisz wiadomo�� do: ");
                    message = server.connections.get(1).receiveMessage();
                    server.connections.get(0).sendStringToClient("Odpowied�: " + message);
                    server.connections.get(1).sendStringToClient("Czekaj na odpowied� od " + server.connections.get(0) +"...");
                }


            }

            din.close();
            dout.close();
            socket.close();

        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}

