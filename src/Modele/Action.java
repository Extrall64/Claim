package Modele;

import Global.Configuration;
import Structures.Iterateur;
import Structures.Sequence;

public class Action extends Commande {
	Niveau courant;
	Niveau copie;
	
	public Action(Niveau courant, Niveau copie) {
		this.courant = courant;
		this.copie = copie;
	}

	void remplace() {
		courant.remplace(copie);
	}
}
