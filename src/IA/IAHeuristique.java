package IA;

import java.util.*;
import Modele.*;
import Patterns.*;

public class IAHeuristique extends IA{
    int [] main;
    public IAHeuristique( int [] ref_main) {
        main =  ref_main;
    }
    @Override
    // renvoyer une carte a jouer a chaque fois la fonction appelée
    public int determineCoup() {
        int carte = 0;
        return carte;
    }
	@Override
	public int typeJoeur() {
		// TODO Auto-generated method stub
		return 0;
	}
}