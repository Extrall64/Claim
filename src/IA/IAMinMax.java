package IA;

import java.util.*;
import Modele.*;

/*
l'implementation suit la meme structuration et nomenclature de l'algorithme vu en cours
pour chaque configuration, l'ia a une liste de meilleur cartes a jouer,
les cores de toutes les cartes sont equivalents
*/
public class IAMinMax implements IA {
	Hashtable<Integer, List<Carte>> coupGagnant;
    Random rand;
    Jeu config;
    int joueur, autreJoueur, horizon;
    float infini;
    Strategie strategie1, strategie2;
    public IAMinMax(Jeu config, int j, int h) {
    	this.config = config;
		coupGagnant = new Hashtable<Integer, List<Carte>> (); 
    	joueur = j;
    	autreJoueur = (joueur + 1) % 2;
    	horizon = h;
    	infini = Float.MAX_VALUE;
        rand = new Random();
        
        // fixer strategies
        strategie1 = new StrategiePhase1();
        strategie1.fixerStrategie("moyenne");
        strategie2 = new StrategiePhase2();
        strategie2.fixerStrategie("difference");
    }
    @Override
    public Carte determineCoup() {
    	int idConfig = config.plateau().hash();
    	Plateau plateau = config.plateau();
    	/* DEBUG: affichage
    	System.out.printf("|Pioche| %d\n", plateau.pioche.size());
    	System.out.printf("|Main A| %d\n", plateau.getMain(0).size());
    	System.out.printf("|Main B| %d\n",  plateau.getMain(1).size());
    	System.out.printf("|Part A| %d\n",  plateau.getPartisans(0).size());
    	System.out.printf("|Part B| %d\n",  plateau.getPartisans(1).size());)
		*/		
		coupGagnant = new Hashtable<>();
    	calculJoueurA(config.clone(), horizon, - infini, + infini);

        List<Carte> meilleursCoup = coupGagnant.get( idConfig );
        int aleatoire = rand.nextInt( meilleursCoup.size() );
        Carte carte = meilleursCoup.get( aleatoire );
        System.out.printf("[IA MinMax] a joué son coup: [%d], %s\n", aleatoire, carte);
        return carte;
    }

	float calculJoueurA(Jeu config, int horizon, float alpha, float beta) {
		Plateau plateau = config.plateau();
		int hash = plateau.hash();

		if (estFeuille(plateau) || horizon == 0) {
			return evaluation(plateau);
		}
		// represente -oo
		float valeur = - infini;
		float a = - infini;
		List<Carte> C = coup(plateau);
		for(Carte c: C) {
			if (plateau.joueurCourant() == joueur && plateau.carteJouable(c)) {
				Jeu nconfig = config.clone();
				// jouer la dans la config clonée
				for(Carte nc: nconfig.plateau().getMain( plateau.joueurCourant())) {
					if (c.estEgale(nc)) {
						nconfig.joueCarte(nc);
						break;
					}
				}
				float temp = calculJoueurB(nconfig, horizon-1, alpha, beta);
				if (valeur <= temp) {
					a = temp;
					List<Carte> l;
					// si la liste est vide ou on trouve un meilleur coup
					// initialser la liste
					if (!coupGagnant.containsKey( hash ) || temp > valeur) {
						l = new ArrayList<Carte>();
						coupGagnant.put(hash, l);
					}
					l = coupGagnant.get( hash );
					l.add(0, c);
					valeur = temp;
				}
				// Beta coupure
				if (valeur > beta) return valeur;
                if (valeur > alpha) alpha = valeur;
			}
		}
		// afficher meilleur coup de ce noeud
		if (a == - infini) System.out.printf("Evaluation A: -oo\n");
		else if (a == infini) System.out.printf("Evaluation A: +oo\n");
		else System.out.printf("Evaluation A: %.2f\n", a);
		return valeur;
	}

	float calculJoueurB(Jeu config, int horizon, float alpha, float beta) {
		Plateau plateau = config.plateau();
		
		if (estFeuille(plateau) || horizon == 0) {
				return evaluation(plateau);
		}
		
		// represente +oo
		float valeur = + infini;
		float b = + infini;
		List<Carte> C = coup(plateau);
		for(Carte c: C) {
			if (plateau.joueurCourant() == autreJoueur && plateau.carteJouable(c)) {
				Jeu nconfig = config.clone();
				// considere que cette carte appartient au joueur b, fausse inclusion dans ca main
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
            if (valeur < alpha)  return valeur;
            if (valeur < beta)  beta = valeur;
			}
		}
		if (b == - infini) System.out.printf("Evaluation B: -oo\n");
		else if (b == + infini) System.out.printf("Evaluation B: +oo\n");
		else System.out.printf("Evaluation B: %.2f\n", b);
		return valeur;
	}

   	// constuire la liste des coups possible pour le joueur courant
	List <Carte> coup(Plateau plateau) {
		List<Carte> r = new ArrayList<>();
		// Cas particulier: si phase 1 et le tour de l'adversiare
		// retourner l'ensemble des cartes possibles que l'adversaire peut avoir
		if (plateau.phase() == 1 && plateau.joueurCourant() == autreJoueur) {
			for(Carte c: plateau.cartes()) {
				int cat = c.getCategorie();
				if (cat == plateau.mainJoueur(autreJoueur) || cat == plateau.iCartes || cat == plateau.iPioche)
					r.add( c );	
			}
			return r;
		}
		//sinon retourner la main de joueur courant
		return plateau.getMain( plateau.joueurCourant());
	}

	boolean estFeuille(Plateau plateau) {
		return plateau.finDePhase1() || plateau.finDePhase2();
	}

	float evaluation(Plateau plateau) {
		return score( plateau );
	}
	
	float score(Plateau plateau) {
		float scoreParDefaut = 10;
		float res = scoreParDefaut;
		int joueur = plateau.joueurCourant();

		if (!plateau.finDePhase2()) switch(plateau.phase()) {
			case 1: res = strategie1.score(plateau); break;
			case 2:	res = strategie2.score(plateau); break;
		}
		// retourner le max pour le joueurA et le min pour le joueurB
		if (joueur == this.joueur) return res;
		return - res;
	}
}
