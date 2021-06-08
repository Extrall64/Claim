package Modele;

public class Action extends Commande {
	Plateau courant;
	Plateau copie;
	
	public Action(Plateau courant, Plateau copie) {
		this.courant = courant;
		this.copie = copie;
	}

	void remplace() {
		Plateau p = courant.clone();
		courant.remplace(copie);
		copie = p.clone();
	}
}
