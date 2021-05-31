package IA;

import java.util.*;
import Modele.*;

/*
*l'implementation suit la meme structuration et nomenclature de l'algorithme vu en cours
*pour chaque configuration, l'ia a une liste de meilleur cartes a jouer,
*les scores de toutes ces cartes sont equivalents
*/
public class IAMinMax implements IA {
	ArrayList<Carte> meilleursCoup;
    public Jeu config;
    int joueur, autreJoueur, horizon;
    Strategie strategie1, strategie2;
    // constructeur 1 --> choisir les strategies
    public IAMinMax(Jeu c, int j, int h, String s1, String s2) {
    	config = c;
    	joueur = j;
    	autreJoueur = (j + 1) % 2;
    	horizon = h;
        // fixer strategies
        strategie1 = new StrategiePhase1();
        strategie1.fixerStrategie(s1);
        strategie2 = new StrategiePhase2();
        strategie2.fixerStrategie(s2);
    }
    // constructeur 2 -> strategies par defaut
    public IAMinMax(Jeu c, int j, int h) {
    	config = c;
    	joueur = j;
    	autreJoueur = (j + 1) % 2;
    	horizon = h;
        // fixer strategies par defaut
        strategie1 = new StrategiePhase1();
        strategie1.fixerStrategie("moyenne");
        strategie2 = new StrategiePhase2();
        strategie2.fixerStrategie("difference");
    }
    @Override
    public Carte determineCoup() {
		meilleursCoup = new ArrayList<Carte>();
    	calculJoueurA(config.clone(), horizon, -oo, +oo);

        int aleatoire = rand.nextInt( meilleursCoup.size() );
        Carte carte = meilleursCoup.get( aleatoire );
        System.out.printf("[IA MinMax] a joué son coup: [%d] %s\n", aleatoire, carte);
        return carte;
    }

	float calculJoueurA(Jeu config, int horizon, float alpha, float beta) {
		Plateau plateau = config.plateau();
		if (estFeuille(plateau) || horizon == 0)
			return evaluation(plateau);

		float valeur = -oo;
		float a = -oo;
		List<Carte> C = coup(plateau);
		for(Carte c: C) {
			if (plateau.joueurCourant() == joueur && plateau.carteJouable(c)) {
				Jeu nconfig = config.clone();
				// jouer le coup dans la config clonée
				for(Carte nc: nconfig.plateau().getMain( joueur )) {
					if (c.estEgale(nc)) {
						nconfig.joueCarte(nc);
						break;
					}
				}
				float temp = calculJoueurB(nconfig, horizon-1, alpha, beta);
				// actualisation de valeur, la liste des meilleurs coups
				if (valeur <= temp) {
					a = temp;
					// si la liste est vide ou on trouve un meilleur coup
					// initialser la liste
					// sinon ajouter le coup dans la liste
					if (this.horizon == horizon) {
						if ( temp > valeur)
							meilleursCoup = new ArrayList<Carte>();
						meilleursCoup.add(c);
					}
					valeur = temp;
				}
				// Beta coupure
				if (alpha >= beta)  return valeur;
                if (valeur > alpha) alpha = valeur;
			}
		}
		/* afficher meilleur coup de ce noeud
		if (a == - oo) System.out.printf("Evaluation A: -oo\n");
		else if (a == oo) System.out.printf("Evaluation A: +oo\n");
		else System.out.printf("Evaluation A: %.2f\n", a);
		*/
		return valeur;
	}

	float calculJoueurB(Jeu config, int horizon, float alpha, float beta) {
		Plateau plateau = config.plateau();
		if (estFeuille(plateau) || horizon == 0)
				return evaluation(plateau);
		
		float valeur = +oo;
		float b = +oo;
		List<Carte> C = coup(plateau);
		for(Carte c: C) {
			if (plateau.joueurCourant() == autreJoueur && plateau.carteJouable(c)) {
				Jeu nconfig = config.clone();
				// considere que cette carte appartient au joueur B, fausse inclusion dans ca main
				for(Carte nc: nconfig.plateau().cartes()) {
					if (c.estEgale(nc)) {
						if (nc.getCategorie() != nconfig.plateau().mainJoueur(autreJoueur)) {
							nconfig.plateau().getMain(autreJoueur).add( nc );
						}
						nc.setCategorie( plateau.mainJoueur( autreJoueur ) );
						nconfig.joueCarte(nc);
						break;
					}
				}

				float temp = calculJoueurA(nconfig, horizon-1, alpha, beta);
				if (valeur > temp) {
					valeur = temp;
					b = temp;
				}
			// Alpha coupure
			if (alpha >= beta)  return valeur;
            if (valeur < beta)  beta = valeur;
			}
		}
		/*
		if (b == - oo) System.out.printf("Evaluation B: -oo\n");
		else if (b == + oo) System.out.printf("Evaluation B: +oo\n");
		else System.out.printf("Evaluation B: %.2f\n", b);
		*/
		return valeur;
	}

   	// constuire la liste des coups possible pour le joueur courant
	List <Carte> coup(Plateau plateau) {
		List<Carte> main = plateau.getMain( plateau.joueurCourant());
		// Cas particulier: si phase 1 et le tour de l'adversiare
		// retourner l'ensemble des cartes possibles que l'adversaire peut avoir
		if (plateau.phase() == 1 && plateau.joueurCourant() == autreJoueur) {
			for(Carte c: plateau.cartes()) {
				int cat = c.getCategorie();
				if (cat == Plateau.iCartes || cat == Plateau.iPioche)
					main.add( c );	
			}
		}
		//sinon retourner la main de joueur courant
		return main;
	}

	boolean estFeuille(Plateau plateau) {
		return plateau.finDePhase1() || plateau.finDePhase2();
	}

	float evaluation(Plateau plateau) {
		return score( plateau );
	}
	
	float score(Plateau plateau) {
		float scoreParDefaut = 10;
		if (!plateau.finDePhase2()) switch(plateau.phase()) {
			case 1: return strategie1.score(plateau);
			case 2:	return strategie2.score(plateau);
		}
		return scoreParDefaut;
	}
}