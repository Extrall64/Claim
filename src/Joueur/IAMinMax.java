package Joueur;

import Arbitre.*;
import Patterns.Point;
import java.util.*;
import java.lang.Math;

public class IAMinMax implements Joueur {
    Modele m;
    int [] main;
	Hashtable<Integer, List<Point>> coupGagnant;
    // represente le tour de A ou de B, utilisé pour determiner l'evaluation()
    boolean estJoueurA;
    public IAMinMax(Modele m, int [] ref_main) {
		this.m = m;
        rand = new Random();
		coupGagnant = new Hashtable<Integer, List<Point> >(); 
        estJoueurA = true;
    }
    @Override
    // renvoyer une carte a jouer a chaque fois la fonction appelée
    public int determineCoup() {
        //s'il y a un coup gagnant renvoyé le sinon jouer un coup aleatoire
        calculJoueurA(n);
        // le coup a faire apres cette configuration (la grille courante)
        int carte = coupGagnant.get(n.hash()).get(0);
        System.out.printf("Carte jouer par l'IA: %d\n", carte);
        return carte;
    }
	int calculJoueurA(Niveau config) {
		return 0;
	}

	int calculJoueurB(InterfaceNiveau config) {
		return 0;
	}
	List <Point> coup(Niveau niv) {
		List<Point> r = new ArrayList<>();
		// enumerer l'ensemble de coup possible
		return r;
	}
	boolean estFeuille(Niveau niv) {
		return false;
	}

	int evaluation(Niveau niv) {
        return 0;
	}
}