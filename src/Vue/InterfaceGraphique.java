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
	
	boolean maximized;
	JFrame frame;
	JPanel menu,hautDePlateau,finPhase1,finDeJeu;//differents affichage
	
	JLabel joueurCourant;
	JButton nouvellePartie_vs_humain,nouvellePartie_vs_ia_aleatoire,nouvellePartie_vs_ia_normale,nouvellePartie_vs_ia_difficile;
	

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

	public void run() {	
		frame = new JFrame("Claim");
		jg = new JeuGraphiqueSwing(jeu);
		menu = new JPanel();
		
		frame.add(jg);
		
		hautDePlateau = new JPanel();
		hautDePlateau.setBackground(Color.GRAY);
		jg.setBackground(Color.GRAY);
		hautDePlateau.setVisible(true);
		jg.add(hautDePlateau,BorderLayout.PAGE_START);
		
		nouvellePartie_vs_humain = createButton("Humain VS Humain", "humain_vs_humain");
		nouvellePartie_vs_humain.setBounds(240, 20, 125, 50);
		menu.add(nouvellePartie_vs_humain);
		nouvellePartie_vs_ia_aleatoire = createButton("Humain VS IA Aleatoire", "humain_vs_ia_alea");
		nouvellePartie_vs_ia_aleatoire.setBounds(240, 20, 125, 50);
		menu.add(nouvellePartie_vs_ia_aleatoire);
		frame.add(menu);
		
		jg.addMouseListener(new AdaptateurSouris(jg, controle));
		//frame.addKeyListener(new AdaptateurClavier(controle));
		Timer time = new Timer(16, new AdaptateurTemps(controle));
		time.start();
		controle.fixerInterfaceUtilisateur(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		frame.setVisible(true);
		
		afficherMenu();
		masquerPlateau();
	}

	@Override
	public void metAJour() {
		jg.repaint();
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
		menu.setVisible(true);
	}
	
	public void masquerMenu() {
		menu.setVisible(false);
	}
	
	public void afficherPlateau() {
		hautDePlateau.setVisible(true);
		jg.add(hautDePlateau,BorderLayout.PAGE_START);
		jg.setVisible(true);
		frame.add(jg);
	}
	
	public void masquerPlateau() {
		jg.setVisible(false);
	}
	
}
