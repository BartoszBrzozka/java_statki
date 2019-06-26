package statki;

public class Plansza{
	public static final int WYMIAR = 10;
	public Plansza(String[][] plansza, Pole[][] pole) { // definicja planszy - domyœlnie zakrywa wszystkie pola
		for (int wiersz = 0; wiersz < WYMIAR; wiersz++) {
			for (int kolumna = 0; kolumna < WYMIAR; kolumna++) {
				plansza[wiersz][kolumna] = "~";
				pole[wiersz][kolumna] = new Pole();
				
			}
		}
		
	}
	public void rysujPlansze(String [][] plansza) { //rysuje pust¹ plansze zdefiniowan¹ funkcj¹ Plansza()
		System.out.print("     " + "A B C D E F G H I J");
		System.out.print("\n" + "     " + "-------------------");
		for (int wiersz = 0; wiersz < WYMIAR; wiersz++) {
			if (wiersz < 9) {
			System.out.print("\n" + (wiersz+1) + "  |");
			}
			else {
				System.out.print("\n" + (wiersz+1) + " |");
			}
			for (int kolumna = 0; kolumna < WYMIAR; kolumna++) {
				System.out.print(" " + plansza[wiersz][kolumna]);	
			}
		}
		System.out.print("\n\n");
	}
	public void sprawdzPlansze(String[][] plansza, Pole[][] pole, Gracz player) { //funkcja wywo³uj¹ca zmiany w planszy
		for (int wiersz = 0; wiersz < WYMIAR; wiersz++) {
			for (int kolumna = 0; kolumna < WYMIAR; kolumna++) {
				//pole[wiersz][kolumna].odkryj(); pozwala na wyœwietlenie zdefiniowanych statków
				if(pole[wiersz][kolumna].statek() && !pole[wiersz][kolumna].zakryte()){ // domyœlnie zawsze bedzie zakryte - pokazuje gdzie sa statki
					plansza[wiersz][kolumna] = "S";
				
				}
				if(pole[wiersz][kolumna].statek()  && pole[wiersz][kolumna].trafione()){ // pokazuje trafione statki - jeœli trafiony - poka¿e siê z automatu
					plansza[wiersz][kolumna] = "X";
				}
				if(!pole[wiersz][kolumna].statek() && pole[wiersz][kolumna].trafione()){ // pokazuje pud³a z automatu
					plansza[wiersz][kolumna] = "#";
				}
				if(pole[wiersz][kolumna].statek() && pole[wiersz][kolumna].zakryte()){ // domyœlnie zawsze bedzie zakryte - pokazuje gdzie sa statki
					plansza[wiersz][kolumna] = "~";
				}

			}
		}
		// oznacz jako zatopione
		 for (Statek statek : player.statki) {
			if (statek.jestZatopiony()) {
				for(Pole poleStatku : statek.zajmowanePola) {
					for (int i = 0; i < 10; i++) {
					    for (int j = 0; j < 10; j++) {
					    	if (poleStatku == pole[i][j]) {
					    		plansza[i][j] = "Z";
					    	}
					}
				}
			}
				//System.out.println("\nZatopiony!\n");	
		}

	} 
	}
		}


