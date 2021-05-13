package Vue;

import java.awt.Image;
import java.io.File;
import java.io.InputStream;

import Global.Configuration;
import Modele.Jeu;
import Modele.Niveau;

public class VueJeu {
	
	Jeu jeu;
	ImageClaim images[][]; //ex images[0][1] image gobelin de force 1
	JeuGraphique ng;
	int phase;

	private ImageClaim chargeImage(String nom) {
		ImageClaim img = null;
		InputStream in = Configuration.charge("carte" + File.separator + nom + ".png");
		return ImageClaim.getImageSokoban(in);
	}

	public VueJeu(Jeu j, JeuGraphique n) {
		images = new ImageClaim[5][10];
		for(int i=0;i<10;i++) {
			System.out.println("ok");
			images[Jeu.NAINS-1][i] = chargeImage("nain_"+i);
			images[Jeu.MORTSVIVANTS-1][i] = chargeImage("mort-vivant_"+i);
			images[Jeu.DOPPELGANGERS-1][i] = chargeImage("doppel_"+i);
			images[Jeu.GLOBELINS-1][i] = chargeImage("gobelin_"+i);
			if(i>=2) {
				images[Jeu.CHEVALIERS-1][i] = chargeImage("chevalier_"+i);
			}
		}
		jeu = j;
		ng = n;

		phase = 0;
	}

	void tracerNiveau() {
		ng.tracerImage(images[0][9], 50, 50, 200, 300);
	}
	
}
