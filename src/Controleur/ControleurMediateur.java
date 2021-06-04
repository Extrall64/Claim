package Controleur;


import IA.IA;
import IA.IAAleatoire;
import IA.IAHeuristique;
import IA.IAMinMax;
import IA.IAMonteCarlo;
import IA.IAVisionComplete;
import IA.Test;
import Modele.Carte;
import Modele.Jeu;
import Modele.Joueur;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

public class ControleurMediateur implements CollecteurEvenements{
    
	Jeu jeu;
	InterfaceUtilisateur inter;
	int decompte;
	public int [] etats;
	public static final int TEMPS = 60;
    public static final int LIBRE = 0;
    public static final int OCCUPEE = 1;
    boolean carteJouer,estMenu;

    boolean doitAnnuler,doitRefaire,doitMenu;
    
	public ControleurMediateur(Jeu j) {
		estMenu = false;
		doitAnnuler = false;
		doitRefaire = false;
		doitMenu = false;
		jeu = j;
		etats = new int[2];
		carteJouer = false;
	}
	
	@Override
	public void clicSouris(Carte carte) {
		int j = jeu.joueurCourant();
		Joueur joueur = jeu.getJoueur(j);
		if (jeu.carteJouable(carte) && joueur.estHumain() && !carteJouer) {
			etats[j] = OCCUPEE;
			joueur.jouerHumain(carte);
			carteJouer = true;
			etats[j] = LIBRE;
			decompte = TEMPS;
		}
	}
	
	public void nouvelle_partie() {
		jeu.nouvellePartie();
		inter.nouvellePartie(Jeu.HUMAIN_VS_HUMAIN);
		jeu.mode(Jeu.HUMAIN_VS_HUMAIN);
	}
	
	public void nouvelle_partie_vs_ia() {
		jeu.nouvellePartie();
		inter.nouvellePartie(Jeu.HUMAIN_VS_IA);
		jeu.mode(Jeu.HUMAIN_VS_IA);
	}
	
	public void nouvelle_partie_ia_vs_ia() {
		jeu.nouvellePartie();
		inter.nouvellePartie(Jeu.IA_VS_IA);
		jeu.mode(Jeu.IA_VS_IA);
	}
	
	public void lancer_partie(String ia1,String ia2,String joueur,String nom1,String nom2) {
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
		estMenu = false;
		jeu.setMenu(false);
		jeu.initialiserPhase1();
		inter.afficherPlateau();
	}
	
	public void initialiserIA(int j,String ia) {
		IA a = null;
		if (ia == null)
			a = null;
		else if(ia.equals("Facile"))
			a = new IAAleatoire(jeu, j);
		else if(ia.equals("Moyen"))
			a = new IAMinMax(jeu,j,6);
		else if(ia.equals("Difficile +"))
			a = new IAVisionComplete(jeu,j, 20);
		else
			a = new IAMonteCarlo(jeu,j, 20);
		
		jeu.getJoueur(j).setAssistant( a );
	}
	
	public void tourIA() {
		int j = jeu.joueurCourant();
		Joueur joueur = jeu.getJoueur(j);
		if(!jeu.finDePartie() && !joueur.estHumain()) {
			etats[j] = OCCUPEE;
			carteJouer = true;
			joueur.jouerAssistant();
			etats[j] = LIBRE;
			decompte = TEMPS;
		}
	}
	
	public void menu() {
		inter.afficherMenu();
		jeu.setSurMenu();
		estMenu = true;
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
				if(jeu.TourHumain()) {
					menu();
				}else{
					doitMenu = true;
				}
				break;
			case "refaire":
				if(jeu.TourHumain()) {
					refaire();
				}else{
					doitRefaire = true;
				}
				break;
			case "annuler":
				if(jeu.TourHumain()) {
					annuler();
				}else{
					doitAnnuler = true;
				}
				break;
			case "charger":
				break;
			case "regle":
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
		//tourIA();
	}
	
	public void annuler() {
		jeu.annule();
		//tourIA();
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
		if (decompte < 0) {
			if(carteJouer){
				jeu.suivant();
				carteJouer = false;
			}else{
				// par defaut appeler la tourIA si le joueur est une "IA" "non occupée" == LIBRE
				// elle joue son coup, sinon l'appel est ignoré
				if (!jeu.estSurMenu() && etats[jeu.joueurCourant()] == LIBRE && !estMenu)
					tourIA();
			}
			if(doitAnnuler){
				annuler();
				doitAnnuler = false;
			}
			if (doitRefaire){
				refaire();
				doitRefaire = false;
			}
			if (doitMenu){
				menu();
				doitMenu = false;
			}
			decompte = TEMPS;
			if (jeu.finDePartie() && jeu.gagnant() != -1) {
				jeu.afficherResultat();
			}
		}
		inter.metAJour();
	}
}
