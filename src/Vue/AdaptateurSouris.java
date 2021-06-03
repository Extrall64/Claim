package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Modele.Carte;


public class AdaptateurSouris extends MouseAdapter {
	JeuGraphiqueSwing jg;
	CollecteurEvenements controle;

	public AdaptateurSouris(JeuGraphiqueSwing j, CollecteurEvenements c) {
		jg = j;
		controle = c;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Carte t = jg.determinerCarte(e.getX(),e.getY());
		if (t != null) {
			controle.clicSouris(t);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		/*System.out.println("OK");
		int c = e.getX() / jg.largeurCase();
		int l = e.getY() / jg.hauteurCase();*/
	}
}
