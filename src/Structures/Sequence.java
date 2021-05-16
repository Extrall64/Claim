package Structures;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface Sequence<Toto> extends Serializable {
	int taille();
	void insereTete(Toto element);
	void insereQueue(Toto element);
	Toto extraitTete();
	boolean estVide();
	Iterateur<Toto> iterateur();


}
