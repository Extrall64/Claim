package IA;

import java.util.*;
import Modele.*;
import Structures.*;

/*pour chaque configuration, l'ia a une liste de meilleur cartes a jouer,
les cores de toutes les cartes sont equivalents
*/
public class IAMinMax implements IA {
	Hashtable<Integer, List<Carte>> coupGagnant;
    Jeu config;
    Random rand;
    int joueur, autreJoueur, horizon;
    float infini, betaInitial;
    Strategie strategie1, strategie2;
    public IAMinMax(Jeu config, int j, int h) {
    	this.config = config;
		coupGagnant = new Hashtable<Integer, List<Carte>> (); 
    	joueur = j;
    	autreJoueur = (joueur + 1) % 2;
    	horizon = h;
    	infini = Float.MAX_VALUE;
    	betaInitial = + infini;
        rand = new Random();
        
        strategie1 = new StrategiePhase1();
        strategie1.fixerStrategie("moyenne");
        strategie2 = new StrategiePhase2();
        strategie2.fixerStrategie("difference");
    }
    @Override
    public Carte determineCoup() {
    	int idConfig = config.plateau().hash();
    	calculJoueurA(config, horizon, betaInitial);

        List<Carte> meilleursCoup = coupGagnant.get( idConfig );
        int aleatoire = rand.nextInt( meilleursCoup.size() );
        Carte carte = meilleursCoup.get( aleatoire );
        System.out.printf("[IA MinMax] a joué son coup: [%d], %s\n", aleatoire, carte);
        return carte;
    }

	float calculJoueurA(Jeu config, int horizon, float beta) {
		Plateau plateau = config.plateau();
		int hash = plateau.hash();

		if (estFeuille(plateau) || horizon == 0) {
			System.out.printf("Evaluation A\n");
			return evaluation(plateau);
		}
		// represente -oo
		float valeur = - infini;
		List<Carte> C = coup(plateau);

		for(Carte c: C) {
			if (joueur == plateau.joueurCourant() && plateau.carteJouable(c)) {
				Jeu nconfig = config.clone();
				nconfig.joueCarte(c);
				float temp = calculJoueurB(nconfig, horizon -1, beta);
				if (valeur <= temp) {

					List<Carte> l;
					if (!coupGagnant.containsKey( hash ) || temp > valeur) {
						l = new ArrayList();
						coupGagnant.put(hash, l);
						if (temp < infini)
							System.out.printf("Meilleur score trouvé pour A: %.2f\n", temp);
					}
					l = coupGagnant.get(hash);
					l.add(0, c);
					valeur = temp;
				}
			}
		}
		return valeur;
	}

	float calculJoueurB(Jeu config, int horizon, float betaAncetre) {
		Plateau plateau = config.plateau();

		if (estFeuille(plateau) || horizon == 0) {
				System.out.printf("Evaluation B\n");
				return evaluation(plateau);
		}
		
		// represente +oo
		float valeur = + infini;
		float beta;
		List<Carte> C = coup(plateau);
		for(Carte c: C) {
			if (autreJoueur == plateau.joueurCourant() && plateau.carteJouable(c)) {
				Jeu nconfig = config.clone();
				// considere que cette carte appartient au joueur b
				c.setCategorie( plateau.mainJoueur( autreJoueur ) );
				nconfig.joueCarte(c);

				beta = valeur;
				// "Coupure" cesse de parcourir les autres noeud frere
				if (beta > betaAncetre) {
					System.out.printf("Beta coupure !!!\n");
					return valeur;
				}
				float temp = calculJoueurA(nconfig, horizon-1, beta);
				if (valeur > temp) {
					valeur = temp;
				}
			}
		}
		return valeur;
	}

   	// constuire la liste des coups possible pour le joueur courant
	List <Carte> coup(Plateau plateau) {
		// Cas particulier: si phase 1 et le de l'adversiare
		// retourner l'ensemble des cartes possibles que l'adversaire peut avoir
		if (plateau.phase() == 1 && plateau.joueurCourant() == autreJoueur) {
			List<Carte> r = new ArrayList<>();
			for(Carte c: plateau.cartes()) {
				int cat = c.getCategorie();
				if (cat != plateau.mainJoueur(joueur) && cat != plateau.iDefausser
					&& cat != plateau.partisansJoueur(joueur)&& cat != plateau.partisansJoueur(autreJoueur))
					r.add( c );	
			}
			return r;
		}
		//sinon retourner la main de joueur courant
		return new ArrayList(plateau.getMain(plateau.joueurCourant()));
	}

	boolean estFeuille(Plateau plateau) {
		return (plateau.phase() == 1 && plateau.finDePhase1()) || (plateau.phase() == 2 && plateau.finDePhase2());
	}

	float evaluation(Plateau plateau) {
		return score( plateau );
	}
	
	float score(Plateau plateau) {
		float scoreParDefaut = 10;
		float res = scoreParDefaut;
		int joueur = plateau.joueurCourant();

		// IA arrive au feuilles, elle retourne le scoreParDefaut
		if (plateau.finDePhase2())
			System.out.println("Arrive au bout");
		// sinon l'IA calcule le score via une fonction heuristique
		else switch(plateau.phase()) {
			case 1: res = strategie1.score( plateau, joueur ); break;
			case 2:	res = res = strategie2.score( plateau, joueur ); break;
		}
		// retourner le max pour le joueurA et le min pour le joueurB
		if (joueur == this.joueur) return res;
		return - res;
	}
}
