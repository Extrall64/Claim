package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Modele.Carte;


public class AdaptateurSouris extends MouseAdapter {
	JeuGraphiqueSwing jg;
	CollecteurEvenements controle;
	Carte estSelect;
	boolean carteOk,estSurZoneDrop;

	public AdaptateurSouris(JeuGraphiqueSwing j, CollecteurEvenements c) {
		jg = j;
		controle = c;
		carteOk = false;
		estSurZoneDrop = false;
	}

	public void mousePressed(MouseEvent e) {
		estSelect = jg.determinerCarte(e.getX(),e.getY());
		if (estSelect != null) {
			controle.clicSouris(estSelect);
		}
	}

	/*
	@Override
	public void mousePressed(MouseEvent e) {
		estSelect = jg.determinerCarte(e.getX(),e.getY());
		if (estSelect != null) {
			carteOk = true;
		}else{
			carteOk = false;
		}
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("DRAG");
		if(carteOk){
			if(jg.okDrop(e.getX(),e.getY())){
				estSurZoneDrop = true;
			}else{
				estSurZoneDrop = false;
			}
		}else {
			estSurZoneDrop = false;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e){
		jg.estRelease();
		if(carteOk && estSurZoneDrop){
			controle.clicSouris(estSelect);
		}else{
			carteOk = false;
		}
	}*/
}
