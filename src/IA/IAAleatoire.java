package IA;


import Modele.Carte;
import Modele.Jeu;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class IAAleatoire implements IA {
	 	int joueur;
	    Jeu config;
	    Random rand;
	    public IAAleatoire(Jeu c, int j) {
	    	config = c;
	        joueur = j;
	        rand = new Random();
	    }
	    public Carte determineCoup() {
	    	Carte c = carteAleatoire( config.plateau().getMain(joueur) );
	        System.out.printf("[IA Aleatoire] a joué son coup: %s\n", c);
	        return c;
	    }
	    // selectionner une carte jouable aleatoire
		public Carte carteAleatoire( List<Carte> main) {
	    	List<Carte> coups = new ArrayList<>();
	    	for(Carte c: main)
	    		if (config.carteJouable(c))
	    			coups.add(c);
	    	int aleatoire = rand.nextInt(coups.size());
	    	return coups.get( aleatoire );
		}
}
