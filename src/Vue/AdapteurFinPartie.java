package Vue;

import Modele.Jeu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdapteurFinPartie implements ActionListener{
	CollecteurEvenements controle;
	FinDePartie f;

	AdapteurFinPartie(CollecteurEvenements c, FinDePartie fin) {
		controle = c;
		f = fin;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Rejouer")) {
			controle.commande("nouvellePartie");
		}else{
			controle.commande("retour-menu");
		}
	}
}