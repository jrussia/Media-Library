package media;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamPicker;
import com.github.sarxos.webcam.WebcamResolution;

/*
 * CSC 478
 * Team2
 * TakePicture.java
 * Purpose: To help process accessing the user's webcam and
 * taking a picture
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 1.1.1 5/2/15
 */

public class TakePicture {
	public static Webcam webcam = null;
	public static BufferedImage img;
	public static JFrame frame;
	public static JButton captureBtn;
	public static void showWebcam(){
		img = null;
		webcam = Webcam.getDefault();
		WebcamPicker picker = new WebcamPicker();
	
		webcam = picker.getSelectedWebcam();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setImageSizeDisplayed(true);
		//panel.setMirrored(false);
		panel.setLayout(new BorderLayout());
		
		captureBtn = new JButton("Capture");
		panel.add(captureBtn, BorderLayout.SOUTH);
		
		frame = new JFrame("Take Photo");
		frame.add(panel);
		frame.setResizable(true);
		frame.setTitle("Webcam Take Picture");
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        webcam.close();
		    }
		});
		
		if(webcam == null){
			JFrame parent = new JFrame();
        	JOptionPane.showMessageDialog(parent, "There was a problem connecting to your webcam.", "Alert", JOptionPane.ERROR_MESSAGE);
		}
		return;
	}
}
