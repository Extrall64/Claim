package Vue;

import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;

public class Regle extends JFrame{
    public Regle(){
        super();
        build();
    }

    private void build(){
        setTitle("Règles de Claim"); //On donne un titre à l'application
        setSize(320,240); //On donne une taille à notre fenêtre
        setLocationRelativeTo(null); //On centre la fenêtre sur l'écran
        setResizable(true); //On permet le redimensionnement
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //On dit à l'application de se fermer lors du clic sur la croix
        setContentPane(buildContentPane());
    }

    public JScrollPane buildContentPane(){
        JTextArea label = new JTextArea();
        label.setFont(new Font("Comic Sans MS", Font.ITALIC | Font.BOLD, 15));
        label.setText("Mise en place\n\n" +
                "Mélangez l’ensemble des cartes puis faites-en une pioche que vous placez au centre de la table. Distribuez 13 cartes à\n" +
                "chaque joueur. Chaque joueur prend ses cartes en main en prenant soin de ne pas les montrer à son adversaire.\n" +
                "\n du jeu\n\n" +
                "Le jeu se déroule en deux phases distinctes. Lors de la première phase, chaque joueur reçoit une main de cartes dont il se\n" +
                "servira pour recruter des partisans. Lors de la seconde phase, chaque joueur devra utiliser ses partisans afin de rallier les\n" +
                "cinq factions du Royaume à sa cause. Au terme de la partie, un joueur ayant une majorité de partisans au sein d’une faction\n" +
                "remporte le vote de celle-ci. Celui qui parvient à réunir les votes d’au moins trois factions remporte la partie !\n" +
                "\nPremière phase : recruter des partisans\n\n" +
                "Cette phase est constituée de treize plis, un pour chaque carte dans la main des joueurs. Le Leader d’un pli est le joueur\n" +
                "ayant remporté le pli précédent (ou, dans le cas du premier pli, le plus jeune des deux joueurs). Chaque pli se joue de\n" +
                "la manière suivante :\n" +
                "Révéler une carte\n" +
                "Placez la première carte de la pioche face visible au milieu de la table, entre les deux joueurs. C’est la carte pour laquelle\n" +
                "les joueurs vont s’affronter.\n" +
                "Jouer des cartes\n" +
                "1. Le Leader joue à présent une carte de sa main.\n" +
                "Remarque : il est possible de jouer une carte appartenant à n’importe quelle faction, pas nécessairement à la même\n" +
                "faction que la carte au centre de la table.\n" +
                "2. Puis l’autre joueur joue une carte de sa main.\n" +
                "Important : le second joueur doit toujours suivre, si possible. Cela signifie que s’il a en main une carte de la même\n" +
                "faction que celle jouée par le Leader, il doit alors la jouer (exception : les Doppelgängers). En d’autres termes, un\n" +
                "joueur ne peut jouer une carte d’une faction différente que s’il ne possède aucune carte de la même faction que celle\n" +
                "jouée par le Leader.\n" +
                "Ramasser les cartes\n" +
                "1. Le joueur ayant joué la carte de plus haute valeur (0 étant la plus faible et 9 la plus forte) de la faction jouée par le\n" +
                "Leader remporte la carte centrale. En cas d’égalité, le Leader remporte le pli.\n" +
                "Remarque : si le second joueur a joué une carte d’une autre faction que celle du Leader, ce dernier remporte alors\n" +
                "automatiquement le pli, à moins qu’un pouvoir de faction (Doppelgängers ou Chevaliers) ne s’applique.\n" +
                "2. Le gagnant place la carte remportée face cachée devant lui, formant ainsi sa pile de Partisans. Le perdant prend la carte\n" +
                "du dessus de la pioche centrale et constitue à son tour sa pile de Partisans. Ce dernier peut regarder la carte piochée\n" +
                "mais ne peut en aucun cas la montrer à son adversaire. Les cartes jouées par les 2 joueurs sont alors défaussées.\n" +
                "Remarque : les cartes de votre pile de Partisans constitueront votre main lors de la seconde phase de la partie.\n" +
                "3. Toute carte Mort Vivant jouée, est placée directement face visible dans la pile de Score du joueur qui remporte le pli.\n" +
                "Important : une carte Mort-vivant provenant du centre de la table est tout de même placée dans la pile de Partisans.\n" +
                "4. Défaussez toutes les autres cartes jouées.\n" +
                "Important : assurez-vous que votre pile de Score soit face visible et votre pile de Partisans face cachée, afin de ne\n" +
                "pas les confondre l’une avec l’autre.\n" +
                "Continuez ainsi jusqu’à ce que la pile centrale soit épuisée et que les joueurs n’aient plus de carte en main.\n" +
                "Passez ensuite à la seconde phase.\n" +
                "\nSeconde phase : rassembler ses soutiens\n\n" +
                "Chaque joueur prend en main les 13 cartes de sa pile de Partisans gagnées lors de la première phase. Les joueurs vont\n" +
                "à présent s’affronter lors de 13 nouveaux plis. Cependant, ils se battront cette fois-ci, non pas pour des cartes du centre\n" +
                "de la table, mais pour les deux cartes jouées lors de chaque pli. Chaque pli est joué de la manière suivante :\n" +
                "Jouer des cartes\n" +
                "1. Le Leader joue une carte de sa main.\n" +
                "2. Puis l’autre joueur en joue une à son tour.\n" +
                "Important: les pouvoirs des différentes factions s’appliquent encore !\n" +
                "Ramasser les cartes\n" +
                "1. Déterminez qui remporte le pli comme lors de la première phase.\n" +
                "2. Le gagnant place les deux cartes jouées face visible dans sa pile de Score, à moins qu’un pouvoir de faction (Nains)\n" +
                "ne s’applique.\n" +
                "Une fois que les treize plis ont été joués et que les joueurs n’ont plus de carte en main, passez au décompte des factions.\n" +
                "Fin de la partie\n" +
                "Les joueurs comptent séparément le nombre de cartes de chaque faction dans leur pile de Score. Le joueur ayant le plus\n" +
                "de cartes dans une faction gagne le vote de cette dernière. En cas d’égalité, le vote va à celui possédant la plus forte\n" +
                "carte de la faction. Le joueur ayant gagné le vote d’au moins trois factions remporte la partie !\n" +
                "\nPouvoirs des factions\n\n" +
                "Certaines factions possèdent un pouvoir modifiant les règles du jeu :\n" +
                "Gobelins\n" +
                "Aucun pouvoir.\n" +
                "Chevaliers\n" +
                "Lorsqu’un Chevalier est joué après un Gobelin, il remporte automatiquement le pli, peu importe la valeur\n" +
                "respective des deux cartes.\n" +
                "Important : le joueur ne doit malgré tout pas oublier de jouer la faction demandée, s’il le peut.\n" +
                "Morts-vivants\n" +
                "Les cartes Morts-vivants jouées ne sont pas défaussées lors de la première phase, contrairement aux autres\n" +
                "cartes. Elles sont placées dans la pile de Score du joueur ayant remporté le pli.\n" +
                "Nains\n" +
                "Lors de la seconde phase, le joueur ayant perdu le pli en cours ramasse toutes les cartes Nains jouées lors\n" +
                "de celui-ci et les place dans sa pile de Score. Le gagnant remporte tout de même la carte d’une autre faction,\n" +
                "s’il y en a une.\n" +
                "Doppelgängers\n" +
                "Cette faction est considérée comme un joker. Il est possible de jouer une carte Doppelgänger à la place de la\n" +
                "faction demandée, même si vous pouvez suivre. Dans ce cas-là, la carte Doppelgänger est considérée comme\n" +
                "étant de la même faction que la première.\n" +
                "Remarque : si le Leader joue une carte Doppelgänger, l’autre joueur doit jouer un Doppelgänger, s’il le peut.\n" +
                "Important : une carte Doppelgänger ne bénéficie pas du pouvoir de la faction qu’il copie. Par exemple, si elle est\n" +
                "jouée lors de la première phase après une carte Mort-Vivant, elle n’est pas prise par le gagnant, pas plus qu’elle\n" +
                "ne serait prise par le perdant si jouée lors de la seconde phase après une carte Nain.");
        JScrollPane panel = new JScrollPane(label);
        return panel;
    }
}