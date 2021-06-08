package Vue;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.*;

import Modele.Jeu;

public class InterfaceGraphique implements Runnable, InterfaceUtilisateur{
	Jeu jeu;
	CollecteurEvenements controle;
	
	boolean maximized;
	
	Menu menu;
	JeuGraphiqueSwing jg;
	HautDePlateau haut;
	NouvellePartie nouv;
	FinDePartie fin;
	Regle r;

	ImageClaim Icon;

	JFrame frame;

	InterfaceGraphique(Jeu j, CollecteurEvenements c) {
		jeu = j;
		controle = c;

		Icon = ImageClaim.getImageClaim("Image/iconPage.jpg");
	}

	public static void demarrer(Jeu j, CollecteurEvenements c) {
		SwingUtilities.invokeLater(new InterfaceGraphique(j, c));
	}

	public void run() {	
		frame = new JFrame("Claim");
		frame.setIconImage(Icon.image());

		r = new Regle();

		menu = new Menu(controle);
		nouv = new NouvellePartie(controle);

		jg = new JeuGraphiqueSwing(jeu);
		jg.addMouseListener(new AdaptateurSouris(jg, controle));
		jg.addMouseMotionListener(new AdaptateurMotionSouris(jg,controle));
		fin = new FinDePartie(controle,jeu);
		
		//haut = new HautDePlateau(controle);
		
		Timer time = new Timer(16, new AdaptateurTemps(controle));//timer
		time.start();
		controle.fixerInterfaceUtilisateur(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 500);
		frame.setVisible(true);
		
		afficherMenu();
	}

	@Override
	public void metAJour() {
		menu.repaint();
		jg.repaint();
		//haut.repaint();
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
	
	public void afficherPlateau(int mode) {
		masquer();
		haut = new HautDePlateau(controle,mode);
		frame.add(haut,BorderLayout.PAGE_START);
		haut.setVisible(true);
		frame.add(jg);
		jg.setVisible(true);
		frame.setVisible(true);
	}
	
	public void afficherNouvellePartie() {
		masquer();
		frame.add(nouv);
		nouv.setVisible(true);
		frame.setVisible(true);
	}
	
	public void masquer() {
		frame.getContentPane().removeAll();
		frame.setVisible(false);
	}
	
	public void nouvellePartie(int mode) {
		nouv.init(mode);
		afficherNouvellePartie();
	}

	@Override
	public void annuler() {
		jg.annuler();
	}

	public void afficherFinPartie(){
		masquer();
		fin.init();
		frame.add(fin);
		fin.setVisible(true);
		frame.setVisible(true);
	}

	public void afficherRegle(){
		r.setVisible(true);
	}
}
