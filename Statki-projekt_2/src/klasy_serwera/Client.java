package klasy_serwera;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.Scanner;

public class Client {

    ClientConnection cc;
    String nickname;
    // The scanner will read the console input, so what the user types
    Scanner console = new Scanner(System.in);

    public static void main(String[] args) {
        new Client();
    }

    public void listenForInput() {

        // Infinte loop
        while(true) {
            // While the scanner doesn't receive a next line (i.e. the user hasn't
            // typed anything and pressed "Enter" afterwards) - so if there is no
            // text line - put the Thread to sleep
            while(!console.hasNextLine()) {
                try {
                    // Make the Thread sleep for a milisecond
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // So, if there is text from console, we break out of the
            // while(!console.hasNextLine()) loop and now we want to
            // read the input
            // console.NextLine() pulls in the line (with no "\n"
            // signs)
            String input = console.nextLine();

            // If user writes "quit" (no matter how he spells it:
            // uppercase, lowercase, mixed)
            if (input.toLowerCase().equals("quit")){
                // Break out of the infinte while(true) loop
                break;
            }

            // Send the input to server
            cc.sendStringToServer(input);

        }

        // Close the client connection
        cc.close();
    }



    public Client() {
        try {
            // We've got a socket which blocks until it connects
            Socket s = new Socket("localhost",6000);

            // this = reference to this Client object
            cc = new ClientConnection(s, this);

            // Starting the Thread object
            cc.start();

            // Podaj i wyœlij swój login



            listenForInput();
        }
        catch(UnknownHostException e){
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }



}
