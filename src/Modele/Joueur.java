package Modele;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import IA.IA;
import IA.IAMonteCarlo;
/*
* Joueur peut etre un humain ou une IA, joue soit via jouerHumain ou jouerAssistant
*/
public class Joueur implements Serializable {
	private List<Carte> main, partisans, score;
	private String nom;
	private int joueur;
	private Jeu config;
	private IA assistant;
	public Joueur(Jeu c, String n, int j, IA a) {
		config = c;
		nom = n;
		joueur = j;
		assistant = a;
	}

	public void setMain( List<Carte> l) { main = l;}
	public void setPartisans(List<Carte> l) { partisans = l;}
	public void setScore(List<Carte> l) { score = l;}
	public void setJeu(Jeu c){ config=c; }//TODO: faire aussi setJeu pour l'IA
	
	public Carte proposerCarte() {
		IA assistant =  new IAMonteCarlo(config, joueur, 50);
		Carte c = assistant.determineCoup();
		return c;
	}
	public void jouerHumain(Carte c) {
		config.jouerCarte(c);
		System.out.printf("[Humain] a joué son coup: %s\n", c);
	}

	public void jouerAssistant() {
		Carte c = assistant.determineCoup();
		if(config.carteJouable(c)) config.jouerCarte(c);
		else System.err.printf("E: L'ia joue une carte qui n'est pas dans sa main [%d] %s\n", joueur, c);

	}
	public void setAssistant( IA ia ) {
		assistant = ia;
	}
	
	public List<Carte> getMain() { return main; }
	public List<Carte> getPartisans() { return partisans; }
	public List<Carte> getScore() { return score; }
	public String getNom() { return nom; }
	public boolean estHumain() { return assistant == null; }

	//on redéfinit writeobject et readobject pour ne pas sauvegarder la config (sinon boucle infinie)
	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.writeObject(main);
		oos.writeObject(partisans);
		oos.writeObject(score);
		oos.writeObject(nom);
		oos.writeObject(joueur);
		//TODO:ecrire IA (sans sauver leur config (sinon boucle infinie)
	}
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		main=(List<Carte>)ois.readObject();
		partisans=(List<Carte>)ois.readObject();
		score=(List<Carte>)ois.readObject();
		nom=(String)ois.readObject();
		joueur=(int)ois.readObject();
		//TODO:lire IA
	}
}
