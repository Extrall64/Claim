package Modele;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Plateau extends Historique<Action> implements Serializable {
	public static final int nbCarte = 52;
    
	public static final int nbFaction = 5;
    // les diffirentes valeur que l'attr faction de carte peut prendre
    public static final int GLOBELINS = 0;
    public static final int NAINS = 1;
    public static final int MORTSVIVANTS = 2;
    public static final int DOPPELGANGERS = 3;
    public static final int CHEVALIERS = 4;
    
    public static final int iCartes = 0;
    public static final int iScoreA = 1;
    public static final int iScoreB = 2;
    public static final int iPioche = 3;
    public static final int iDefausser = 4;
    public static final int iMainA = 5;
    public static final int iMainB = 6;
    public static final int iPartisansA = 7;
    public static final int iPartisansB = 8;
    public static final int icourantA = 9;
    public static final int icourantB = 10;
    public static final int icarteAjouer = 11;
    
    public static final int JoueurA = 0;
    public static final int JoueurB = 1;
      
	private Carte[] cartes;
	private Random rand;
    private int joueurCourant,phase;
	private Carte[] carteCourante;
	private Carte carteAJouer;
	private List<Carte> pioche, defausse;
	private List <List<Carte>> partisans, scores, mains;

	public void initialiser() {
        rand = new Random();
        cartes = new Carte[nbCarte];
        initialiserCartes();
        initialiserPiles();
        carteCourante = new Carte[2];
        carteCourante[0] = null; carteCourante[1] = null;
    }
	
	public void initialiserCartes() {
		int cur = 0;
        // construire les 4 carte globelins de poid 0
        for (int i = 0; i < 4; i ++) {
        	cartes[cur] = new Carte(GLOBELINS, 0);
            cur++;
        }
        // constuire nains, mort-vivant, doppelgangers
        for (int i = 0; i < 10; i++) {
        	cartes[cur] = new Carte(GLOBELINS, i); 
        	cur++;
        	cartes[cur] = new Carte(NAINS, i);
        	cur++;
        	cartes[cur] = new Carte(MORTSVIVANTS, i);
        	cur++;
        	cartes[cur] = new Carte(DOPPELGANGERS, i);
        	cur++;
        }
        for(int i = 2; i < 10; i++) {
        	cartes[cur] = new Carte(CHEVALIERS, i);
        	cur++;
        }
    }

    public void initialiserPiles() {
        pioche = new ArrayList<Carte>();
        defausse = new ArrayList<Carte>();
        mains = new ArrayList<>();
        scores = new ArrayList<>();
        partisans = new ArrayList<>();
    	for(int i = 0; i < 2; i++) {
	        mains.add(new ArrayList<Carte>());
	        scores.add(new ArrayList<Carte>());
	        partisans.add(new ArrayList<Carte>());
    	}
    }
    
    public void initialiserPhase1() {
    	for(int i = 0;i<13;i++) {
    		cartes[i].setCategorie(iMainA);
    		mains.get(JoueurA).add(cartes[i]);

    		cartes[i+13].setCategorie(iMainB);
    		mains.get(JoueurB).add(cartes[i+13]);

    		cartes[i+26].setCategorie(iPioche);
    		pioche.add(cartes[i+26]);
    		cartes[i+39].setCategorie(iPioche);
    		pioche.add(cartes[i+39]);
    	}
    	phase = 1;
    }

    public void initialiserPhase2(){ //bouger les cartes partisans dans les mains
    	for(int i = 0;i<13;i++) {
    		Carte c = partisans.get(JoueurA).remove(0);
    		c.setCategorie(iMainA);
    		mains.get(JoueurA).add( c );
    		c = partisans.get(JoueurB).remove(0);
    		c.setCategorie(iMainB);
    		mains.get(JoueurB).add( c );
    	}
    	phase = 2;
    }
    
    public void joueurCommenceAleatoire(){ 
    	joueurCourant = rand.nextInt(2);
    }
    
    public Carte piocherCarte() {
        carteAJouer = pioche.remove( 0 );
        return carteAJouer;
    }
    
    // utile lors de annuler refaire l'etape
    // utile pour recharger une partie sauvegarder dans un fichier
    public void reconstruirePiles() {
        initialiserPiles();
        for (int i = 0; i < nbCarte; i++) {
            switch( cartes[i].getCategorie()) {
                case Plateau.iScoreA: scores.get(JoueurA).add( cartes[i] ); break;
                case Plateau.iScoreB: scores.get(JoueurB).add( cartes[i] );break;
                case Plateau.iPioche: pioche.add( cartes[i] );break;
                case Plateau.iDefausser: defausse.add( cartes[i] );break;
                case Plateau.iMainA: mains.get(JoueurA).add( cartes[i] );break;
                case Plateau.iMainB: mains.get(JoueurB).add( cartes[i] );break;
                case Plateau.iPartisansA: partisans.get(JoueurA).add( cartes[i] );break;
                case Plateau.iPartisansB: partisans.get(JoueurB).add( cartes[i] );break;
            }
        }
    }

    public void changerJoueur() {
        joueurCourant = (joueurCourant + 1) % 2;
    }
    
    public int joueurCourant() {
    	return joueurCourant;
    }
    public int autreJoueur() {
    	return (joueurCourant() +1) % 2;
    }

    
    public void retournerNouvelleCarteEnJeu() {
    	 Carte c = piocherCarte();
    	 carteAJouer = c;
    	 c.setCategorie(icarteAjouer);
    }
    
    public boolean estCarteValide(Carte carte) {
    	return ( estCarteDansMainCourante(carte) && carteJouable(carte));
    }
    
    public boolean estCarteDansMainCourante(Carte carte) {
    	return carte.getCategorie() == mainJoueur(joueurCourant);
    }
    
    public boolean carteJouable(Carte carte) {
    	if(carteCourante[JoueurA] == null && carteCourante[JoueurB] == null) {
    		return true;
    	}else if (!(carteCourante[JoueurA] == null) && !(carteCourante[JoueurB] == null)) {
    		return false;
    	}else{
    		Carte x;
    		if(carteCourante[JoueurA] == null) {
    			x = carteCourante[JoueurB];
    		}else {
    			x = carteCourante[JoueurA];
    		}
    		
    		if(x.getFaction() == carte.getFaction() || carte.getFaction() == DOPPELGANGERS) {
    			return true;
    		}
    		return (aucuneCarteFaction(x.getFaction()));
    	}
    }
    
    private boolean aucuneCarteFaction(int faction) {
    	List<Carte> l = mains.get(joueurCourant());    		
    	for(Carte c: l) {
	    	if(c.getFaction() == faction) return false;
    	}
    	return true;
    }
    
    public boolean combatPret() {
    	return carteCourante[JoueurA] != null && carteCourante[JoueurB] != null;
    }
    
    public void jouerCarte(Carte carte) {
    	nouveau(new Action(this, this.clone()));//mise a jour historique
    	carteCourante[joueurCourant] = carte;
    	carte.setCategorie(courantJoueur(joueurCourant)); 
    	retirerCarteMainCourante(carte);
    }
   
    private void retirerCarteMainCourante(Carte carte) {
    	mains.get(joueurCourant()).remove(carte);
    }
    
    public int score(int joueur, int faction) {
    	List<Carte> l = scores.get(joueur);
    	int s = 0;
    	for( Carte c: l) {
    		if(faction == c.getFaction()) s++;
    	}
    	return s;
    }
    
    public int plusGros(int faction) {
    	List<Carte> a = scores.get(JoueurA);
    	List<Carte> b = scores.get(JoueurB);
    	int sa = -1;
    	int sb = -1;
    	for (Carte c: a) {
    		if (sa < c.getPoid()) sa = c.getPoid();
    	}
    	for (Carte c: b) {
    		if (sb < c.getPoid()) sb = c.getPoid();
    	}
    	if(sa < sb) return JoueurB;
    	else if(sa > sb) return JoueurA;
    	return -1;
    }
    
    public int joueurAVote(int faction) {
    	if(score(JoueurA, faction) > score(JoueurB,faction))
    		return JoueurA;
    	else if(score(JoueurA,faction) < score(JoueurB,faction))
    		return JoueurB;
    	return plusGros(faction);
    }
    
    public boolean finDePhase1() {
    	return phase() == 1 && pioche.isEmpty();
    }
    
    public boolean finDePhase2() {
    	return phase() == 2 && mains.get(JoueurA).isEmpty() && mains.get(JoueurB).isEmpty() && carteCourante[JoueurA] == null;
    }

    public int joueurGagant() {
    	int a = 0;
    	int b = 0;
    	if(joueurAVote(CHEVALIERS) == JoueurA)
    		a++;
    	else if(joueurAVote(CHEVALIERS) == JoueurB)
    		b++;
    	if(joueurAVote(NAINS) == JoueurA)
    		a++;
    	else if(joueurAVote(NAINS) == JoueurB)
    		b++;
    	if(joueurAVote(DOPPELGANGERS) == JoueurA)
    		a++;
    	else if(joueurAVote(DOPPELGANGERS) == JoueurB)
    		b++;
    	if(joueurAVote(MORTSVIVANTS) == JoueurA)
    		a++;
    	else if(joueurAVote(MORTSVIVANTS) == JoueurB)
    		b++;
    	if(joueurAVote(GLOBELINS) == JoueurA)
    		a++;
    	else if(joueurAVote(GLOBELINS) == JoueurB)
    		b++;
    	if(a>b)
    		return JoueurA;
    	else if(a<b)
    		return JoueurB;
    	else
    		return -1;//egalite
    }
   
    public void combatPhase1() {
    	Carte a = carteCourante[joueurCourant()];
    	Carte b = carteCourante[autreJoueur()];
    	
    	carteAJouer.setCategorie(partisansJoueur(joueurCourant()));
    	partisans.get(joueurCourant()).add(0, carteAJouer);
    	carteAJouer = null;
    	
    	Carte c = piocherCarte();
    	if (c != null) {
    		c.setCategorie( partisansJoueur( autreJoueur() ));
    		partisans.get(autreJoueur()).add(0, c);
    	}
    	//defausse
    	if(a.getFaction() == MORTSVIVANTS && b.getFaction() == MORTSVIVANTS) {
    		a.setCategorie( scoreJoueur( joueurCourant() ));
    		b.setCategorie(scoreJoueur( joueurCourant() ));
    		scores.get(joueurCourant()).add(0, a);
    		scores.get(joueurCourant()).add(0, b);
    	}
    	else if(a.getFaction() == MORTSVIVANTS){
    		a.setCategorie( scoreJoueur( joueurCourant() ));
    		scores.get(joueurCourant()).add(0, a);
    		b.setCategorie(iDefausser);
    		defausse.add(b); 
    	}
    	else if (b.getFaction() == MORTSVIVANTS) {
    		b.setCategorie(scoreJoueur( joueurCourant() ));
    		scores.get(joueurCourant()).add(0, b);
    		a.setCategorie(iDefausser);
    		defausse.add(a);
    	}
    	else {
    		a.setCategorie(iDefausser);
    		b.setCategorie(iDefausser);
    		defausse.add(a);
    		defausse.add(b);
    	}
    }
    
    public void combatPhase2() {
    	Carte a = carteCourante[joueurCourant()];
    	Carte b = carteCourante[autreJoueur()];
    	
    	//score
    	if(a.getFaction() == NAINS && b.getFaction() == NAINS) {
    		a.setCategorie( scoreJoueur( autreJoueur() ));
    		b.setCategorie( scoreJoueur( autreJoueur() ));
    		scores.get(autreJoueur()).add(0, a);
    		scores.get(autreJoueur()).add(0, b);
    	}
    	else if(a.getFaction() == NAINS){
    		a.setCategorie( scoreJoueur( autreJoueur() ));
    		scores.get(autreJoueur()).add(a);
    		b.setCategorie( scoreJoueur( joueurCourant() ));
    		scores.get(joueurCourant()).add(0, b);
    	}
    	else if (b.getFaction() == NAINS) {
    		a.setCategorie( scoreJoueur( joueurCourant() ));
    		b.setCategorie( scoreJoueur( autreJoueur() ));
    		scores.get(autreJoueur()).add(0, b);
    		scores.get(joueurCourant()).add(0, a);
    	}
    	else {
    		a.setCategorie( scoreJoueur( joueurCourant()));
    		b.setCategorie( scoreJoueur( joueurCourant() ));
    		scores.get(joueurCourant()).add(0, a);
    		scores.get(joueurCourant()).add(0, b);
    	}
    }

    public int quiGagneCombat() {
    	Carte a = carteCourante[autreJoueur()];
    	Carte b = carteCourante[joueurCourant()];
    	int joueur;
    	if(a.getFaction() == GLOBELINS && b.getFaction() == CHEVALIERS) { //seul cas particulier
    		joueur = joueurCourant();
    	}
    	else if(b.getFaction() != DOPPELGANGERS && a.getFaction() != b.getFaction()) {
    		joueur = autreJoueur();
    	}
    	else { //poids de plus hautes valeurs l'emporte
    		if(a.getPoid() == b.getPoid()) {
    			joueur = autreJoueur();
    		}
    		else if(a.getPoid() < b.getPoid()) {
    			joueur = joueurCourant();
    		}
    		else {
    			joueur = autreJoueur();
    		}
    	}
    	return joueur;
    }
    
    public void reinitialiserCarteCourante() {
    	carteCourante[JoueurA] = null;
    	carteCourante[JoueurB] = null;
    }
    
    

    public Carte carteCourante(int j) { return carteCourante[j]; }
    
    public Carte cartePosMain(int pos,int j) {
    	if(mains.get(j).size() > pos)
    		return mains.get(j).get(pos);
    	return null;
    }
    
    public int mainJoueur(int j) {
    	if (j == JoueurA) return iMainA;
    	return iMainB;
    }
    
    public int scoreJoueur(int j) {
    	if (j == JoueurA) return iScoreA;
    	return iScoreB;
    }
    
    public int courantJoueur(int j) {
    	if (j == JoueurA) return icourantA;
    	return icourantB;
    }
    
    public int partisansJoueur(int j) {
    	if (j == JoueurA) return iPartisansA;
    	return iPartisansB;
    }

    public void metAJourCartes(Carte [] config) {
        cartes = config;
    }

    public Carte contenu(int i) {
        return cartes[i];
    }
    
    public Carte [] cartes() { return cartes; }


    public void afficher() {
    	System.out.printf("Phase %s\n Joueur: %s\n", phase, joueurCourant);
        for(Carte c : cartes) System.out.println(c);
    }
    
    public List<Carte> getMain(int j) {
    	return mains.get(j);
    }
    public List<Carte> getScore(int j) {
    	return scores.get(j);
    }
    public List<Carte> getPartisans(int j) {
    	return partisans.get(j);
    }
    
    public void melanger() {
        for (int x = 0; x < nbCarte*2; x++) {
            int i = rand.nextInt(nbCarte);
            int j = rand.nextInt(nbCarte);
            Carte t = cartes[i];
            cartes[i] = cartes[j];
            cartes[j] = t;
        } 
    }
    
    public int phase() {
    	return phase;
    }
    
    public Carte carteAJouer() {
    	return carteAJouer;
    }
    
    private void setPhase(int p) {
    	phase = p;
    }
    
    public void setJoueur(int j) {
    	joueurCourant = j;
    }
    
    public Plateau clone() {
		Plateau clone = new Plateau();
		clone.initialiser();
		clone.setJoueur(joueurCourant);
    	clone.setPhase(phase);
        for(int j = 0; j < nbCarte; j++) {
            clone.cartes[j] = new Carte(cartes[j].getFaction(), cartes[j].getPoid());
            clone.cartes[j].setCategorie(cartes[j].getCategorie());
            clone.cartes[j].setEstCachee(JoueurA,cartes[j].estCachee(JoueurA));
            clone.cartes[j].setEstCachee(JoueurB,cartes[j].estCachee(JoueurB));
        }
        clone.reconstruirePiles();
		return clone;
	}
    
    public int hash() {
        return Arrays.deepHashCode(cartes);
    }

    public void remplace(Plateau n) { //appel de l'historique
    	cartes = n.cartes();
     	phase = n.phase();
    	joueurCourant = n.joueurCourant();
    	reconstruirePiles();
    }


	// writeObject est celui par defaut
	// readObject doit appeler initialiserPiles d'abord pour que les sequences soient du bon type
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    	initialiserPiles();
    	ois.defaultReadObject();
	}
}
