package IA;

import Modele.Jeu;

public class IAVisionComplete extends IAMonteCarlo {
	public IAVisionComplete(Jeu c, int j, int nb) {
		super(c, j, nb );
		activerVisionComplete();
	}
}
