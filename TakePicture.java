import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;


public class TakePicture {

	
	public TakePicture() throws IOException {
		
		Webcam webcam = Webcam.getDefault();
		
		try {	//set a fairly standard custom resolution
			webcam.setViewSize(new Dimension(640,480));
		} catch (IllegalArgumentException e) {}
		
		webcam.open();
		
		BufferedImage image = webcam.getImage();
		ImageIO.write(image, "JPG", new File("cachePix.jpg"));
		webcam.close();
	}

}
