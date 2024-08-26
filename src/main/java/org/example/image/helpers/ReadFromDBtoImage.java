package org.example.image.helpers;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class ReadFromDBtoImage {

    // Database connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/db_name";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public static void main(String[] args) {
        // Replace with your image ID or other identifier
        int imageId = 2;

        // SQL query to select binary data
        String sql = "select data from test.wp_custom_blobs wcb where id = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the image ID parameter
            pstmt.setInt(1, imageId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    byte[] blobData = rs.getBytes("data");

                    byte[] imageData = null;

                    // Attempt to decode Base64
                    try {
                        String base64Data = new String(blobData);
                        imageData = Base64.getDecoder().decode(base64Data);
                    } catch (IllegalArgumentException e) {

                        System.out.println("Base64 decoding failed, treat data as binary , " +  e);
                        // Base64 decoding failed, treat data as binary
                        imageData = blobData;
                    }

                    // Write binary data to file
                    if (imageData != null) {
                        try (FileOutputStream fos = new FileOutputStream(new File("output_image.png"))) {
                            fos.write(imageData);
                        } catch (IOException e) {
                            System.err.println("Error writing image data to file: " + e.getMessage());
                        }
                    } else {
                        System.out.println("No image data found for the specified ID.");
                    }
                } else {
                    System.out.println("No image found for the specified ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
