package Controleur;

import Global.Configuration;
import Modele.Jeu;
import Structures.Sequence;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

public class ControleurMediateur implements CollecteurEvenements{
	Jeu jeu;
	InterfaceUtilisateur inter;
	
	public ControleurMediateur(Jeu j) {
		jeu = j;
	}
	@Override
	public void clicSouris(int l, int c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean commande(String c) {
		// TODO Auto-generated method stub
		return false;
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
