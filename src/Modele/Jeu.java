package Modele;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
    
    Niveau niveau;
    boolean menu;
    
    public Jeu() {
        niveau = new Niveau();
        menu = true;
    }
    
    public void nouvellePartie() {
    	lancerUnePartie();
        niveau.initialiserPhase1();
        niveau.determinerJoueurCommence();
        niveau.retournerNouvelleCarteEnJeu();
        menu = false;
    }
    
    public boolean estSurMenu() {
    	return menu;
    }
    
    public void lancerUnePartie() {
        niveau.initialiser();

        // Pour tester sauve/charge:1)mettre que sauvegarder 2)mettre que charger sans meme initialiser avant
        //sauvegarder();
       	//charger();
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
    
    public void annule() {
		
	}

	public void refaire() {
		
	}
    

	public static final String fichierSauvegarde="sauvegarde"; //TODO:pouvoir choisir la partie sauvegardée
    /* sauvegarder le niveau dans un fichier
    */
    public void sauvegarder() {
       try {
           FileOutputStream fileOutputStream = new FileOutputStream(fichierSauvegarde);
           ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
           objectOutputStream.writeObject(niveau);
           objectOutputStream.flush();
           objectOutputStream.close();
       }catch(Exception e){
           System.err.println("erreur sauvegarde"+e.toString());

       }
    }
    // charger la partie depuis un fichier
    public void charger() {
        try{
            FileInputStream fileInputStream = new FileInputStream(fichierSauvegarde);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            niveau = (Niveau) objectInputStream.readObject();
            objectInputStream.close();
        }catch(Exception e){
         System.err.println("erreur chargement"+e.toString());

    }
    }
    
    public Niveau niveau() {
    	return niveau;
    }
}

