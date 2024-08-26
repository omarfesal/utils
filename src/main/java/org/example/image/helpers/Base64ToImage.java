package org.example.image.helpers;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;

public class Base64ToImage {

    public static void main(String[] args) {
        try {
            File file = new File("data-base64");
            FileInputStream fis = new FileInputStream(file);
            byte[] base64Bytes = new byte[(int) file.length()];
            fis.read(base64Bytes);
            fis.close();

            // Convert base64 string to byte array
            byte[] imageBytes = Base64.getDecoder().decode(new String(base64Bytes));

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            ImageIO.write(image, "png", new File("image-b64.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

