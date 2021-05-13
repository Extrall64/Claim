package Modele;

import java.util.Random;

import IA.IA;

public class Jeu {
    public static final int nbCarte = 52;
    public static final int GLOBELINS = 1;
    public static final int NAINS = 2;
    public static final int MORTSVIVANTS = 3;
    public static final int DOPPELGANGERS = 4;
    public static final int CHEVALIERS = 5;
    public Niveau niv;
    IA [] joueurs;
    int nbFaction;
    // variables qui changent a chaque nouveau gagneur de plis
    int leaderCourant;
    // variables qui changent a chaque tour
    int joueurCourant, factionDemander;
    Random rand;

    public Jeu() {
        rand = new Random();
        niv = new Niveau();
        joueurCourant = 0;
        leaderCourant = rand.nextInt(2); 
    }

    public void changerJoueur() {
        joueurCourant = (joueurCourant + 1) % 2;
    }
    // si le joueur A emporte le plis
    public boolean estJoueurAEmporte(Carte carteB) {
        Carte carteA = niv.cartePrecedenteA;
        // si la carte de A est DOPPERLGANERS ou
        // si la carte de A a plus de poid A gagne , si les poids egaux,
        // A gagne si c'est lui le leader
        if (carteA.faction == carteB.faction || carteA.faction == DOPPELGANGERS) {
            return carteA.poid > carteB.poid || (carteA.poid == carteB.poid && leaderCourant == 0);
        }
        return false;
    }
    public void jouerCarte(Carte carte) {
        if (estJoueurAEmporte(carte)) {
            // A FAIRE mettre a jour le plis et le defausser
            // le joueur qui l'emporte devient un leader
            switch(carte.faction) {
                // aucun pourvoir
                case GLOBELINS: break;
                case NAINS: break;
                // ces cartes ne sont pas defausser lors de la 1ere phase
                case MORTSVIVANTS:
                break;
                // ces cartes peuvents passer avec toutes les factions
                // si le leader a joueur une doppelgangers alors
                // l'autre joueur doit le jouer s'il peut
                case DOPPELGANGERS: break;
                //lorsqu'un chevalier est joué apres un globelins
                // il emporte automatiquement le plis
                case CHEVALIERS: break;
            }
        }
    }

    public boolean estPhase1Fini() {
        return false;
    }
    public boolean estPhase2Fini() {
        return false;
    }
    public void jouerPhase1() {
        while(!estPhase1Fini()) {
            Carte carte = new Carte(0, 0);
            jouerCarte( carte );
            changerJoueur();
        }
    }
    public void jouerPhase2() { }

    public void tictac() { }
}