package Modele;

import Global.Configuration;
import Structures.Sequence;

public class Historique<E extends Commande> {
	Sequence<E> passe, futur;

	Historique() {
		passe = Configuration.instance().nouvelleSequence();
		futur = Configuration.instance().nouvelleSequence();
	}

	void videHisto(){
		passe = Configuration.instance().nouvelleSequence();
		futur = Configuration.instance().nouvelleSequence();
	}

	void nouveau(E c) {
		passe.insereTete(c);
		while (!futur.estVide())
			futur.extraitTete();
	}

	public boolean peutAnnuler() {
		return !passe.estVide();
	}

	E annuler() {
		if (peutAnnuler()) {
			E c = passe.extraitTete();
			c.remplace();
			futur.insereTete(c);
			return c;
		} else {
			return null;
		}
	}

	public boolean peutRefaire() {
		return !futur.estVide();
	}

	E refaire() {
		if (peutRefaire()) {
			E c = futur.extraitTete();
			c.remplace();
			passe.insereTete(c);
			return c;
		} else {
			return null;
		}
	}
}
