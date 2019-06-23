package statki;
import java.util.ArrayList;

public class Statek {
	
	public int liczbaMasztow;
	private int pozostaloMasztow;
	private boolean zatopiony;
	
	ArrayList<Pole> zajmowanePola = new ArrayList<>();

	public Statek (int liczbaMasztow) {
		this.liczbaMasztow = liczbaMasztow;
		this.pozostaloMasztow = liczbaMasztow;
		this.zatopiony = false;
		
	}

	public boolean jestZatopiony()
	 {
	   return this.zatopiony;
	 }
	
	public boolean prawidlowoUstawiony(Pole[][] pole,  int statekWiersz, int statekKolumna) {
        //jesli namiary sa poza plansz¹ 
		if (statekWiersz < 0 || statekWiersz > 9 || statekKolumna < 0 || statekKolumna > 9) {
			System.out.print("\nNamiary ustawianego masztu wykraczaj¹ poza granice planszy.\nUstaw ponownie:");
			return false;
		}
		//jesli w ustawianym miejscu znajduje sie juz statek
        if (pole[statekWiersz][statekKolumna].statek()) {
        	System.out.print("\nW tym miejscu znajduje siê ju¿ maszt.\nUstaw ponownie:");
        	return false;
        }
		
		// jesli lokowany maszt znajduje sie w kontakcie z innym ustawionym juz statkiem
		boolean zadnegoStatku = true;
        int extend1a = 2;
        int extend1b = 2;
		for (int a = statekWiersz - 1; a < statekWiersz + extend1a; a++) {  
			for (int b = statekKolumna -1; b < statekKolumna + extend1b; b++) { 
				
				//by iteracja przeszla przez wszystkie mozliwe, dostepne opcje bez wpadania w nieskonczonosc 
				if(a < 0) a = 0;
				if(b < 0) b = 0;
				
				if(a >= 9) {
					a = 9;
					extend1a = 1;
				} else extend1a = 2;
				
	            if (b >= 9) {
	            	b = 9;
	            	extend1b = 1;
	            } else extend1b = 2;
					
	
				if (a == statekWiersz && b == statekKolumna) continue; // pomin sprawdzenie miejsce gdzie ma nastapic ulokowanie statku
				
				zadnegoStatku = zadnegoStatku && !pole[a][b].statek();
				if (!zadnegoStatku) {
					if (zajmowanePola.contains(pole[a][b])) {
						zadnegoStatku = true; //jesli to ten sam statek to sprawdzaj dalej
					} else { //inny statek w kontakcie
						System.out.print("\nInny statek w bezposrednim kontakcie!\nUstaw ponownie:");
						return false; 
					}
				}
			}
		}
		//gdy dany statek nie ma zadnego postawionego masztu
		if (zadnegoStatku && zajmowanePola.size() == 0) {
			zajmowanePola.add(pole[statekWiersz][statekKolumna]);
			return true;
			}
		
		/// PRZYLEGANIE
		int extend2k = 2;
        int extend2j = 2;
		for (int k = statekWiersz - 1; k < statekWiersz + extend2k; k++) {  

			for (int j = statekKolumna -1; j < statekKolumna + extend2j; j++) {

				if(k < 0) k = 0;
				if(j < 0) j = 0;
				
				
				if(k >= 9) {
					k = 9;
					extend2k = 1;
				} else extend2k = 2;
				
				
				if(j >= 9) {
					j = 9;
					extend2j = 1;
				} else extend2j = 2;
					
				if (k == statekWiersz && j == statekKolumna) continue; // miejsce gdzie ma nastapic ulokowanie statku
				
				boolean statekWzasiegu = pole[k][j].statek();  //czy jakis statek wystepuje w bezposrednim zasiegu omawianej lokalizacji
				
				if (statekWzasiegu) {
					if (zajmowanePola.contains(pole[k][j])) {  //jesli statek w zasiegu jest tym samym statkiem, co aktualnie ustawiany
				        boolean przylegaja = false;  //to sprawdz czy przylegaja do siebie pionowo/poziomo
				        for (int i = 0; i < zajmowanePola.size(); i++) {
				    	//czy przelegaja do siebie
				    	// gdy ktorys z warunkow zostanie spelniony chocby raz --> wyjdz z petli
					
					    przylegaja = przylegaja || (
					    		(k - 1 == statekWiersz && j == statekKolumna) ||
					    		(k + 1 == statekWiersz && j == statekKolumna) ||
					    		(k == statekWiersz && j - 1 == statekKolumna) ||
					    		(k == statekWiersz && j + 1 == statekKolumna)
					    		);
 
					    if (przylegaja) {
						    zajmowanePola.add(pole[statekWiersz][statekKolumna]);
						    return true;
						    }
					    
					    else { 
						    continue;
						    }
				        }
					}
				}

			}

		}
		//gdy ¿aden ze statkow w zasiegu nie przylega pionowo/poziomo
		System.out.println("\nMaszty musz¹ do siebie przylegaæ pionowo lub poziomo!\nUstaw ponownie:");
		return false;	
		}


	public boolean okreslCzyTrafionyZatopiony(Pole[][] pole,  int strzalX, int strzalY) {
		
		if (zajmowanePola.contains(pole[strzalX][strzalY]) && !jestZatopiony()) {
			pozostaloMasztow--;
			if (pozostaloMasztow == 0) {
				zatopiony = true;
				System.out.println("\nZatopiony!\n");
		}
			return true;
		}
		 else {
			return false;
		}
		
	}
}

