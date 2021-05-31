package Vue;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import Modele.Carte;
import Modele.Jeu;


public class JeuGraphiqueSwing extends JComponent implements JeuGraphique{
	int largeur, hauteur;
	Graphics2D drawable;
	VueJeu vue;

	public JeuGraphiqueSwing(Jeu j) {
		vue = new VueJeu(j, this);
	}

	@Override
	public void paintComponent(Graphics g) {
		
		drawable = (Graphics2D) g;

		largeur = getSize().width;
		hauteur = getSize().height;

		drawable.clearRect(0, 0, largeur, hauteur); //efface tout
		
		tracerNiveau();
	}
	
	void tracerNiveau(){
		vue.tracerNiveau();
	}

	@Override
	public int largeur() {
		return largeur;
	}

	@Override
	public int hauteur() {
		return hauteur;
	}
	
	public int largeurCase() {
		return vue.largeurCase();
	}
	
	public int hauteurCase() {
		return vue.hauteurCase();
	}
	
	@Override
	public void tracerImage(ImageClaim img, int x, int y, int largeur, int hauteur) {
		drawable.drawImage(img.image(), x, y, largeur, hauteur, null);
	}
	
	public void tracerFond(ImageClaim img) {
		drawable.drawImage(img.image(), 0, 0, largeur, hauteur, null);
	}
	
	public Carte determinerCarte(int l, int c) {
		return vue.determinerCarte(l,c);
	}
	
	public int joueurCourant() {
		return vue.joueurCourant();
	}
	
}
