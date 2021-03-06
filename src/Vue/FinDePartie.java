package Vue;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;

import Modele.Carte;
import Modele.Jeu;
import Modele.Plateau;

import static java.lang.Math.min;

public class FinDePartie extends JComponent{
    int largeur, hauteur,margeL,margeH,carteL,carteH;
    Graphics2D drawable;
    CollecteurEvenements controle;
    Jeu jeu;

    ImageClaim fond,j1,j2,claim,nain,dop,chev,gob,mor;
    JButton rejouerMemeReglages,menu;
    JLabel nom1,nom2,gagnantTxt,egaliteTxt;
    int gagnant;

    int[] cartesJ0;
    int[] cartesJ1;
    String n1,n2;

    public FinDePartie(CollecteurEvenements c,Jeu j) {
        cartesJ0 = new int[5];
        cartesJ1 = new int[5];
        jeu = j;
        controle = c;
        initilaiser();
    }

    private ImageClaim chargeImage(String nom) {
        return ImageClaim.getImageClaim("Image/" + nom + ".jpg");
    }

    private ImageClaim chargeCarte(String nom) {
        return ImageClaim.getImageClaim("carte/" + nom + ".jpg");
    }

    private JButton createButton(String s, String c) {
        JButton but = new JButton(s);
        but.addActionListener(new AdapteurFinPartie(controle, this));
        but.setAlignmentX(Component.CENTER_ALIGNMENT);
        but.setFocusable(false);
        return but;
    }

    private JLabel createLabel(String s) {
        JLabel lab = new JLabel(s);
        lab.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lab;
    }

    private JLabel setFont(JLabel lab,int taille) {
        Font font = new Font("Comic Sans MS", Font.ITALIC | Font.BOLD, taille);
        lab.setFont(font);
        return lab;
    }

    public void grise(int x,int y,int larg,int haut){
        drawable.setColor(new Color(96,96,96,170));
        drawable.fillRect(x,y,larg,haut);
        drawable.setColor(Color.black);
    }

    public void init(){
        gagnant = jeu.gagnant();
        n1 = jeu.getJoueur(0).getNom();
        n2 = jeu.getJoueur(1).getNom();

        if(nom1!=null){
            this.remove(nom1);
        }
        if(nom2!=null){
            this.remove(nom2);
        }

        nom1 = createLabel(n1);
        this.add(nom1);
        nom2 = createLabel(n2);
        this.add(nom2);

        for(int i=0;i<5;i++){
            cartesJ0[i]=0;
            cartesJ1[i]=0;
        }

        Carte c;
        List<Carte> l = jeu.getJoueur(0).getScore();
        Iterator<Carte> it = l.iterator();
        while(it.hasNext()){
            c = it.next();
            cartesJ0[c.getFaction()] += 1;
        }

        l = jeu.getJoueur(1).getScore();
        it = l.iterator();
        while(it.hasNext()){
            c = it.next();
            cartesJ1[c.getFaction()] += 1;
        }
    }

    public void paintComponent(Graphics g) {
        drawable = (Graphics2D) g;

        largeur = getSize().width;
        hauteur = getSize().height;

        margeL = largeur/40;
        margeH = hauteur/40;

        carteH = 10*margeH;
        carteL = 4*margeL;

        drawable.clearRect(0, 0, largeur, hauteur); //efface tout
        drawable.drawImage(fond.image(), 0, 0, largeur, hauteur, null);
        drawable.drawImage(claim.image(), 0, 0, 8*margeL, 8*margeH, null);
        ajuster();
    }

    private void traceImage(ImageClaim im,int i,int faction){
        drawable.drawImage(im.image(),largeur-7*margeL - i*6*margeL,10*margeH,carteL,carteH,null);
        drawable.drawString("x "+cartesJ0[faction],largeur-7*margeL  - i*6*margeL,21*margeH);

        drawable.drawImage(im.image(),largeur-7*margeL - i*6*margeL,23*margeH,carteL,carteH,null);
        drawable.drawString("x "+cartesJ1[faction],largeur-7*margeL  - i*6*margeL,34*margeH);

        if(cartesJ0[faction] > cartesJ1[faction]){
            grise(largeur-7*margeL - i*6*margeL,23*margeH,carteL,carteH);
        }else if (cartesJ0[faction] < cartesJ1[faction]){
            grise(largeur-7*margeL - i*6*margeL,10*margeH,carteL,carteH);
        }
    }

