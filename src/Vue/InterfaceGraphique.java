package Vue;

import java.awt.BorderLayout;
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
	
	boolean maximized;
	
	Menu menu;
	JeuGraphiqueSwing jg;
	HautDePlateau haut;
	
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
		
		haut = new HautDePlateau(controle);
		
		Timer time = new Timer(16, new AdaptateurTemps(controle));//timer
		time.start();
		controle.fixerInterfaceUtilisateur(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		frame.setVisible(true);
		
		afficherMenu();
	}

	@Override
	public void metAJour() {
		menu.repaint();
		jg.repaint();
		haut.repaint();
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
		frame.add(menu);
		menu.setVisible(true);
		frame.setVisible(true);
	}
	
	public void afficherPlateau() {
		masquer();
		frame.add(haut,BorderLayout.PAGE_START);
		haut.setVisible(true);
		frame.add(jg);
		jg.setVisible(true);
		frame.setVisible(true);
	}
	
	public void masquer() {
		frame.getContentPane().removeAll();
	}	
}
