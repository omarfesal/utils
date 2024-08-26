package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class ImagesToPdfWithHeaderAndFooter {

    public static void main(String[] args) {
        // Array of image paths
        String[] imagePaths = {
                "test.png",
                "test.png",
                "test.png",
        };


        // Output PDF file
        String pdfPath = "output3.pdf";
        // Path to the header image
        String headerImagePath = "header.png";

        try {
            // Create a new Document with zero margins
            Document document = new Document(PageSize.A4, 0, 0, 100, 50); // Setting top and bottom margins for header and footer
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

            // Add event handler for header and footer
            HeaderFooterPageEvent event = new HeaderFooterPageEvent(headerImagePath);
            writer.setPageEvent(event);

            // Open the document
            document.open();

            float availableHeight = PageSize.A4.getHeight() - 150; // Subtracting top and bottom margins for header and footer
            float currentYPosition = availableHeight;

            for (String imagePath : imagePaths) {
                // Create an image instance
                Image image = Image.getInstance(imagePath);

                // Scale image to fit page width, maintaining aspect ratio
                image.scaleToFit(PageSize.A4.getWidth(), availableHeight);

                // Check if the image fits in the remaining space
                if (currentYPosition - image.getScaledHeight() < 50) { // Consider bottom margin for footer
                    // Create a new page
                    document.newPage();
                    currentYPosition = availableHeight;
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

        public HeaderFooterPageEvent(String headerImagePath) {
            try {
                this.headerImage = Image.getInstance(headerImagePath);
//                this.headerImage.scaleToFit(PageSize.A4.getWidth(), 50); // Scale header image to fit the page width
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
                headerImage.scaleAbsolute(PageSize.A4.getWidth(), 100); // Set the header image to the full width of the page
                headerImage.setAbsolutePosition(0, document.top()); // Position header image at the top
                try {
                    cb.addImage(headerImage);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }

            // Footer
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase(String.format("Page %d", writer.getPageNumber()), footerFont),
                    document.right() / 2, document.bottom() - 20, 0);
        }
    }
}
