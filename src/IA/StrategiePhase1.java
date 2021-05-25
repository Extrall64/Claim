package IA;

import Modele.*;

public class StrategiePhase1 implements Strategie {
	String strategie;
	public StrategiePhase1() {}
	public void fixerStrategie(String nom) {
		strategie = nom;
	}

	public float score(Plateau plateau, int joueur) {
		switch(strategie) {
			case "moyenne": return moyenne(plateau, joueur);
			case "moyenne3Faction": return moyenne3Faction(plateau, joueur);
			default: System.out.println("Stategie non reconnue !"); break;
		}
		return 0;
	}

	// la moyenne entre les cartes de poid fort de chaque faction dans la pile partisans
	// permet de evaluer la config qui donne les cartes de poid fort et de different faction
	public float moyenne(Plateau plateau, int joueur) {
		float res = 0;
		int nbFaction = 5;
		int [] max = new int[nbFaction + 1];
		for(Carte c: plateau.getPartisans(joueur))
			if (max[c.getFaction()] < c.getPoid()) max[c.getFaction()] = c.getPoid();
		for(int n: max) res += n;
		// le resultat sera negatif la valeur est < 5 sinon positif si > 5
		return (res / nbFaction) - 5;	
	}

	// calculer la moyenne de poid de 3 faction majoritaire
	public float moyenne3Faction(Plateau plateau, int joueur) {
		int x = 0;
		float res = 0;
		int nbFaction = 5;

		int [] max = new int[nbFaction + 1];
		for(Carte c: plateau.getPartisans(joueur))
			if (max[c.getFaction()] < c.getPoid()) max[c.getFaction()] = c.getPoid();

		int [] factionMajorite = new int[nbFaction + 1];
		for(Carte c: plateau.getPartisans(joueur))
			factionMajorite[c.getFaction()] += 1;

		// eliminer les 2 factions minimum
		for(int i = 0; i < nbFaction; i++)
			if (factionMajorite[x] > factionMajorite[i])
				x = i;
		max[x] = 0;

		for(int i = 0; i < nbFaction; i++)
			if (factionMajorite[x] > factionMajorite[i])
				x = i;
		max[x] = 0;

		for(int n: max) res += n;
		return res / 3;	
	}
	

}
