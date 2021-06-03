package Vue;

import java.awt.*;

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

	public boolean okDrop(int x,int y){
		return vue.okDrop(x,y);
	}

	public void estRelease(){
		vue.estRelease();
	}
	
	public int joueurCourant() {
		return vue.joueurCourant();
	}

	public void tracerRond(int x,int y,int largeur,int hauteur, Color couleur){
		drawable.setColor(couleur);
		drawable.fillOval(x,y,largeur,hauteur);
	}

	public void tracerRectArrond(int x,int y,int largeur,int hauteur,int arcL,int arcH){
		drawable.setColor(Color.darkGray);
		drawable.drawRoundRect(x,y,largeur,hauteur,arcL,arcH);
	}

	public void tracerLigne(int x1,int y1,int x2,int y2){
		drawable.drawLine(x1,y1,x2,y2);
	}

	public void tracerTxt(String s,int x,int y){
		drawable.setColor(Color.black);
		drawable.drawString(s,x,y);
	}
	
}
