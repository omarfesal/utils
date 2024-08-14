package org.example;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class ImagesToPdf {

    public static void main(String[] args) {
        // Array of image paths
        String[] imagePaths = {
                "C:\\Users\\Ozi\\Desktop\\mother-data.png",
                "C:\\Users\\Ozi\\Desktop\\father-data.png",
                "C:\\Users\\Ozi\\Desktop\\test.png",
                "C:\\Users\\Ozi\\Desktop\\test.png",
                "C:\\Users\\Ozi\\Desktop\\test.png"
        };

        // Output PDF file
        String pdfPath = "output.pdf";

            try {
            // Create a new Document
            Document document = new Document(PageSize.A4,  0, 0, 0, 0);
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

            // Open the document
            document.open();

            float availableHeight = PageSize.A4.getHeight() - document.bottomMargin() - document.topMargin();
            float currentYPosition = availableHeight;

            for (String imagePath : imagePaths) {
                // Create an image instance
                Image image = Image.getInstance(imagePath);

                // Scale image to fit page width, maintaining aspect ratio
                image.scaleToFit(PageSize.A4.getWidth() - document.leftMargin() - document.rightMargin(), PageSize.A4.getHeight());

                // Check if the image fits in the remaining space
                if (currentYPosition - image.getScaledHeight() < document.bottomMargin()) {
                    // Create a new page
                    document.newPage();
                    currentYPosition = availableHeight;
                }

                // Set the position of the image
                currentYPosition -= image.getScaledHeight();
                image.setAbsolutePosition(document.leftMargin(), currentYPosition);

                // Add the image to the document
                document.add(image);
            }

            // Close the document
            document.close();
            System.out.println("PDF created successfully!");

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}




