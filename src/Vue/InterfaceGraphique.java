package Vue;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import Modele.Jeu;

public class InterfaceGraphique implements Runnable, InterfaceUtilisateur{
	Jeu jeu;
	CollecteurEvenements controle;
	
	boolean maximized;
	
	Menu menu;
	PlateauDeJeu plateau;
	HautDePlateau haut;
	
	JFrame frame;

	InterfaceGraphique(Jeu j, CollecteurEvenements c) {
		jeu = j;
		controle = c;
	}

	public static void demarrer(Jeu j, CollecteurEvenements c) {
		SwingUtilities.invokeLater(new InterfaceGraphique(j, c));
	}

	public void run() {	
		frame = new JFrame("Claim");
		
		menu = new Menu(controle);
		plateau = new PlateauDeJeu(controle,jeu);
		
		Timer time = new Timer(16, new AdaptateurTemps(controle));//timer
		time.start();
		controle.fixerInterfaceUtilisateur(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		frame.setVisible(true);
		
		frame.getContentPane().add(menu);
		frame.getContentPane().add(plateau);
		
		afficherMenu();
	}

	@Override
	public void metAJour() {
		plateau.metAJour();
		menu.repaint();
	}
	
	@Override
	public void basculePleinEcran() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		if (maximized) {
			device.setFullScreenWindow(null);
			maximized = false;
		} else {
			device.setFullScreenWindow(frame);
			maximized = true;
		}
	}
	
	public void afficherMenu() {
		masquer();
		menu.setVisible(true);
	}
	
	public void afficherPlateau() {
		masquer();
		plateau.afficher();
		plateau.setVisible(true);
	}
	
	public void masquer() {
		menu.setVisible(false);
		plateau.setVisible(false);
	}	
}
