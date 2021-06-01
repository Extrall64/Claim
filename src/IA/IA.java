package IA;

import java.util.Random;
import Modele.Carte;

public interface IA {
	// infini
	final static float oo = Float.MAX_VALUE;
	final static Random rand = new Random();
	public Carte determineCoup();
}