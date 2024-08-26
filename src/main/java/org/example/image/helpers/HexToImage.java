package org.example.image.helpers;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Hex;

public class HexToImage {

    public static void main(String[] args) {
        try {
            File file = new File("path/to/your/hex/file");
            FileInputStream fis = new FileInputStream(file);
            byte[] hexBytes = new byte[(int) file.length()];
            fis.read(hexBytes);
            fis.close();

            // Convert hex string to byte array
            byte[] imageBytes = Hex.decodeHex(new String(hexBytes));

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            ImageIO.write(image, "png", new File("output/image.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
