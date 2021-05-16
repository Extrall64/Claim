package Vue;

import java.io.InputStream;

public abstract class ImageClaim {
	static ImageClaim getImageClaim(InputStream in) {
		return new ImageClaimSwing(in);
	}
	abstract <E> E image();
}
