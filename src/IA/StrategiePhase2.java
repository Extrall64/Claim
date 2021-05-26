package IA;

import Modele.*;

public class StrategiePhase2 implements Strategie {
	String strategie;
	public StrategiePhase2() {}
	public void fixerStrategie(String nom) {
		strategie = nom;
	}

	public float score(Plateau plateau) {
		switch(strategie) {
			case "difference": return difference(plateau, plateau.joueurCourant());
			default: System.out.println("Stategie non reconnue !"); break;
		}
		return 0;
	}

	// le score sera la difference entre nombre de carte de chaque faction
	public float difference(Plateau plateau, int joueur) {
		int res = 0;
		int nbFaction = 5;
		int autreJoueur = plateau.autreJoueur();
		
		int [] A = new int [nbFaction ];
		int [] B = new int [nbFaction ];
		for(Carte c: plateau.getScore(joueur))
			A[c.getFaction()] += 1;
		for(Carte c: plateau.getScore(autreJoueur))
			B[c.getFaction()] += 1;
		for(int i = 0; i < nbFaction; i++)
			res += A[i] - B[i];
		return res;
	}
}