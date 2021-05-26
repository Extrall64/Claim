package Vue;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import Global.Configuration;

public class Menu extends JComponent{
	int largeur, hauteur;
	Graphics2D drawable;

	CollecteurEvenements controle;
	
	ImageClaim fond;
	JButton nouvellePartie_h_vs_h,nouvellePartie_ia_vs_ia,nouvellePartie_h_vs_ia,test_ia;
	JButton charger,regle,aide,parametre;
	JLabel titre,nouvelle_partie;
	
	public Menu(CollecteurEvenements c) {
		controle = c;
		initilaiser();
	}
	
	private ImageClaim chargeImage(String nom) {
		InputStream in = Configuration.charge("Image" + File.separator + nom + ".jpg");
		return ImageClaim.getImageClaim(in);
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
	
	public void paintComponent(Graphics g) {
		drawable = (Graphics2D) g;

		largeur = getSize().width;
		hauteur = getSize().height;

		drawable.clearRect(0, 0, largeur, hauteur); //efface tout
		drawable.drawImage(fond.image(), 0, 0, largeur, hauteur, null);
		ajuster();
	}
	
	private void ajuster() {
		int btnL = largeur/17 * 3;
		int btnH = hauteur/12;
		int orgX = largeur/17 * 2;
		int orgY = hauteur/24;
		
		titre.setBounds(largeur/2 - btnL/2, orgY, btnL, btnH);
		nouvelle_partie.setBounds(orgX, hauteur/3 - orgY, btnL, btnH);
		
		nouvellePartie_h_vs_h.setBounds(orgX, hauteur/3 + orgY, btnL, btnH);
		nouvellePartie_h_vs_ia.setBounds(orgX, hauteur/3 + orgY * 2 + btnH, btnL, btnH);
		nouvellePartie_ia_vs_ia.setBounds(orgX, hauteur/3 + orgY * 3 + btnH *2, btnL, btnH);
		test_ia.setBounds(orgX,hauteur/3 + orgY * 4 + btnH * 3, btnL, btnH);
		
		charger.setBounds(orgX * 2 + btnL, hauteur/3 + orgY, btnL, btnH);
		
		regle.setBounds(orgX * 3 + btnL * 2, hauteur/3 + orgY * 2 + btnH, btnL, btnH);
		aide.setBounds(orgX * 3 + btnL * 2, hauteur/3 + orgY * 3 + btnH *2, btnL, btnH);
		parametre.setBounds(orgX * 3 + btnL * 2, hauteur/3 + orgY, btnL, btnH);
	}
	private void initilaiser() {
		
		fond = chargeImage("fond");

		nouvellePartie_h_vs_h = createButton("Humain VS Humain", "humain_vs_humain");
		this.add(nouvellePartie_h_vs_h);
	
		nouvellePartie_ia_vs_ia = createButton("IA VS IA", "ia_vs_ia");
		this.add(nouvellePartie_ia_vs_ia);
		
		nouvellePartie_h_vs_ia = createButton("Humain VS IA", "humain_vs_ia");
		this.add(nouvellePartie_h_vs_ia);
		
		test_ia = createButton("Test IA VS IA", "test_ia");
		this.add(test_ia);

		charger = createButton("Charger", "charger");
		this.add(charger);
		
		regle = createButton("Regle", "regle");
		this.add(regle);
		
		aide = createButton("Aide", "aide");
		this.add(aide);
		
		parametre = createButton("Parametre", "parametre");
		this.add(parametre);
		
		titre = createLabel("MENU");
		this.add(titre);
		
		nouvelle_partie = createLabel("Nouvelle Partie :");
		this.add(nouvelle_partie);
	}	
}
