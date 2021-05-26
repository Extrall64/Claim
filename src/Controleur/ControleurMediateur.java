package Controleur;


import IA.IA;
import IA.IAAleatoire;
import IA.IAHeuristique;
import IA.IAMinMax;
import IA.Test;
import Modele.Carte;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

public class ControleurMediateur implements CollecteurEvenements{
    
	Jeu jeu;
	InterfaceUtilisateur inter;
	IA[] joueurs;
	
	public ControleurMediateur(Jeu j) {
		jeu = j;
		joueurs = new IA[2];
	}
	
	@Override
	public void clicSouris(int carte) {
		
		
	}
	
	public void jouerCarte(Carte carte) {
		boolean x = jeu.carteJouable(carte);
		if (x) {
			jeu.jouerCarte(carte);
			if(jeu.combatPret()) { 
				//temporisation();
				jeu.combat(); 
			}
			else {
				jeu.changeJoueur();
			}
			tourIA();
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
		else
			joueurs[j] = new IAHeuristique(jeu,j,6);
	}
	
	public void tourIA() {
		if(!jeu.finDePartie() && joueurs[jeu.joueurCourant()] != null) {//tour de l'ia
			Carte c = joueurs[jeu.joueurCourant()].determineCoup();
			if(jeu.carteJouable(c)) {
				jouerCarte(c);
			}
			else {
				System.err.println("erreur l'ia joue une carte qui n'est pas dans sa main");
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
				Test test = new Test( jeu );
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
		inter.metAJour();
	}
	
	public void temporisation() {
		long temps = System.currentTimeMillis();
		while(System.currentTimeMillis() - temps < 1000) {
			inter.metAJour();
		}
	}
}
