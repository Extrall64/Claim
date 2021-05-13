package Modele;

public class Carte {
    public static final int GLOBELINS = 1;
    public static final int NAINS = 2;
    public static final int MORTSVIVANTS = 3;
    public static final int DOPPELGANGERS = 4;
    public static final int CHEVALIERS = 5;

    public int poid, faction, categorie;
    public boolean estCachee;
    public Carte(int faction, int poid) {
        this.faction = faction;
        this.poid = poid;
        categorie = 0;
    }
    public void definirCategorie(int cat) {
        categorie = cat;
    }
}