package Vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import Global.Configuration;

public class HautDePlateau extends Box{
	int largeur, hauteur;
	Graphics2D drawable;

	CollecteurEvenements controle;
	
	JButton menu;
	//JLabel titre,nouvelle_partie;
	
	public HautDePlateau(CollecteurEvenements c) {
		super(BoxLayout.LINE_AXIS);
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

		//drawable.clearRect(0, 0, largeur, hauteur); //efface tout
	}
	
	private void initilaiser() {
		this.setBackground(Color.DARK_GRAY);
		menu = createButton("Menu", "menu");
		this.add(menu);
	}	
}
