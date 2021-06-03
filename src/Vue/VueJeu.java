package Vue;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import Global.Configuration;
import Modele.Carte;
import Modele.Jeu;
import Modele.Plateau;

public class VueJeu {
	int joueurCourant,hauteurCarte;
	Point[] posCartes;
	Jeu jeu;
	ImageClaim images[][]; //ex images[0][1] image gobelin de force 1
	ImageClaim dos;
	ImageClaim fond;
	ImageClaim j1,j2;
	ImageClaim cadreCarte;
	JeuGraphique jg;

	boolean estEnAnim;
	int x,y,vx,vy;

	private ImageClaim chargeCartes(String nom) {
		InputStream in = Configuration.charge("carte" + File.separator + nom + ".jpg");
		return ImageClaim.getImageClaim(in);
	}
	
	private ImageClaim chargeImage(String nom) {
		InputStream in = Configuration.charge("Image" + File.separator + nom + ".jpg");
		return ImageClaim.getImageClaim(in);
	}

	public VueJeu(Jeu j, JeuGraphique n) {
		estEnAnim = false;
		images = new ImageClaim[5][10];
		for(int i=0;i<10;i++) {
			images[Plateau.NAINS][i] = chargeCartes("nain_"+i);
			images[Plateau.MORTSVIVANTS][i] = chargeCartes("mort-vivant_"+i);
			images[Plateau.DOPPELGANGERS][i] = chargeCartes("doppel_"+i);
			images[Plateau.GLOBELINS][i] = chargeCartes("gobelin_"+i);
			if(i>=2) {
				images[Plateau.CHEVALIERS][i] = chargeCartes("chevalier_"+i);
			}
		}
		dos = chargeCartes("carte_dos");
		fond = chargeImage("fond");
		jeu = j;
		jg = n;
		j1 = chargeImage("j1");
		j2 = chargeImage("j2");
		cadreCarte = ImageClaim.getImageClaim(Configuration.charge("Image/Fond_carte.png"));
	}

	void tracerNiveau() {
		jg.tracerFond(fond);
		tracerPartie();
	}

	public void tracerPartie() {
		Plateau plateau = jeu.plateau();
		int largeur = jg.largeur();
		int hauteur = jg.hauteur();

		dessineJoueur(1, 0, 0, largeur / 7, hauteur / 3);
		dessineJoueur(0, 0, 2 * hauteur / 3, largeur / 7, hauteur);

		dessineMain(1, largeur / 7, 0, 5 * largeur / 7, hauteur / 3);
		dessineMain(0, largeur / 7, 2 * hauteur / 3, 5 * largeur / 7, hauteur);

		dessineCartesCourantes(largeur / 3, hauteur / 3, 2 * largeur / 3, 2 * hauteur / 3);

		dessineDefausse(2 * largeur / 3, hauteur / 3, largeur, 2 * hauteur / 3);
		if(plateau.phase()==1) {
			dessinePioche(0, hauteur / 3, largeur / 3, 2 * hauteur / 3);
			dessinePartisansScore(1, 5 * largeur / 7, 0, largeur, hauteur / 3);
			dessinePartisansScore(0, 5 * largeur / 7, 2 * hauteur / 3, largeur, hauteur);
		}else{
			dessineScore(1, 5 * largeur / 7, 0, largeur, hauteur / 3);
			dessineScore(0, 5 * largeur / 7, 2 * hauteur / 3, largeur, hauteur);
		}
		if(jeu.combatPret()){

		}
	}

	private void dessineScore(int joueur,int x1,int y1,int x2,int y2){
		int largeur = x2-x1;
		int hauteur = y2-y1;

		int margeL = largeur/7;
		int margeH = hauteur/20;

		dessineCarteScore(joueur,x1+2*margeL,y1+joueur*2*margeH,4*margeL,hauteur-3*margeH);
	}

	//dessine les perso entre les points
	private void dessineJoueur(int joueur,int x1,int y1,int x2,int y2){
		int largeur = x2-x1;
		int hauteur = y2-y1;
		Color c;
		if(joueur == jeu.joueurCourant()){
			c = Color.YELLOW;
		}else{
			c = Color.lightGray;
		}

		int margeL = largeur/10;
		int margeH = hauteur/10;

		jg.tracerRond(x1+margeL,y1+margeH,largeur-2*margeL,hauteur-2*margeH,c);

		if (joueur==0){
			jg.tracerImage(j1,x1+3*margeL,y1+3*margeH,largeur-6*margeL,hauteur-6*margeH);
			jg.tracerTxt(jeu.getNom(joueur), +3*margeL,y1+3*margeH+hauteur-5*margeH);
		}else{
			jg.tracerImage(j2,x1+3*margeL,y1+3*margeH,largeur-6*margeL,hauteur-6*margeH);
			jg.tracerTxt(jeu.getNom(joueur),x1+3*margeL,y1+3*margeH+hauteur-5*margeH);
		}
	}
	private void dessinePioche(int x1,int y1,int x2,int y2){
		int largeur = x2-x1;
		int hauteur = y2-y1;

		int margeL = largeur/20;
		int margeH = hauteur/20;
		int l = 15*largeur/40;
		int h = hauteur - 2*margeH;

		if(jeu.plateau().pioche.size() > 0) {
			jg.tracerImage(dos, x1 + 2 * margeL, y1, l, h);
			jg.tracerTxt("Pioche",2*margeL+x1,y1+h+margeH);
		}
		Carte aGagner = jeu.plateau().carteAJouer();
		if(aGagner != null) {
			jg.tracerImage(images[aGagner.getFaction()][aGagner.getPoid()], x1 + 3 * margeL + l, y1, l, h);
		}

	}
	private void dessineMain(int joueur,int x1,int y1,int x2,int y2){
		joueurCourant = jeu.joueurCourant();
		if(joueurCourant == joueur){
			posCartes = new Point[14];
		}
		int nbCarteEspace = 6;
		int largeur = x2-x1;
		hauteurCarte = y2-y1;

		int margeH = hauteurCarte/30;

		List<Carte> main = jeu.plateau().getMain(joueur);
		int nbCartes = main.size();
		int tailleCarte = largeur/nbCarteEspace;
		int tailleReel = largeur/(nbCartes+1);
		tailleReel = (tailleReel > tailleCarte)?tailleCarte:tailleReel;

		int coef = (joueur == 0)? 1:-1;

		Carte c;
		int i = 0;
		while (i<nbCartes) {
			c = main.get(i);
			if (jeu.joueurCourant() == joueur && jeu.TourHumain() || jeu.estIAVsIA() || jeu.estHumVsIA() && joueur==0) {
				jg.tracerImage(images[c.getFaction()][c.getPoid()], x1 + i * tailleReel, y1 + coef * margeH, tailleCarte, hauteurCarte);
				if (jeu.TourHumain()) {
					posCartes[i] = new Point(x1 + i * tailleReel, y1 + coef * margeH);
				}
			} else {
				jg.tracerImage(dos, x1 + i * tailleReel, y1 + coef * margeH, tailleCarte, hauteurCarte);
			}
			i++;
		}
		if(jeu.joueurCourant()==joueur && nbCartes !=0 && jeu.TourHumain()) {
			posCartes[i] = new Point(posCartes[i - 1].x + tailleCarte, y1 + coef * margeH);
		}
	}

