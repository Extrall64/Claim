package Vue;

import Modele.Carte;

public interface CollecteurEvenements {
		void clicSouris(Carte carte);
		void commande(String c);
		void lancer_partie(String ia1,String ia2,String joueur,String nom1,String nom2);
		void fixerInterfaceUtilisateur(InterfaceUtilisateur i);
		void tictac();
		//void jouerCarte(Carte t);
}