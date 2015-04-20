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
		
		if(webcam == null){
			JFrame parent = new JFrame();
        	JOptionPane.showMessageDialog(parent, "There was a problem connecting to your webcam.", "Alert", JOptionPane.ERROR_MESSAGE);
		}
		return;
	}
}
