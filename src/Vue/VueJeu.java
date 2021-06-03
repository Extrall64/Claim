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
	int joueurCourant;
	Point[] posCartes;
	Jeu jeu;
	ImageClaim images[][]; //ex images[0][1] image gobelin de force 1
	ImageClaim dos;
	ImageClaim fond;
	ImageClaim j1,j2;
	ImageClaim cadreCarte;
	JeuGraphique jg;

	int hauteur,largeur;
	int carteL,carteH;
	int margeL,margeH;

	Carte anim;
	boolean estEnAnim,combatAnim;
	int xCarteWin,yCarteWin,xPioche,yPioche;
	int vx,vy;
	int destxJ1,destyJ1,destxJ2,destyJ2;
	int depuisxPioche,depuisyPioche,depuisxCarte,depuisyCarte;
	int joueurWin;

	Carte dragAndDrop;
	int xDropDeb,yDropDeb,xDropFin,yDropFin;
	int dragX,dragY;

	private ImageClaim chargeCartes(String nom) {
		InputStream in = Configuration.charge("carte" + File.separator + nom + ".jpg");
		return ImageClaim.getImageClaim(in);
	}
	
	private ImageClaim chargeImage(String nom) {
		InputStream in = Configuration.charge("Image" + File.separator + nom + ".jpg");
		return ImageClaim.getImageClaim(in);
	}

	public VueJeu(Jeu j, JeuGraphique n) {
		vx = 7;
		vy = 3;
		combatAnim = false;
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

	public void tracerPartie(){
		Plateau plateau = jeu.plateau();
		largeur = jg.largeur();
		hauteur = jg.hauteur();

		carteL = 2*largeur/20;
		carteH = 5*hauteur/20;
		margeL = largeur/40;
		margeH = hauteur/40;

		dessineJoueur();
		dessineScore();
		dessineCartesCourantes();
		dessineMains();

		dessineDefausse();
		if(plateau.phase()==1) {
			dessinePioche();
			dessineAgagner();
			dessinePartisans();
		}
	}

	private void dessineMains(){
		joueurCourant = jeu.joueurCourant();
		if(jeu.TourHumain()){
			posCartes = new Point[14];
		}
		//joueur 1
		List<Carte> mainJ0 = jeu.plateau().getMain(0);
		int nbCartesJ0 = mainJ0.size();
		int tailleReelJ0 = 20*margeL/(nbCartesJ0+1);
		tailleReelJ0 = (tailleReelJ0 > carteL)?carteL:tailleReelJ0;
		Carte c;
		int i = 0;
		while (i<nbCartesJ0) {
			c = mainJ0.get(i);
			//face
			if (jeu.joueurCourant() == 0 && jeu.TourHumain() || jeu.estIAVsIA() || jeu.estHumVsIA()) {
				if(dragAndDrop != null && c.equals(dragAndDrop)){
					jg.tracerImage(images[c.getFaction()][c.getPoid()], dragX, dragY, carteL, carteH);
				}else {
					jg.tracerImage(images[c.getFaction()][c.getPoid()],  8*margeL + i * tailleReelJ0, hauteur-carteH, carteL, carteH);
				}
				if (jeu.TourHumain()) {
					posCartes[i] = new Point(8*margeL + i * tailleReelJ0, hauteur-carteH);
				}
			} else { //dos
				jg.tracerImage(dos, 8*margeL + i * tailleReelJ0, hauteur-carteH, carteL, carteH);
			}
			i++;
		}
		if(jeu.joueurCourant()==0 && nbCartesJ0 !=0 && jeu.TourHumain()) {
			posCartes[i] = new Point(posCartes[i - 1].x + carteL, hauteur-carteH);
		}

		//joueur 2
		List<Carte> mainJ1 = jeu.plateau().getMain(1);
		int nbCartesJ1 = mainJ1.size();
		int tailleReelJ1 = 20*margeL/(nbCartesJ1+1);
		tailleReelJ1 = (tailleReelJ1 > carteL)?carteL:tailleReelJ1;
		i = 0;
		while (i<nbCartesJ1) {
			c = mainJ1.get(i);
			//face
			if (jeu.joueurCourant() == 1 && jeu.TourHumain() || jeu.estIAVsIA()) {
				if(dragAndDrop != null && c.equals(dragAndDrop)){
					jg.tracerImage(images[c.getFaction()][c.getPoid()], dragX, dragY, carteL, carteH);
				}else {
					jg.tracerImage(images[c.getFaction()][c.getPoid()],  8*margeL + i * tailleReelJ1, 0, carteL, carteH);
				}
				if (jeu.TourHumain()) {
					posCartes[i] = new Point(8*margeL + i * tailleReelJ1, 0);
				}
			} else { //dos
				jg.tracerImage(dos, 8*margeL + i * tailleReelJ1, 0, carteL, carteH);
			}
			i++;
		}
		if(jeu.joueurCourant()==1 && nbCartesJ1 !=0 && jeu.TourHumain()) {
			posCartes[i] = new Point(posCartes[i - 1].x + carteL, 0);
		}
	}

	private void dessineCartesCourantes(){
		//joueur 1
		Carte a = jeu.plateau().carteCourante(0);
		if(a != null) {
			jg.tracerImage(images[a.getFaction()][a.getPoid()],16*margeL,14*margeH,carteL,carteH);
		}
		jg.tracerImage(cadreCarte,16*margeL,14*margeH,carteL,carteH);
		jg.tracerTxt(jeu.getNom(0),16*margeL,hauteur-16*margeH);

		//joueur2
		Carte b = jeu.plateau().carteCourante(1);
		if(b != null) {
			jg.tracerImage(images[b.getFaction()][b.getPoid()],largeur - 18*margeL,14*margeH,carteL,carteH);
		}
		jg.tracerImage(cadreCarte,largeur - 18*margeL,14*margeH,carteL,carteH);
		jg.tracerTxt(jeu.getNom(1), largeur - 18*margeL,hauteur-16*margeH);

		//zone de drop
		if (jeu.joueurCourant() == 0){
			xDropDeb = 16*margeL;
		}else {
			xDropDeb = largeur - 18*margeL;
		}
		yDropDeb = 14*margeH;
		xDropFin = xDropDeb + carteL;
		yDropFin = yDropDeb + carteH;
	}

	private void dessineDefausse(){
		List<Carte> def = jeu.plateau().defausse;
		if(def.size() >0){
			Carte c = def.get(def.size()-1);
			jg.tracerImage(images[c.getFaction()][c.getPoid()],largeur - 8*margeL,14*margeH,carteL,carteH);
		}
		jg.tracerTxt("Défausse",largeur - 8*margeL,hauteur-16*margeH);
	}

	private void dessinePartisans(){
		//joueur 1
		if(jeu.plateau().getPartisans(0).size() > 0) {
			jg.tracerImage(dos, largeur-10*margeL, hauteur - 13*margeH, carteL, carteH);
		}
		jg.tracerTxt("Partisans",largeur-10*margeL,hauteur-margeH);

		//joueur 2
		if(jeu.plateau().getPartisans(1).size() > 0) {
			jg.tracerImage(dos, largeur-10*margeL, 2*margeH, carteL, carteH);
		}
		jg.tracerTxt("Partisans",largeur-10*margeL,2*margeH);
	}

	private void dessineCarteScore(int joueur,int x,int y){
		List<Carte> cartes = jeu.plateau().getScore(joueur);
		int nbCartes = cartes.size();
		if(nbCartes > 0) {
			Carte c = cartes.get(0);
			jg.tracerImage(images[c.getFaction()][c.getPoid()], x, y, carteL, carteH);
		}
	}
	private void dessineScore(){
		int x1,y1,x2,y2;
		if(jeu.plateau().phase()==1){
			x1 = largeur - 5*margeL;
			x2 = x1;
			y2 = 2*margeH;
			y1 = hauteur - 13*margeH;
		}else{
			x1 = largeur - 8*margeL;
			x2 = x1;
			y2 = 2*margeH;
			y1 = hauteur - 13*margeH;
		}
		//joueur 1
		dessineCarteScore(0,x1,y1);
		jg.tracerTxt("Score",x1,hauteur-margeH);

		//joueur 2
		dessineCarteScore(1,x2,y2);
		jg.tracerTxt("Score",x2,2*margeH);
	}

	private void dessineAgagner(){
		Carte aGagner = jeu.plateau().carteAJouer();
		if(aGagner != null) {
			jg.tracerImage(images[aGagner.getFaction()][aGagner.getPoid()], 6*margeL, 14*margeH, carteL, carteH);
		}
	}

	private void dessinePioche(){
		if(jeu.plateau().pioche.size() > 0) {
			jg.tracerImage(dos, margeL, 14*margeH, carteL, carteH);
			jg.tracerTxt("Pioche",margeL,26*margeH);
		}
	}

	private void dessineJoueur(){
		Color cJ1,cJ2;
		if(jeu.joueurCourant() == 0){
			cJ1 = Color.YELLOW;
			cJ2 = Color.lightGray;
		}else{
			cJ2 = Color.YELLOW;
			cJ1 = Color.lightGray;
		}

		/* dessin du Joueur 1*/
		jg.tracerRond(1*margeL,hauteur-12*margeH,5*margeL,9*margeH,cJ1);
		jg.tracerImage(j1,2*margeL,hauteur-11*margeH,3*margeL,5*margeH);
		jg.tracerTxt(jeu.getNom(0), 2*margeL,hauteur-5*margeH);

		/* dessin du Joueur 2*/
		jg.tracerRond(1*margeL,2*margeH,5*margeL,9*margeH,cJ2);
		jg.tracerImage(j2,2*margeL,3*margeH,3*margeL,5*margeH);
		jg.tracerTxt(jeu.getNom(1),2*margeL,9*margeH);

	}


	/*
	public void tracerPartieV() {
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
			if(jeu.combatPret()){
				if(!combatAnim) {
					estEnAnim = true;
					combatAnim = true;
					joueurWin = jeu.plateau().quiGagneCombat();
					anim = jeu.plateau().carteAJouer();
					//xCarteWin
				}
				dessinePioche(0, hauteur / 3, largeur / 3, 2 * hauteur / 3);
				if(joueurWin == 0){
					xCarteWin = (xCarteWin+vx<destxJ1)?xCarteWin+vx:destxJ1;
					yCarteWin = (xCarteWin+vx<destxJ1)?xCarteWin+vx:destxJ1;
					xPioche = (xCarteWin+vx<destxJ1)?xCarteWin+vx:destxJ1;
					yPioche = (xCarteWin+vx<destxJ1)?xCarteWin+vx:destxJ1;
					//jg.tracerImage(images[anim.getFaction()][anim.getPoid()],xCarteWin);
					if(destxJ1 == xCarteWin && destyJ1 == yCarteWin && destxJ2 == xPioche && destyJ2 == yPioche){
						estEnAnim = false;
					}
				}else{
					if(destxJ2>xCarteWin+vx){
						estEnAnim = false;
					}
				}
			}else {
				combatAnim = false;
				dessinePioche(0, hauteur / 3, largeur / 3, 2 * hauteur / 3);
				dessineAgagner(0, hauteur / 3, largeur / 3, 2 * hauteur / 3);
			}
			dessinePartisansScore(1, 5 * largeur / 7, 0, largeur, hauteur / 3);
			dessinePartisansScore(0, 5 * largeur / 7, 2 * hauteur / 3, largeur, hauteur);
		}else{
			dessineScore(1, 5 * largeur / 7, 0, largeur, hauteur / 3);
			dessineScore(0, 5 * largeur / 7, 2 * hauteur / 3, largeur, hauteur);
		}
	}

	private void dessineScore(int joueur,int x1,int y1,int x2,int y2){
		int largeur = x2-x1;
		int hauteur = y2-y1;

		int margeL = largeur/7;
		int margeH = hauteur/20;

		dessineCarteScore(joueur,x1+2*margeL,y1+joueur*2*margeH,3*margeL,hauteur-3*margeH);
		if(joueur == 0){
			jg.tracerTxt("Score",x1+2*margeL,y2-margeH);
		}else{
			jg.tracerTxt("Score",x1+2*margeL,y1+2*margeH/3);
		}
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
	private void dessineAgagner(int x1,int y1,int x2,int y2){
		int largeur = x2-x1;
		int hauteur = y2-y1;

		int margeL = largeur/20;
		int margeH = hauteur/20;
		int l = 15*largeur/40;
		int h = hauteur - 2*margeH;

		Carte aGagner = jeu.plateau().carteAJouer();
		if(aGagner != null) {
			jg.tracerImage(images[aGagner.getFaction()][aGagner.getPoid()], x1 + 3 * margeL + l, y1, l, h);
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
				if(dragAndDrop != null && c.equals(dragAndDrop)){
					jg.tracerImage(images[c.getFaction()][c.getPoid()], dragX, dragY, tailleCarte, hauteurCarte);
				}else {
					jg.tracerImage(images[c.getFaction()][c.getPoid()], x1 + i * tailleReel, y1 + coef * margeH, tailleCarte, hauteurCarte);
				}
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
			Carte c = cartes.get(0);
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
			destxJ1 = x1 + 2 * margeL;
			destyJ1 =  y1;
			jg.tracerTxt("Partisans",x1+2*margeL,y1+hauteur-2*margeH);
			dessineCarteScore(joueur,x1+11*margeL,y1,8*largeur/20,hauteur-3*margeH);
			jg.tracerTxt("Score",11*margeL+x1,y1+hauteur-2*margeH);
		}else{
			if(jeu.plateau().getPartisans(joueur).size() > 0) {
				jg.tracerImage(dos, x1 + 2 * margeL, 3 * margeH + y1, 8 * largeur / 20, hauteur - 3 * margeH);
			}
			destxJ2 = x1 + 2 * margeL;
			destyJ2 =  3 * margeH + y1;
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
		if (jeu.joueurCourant() == 0){
			xDropDeb = x1+margeL;
		}else {
			xDropDeb = x1 + 11 * margeL;
		}
		yDropDeb = y1+margeH;
		xDropFin = xDropDeb + 8*margeL;
		yDropFin = yDropDeb + 17*margeH;

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
	}*/
	
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
				if (x >= posCartes[i].x && x < posCartes[i + 1].x && y >= posCartes[i].y && y <= posCartes[i].y + carteH) {
					dragAndDrop = jeu.plateau().cartePosMain(i, jeu.joueurCourant());
					dragX = x;
					dragY = y;
					return dragAndDrop;
				}
				i++;
			}
		}
		return null;
	}

	public boolean okDrop(int x,int y){
		dragX = x;
		dragY = y;
		if(x>=xDropDeb && x<= xDropFin && y>=yDropDeb && y<=yDropFin){
			return true;
		}
		return false;
	}

	public void estRelease(){
		dragAndDrop = null;
	}
}
