package Vue;

import Global.Configuration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.InputStream;

public class ImageClaimSwing extends ImageClaim {
	Image img;

	ImageClaimSwing(String in) {
		try {
			// Chargement d'une image utilisable dans Swing
			img = ImageIO.read((getClass().getClassLoader().getResource(in)));
		} catch (Exception e) {
			Configuration.instance().logger().severe("Impossible de charger l'image : " + e);
			System.exit(1);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	<E> E image() {
		return (E) img;
	}
}