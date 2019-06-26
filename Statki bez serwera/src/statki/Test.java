package statki;

public class Test{

	public static void main(String[] args) {
		final int WYMIAR = 10; // wymiar planszy niezbedny do wywo³ania obiektu
		String [][] plansza = new String[WYMIAR][WYMIAR]; // tablica stringow niezbedna do rysowania planszy - mo¿e bedzie mo¿na to ominac przy interfejsie
		String [][] plansza1 = new String[WYMIAR][WYMIAR]; //j/w
		Pole[][] pole = new Pole[WYMIAR][WYMIAR]; // tablica obiektow typu pole - niezbedna do wywo³ywania reakcji na planszy
		Pole[][] pole1 = new Pole[WYMIAR][WYMIAR];
		Plansza pl = new Plansza(plansza, pole); // obiekt typu plansza - argumenty rozne dla róznych plansz!!!
		Plansza pl1 = new Plansza(plansza1, pole1);
		
         
		Gracz g1 = new Gracz("michal");
		Gracz g2 = new Gracz("dummyAI");

		
		
		
		System.out.println("Autorzy poni¿szego programu:      |"
				//+ "\n__________________________________"
				+ "\n                                  |   "
				+ "\nJoyce Opio (JoyceDOpio)           "
				+ "|\nKrzysztof Czerski (Sysu1996)      |"
				+ "\nBartosz Brzózka (BartoszBrzozka)  |"
				+ "\nMicha³ Skrzypczyñski (justFlow5)  |"
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
		System.out.println("Gra w statki to popularna gra towarzyska dla dwóch lub wiêkszej iloœci osób.");
		System.out.println("Celem ka¿dego z graczy jest zatopienie wszystkich okrêtów swoich przeciwników!");
		System.out.println("Za jednorazowe spalenie jednego z masztów danego statku graczowi przyznawany jest 1 punkt.");
		System.out.println("Wygrywa ten, kto zdobêdzie najwiêksz¹ iloœæ punktów!");
		System.out.println("\n--------------------------------------------------------------------------\n");
		System.out.println("ZASADY ROZMIESZCZENIA STATKÓW NA PLANSZY\n");
		System.out.println("Ka¿dy z graczy rozmieszcza na swojej planszy statki w donwolny sposób.");
		System.out.println("Statki mo¿na obracaæ i wyginaæ z zachowaniem zasad, ¿e ka¿dy maszt jednego statku musi stykaæ siê z jego\r\n" + 
				"kolejnym masztem œciank¹ boczn¹ (nie mo¿e ³¹czyæ siê na ukos) oraz dwa statki nie mog¹\r\n" + 
				"dotykaæ siê ani ukosem, ani ¿adnym bokiem masztu.");
		
		System.out.println("\n--------------------------------------------------------------------------");
		System.out.println("\nKa¿dy z graczy na pocz¹tku gry otrzymuje do rozmieszczenia:\n");
		System.out.println("*jeden piêciomasztowiec,\n*jeden czteromasztowiec,\n*dwa trójmasztowce,\n*dwa dwumasztowce,\n*dwa jednomasztowce,\n*dwa jednomasztowce. \n");
		System.out.println("\n--------------------------------------------------------------------------");
		System.out.println("\nLegenda mapy:");
		System.out.println("\n~ - zakryte pole, pod którym mo¿e znajdowaæ siê okrêt przeciwnika");
		System.out.println("\nS - aktualna lokalizacja Twojego okrêtu");
		System.out.println("\n# - miejsce, w które zosta³ oddany strza³, ale okaza³ siê by³ pud³em");
		System.out.println("\nX - maszt danego statku zosta³ zatopiony");
		System.out.println("\nZ - ca³y statek zosta³ zatopiony");
		System.out.println("\n--------------------------------------------------------------------------\n");
		System.out.println("                     Za chwilê zacznie siê wielka bitwa!");
		System.out.println("                            Niech wygra najlepszy!\n");
		System.out.println("--------------------------------------------------------------------------\n\n\n");
		
		pl.rysujPlansze(plansza1);
		g1.ustawStatek(pole1, pl1, plansza1, g2);
		pl1.rysujPlansze(plansza);
		g2.ustawStatek(pole, pl, plansza, g1);

		
		boolean zatopioneG1; // slabe nazewnictwo zatopione = true choc nie sa zatopione ale dziala wiec whatever
		boolean zatopioneG2;

		do {    	
            pl1.sprawdzPlansze(plansza, pole, g2);
            pl1.rysujPlansze(plansza);
			g1.strzelaj(pole, pl, plansza, g2);
			
            pl.sprawdzPlansze(plansza1, pole1, g2);
            pl.rysujPlansze(plansza1);
			g2.strzelaj(pole1, pl1, plansza1, g1);
			

			
			zatopioneG1 = true;
			for (Statek statek : g1.statki) {  //sprawdzenie czy wszystkie statki gracza sa zatopione
		    	zatopioneG1 = zatopioneG1 && statek.jestZatopiony();   // jjesli wszystkie statki zatopione to zatopioneG1 = false  
			}
			zatopioneG2 = true;
			for (Statek statek : g2.statki) { 
            	zatopioneG2 = zatopioneG2 && statek.jestZatopiony();
		    }

        if (zatopioneG1 || zatopioneG2) break;  //dopoki ktorys z graczy nie stracil wszystkich statkow
		}
		while (!zatopioneG1 || !zatopioneG2); 
		
		System.out.println("\nKoniec gry\n");
		String winner ="";
		
		
		if (g1.liczbaPunktow > g2.liczbaPunktow) {
			winner = g1.getID();
			}
		else if (g1.liczbaPunktow < g2.liczbaPunktow) {
			winner = g2.getID();
		}
		else if (g1.liczbaPunktow == g2.liczbaPunktow) {
			winner = g1.getID() + " oraz " + g2.getID();
			System.out.println("Mamy remis!\n");
		}
		
		System.out.println("Zwyciêzc¹ zostaje: " + winner);
		System.out.println("\nBrawo!");
	}
}


