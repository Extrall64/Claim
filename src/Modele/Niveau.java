package Modele;

import java.util.Random;

import Global.Configuration;
import Structures.Iterateur;
import Structures.Sequence;

public class Niveau extends Historique<Action>{
  
	private Carte[] cartes;
	private Random rand;
    private int joueurCourant,phase;
	private Sequence<Integer>[] scores,partisans,mains;
	private Sequence<Integer> pioche,defausse;
	private int[] carteCourantes;
	private int carteAJouer;
    
    public void initialiser() {
        rand = new Random();
        cartes = new Carte[Jeu.nbCarte];
        initialiserCartes();
        melanger();
        initialiserPiles();
    }
    
    public void initialiserPiles(){
    	//initialisation des piles
    	carteAJouer = -1;
    	carteCourantes = new int[2];
    	carteCourantes[0] = -1; carteCourantes[1] = -1;
    	pioche = Configuration.instance().nouvelleSequence();
    	defausse = Configuration.instance().nouvelleSequence();
    	scores = new Sequence[2];
    	partisans = new Sequence[2];
    	mains = new Sequence[2];
	    for(int i=0;i<2;i++) { //initialisations des piles vides
	    	Sequence<Integer> s = Configuration.instance().nouvelleSequence();
	    	scores[i] = s;
	    }
	    for(int i=0;i<2;i++) { //initialisations des piles vides
	    	Sequence<Integer> s = Configuration.instance().nouvelleSequence();
	    	partisans[i] = s;
	    }
	    for(int i=0;i<2;i++) { //initialisations des piles vides
	    	Sequence<Integer> s = Configuration.instance().nouvelleSequence();
	    	mains[i] = s;
	    }
    }
    
    private void initialiserCartes() {
    	int x = 0;
        for (int i = 0; i < 4; i ++) { // construire les 4 carte globelins de poid 0
        	cartes[x] = new Carte(Jeu.GLOBELINS, 0);
            x++;
        }
        for (int i = 0; i < 10; i++) { // constuire nains, mort-vivant, doppelgangers,chevalier
        	cartes[x] = new Carte(Jeu.GLOBELINS, i); 
            x++;
            cartes[x] = new Carte(Jeu.NAINS, i);
            x++;
            cartes[x] = new Carte(Jeu.MORTSVIVANTS, i);
            x++;
            cartes[x] = new Carte(Jeu.DOPPELGANGERS, i);
            x++;
            if(i >= 2) {
            	cartes[x] = new Carte(Jeu.CHEVALIERS, i);
            	x++;
            }
        }
    }
    
    private void melanger() {
        for (int x = 0; x < Jeu.nbCarte*2; x++) {
            int i = rand.nextInt(Jeu.nbCarte);
            int j = rand.nextInt(Jeu.nbCarte);
            Carte t = cartes[i];
            cartes[i] = cartes[j];
            cartes[j] = t;
        } 
    }
    
    public void initialiserPhase1(){
    	for(int i = 0;i<13;i++) {
    		mains[Jeu.JoueurA].insereQueue(i);
    		mains[Jeu.JoueurB].insereQueue(i+13);
    		pioche.insereQueue(i+26);
    		pioche.insereQueue(i+39);
    	}
    	phase = 1;
    }
    
    public void determinerJoueurCommence(){ 
    	joueurCourant = rand.nextInt(2);
    }
    
    public void initialiserPhase2(){ //bouger les cartes partisans dans les mains
    	for(int i = 0;i<13;i++) {
    		mains[Jeu.JoueurA].insereQueue(partisans[Jeu.JoueurA].extraitTete());
    		mains[Jeu.JoueurB].insereQueue(partisans[Jeu.JoueurB].extraitTete());
    	}
    	phase = 2;
    }
    
    private int piocheCarte() {
    	return pioche.extraitTete();
    }
    
    public void retournerNouvelleCarteEnJeu() {
    	carteAJouer = piocheCarte();
    }
    
    public void jouerCarte(int carte) {
    	nouveau(new Action(this, this.clone()));//mise a jour historique
    	carteCourantes[joueurCourant] = carte;
    	retirerCarteMainCourante(carte);
    	if(carteCourantes[autreJoueur()] == -1) {
    		inverserJoueur();
    	}
    	
    }
    
    public int jouerCarteValide(int carte) {
    	if( estCarteDansMainCourante(carte) && carteJouable(carte)) {
    		return carte;
    	}
    	else {
    		return -1;
    	}
    }
    
