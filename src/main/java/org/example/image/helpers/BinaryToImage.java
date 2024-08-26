package org.example.image.helpers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BinaryToImage {

    public static void main(String[] args) {
        try {
            File file = new File("data-bin.png");
            BufferedImage image = ImageIO.read(file);
            ImageIO.write(image, "png", new File("image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

