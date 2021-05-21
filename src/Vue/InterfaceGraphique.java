package Vue;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import Modele.Jeu;

public class InterfaceGraphique implements Runnable, InterfaceUtilisateur{
	Jeu jeu;
	CollecteurEvenements controle;
	JeuGraphiqueSwing jg;
	
	boolean maximized;
	
	Menu menu;
	PlateauDeJeu plateau;
	
	JPanel panneau;
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
		
		jg = new JeuGraphiqueSwing(jeu);
		jg.addMouseListener(new AdaptateurSouris(jg, controle));
		
		Timer time = new Timer(16, new AdaptateurTemps(controle));//timer
		time.start();
		controle.fixerInterfaceUtilisateur(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		frame.setVisible(true);
		
		frame.add(jg);
		frame.add(menu);
		
		afficherMenu();
	}

	@Override
	public void metAJour() {
		jg.repaint();
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
		frame.add(menu);
		//frame.add(menu);
	}
	
	public void afficherPlateau() {
		masquer();
		jg.setVisible(true);
		frame.add(jg);
		//plateau.setVisible(true);
		//frame.add(plateau);
		//plateau.afficher();
	}
	
	public void masquer() {
		menu.setVisible(false);
		jg.setVisible(false);
		//frame.getContentPane().removeAll();
	}	
}
