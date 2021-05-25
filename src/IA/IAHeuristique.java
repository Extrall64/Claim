package IA;

import java.util.*;
import Modele.*;
import Structures.*;

/* la meme implementation que MinMax mais les stategie changent dynamiquement si on perd plus souvent
	chaque coup on met a jour la valeur heuristiquePhase1 OU heuristiquePhase2 selon la phase courante
	si ces valeur tombe a 0 alors on change la strategie et initialiser ces valeur heuristiquePhase1 et heuristiquePhase2 a biais
*/
public class IAHeuristique implements IA {
	Jeu config;
	Hashtable<Integer, List<Carte>> coupGagnant;
	final float biais = 13 / 3;
	float heuristiquePhase1, heuristiquePhase2;
    Strategie [] strategiesPhase1, strategiesPhase2;
    Random rand;
    int joueur, autreJoueur, horizon;
    float infini;

    public IAHeuristique(Jeu config, int j, int h) {
    	this.config = config;
		coupGagnant = new Hashtable<Integer, List<Carte>> (); 
    	joueur = j;
    	autreJoueur = (joueur + 1) % 2;
    	horizon = h;
    	infini = Float.MAX_VALUE;
        rand = new Random();
        // fixer les strategies et ces alternatives
        strategiesPhase1 = new Strategie [2];
        strategiesPhase1[0] = new StrategiePhase1();
        strategiesPhase1[0].fixerStrategie("moyenne3Faction");
        strategiesPhase1[1] = new StrategiePhase1();        
        strategiesPhase1[1].fixerStrategie("moyenne");
                
        strategiesPhase2 = new Strategie [2];
        strategiesPhase2[0] = new StrategiePhase2();
        strategiesPhase2[0].fixerStrategie("difference");
        strategiesPhase2[1] = new StrategiePhase2();
        strategiesPhase2[1].fixerStrategie("difference");
  
        heuristiquePhase1 = biais;
        heuristiquePhase2 = biais;
    }
    @Override
    public Carte determineCoup() {
    	int idConfig = config.plateau().hash();
    	float scoreCarte = calculJoueurA(config.clone(), horizon, - infini, + infini);
        List<Carte> meilleursCoup = coupGagnant.get( idConfig );
        int aleatoire = rand.nextInt( meilleursCoup.size() );
        Carte carte = meilleursCoup.get( aleatoire );
        System.out.printf("[IA Heuristique] a joué son coup: [%d], %s\n", aleatoire, carte);

        // mise a jour de l'heuristique
        if (scoreCarte < 0) {
        	if (config.plateau().phase() == 1) heuristiquePhase1 -= 1;
        	else heuristiquePhase2 -= 1;
        }
        if (scoreCarte > 0) {
        	if (config.plateau().phase() == 1) heuristiquePhase1 += 0.5;
        	else heuristiquePhase2 += 0.5;
        }
        // changer la strategie si on perd plus souvant avec la courante
        if (heuristiquePhase1 <= 0) {
        	heuristiquePhase1 = biais;
        	changerStrategie( strategiesPhase1 );
        	System.out.println("Strategie phase 1 changée !");
        }
        if (heuristiquePhase2 <= 0) {
        	heuristiquePhase2 = biais;
        	changerStrategie( strategiesPhase2 );
        	System.out.println("Strategie phase 2 changée !");    	
        }
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
						l = new ArrayList();
						coupGagnant.put(hash, l);
					}
					l = coupGagnant.get( hash );
					l.add(0, c);
					valeur = temp;
				}
				// Beta coupure
				if (valeur >= beta) return valeur;
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
                if (valeur <= alpha)  return valeur;
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
		// Cas particulier: si phase 1 et le de l'adversiare
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
		int phase = plateau.phase();
		int joueur = plateau.joueurCourant();

		if (!plateau.finDePhase2()) switch(phase) {
			case 1: res = strategiesPhase1[0].score( plateau, joueur ); break;
			case 2:	res = strategiesPhase2[0].score( plateau, joueur ); break;
		}
		// retourner le max pour le joueurA et le min pour le joueurB
		if (joueur == this.joueur) return res;
		return - res;
	}
	void changerStrategie(Strategie [] s) {
		Strategie t = s[0];
		s[0] = s[1];
		s[1] = t;
	}
}
