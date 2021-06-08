package Vue;

import java.awt.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import Modele.Carte;
import Modele.Jeu;
import Modele.Plateau;

public class VueJeu {
	static int TMPANIM = 20;

	Point[] posCartes;
	Jeu jeu;
	ImageClaim[][] images; //ex images[0][1] image gobelin de force 1
	ImageClaim dos;
	ImageClaim fond;
	ImageClaim j1,j2;
	ImageClaim cadreCarte;
	JeuGraphique jg;
	Font f,f1;

	int[] cartesJ0;
	int[] cartesJ1;
	String[] nomFact,nomFactCourt;

	int hauteur,largeur;
	int carteL,carteH;
	int margeL,margeH;

	boolean combatAnim,combatAnimPioche, carteCouranteAnim,piocheAnim;
	int joueurWin;

	int comptAvtAnim;
	Point versPartisansJ0, versPartisansJ1;
	Point depCarte;
	Point pioche, depPioche;
	Point carteWin;
	Point cc0, cc1, depCC0, depCC1, versCC0, versCC1;
	Point drag, dropDeb, dropFin;
	Point destNvlPioche;
	
	Carte dragAndDrop;
	boolean estSurZoneDrop;
	final static double PRECISION = 3.5;

	// fonction de tweeing lineaire pour les animations
	private void interpolation(Point dep, Point arr) {
		dep.y = (int) (dep.y + (arr.y - dep.y) / 3.5);
		dep.x = (int) (dep.x + (arr.x - dep.x) / 3.5);
		if (Math.abs(dep.x - arr.x) < PRECISION) dep.x = arr.x;
		if (Math.abs(dep.y - arr.y) < PRECISION) dep.y = arr.y;

	}
	private ImageClaim chargeCartes(String nom) {
//		/InputStream in = Configuration.charge("carte" + File.separator + nom + ".jpg");
		return ImageClaim.getImageClaim("carte/" + nom + ".jpg");
	}
	
	private ImageClaim chargeImage(String nom) {
		return ImageClaim.getImageClaim("Image/"+ nom + ".jpg");
	}

