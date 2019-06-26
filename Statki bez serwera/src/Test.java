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
	}
}


