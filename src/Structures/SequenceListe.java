package Structures;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Maillon<Tutu> {
	Tutu element;
	Maillon<Tutu> suivant;
}

public class SequenceListe<Titi> implements Sequence<Titi> {
	Maillon<Titi> tete, queue;
	int taille;

	public void insereTete(Titi element) {
		Maillon<Titi> nouveau = new Maillon<>();
		nouveau.element = element;
		nouveau.suivant = tete;
		if (tete == null) {
			tete = nouveau;
			queue = nouveau;
		} else {
			tete = nouveau;
		}
		taille++;
	}

	public void insereQueue(Titi element) {
		Maillon<Titi> nouveau = new Maillon<>();
		nouveau.element = element;
		nouveau.suivant = null;
		if (tete == null) {
			tete = nouveau;
			queue = nouveau;
		} else {
			queue.suivant = nouveau;
			queue = nouveau;
		}
		taille++;
	}

	public Titi extraitTete() {
		if (tete == null)
			throw new RuntimeException("Sequence vide !");
		Titi resultat = tete.element;
		tete = tete.suivant;
		// Ici, oubli de la mise à jour de la queue probablement sans conséquences :
		// la queue n'est incohérente qu'en cas de liste vide, dans ce cas pas d'itération
		// possible sur ses éléments et tout sera remis en cohérence à la prochaine insertion
		taille--;
		return resultat;
	}

	public boolean estVide() {
		return tete == null;
	}

	@Override
	public Iterateur<Titi> iterateur() {
		return new IterateurListe<>(this);
	}

	public String toString() {
		String resultat = "Sequence liste : [ ";
		Maillon<Titi> courant = tete;
		while (courant != null) {
			resultat += courant.element + " ";
			courant = courant.suivant;
		}
		resultat += "]";
		return resultat;
	}

	@Override
	public int taille() {
		return taille;
	}
	
	public void decrementeTaille() {
		taille--;
	}


	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.writeObject(taille);
		IterateurListe<Titi> it=new IterateurListe<>(this);
		while(it.aProchain()){
			oos.writeObject(it.prochain());
		}
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		taille=0;
		tete=null;
		queue=null;

		int t=(int)ois.readObject();

		for(int i=0;i<t;i++){
			Titi o=(Titi) ois.readObject();
			insereQueue(o);
		}
	}

}
