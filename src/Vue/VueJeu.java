package Vue;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import Global.Configuration;
import Modele.Carte;
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
			images[Niveau.NAINS][i] = chargeImage("nain_"+i);
			images[Niveau.MORTSVIVANTS][i] = chargeImage("mort-vivant_"+i);
			images[Niveau.DOPPELGANGERS][i] = chargeImage("doppel_"+i);
			images[Niveau.GLOBELINS][i] = chargeImage("gobelin_"+i);
			if(i>=2) {
				images[Niveau.CHEVALIERS][i] = chargeImage("chevalier_"+i);
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
		
		List<Carte> main = niv.getMain(joueurCourant);
		int nbCarte = main.size();
		int debut = (13-nbCarte)/2 * 2;
		int h = nbCaseH - 3;
		for(Carte c: main) {
			jg.tracerImage(images[c.getFaction()][c.getPoid()], debut * largeurCase, h * hauteurCase, largeurCase*2, hauteurCase*3);
			debut = debut + 2;
		}
		
		if(niv.phase() == 1) {
			jg.tracerImage(dos,largeurCase, hauteurCase, largeurCase*4, hauteurCase*6);
			Carte x = niv.carteAJouer();
			if(niv.carteAJouer() == null) {System.out.println(""+ niv.phase());System.exit(0);}
			jg.tracerImage(images[x.getFaction()][x.getPoid()],largeurCase * 6, hauteurCase, largeurCase*4, hauteurCase*6);
		}
		
		Carte a = niv.carteCourante(0);
		if(a != null) {
			jg.tracerImage(images[a.getFaction()][a.getPoid()],largeurCase * 13, hauteurCase, largeurCase*4, hauteurCase*6);
		}
		Carte b = niv.carteCourante(1);
		if(b != null) {
			jg.tracerImage(images[b.getFaction()][b.getPoid()],largeurCase * 19, hauteurCase, largeurCase*4, hauteurCase*6);
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
	
	public Carte determinerCarte(int l, int c) {
		if(!jeu.estSurMenu()) {
			Niveau niv = jeu.niveau();
			List<Carte> main = niv.getMain(joueurCourant);
			int nbCarte = main.size();
			int debut = (13-nbCarte)/2 * 2;
			int h = nbCaseH - 3;
			if(l >= h && c < debut + 2*nbCarte && c >= debut) {
				int posCarte = c/2 - debut/2;
				return niv.cartePosMain(posCarte,joueurCourant);
			}
		}
		return null;

	}
	
}
