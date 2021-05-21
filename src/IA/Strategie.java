package IA;

import Modele.*;

public interface Strategie {
	public void fixerStrategie(String nom);
	public float score(Plateau plateau, int joueur);
}