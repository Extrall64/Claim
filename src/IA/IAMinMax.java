package IA;

import Modele.Carte;
import Modele.Jeu;
import Modele.Plateau;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class IAMinMax implements IA {
	Hashtable<Integer, Carte> coupGagnant;
    Jeu config;
    int joueur, horizon;
    boolean estJoueurA;
    public IAMinMax(Jeu config, int j, int h) {
    	this.config = config;
    	joueur = j;
    	horizon = h;
		coupGagnant = new Hashtable<Integer, Carte>(); 
        estJoueurA = true;
    }
    @Override
    // renvoyer une carte a jouer a chaque fois la fonction appelée
    public Carte determineCoup() {
    	int idConfig = config.plateau().hash();
    	calculJoueurA(config, horizon);
        Carte carte = coupGagnant.get( idConfig );
        System.out.printf("IA a joué son coup: %s\n", carte);
        return carte;
    }
	int calculJoueurA(Jeu config, int h) {
		estJoueurA = true;
		Plateau plateau = config.plateau();
		if (estFeuille(plateau) || h == 0)
			return evaluation(plateau);
		// represente -oo
		int valeur = - Integer.MAX_VALUE;
		List<Carte> C = coup(plateau);
		for(Carte c: C) {
			Jeu nconfig = config.clone();
			nconfig.joueCarte(c);
			int temp = calculJoueurB(nconfig, h -1);
			if (valeur < temp) {
				valeur = temp;
				System.out.printf("Meilleur coup trouvé\n");
				coupGagnant.put( plateau.hash() , c);
			}
		}
		return valeur;
	}

	int calculJoueurB(Jeu config, int h) {
		estJoueurA = false;
		Plateau plateau = config.plateau();
		if (estFeuille(plateau) || h == 0)
			return evaluation(plateau);
		// represente +oo
		int valeur = Integer.MAX_VALUE;
		List<Carte> C = coup(plateau);
		for(Carte c: C) {
			Jeu nconfig = config.clone();
			// considere que cette carte appartient au joueur b
			c.setCategorie(config.plateau().mainJoueur( config.joueurCourant() ));
			nconfig.joueCarte(c);
			int temp = calculJoueurA(nconfig, h-1);
			if (valeur > temp) {
				valeur = temp;
			}
		}
		return valeur;
	}

   	// constuire la main de joueur ou de son adversaire
   	// main adversaire contient les cartes qui ne sont pas dans mainA, scoreA, partisansA, defaussé
	List <Carte> coup(Plateau plateau) {
		List<Carte> r = new ArrayList<>();
    	for(Carte c: plateau.cartes()) {
    		if (plateau.joueurCourant() == joueur) {
    			if ( plateau.mainJoueur(joueur) == c.getCategorie())
    				r.add( c );
    		}
    		else {
				if (c.getCategorie() != plateau.mainJoueur(joueur) && c.getCategorie() != plateau.scoreJoueur(joueur)
					&& c.getCategorie() != plateau.partisansJoueur(joueur) && c.getCategorie() != Plateau.iDefausser)
					r.add( c );
			}
    	}
		return r;
	}
	boolean estFeuille(Plateau plateau) {
		return (plateau.phase() == 1 && plateau.finDePhase1()) || (plateau.phase() == 2 && plateau.finDePhase2());
	}

	int evaluation(Plateau plateau) {
		return score( plateau );
	}
	
	int score(Plateau plateau) {
		int res = 10;
		int phase = plateau.phase();
		int joueur = plateau.joueurCourant();
		int autreJoueur = plateau.autreJoueur();
		int nbFaction = 5;
		if (plateau.phase() == 2 && plateau.finDePhase2()) {
			System.out.println("Arrive au bout");
			if (plateau.joueurGagant() == joueur) return res;
			return - res;
		}
		switch(phase) {
			case 1:
				// la moyenne entre les cartes de poid fort de chaque faction dans la pile partisans
				// permet de evaluer la config qui donne les cartes de poid fort et de different faction
				int [] max = new int[nbFaction + 1];
				for(Carte c: plateau.getPartisans(joueur))
					if (max[c.getFaction()] < c.getPoid()) max[c.getFaction()] = c.getPoid();
				for(int n: max) res += n;
				res = res / nbFaction;
			break;
			
			case 2:
				// le score sera la difference entre nombre de carte de chaque faction
				int [] A = new int [nbFaction + 1];
				int [] B = new int [nbFaction + 1];
				for(Carte c: plateau.getScore(joueur))
					A[c.getFaction()] += 1;
				for(Carte c: plateau.getScore(autreJoueur))
					B[c.getFaction()] += 1;
				for(int i = 0; i < nbFaction; i++)
					res += A[i] - B[i];
			break;
		}
		if(plateau.joueurCourant() == this.joueur) return res;
		return - res;
	}
}
