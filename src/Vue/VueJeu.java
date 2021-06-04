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

	boolean combatAnim,combatAnimPioche;
	int xCarteWin,yCarteWin,xPioche,yPioche;
	int vx,vy,vxPioche,vyPioche;
	int versPartisansJ1x,versPartisansJ1y,versPartisansJ0x,versPartisansJ0y;
	int depuisxPioche,depuisyPioche,depuisxCarte,depuisyCarte;
	int joueurWin;

	boolean carteCouranteAnim;
	int xCC0,yCC0,xCC1,yCC1;
	int dxCC0,dyCC0,dxCC1,dyCC1;
	int vxCC0,vyCC0,vxCC1,vyCC1;
	int versxCC0,versyCC0,versxCC1,versyCC1;

	Carte dragAndDrop;
	int xDropDeb,yDropDeb,xDropFin,yDropFin;
	int dragX,dragY;
	boolean estSurZoneDrop;

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
		dessineDefausse();
		dessineScore();
		dessineCartesCourantes();
		dessineMains();


		if(plateau.phase()==1) {
			dessinePioche();
			dessinePartisans();
			dessineAgagner();
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
		dxCC0 = 16 * margeL;
		dyCC0 = 14 * margeH;
		dxCC1 = largeur - 18 * margeL;
		dyCC1 = 14 * margeH;
		if(jeu.plateau().combatPret()) {
			if(!carteCouranteAnim){
				carteCouranteAnim = true;
				joueurWin = jeu.plateau().quiGagneCombat();
				xCC0 = dxCC0;
				yCC0 = dyCC0;
				xCC1 = dxCC1;
				yCC1 = dyCC1;
				vxCC0 = (versxCC0 - dxCC0)/15;
				vyCC0 = (versyCC0 - dyCC0)/15;
				vxCC1 = (versxCC1 - dxCC1)/15;
				vyCC1 = (versyCC1 - dyCC1)/15;
			}
			xCC0 = (xCC0 + vxCC0 > versxCC0)?versxCC0:xCC0 + vxCC0;
			if(vyCC0<0){
				yCC0 = (yCC0 + vyCC0 < versyCC0)?versyCC0:yCC0 + vyCC0;
			}else{
				yCC0 = (yCC0 + vyCC0 >= versyCC0)?versyCC0:yCC0 + vyCC0;
			}
			xCC1 = (xCC1 + vxCC1 > versxCC1)?versxCC1:xCC1 + vxCC1;
			if(vyCC1<0){
				yCC1 = (yCC1 + vyCC1 < versyCC1)?versyCC1:yCC1 + vyCC1;
			}else{
				yCC1 = (yCC1 + vyCC1 >= versyCC1)?versyCC1:yCC1 + vyCC1;
			}
		}else{
			carteCouranteAnim = false;
			xCC0 = dxCC0;
			yCC0 = dyCC0;
			xCC1 = dxCC1;
			yCC1 = dyCC1;
		}

		//joueur 1
		Carte a = jeu.plateau().carteCourante(0);
		if (a != null) {
			jg.tracerImage(images[a.getFaction()][a.getPoid()], xCC0, yCC0, carteL, carteH);
		}
		//joueur2
		Carte b = jeu.plateau().carteCourante(1);
		if (b != null) {
			jg.tracerImage(images[b.getFaction()][b.getPoid()], xCC1, yCC1, carteL, carteH);
		}

		//j1
		jg.tracerImage(cadreCarte,dxCC0,dyCC0,carteL,carteH);
		jg.tracerTxt(jeu.getNom(0),dxCC0,hauteur-16*margeH);
		//j2
		jg.tracerImage(cadreCarte,dxCC1,dyCC1,carteL,carteH);
		jg.tracerTxt(jeu.getNom(1), dxCC1,hauteur-16*margeH);

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
		if(jeu.plateau().phase()==1) {
			versyCC0 = 14 * margeH;
			versyCC1 = versyCC0;
			versxCC0 = largeur - 8 * margeL;
			versxCC1 = versxCC0;
		}
		if(def.size() >0){
			Carte c = def.get(def.size()-1);
			jg.tracerImage(images[c.getFaction()][c.getPoid()],largeur - 8*margeL,14*margeH,carteL,carteH);
		}
		jg.tracerTxt("Défausse",largeur - 8*margeL,hauteur-16*margeH);
	}

	private void dessinePartisans(){
		//joueur 1
		versPartisansJ0x = largeur-10*margeL;
		versPartisansJ0y = hauteur - 13*margeH;
		if(jeu.plateau().getPartisans(0).size() > 0) {
			jg.tracerImage(dos, versPartisansJ0x, versPartisansJ0y, carteL, carteH);
		}
		jg.tracerTxt("Partisans",largeur-10*margeL,hauteur-margeH);

		//joueur 2
		versPartisansJ1x = largeur-10*margeL;
		versPartisansJ1y = 2*margeH;
		if(jeu.plateau().getPartisans(1).size() > 0) {
			jg.tracerImage(dos, versPartisansJ1x, versPartisansJ1y, carteL, carteH);
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
			if(jeu.plateau().combatPret()) {
				if (jeu.plateau().carteCourante(0).getFaction() == jeu.plateau().MORTSVIVANTS) {
					if(jeu.plateau().quiGagneCombat() == 0) {
						versxCC0 = x1;
						versyCC0 = y1;
					}else{
						versxCC0 = x2;
						versyCC0 = y2;
					}
				}
				if (jeu.plateau().carteCourante(1).getFaction() == jeu.plateau().MORTSVIVANTS) {
					if(jeu.plateau().quiGagneCombat() == 1) {
						versxCC1 = x2;
						versyCC1 = y2;
					}else{
						versxCC1 = x1;
						versyCC1 = y1;
					}
				}
			}
		}else{
			x1 = largeur - 8*margeL;
			x2 = x1;
			y2 = 2*margeH;
			y1 = hauteur - 13*margeH;
			if(jeu.plateau().combatPret()) {
				if (jeu.plateau().carteCourante(0).getFaction() == jeu.plateau().NAINS) {
					if(jeu.plateau().quiGagneCombat() == 1) {
						versxCC0 = x1;
						versyCC0 = y1;
					}else{
						versxCC0 = x2;
						versyCC0 = y2;
					}
				}else{
					if(jeu.plateau().quiGagneCombat() == 0) {
						versxCC0 = x1;
						versyCC0 = y1;
					}else{
						versxCC0 = x2;
						versyCC0 = y2;
					}
				}
				if (jeu.plateau().carteCourante(1).getFaction() == jeu.plateau().NAINS) {
					if(jeu.plateau().quiGagneCombat() == 0) {
						versxCC1 = x2;
						versyCC1 = y2;
					}else{
						versxCC1 = x1;
						versyCC1 = y1;
					}
				}else{
					if(jeu.plateau().quiGagneCombat() == 1) {
						versxCC1 = x2;
						versyCC1 = y2;
					}else{
						versxCC1 = x1;
						versyCC1 = y1;
					}
				}
			}
		}
		//joueur 1
		dessineCarteScore(0,x1,y1);
		jg.tracerTxt("Score",x1,hauteur-margeH);

		//joueur 2
		dessineCarteScore(1,x2,y2);
		jg.tracerTxt("Score",x2,2*margeH);
	}

	private void dessineAgagner(){
		depuisxCarte = 6*margeL;
		depuisyCarte = 14*margeH;

		if(jeu.plateau().combatPret()){
			if(!combatAnim){
				combatAnim = true;
				joueurWin = jeu.plateau().quiGagneCombat();
				xCarteWin = depuisxCarte;
				yCarteWin = depuisyCarte;
				vx = (versPartisansJ0x - depuisxCarte)/15;
				vy = (versPartisansJ0y - depuisyCarte)/15;
			}
			if(joueurWin == 0){
				xCarteWin = (xCarteWin+vx>versPartisansJ0x)?versPartisansJ0x:xCarteWin+vx;
				yCarteWin = (yCarteWin+vy>versPartisansJ0y)?versPartisansJ0y:yCarteWin+vy;
			}else{
				xCarteWin = (xCarteWin+vx>versPartisansJ1x)?versPartisansJ1x:xCarteWin+vx;
				yCarteWin = (yCarteWin-vy<versPartisansJ1y)?versPartisansJ1y:yCarteWin-vy;
			}
		}else{
			combatAnim = false;
			xCarteWin = depuisxCarte;
			yCarteWin = depuisyCarte;
		}
		Carte aGagner = jeu.plateau().carteAJouer();
		if(aGagner != null) {
			jg.tracerImage(images[aGagner.getFaction()][aGagner.getPoid()], xCarteWin, yCarteWin, carteL, carteH);
		}
	}

	private void dessinePioche(){
		depuisxPioche = margeL;
		depuisyPioche = 14*margeH;
		if(jeu.plateau().combatPret()){
			if(!combatAnimPioche){
				combatAnimPioche = true;
				joueurWin = jeu.plateau().quiGagneCombat();
				xPioche = depuisxPioche;
				yPioche = depuisyPioche;
				vxPioche = (versPartisansJ0x - depuisxPioche)/15;
				vyPioche = (versPartisansJ0y - depuisyPioche)/15;
			}
			if(joueurWin == 0){
				xPioche = (xPioche+vxPioche>versPartisansJ1x)?versPartisansJ1x:xPioche+vxPioche;
				yPioche = (yPioche-vyPioche<versPartisansJ1y)?versPartisansJ1y:yPioche-vyPioche;
			}else{
				xPioche = (xPioche+vxPioche>versPartisansJ0x)?versPartisansJ0x:xPioche+vxPioche;
				yPioche = (yPioche+vyPioche>versPartisansJ0y)?versPartisansJ0y:yPioche+vyPioche;
			}
			if(jeu.plateau().pioche.size() > 1){
				jg.tracerImage(dos, depuisxPioche, depuisyPioche, carteL, carteH);
			}
		}else{
			combatAnimPioche = false;
			xPioche = depuisxPioche;
			yPioche = depuisyPioche;
		}
		if(jeu.plateau().pioche.size() > 0) {
			jg.tracerImage(dos, xPioche, yPioche, carteL, carteH);
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

	
	public int joueurCourant() {
		return joueurCourant;
	}
	
	public Carte determinerCarte(int x, int y) {
		if(jeu.TourHumain() && jeu.plateau().carteCourante(jeu.joueurCourant()) == null) {
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
			estSurZoneDrop = true;
			return true;
		}
		estSurZoneDrop = false;
		return false;
	}

	public void estRelease(){
		dragAndDrop = null;
	}

	public boolean estCarteOk(){
		return dragAndDrop != null;
	}

	public boolean estSurZoneDrop(){
		return estSurZoneDrop;
	}
}
