package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class ImagesToPdfWithHeaderAndFooter {

    public static final int HEADER_HEIGHT = 100;
    public static final int FOOTER_HEIGHT = 100;

    public static void main(String[] args) {
        // Array of image paths
        String[] imagePaths = {
                "father-data.png",
                "father-data.png",
                "father-data2.png",
                "father-data2.png",
                "father-data.png",
        };


        // Output PDF file
        String pdfPath = "output3.pdf";
        // Path to the header image
        String headerImagePath = "header.png";
        String footerImagePath = "footer.png";

        try {
            // Create a new Document with zero margins
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

            // Add event handler for header and footer
            HeaderFooterPageEvent event = new HeaderFooterPageEvent(headerImagePath, footerImagePath);
            writer.setPageEvent(event);

            // Open the document
            document.open();

            final float INITIAL_START_Y_POINT = PageSize.A4.getHeight() - HEADER_HEIGHT;
            float currentYPosition = INITIAL_START_Y_POINT;

            for (String imagePath : imagePaths) {
                // Create an image instance
                Image image = Image.getInstance(imagePath);

                // Scale image to fit page width, maintaining aspect ratio
                image.scaleToFit(PageSize.A4.getWidth(), INITIAL_START_Y_POINT);

                // Check if the image fits in the remaining space
                boolean isCurrentImageNotFitRemainingSpace = currentYPosition - image.getScaledHeight() - FOOTER_HEIGHT < 0;
                if (isCurrentImageNotFitRemainingSpace) {
                    document.newPage();
                    currentYPosition = INITIAL_START_Y_POINT;
                }

                // Set the position of the image
                currentYPosition -= image.getScaledHeight();
                image.setAbsolutePosition(0, currentYPosition);

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

    static class HeaderFooterPageEvent extends PdfPageEventHelper {
        private Image headerImage;
        private Image footerImage;

        public HeaderFooterPageEvent(String headerImagePath, String footerImagePath) {
            try {
                this.headerImage = Image.getInstance(headerImagePath);
                this.footerImage = Image.getInstance(footerImagePath);
            } catch (BadElementException | IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);

            // Header
            if (headerImage != null) {
                headerImage.scaleAbsolute(PageSize.A4.getWidth(), HEADER_HEIGHT); // Set the header image to the full width of the page
                headerImage.setAbsolutePosition(0, document.top() - HEADER_HEIGHT); // Adjust positioning for the header image
                try {
                    cb.addImage(headerImage);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }

            // Footer Image
            if (footerImage != null) {
                footerImage.scaleAbsolute(PageSize.A4.getWidth(), FOOTER_HEIGHT); // Set the footer image to the full width of the page
                footerImage.setAbsolutePosition(0, document.bottom()); // Adjust positioning for the footer image
                try {
                    cb.addImage(footerImage);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
