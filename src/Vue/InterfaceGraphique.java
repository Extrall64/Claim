package Vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.*;

import Modele.Jeu;

public class InterfaceGraphique implements Runnable, InterfaceUtilisateur{
	Jeu jeu;
	CollecteurEvenements controle;
	JeuGraphiqueSwing jg;
	Menu menu;
	
	boolean maximized;
	JFrame frame;
	JPanel plateau,hautDePlateau,finPhase1,finDeJeu;//differents affichage
	
	JMenu bMenu;
	JLabel joueurCourant;
	
	Box haut;

	InterfaceGraphique(Jeu j, CollecteurEvenements c) {
		jeu = j;
		controle = c;
	}

	public static void demarrer(Jeu j, CollecteurEvenements c) {
		SwingUtilities.invokeLater(new InterfaceGraphique(j, c));
	}
	
	private JLabel createLabel(String s) {
		JLabel lab = new JLabel(s);
		lab.setAlignmentX(Component.CENTER_ALIGNMENT);
		return lab;
	}
	
	private JButton createButton(String s, String c) {
		JButton but = new JButton(s);
		but.addActionListener(new AdaptateurCommande(controle, c));
		but.setAlignmentX(Component.CENTER_ALIGNMENT);
		but.setFocusable(false);
		return but;
	}
	
	private JMenu createMenu() {
		JMenu m = new JMenu();
		return m;
	}

	public void run() {	
		frame = new JFrame("Claim");
		jg = new JeuGraphiqueSwing(jeu);
		
		
		creationDuPlateau();
		menu = new Menu(controle);
		
		jg.addMouseListener(new AdaptateurSouris(jg, controle));
		//frame.addKeyListener(new AdaptateurClavier(controle));
		Timer time = new Timer(16, new AdaptateurTemps(controle));
		time.start();
		controle.fixerInterfaceUtilisateur(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		frame.setVisible(true);
		
		frame.add(jg);
		frame.add(menu);
		
		
		afficherMenu();
		masquerPlateau();
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
		menu.afficher();
	}
	
	public void masquerMenu() {
		menu.masquer();
	}
	
	public void afficherPlateau() {
		haut.setVisible(true);
		jg.setVisible(true);
		frame.add(jg);
		//creationDuPlateau();
		/*plateau.setVisible(true);
		frame.add(plateau);*/
	}
	
	public void masquerPlateau() {
		haut.setVisible(false);
		jg.setVisible(false);
	}
	
	private void creationDuPlateau() {
		plateau = new JPanel();
		hautDePlateau = new JPanel();
		hautDePlateau.setBackground(Color.GRAY);
		plateau.add(hautDePlateau,BorderLayout.PAGE_START);
		haut = Box.createHorizontalBox();
		haut.add(createButton("Menu", "menu"));
		haut.add(Box.createGlue());
		haut.setVisible(false);
		
		bMenu = createMenu();
		haut.add(bMenu);
		
		frame.add(haut, BorderLayout.PAGE_START);
		
		frame.setVisible(true);
	}
	
}
