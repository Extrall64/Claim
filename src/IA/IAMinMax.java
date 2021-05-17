package IA;

import Modele.Carte;
import Modele.Jeu;

import java.lang.Math;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class IAMinMax implements IA {
	Hashtable<Integer, Carte> coupGagnant;
    // represente le tour de A ou de B, utilisé pour determiner l'evaluation()
    Jeu config;
    Random rand;
    // la main de joueur adversaire contient tt les cartes possible qu'il peut avoir
    List<Carte> mainA, mainB;
    boolean estJoueurA;
    public IAMinMax(Jeu config) {
    	this.config = config;
        rand = new Random();
		coupGagnant = new Hashtable<Integer, Carte>(); 
        estJoueurA = true;
    }
    @Override
    // renvoyer une carte a jouer a chaque fois la fonction appelée
    public Carte determineCoup() {
    	/*// constuire les mainde joueur, la main de l'adversaire sera
    	// ttes les cartes sauf les carte de A et celles dans defausser, score , partisans ...
    	for(Carte c: jeu.niveau().cartes()) {
    		if ( mainJoueur(jeu.joueurCourant()) == c.categorie)
    			mainA.add( c );
    		else if ( c.categorie == niv.iCarte || c.categorie == niv.iPioche || c.categorie == mainJoueur(jeu.autreJoueur())
    		mainB.add( c );
    	}
       	//s'il y a un coup gagnant renvoyé le sinon jouer un coup aleatoire
        calculJoueurA(config);
        // le coup a faire apres cette configuration (la grille courante)
        Carte carte = coupGagnant.get( config.niveau().hash() );
        System.out.printf("Carte jouer par l'IA: %d\n", carte);
        return carte;*/
    	return null;
    }
	int calculJoueurA(Jeu config, int horizon) {
		/*estJoueurA = true;
		if (estFeuille(config) || horizon == 0)
			return evaluation(config);
		
		// represente -oo
		int valeur = - Integer.MAX_VALUE;
		List<Carte> c = coup(config);
		for(Carte c: C) {
			Jeu nconfig = config.clone();
			nconfig.joueCarte(c);
			int temp = calculJoueurB(nconfig, horizon-1);
			if (valeur < temp) {
				valeur = temp;
				coupGagnant.put( config.niveau().hash() , c);
			}
		}
		return valeur;*/
		return 0;
	}

	int calculJoueurB(Jeu config, int horizon) {
		/*estJoueurA = false;
		if (estFeuille(config) || horizon == 0)
			return evaluation(config);
		
		// represente +oo
		int valeur = Integer.MAX_VALUE;
		List<Carte> c = coup(config);
		for(Carte c: C) {
			Jeu nconfig = config.clone();
			// fonction applique le coup joueur dans config
			nconfig.joueCarte(c);
			int temp = calculJoueurA(nconfig, horizon-1);
			if (valeur > temp) {
				valeur = temp;
			}
		}
		return valeur;*/
		return 0;
	}
	List <Carte> coup(Jeu config) {
		//List<Carte> r = new ArrayList<>();
		// enumerer l'ensemble de coup possible
		return null;
	}
	boolean estFeuille(Jeu config) {
		return true;
	}

	int evaluation(Jeu config) {
        return 0;
	}
}
