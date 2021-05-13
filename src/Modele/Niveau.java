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
    int cur;
    Carte [] T;
    int nbFaction;
    Random rand;
    Carte cartePrecedenteA, cartePrecedenteB, carteDePlis;

    public Niveau() {
        initialiser();
    }
    public void initialiser() {
        rand = new Random();
        T = new Carte[nbCarte];
        iCartes = 0;
        iScoreA = 1;
        iScoreB = 2;
        iPlis = 3;
        iDefausser = 4;
        iMainA = 5;
        iMainB = 6;
        cur = 0;
        initialiserCartes();
        melanger();
    }
    public List<Carte> initialiserPlis() {
        int nb = 13;
        List<Carte> r = new ArrayList<Carte>();
        for(int i = 0; i < nb; i++) {
            T[i].categorie = iPlis; 
            r.add( T[i] );
        }
        return r;
    }
    public void initialiserCartes() {
        // construire les 4 carte globelins de poid 0
        for (int i = 0; i < 4; i ++) {
            Carte g = new Carte(GLOBELINS, 0);
            T[cur] = g; cur++;
        }
        // constuire nains, mort-vivant, doppelgangers
        for (int i = 0; i < 10; i++) {
            Carte g = new Carte(GLOBELINS, i); 
            Carte n = new Carte(NAINS, i);
            Carte m = new Carte(MORTSVIVANTS, i);
            Carte d = new Carte(DOPPELGANGERS, i);
            T[cur] = g; cur++; T[cur] = n; cur++; T[cur] = m; cur++; T[cur] = d; cur++;
        }
        for(int i = 2; i < 10; i++) {
            Carte c = new Carte(CHEVALIERS, i);
            T[cur] = c; cur++;
        }
    }
    private List<Carte> filtrer(int categorie) {
        List<Carte> r = new ArrayList<>();
        for (int i = 0 ; i < nbCarte; i++) {
            if (T[i].categorie == categorie)
                r.add(T[i]);
        }
        return r;
    }
    // la reference de ces tableau seront passer pour chaque joueur
    public List<Carte> mainA() {
        return filtrer(iMainA);
    }
    public List<Carte> mainB() {
        return filtrer(iMainB);
    }
    public List<Carte> scoreA() {
        return filtrer(iScoreA);
    }
    
    public List<Carte> scoreB() {
        return filtrer(iScoreB);
    }
    public List<Carte> plis() {
        return filtrer(iPlis);
    }
    public List<Carte> defausser() {
        return filtrer(iDefausser);
    }
    public Carte [] cartes() { return T; }

    public void melanger() {
        for (int x = 0; x < nbCarte; x++) {
            int i = rand.nextInt(nbCarte);
            int j = rand.nextInt(nbCarte);
            Carte t = T[i];
            T[i] = T[j];
            T[j] = t;
        } 
    }
    public void afficher() {
        for(Carte c : T) {
            System.out.printf("(%d, %d)\n", c.faction , c.poid);
        }    
    }
	public Niveau clone() {
		Niveau clone = new Niveau();
        for(int j = 0; j < nbCarte; j++)
            clone.T[j] = T[j];
		return clone;
	}
    public int hash() {
        return Arrays.deepHashCode(T);
    }

}
