package media;

/*
 * CSC 478
 * Team2
 * imageProcessing.java
 * Purpose: To process reading the barcode and taking a picture
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 1.1.5 5/1/15
 */

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class imageProcessing {

	public static String readBarcode (InputStream barcodeIS){
		try {
			BufferedImage barcodeBI = ImageIO.read(barcodeIS);
			LuminanceSource source = new BufferedImageLuminanceSource(barcodeBI);
			BinaryBitmap bm = new BinaryBitmap(new HybridBinarizer(source));
			Reader reader = new MultiFormatReader();
			Result result = reader.decode(bm);
			System.out.println(result.toString());
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public String OCR (Image image){
		
		return null;
	}
	
	public int compareImages (Image image, Image image2){
		
		return 0;
	}
	
	public static byte[] getImageBlob(InputStream fileInputStream){
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
