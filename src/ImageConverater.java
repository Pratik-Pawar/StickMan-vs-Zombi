// Java program to demonstrate creation of mirror image

import GameEntity.Animations.DirectionalAnimation;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mainComponent.TimeMonitar;

public class ImageConverater {

    public static void main(String args[]) throws IOException {
        int frameCount = 15;
        BufferedImage rightFrames[] = new BufferedImage[frameCount];
        //Loding Images to rightFrames
        BufferedImage sheet = null;
        try {
            sheet = ImageIO.read(new File("E:/Pratik/StickMan Game/StickMan/src/Animaction/xST Fighting.png"));
        } catch (Exception ex) {
            Logger.getLogger(DirectionalAnimation.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error: Image does not loded");
            System.err.println("Error: Image does not loded");
            System.err.println("Error: Image does not loded");
            System.exit(1);
        }
        int height = sheet.getHeight();
        int width = sheet.getWidth() / frameCount;

        for (int i = 0; i < frameCount; i++) {
            rightFrames[i] = sheet.getSubimage(i * width, 0, width, height);
        }

        BufferedImage newFrames[] = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            newFrames[i] = new BufferedImage(width + 40, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newFrames[i].createGraphics();
            g.drawImage(rightFrames[i], 20, 0, null);
        }
        BufferedImage img = new BufferedImage(newFrames[0].getWidth() * frameCount, newFrames[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        for (int i = 0; i < newFrames.length; i++) {
            BufferedImage newFrame = newFrames[i];
            g.drawImage(newFrame, i * newFrames[0].getWidth(), 0, null);
        }
        ImageIO.write(img, "png", new File("E:/Pratik/StickMan Game/StickMan/src/Animaction/ST Fighting.png"));
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
