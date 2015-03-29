package media;

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

	public static String readBarcode (String filePath){
		InputStream barcodeIS;
		try {
			barcodeIS = new FileInputStream(filePath);
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
