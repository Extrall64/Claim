package IA;

import Modele.*;

public interface Strategie {
	final static int nbFaction = 5;
	public void fixerStrategie(String nom);
	public float score(Plateau plateau);
}