package statki;
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
    int kolejnosc;


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
                    System.out.println("\tWystąpił błąd podczas usypiania wątku Klienta(Klasa Client): " + e);
                    continue;
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
                // Close the client connection
                cc.close();
                // Break out of the infinte while(true) loop
                break;
            }
            else{
                // Send the input to server
                cc.sendStringToServer(input);
            }
        }

    }




    public Client() {
        try {
            // We've got a socket which blocks until it connects
            Socket s = new Socket("localhost",5000);

            // this = reference to this Client object
            cc = new ClientConnection(s, this);

            // Starting the Thread object
            cc.start();

            // Podaj i wyślij swój login4
            System.out.println("Podaj swoją nazwę gracza:");
            nickname = console.nextLine();
            cc.sendStringToServer(nickname);

            //listenForInput();
        }
        /*catch(UnknownHostException e){
            System.out.println("\tWystąpił błąd przy tworzeniu Socket'u i wątku klienta(Klasa Client, UnknownHostException): " + e);
        }*/
        catch(IOException e) {
            System.out.println("\tWystąpił błąd przy tworzeniu Socket'u i wątku klienta(Klasa Client, IOException) " + e);
        }
    }



}
