package Vue;

import java.awt.*;
import java.io.File;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import Global.Configuration;

public class Menu extends JComponent{
	int largeur, hauteur,margeL,margeH;
	Graphics2D drawable;

	CollecteurEvenements controle;
	
	ImageClaim fond,claim;
	JButton nouvellePartie_h_vs_h,nouvellePartie_ia_vs_ia,nouvellePartie_h_vs_ia,test_ia;
	JButton charger,regle;
	JLabel nouvelle_partie;
	JButton aide,parametre;

	public Menu(CollecteurEvenements c) {
		controle = c;
		initilaiser();
	}
	
	private ImageClaim chargeImage(String nom) {
		InputStream in = Configuration.charge("Image" + File.separator + nom + ".jpg");
		return ImageClaim.getImageClaim("Image/"+ nom + ".jpg");
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

		margeL = largeur/40;
		margeH = hauteur/40;

		drawable.clearRect(0, 0, largeur, hauteur); //efface tout
		drawable.drawImage(fond.image(), 0, 0, largeur, hauteur, null);
		drawable.drawImage(claim.image(), 12*margeL, 0, 16*margeL, 12*margeH, null);
		ajuster();
	}
	
	private void ajuster() {
		int btnL = largeur/17 * 3;
		int btnH = hauteur/12;
		int orgX = largeur/17 * 2;
		int orgY = hauteur/24;

		nouvelle_partie.setBounds(orgX, hauteur/3 - orgY, btnL, btnH);
		
		nouvellePartie_h_vs_h.setBounds(orgX, hauteur/3 + orgY, btnL, btnH);
		nouvellePartie_h_vs_ia.setBounds(orgX, hauteur/3 + orgY * 2 + btnH, btnL, btnH);
		nouvellePartie_ia_vs_ia.setBounds(orgX, hauteur/3 + orgY * 3 + btnH *2, btnL, btnH);
		test_ia.setBounds(orgX,hauteur/3 + orgY * 4 + btnH * 3, btnL, btnH);
		
		charger.setBounds(orgX * 2 + btnL, hauteur/3 + orgY, btnL, btnH);
		
		regle.setBounds(orgX * 3 + btnL * 2, hauteur/3 + orgY, btnL, btnH);
		//aide.setBounds(orgX * 3 + btnL * 2, hauteur/3 + orgY * 3 + btnH *2, btnL, btnH);
		//parametre.setBounds(orgX * 3 + btnL * 2, hauteur/3 + orgY, btnL, btnH);
	}
	private void initilaiser() {
		
		fond = chargeImage("fond");
		claim = ImageClaim.getImageClaim("Image/claimLogo.png");

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
		
		regle = createButton("Regles", "regle");
		this.add(regle);

		/*
		aide = createButton("Aide", "aide");
		this.add(aide);
		parametre = createButton("Parametre", "parametre");
		this.add(parametre);
		*/

		nouvelle_partie = createLabel("Nouvelle Partie :");
		this.add(nouvelle_partie);
	}	
}
