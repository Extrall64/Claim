package IA;


import Modele.Carte;
import Modele.Jeu;
import java.util.List;
import java.util.ArrayList;

public class IAAleatoire implements IA {
	 	int joueur;
	    Jeu config;
	    public IAAleatoire(Jeu c, int j) {
	    	config = c;
	        joueur = j;
	    }
	    public Carte determineCoup() {
	    	Carte c = carteAleatoire( config.plateau().getMain(joueur) );
	        System.out.printf("[IA Aleatoire] a joué son coup: %s\n", c);
	        return c;
	    }
	    // selectionner une carte jouable aleatoire
		public Carte carteAleatoire( List<Carte> main) {
	    	List<Carte> l = new ArrayList<>();
	    	for(Carte c: main)
	    		if (config.carteJouable(c)) l.add(c);
	    	return l.get( rand.nextInt(l.size()) );
		}
}