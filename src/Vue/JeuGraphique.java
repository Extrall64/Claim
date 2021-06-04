package Vue;

import java.awt.*;

public interface JeuGraphique {
	int largeur();
	int hauteur();
	void tracerImage(ImageClaim img, int x, int y, int largeur, int hauteur);
	void tracerFond(ImageClaim img);
	void tracerRond(int x,int y,int largeur,int hauteur, Color couleur);
	void tracerRectArrond(int x,int y,int largeur,int hauteur,int arcL,int arcH);
	void tracerLigne(int x1,int y1,int x2,int y2);
	void tracerTxt(String s,int x,int y);
	void grise(int x,int y,int larg,int haut);
}
