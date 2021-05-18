package Vue;

import java.awt.BorderLayout;

import javax.swing.JComponent;

import Modele.Jeu;

public class PlateauDeJeu  extends JComponent{
	
	Jeu jeu;
	CollecteurEvenements controle;
	
	JeuGraphiqueSwing jg;
	HautDePlateau haut;
	
	
	public PlateauDeJeu(CollecteurEvenements c, Jeu j) {
		controle = c;
		jeu = j;
		
		jg = new JeuGraphiqueSwing(jeu);
		jg.addMouseListener(new AdaptateurSouris(jg, controle));
		//frame.addKeyListener(new AdaptateurClavier(controle));
		
		haut = new HautDePlateau(controle);
	}

	public void metAJour() {
		jg.repaint();
		haut.repaint();
	}
	
	public void afficher() {
		haut.setVisible(true);
		jg.setVisible(true);
		this.add(haut,BorderLayout.PAGE_START);
		this.add(jg,BorderLayout.CENTER);
	}
}
