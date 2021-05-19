package IA;

import Modele.Carte;
import Modele.Jeu;
import Modele.Plateau;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class IAMinMax implements IA {
	Hashtable<Integer, List<Carte>> coupGagnant;
    Jeu configPremiere;
    Random rand;
    int joueur, autreJoueur, horizon;
    float infini, betaInitial;
    public IAMinMax(Jeu config, int j, int h) {
    	configPremiere = config;
		coupGagnant = new Hashtable<Integer, List<Carte>> (); 
    	joueur = j;
    	autreJoueur = (joueur + 1) % 2;
    	horizon = h;
    	infini = Float.MAX_VALUE;
    	betaInitial = Float.MAX_VALUE;
        rand = new Random();		
    }
    @Override
    public Carte determineCoup() {
    	int idConfig = configPremiere.plateau().hash();
    	calculJoueurA(configPremiere, horizon, betaInitial);

        List<Carte> meilleursCoup = coupGagnant.get( idConfig );
        int aleatoire = rand.nextInt( meilleursCoup.size() );
        Carte carte = meilleursCoup.get( aleatoire );
        System.out.printf("IA a joué son coup: [%d], %s\n", aleatoire, carte);
        return carte;
    }

	float calculJoueurA(Jeu config, int h, float beta) {
		Plateau plateau = config.plateau();
		int hash = plateau.hash();

		if (estFeuille(plateau) || h == 0) {
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
				float temp = calculJoueurB(nconfig, h -1, beta);
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

	float calculJoueurB(Jeu config, int h, float betaAncetre) {
		Plateau plateau = config.plateau();

		if (estFeuille(plateau) || h == 0) {
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
				float temp = calculJoueurA(nconfig, h-1, beta);
				if (valeur > temp) {
					valeur = temp;
				}
			}
		}
		return valeur;
	}

   	// constuire la main de l'IA ou de l'adversaire
	List <Carte> coup(Plateau plateau) {
		// si IA joue, retourner sa main
		if (plateau.joueurCourant() == joueur) return new ArrayList(plateau.getMain(joueur));
		// si l'adversaire, retourner l'ensemble des cartes possibles que l'adversaire peut avoir
		List<Carte> r = new ArrayList<>();
    	for(Carte c: plateau.cartes()) {
    			int cat = c.getCategorie();
				if (cat != plateau.mainJoueur(joueur) && cat != plateau.scoreJoueur(joueur)
					&& cat != plateau.partisansJoueur(joueur) && cat != plateau.iDefausser)
					r.add( c );
    	}
		return r;
	}
	boolean estFeuille(Plateau plateau) {
		return (plateau.phase() == 1 && plateau.finDePhase1()) || (plateau.phase() == 2 && plateau.finDePhase2());
	}

	float evaluation(Plateau plateau) {
		return score( plateau );
	}
	
	float score(Plateau plateau) {
		int nbFaction = 5;
		float scoreParDefaut = 10;
		float res = scoreParDefaut;
		int phase = plateau.phase();
		int joueur = plateau.joueurCourant();
		int autreJoueur = plateau.autreJoueur();

		// IA arrive au feuilles, elle retourne le scoreParDefaut
		if (plateau.phase() == 2 && plateau.finDePhase2()) {
			System.out.println("Arrive au bout");
			if (plateau.joueurGagant() == joueur) return res;
			return - res;
		}
		// sinon l'IA calcule le score via une fonction heuristique
		switch(phase) {
			case 1:
				// la moyenne entre les cartes de poid fort de chaque faction dans la pile partisans
				// permet de evaluer la config qui donne les cartes de poid fort et de different faction
				int [] max = new int[nbFaction + 1];
				for(Carte c: plateau.getPartisans(joueur)) {
					if (max[c.getFaction()] < c.getPoid()) max[c.getFaction()] = c.getPoid();
				}
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
		// retourner le max pour le joueurA et le min pour le joueur
		if (plateau.joueurCourant() == joueur) return res;
		return - res;
	}

}
