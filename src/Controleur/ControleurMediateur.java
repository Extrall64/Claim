package Controleur;

import IA.IA;
import IA.IAAleatoire;
import IA.IAHeuristique;
import IA.IAMinMax;
import Modele.Carte;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

public class ControleurMediateur implements CollecteurEvenements{
	
    // les diffirentes valeur des modes de jeu
    public static final int HUMAIN_VS_HUMAIN = 1;
    public static final int HUMAIN_VS_IA = 2;
    public static final int IA_VS_IA = 3;
    public static final int HUMAIN_VS_HUMAIN_RESEAU = 4;
    
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
			jeu.joueCarte(carte);
			tourIA();
		}
	}
	
	public void nouvelle_partie() {
		inter.afficherPlateau();
		jeu.nouvellePartie();
	}
	
	public void nouvelle_partie_ia() {
		inter.afficherPlateau();
		jeu.nouvellePartie();
		tourIA();
	}
	
	public void nouvelle_ia_alea() {
		joueurs[1] = new IAAleatoire(jeu,1);
		nouvelle_partie_ia();
	}
	
	public void nouvelle_ia_h() {
		//joueurs[1] = new IAHeuristique(jeu,1);
	}
	
	public void nouvelle_ia_minmax() {
		joueurs[1] = new IAMinMax(jeu,1,5);
		nouvelle_partie_ia();
	}
	
	public void tourIA() {
		if(!jeu.finDePartie() && joueurs[jeu.joueurCourant()] != null) {//tour de l'ia
			Carte c = joueurs[jeu.joueurCourant()].determineCoup();
			if(jeu.carteJouable(c))
					jeu.joueCarte(c);
			else
				System.err.println("erreur l'ia joue une carte qui n'est pas dans sa main");
		}
	}
	
	public void menu() {
		jeu.setSurMenu();
		inter.afficherMenu();
	}

	public void commande(String c) {
		switch (c) {
			case "humain_vs_humain":
				nouvelle_partie();
				break;
			case "humain_vs_ia_alea":
				nouvelle_ia_alea();
				break;
			case "humain_vs_ia_heuristique":
				nouvelle_ia_h();
				break;
			case "humain_vs_ia_minmax":
				nouvelle_ia_minmax();
				break;
			case "menu":
				menu();
				break;
			default:
			
		}
	}

	@Override
	public void fixerInterfaceUtilisateur(InterfaceUtilisateur i) {
		inter = i;
	}

	@Override
	public void tictac() {
		inter.metAJour();
	}

}
