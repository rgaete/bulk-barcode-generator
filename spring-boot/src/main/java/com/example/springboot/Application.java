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
		Image img = barcode.encode(EncodingType.CODE128, "12345678");
		return savePic(img,"PNG","testts.png");
	}

	public String savePic(Image image, String type, String dst){

		int heightImage = 300;
		int widthImage = 300;
		int heightImageSmall = 200;
		int widthImageSmall = 200;

		BufferedImage bufferedImage = new BufferedImage(widthImage,heightImage, BufferedImage.TYPE_INT_BGR);
		Graphics graphics = bufferedImage.getGraphics();

		try {
			graphics.setColor(Color.WHITE);
			Font fontTitle = new Font(Font.SERIF, Font.PLAIN, 25);
			Font fontCode = new Font(Font.SERIF, Font.PLAIN,25);
			Font fontDesc = new Font(Font.SERIF, Font.PLAIN, 20);
			Font fontPrice = new Font(Font.SERIF, Font.BOLD,25);

			FontMetrics fontMetrics = graphics.getFontMetrics();
			graphics.fillRect(0,0,widthImage,heightImage);
			graphics.setColor(Color.BLACK);
			graphics.setFont(fontTitle);
			fontMetrics = graphics.getFontMetrics();
			graphics.drawString("BIGMARKET",(widthImage - fontMetrics.stringWidth("BIGMARKET"))/2, fontMetrics.getHeight() + 5);
			graphics.drawImage(image, 0, fontMetrics.getHeight() + 10, null);
			graphics.setFont(fontCode);
			fontMetrics = graphics.getFontMetrics();
			graphics.drawString("12345678",(widthImage - fontMetrics.stringWidth("12345678"))/2,80 + image.getHeight(null));
			graphics.setFont(fontDesc);
			fontMetrics = graphics.getFontMetrics();
			graphics.drawString("Descripcion",(widthImage - fontMetrics.stringWidth("Descripcion"))/2,110 + image.getHeight(null));
			graphics.setFont(fontPrice);
			fontMetrics = graphics.getFontMetrics();
			graphics.drawString("$10.000",(widthImage - fontMetrics.stringWidth("$10.000"))/2,140 + image.getHeight(null));

			BufferedImage bufferedImageSmall = new BufferedImage(widthImageSmall,heightImageSmall,BufferedImage.TYPE_INT_BGR);
			bufferedImageSmall.getGraphics().drawImage(bufferedImage.getScaledInstance(widthImageSmall,heightImageSmall,BufferedImage.SCALE_REPLICATE),0,0,null);

			ImageIO.write(bufferedImageSmall, type, new File(dst));
			return "BIGMARKET WIDTH: " + (widthImage - fontMetrics.stringWidth("BIGMARKET"))/2 + " W: " + widthImage + "Font H: " + fontMetrics.getHeight() + "BC W: " + image.getWidth(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "FAIL";
	}

}
