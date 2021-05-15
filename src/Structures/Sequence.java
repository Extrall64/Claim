package Structures;

public interface Sequence<Toto> {
	int taille();
	void insereTete(Toto element);
	void insereQueue(Toto element);
	Toto extraitTete();
	boolean estVide();
	Iterateur<Toto> iterateur();
}
