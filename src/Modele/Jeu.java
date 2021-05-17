package Modele;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Jeu {

    private Niveau niveau;
    private boolean menu;
    
    public Jeu() {
        niveau = new Niveau();
        menu = true;
    }
    
    public void nouvellePartie() {
    	lancerUnePartie();
    	niveau.melanger();
        niveau.initialiserPhase1();
        niveau.joueurCommenceAleatoire();
        niveau.retournerNouvelleCarteEnJeu();
        menu = false;
    }
    
    public boolean estSurMenu() {
    	return menu;
    }
    
    public boolean finDePartie() {
    	return niveau.finDePhase2();
    }
    
    public void lancerUnePartie() {
        niveau.initialiser();

        // Pour tester sauve/charge:1)mettre que sauvegarder 2)mettre que charger sans meme initialiser avant
        //sauvegarder();
       	//charger();
    }
 
    public boolean carteJouable(Carte carte) {
    	return niveau.estCarteValide(carte);
    }
    
    public void joueCarte(Carte carte) {
    	niveau.jouerCarte(carte);
    	if(niveau.combatPret()) {
    		int j = niveau.quiGagneCombat();
    		niveau.setJoueur(j);
    		if(niveau.phase() == 1) {
    			niveau.combatPhase1();
    		}
    		else {
    			niveau.combatPhase2();
    		}
    		niveau.reinitialiserCarteCourante();
    		if(niveau.finDePhase1()) {
    			niveau.initialiserPhase2();
    		}
    		if(niveau.finDePhase2()) {
    			int x = niveau.joueurGagant();
    			if(x == Niveau.JoueurA)
    				System.out.println("Joueur 1 a gagne");
    			else if(x == Niveau.JoueurB)
    				System.out.println("Joueur 2 a gagne");
    			else
    				System.out.println("Match nul");
    		}
    		if(niveau.phase() == 1) {
    			 niveau.retournerNouvelleCarteEnJeu();
    		}
    	}
    	else {
    		niveau.changerJoueur();
    	}
    }
    
    public int joueurCourant() {
    	return niveau.joueurCourant();
    }
    
    public void annule() {
		niveau.annuler();
	}

	public void refaire() {
		niveau.refaire();
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

