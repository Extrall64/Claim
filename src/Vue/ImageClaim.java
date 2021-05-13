package Vue;

import java.io.InputStream;

public abstract class ImageClaim {
	static ImageClaim getImageSokoban(InputStream in) {
		return new ImageClaimSwing(in);
	}
	abstract <E> E image();
}
