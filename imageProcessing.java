package media;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class imageProcessing {

	public String readBarcode (Image image){
		
		return null;
	}
	
	public String OCR (Image image){
		
		return null;
	}
	
	public int compareImages (Image image, Image image2){
		
		return 0;
	}
	
	public static byte[] getImageBlob(FileInputStream fileInputStream){
		byte[] buf = null;
    	byte[] bytes = null;
    	if(fileInputStream != null){
    		BufferedInputStream bis = new BufferedInputStream(fileInputStream);
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		int current = 0;
    		buf = new byte[1024];
    		try {
				for(int readNum; (readNum = fileInputStream.read(buf)) != -1;){
					bos.write(buf, 0, readNum);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
    		bytes = bos.toByteArray();
    	}
    	else{
    		buf = null;
    	}
    	return bytes;
	}
	
	public Image iconToImage(Icon icon){
		if(icon instanceof ImageIcon){
			return ((ImageIcon)icon).getImage();
		}
		else{
			return null;
		}
	}
}
