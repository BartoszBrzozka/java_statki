import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    final int WYMIAR = 10; // wymiar planszy niezbedny do wywo�ania obiektu
    String [][] plansza = new String[WYMIAR][WYMIAR]; // tablica stringow niezbedna do rysowania planszy - mo�e bedzie mo�na to ominac przy interfejsie
    String [][] plansza1 = new String[WYMIAR][WYMIAR]; //j/w
    Pole[][] pole = new Pole[WYMIAR][WYMIAR]; // tablica obiektow typu pole - niezbedna do wywo�ywania reakcji na planszy
    Pole[][] pole1 = new Pole[WYMIAR][WYMIAR];
    Plansza pl = new Plansza(plansza, pole); // obiekt typu plansza - argumenty rozne dla r�znych plansz!!!
    Plansza pl1 = new Plansza(plansza1, pole1);
    Gracz g1 =  new Gracz("gracz1");
    Gracz g2 = new Gracz ("gracz2");
    boolean zatopioneG1; // slabe nazewnictwo zatopione = true choc nie sa zatopione ale dziala wiec whatever
    boolean zatopioneG2;
    boolean kolejnosc;
    boolean kolej;
    ServerSocket ss;
    // Lista przechowująca obiekty typu ServerConnection
    ArrayList<ServerConnection> connections = new ArrayList<ServerConnection>();
    boolean shouldRun = true;
    ArrayList<String> names = new ArrayList<String>();
    int runda = 0;
    boolean reachedMaxNumberOfClients = false;
    volatile boolean graczPierwszyRozstawilStatki;
    volatile boolean graczDrugiRozstawilStatki;
    String jakoObiekt;

    public boolean zmienNaTruegraczPierwszyRozstawilStatki(){
        return graczPierwszyRozstawilStatki = true;
    }

    public boolean zmienNaTruegraczDrugiRozstawilStatki(){
        return graczDrugiRozstawilStatki = true;
    }

    public void listenForClients(){

    }


    public Server() {

        try {
            // Create a socket that opens a server on port 5000
            ss = new ServerSocket(5000);

            while(shouldRun){
                // Continue accepting until the number of clients is 2
                while(connections.size() < 2){
                    try{
                        System.out.println("Oczekiwanie na połączenie...");
                        // The server listens for new connections and accepts
                        // a client (an object of type Socket)
                        Socket s = ss.accept();

                        // After detecting a client socket, it passes it
                        // into a new ServerCnnection object along with
                        // a reference to itself
                        ServerConnection sc = new ServerConnection(s, this);

                        // We're starting the Thread object, i.e. sc
                        sc.start();



                        // We're adding this server connection to the list
                        // of server connections
                        connections.add(sc);
                        System.out.println("connections.size(): " + connections.size());

                        // Chciałabym, żeby wyświetlało, który gracz się podłączył,
                        // ale nie umiem się dostać do zmiennej nickname Client'a
                        if(connections.size() == 1){
                            try{
                                Thread.sleep(200);
                                // Odbierz nazwę login podłączonego klienta
                                String name = connections.get(0).receiveMessage();
                                names.add(name);
                                connections.get(0).sendStringToClient("Podłączyłeś się do serwera: " + this);
                            }catch(Exception e){
                                System.out.println("\tWystąpił błąd przy wysyłaniu informacji do pierwszego Klienta(Klasa Server): " + e);
                                connections.remove(0);
                                System.out.println("Ups, nie udało się podłączyć gracza.");
                            }
                        }

                        else if(connections.size() == 2){//Nie wiem, dlaczego, ale  gdy daję tu warunek (connections.size() >1), to nie działa..
                            runda ++;
                            System.out.println("Runda: " + runda);
                            try{
                                // Odbierz nazwę login podłączonego klienta
                                Thread.sleep(200);
                                String name = connections.get(1).receiveMessage();
                                names.add(name);
                                connections.get(1).sendStringToClient("Podłączyłeś się do serwera: " + this);// <-- ..to


                            }catch(Exception e){
                                System.out.println("\tWystąpił błąd przy wysyłaniu informacji do drugiego Klienta(Klasa Server): " + e);
                                connections.remove(1);
                                System.out.println("Ups, nie udało się podłączyć drugiego gracza.");
                            }

                            try{
                                if(connections.size() == 2 && names.size() == 2){
                                    connections.get(0).sendStringToClient("Gracz " + names.get(1) + " (socket: " + connections.get(1) + ") się podłączył");
                                }
                                else{
                                    connections.get(0).sendStringToClient("Jeszcze nie udało się podłączyć drugiego gracza.");
                                }

                            }catch(Exception e){
                                System.out.println("\tWystąpił błąd przy wysyłaniu informacji o drugim Kliencie do pierwszego Klienta(Klasa Server): " + e);
                                connections.remove(0);
                                System.out.println("Ups, straciliśmy pierwszego gracza.");
                            }
                        }

                        System.out.println("connections: " + connections + "\n");

                        // Jeśli mamy podłączonych dwóch klientów, to osiągnęliśmy
                        // maksymalną liczbę klientów
                        if (connections.size() == 2){

                            for(int i = 0; i < 2; i++){//i < (connections.size()-1) nie działa
                                connections.get(i).sendStringToClient("\nWszyscy gracze się już podłączyli.");
                                connections.get(i).sendStringToClient("Lista graczy: " + names + "\n");
                            }
                            // Poinformuj o kolejności
                            connections.get(0).sendStringToClient("\nTwoja kolejność:");
                            connections.get(0).sendStringToClient("pierwszy");

                            connections.get(1).sendStringToClient("\nTwoja kolejność:");
                            connections.get(1).sendStringToClient("drugi");

                        }
                    }
                    catch(IOException e){
                        System.out.println("Wystąpił błąd przy łączeniu się z klientami(Klasa Server): " + e + "\n");
                        continue;
                    }

                }
            }

            System.out.println("Podłączono dwóch graczy: " + connections);



        }
        catch(IOException e) {
            System.out.println("\tWystąpił błąd przy tworzeniu obiektu ServerSocket(Klasa Server): " + e);
            System.out.println("Połączenie zostanie zresetowane.");
            connections.clear();
            System.out.println("Oczyszczono listę nawiązanych połączeń.");
        }

    }

    public static void main(String[] args) {
        //--------------------------------------------- INTERFEJS KRZYSKA --------------------------------------------
        System.out.println("Autorzy ponizszego programu:      |"
                //+ "\n__________________________________"
                + "\n                                  |   "
                + "\nJoyce Opio (JoyceDOpio)           "
                + "|\nKrzysztof Czerski (Sysu1996)      |"
                + "\nBartosz Brzozka (BartoszBrzozka)  |"
                + "\nMichał Skrzypczyński (justFlow5)  |"
                + "\n                                  |   "
                + "\n__________________________________");

        System.out.println("\r\n" +
                "                                       # #  ( )\r\n" +
                "                                  ___#_#___|__\r\n" +
                "                              _  |____________|  _\r\n" +
                "                       _=====| | |            | | |==== _\r\n" +
                "                 =====| |.---------------------------. | |====\r\n" +
                "   <--------------------'   .  .  .  .  .  .  .  .   '--------------/\r\n" +
                "     \\                                                             /\r\n" +
                "      \\_______________________________________________WWS_________/\r\n" +
                "  wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww\r\n" +
                "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww\r\n" +
                "   wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");

        System.out.println("\n\n--------------------------------------------------------------------------");
        System.out.println("\n                       Witamy w grze Statki!\n");
        System.out.println("--------------------------------------------------------------------------\n");
        System.out.println("CEL GRY\n");
        //System.out.println("\n");
        System.out.println("Gra w statki to popularna gra towarzyska dla dwóch lub większej ilości osób.");
        System.out.println("Celem każdego z graczy jest zatopienie wszystkich okrętów swoich przeciwników!");
        System.out.println("Za jednorazowe spalenie jednego z masztów danego statku graczowi przyznawany jest 1 punkt.");
        System.out.println("Wygrywa ten, kto zdobędzie największą ilość punktów!");
        System.out.println("\n--------------------------------------------------------------------------\n");
        System.out.println("ZASADY ROZMIESZCZENIA STATKÓW NA PLANSZY\n");
        System.out.println("Każdy z graczy rozmieszcza na swojej planszy statki w dowolny sposób.");
        System.out.println("Statki można obracać i wyginać z zachowaniem zasad, że każdy maszt jednego statku musi stykać się z jego\r\n" +
                "kolejnym masztem ścianką boczną (nie może ��czy� się na ukos) oraz dwa statki nie mogą\r\n" +
                "dotykać się ani ukosem, ani żadnym bokiem masztu.");

        System.out.println("\n--------------------------------------------------------------------------");
        System.out.println("\nKażdy z graczy na początku gry otrzymuje do rozmieszczenia:\n");
        System.out.println("*jeden pięciomasztowiec,\n*jeden czteromasztowiec,\n*dwa trójmasztowce,\n*dwa dwumasztowce,\n*dwa jednomasztowce,\n*dwa jednomasztowce. \n");
        System.out.println("\n--------------------------------------------------------------------------\n");
        System.out.println("                     Za chwilę zacznie się wielka bitwa!");
        System.out.println("                            Niech wygra najlepszy!\n");
        System.out.println("--------------------------------------------------------------------------\n\n\n");
/*
        // Tworzymy obiekt do sczytywania danych wprowadzonych z klawiatury
        Scanner console = new Scanner(System.in);

            //------------------------ TEST MICHALA ----------------------------------


            // ---------------------MOJA WSTAWKA SERWERA I KLIENTA -------------------
            Server server = new Server();

            // Stwórz objekt gracza rozpoczynającego
            Client gracz = new Client();

            /// Określ liczbę graczy
            System.out.println("Określi liczbę graczy: ");
            int liczbaGraczy = console.nextInt();

            // Stwórz obiekt serwera
            Server server = new Server(liczbaGraczy);


            // -------------------- RESZTA TESTU MIACHALA ----------------------
            Gracz g1 = new Gracz("michal");
            Gracz g2 = new Gracz("dummyAI");
            pl.rysujPlansze(plansza1);
            g1.ustawStatek(pole1, pl1, plansza1, g2);
            pl1.rysujPlansze(plansza);
            g2.ustawStatek(pole, pl, plansza, g1);


            boolean zatopioneG1; // slabe nazewnictwo zatopione = true choc nie sa zatopione ale dziala wiec whatever
            boolean zatopioneG2;

            do {
                zatopioneG1 = true;
                for (Statek statek : g1.statki) {  //sprawdzenie czy wszystkie statki gracza sa zatopione
                    zatopioneG1 = zatopioneG1 && statek.jestZatopiony();   // jjesli wszystkie statki zatopione to zatopioneG1 = false

                }
                zatopioneG2 = true;
                for (Statek statek : g2.statki) {
                    zatopioneG2 = zatopioneG2 && statek.jestZatopiony();
                }

                pl1.sprawdzPlansze(plansza, pole, g2);
                pl1.rysujPlansze(plansza);
                g1.strzelaj(pole, pl, plansza, g2);

                pl.sprawdzPlansze(plansza1, pole1, g2);
                pl.rysujPlansze(plansza1);
                g2.strzelaj(pole1, pl1, plansza1, g1);

            }
            while (!zatopioneG1 || !zatopioneG1); //dopoki ktorys z graczy nie stracil wszystkich statkow

            System.out.println("Koniec gry!\nZwyciezca zostal: ");
            String winner = g1.liczbaPunktow > g2.liczbaPunktow ? "" + g1.getID() : "" + g2.getID();

            System.out.println(winner + "!\n Brawo!");

            */




        new Server();

    }

}