package Vue;

public interface JeuGraphique {
	int largeur();
	int hauteur();
	void tracerImage(ImageClaim img, int x, int y, int largeur, int hauteur);
	/*void tracerCroix(int valeur, int x, int y, int largeurCase, int hauteurCase);

	void decale(double dL, double dC, int l, int c);

	void etapePousseur();

	void changeDirectionPousseur(int dL, int dC);*/
}