	private void dessineCarteScore(int joueur,int x,int y,int larg,int haut){
		List<Carte> cartes = jeu.plateau().getScore(joueur);
		int nbCartes = cartes.size();
		if(nbCartes > 0) {
			Carte c = cartes.get(nbCartes-1);
			jg.tracerImage(images[c.getFaction()][c.getPoid()], x, y, larg, haut);
		}
	}
	private void dessinePartisansScore(int joueur,int x1,int y1,int x2,int y2){
		int largeur = x2-x1;
		int hauteur = y2-y1;

		int margeL = largeur/20;
		int margeH = hauteur/20;

		if (joueur==0){
			if(jeu.plateau().getPartisans(joueur).size() > 0) {
				jg.tracerImage(dos, x1 + 2 * margeL, y1, 8 * largeur / 20, hauteur - 3 * margeH);
			}
			jg.tracerTxt("Partisans",x1+2*margeL,y1+hauteur-2*margeH);
			dessineCarteScore(joueur,x1+11*margeL,y1,8*largeur/20,hauteur-3*margeH);
			jg.tracerTxt("Score",11*margeL+x1,y1+hauteur-2*margeH);
		}else{
			if(jeu.plateau().getPartisans(joueur).size() > 0) {
				jg.tracerImage(dos, x1 + 2 * margeL, 3 * margeH + y1, 8 * largeur / 20, hauteur - 3 * margeH);
			}
			jg.tracerTxt("Partisans",2*margeL+x1,y1+2*margeH);
			dessineCarteScore(joueur,x1+11*margeL,3*margeH+y1,8*largeur/20,hauteur-3*margeH);
			jg.tracerTxt("Score",11*margeL+x1,y1+2*margeH);
		}
	}

	private void dessineCartesCourantes(int x1,int y1,int x2,int y2){
		int largeur = x2-x1;
		int hauteur = y2-y1;

		int margeL = largeur/20;
		int margeH = hauteur/20;

		Carte a = jeu.plateau().carteCourante(0);
		if(a != null) {
			jg.tracerImage(images[a.getFaction()][a.getPoid()],x1+margeL,y1+margeH,8*margeL,17*margeH);
		}
		Carte b = jeu.plateau().carteCourante(1);
		if(b != null) {
			jg.tracerImage(images[b.getFaction()][b.getPoid()],x1+11*margeL,y1+margeH,8*margeL,17*margeH);
		}
		jg.tracerImage(cadreCarte,x1+margeL,y1+margeH,8*margeL,17*margeH);
		jg.tracerTxt(jeu.getNom(0),x1+margeL,y2-margeH);
		jg.tracerImage(cadreCarte,x1+11*margeL,y1+margeH,8*margeL,17*margeH);
		jg.tracerTxt(jeu.getNom(1), 11*margeL+x1,y2-margeH);
	}
	private void dessineDefausse(int x1,int y1,int x2,int y2){
		int largeur = x2-x1;
		int hauteur = y2-y1;

		int margeL = largeur/10;
		int margeH = hauteur/10;

		List<Carte> def = jeu.plateau().defausse;
		if(def.size() >0){
			Carte c = def.get(def.size()-1);
			jg.tracerImage(images[c.getFaction()][c.getPoid()],x1+3*margeL,y1+margeH,3*margeL,hauteur-3*margeH);
		}
		jg.tracerTxt("Défausse",x1+3*margeL,y2-1*margeH);
	}
	
	public int joueurCourant() {
		return joueurCourant;
	}
	
	public Carte determinerCarte(int x, int y) {
		if(!estEnAnim && jeu.TourHumain()) {
			joueurCourant = jeu.joueurCourant();
			int i = 0;
			List<Carte> main = jeu.plateau().getMain(joueurCourant);
			int nbCarte = main.size();
			while (i < nbCarte) {
				if (x >= posCartes[i].x && x < posCartes[i + 1].x && y >= posCartes[i].y && y <= posCartes[i].y + hauteurCarte) {
					return jeu.plateau().cartePosMain(i, jeu.joueurCourant());
				}
				i++;
			}
		}
		return null;
	}
}
