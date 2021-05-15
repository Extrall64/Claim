package Controleur;

import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

public class ControleurMediateur implements CollecteurEvenements{
	Jeu jeu;
	InterfaceUtilisateur inter;
	
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
		
	}

	public void commande(String c) {
		switch (c) {
			case "nouvelle_partie":
				
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
