package statki;

public class Pole
{
 private boolean statek;
 private boolean trafione;
 private boolean zakryte;
 
 public Pole()
 {
   statek = false;
   trafione = false;
   zakryte = false;
 }

 public boolean trafione()
 {
   return trafione;
 }


 public boolean zakryte() {
	return zakryte;
}


public boolean statek()
 {
   return statek;
 }
public void setZakryte() {
	zakryte = true;
}
public void odkryj() {
	zakryte = false;
}

 public void setTrafione()
 {
   trafione = true;
   zakryte = false;
 }


 public void setStatek()
 {
   statek = true;
   zakryte = true;
 }


 public void set(boolean statek, boolean trafione, boolean zakryte)
 {
   this.statek = statek;
   this.trafione = trafione;
   this.zakryte = zakryte;
 }

}
