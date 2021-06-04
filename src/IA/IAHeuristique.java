package IA;

import java.util.*;
import Modele.*;

/*
 * extension de l'IA minmax qui change les strategies de phase dynamiquement si la strategie appliqueée perd plus souvent
*/
public class IAHeuristique extends IAMinMax {
	final float biais = 13 / 4;
	float [] heuristiquePhase;
    Strategie [][] strategiePhase;

    public IAHeuristique(Jeu c, int j, int h) {
    	super(c, j, h);
        // fixer les strategies et ces alternatives
    	strategiePhase = new Strategie [][]
    	{
    		{ new StrategiePhase1("moyenne3FactionMajoritaire"), new StrategiePhase1("favoriseFactionEnMain")},
    		{ new StrategiePhase2("difference"), new StrategiePhase2("difference2") }
    	};
    	// valeur heuristique
    	heuristiquePhase = new float[] {biais, biais};
    }
    @Override
    public Carte determineCoup() {
		meilleursCoup = new ArrayList<Carte>();
		float scoreCarte = calculJoueurA(config.clone(), horizon, -oo, +oo);

    	int phase = config.plateau().phase() - 1;
        // mise a jour de l'heuristique
        if (scoreCarte < 0) heuristiquePhase[phase] -= 1;
        else heuristiquePhase[phase] += 0.5;
        
        // changer la strategie si on perd plus souvant avec la courante
        if (heuristiquePhase[phase] < 0) {
        	heuristiquePhase[phase] = biais;
        	changerStrategie();
        	System.out.printf("Strategie phase %d changée !", phase + 1);
        }
        // retourner un coup aleatoire de meilleursCoup
        int aleatoire = rand.nextInt( meilleursCoup.size() );
        Carte carte = meilleursCoup.get( aleatoire );
        System.out.printf("[IA Heuristique] a joué son coup: [%d], %s\n", aleatoire, carte);
        return carte;
    }

    @Override
	float score(Plateau plateau) {
		float scoreParDefaut = 10;
		int phase = plateau.phase() - 1;
		if (!plateau.finDePhase2())
			return strategiePhase[ phase ][0].score(plateau);
		return scoreParDefaut;
	}
	void changerStrategie() {
		Plateau plateau = config.plateau();
		int phase = plateau.phase() - 1;
		Strategie t = strategiePhase[phase][0];
		strategiePhase[phase][0] = strategiePhase[phase][1];
		strategiePhase[phase][1] = t;
	}
}
