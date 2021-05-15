package Modele;

import java.util.Random;

import IA.IA;

public class Jeu {
	public static final int nbCarte = 52;
    
	public static final int nbFaction = 5;
    // les diffirentes valeur que l'attr faction de carte peut prendre
    public static final int GLOBELINS = 0;
    public static final int NAINS = 1;
    public static final int MORTSVIVANTS = 2;
    public static final int DOPPELGANGERS = 3;
    public static final int CHEVALIERS = 4;
    
    public static final int JoueurA = 0;
    public static final int JoueurB = 1;
    // les diffirentes valeur que l'attr categorie de carte peut prendre pour les sauvegardes
    public static final int nbPile = 11;
    public static final int Pioche = 0;
    public static final int ScoreA = 1;
    public static final int ScoreB = 2;
    public static final int Defausse = 3;
    public static final int MainA = 4;
    public static final int MainB = 5;
    public static final int PartisansA = 6;
    public static final int PartisansB = 7;
    public static final int CarteAGagner = 8;
    public static final int CarteJouerA = 9;
    public static final int CarteJouerB = 10;
    
    // les diffirentes valeur des modes de jeu
    public static final int HUMAIN_VS_HUMAIN = 1;
    public static final int HUMAIN_VS_IA = 2;
    public static final int IA_VS_IA = 3;
    public static final int HUMAIN_VS_HUMAIN_RESEAU = 4;
    
    
    Niveau niveau;
    int mode;
    boolean menu;
    IA[] joueurs;
    
    public Jeu() {
        niveau = new Niveau();
        menu = false;
        joueurs = new IA[2];
        lancerUnePartie(HUMAIN_VS_HUMAIN,null,null);
        niveau.initialiserPhase1();
        niveau.determinerJoueurCommence();
        niveau.retournerNouvelleCarteEnJeu();
    }
    
    public boolean estSurMenu() {
    	return menu;
    }
    
    public void lancerUnePartie(int m, IA jA, IA jB) {
    	mode = m;
    	joueurs[JoueurA] = jA;
    	joueurs[JoueurB] = jB;
    	niveau.initialiser();
    }
 
    public int carteJouable(int carte) {
    	return niveau.jouerCarteValide(carte);
    }
    
    public void joueCarte(int carte) {
    	niveau.jouerCarte(carte);
    	if(niveau.combatPret()) {
    		niveau.combat();
    		if(niveau.finDePhase1()) {
    			niveau.initialiserPhase2();
    		}
    		if(niveau.finDePhase2()) {
    			int x = niveau.joueurGagant();
    			if(x == JoueurA)
    				System.out.println("Joueur 1 a gagne");
    			else if(x == JoueurB)
    				System.out.println("Joueur 2 a gagne");
    			else
    				System.out.println("Match nul");
    		}
    		if(niveau.phase() == 1) {
    			 niveau.retournerNouvelleCarteEnJeu();
    		}
    	}
    }
    
    public Action annule() {
		return null;
	}

	public Action refaire() {
		return null;
	}
    

    /* sauvegarder le tableau de carte et aussi le leader
        * un tableau de taille nbCarte
            chaque case est un quadriplet (faction, poid, categorie estCache)
        * l'indice de leader
        * le joueur courant de cette config
    */
    public void sauvegarder() {

    }
    // charger la partie sauvegarder
    public void charger() {

    }
    
    public Niveau niveau() {
    	return niveau;
    }
}

