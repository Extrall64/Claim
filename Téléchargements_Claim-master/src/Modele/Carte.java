package Modele;

import java.io.Serializable;

// l'attribut id utile pour voir quelle carte se manipule parmis les gobelins de poid identique 0
public class Carte implements Serializable, Comparable {
    private int poid, faction, categorie;
    private boolean estCachee[];
	public int id;

    public Carte(int faction, int poid, int id) {
        this.faction = faction;
        this.poid = poid;
        estCachee = new boolean[2];
        categorie = 0;
        estCachee[0] = true;
        estCachee[1] = true;
        this.id = id;
    }
    
    public Carte(int faction, int poid) {
        this.faction = faction;
        this.poid = poid;
        estCachee = new boolean[2];
        categorie = 0;
        estCachee[0] = true;
        estCachee[1] = true;
        id = 0;
    }

	@Override
	public int compareTo(Object o) {
		Carte c = (Carte) o;
		if ( faction < c.getFaction()) return -1;
		if ( faction == c.getFaction() && poid < c.getPoid()) return -1;
		return 1;
	}

	public boolean estEgale(Object o) {
		Carte c = (Carte) o;
	   	return c.getPoid() == poid && c.getFaction() == faction && c.id == id; 
	}
    public int hash() {
        int r = 17;
        r ^= poid << 17;
        r *= 830770091;   // 0xed5ad4bb
        r ^= faction << 11;
        r *= -1404298415; // 0xac4c1b51
        r ^= (1 + (id +faction + poid+ 1) * categorie) >> 15;
        r *= 830770091;   // 0x31848bab
        r ^= r << 14 + id;
        return r;
    }

    public String toString() {
        String str = "(faction:" + Integer.toString(faction) + ", poid:" + Integer.toString(poid) + ", categorie:" + Integer.toString(categorie) +  ", id: " + Integer.toString(id) + ")";
        return str;
    }

	public Carte clone() {
		Carte c = new Carte(faction, poid);
		c.categorie = categorie;
		c.estCachee[0] = estCachee[0];
		c.estCachee[1] = estCachee[1];
		c.id = id;
		return c;
	}


    public int getPoid() {
    	return poid;
    }
    
    public int getFaction() {
    	return faction;
    }
    
    public int getCategorie() {
    	return categorie;
    }
    
    public boolean estCachee(int j) {
    	return estCachee[j];
    }
    
    public void setCategorie(int cat) {
    	categorie = cat;
    }
    
    public void setVisible(int j) {
    	estCachee[j] = false;
    }
    
    public void setCachee(int j) {
    	estCachee[j] = true;
    }
    
    public void setEstCachee(int j,boolean b) {
    	estCachee[j] = b;
    }
}
