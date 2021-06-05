package IA;

/* IA Monte Carlo simulation , evalue chaque coup possible pour une configuration donnée par le
 * nombre de configuration gagnante en jouant ce coup
 * choisir ce coup comme etant le meilleur
 */
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import Modele.Carte;
import Modele.Jeu;
import Modele.Plateau;

public class IAMonteCarlo implements IA {
	Jeu config;
	int joueur, autreJoueur, meilleurScore, echantillon;
	List<Carte> meilleursCoup;
	private boolean visionComplete;
	Random rand;
	public IAMonteCarlo(Jeu c, int j, int nb) {
		config = c;
		echantillon = nb;
		joueur = j;
		autreJoueur = (joueur + 1) % 2;
		visionComplete = false;
		rand = new Random();
	}
    public Carte determineCoup() {
		meilleursCoup = new ArrayList<>();
    	meilleurScore = -1;
    	Plateau plateau = config.plateau();
    	int joueur = plateau.joueurCourant();
    	// pour chaque carte jouable
    	for (Carte c: plateau.getMain( joueur )) {
    		if (config.carteJouable(c)) {
    			int score = 0;
    			//jouer une N partie aleatoire et evaluer ce coup par le nb de configuration gagante eu
    			for(int i = 0; i < echantillon; i++) {
        			Jeu nconfig = config.clone();
        			// cherche ce coup dans la main cloné et la jouer
    				for(Carte nc: nconfig.plateau().getMain( joueur )) {
    					if (c.estEgale(nc)) {
    						nconfig.joueCarte(nc);
    						break;
    					}
    				}
    				// continuer la partie aleatoirement
        			continueAleatoirement(nconfig);
        			// actualiser le score de coup
        			if (nconfig.gagnant() - 1 == joueur)
        				score += 1;
    			}
    			// actualiser/construire la liste des meilleurs coup ayant le meme score
    			if (score >= meilleurScore) {
    				if (score > meilleurScore)
    					meilleursCoup = new ArrayList<Carte>();
    				meilleurScore = score;
    				meilleursCoup.add( c );
    			}
    		}
    	}
        //choisir un coup aleatoire de meme score
        int aleatoire = rand.nextInt( meilleursCoup.size() );
        Carte carte = meilleursCoup.get( aleatoire );
        System.out.printf("[IA MonteCarlo] a joué son coup: [%d] %s\n", aleatoire, carte);
    	return carte;
    }
    
    // terminer la partie aleatoirement
    public void continueAleatoirement(Jeu config) {
    	Plateau plateau = config.plateau();
    	// jouer jusqu' a la fin de partie
    	// construction main courante, Cas particulier: si phase 1 et le tour de l'adversiare
		// retourner l'ensemble des cartes possibles que l'adversaire peut avoir
		while(!config.finDePartie()) {
			List<Carte> main = plateau.getMain( config.joueurCourant() );
	    	List<Carte> l = new ArrayList<>();
	    	for(Carte c: main) if (config.carteJouable(c)) l.add(c);
	    	if (plateau.phase() == 1 && config.joueurCourant() == autreJoueur) {
	    		for(Carte c: plateau.cartes()) {
	    			int cat = c.getCategorie();
					if (config.carteJouable(c) && (cat == Plateau.iCartes || cat == Plateau.iPioche)) {
						l.add(c);
					}
	    		}
	    	}
			// selectionner aleatoirement une carte de la main de joueur courant
	    	Carte c = l.get( rand.nextInt(l.size()) );
			config.joueCarte(c);
		}
    }

	public void activerVisionComplete() {
    	visionComplete = true;
    }
}
