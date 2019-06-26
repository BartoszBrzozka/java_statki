import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataInputStream;

/* Ta klasa jest "nicią" łączącą klienta z serwerem, po której można przesyłać dane. Umożliwia
 * przesyłanie danych pomiędzy serwerem a klientem/klientami - "nić" PO STRONIE SERWERA*/
public class ServerConnection extends Thread{
    Socket socket;
    Server server;
    DataInputStream din;
    DataOutputStream dout;
    //ServerSocket ss;
    boolean shouldRun = true;
    boolean playing = false;
    String message;

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
            System.out.println("\tWystąpił błąd przy wysyłaniu danych(Klasa ServerConnection): " + e);
            System.out.println("Zamykanie połączenia...");
            try{
                din.close();
                dout.close();
                socket.close();
            }catch(Exception e2){
                System.out.println("\tWystąpił błąd przy zamykaniu obiektów DataInputStream, DataOutputStream i Socket (Klasa ServerConnection): " + e2);
            }
        }
    }


    public String receiveMessage(){
        try{
            String textIn = din.readUTF();
            return textIn;
        }catch(IOException e){
            System.out.println("\tWystąpił błąd przy otrzymywaniu danych(Klasa ServerConnection): " + e);
            return null;
        }

    }

    // Metoda, która wysyła otrzymaną wiadomość do wszystkich podłączonych klientów
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

                    while (din.available() == 0){
                        try{
                            Thread.sleep(1);
                        }
                        catch(InterruptedException e){
                            System.out.println("\tWystąpił błąd przy usypianiu wątku(Klasa ServerConnection): " + e);
                        }
                    }

                    // Read the message that has been sent by a client to the server..
                    //String textIn = din.readUTF();



                }


                // Utrzymuj diałog pomiędzy klientami, dopóki obaj są podłączeni
                if (server.connections.size() == 2){
                    try{
                        // Spróbuj wysłać informację do pierwszego gracza, żeby napisął wiadomość
                        // do drugiego gracza
                        server.connections.get(0).sendStringToClient("\nNapisz wiadomość: ");
                        message = server.connections.get(0).receiveMessage();
                    }catch(Exception e){
                        System.out.println("Wystąpił błąd przy wysyłaniu/odbieraniu danych od pierwszego klienta(Klasa ServerConnection): " + e);
                        server.connections.remove(0);
                        server.names.remove(0);
                        playing = false;
                        System.out.println("Usunięto pierwszego gracza z listy połączeń");
                    }

                    try{
                        /*// Jeśli użytkownik wpisze quit, to ma zamknąć serwer <-- NIESPRAWDZONE
                        if(message.toLowerCase().equals("quit")){
                            // Powiadom drugiego gracza o chęci zakończenia gry
                            server.connections.get(1).sendStringToClient("Gracz " + server.names.get(0) + " chce zakończyć grę.");
                            if (ss != null && !ss.isClosed()) {
                                try {
                                    ss.close();
                                } catch (IOException e)
                                {
                                    System.out.println((System.err));
                                }
                            }
                        }
                        else{
                            connections.get(0).sendStringToClient("Odpowiedź: " + message);
                            System.out.println("Czekaj na odpowiedź od " + names.get(1) +"...");

                            connections.get(0).sendStringToClient("\nNapisz wiadomość: ");
                            message = connections.get(0).receiveMessage();
                            System.out.println("Odpowiedź: " + message);
                            connections.get(0).sendStringToClient("Czekaj na odpowiedź od " + names.get(0) +"...");
                        }*/
                        // Spróbuj wysłać wiadomość od pierwszego gracza do drugiego
                        server.connections.get(1).sendStringToClient("Odpowiedź: " + message);
                    }catch(Exception e){
                        System.out.println("Wystąpił błąd przy wysyłaniu danych do drugiego klienta(Klasa ServerConnection): " + e);
                        server.connections.remove(1);
                        server.names.remove(1);
                        playing = false;
                        System.out.println("Usunięto drugiego gracza z listy połączeń");
                    }

                    try{
                        // Spróbuj wysłać wiadomość do pierwszego gracza, żeby czekał na odpowiedź
                        server.connections.get(0).sendStringToClient("Czekaj na odpowiedź od " + server.names.get(1) +"...");
                    }catch(Exception e){
                        System.out.println("Wystąpił błąd przy wysyłaniu danych do pierwszego klienta(Klasa ServerConnection): " + e);
                        server.connections.remove(0);
                        server.names.remove(0);
                        playing = false;
                        System.out.println("Usunięto pierwszego gracza z listy połączeń");
                    }

                    try{
                        // Spróbuj wysłać informację do drugiego gracza, żeby wysłał wiadomość
                        // do pierwszego gracza
                        server.connections.get(1).sendStringToClient("\nNapisz wiadomość: ");
                        message = server.connections.get(1).receiveMessage();
                    }catch(Exception e){
                        System.out.println("Wystąpił błąd przy wysyłaniu/odbieraniu danych od drugiego klienta(Klasa ServerConnection): " + e);
                        server.connections.remove(1);
                        server.names.remove(1);
                        playing = false;
                        System.out.println("Usunięto drugiego gracza z listy połączeń");
                    }

                    try{
                        // Spróbuj wysłać odpowiedź do pierwszego gracza
                        server.connections.get(0).sendStringToClient("Odpowiedź: " + message);
                    }catch(Exception e){
                        System.out.println("Wystąpił błąd przy wysyłaniu danych do pierwszego klienta(Klasa ServerConnection): " + e);
                        server.connections.remove(0);
                        server.names.remove(0);
                        playing = false;
                        System.out.println("Usunięto pierwszego gracza z listy połączeń");
                    }

                    try{
                        // Spróbuj wysłać drugiemu graczowi informację, żeby czekał na odpowiedź
                        server.connections.get(1).sendStringToClient("Czekaj na odpowiedź od " + server.names.get(0) +"...");
                    }catch(Exception e){
                        System.out.println("Wystąpił błąd przy wysyłaniu danych do drugiego klienta(Klasa ServerConnection): " + e);
                        server.connections.remove(1);
                        server.names.remove(1);
                        playing = false;
                        System.out.println("Usunięto drugiego gracza z listy połączeń");
                    }

                }
                else{
                    if(server.connections.size() == 1){
                        System.out.println("server.connections: " + server.connections);
                        // Jeśli któryś się odłączył, poinformuj o tym drugiego
                        server.connections.get(0).sendStringToClient("Oczekiwanie na ponowne podłączenie się przeciwnika do serwera...");
                        break;
                    }
                    else if(server.connections.size() == 0){
                        System.out.println("server.connections: " + server.connections);
                    }

                }


            }

            din.close();
            dout.close();
            socket.close();

        }
        catch(IOException e){
            System.out.println("\tWystąpił błąd przy tworzeniu i zamykaniu obiektów DataInputStream, DataOutputStream i Socket(Klasa ServerConnection): " + e);
            server.connections.remove(this);
            System.out.println("Usunięto nawiązane połączenie.");
        }

    }
}