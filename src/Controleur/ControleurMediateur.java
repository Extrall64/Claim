package Controleur;


import IA.IA;
import IA.IAAleatoire;
import IA.IAHeuristique;
import IA.IAMinMax;
import IA.IAMonteCarlo;
import IA.Test;
import Modele.Carte;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

public class ControleurMediateur implements CollecteurEvenements{
    
	Jeu jeu;
	InterfaceUtilisateur inter;
	int decompte;
	public IA[] joueurs;
	public int [] etats;
	public static final int TEMPS = 60;
    public static final int LIBRE = 0;
    public static final int OCCUPEE = 1;
    
	public ControleurMediateur(Jeu j) {
		jeu = j;
		joueurs = new IA[2];
		etats = new int[2];
	}
	
	@Override
	public void clicSouris(int carte) {
		
		
	}
	
	public void jouerCarte(Carte carte) {
		if (joueurs[jeu.joueurCourant()] == null) {
			etats[jeu.joueurCourant()] = OCCUPEE;
			boolean x = jeu.carteJouable(carte);
			if (x) {
				jeu.joueCarte(carte);
		    	System.out.printf("[Humain] a joué son coup: %s\n", carte);
				etats[jeu.joueurCourant()] = LIBRE;
				decompte = TEMPS;
			}
		}
	}
	
	public void nouvelle_partie() {
		jeu.nouvellePartie();
		inter.nouvellePartie(Jeu.HUMAIN_VS_HUMAIN);
		jeu.mode(Jeu.HUMAIN_VS_HUMAIN);
	}
	
	public void nouvelle_partie_vs_ia() {
		jeu.nouvellePartie();
		inter.nouvellePartie(Jeu.HUMAIN_VS_IA);
		jeu.mode(Jeu.HUMAIN_VS_IA);
	}
	
	public void nouvelle_partie_ia_vs_ia() {
		jeu.nouvellePartie();
		inter.nouvellePartie(Jeu.IA_VS_IA);
		jeu.mode(Jeu.IA_VS_IA);
	}
	
	public void lancer_partie(String ia1,String ia2,String joueur,String nom1,String nom2) {
		initialiserIA(0,ia1);
		initialiserIA(1,ia2);
		
		if(joueur.equals("Aleatoire"))
			jeu.joueurAleatoire();
		else if(joueur.equals("Joueur 1"))
			jeu.joueurCommence(0);
		else
			jeu.joueurCommence(1);
		
		jeu.setNom(0, nom1);
		jeu.setNom(1, nom2);
		
		jeu.initialiserPhase1();
		inter.afficherPlateau();
	}
	
	public void initialiserIA(int j,String ia) {
		if(ia == null)
			joueurs[j] = null;
		else if(ia.equals("Aleatoire"))
			joueurs[j] = new IAAleatoire(jeu, j);
		else if(ia.equals("MinMax"))
			joueurs[j] = new IAMinMax(jeu,j,6);
		else if(ia.equals("Monte Carlo"))
			joueurs[j] = new IAMonteCarlo(jeu,j, 20);
		else
			joueurs[j] = new IAHeuristique(jeu,j,6);
	}
	
	public void tourIA() {
		if(!jeu.finDePartie() && joueurs[jeu.joueurCourant()] != null) {
			
			Carte c = joueurs[jeu.joueurCourant()].determineCoup( );
			if(jeu.carteJouable(c)) {
				etats[jeu.joueurCourant()] = OCCUPEE;
				jeu.joueCarte(c);
				etats[jeu.joueurCourant()] = LIBRE;
				decompte = TEMPS;
			}
			else { 
				System.err.printf("E: L'ia joue une carte qui n'est pas dans sa main [%d] %s\n", jeu.joueurCourant(), c);
			}
		}
	}
	
	public void menu() {
		inter.afficherMenu();
	}

	public void commande(String c) {
		switch (c) {
			case "humain_vs_humain":
				nouvelle_partie();
				break;
			case "humain_vs_ia":
				nouvelle_partie_vs_ia();
				break;
			case "ia_vs_ia":
				nouvelle_partie_ia_vs_ia();
				break;
			case "test_ia":
				Test test = new Test();
				test.demarrer( 50 );
				break;
			case "menu":
				menu();
				break;
			case "refaire":
				refaire();
				break;
			case "annuler":
				annuler();
				break;
			case "charger":
				break;
			case "regle":
				break;
			case "aide":
				break;
			case "parametre":
				break;
			default:
				break;
		}
	}
	
	public void refaire() {
		jeu.refaire();
		//tourIA();
	}
	
	public void annuler() {
		jeu.annule();
		//tourIA();
	}

	@Override
	public void fixerInterfaceUtilisateur(InterfaceUtilisateur i) {
		inter = i;
	}

	@Override
	public void tictac() {
		decompte = decompte - 1;
		// temporisation
		if (decompte < 0) {
			decompte = TEMPS;
			// par defaut appeler la tourIA si le joueur est une "IA" "non occupée" == LIBRE
			// elle joue son coup, sinon l'appel est ignoré
			if (!jeu.estSurMenu() && etats[ jeu.joueurCourant() ] == LIBRE )
				tourIA();
			if (jeu.finDePartie() && jeu.gagnant() != -1) {
				jeu.afficherResultat();
			}
		inter.metAJour();
		}
	}
	
	public void temporisation() {
		long temps = System.currentTimeMillis();
		while(System.currentTimeMillis() - temps < 1000) {
			inter.metAJour();
		}
	}
}
