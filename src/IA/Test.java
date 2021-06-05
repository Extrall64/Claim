package IA;
import Modele.Carte;
import Modele.Jeu;

public class Test {
	private Jeu jeu;
	public Test() {
		jeu = new Jeu();
		jeu.nouvellePartie();
	}
	
	public void demarrer(int n) {
		int [] a = new int [2];
		int [] b = new int [2];
		int [] c = new int [2];
		int [] d = new int [2];
		int [] e = new int [2];
		int [] f = new int [2];

		IA [] joueurs =  new IA [2];
		System.out.println("Demarrage de tests..");
		
		joueurs[0] = new IAAleatoire(jeu, 0);		
		joueurs[1] = new IAMinMax(jeu, 1, 6);
		combat(n, joueurs , a);

		joueurs[0] = new IAAleatoire(jeu, 0);
		joueurs[1] = new IAHeuristique(jeu, 1, 6);
		combat(n, joueurs, b);

		joueurs[0] = new IAAleatoire(jeu, 0);
		joueurs[1] = new IAMonteCarlo(jeu, 1, 20);
		combat(n, joueurs, c);

		joueurs[0] = new IAMinMax (jeu, 0, 6);
		joueurs[1] = new IAHeuristique(jeu, 1, 6);
		//combat(n, joueurs, d);
		
		joueurs[0] = new IAMinMax (jeu, 0, 6);
		joueurs[1] = new IAMonteCarlo(jeu, 1, 20);
		//combat(n, joueurs, e);
		
		joueurs[0] = new IAMinMax (jeu, 0, 6);
		joueurs[1] = new IAVisionComplete(jeu, 1, 20);
		//combat(n, joueurs, f);

		System.out.println("Resultat");
		System.out.printf("Sur %d combats, IA Aleatoire gagne %d, IA Minmax gagne %d\n", n, a[0], a[1]);
		System.out.printf("Sur %d combats, IA Aleatoire gagne %d, IA Heuristique gagne %d\n", n, b[0], b[1]);
		System.out.printf("Sur %d combats, IA Aleatoire gagne %d, IA MonteCarlo gagne %d\n", n, c[0], c[1]);
		System.out.printf("Sur %d combats, IA Minmax gagne %d, IA Heuristique gagne %d\n", n, d[0], d[1]);
		System.out.printf("Sur %d combats, IA Minmax gagne %d, IA MonteCarlo gagne %d\n", n, e[0], e[1]);
		System.out.printf("Sur %d combats, IA Minmax gagne %d, IA VisionComplete gagne %d\n", n, f[0], f[1]);

	}

	private void combat(int n, IA [] joueurs, int [] res) {
		int i = 0;
		int gagnant = -1;
		while (i < n) {
			jeu.nouvellePartie();
			jeu.initialiserPhase1();
			jeu.joueurCommence(0);
			gagnant = jeu.gagnant();
			while (!jeu.finDePartie()) {
				Carte c = joueurs[jeu.joueurCourant()].determineCoup( );
				if (jeu.carteJouable(c)) jeu.joueCarte(c);
				else System.err.printf("E: L'ia joue une carte qui n'est pas dans sa main [%d] %s\n", jeu.joueurCourant(), c);
			}
			gagnant = jeu.gagnant();
			if (gagnant > 0) res[ gagnant - 1 ] += 1;
			i += 1;
		}
	}
}
