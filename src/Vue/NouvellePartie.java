package Vue;

import java.awt.*;
import java.io.File;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import Global.Configuration;
import Modele.Jeu;

public class NouvellePartie extends JComponent {
	int largeur, hauteur,margeL,margeH;
	Graphics2D drawable;

	CollecteurEvenements controle;
	int mode;
	
	ImageClaim fond;
	JButton jouer,retour;
	JLabel nom1,nom2,ia1,ia2,joueur1,joueur2,quiCommence;
	JComboBox joueurCommence,choixia1,choixia2;
	JTextField choixnom1,choixnom2;

	ImageClaim livre,nvlpartie;
	
	public NouvellePartie(CollecteurEvenements c) {
		controle = c;
		initilaiser();
		livre = ImageClaim.getImageClaim("Image/livreOuvert.png");
		nvlpartie = ImageClaim.getImageClaim("Image/nvlPartie.png");
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
		Font font = new Font("Comic Sans MS", Font.ITALIC | Font.BOLD, 15);
		lab.setFont(font);
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

		margeL = largeur/40;
		margeH = hauteur/40;

		drawable.clearRect(0, 0, largeur, hauteur); //efface tout
		drawable.drawImage(fond.image(), 0, 0, largeur, hauteur, null);
		drawable.drawImage(livre.image(),2*margeL,8*margeH,36*margeL,32*margeH,null);
		drawable.drawImage(nvlpartie.image(),12*margeL,margeH,16*margeL,7*margeH,null);
		ajuster();
	}
	
	private void ajuster() {
		joueur1.setBounds(7*margeL,11*margeH,12*margeL,2*margeH);
		joueur2.setBounds(24*margeL,11*margeH,12*margeL,2*margeH);

		nom1.setBounds(7*margeL, 15*margeH, 5*margeL, 2*margeH);
		nom2.setBounds(24*margeL, 15*margeH, 5*margeL, 2*margeH);

		choixnom1.setBounds(10*margeL, 15*margeH, 6*margeL, 2*margeH);
		choixnom2.setBounds(27*margeL, 15*margeH, 6*margeL, 2*margeH);

		ia1.setBounds(7*margeL, 19*margeH, 5*margeL, 2*margeH);
		ia2.setBounds(24*margeL, 19*margeH, 5*margeL, 2*margeH);

		choixia1.setBounds(10*margeL, 19*margeH, 6*margeL, 2*margeH);
		choixia2.setBounds(27*margeL, 19*margeH, 6*margeL, 2*margeH);

		quiCommence.setBounds(22*margeL, 25*margeH, 10*margeL, 2*margeH);
		joueurCommence.setBounds(29*margeL, 25*margeH, 5*margeL, 2*margeH);

		jouer.setBounds(29*margeL, 30*margeH, 6*margeL, 2*margeH);
		retour.setBounds(7*margeL, 30*margeH, 6*margeL, 2*margeH);
	}
	
	private void initilaiser() {
		
		fond = chargeImage("fond");

		jouer = createButton("Jouer", "demarrer_partie");
		this.add(jouer);
		retour = createButton("Accueil", "retour-menu");
		this.add(retour);


		nom1 = createLabel("Nom :");
		this.add(nom1);
		nom2 = createLabel("Nom :");
		this.add(nom2);
		ia1 = createLabel("IA :");
		this.add(ia1);
		ia2 = createLabel("IA :");
		this.add(ia2);
		quiCommence = createLabel("Qui commence ?");
		this.add(quiCommence);
		joueur1 = createLabel("Joueur 1 :");
		this.add(joueur1);
		joueur2 = createLabel("Joueur 2 :");
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
		choixnom1.setDocument(new LimitJTextField(12));
		choixnom1.setText("Joueur 1");
		this.add(choixnom1);

		choixnom2 = new JTextField(20);
		choixnom2.setDocument(new LimitJTextField(12));
		choixnom2.setText("Joueur 2");
		this.add(choixnom2);

	}

	class LimitJTextField extends PlainDocument {
		private int max;

		LimitJTextField(int max) {
			super();
			this.max = max;
		}

		public void insertString(int offset, String text, AttributeSet attr) throws BadLocationException {
			if (text == null)
				return;
			if ((getLength() + text.length()) <= max) {
				super.insertString(offset, text, attr);
			}
		}
	}
}
