package Modele;

import java.io.Serializable;

public class Carte implements Serializable {
    private int poid, faction, categorie;
    private boolean estCachee[];

    public Carte(int faction, int poid) {
        this.faction = faction;
        this.poid = poid;
        estCachee = new boolean[2];
        categorie = 0;
        estCachee[0] = true;
        estCachee[1] = true;
    }

    public String toString() {
        String str = "(faction:" + Integer.toString(faction) + ", poid:" + Integer.toString(poid) + ", categorie:" + Integer.toString(categorie) +  ")";
        return str;
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