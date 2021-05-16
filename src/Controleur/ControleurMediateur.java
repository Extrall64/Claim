package Controleur;

import IA.IA;
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
	}
	
	@Override
	public void clicSouris(int carte) {
		
		
	}
	
	public void jouerCarte(int carte) {
		int x = jeu.carteJouable(carte);
		if (x != -1) {
			jeu.joueCarte(carte);
			//animation carte
		}
	}
	
	public void nouvelle_partie() {
		inter.masquerMenu();
		inter.afficherPlateau();
		jeu.nouvellePartie();
	}

	public void commande(String c) {
		switch (c) {
			case "nouvelle_partie":
				nouvelle_partie();
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
