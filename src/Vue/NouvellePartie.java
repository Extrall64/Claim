package Vue;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Global.Configuration;
import Modele.Jeu;

public class NouvellePartie extends JComponent {
	int largeur, hauteur;
	Graphics2D drawable;

	CollecteurEvenements controle;
	int mode;
	
	ImageClaim fond;
	JButton jouer;
	JLabel nouvelle_partie,nom1,nom2,ia1,ia2,joueur1,joueur2,joueur;
	JComboBox joueurCommence,choixia1,choixia2;
	JTextField choixnom1,choixnom2;
	
	public NouvellePartie(CollecteurEvenements c) {
		controle = c;
		initilaiser();
	}
	
	public void init(int m) {
		mode = m;
		ia1.setVisible(true);
		choixia1.setVisible(true);
		ia2.setVisible(true);
		choixia2.setVisible(true);
		if(m == Jeu.HUMAIN_VS_IA) {
			ia1.setVisible(false);
			choixia1.setVisible(false);
		}
		else if(m == Jeu.HUMAIN_VS_HUMAIN) {
			ia1.setVisible(false);
			choixia1.setVisible(false);
			ia2.setVisible(false);
			choixia2.setVisible(false);
		}
	}
	
	private ImageClaim chargeImage(String nom) {
		//InputStream in = Configuration.charge("Image" + File.separator + nom + ".jpg");
		return ImageClaim.getImageClaim("Image/" + nom + ".jpg");
	}
	
	private JLabel createLabel(String s) {
		JLabel lab = new JLabel(s);
		lab.setAlignmentX(Component.CENTER_ALIGNMENT);
		return lab;
	}
	
	private JButton createButton(String s, String c) {
		JButton but = new JButton(s);
		but.addActionListener(new AdapteurNouvellePartie(controle, this));
		but.setAlignmentX(Component.CENTER_ALIGNMENT);
		but.setFocusable(false);
		return but;
	}
	
	private JTextField createTextField() {
		return null;
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
		
		nouvelle_partie.setBounds(largeur/2 - btnL/2, orgY, btnL, btnH);
		joueur1.setBounds(largeur/4 - btnL/2, orgY *3, btnL, btnH);
		joueur2.setBounds(largeur/4 * 3 - btnL/2, orgY*3, btnL, btnH);
		
		nom1.setBounds(largeur/6 - btnL/2, orgY *6 , btnL, btnH);
		nom2.setBounds(largeur/6 * 4 - btnL/2, orgY*6, btnL, btnH);
		choixnom1.setBounds(largeur/6 * 2 - btnL/2, orgY*6, btnL, btnH);
		choixnom2.setBounds(largeur/6 * 5 - btnL/2, orgY*6, btnL, btnH);
		
		ia1.setBounds(largeur/6 - btnL/2, orgY*9, btnL, btnH);
		ia2.setBounds(largeur/6 *4 - btnL/2, orgY*9, btnL, btnH);
		choixia1.setBounds(largeur/6 *2 - btnL/2, orgY*9, btnL, btnH);
		choixia2.setBounds(largeur/6 * 5 - btnL/2, orgY*9, btnL, btnH);
		
		joueur.setBounds(largeur/3 - btnL/2, orgY * 12, btnL, btnH);
		joueurCommence.setBounds(largeur/3 *2 - btnL/2, orgY*12, btnL, btnH);
		
		jouer.setBounds(largeur/2 - btnL/2, orgY*18, btnL, btnH);
		
	}
	
	private void initilaiser() {
		
		fond = chargeImage("fond");

		jouer = createButton("Jouer", "demarrer_partie");
		this.add(jouer);
		
		nouvelle_partie = createLabel("NOUVELLE PARTIE");
		this.add(nouvelle_partie);
		nom1 = createLabel("Nom :");
		this.add(nom1);
		nom2 = createLabel("Nom :");
		this.add(nom2);
		ia1 = createLabel("IA :");
		this.add(ia1);
		ia2 = createLabel("IA :");
		this.add(ia2);
		joueur = createLabel("Joueur Commence :");
		this.add(joueur);
		joueur1 = createLabel("Joueur 1");
		this.add(joueur1);
		joueur2 = createLabel("Joueur 2");
		this.add(joueur2);
		
		String[] ia = { "Facile", "Moyen", "Difficile","Difficile +" };
		choixia1 = new JComboBox(ia);
		choixia1.setSelectedIndex(0);
		this.add(choixia1);
		choixia2 = new JComboBox(ia);
		choixia2.setSelectedIndex(0);
		this.add(choixia2);
		String[] joueur = { "Aleatoire", "Joueur1", "Joueur2" };
		joueurCommence = new JComboBox(joueur);
		joueurCommence.setSelectedIndex(0);
		this.add(joueurCommence);
		
		choixnom1 = new JTextField(20);
		choixnom1.setText("Joueur 1");
		this.add(choixnom1);
		choixnom2 = new JTextField(20);
		choixnom2.setText("Joueur 2");
		this.add(choixnom2);
	}	
}
