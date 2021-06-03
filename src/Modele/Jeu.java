package Modele;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Jeu {

    private Plateau plateau;
    private int gagnant;
    private int mode;
    private String[] noms;
    boolean menu;
    public Jeu() {
        plateau = new Plateau();
        noms = new String[2];
        menu = true;
    }
    
    public void nouvellePartie() {
    	lancerUnePartie();
    	plateau.melanger();
        menu = false;
        gagnant = -1;
    }
    
    public void initialiserPhase1() {
    	plateau.initialiserPhase1();
        plateau.retournerNouvelleCarteEnJeu();
    }
    
    public void joueurCommence(int j) {
    	plateau.setJoueur(j);
    }
    
    public void joueurAleatoire() {
    	plateau.joueurCommenceAleatoire();
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
    
    public void jouerCarte(Carte carte) {
    	plateau.jouerCarte(carte);
    }
    
    public boolean combatPret(){
    	return plateau.combatPret();
    }
    
    public void combat() {
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
    			if(x == Plateau.JoueurA) {
    				gagnant = 1;
    			}
    			else if(x == Plateau.JoueurB) {
    				gagnant = 2;
    			}
    			else {
    				gagnant = 0;
    			}
    		}
    		if(plateau.phase() == 1) {
    			 plateau.retournerNouvelleCarteEnJeu();
    		}
    	}
    }

    public void joueCarte(Carte carte) {
    	jouerCarte(carte);
    	if(combatPret())
    		combat();
    	else
    		changeJoueur();
    }
    public void suivant(){
        if(combatPret())
            combat();
        else
            changeJoueur();
    }
    
    public void changeJoueur() {
		plateau.changerJoueur();
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
    public Jeu clone() {
    	Jeu clone = new Jeu();
    	clone.plateau = plateau.clone();
    	return clone;
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
    
    public int gagnant() {return gagnant;}
    
 // les diffirentes valeur des modes de jeu
    public static final int HUMAIN_VS_HUMAIN = 1;
    public static final int HUMAIN_VS_IA = 2;
    public static final int IA_VS_IA = 3;
    
    public void mode(int m) {
    	mode = m;
    }
    
    public int getMode() {
    	return mode;
    }
    
    public String getNom(int joueur) {
    	return noms[joueur];
    }
    
    public void setNom(int joueur, String s) {
    	noms[joueur] = s;
    }
    public void afficherResultat() {
		if (gagnant() == 0) System.out.println("Match nul");
		if (gagnant() == 1) System.out.println("Joueur 1 a gagne");
		if (gagnant() == 2) System.out.println("Joueur 2 a gagne"); 	
		gagnant = -1;
    }
	public boolean estSurMenu() {
		return menu;
	}
	
	public void setSurMenu() {
		menu = true;
	}

	public boolean estIAVsIA(){
        return mode == IA_VS_IA;
    }

    public boolean estHumVsIA(){
        return mode == HUMAIN_VS_IA;
    }

    public boolean TourHumain(){
        return mode == HUMAIN_VS_HUMAIN || mode == HUMAIN_VS_IA && joueurCourant()==0;
    }
}