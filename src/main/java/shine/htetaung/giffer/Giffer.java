package shine.htetaung.giffer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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
		
		ImageTypeSpecifier imageType = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB);
		
		IIOMetadata img_metadata = gifWriter.getDefaultImageMetadata(imageType, null);
		String native_format = img_metadata.getNativeMetadataFormatName();
		
		IIOMetadataNode node_tree = (IIOMetadataNode)img_metadata.getAsTree(native_format);
		IIOMetadataNode graphics_node = null;
		
		System.out.println("node_tree length: " + node_tree.getLength());
		System.out.println("\nnode_tree items:");
		for (int i = 0; i < node_tree.getLength(); i++) System.out.println(node_tree.item(i).getNodeName());
		
		for (int i = 0; i < node_tree.getLength(); i++)
		{
			if(node_tree.item(i).getNodeName().compareToIgnoreCase("GraphicControlExtension") == 0)
				graphics_node = (IIOMetadataNode) node_tree.item(i);
		}
		
		int delayTime = 10;
		
		System.out.println("\ngraphics_node: " + graphics_node.getNodeName());
		graphics_node.setAttribute("delayTime", String.valueOf(delayTime));
		graphics_node.setAttribute("disposalMethod", "none");
		graphics_node.setAttribute("userInputFlag", "FALSE");
		
		IIOMetadataNode app_exts_node = new IIOMetadataNode("ApplicationExtensions");
		IIOMetadataNode app_node = new IIOMetadataNode("ApplicationExtension");
		
		app_node.setAttribute("applicationID", "NETSCAPE");
		app_node.setAttribute("authenticationCode", "2.0");
		app_node.setUserObject(new byte[]{ 0x1, (byte) (0 & 0xFF), (byte) ((0 >> 8) & 0xFF)});
		
		app_exts_node.appendChild(app_node);
		node_tree.appendChild(app_exts_node);
		
		try {
			img_metadata.setFromTree(native_format, node_tree);
		} catch (IIOInvalidTreeException e) {
			System.out.println("Cannot set the tree");
		}
		

		
		try {
			gifWriter.prepareWriteSequence(null);
			for (String s: img_strings) {
				img = ImageIO.read(new File("sample-images/" + s));
				IIOImage temp = new IIOImage(img, null, img_metadata);
				gifWriter.writeToSequence(temp, null);
			}
			gifWriter.endWriteSequence();
		} catch (Exception e) {
			System.out.println("oh no, something went wrong!");
		}
	}

}
