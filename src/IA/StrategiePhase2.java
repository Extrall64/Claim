package IA;

import Modele.Carte;
import Modele.Plateau;

public class StrategiePhase2 implements Strategie {
	String strategie;
	int joueur, autreJoueur;
	public StrategiePhase2(String nom) {
		strategie = nom;
	}
	public void fixerStrategie(String nom) {
		strategie = nom;
	}
	/*
	 * retourner le score de la strategie choisie par l'IA
	 */
	public float score(Plateau plateau) {
		joueur = plateau.joueurCourant();
		autreJoueur = plateau.autreJoueur();
		switch(strategie) {
			case "difference": return difference(plateau);
			case "difference2": return difference2(plateau);
			default: System.err.println("Stategie non reconnue !"); break;
		}
		return 0;
	}

	/*
	 *  le score sera la difference entre nombre de carte de chaque faction de chaque joueur
	 */
	public float difference(Plateau plateau) {
		float res = 0;
		int [] R = new int [nbFaction ];
		for(Carte c: plateau.getScore(joueur))
			R[c.getFaction()] += 1;
		for(Carte c: plateau.getScore(autreJoueur))
			R[c.getFaction()] -= 1;
		for(int i = 0; i < nbFaction; i++)
			res += R[i];
		return res;
	}
	/*
	 *  meme score que "difference" mais deviser par la difference de poid entre la carte de joueur et son advzesaire
	 *  joueur le coup ayant une minimum diffirence entre la carte de joueur et son adversaire quand c'est possible
	 */
	public float difference2(Plateau plateau) {
		Carte a = plateau.carteCourante[joueur];
		Carte b = plateau.carteCourante[ autreJoueur ];
		float res = difference(plateau);
		
		if (a != null && b != null) res /= a.getPoid() - b.getPoid();
		return res;
	}
}