	public VueJeu(Jeu j, JeuGraphique n) {
		comptAvtAnim = TMPANIM;
		cartesJ0 = new int[5];
		cartesJ1 = new int[5];
		nomFact = new String[5];
		nomFactCourt = new String[5];
		nomFact[0] = "Gobelins";
		nomFact[1] = "Nains";
		nomFact[2] = "Mort-vivants";
		nomFact[3] = "Doppelgangers";
		nomFact[4] = "Chevaliers";
		nomFactCourt[0] = "Gobe.";
		nomFactCourt[1] = "Nain.";
		nomFactCourt[2] = "Mort.";
		nomFactCourt[3] = "Dopp.";
		nomFactCourt[4] = "Chev.";
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
		cadreCarte = ImageClaim.getImageClaim("Image/Fond_carte.png");
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

		//f1 = new Font("Comic Sans MS", Font.ITALIC | Font.BOLD, (min(2*margeL/3,5*margeH/4)));

		dessineJoueur();
		if(plateau.phase()==1) {
			dessineDefausse();
		}
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
		if(jeu.TourHumain()){
			posCartes = new Point[14];
		}
		//joueur 1
		List<Carte> mainJ0 = jeu.getJoueur(0).getMain();
		Collections.sort(mainJ0);
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
					jg.tracerImage(cadreCarte,15 * margeL,13 * margeH,carteL+2*margeL,carteH+4*margeH);
					jg.tracerImage(images[c.getFaction()][c.getPoid()], drag.x, drag.y, carteL, carteH);
				}else {
					jg.tracerImage(images[c.getFaction()][c.getPoid()],  8*margeL + i * tailleReelJ0, hauteur-carteH, carteL, carteH);
					if(jeu.joueurCourant() == 0 && (!jeu.plateau().carteJouable(c)||jeu.plateau().carteCourante(0)!=null)
							|| jeu.estHumVsIA() && jeu.joueurCourant()==1){
						jg.grise(8*margeL + i * tailleReelJ0,hauteur-carteH,carteL,carteH);
					}
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
		List<Carte> mainJ1 = jeu.getJoueur(1).getMain();
		Collections.sort(mainJ1);
		int nbCartesJ1 = mainJ1.size();
		int tailleReelJ1 = 20*margeL/(nbCartesJ1+1);
		tailleReelJ1 = (tailleReelJ1 > carteL)?carteL:tailleReelJ1;
		i = 0;
		while (i<nbCartesJ1) {
			c = mainJ1.get(i);
			//face
			if (jeu.joueurCourant() == 1 && jeu.TourHumain() || jeu.estIAVsIA()) {
				if(dragAndDrop != null && c.equals(dragAndDrop)){
					jg.tracerImage(cadreCarte,largeur - 19 * margeL,13 * margeH,carteL+2*margeL,carteH+4*margeH);
					jg.tracerImage(images[c.getFaction()][c.getPoid()], drag.x, drag.y, carteL, carteH);
				}else {
					jg.tracerImage(images[c.getFaction()][c.getPoid()],  8*margeL + i * tailleReelJ1, 0, carteL, carteH);
					if(jeu.joueurCourant() == 1 && (!jeu.plateau().carteJouable(c)||jeu.plateau().carteCourante(1)!=null)){
						jg.grise(8*margeL + i * tailleReelJ1,0,carteL,carteH);
					}
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
		depCC0 = new Point(16 * margeL, 14 * margeH);
		depCC1 = new Point( largeur - 18 * margeL, 14 * margeH);
		if(jeu.plateau().combatPret()) {
			if(comptAvtAnim == 0) {
				if (!carteCouranteAnim) {
					carteCouranteAnim = true;
					joueurWin = jeu.plateau().quiGagneCombat();
					cc0 = new Point(depCC0.x, depCC0.y);
					cc1 = new Point(depCC1.x, depCC1.y);
				}
				interpolation(cc0, versCC0);
				interpolation(cc1, versCC1);
			}else{
				comptAvtAnim--;
				cc0 = new Point(depCC0.x ,depCC0.y);
				cc1 = new Point(depCC1.x ,depCC1.y);
			}
		}else{
			comptAvtAnim = TMPANIM;
			carteCouranteAnim = false;
			cc0 = new Point(depCC0.x ,depCC0.y);
			cc1 = new Point(depCC1.x ,depCC1.y);
		}

		//joueur 1
		Carte a = jeu.plateau().carteCourante(0);
		if (a != null) {
			jg.tracerImage(images[a.getFaction()][a.getPoid()], cc0.x, cc0.y, carteL, carteH);
		}
		//joueur2
		Carte b = jeu.plateau().carteCourante(1);
		if (b != null) {
			jg.tracerImage(images[b.getFaction()][b.getPoid()], cc1.x, cc1.y, carteL, carteH);
		}

		//j1
		jg.tracerImage(cadreCarte, depCC0.x-margeL/10,depCC0.y-margeH/6,carteL+margeL/5,carteH+margeH/3);
		jg.tracerTxt(jeu.getJoueur(0).getNom(),depCC0.x,hauteur-17*margeH,f);
		//j2
		jg.tracerImage(cadreCarte,depCC1.x-margeL/10,depCC1.y-margeH/6,carteL+margeL/5,carteH+margeH/3);
		jg.tracerTxt(jeu.getJoueur(1).getNom(), depCC1.x,hauteur-17*margeH,f);

		//zone de drop
		int xDropDeb;
		if (jeu.joueurCourant() == 0){
			xDropDeb = 15*margeL;
		}else {
			xDropDeb = largeur - 19 * margeL;
		}
		dropDeb = new Point (xDropDeb, 13*margeH);
		dropFin = new Point( dropDeb.x + carteL + 2*margeL, dropDeb.y + carteH + 4*margeH );
	}

	private void dessineDefausse(){
		List<Carte> def = jeu.plateau().defausse;
		if(jeu.plateau().phase()==1) {
			versCC0 = new Point(largeur - 8 * margeL, 14 * margeH);
			versCC1 = new Point(versCC0.x, versCC0.y);
		}
		if(def.size() >0){
			Carte c = def.get(def.size()-1);
			jg.tracerImage(images[c.getFaction()][c.getPoid()],largeur - 8*margeL,14*margeH,carteL,carteH);
		}
		jg.tracerTxt("Défausse",largeur - 8*margeL,hauteur-16*margeH,f);
	}

	private void dessinePartisans(){
		//joueur 1
		versPartisansJ0 = new Point (largeur-10*margeL, hauteur - 13*margeH);
		if(jeu.plateau().getPartisans(0).size() > 0) {
			jg.tracerImage(dos, versPartisansJ0.x, versPartisansJ0.y, carteL, carteH);
		}
		jg.tracerTxt("Partisans",largeur-10*margeL,hauteur-margeH,f);

		//joueur 2
		versPartisansJ1= new Point(largeur-10*margeL, 2*margeH);
		if(jeu.plateau().getPartisans(1).size() > 0) {
			jg.tracerImage(dos, versPartisansJ1.x, versPartisansJ1.y, carteL, carteH);
		}
		jg.tracerTxt("Partisans",largeur-10*margeL,2*margeH - margeH/2,f);
	}

	private void metAJourCartesScore(){
		for(int i=0;i<5;i++){
			cartesJ0[i]=0;
			cartesJ1[i]=0;
		}

		Carte c;
		List<Carte> l = jeu.getJoueur(0).getScore();
		Iterator<Carte> it = l.iterator();
		while(it.hasNext()){
			c = it.next();
			cartesJ0[c.getFaction()] += 1;
		}

		l = jeu.getJoueur(1).getScore();
		it = l.iterator();
		while(it.hasNext()){
			c = it.next();
			cartesJ1[c.getFaction()] += 1;
		}

	}

	private void afficheScore(){
		//J1
		jg.tracerImage(cadreCarte, largeur - 10*margeL -margeL/10, hauteur - 13*margeH-margeH/6,9*margeL,carteH+margeH/3);
		for(int i=0;i<5;i++){
			if(largeur>1200){
				jg.tracerTxt(nomFact[i]+": x "+cartesJ0[i],largeur - 5*margeL,hauteur - 13*margeH +(i+1)*2*margeH,f1);
			}else{
				jg.tracerTxt(nomFactCourt[i]+": x "+cartesJ0[i],largeur - 5*margeL,hauteur - 13*margeH +(i+1)*2*margeH,f1);
			}

		}

		//J2
		jg.tracerImage(cadreCarte, largeur - 10*margeL -margeL/10, 2*margeH-margeH/6,9*margeL,carteH+margeH/3);
		for(int i=0;i<5;i++){
			if(largeur>1200){
				jg.tracerTxt(nomFact[i]+": x "+cartesJ1[i],largeur - 5*margeL,2*margeH +(i+1)*2*margeH,f1);
			}else{
				jg.tracerTxt(nomFactCourt[i]+": x "+cartesJ1[i],largeur - 5*margeL,2*margeH +(i+1)*2*margeH,f1);
			}

		}
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
						versCC0 = new Point (x1, y1);
					}else{
						versCC0 = new Point (x2, y2);

					}
				}
				if (jeu.plateau().carteCourante(1).getFaction() == jeu.plateau().MORTSVIVANTS) {
					if(jeu.plateau().quiGagneCombat() == 1) {
						versCC1 = new Point (x2, y2);

					}else{
						versCC1 = new Point (x1, y1);
					}
				}
			}
		}else{
			x1 = largeur - 10*margeL;
			x2 = x1;
			y2 = 2*margeH;
			y1 = hauteur - 13*margeH;
			if(jeu.plateau().combatPret()) {
				if (jeu.plateau().carteCourante(0).getFaction() == jeu.plateau().NAINS) {
					if(jeu.plateau().quiGagneCombat() == 1) {
						versCC0 = new Point (x1, y1);

					}else{
						versCC0 = new Point (x2, y2);

					}
				}else{
					if(jeu.plateau().quiGagneCombat() == 0) {
						versCC0 = new Point (x1, y1);

					}else{
						versCC0 = new Point (x2, y2);

					}
				}
				if (jeu.plateau().carteCourante(1).getFaction() == jeu.plateau().NAINS) {
					if(jeu.plateau().quiGagneCombat() == 0) {
						versCC1 = new Point (x2, y2);

					}else{
						versCC1 = new Point (x1, y1);
					}
				}else{
					if(jeu.plateau().quiGagneCombat() == 1) {
						versCC1 = new Point (x2, y2);
					}else{
						versCC1 = new Point (x1, y1);
					}
				}
			}
			if(jeu.plateau().carteCourante(0)==null && jeu.plateau().carteCourante(1)==null){
				metAJourCartesScore();
			}
		}

