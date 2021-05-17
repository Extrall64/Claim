package Modele;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Jeu {

    private Plateau plateau;
    private boolean menu;
    
    public Jeu() {
        plateau = new Plateau();
        menu = true;
    }
    
    public void nouvellePartie() {
    	lancerUnePartie();
    	plateau.melanger();
        plateau.initialiserPhase1();
        plateau.joueurCommenceAleatoire();
        plateau.retournerNouvelleCarteEnJeu();
        menu = false;
    }
    
    public boolean estSurMenu() {
    	return menu;
    }
    
    public boolean finDePartie() {
    	return plateau.finDePhase2();
    }
    
    public void lancerUnePartie() {
        plateau.initialiser();

        // Pour tester sauve/charge:1)mettre que sauvegarder 2)mettre que charger sans meme initialiser avant
        //sauvegarder();
       	//charger();
    }
 
    public boolean carteJouable(Carte carte) {
    	return plateau.estCarteValide(carte);
    }
    
    public void joueCarte(Carte carte) {
    	plateau.jouerCarte(carte);
    	if(plateau.combatPret()) {
    		int j = plateau.quiGagneCombat();
    		plateau.setJoueur(j);
    		if(plateau.phase() == 1) {
    			plateau.combatPhase1();
    		}
    		else {
    			plateau.combatPhase2();
    		}
    		plateau.reinitialiserCarteCourante();
    		if(plateau.finDePhase1()) {
    			plateau.initialiserPhase2();
    		}
    		if(plateau.finDePhase2()) {
    			int x = plateau.joueurGagant();
    			if(x == plateau.JoueurA)
    				System.out.println("Joueur 1 a gagne");
    			else if(x == plateau.JoueurB)
    				System.out.println("Joueur 2 a gagne");
    			else
    				System.out.println("Match nul");
    		}
    		if(plateau.phase() == 1) {
    			 plateau.retournerNouvelleCarteEnJeu();
    		}
    	}
    	else {
    		plateau.changerJoueur();
    	}
    }
    
    public int joueurCourant() {
    	return plateau.joueurCourant();
    }
    
    public void annule() {
		plateau.annuler();
	}

	public void refaire() {
		plateau.refaire();
	}
    

	public static final String fichierSauvegarde="sauvegarde"; //TODO:pouvoir choisir la partie sauvegardée
    /* sauvegarder le plateau dans un fichier
    */
    public void sauvegarder() {
       try {
           FileOutputStream fileOutputStream = new FileOutputStream(fichierSauvegarde);
           ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
           objectOutputStream.writeObject(plateau);
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
            plateau = (Plateau) objectInputStream.readObject();
            objectInputStream.close();
        }catch(Exception e){
        	System.err.println("erreur chargement"+e.toString());
        }
    }
    
    public Plateau plateau() {
    	return plateau;
    }
}

