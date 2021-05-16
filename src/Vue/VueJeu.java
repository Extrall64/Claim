package Vue;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import Global.Configuration;
import Modele.Jeu;
import Modele.Niveau;
import Structures.Iterateur;
import Structures.Sequence;

public class VueJeu {
	public static final int nbCaseH = 12;
	public static final int nbCaseL = 26;
	int largeurCase, hauteurCase,joueurCourant;
	Jeu jeu;
	ImageClaim images[][]; //ex images[0][1] image gobelin de force 1
	ImageClaim dos;
	JeuGraphique jg;

	private ImageClaim chargeImage(String nom) {
		ImageClaim img = null;
		InputStream in = Configuration.charge("carte_JPEG" + File.separator + nom + ".jpg");
		return ImageClaim.getImageClaim(in);
	}

	public VueJeu(Jeu j, JeuGraphique n) {
		images = new ImageClaim[5][10];
		for(int i=0;i<10;i++) {
			images[Jeu.NAINS][i] = chargeImage("nain_"+i);
			images[Jeu.MORTSVIVANTS][i] = chargeImage("mort-vivant_"+i);
			images[Jeu.DOPPELGANGERS][i] = chargeImage("doppel_"+i);
			images[Jeu.GLOBELINS][i] = chargeImage("gobelin_"+i);
			if(i>=2) {
				images[Jeu.CHEVALIERS][i] = chargeImage("chevalier_"+i);
			}
		}
		dos = chargeImage("carte_dos");
		jeu = j;
		jg = n;
	}

	void tracerNiveau() {
		if(!jeu.estSurMenu()) {
			tracerPartie();
		}
	}
	
	public void tracerPartie() {
		Niveau niv = jeu.niveau();
		largeurCase = jg.largeur() / nbCaseL;
		hauteurCase = jg.hauteur() / nbCaseH;
		//largeurCase = Math.min(largeurCase, hauteurCase);
		//hauteurCase = largeurCase;
		joueurCourant = niv.joueurCourant();
		
		Sequence<Integer> main = niv.getMain(niv.joueurCourant());
		Iterateur<Integer> imain = main.iterateur();
		int nbCarte = main.taille();
		int debut = (13-nbCarte)/2 * 2;
		int h = nbCaseH - 3;
		while(imain.aProchain()) {
			int x = imain.prochain();
			int poid = niv.poid(x);
			int faction = niv.faction(x);
			jg.tracerImage(images[faction][poid], debut * largeurCase, h * hauteurCase, largeurCase*2, hauteurCase*3);
			debut = debut + 2;
		}
		
		if(niv.phase() == 1) {
			jg.tracerImage(dos,largeurCase, hauteurCase, largeurCase*4, hauteurCase*6);
			int x = niv.carteAJouer();
			if(niv.carteAJouer() == -1) {System.out.println(""+niv.phase());System.exit(0);}
			int poid = niv.poid(x);
			int faction = niv.faction(x);
			jg.tracerImage(images[faction][poid],largeurCase * 6, hauteurCase, largeurCase*4, hauteurCase*6);
		}
		
		int x = niv.carteCourante(0);
		if(x != -1) {
			int poid = niv.poid(x);
			int faction = niv.faction(x);
			jg.tracerImage(images[faction][poid],largeurCase * 13, hauteurCase, largeurCase*4, hauteurCase*6);
		}
		x =	niv.carteCourante(1);
		if(x != -1) {
			int poid = niv.poid(x);
			int faction = niv.faction(x);
			jg.tracerImage(images[faction][poid],largeurCase * 19, hauteurCase, largeurCase*4, hauteurCase*6);
		}
	}
	
	public int largeurCase() {
		return largeurCase;
	}
	
	public int hauteurCase() {
		return hauteurCase;
	}
	
	public int joueurCourant() {
		return joueurCourant;
	}
	
	public int determinerCarte(int l, int c) {
		if(!jeu.estSurMenu()) {
			Niveau niv = jeu.niveau();
			Sequence<Integer> main = niv.getMain(niv.joueurCourant());
			int nbCarte = main.taille();
			int debut = (13-nbCarte)/2 * 2;
			int h = nbCaseH - 3;
			if(l >= h && c < debut + 2*nbCarte && c >= debut) {
				int posCarte = c/2 - debut/2;
				return niv.cartePosMain(posCarte,joueurCourant);
			}
		}
		return -1;
	}
	
}
