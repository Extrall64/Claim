package IA;

import java.util.Random;

import Modele.Carte;
import Modele.Jeu;
import Modele.Plateau;

public class IAAleatoire implements IA {
	 int joueur;
	    Random rand;
	    Jeu config;
	    public IAAleatoire(Jeu config, int j) {
	    	this.config = config;
	        joueur = j;
	        rand = new Random();
	    }
	    public Carte determineCoup() {
	    	Plateau plateau = config.plateau();
	    	Carte c = null;
	    	while (c == null) {
	    		Carte x = plateau.getMain(joueur).get( rand.nextInt( plateau.getMain(joueur).size() ) );
	    		if (config.carteJouable(x))
	    			c = x;
	    	}
	        plateau.getMain(joueur).remove(c);
	        System.out.printf("IA a joué son coup: %s\n", c);
	        return c;
	    }
}