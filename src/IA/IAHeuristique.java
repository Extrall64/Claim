package IA;

import Modele.*;

public class IAHeuristique implements IA{
    int [] main;
    public IAHeuristique( int [] ref_main) {
        main =  ref_main;
    }
    @Override
    // renvoyer une carte a jouer a chaque fois la fonction appelée
    public Carte determineCoup() {
        int carte = 0;
        return  null;
    }
	@Override
	public int typeJoeur() {
		// TODO Auto-generated method stub
		return 0;
	}
}