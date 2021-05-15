package Modele;

public class Carte {
    public int poid, faction;

    public Carte(int faction, int poid) {
        this.faction = faction;
        this.poid = poid;
    }
    
    public String toString() {
        return ("(faction:" + Integer.toString(faction) + ", poid:" + Integer.toString(poid) + " )");
    }
}