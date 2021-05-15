package IA;


import java.util.*;
import Modele.*;

public class IAAleatoire extends IA{
    int [] main;
    Random rand;
    public IAAleatoire(int [] ref_main) {
        main = ref_main;
        rand = new Random();
    }
    // renvoyer une carte a jouer a chaque fois la fonction appelée
    public int determineCoup() {
        int carte = rand.nextInt( main.length );
        return carte;
    }
	@Override
	public int typeJoeur() {
		// TODO Auto-generated method stub
		return 0;
	}
}
