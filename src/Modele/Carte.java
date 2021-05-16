package Modele;

import java.io.Serializable;

public class Carte implements Serializable {
    public int poid, faction;

    public Carte(int faction, int poid) {
        this.faction = faction;
        this.poid = poid;
    }
    
    public String toString() {
        return ("(faction:" + Integer.toString(faction) + ", poid:" + Integer.toString(poid) + " )");
    }
}