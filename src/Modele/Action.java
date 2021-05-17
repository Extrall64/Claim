package Modele;

import Global.Configuration;
import Structures.Iterateur;
import Structures.Sequence;

public class Action extends Commande {
	Plateau courant;
	Plateau copie;
	
	public Action(Plateau courant, Plateau copie) {
		this.courant = courant;
		this.copie = copie;
	}

	void remplace() {
		courant.remplace(copie);
	}
}
