package Vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Modele.*;

public class AdaptateurTemps implements ActionListener {
	private CollecteurEvenements controle;
	
	public AdaptateurTemps(CollecteurEvenements c) {
		controle = c;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controle.tictac();
	}
}
