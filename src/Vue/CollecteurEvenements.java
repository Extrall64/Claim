package Vue;

public interface CollecteurEvenements {
		void clicSouris(int carte);
		void commande(String c);
		void fixerInterfaceUtilisateur(InterfaceUtilisateur i);
		void tictac();
		void jouerCarte(int t);
}