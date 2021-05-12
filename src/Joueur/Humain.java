package Joueur;

import Patterns.Point;

public class Humain implements Joueur{
    Modele m;
    int [] main;
    boolean estTerminer;
    public Humain( Modele m, int [] ref_main) {
        this.m = m;
        main = ref_main;
        estTerminer = false;
    }
    @Override
    // renvoyer une carte a jouer a chaque fois la fonction appelée
    public int determineCoup() {
        // attendre que estTerminer soit vraie des le clique de la souris
        while(!estTerminer) {} 
        estTerminer = false;
        return 0;
    }
}

