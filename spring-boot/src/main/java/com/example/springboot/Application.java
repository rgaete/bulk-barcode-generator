package com.example.springboot;

import com.pnuema.java.barcode.Barcode;
import com.pnuema.java.barcode.EncodingType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
@SpringBootApplication
public class Application {

	public static final String TITLE = "BIGMARKET";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping ("/")
	public String index() throws IOException {
		generateImages("test.xlsx");
		return "SUCCESS !!! ";
	}

	public void generateImages(String fileLocation) throws IOException {
		FileInputStream file = new FileInputStream(new File(fileLocation));
		Workbook workbook = new XSSFWorkbook(file);

		Sheet sheet = workbook.getSheetAt(0);

		Map<Integer, List<String>> data = new HashMap<>();
		int i = 0;
		for (Row row : sheet) {
			data.put(i, new ArrayList<String>());
			for (Cell cell : row) {
				data.get(i).add(cell.toString().trim().replace('.','0'));
			}
			i++;
		}

		for (Map.Entry entrySet: data.entrySet()) {
			if((int)entrySet.getKey() > 0){
				ArrayList<String> lista = ((ArrayList<String>) entrySet.getValue());
				Barcode barcode = new Barcode();
				Image img = barcode.encode(EncodingType.CODE128, lista.get(0));
				img = img.getScaledInstance(img.getWidth(null),80,BufferedImage.SCALE_REPLICATE);
				savePic(img,"PNG","Producto_" + lista.get(0) + ".png", lista.get(0), lista.get(1), lista.get(7));
			}
		}
	}


	public void savePic(Image image, String extension, String fileDestination, String code, String description, String price){

		int heightImage = 280;
		int widthImage = 930;
		int heightImageSmall = 200;
		int widthImageSmall = 720;

		BufferedImage bufferedImage = new BufferedImage(widthImage,heightImage, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = bufferedImage.getGraphics();

		try {
			graphics.setColor(Color.WHITE);
			Font fontTitle = new Font(Font.SERIF, Font.PLAIN, 25);
			Font fontCode = new Font(Font.SERIF, Font.PLAIN,25);
			Font fontDesc = new Font(Font.SERIF, Font.PLAIN, 20);
			Font fontPrice = new Font(Font.SERIF, Font.BOLD,25);

			graphics.fillRect(0,0,widthImage,heightImage);
			graphics.setColor(Color.BLACK);

			//graphics.setFont(fontTitle);
			FontMetrics fontMetrics = graphics.getFontMetrics();
			//graphics.drawString(TITLE,(widthImage - fontMetrics.stringWidth(TITLE))/2, fontMetrics.getHeight() + 5);

			graphics.drawImage(image, 5,  10, null);
			graphics.drawImage(image, 315,  10, null);
			graphics.drawImage(image, 625,  10, null);

			graphics.setFont(fontCode);
			fontMetrics = graphics.getFontMetrics();
			graphics.drawString(code,(310 - fontMetrics.stringWidth(code))/2,40 + image.getHeight(null));
			graphics.drawString(code,310 + (310 - fontMetrics.stringWidth(code))/2,40 + image.getHeight(null));
			graphics.drawString(code,625 + (305 - fontMetrics.stringWidth(code))/2,40 + image.getHeight(null));

			graphics.setFont(fontDesc);
			fontMetrics = graphics.getFontMetrics();
			description = (description.length() < 12 ) ? description :  description.substring(0,12);
			graphics.drawString(description,(310 - fontMetrics.stringWidth(description))/2,65 + image.getHeight(null));
			graphics.drawString(description,310 + (310 - fontMetrics.stringWidth(description))/2,65 + image.getHeight(null));
			graphics.drawString(description,625 + (310 - fontMetrics.stringWidth(description))/2,65 + image.getHeight(null));


			graphics.setFont(fontPrice);
			fontMetrics = graphics.getFontMetrics();
			price = "$ " + price;
			graphics.drawString(price,(310 - fontMetrics.stringWidth(price))/2,100 + image.getHeight(null));
			graphics.drawString(price,310 + (310 - fontMetrics.stringWidth(price))/2,100 + image.getHeight(null));
			graphics.drawString(price,625 + (310 - fontMetrics.stringWidth(price))/2,100 + image.getHeight(null));

			BufferedImage bufferedImageSmall = new BufferedImage(widthImageSmall,heightImageSmall,BufferedImage.TYPE_INT_BGR);
			bufferedImageSmall.getGraphics().drawImage(bufferedImage.getScaledInstance(widthImageSmall,heightImageSmall,BufferedImage.SCALE_REPLICATE),0,0,null);

			ImageIO.write(bufferedImage, extension, new File(fileDestination));
			//ImageIO.write(bufferedImageSmall, extension, new File(fileDestination));
			System.out.println("BIGMARKET WIDTH: " + (widthImage - fontMetrics.stringWidth(TITLE))/2 + " W: " + widthImage + "Font H: " + fontMetrics.getHeight() + "BC W: " + image.getWidth(null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
