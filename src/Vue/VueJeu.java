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

	public void tracerPartieV() {
		Plateau plateau = jeu.plateau();
		int largeur = jg.largeur();
		int hauteur = jg.hauteur();

		dessineJoueur(1,0,0,largeur/5,hauteur/3);
		dessineJoueur(0,0,2*hauteur/3,largeur/5,hauteur);

		dessinePioche(0,hauteur/3,largeur/3,2*hauteur/3);
		dessineMain(1,largeur/5,0,4*largeur/5,hauteur/3);
		dessineMain(0,largeur/5,2*hauteur/3,4*largeur/5,hauteur);
		dessinePartisansScore(1,4*largeur/5,0,largeur,hauteur/3);
		dessinePartisansScore(0,4*largeur/5,2*hauteur/3,largeur,hauteur);
		dessineCartesCourantes(largeur/3,hauteur/3,2*largeur/3,2*hauteur/3);
		dessineDefausse(2*largeur/3,hauteur/3,largeur,2*hauteur/3);
	}

	//dessine les perso entre les points
	private void dessineJoueur(int joueur,int x1,int y1,int x2,int y2){
		int largeur = x2-x1;
		int hauteur = y2-y1;

		if(largeur<=0 || hauteur <=0){
			System.out.println("Largeur ou hauteur a 0 dans VueJeu");
		}

		Color c;
		if(joueur == jeu.joueurCourant()){
			c = Color.YELLOW;
		}else{
			c = Color.lightGray;
		}

		int margeL = largeur/10;
		int margeH = hauteur/10;
		System.out.println("Joueur "+jeu.joueurCourant());
		jg.tracerRond(x1+margeL,y1+margeH,largeur-margeL,hauteur-2*margeH,c);
	}
	private void dessinePioche(int x1,int y1,int x2,int y2){}
	private void dessineMain(int joueur,int x1,int y1,int x2,int y2){}
	private void dessinePartisansScore(int joueur,int x1,int y1,int x2,int y2){}
	private void dessineCartesCourantes(int x1,int y1,int x2,int y2){}
	private void dessineDefausse(int x1,int y1,int x2,int y2){}

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
