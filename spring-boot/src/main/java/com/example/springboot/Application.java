package com.example.springboot;

import com.pnuema.java.barcode.Barcode;
import com.pnuema.java.barcode.EncodingType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RestController
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping ("/")
	public String index() {
		Barcode barcode = new Barcode();
		Image img = barcode.encode(EncodingType.UPCA, "038000356216");
		return savePic(img,"PNG","testts.png");
	}

	public String savePic(Image image, String type, String dst){

		BufferedImage bi = new BufferedImage(300,300, BufferedImage.TYPE_INT_ARGB);
		int heightImage = bi.getHeight();
		int widthImage = bi.getWidth();
		Graphics g = bi.getGraphics();
		try {
			g.setColor(Color.WHITE);
			g.fillRect(0,0,widthImage,heightImage);
			g.setColor(Color.RED);
			Font font = new Font(Font.SERIF, Font.PLAIN, 24);
			g.setFont(font);
			FontMetrics fontMetrics = g.getFontMetrics();
			g.fillRect(0,0,widthImage,fontMetrics.getHeight()+10);
			g.setColor(Color.WHITE);
			g.drawString("BIGMARKET",(widthImage - fontMetrics.stringWidth("BIGMARKET"))/2, fontMetrics.getHeight());
			g.drawImage(image, 0, 80, null);
			ImageIO.write(bi, type, new File(dst));
			return "H: " + heightImage + " W: " + widthImage + "Font H: " + fontMetrics.getHeight();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "FAIL";
	}

}
