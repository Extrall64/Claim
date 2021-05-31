package Vue;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.InputStream;

import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JLabel;

import Global.Configuration;

public class FinDePartie extends JComponent {
	int largeur, hauteur;
	Graphics2D drawable;

	CollecteurEvenements controle;
	
	ImageClaim fond;
	JButton nouvellePartie_vs_humain,nouvellePartie_vs_ia_aleatoire,nouvellePartie_vs_ia_heuristique,nouvellePartie_vs_ia_minmax;
	JButton ia_vs_ia, test_ia;
	JButton charger,regle,tutoriel,parametre;
	JLabel titre,nouvelle_partie;
	
	public void Menu(CollecteurEvenements c) {
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
		int btnL = 240;
		int btnH = 40;
		int margeY = 25;
		int orgX = largeur/2 - btnL/2;
		int orgY = 25;
		nouvellePartie_vs_humain.setBounds(orgX, orgY, btnL, btnH);
		orgY += margeY + btnH;
		
		nouvellePartie_vs_ia_aleatoire.setBounds(orgX, orgY, btnL, btnH);
		orgY += margeY + btnH;

		nouvellePartie_vs_ia_heuristique.setBounds(orgX, orgY, btnL, btnH);
		orgY += margeY + btnH;

		nouvellePartie_vs_ia_minmax.setBounds(orgX, orgY, btnL, btnH);
		orgY += margeY + btnH;

		ia_vs_ia.setBounds(orgX, orgY, btnL, btnH);
		orgY += margeY + btnH;
		
		test_ia.setBounds(orgX, orgY, btnL, btnH);

	}
	private void initilaiser() {
		
		fond = chargeImage("fond");

		nouvellePartie_vs_humain = createButton("Humain VS Humain", "humain_vs_humain");
		this.add(nouvellePartie_vs_humain);
	
		nouvellePartie_vs_ia_aleatoire = createButton("Humain VS IA Aleatoire", "humain_vs_ia_alea");
		this.add(nouvellePartie_vs_ia_aleatoire);
		
		nouvellePartie_vs_ia_heuristique = createButton("Humain VS IA Heuristique", "humain_vs_ia_heuristique");
		this.add(nouvellePartie_vs_ia_heuristique);
		
		nouvellePartie_vs_ia_minmax = createButton("Humain VS IA MinMax", "humain_vs_ia_minmax");
		this.add(nouvellePartie_vs_ia_minmax);

		ia_vs_ia = createButton("IA VS IA", "ia_vs_ia");
		this.add(ia_vs_ia);

		test_ia = createButton("TEST IA", "test_ia");
		this.add(test_ia);
	}	
}
