package IA;

import Modele.Carte;
import Modele.Plateau;

public class StrategiePhase1 implements Strategie {
	String strategie;
	int joueur;
	public StrategiePhase1(String nom) {
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
		switch(strategie) {
			case "moyenne": return moyenne(plateau);
			case "moyenne3FactionMajoritaire": return moyenne3FactionMajoritaire(plateau);
			case "moyenneAvecPoidFaction": return moyenneAvecPoidFaction(plateau);
			case "favoriseFactionEnMain": return favoriseFactionEnMain(plateau);
			default: System.err.println("Stategie non reconnue !"); break;
		}
		return 0;
	}

	/*
	 *  la moyenne des cartes de poid fort de chaque faction dans la pile partisans
	 *	favoriser la config qui pocessede les cartes de poid fort et de differente faction au meme temps
	 */
	public float moyenne(Plateau plateau) {
		float res = 0;
		int [] max = new int[nbFaction];
		for(Carte c: plateau.getPartisans(joueur))
			if (max[c.getFaction()] < c.getPoid()) max[c.getFaction()] = c.getPoid();
		for(int n: max) res += n;
		return res/nbFaction - 5;	
	}

	/*
	 *  calculer la moyenne des cartes de poid fort de 3 faction majoritaire dans le pile partisans
	 *	ne favorise pas les config de moyenne plus fort de 5 factions sur les 3 faction majoritaire demandée
	 */
	public float moyenne3FactionMajoritaire(Plateau plateau) {
		int x = 0;
		float res = 0;

		int [] max = new int[nbFaction];
		for(Carte c: plateau.getPartisans(joueur))
			if (max[c.getFaction()] < c.getPoid()) max[c.getFaction()] = c.getPoid();

		int [] factionMajorite = new int[nbFaction];
		for(Carte c: plateau.getPartisans(joueur))
			factionMajorite[c.getFaction()] += 1;

		// eliminer les 2 factions minoritaires
		int i = 0;
		for(i = 0; i < nbFaction; i++)
			if (factionMajorite[x] > factionMajorite[i])
				x = i;
		max[x] = 0;

		for(i = i + 1; i < nbFaction; i++)
			if (factionMajorite[x] > factionMajorite[i])
				x = i;
		max[x] = 0;

		for(int n: max) res += n;
		return res/3 - 5;	
	}
	
	/*	la meme strategie de moyenne mais a chaque faction son poid
	 *	favoriser la config qui possede des nains faibles dans la pile partisans pour activation de pouvoir
	 */
	public float moyenneAvecPoidFaction(Plateau plateau) {
		float res = 0;
		float [] poid = new float [] {1, -1, 1, 1, 1};
		int [] max = new int[nbFaction];
		for(Carte c: plateau.getPartisans(joueur))
			if (max[c.getFaction()] < c.getPoid()) max[c.getFaction()] = c.getPoid();
		for(int i = 0; i < nbFaction; i++) res += poid[i] * max[i];
		return res/nbFaction - 5;	
	}
	/*
	 *  favorise l'aquisition des cartes des faction les plus presentes en main
	 *  le poid est le nombre de carte d'une faction
	 */
	public float favoriseFactionEnMain(Plateau plateau) {
		float res = 0;
		float [] poid = new float [nbFaction];
		float [] max = new float [nbFaction];

		for(Carte c: plateau.getPartisans(joueur))
			if (max[c.getFaction()] < c.getPoid()) max[c.getFaction()] = c.getPoid();
		for(Carte c: plateau.getMain(joueur))
			poid[c.getFaction()] += 1;
		for(int i = 0; i < nbFaction; i++) res += poid[i] * max[i]; 
		return res - 5;
	}
}
