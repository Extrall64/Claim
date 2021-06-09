package Controleur;


import IA.IA;
import IA.IAAleatoire;
import IA.IAMinMax;
import IA.IAMonteCarlo;
import IA.IAVisionComplete;
import IA.Test;
import Modele.Carte;
import Modele.Jeu;
import Modele.Joueur;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

// etat du joueur courant, occupe == entrain de jouer ca carte/calculer si c'est l'IA
enum Etat {LIBRE, OCCUPE};
// action a faire au prochain appel de timer, NOP == rien a faire
enum Action {ANNULER, REFAIRE, MENU,CHARGER,SAUVER,NVLPARTIE, NOP};

public class ControleurMediateur implements CollecteurEvenements{
    
	Jeu jeu;
	InterfaceUtilisateur inter;
	int decompte;
    boolean carteJouer;
    Action action;
    Etat etat;
	public static final int TEMPS = 60;

	String infoia1,infoia2, infojoueur, infonom1, infonom2;
	int mode;


    public ControleurMediateur(Jeu j) {
		action = Action.NOP;
		etat = Etat.LIBRE;
		jeu = j;
		carteJouer = false;
	}
	
	@Override
	public void clicSouris(Carte carte) {
		int j = jeu.joueurCourant();
		Joueur joueur = jeu.getJoueur(j);
		if (jeu.carteJouable(carte) && joueur.estHumain() && !carteJouer) {
			etat = Etat.OCCUPE;
			joueur.jouerHumain(carte);
			carteJouer = true;
			etat = Etat.LIBRE;
			decompte = TEMPS;
		}
	}
	
	public void nouvelle_partie() {
    	mode = Jeu.HUMAIN_VS_HUMAIN;
		jeu.nouvellePartie();
		inter.nouvellePartie(Jeu.HUMAIN_VS_HUMAIN);
		jeu.mode(Jeu.HUMAIN_VS_HUMAIN);
	}
	
	public void nouvelle_partie_vs_ia() {
		mode = Jeu.HUMAIN_VS_IA;
		jeu.nouvellePartie();
		inter.nouvellePartie(Jeu.HUMAIN_VS_IA);
		jeu.mode(Jeu.HUMAIN_VS_IA);
	}
	
	public void nouvelle_partie_ia_vs_ia() {
		mode = Jeu.IA_VS_IA;
		jeu.nouvellePartie();
		inter.nouvellePartie(Jeu.IA_VS_IA);
		jeu.mode(Jeu.IA_VS_IA);
	}
	
	public void lancer_partie(String ia1,String ia2,String joueur,String nom1,String nom2) {
    	infoia1 = ia1;
    	infoia2 = ia2;
    	infojoueur = joueur;
    	infonom1 = nom1;
    	infonom2 = nom2;

		jeu.setJoueur(0, nom1, null);
		jeu.setJoueur(1, nom2, null);
		
		initialiserIA(0,ia1);
		initialiserIA(1,ia2);
		
		if(joueur.equals("Aleatoire"))
			jeu.joueurAleatoire();
		else if(joueur.equals("Joueur1"))
			jeu.joueurCommence(0);
		else
			jeu.joueurCommence(1);
		jeu.setMenu(false);
		jeu.initialiserPhase1();
		inter.afficherPlateau(jeu.getMode());
		decompte = TEMPS;
	}
	
	public void initialiserIA(int j,String ia) {
		IA a = null;
		if (ia == null)
			a = null;
		else if(ia.equals("Facile"))
			a = new IAAleatoire(jeu, j);
		else if(ia.equals("Moyen"))
			a = new IAMinMax(jeu,j,6);
		else if(ia.equals("Difficile"))
			a = new IAMonteCarlo(jeu,j, 20);
		else
			a = new IAVisionComplete(jeu,j, 20);
		// le joueur j sera une IA
		jeu.getJoueur(j).setAssistant( a );
	}
	
	public void tourIA() {
		int j = jeu.joueurCourant();
		Joueur joueur = jeu.getJoueur(j);
		if(!jeu.finDePartie() && !joueur.estHumain()) {
			etat = Etat.OCCUPE;
			carteJouer = true;
			joueur.jouerAssistant();
			etat = Etat.LIBRE;
			decompte = TEMPS;
		}
	}
	
	public void menu() {
		inter.afficherMenu();
		jeu.setMenu(true);
	}

	public void charger (){
    	jeu.charger();
    	if(!jeu.getChargePb()) {
			inter.afficherPlateau(jeu.getMode());
			System.out.println("charger");
		}
	}
	public void sauver (){
    	jeu.sauvegarder();
		System.out.println("sauver");
	}

	public void commande(String c) {
		switch (c) {
			case "humain_vs_humain":
				nouvelle_partie();
				break;
			case "humain_vs_ia":
				nouvelle_partie_vs_ia();
				break;
			case "ia_vs_ia":
				nouvelle_partie_ia_vs_ia();
				break;
			case "test_ia":
				Test test = new Test();
				test.demarrer( 50 );
				break;
			case "menu":
				action = Action.MENU;
				break;
			case "retour-menu":
				menu();
				break;
			case "refaire":
				action = Action.REFAIRE;
				break;
			case "annuler":
				action = Action.ANNULER;
				break;
			case "charger":
				action=Action.CHARGER;
				break;
			case "sauver":
				action=Action.SAUVER;
				break;
			case "nouvellePartie":
				action = Action.NVLPARTIE;
				break;
			case "regle":
				inter.afficherRegle();
				break;
			case "aide":
				break;
			case "parametre":
				break;
			default:
				break;
		}
	}
	
	public void refaire() {
		jeu.refaire();
	}
	
	public void annuler() {
		jeu.annule();
		inter.annuler();
	}

	@Override
	public void fixerInterfaceUtilisateur(InterfaceUtilisateur i) {
		inter = i;
	}


	//decooupé : le clic souris select la carte et le timer la joue pour laisser le temps a l'anim
	@Override
	public void tictac() {
		decompte = decompte - 1;
		// temporisation
		if (!jeu.getMenu() && decompte < 0) {
			if(carteJouer){
				jeu.suivant();
				carteJouer = false;
			}else{
				// par defaut appeler la tourIA si le joueur est une "IA" "non occupée" == LIBRE
				// elle joue son coup, sinon l'appel est ignoré
				if (etat == Etat.LIBRE)
					tourIA();
			}
			decompte = TEMPS;
			if (jeu.finDePartie()) {
				jeu.afficherResultat();
				inter.afficherFinPartie();
				jeu.setMenu(true);
			}
		}
		// executer les actions dés que possible
		if (etat == Etat.LIBRE) {
			switch(action) {
				case ANNULER: annuler(); break;
				case REFAIRE: refaire(); break;
				case MENU: menu(); break;
				case CHARGER:  charger(); break;
				case SAUVER: sauver(); break;
				case NVLPARTIE: nvlpartie(); break;
				default: break;
			}
			// reinitialiser action
			action = Action.NOP;
		}
		// actualiser l'affichage
		inter.metAJour();
	}

	private void nvlpartie() {
		jeu.nouvellePartie();
		jeu.mode(mode);
		lancer_partie(infoia1,infoia2,infojoueur,infonom1,infonom2);
	}
}
