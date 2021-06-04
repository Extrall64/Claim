package Vue;

import java.io.InputStream;

public abstract class ImageClaim {
	static ImageClaim getImageClaim(String in) {
		
		return new ImageClaimSwing(in);
	}
	abstract <E> E image();
}
