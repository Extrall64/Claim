package Vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import Modele.Jeu;

public class AdapteurNouvellePartie implements ActionListener{
	CollecteurEvenements controle;
	NouvellePartie n;
	
	AdapteurNouvellePartie(CollecteurEvenements c, NouvellePartie nouv) {
		controle = c;
		n = nouv;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String ia1,ia2,joueur;
		String nom1,nom2;
		ia1 = (String) n.choixia1.getSelectedItem();
		ia2 = (String) n.choixia2.getSelectedItem();
		joueur = (String) n.joueurCommence.getSelectedItem();
		nom1 =  n.choixnom1.getText();
		nom2 =  n.choixnom2.getText();
		if(n.mode == Jeu.HUMAIN_VS_HUMAIN) {
			ia1 = null;
			ia2 = null;
		}
		else if(n.mode == Jeu.HUMAIN_VS_IA) {
			ia1 = null;
		}
		controle.lancer_partie(ia1,ia2,joueur,nom1,nom2);	
	}
}