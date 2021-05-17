package Controleur;

import IA.IA;
import IA.IAAleatoire;
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
		inter.masquerMenu();
		inter.afficherPlateau();
		jeu.nouvellePartie();
	}
	
	public void nouvelle_partie_ia() {
		joueurs[1] = new IAAleatoire(jeu,1);
		inter.masquerMenu();
		inter.afficherPlateau();
		jeu.nouvellePartie();
		tourIA();
	}
	
	public void tourIA() {
		if(!jeu.finDePartie() && joueurs[jeu.joueurCourant()] != null) {//tour de l'ia
			jeu.joueCarte(joueurs[jeu.joueurCourant()].determineCoup());
		}
	}

	public void commande(String c) {
		switch (c) {
			case "humain_vs_humain":
				nouvelle_partie();
				break;
			case "humain_vs_ia_alea":
				nouvelle_partie_ia();
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
