package IA;
import Modele.Carte;
import Modele.Jeu;

public class Test {
	private Jeu jeu;
	public Test(Jeu jeu) {
		this.jeu = jeu;
		jeu.nouvellePartie();
	}
	
	public void demarrer(int n) {
		int [] a = new int [2];
		int [] b = new int [2];
		int [] c = new int [2];
		IA [] joueurs =  new IA [2];
		System.out.println("Demarrage de tests..");
		
		joueurs[0] = new IAAleatoire(jeu, 0);
		joueurs[1] = new IAMinMax(jeu, 1, 5);
		//combat(n, joueurs , a);

		joueurs[0] = new IAAleatoire(jeu, 0);
		joueurs[1] = new IAHeuristique(jeu, 1, 5);
		//combat(n, joueurs, b);

		joueurs[0] = new IAMinMax (jeu, 0, 5);
		joueurs[1] = new IAHeuristique(jeu, 1, 5);
		combat(n, joueurs, c);

		System.out.println("Resultat");
		System.out.printf("Sur %d combats, IA aleatoire gagne %d, IA Minmax gagne %d\n", n, a[0], a[1]);
		System.out.printf("Sur %d combats, IA aleatoire gagne %d, IA Heuristique gagne %d\n", n, b[0], b[1]);
		System.out.printf("Sur %d combats, IA Minmax gagne %d, IA Heuristique gagne %d\n", n, c[0], c[1]);

	}

	private void combat(int n, IA [] joueurs, int [] res) {
		int i = 0;
		while (i < n) {
			jeu.nouvellePartie();
			int gagnant = jeu.gagnant();
			while (gagnant == -1) {
				if(!jeu.finDePartie() && joueurs[jeu.joueurCourant()] != null) {
					Carte c = joueurs[jeu.joueurCourant()].determineCoup( );
					if(jeu.carteJouable(c)) jeu.joueCarte(c);
					else System.err.println("erreur l'ia joue une carte qui n'est pas dans sa main");
				}
				gagnant = jeu.gagnant();
			}
			if (gagnant > 0) res[ gagnant - 1 ] += 1;
			i += 1;
		}
	}
}
