package Controleur;


import IA.IA;
import IA.IAAleatoire;
import IA.IAHeuristique;
import IA.IAMinMax;
import IA.IAMonteCarlo;
import IA.Test;
import Modele.Carte;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

public class ControleurMediateur implements CollecteurEvenements{
    
	Jeu jeu;
	InterfaceUtilisateur inter;
	int decompte;
	public IA[] joueurs;
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
		joueurs = new IA[2];
		etats = new int[2];
		carteJouer = false;
	}
	
	@Override
	public void clicSouris(Carte carte) {
		if (joueurs[jeu.joueurCourant()] == null && !carteJouer) {
			etats[jeu.joueurCourant()] = OCCUPEE;
			boolean x = jeu.carteJouable(carte);
			if (x) {
				jeu.jouerCarte(carte);
				carteJouer = true;
				System.out.printf("[Humain] a joué son coup: %s\n", carte);
				etats[jeu.joueurCourant()] = LIBRE;
				decompte = TEMPS;
			}
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
		estMenu = false;
		initialiserIA(0,ia1);
		initialiserIA(1,ia2);
		
		if(joueur.equals("Aleatoire"))
			jeu.joueurAleatoire();
		else if(joueur.equals("Joueur1"))
			jeu.joueurCommence(0);
		else
			jeu.joueurCommence(1);
		
		jeu.setNom(0, nom1);
		jeu.setNom(1, nom2);
		
		jeu.initialiserPhase1();
		inter.afficherPlateau();
	}
	
	public void initialiserIA(int j,String ia) {
		if(ia == null)
			joueurs[j] = null;
		else if(ia.equals("Aleatoire"))
			joueurs[j] = new IAAleatoire(jeu, j);
		else if(ia.equals("MinMax"))
			joueurs[j] = new IAMinMax(jeu,j,6);
		else if(ia.equals("Monte Carlo"))
			joueurs[j] = new IAMonteCarlo(jeu,j, 20);
		else
			joueurs[j] = new IAHeuristique(jeu,j,6);
	}
	
	public void tourIA() {
		if(!jeu.finDePartie() && joueurs[jeu.joueurCourant()] != null) {
			Carte c = joueurs[jeu.joueurCourant()].determineCoup( );
			if(jeu.carteJouable(c)) {
				etats[jeu.joueurCourant()] = OCCUPEE;
				jeu.jouerCarte(c);
				carteJouer = true;
				etats[jeu.joueurCourant()] = LIBRE;
				decompte = TEMPS;
			}
			else { 
				System.err.printf("E: L'ia joue une carte qui n'est pas dans sa main [%d] %s\n", jeu.joueurCourant(), c);
			}
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
				Test test = new Test( jeu );
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
			// par defaut appeler la tourIA si le joueur est une "IA" "non occupée" == LIBRE
			// elle joue son coup, sinon l'appel est ignoré
			if (!jeu.estSurMenu() && etats[jeu.joueurCourant()] == LIBRE && !estMenu)
				tourIA();
			if (jeu.finDePartie() && jeu.gagnant() != -1) {
				jeu.afficherResultat();
			}
		}
		inter.metAJour();
	}
}
