package Vue;

import Modele.Carte;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class AdaptateurMotionSouris implements MouseMotionListener {
	JeuGraphiqueSwing jg;
	CollecteurEvenements controle;
	Carte estSelect;
	boolean carteOk,estSurZoneDrop;

	public AdaptateurMotionSouris(JeuGraphiqueSwing j, CollecteurEvenements c) {
		jg = j;
		controle = c;
		carteOk = false;
		estSurZoneDrop = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(jg.estCarteOk()){
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
	public void mouseMoved(MouseEvent e) {}
}
