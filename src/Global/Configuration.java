package Global;

import Structures.Sequence;
import Structures.SequenceListe;
import Structures.SequenceTableau;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuration {
	private static Configuration instance = null;
	Properties prop;
	public static InputStream charge(String nom) {
		return ClassLoader.getSystemClassLoader().getResourceAsStream(nom);
	}

	private Configuration() {
		prop = new Properties();
		try {
			InputStream propIn = charge("default.cfg");
			prop.load(propIn);
			String home = System.getProperty("user.home");
			FileInputStream f = new FileInputStream(home + File.separator + ".claim");
			prop = new Properties(prop);
			prop.load(f);
		} catch (Exception e) {
			System.err.println("Erreur lors de la lecture de la configuration : " + e);
		}
	}

	public static Configuration instance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	public String lis(String cle) {
		String resultat = prop.getProperty(cle);
		if (resultat == null)
			throw new NoSuchElementException("Propriete " + cle + " non definie");
		return resultat;
	}

	public <E> Sequence<E> nouvelleSequence() {
		String type = lis("Sequence");
		switch (type) {
			case "Tableau":
				return new SequenceTableau<>();
			case "Liste":
				return new SequenceListe<>();
			default:
				throw new RuntimeException("Type de s�quence inconnu : " + type);
		}
	}
	
	public Logger logger() {
		Logger log = Logger.getLogger("Claim.Logger");
		log.setLevel(Level.parse(lis("LogLevel")));
		return log;
	}
 }
