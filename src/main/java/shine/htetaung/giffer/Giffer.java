package shine.htetaung.giffer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class Giffer {
	
	public static void main(String[] args)
	{
		ImageOutputStream ios = null;
		File output = new File("sample-images/out.gif");
		String[] img_strings = {"cool.png", "cry.png", "love.png", "oh.png"};
		BufferedImage img = null;
		ImageWriter gifWriter = ImageIO.getImageWritersByFormatName("gif").next();
		
		try {
			ios = ImageIO.createImageOutputStream(output);
		} catch (IOException e) {
			System.out.println("Cannot create output stream");
		}
		
		gifWriter.setOutput(ios);
		
		try {
			gifWriter.prepareWriteSequence(null);
			for (String s: img_strings) {
				img = ImageIO.read(new File("sample-images/" + s));
				IIOImage temp = new IIOImage(img, null, null);
				gifWriter.writeToSequence(temp, null);
			}
			gifWriter.endWriteSequence();
		} catch (Exception e) {
			System.out.println("oh no, something went wrong!");
		}
	}

}
