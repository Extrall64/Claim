package Joueur;


import java.util.*;
import Modele.*;
import Patterns.*;

public class IAAleatoire implements Joueur{
    Modele m;
    int [] main;
    Random rand;
    public IAAleatoire(Modele m, int [] ref_main) {
        this.m = m;
        main = ref_main;
        rand = new Random();
    }
    // renvoyer une carte a jouer a chaque fois la fonction appelée
    public int determineCoup() {
        int carte = rand.nextInt( main.length );
        return carte;
    }
}
