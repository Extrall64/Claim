package Vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import Modele.*;
import Patterns.*;

public class JeuGraphiqueSwing extends JComponent {
	int largeur, hauteur;
	Graphics2D drawable;
	VueJeu vue;

	public JeuGraphiqueSwing(Jeu j) {
		
	}

	@Override
	public void paintComponent(Graphics g) {
		drawable = (Graphics2D) g;

		largeur = getSize().width;
		hauteur = getSize().height;

		drawable.clearRect(0, 0, largeur, hauteur); //efface tout
		tracerNiveau();
	}
	
	void tracerNiveau() {
	}
	
}
