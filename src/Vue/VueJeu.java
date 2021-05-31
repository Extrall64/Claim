package Vue;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import Global.Configuration;
import Modele.Carte;
import Modele.Jeu;
import Modele.Plateau;

public class VueJeu {
	public static final int nbCaseH = 12;
	public static final int nbCaseL = 26;
	int largeurCase, hauteurCase,joueurCourant;
	Jeu jeu;
	ImageClaim images[][]; //ex images[0][1] image gobelin de force 1
	ImageClaim dos;
	ImageClaim fond;
	JeuGraphique jg;

	private ImageClaim chargeCartes(String nom) {
		InputStream in = Configuration.charge("carte" + File.separator + nom + ".jpg");
		return ImageClaim.getImageClaim(in);
	}
	
	private ImageClaim chargeImage(String nom) {
		InputStream in = Configuration.charge("Image" + File.separator + nom + ".jpg");
		return ImageClaim.getImageClaim(in);
	}

	public VueJeu(Jeu j, JeuGraphique n) {
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
	}

	void tracerNiveau() {
		jg.tracerFond(fond);
		tracerPartie();
	}
	
	public void tracerPartie() {
		Plateau plateau = jeu.plateau();
		largeurCase = jg.largeur() / nbCaseL;
		hauteurCase = jg.hauteur() / nbCaseH;
		//largeurCase = Math.min(largeurCase, hauteurCase);
		//hauteurCase = largeurCase;
		joueurCourant = plateau.joueurCourant();
		
		List<Carte> main = plateau.getMain(joueurCourant);
		Collections.sort(main);
		int nbCarte = main.size();
		int debut = (13-nbCarte)/2 * 2;
		int h = nbCaseH - 3;
		for(Carte c: main) {
			jg.tracerImage(images[c.getFaction()][c.getPoid()], debut * largeurCase, h * hauteurCase, largeurCase*2, hauteurCase*3);
			debut = debut + 2;
		}
		
		if(plateau.phase() == 1) {
			jg.tracerImage(dos,largeurCase, hauteurCase, largeurCase*4, hauteurCase*6);
			Carte x = plateau.carteAJouer();
			if(plateau.carteAJouer() == null) {System.out.println(""+ plateau.phase());System.exit(0);}
			jg.tracerImage(images[x.getFaction()][x.getPoid()],largeurCase * 6, hauteurCase, largeurCase*4, hauteurCase*6);
		}
		
		Carte a = plateau.carteCourante(0);
		if(a != null) {
			jg.tracerImage(images[a.getFaction()][a.getPoid()],largeurCase * 13, hauteurCase, largeurCase*4, hauteurCase*6);
		}
		Carte b = plateau.carteCourante(1);
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
		
			Plateau plateau = jeu.plateau();
			List<Carte> main = plateau.getMain(joueurCourant);
			int nbCarte = main.size();
			int debut = (13-nbCarte)/2 * 2;
			int h = nbCaseH - 3;
			if(l >= h && c < debut + 2*nbCarte && c >= debut) {
				int posCarte = c/2 - debut/2;
				return plateau.cartePosMain(posCarte,joueurCourant);
			}
		
		return null;

	}
	
}
