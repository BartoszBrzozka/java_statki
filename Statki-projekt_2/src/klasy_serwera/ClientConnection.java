package klasy_serwera;

import java.io.IOException;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/* Ta klasa jest "nici¹" ³¹cz¹c¹ klienta z serwerem, po której mo¿na przesy³aæ dane. Umo¿liwia
 * przesy³anie danych pomiêdzy serwerem a klientem/klientami - "niæ" PO STRONIE KLIENTA*/
public class ClientConnection extends Thread{
    // Declaration of global variables
    Socket s;
    // An object for receiving incoming data
    DataInputStream din;
    // An object for sending out data
    DataOutputStream dout;
    boolean shouldRun = true;

    public ClientConnection(Socket socket, Client client){
        this.s = socket;
    }

    public void sendStringToServer(String text){
        // Try to send out the input in the UTF language as
        // dout
        try{
            // Write the input in the UTF language
            dout.writeUTF(text);
            // To make sure that all of our data is sent out
            dout.flush();
        }catch(IOException e){
            e.printStackTrace();
            close();
        }

    }

    public void run(){
        try{
            // We're creating an object to get data from
            // the socket
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(shouldRun){
                try{
                    // While there's nothing available there to read put the
                    // the Thread to sleep. If there's something to read,
                    // break out of the loop. That is, if there is a message
                    // from the server, break out of the loop.
                    while(din.available() == 0) {
                        try {
                            // Make the Thread sleep
                            Thread.sleep(1);
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }

                    // Receive a message from server and read it
                    // from the UTF language
                    String reply = din.readUTF();

                    // Print the message so that the user can see it
                    System.out.println("Przys³ana wiadomoœæ: " + reply);


                    // Receive a message from server and read it
                    // from the UTF language
                    reply = din.readUTF();

                    // Print the message so that the user can see it
                    System.out.println("Przys³ana wiadomoœæ: " + reply);




                } catch(IOException e){
                    e.printStackTrace();
                    close();
                }



            }
        }catch(IOException e){
            e.printStackTrace();
            close();
        }



    }

    public void close(){
        try {
            din.close();
            dout.close();
            s.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