		//joueur 1
		dessineCarteScore(0,x1,y1);
		jg.tracerTxt("Score",x1+2*margeL,hauteur-margeH,f);

		//joueur 2
		dessineCarteScore(1,x2,y2);
		jg.tracerTxt("Score",x2+2*margeL,2*margeH - margeH/2,f);

		if(jeu.plateau().phase()==2){
			afficheScore();
		}
	}

	private void dessineAgagner(){
		depCarte = new Point( 6*margeL, 14*margeH);

		if(jeu.plateau().combatPret()){
			if(!combatAnim){
				combatAnim = true;
				joueurWin = jeu.plateau().quiGagneCombat();
				carteWin = new Point (depCarte.x, depCarte.y);
			}
			if(joueurWin == 0){
				interpolation(carteWin, versPartisansJ0);
			}else{
				interpolation(carteWin, versPartisansJ1);
			}
		}else{
			combatAnim = false;
			if(jeu.plateau().carteCourante(0)==null && jeu.plateau().carteCourante(1)==null){
				destNvlPioche = depCarte;
				if(!piocheAnim){
					piocheAnim = true;
					carteWin = new Point(depPioche.x,depPioche.y);
				}
				interpolation(carteWin,destNvlPioche);
			}else{
				piocheAnim = false;
				carteWin = new Point (depCarte.x, depCarte.y);
			}
		}
		Carte aGagner = jeu.plateau().carteAJouer();
		if(aGagner != null) {
			jg.tracerImage(images[aGagner.getFaction()][aGagner.getPoid()], carteWin.x, carteWin.y, carteL, carteH);
		}
	}

	private void dessinePioche(){
		depPioche = new Point(margeL, 14*margeH);
		if(jeu.plateau().combatPret()){
			if(!combatAnimPioche){
				combatAnimPioche = true;
				joueurWin = jeu.plateau().quiGagneCombat();
				pioche  = new Point(depPioche.x, depPioche.y);
			}
			if(joueurWin == 0){
				interpolation(pioche, versPartisansJ1);
			}else{
				interpolation(pioche, versPartisansJ0);
			}
			if(jeu.plateau().pioche.size() > 1) {
				jg.tracerImage(dos, depPioche.x, depPioche.y, carteL, carteH);
			}
		}else{
			combatAnimPioche = false;
			pioche = new Point(depPioche.x, depPioche.y);
		}
		if(jeu.plateau().pioche.size() > 0) {
			jg.tracerImage(dos, pioche.x, pioche.y, carteL, carteH);
			jg.tracerTxt("Pioche",margeL,26*margeH,f);
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
		jg.tracerTxt(jeu.getJoueur(0).getNom(), 2*margeL,hauteur-5*margeH,f);

		/* dessin du Joueur 2*/
		jg.tracerRond(1*margeL,2*margeH,5*margeL,9*margeH,cJ2);
		jg.tracerImage(j2,2*margeL,3*margeH,3*margeL,5*margeH);
		jg.tracerTxt(jeu.getJoueur(1).getNom(),2*margeL,9*margeH,f);

	}

	
	public Carte determinerCarte(int x, int y) {
		if(jeu.TourHumain() && jeu.plateau().carteCourante(jeu.joueurCourant()) == null) {
			int i = 0;
			List<Carte> main = jeu.plateau().getMain(jeu.joueurCourant());
			int nbCarte = main.size();
			while (i < nbCarte) {
				if (x >= posCartes[i].x && x < posCartes[i + 1].x && y >= posCartes[i].y && y <= posCartes[i].y + carteH) {
					dragAndDrop = jeu.plateau().cartePosMain(i, jeu.joueurCourant());
					if(jeu.plateau().carteJouable(dragAndDrop)){
						drag = new Point(x, y);
						estSurZoneDrop = false;
						return dragAndDrop;
					}else{
						dragAndDrop = null;
					}
				}
				i++;
			}
		}
		return null;
	}

	public boolean okDrop(int x,int y){
		drag = new Point(x, y);
		if(x>=dropDeb.x && x<= dropFin.x && y>= dropDeb.y && y<= dropFin.y){
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

	public void annuler(){
		posCartes = new Point[14];
		cartesJ0 = new int[5];
		cartesJ1 = new int[5];
		dragAndDrop = null;
		/*
		combatAnim = false;
		combatAnimPioche = false;
		carteCouranteAnim = false;
		piocheAnim = false;*/
		estSurZoneDrop = false;
	}
}