    public boolean estCarteDansMainCourante(int carte) {
    	Iterateur<Integer> i = mains[joueurCourant].iterateur();
    	while(i.aProchain()) {
    		if(carte == i.prochain()) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean carteJouable(int carte) {
    	if(carteCourantes[Jeu.JoueurA] == -1 && carteCourantes[Jeu.JoueurB] == -1) {
    		return true;
    	}else if (!(carteCourantes[Jeu.JoueurA] == -1) && !(carteCourantes[Jeu.JoueurB] == -1)) {
    		return false;
    	}else{
    		int x;
    		if(carteCourantes[Jeu.JoueurA] == -1) {
    			x = carteCourantes[Jeu.JoueurB];
    		}else {
    			x = carteCourantes[Jeu.JoueurA];
    		}
    		
    		if(cartes[x].faction == cartes[carte].faction || cartes[carte].faction == Jeu.DOPPELGANGERS) {
    			return true;
    		}
    		else {
    			if(aucuneCarteFaction(cartes[x].faction))
    				return true;
    			else
    				return false;
    		}
    	}
    }
    
    private boolean aucuneCarteFaction(int faction) {
    	Iterateur<Integer> i = mains[joueurCourant].iterateur();
    	while(i.aProchain()) {
    		if(faction(i.prochain()) == faction)
    			return false;
    	}
    	return true;
    }
    
    private void retirerCarteMainCourante(int carte) {
    	Iterateur<Integer> i = mains[joueurCourant].iterateur();
    	while(i.aProchain() && carte != i.prochain());
    	i.supprime();
    }
    
    private void inverserJoueur() {
    	joueurCourant = (joueurCourant + 1) % 2;
    }
    
    public boolean combatPret() {
    	return carteCourantes[Jeu.JoueurA] != -1 && carteCourantes[Jeu.JoueurB] != -1;
    }
    
    public int autreJoueur() {
    	return (joueurCourant+1)%2;
    }
    
    public void combat() {
    	Carte a = cartes[carteCourantes[joueurCourant]];
    	Carte b = cartes[carteCourantes[autreJoueur()]];
    	int joueur;
    	if(a.faction == Jeu.GLOBELINS && b.faction == Jeu.CHEVALIERS) { //seul cas particulier
    		joueur = autreJoueur();
    	}
    	else if(b.faction != Jeu.DOPPELGANGERS && a.faction != b.faction) {
    		joueur = joueurCourant;
    	}
    	else { //poids de plus hautes valeurs l'emporte
    		if(a.poid == b.poid) {
    			joueur = joueurCourant;
    		}
    		else if(a.poid < b.poid) {
    			joueur = autreJoueur();
    		}
    		else {
    			joueur = joueurCourant;
    		}
    	}
    	if(phase == 1) {
    		joueurCourant = combatPhase1(joueur);
    	}
    	else {
    		joueurCourant = combatPhase2(joueur);
    	}
    	carteCourantes[Jeu.JoueurA] = -1;
    	carteCourantes[Jeu.JoueurB] = -1;
    }
    
    private int combatPhase1(int joueur) {
    	Carte a = cartes[carteCourantes[Jeu.JoueurA]];
    	Carte b = cartes[carteCourantes[Jeu.JoueurB]];
    	
    	partisans[joueur].insereTete(carteAJouer);
    	carteAJouer = -1;
    	partisans[autreJoueur()].insereTete(piocheCarte());
    	
    	//defausse
    	if(a.faction == Jeu.MORTSVIVANTS && b.faction == Jeu.MORTSVIVANTS) {
    		scores[joueur].insereTete(carteCourantes[Jeu.JoueurA]);
    		scores[joueur].insereTete(carteCourantes[Jeu.JoueurB]);
    	}
    	else if(a.faction == Jeu.MORTSVIVANTS){
    		scores[joueur].insereTete(carteCourantes[Jeu.JoueurA]);
    		defausse.insereTete(carteCourantes[Jeu.JoueurB]); 
    	}
    	else if (b.faction == Jeu.MORTSVIVANTS) {
    		scores[joueur].insereTete(carteCourantes[Jeu.JoueurB]);
    		defausse.insereTete(carteCourantes[Jeu.JoueurA]);
    	}
    	else {
    		defausse.insereTete(carteCourantes[Jeu.JoueurA]);
    		defausse.insereTete(carteCourantes[Jeu.JoueurB]);
    	}
    	return joueur;
    }
    
    private int combatPhase2(int joueur) {
    	Carte a = cartes[carteCourantes[Jeu.JoueurA]];
    	Carte b = cartes[carteCourantes[Jeu.JoueurB]];
    	
    	//score
    	if(a.faction == Jeu.NAINS && b.faction == Jeu.NAINS) {
    		scores[autreJoueur()].insereTete(carteCourantes[Jeu.JoueurA]);
    		scores[autreJoueur()].insereTete(carteCourantes[Jeu.JoueurB]);
    	}
    	else if(a.faction == Jeu.NAINS){
    		scores[autreJoueur()].insereTete(carteCourantes[Jeu.JoueurA]);
    		scores[joueur].insereTete(carteCourantes[Jeu.JoueurB]);
    	}
    	else if (b.faction == Jeu.NAINS) {
    		scores[autreJoueur()].insereTete(carteCourantes[Jeu.JoueurB]);
    		scores[joueur].insereTete(carteCourantes[Jeu.JoueurA]);
    	}
    	else {
    		scores[joueur].insereTete(carteCourantes[Jeu.JoueurA]);
    		scores[joueur].insereTete(carteCourantes[Jeu.JoueurB]);
    	}
    	return joueur;
    }
    	
    public boolean finDePhase1() {
    	return phase == 1 && pioche.estVide();
    }
    
    public boolean finDePhase2() {
    	return phase == 2 && mains[Jeu.JoueurA].estVide() && mains[Jeu.JoueurB].estVide() && carteCourantes[Jeu.JoueurA] == -1;
    }
    
    public int score(int joueur, int faction) {
    	Iterateur<Integer> i = scores[joueur].iterateur();
    	int s = 0;
    	while(i.aProchain()) {
    		if(faction == cartes[i.prochain()].faction) {
    			s++;
    		}
    	}
    	return s;
    }
    
    public int joueurAVote(int faction) {
    	if(score(Jeu.JoueurA,faction) > score(Jeu.JoueurB,faction))
    		return Jeu.JoueurA;
    	else if(score(Jeu.JoueurA,faction) < score(Jeu.JoueurB,faction))
    		return Jeu.JoueurB;
    	else
    		return plusGros(faction);
    }
    
    public int plusGros(int faction) {
    	Iterateur<Integer> a = scores[Jeu.JoueurA].iterateur();
    	Iterateur<Integer> b = scores[Jeu.JoueurB].iterateur();
    	int sa = -1;
    	int sb = -1;
    	while(a.aProchain()) {
    		int tmp = cartes[a.prochain()].poid;
    		if(sa < tmp) {
    			sa = tmp;
    		}
    	}
    	while(b.aProchain()) {
    		int tmp = cartes[b.prochain()].poid;
    		if(sb < tmp) {
    			sb = tmp;
    		}
    	}
    	if(sa < sb)
    		return Jeu.JoueurB;
    	else if(sa > sb)
    		return Jeu.JoueurA;
    	else
    		return -1;
  
    }
    
    public int joueurGagant() {
    	int a = 0;
    	int b = 0;
    	if(joueurAVote(Jeu.CHEVALIERS) == Jeu.JoueurA)
    		a++;
    	else if(joueurAVote(Jeu.CHEVALIERS) == Jeu.JoueurB)
    		b++;
    	if(joueurAVote(Jeu.NAINS) == Jeu.JoueurA)
    		a++;
    	else if(joueurAVote(Jeu.NAINS) == Jeu.JoueurB)
    		b++;
    	if(joueurAVote(Jeu.DOPPELGANGERS) == Jeu.JoueurA)
    		a++;
    	else if(joueurAVote(Jeu.DOPPELGANGERS) == Jeu.JoueurB)
    		b++;
    	if(joueurAVote(Jeu.MORTSVIVANTS) == Jeu.JoueurA)
    		a++;
    	else if(joueurAVote(Jeu.MORTSVIVANTS) == Jeu.JoueurB)
    		b++;
    	if(joueurAVote(Jeu.GLOBELINS) == Jeu.JoueurA)
    		a++;
    	else if(joueurAVote(Jeu.GLOBELINS) == Jeu.JoueurB)
    		b++;
    	if(a>b)
    		return Jeu.JoueurA;
    	else if(a<b)
    		return Jeu.JoueurB;
    	else
    		return -1;//egalite
    	
    }
    
    public int joueurCourant() {
    	return joueurCourant;
    }
    
    public NiveauIAPhase1 passerEnNiveauIAPhase1() {
    	return null;
    }
    
    public NiveauIAPhase2 passerEnNiveauIAPhase2() {
    	return null;
    }
    
    public int poid(int carte) {
    	return cartes[carte].poid;
    }
    
    public int faction(int carte) {
    	return cartes[carte].faction;
    }
    
    public int phase() {
    	return phase;
    }
    
    public int carteAJouer() {
    	return carteAJouer;
    }
    
    public int carteCourante(int joueur) {
    	return carteCourantes[joueur];
    }
    
    public int cartePosMain(int pos,int j) {
    	Iterateur<Integer> i = mains[j].iterateur();
    	int k = -1;
    	int x = -1;
    	while(k != pos) {
    		if(i.aProchain()) {
    			x = i.prochain();
    		}
    		else {
    			System.err.println("Niveau/cartePosMain erreur main ne contient pas la carte");
    			x = -1;
    			break;
    		}
    		k++;
    	}
    	return x;
    }
    
    public void remplace(Niveau n) { //appel de l'historique
    	joueurCourant = n.joueurCourant();
    	phase = n.phase();
    	carteAJouer = n.carteAJouer();
    	carteCourantes[0] = n.carteCourante(0);
    	carteCourantes[1] = n.carteCourante(1);
    	pioche = n.getPioche();
    	defausse = n.getDefausse();
    	scores[0] = n.getScore(0);
    	partisans[0] = n.getPartisans(0);
    	mains[0] = n.getMain(0);
    	scores[1] = n.getScore(1);
    	partisans[1] = n.getPartisans(1);
    	mains[1] = n.getMain(1);
    }
    
    public Sequence<Integer> getPioche(){
    	return pioche;
    }
    public Sequence<Integer> getDefausse(){
    	return defausse;
    }
    public Sequence<Integer> getScore(int joueur){
    	return scores[joueur];
    }
    public Sequence<Integer> getPartisans(int joueur){
    	return partisans[joueur];
    }
    public Sequence<Integer> getMain(int joueur){
    	return mains[joueur];
    }
    
    public void setPhase(int p) {
    	phase = p;
    }
    public void setJoueur(int j) {
    	joueurCourant = j;
    }
    public void setPioche(Sequence<Integer> p){
    	pioche = p;
    }
    public void setCarteCourante(int j, int c) {
		carteCourantes[j] = c;
    }
    public void setCarteAJouer(int c) {
    	carteAJouer = c;
    }
    public void setDefausse(Sequence<Integer> d){
    	defausse = d;
    }
    public void setScore(int joueur, Sequence<Integer> s){
    	scores[joueur] = s;
    }
    public void setPartisans(int joueur, Sequence<Integer> p){
    	partisans[joueur] = p;
    }
    public void setMain(int joueur,Sequence<Integer> m){
    	mains[joueur] = m;
    }

    public Niveau clone() {
    	Niveau n = new Niveau();
    	n.initialiserPiles();
    	n.setPhase(phase);
    	n.setJoueur(joueurCourant);
    	n.setPioche(this.cloneSequence(pioche));
    	n.setCarteCourante(0, carteCourantes[0]);
    	n.setCarteCourante(1, carteCourantes[1]);
    	n.setPioche(this.cloneSequence(pioche));
    	n.setCarteAJouer(carteAJouer);
    	n.setDefausse(this.cloneSequence(defausse));
    	n.setScore(0,this.cloneSequence(scores[0]));
    	n.setScore(1,this.cloneSequence(scores[1]));
    	n.setPartisans(0,this.cloneSequence(partisans[0]));
    	n.setPartisans(1,this.cloneSequence(partisans[1]));
    	n.setMain(0,this.cloneSequence(mains[0]));
    	n.setMain(1,this.cloneSequence(mains[1]));
    	return n;
    }
    
    private Sequence<Integer> cloneSequence(Sequence<Integer> s){
    	Sequence<Integer> c = Configuration.instance().nouvelleSequence();
    	Iterateur<Integer> i = s.iterateur();
    	while(i.aProchain()) {
    		c.insereTete(i.prochain());
    	}
    	return c;
    }
}