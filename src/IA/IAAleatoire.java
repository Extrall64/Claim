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
    	Plateau niv = config.plateau();
    	Carte c = null;
    	while (c == null) {
    		System.out.println("c");
    		Carte x = niv.getMain(joueur).get( rand.nextInt( niv.getMain(joueur).size() ) );
    		if (config.carteJouable(x))
    			c = x;
    	}
        System.out.print("IA joue:"); System.out.println(c);
        return c;
    }
}