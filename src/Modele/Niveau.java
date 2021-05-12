package Modele;
import java.util.*;

public class Niveau {
    public static final int nbCarte = 52;
    public static final int GLOBELINS = 1;
    public static final int NAINS = 2;
    public static final int MORTSVIVANTS = 3;
    public static final int DOPPELGANGERS = 4;
    public static final int CHEVALIERS = 5;
    int iCartes, iScoreA, iScoreB, iPlis, iDefausser, iMainA, iMainB;
    int [][] T;
    int nbFaction;
    Random rand;
    // variables qui changent a chaque tour
    int sommetPlis, sommetCartes;
    // les dernieres cartes jouée par A, B
    // la carte piocher depuis le plis
    int cartePrecedenteA, cartePrecedenteB;
    int carteDePlis;

    public Niveau() {}
    public void initialiser() {
        rand = new Random();
        T = new int [7][nbCarte];
        iCartes = 0;
        iScoreA = 1;
        iScoreB = 2;
        iPlis = 3;
        iDefausser = 4;
        iMainA = 5;
        iMainB = 6;
        sommetCartes = nbCarte;
        sommetPlis = 0;
        initialiserCartes();
        melanger();
        initialiserPlis();
    }
    // la reference de ces tableau seront passer pour chaque joueur
    public int [] mainA() { return T[iMainA]; }
    public int [] mainB() { return T[iMainB]; }

    public void melanger() {
        for (int x = 0; x < nbCarte; x++) {
            int i = rand.nextInt(nbCarte);
            int j = rand.nextInt(nbCarte);
            int t = T[iCartes][i];
            T[iCartes][i] = T[iCartes][j];
            T[iCartes][j] = t;
        } 
    }

    public boolean estPlisVide() { return sommetPlis == 0; }
    public boolean estCartesVide() { return sommetCartes == 0; }

    public void ajouterDefaussier(int carte) {
        int i = 0;
        while (T[iDefausser][i] != 0) i++;
        if (i < nbCarte) T[iDefausser][i] = carte;
    }
    public void ajouterScoreA(int carte) {
        int i = 0;
        while (T[iScoreA][i] != 0) i++;
        if (i < nbCarte) T[iScoreA][i] = carte;
    }
    public void ajouterScoreB(int carte) {
        int i = 0;
        while (T[iScoreB][i] != 0) i++;
        if (i < nbCarte) T[iScoreB][i] = carte;
    }
    public int piocherPlis() {
        int r = T[iPlis][sommetPlis];
        sommetPlis--;
        return r;
    }
    public int piocherCarte() {
        int carte = T[iCartes][sommetCartes];
        sommetCartes--;
        return carte;
    } 
    public void initialiserPlis() {
        int nb = 13;
        for(int i = 0; i < nb; i++) T[iCartes][i] = piocherCarte();
    }
    public void initialiserCartes() {
        for (int i = 0; i < 4; i ++) T[iCartes][i] = GLOBELINS * 10;
        for (int i = 0; i < 10; i ++) {
            T[iCartes][i + 4] = GLOBELINS * 10 + i;
            T[iCartes][i + 14] = NAINS * 10 + i;
            T[iCartes][i + 24] = MORTSVIVANTS * 10 + i;
            T[iCartes][i + 34] = DOPPELGANGERS * 10 + i;
        }
        for(int i = 2; i < 10; i++)
            T[iCartes][i + 43] = CHEVALIERS * 10 + i;      
    }

	public Niveau clone() {
		Niveau clone = new Niveau();
        for (int i = 0; i < 7; i++)
            for(int j = 0; j < nbCarte; j++)
                clone.T[i][j] = T[i][j];
		return clone;
	}
    public int hash() {
        return Arrays.deepHashCode(T);
    }
}
