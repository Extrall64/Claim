package Joueur;

import java.util.*;
import Modele.*;
import Patterns.*;

public class IAHeuristique implements Joueur {
    Modele m;
    int [] main;
    public IAHeuristique(Modele m, int [] ref_main) {
        this.m = m;
        main =  ref_main;
    }
    @Override
    // renvoyer une carte a jouer a chaque fois la fonction appelée
    public int determineCoup() {
        int carte = 0;
        return carte;
    }
}