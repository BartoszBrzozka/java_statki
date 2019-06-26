package statki;
import java.io.*;
import java.net.Socket;

/* Ta klasa jest "nicią" łączącą klienta z serwerem, po której można przesyłać dane. Umożliwia
 * przesyłanie danych pomiędzy serwerem a klientem/klientami - "nić" PO STRONIE KLIENTA*/
public class ClientConnection extends Thread{
    // Declaration of global variables
    Socket s;
    Client c;
    // An object for receiving incoming data
    DataInputStream din;
    // An object for sending out data
    DataOutputStream dout;

    //Scanner console = new Scanner(System.in);
    boolean kolej;
    volatile boolean shouldRun = true;


    public ClientConnection(Socket socket, Client client){
        this.s = socket;
        this.c = client;
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
            System.out.println("WystÄ…piĹ‚ bĹ‚Ä…d przy wysyĹ‚aniu danych (klasa Clientconnection):\n\t" + e);
            close();
        }

    }

    public void run(){
        try{
            // We're creating an object to get data from
            // the socket
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            while(shouldRun == true){
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
                            System.out.println("WystÄ…piĹ‚ bĹ‚Ä…d przy usypianiu wÄ…tku (klasa ClientConnection):\n\t" + e);
                            break;
                        }
                    }

                    // Receive a message from server and read it
                    // from the UTF language

                    String reply = din.readUTF();
                    System.out.println(reply);
                    if(reply.equals("pierwszy")  || reply.equals("drugi")){
                        Server server = new Server();


                        if(reply.equals("pierwszy")){
                            c.kolejnosc = 0;

                            //System.out.println("Kolejnoœæ: " + c.kolejnosc);

                            server.pl.rysujPlansze(server.plansza1);
                            server.g1.ustawStatek(server.pole1, server.pl1, server.plansza1, server.g2);
                            server.pl.rysujPlansze(server.plansza1);
                            FileOutputStream ostream = null;
                            //---------------------zapis planszy i pola---------------//
                            try {
                                ostream = new FileOutputStream("planszacl1.sav");
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ObjectOutputStream p = null;
                            try {
                                p = new ObjectOutputStream(ostream);
                                p.writeObject(server.plansza1);
                                p.flush();
                                ostream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            FileOutputStream xstream = null;
                            try {
                                xstream = new FileOutputStream("polecl1.sav");
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ObjectOutputStream x = null;
                            try {
                                x = new ObjectOutputStream(xstream);
                                x.writeObject(server.pole1);
                                p.flush();
                                ostream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //--------------------------------------------------------//
                            server.pl1.rysujPlansze(server.plansza);
//                            server.graczPierwszyRozstawilStatki = true;
                            server.zmienNaTruegraczPierwszyRozstawilStatki();
                            server.kolejnosc = true;


                            while(server.graczPierwszyRozstawilStatki == false || server.graczDrugiRozstawilStatki == false){
                                System.out.println("graczPierwszyRozstawilStatki: " + server.graczPierwszyRozstawilStatki);
                                System.out.println("graczDrugiRozstawilStatki: " + server.graczDrugiRozstawilStatki);
                            }

                            System.out.println("graczPierwszyRozstawilStatki: " + server.graczPierwszyRozstawilStatki);
                            System.out.println("graczDrugiRozstawilStatki: " + server.graczDrugiRozstawilStatki);

                            if(server.kolejnosc = true) {
                                do {
                                    //-------------wczytywanie planszy przeciwnika---------//
                                    FileInputStream istream = null;
                                    String [][] planszax = null;
                                    try {
                                        istream = new FileInputStream("planszacl2.sav");
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    ObjectInputStream q;
                                    try {
                                        q = new ObjectInputStream(istream);
                                        try {
                                            planszax = (String [][])q.readObject();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                        istream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    FileInputStream zstream = null;
                                    Pole [][] polex = null;
                                    try {
                                        zstream = new FileInputStream("polecl2.sav");
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    ObjectInputStream z;
                                    try {
                                        z = new ObjectInputStream(zstream);
                                        try {
                                            polex = (Pole [][])z.readObject();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                        zstream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //-----------------------------------------------------------//
                                    server.pl1.sprawdzPlansze(planszax, polex, server.g2);
                                    server.pl1.rysujPlansze(planszax);
                                    server.graczPierwszyRozstawilStatki = true;

                                    // Strzela gracz 1
                                    server.g1.strzelaj(polex, server.pl, planszax, server.g2);

                                    server.zatopioneG1 = true;
                                    for (Statek statek : server.g1.statki) {  //sprawdzenie czy wszystkie statki gracza sa zatopione
                                        server.zatopioneG1 = server.zatopioneG1 && statek.jestZatopiony();   // jjesli wszystkie statki zatopione to zatopioneG1 = false
                                    }
                                    if (server.zatopioneG1 || server.zatopioneG2) break;
                                    server.kolejnosc = false;

                                }
                                while (!server.zatopioneG1 || !server.zatopioneG2);

                            }
                            else {
                                System.out.println("Poczekaj na swoją kolej");
                                try {
                                    Thread.sleep(2000000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        else{
                            c.kolejnosc = 1;
                            //System.out.println("Kolejnoœæ: " + c.kolejnosc);

                            server.pl1.rysujPlansze(server.plansza);
                            server.g2.ustawStatek(server.pole, server.pl, server.plansza, server.g1);

                            server.pl1.rysujPlansze(server.plansza);

                            //---------------------zapis planszy i pola---------------//
                            FileOutputStream ostream = null;
                            try {
                                ostream = new FileOutputStream("planszacl2.sav");
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ObjectOutputStream p = null;
                            try {
                                p = new ObjectOutputStream(ostream);
                                p.writeObject(server.plansza1);
                                p.flush();
                                ostream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            FileOutputStream xstream = null;
                            try {
                                xstream = new FileOutputStream("polecl2.sav");
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            ObjectOutputStream x = null;
                            try {
                                x = new ObjectOutputStream(xstream);
                                x.writeObject(server.pole1);
                                p.flush();
                                ostream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            server.graczDrugiRozstawilStatki = true;
                            server.zmienNaTruegraczDrugiRozstawilStatki();

                            //----------------------------------------------------//
                            while(server.graczPierwszyRozstawilStatki != true || server.graczDrugiRozstawilStatki != true){
                                System.out.println("graczPierwszyRozstawilStatki: " + server.graczPierwszyRozstawilStatki);
                                System.out.println("graczDrugiRozstawilStatki: " + server.graczDrugiRozstawilStatki);
                            }

                            do {
                                //-------------wczytywanie planszy przeciwnika---------//
                                FileInputStream istream = null;
                                String [][] planszax = null;
                                try {
                                    istream = new FileInputStream("planszacl1.sav");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                ObjectInputStream q;
                                try {
                                    q = new ObjectInputStream(istream);
                                    try {
                                        planszax = (String [][])q.readObject();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }

                                    istream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                FileInputStream zstream = null;
                                Pole [][] polex = null;
                                try {
                                    zstream = new FileInputStream("polecl1.sav");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                ObjectInputStream z;
                                try {
                                    z = new ObjectInputStream(zstream);
                                    try {
                                        polex = (Pole [][])z.readObject();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }

                                    zstream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //--------------------------------------------------//
                                server.pl.sprawdzPlansze(planszax, polex, server.g2);
                                server.pl.rysujPlansze(planszax);

                                // Strzela gracz 2
                                server.g2.strzelaj(server.pole1, server.pl1, server.plansza1, server.g1);

                                server.zatopioneG2 = true;
                                for (Statek statek : server.g2.statki) {
                                    server.zatopioneG2 = server.zatopioneG2 && statek.jestZatopiony();
                                }
                                if (server.zatopioneG1 || server.zatopioneG2) break;  //dopoki ktorys z graczy nie stracil wszystkich statkow
                            }

                            while (!server.zatopioneG1 || !server.zatopioneG2);
                            server.kolejnosc = true;


                           /* }
                            else {
                            	System.out.println("Poczekaj na swoją kolej");
                            	try {
									Thread.sleep(200000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
                            }*/
                        }

                        System.out.println("\nKoniec gry\n");
                        String winner ="";


                        if (server.g1.liczbaPunktow > server.g2.liczbaPunktow) {
                            winner = server.g1.getID();
                        }
                        else if (server.g1.liczbaPunktow < server.g2.liczbaPunktow) {
                            winner = server.g2.getID();
                        }
                        else if (server.g1.liczbaPunktow == server.g2.liczbaPunktow) {
                            winner = server.g1.getID() + " oraz " + server.g2.getID();
                            System.out.println("Mamy remis!\n");
                        }

                        System.out.println("Zwycięzcą zostaje: " + winner);
                        System.out.println("\nBrawo!");
                    }


                    /*// Receive a message from server and read it
                    // from the UTF language
                    reply = din.readUTF();

                    // Print the message so that the user can see it
                    System.out.println(reply);*/


                } catch(IOException e){
                    System.out.println("WystÄ…piĹ‚ bĹ‚Ä…d przy odbieraniu danych (klasa ClientConnection):\n\t" + e);
                    System.out.println("ZamkniÄ™to poĹ‚Ä…czenie z serwerem.");
                    close();
                }



            }
        }catch(IOException e){
            System.out.println("WystÄ…piĹ‚ bĹ‚Ä…d uruchamianiu metody run() (klasa ClientConnection):\n\t" + e);
            close();
        }



    }

    public void close(){
        shouldRun = false;
        try {
            din.close();
            dout.close();
            s.close();
        } catch(IOException e) {
            System.out.println("\tWystÄ…piĹ‚ bĹ‚Ä…d przy zamykaniu obiektĂłw DataInputStream, DataOutputStream i Socket(Klasa ClientConnection): " + e);
        }
    }
}