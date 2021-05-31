package Vue;

import java.awt.*;

public interface JeuGraphique {
	int largeur();
	int hauteur();
	void tracerImage(ImageClaim img, int x, int y, int largeur, int hauteur);
	void tracerFond(ImageClaim img);
	void tracerRond(int x,int y,int largeur,int hauteur, Color couleur);
}
