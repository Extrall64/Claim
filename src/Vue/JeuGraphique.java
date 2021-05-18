package Vue;

public interface JeuGraphique {
	int largeur();
	int hauteur();
	void tracerImage(ImageClaim img, int x, int y, int largeur, int hauteur);
	void tracerFond(ImageClaim img);
}