    private void ajuster() {
        drawable.setColor(Color.black);
        drawable.drawLine(5*margeL,9*margeH,largeur-5*margeL,9*margeH);

        switch (gagnant){
            case 0:
                gagnantTxt.setVisible(false);
                egaliteTxt.setVisible(true);
                setFont(egaliteTxt,min(3*margeL,4*margeH));
                egaliteTxt.setBounds(16*margeL, 4*margeH, 16*margeL, 5*margeH);
                /*
                drawable.drawImage(j1.image(), 30*margeL, 4*margeH, 4*margeL, 4*margeH, null);
                drawable.drawImage(j2.image(), 35*margeL, 4*margeH, 4*margeL, 4*margeH, null);*/
                break;
            case 1:
                gagnantTxt.setVisible(true);
                egaliteTxt.setVisible(false);
                setFont(gagnantTxt,min(2*margeL,3*margeH));
                gagnantTxt.setBounds(8*margeL, 3*margeH, 11*margeL, 5*margeH);

                setFont(nom1,min(margeL,2*margeH));
                nom1.setBounds(18*margeL,3*margeH,18*margeL,5*margeH);
                /*
                drawable.drawImage(j1.image(), 30*margeL, 4*margeH, 4*margeL, 4*margeH, null);
                drawable.drawImage(j2.image(), 35*margeL, 4*margeH, 4*margeL, 4*margeH, null);
                grise(35*margeL, 4*margeH, 4*margeL, 4*margeH);*/
                break;
            case 2:
                gagnantTxt.setVisible(true);
                egaliteTxt.setVisible(false);
                setFont(gagnantTxt,min(2*margeL,3*margeH));
                gagnantTxt.setBounds(8*margeL, 3*margeH, 11*margeL, 5*margeH);

                setFont(nom2,min(margeL,2*margeH));
                nom2.setBounds(18*margeL,3*margeH,18*margeL,5*margeH);
                /*
                drawable.drawImage(j1.image(), 30*margeL, 4*margeH, 4*margeL, 4*margeH, null);
                drawable.drawImage(j2.image(), 35*margeL, 4*margeH, 4*margeL, 4*margeH, null);
                grise(30*margeL, 4*margeH, 4*margeL, 4*margeH);*/
                break;
            default:
                break;
        }

        Font font = new Font("Comic Sans MS", Font.ITALIC | Font.BOLD, min(margeL/2,margeH));
        drawable.setFont(font);

        drawable.drawImage(j1.image(), 2*margeL, 10*margeH, 5*margeL, 7*margeH, null);
        drawable.drawString(n1,2*margeL,18*margeH);
        drawable.drawImage(j2.image(), 2*margeL, 23*margeH, 5*margeL, 7*margeH, null);
        drawable.drawString(n2,2*margeL,31*margeH);

        traceImage(gob,0,Plateau.GLOBELINS);
        traceImage(dop,1,Plateau.DOPPELGANGERS);
        traceImage(chev,2,Plateau.CHEVALIERS);
        traceImage(mor,3,Plateau.MORTSVIVANTS);
        traceImage(nain,4,Plateau.NAINS);

        if(gagnant==1){
            grise(2*margeL, 23*margeH, 5*margeL, 7*margeH);
        }else if(gagnant==2){
            grise(2*margeL, 10*margeH, 5*margeL, 7*margeH);
        }

        rejouerMemeReglages.setBounds(28*margeL, 35*margeH, 8*margeL, 3*margeH);
        menu.setBounds(4*margeL, 36*margeH, 8*margeL, 3*margeH);
    }

    private void initilaiser() {
        fond = chargeImage("fond");
        j1 = chargeImage("j1");
        j2 = chargeImage("j2");
        claim = ImageClaim.getImageClaim("Image/claimLogo.png");
        nain = chargeCarte("nain_9");
        dop = chargeCarte("doppel_9");
        chev = chargeCarte("chevalier_9");
        mor = chargeCarte("mort-vivant_9");
        gob = chargeCarte("gobelin_9");

        gagnantTxt = createLabel("Gagnant : ");
        this.add(gagnantTxt);
        egaliteTxt = createLabel("Egalit?? ! ");
        this.add(egaliteTxt);

        rejouerMemeReglages = createButton("Rejouer", "Rejouer");
        this.add(rejouerMemeReglages);
        menu = createButton("Ecran d'accueil", "retour-menu");
        this.add(menu);
    }
}

