package klasy_serwera;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import java.rmi.UnknownHostException;
import java.util.ArrayList;

public class Server {

    ServerSocket ss;
    // Lista przechowuj¹ca obiekty typu ServerConnection
    ArrayList<ServerConnection> connections = new ArrayList<ServerConnection>();
    boolean shouldRun = true;
    ArrayList<String> names = new ArrayList<String>();
    int numberOfClients = 0;
    boolean reachedMaxNumberOfClients = false;

    public Server() {

        try {
            // Create a socket that opens a server on port 5000
            ss = new ServerSocket(6000);

            while(shouldRun){
                while(connections.size() < 2){
                    System.out.println("Awaiting connection...");
                    // The server listens for new connections and accepts
                    // a client (an object of type Socket)
                    Socket s = ss.accept();
                    System.out.println("Connected " + s);

                    // After detecting a client socket, it passes it
                    // into a new ServerCnnection object along with
                    // a reference to itself
                    ServerConnection sc = new ServerConnection(s, this);

                    // We're starting the Thread object, i.e. sc
                    sc.start();

                    // Odbierz nazwê login pod³¹czonego klienta


                    // We're adding this server connection to the list
                    // of server connections
                    connections.add(sc);
                    System.out.println("connections.size(): " + connections.size());

                    // Chcia³abym, ¿eby wyœwietla³o, który gracz siê pod³¹czy³,
                    // ale nie umiem siê dostaæ do zmiennej nickname Client'a
                    if(connections.size() == 1){
                        try{
                            connections.get(0).sendStringToClient("Pod³¹czy³eœ siê do serwera: " + this);
                        }catch(NullPointerException e){
                            // Try to reconnect
                            System.out.println(e);
                            //e.printStackTrace();
                            connections.remove(0);
                            System.out.println("connections.size() after removing object 0: " + connections.size());
                            break;
                        }
                    }
                }





                if(connections.size() == 2){//Nie wiem, dlaczego, ale  gdy dajê tu warunek (connections.size() >1), to nie dzia³a..
                    try{
                        numberOfClients ++;
                        System.out.println("numberOfClients: " + numberOfClients);
                        connections.get(1).sendStringToClient("Pod³¹czy³eœ siê do serwera: " + this);// <-- ..to

                        for(int i = 0; i < 2; i++){// Nie dzia³a, jesli dam warunek i < conncections.size()
                            connections.get(i).sendStringToClient("Gracz " + connections.get(1) + " siê pod³¹czy³");
                        }
                    }catch(NullPointerException e){
                        e.printStackTrace();
                        connections.remove(1);
                        System.out.println("connections.size() after removing object 1: " + connections.size());
                        //break;
                    }


                }

                System.out.println("connections: " + connections);

                // Jeœli mamy pod³¹czonych dwóch klientów, to osi¹gnêliœmy
                // maksymaln¹ liczbê klientów
                if (connections.size() == 2){
                    reachedMaxNumberOfClients = true;
                    for(int i = 0; i < 2; i++){//i < (connections.size()-1) nie dzia³a
                        connections.get(i).sendStringToClient("Wszyscy gracze siê ju¿ pod³¹czyli.");
                        connections.get(i).sendStringToClient("Lista graczy: " + connections);
                    }
                }

                while(reachedMaxNumberOfClients){

                }
            }



        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Server();
    }





}

