package com.eventplanner.util;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Saves uploaded event and advertisement images inside the "uploads"
 * folder of the web application.
 *
 * Only JPG, PNG and GIF files up to 5 MB are accepted.
 */
public class FileUploadUtil {

    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    private static final List<String> ALLOWED_EXTENSIONS =
            Arrays.asList("jpg", "jpeg", "png", "gif");

    private static final List<String> ALLOWED_CONTENT_TYPES =
            Arrays.asList("image/jpeg", "image/png", "image/gif", "image/jpg");

    private FileUploadUtil() {
    }

    /**
     * Returns the physical upload directory. The folder is created when it
     * does not exist yet.
     */
    public static File getUploadDirectory(ServletContext context) throws IOException {
        String realPath = context.getRealPath("/uploads");
        if (realPath == null) {
            realPath = System.getProperty("java.io.tmpdir") + File.separator + "eventplanner-uploads";
            context.log("WARNING: The web application is not exploded. Uploaded images are stored in " + realPath);
        }
        File directory = new File(realPath);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Could not create the uploads directory: " + realPath);
        }
        return directory;
    }

    /**
     * Saves the uploaded file and returns its new file name.
     *
     * @param part           uploaded file part (may be null or empty)
     * @param uploadDirectory directory where images are stored
     * @param existingImage  image name that should be kept when no new file
     *                       was selected
     * @return the file name to store in the database
     */
    public static String saveImage(Part part, File uploadDirectory, String existingImage)
            throws IOException {

        if (part == null || part.getSize() == 0 || WebUtil.isBlank(part.getSubmittedFileName())) {
            return existingImage;
        }

        if (part.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("The image is too large. Please select an image smaller than 5 MB.");
        }

        String originalName = new File(part.getSubmittedFileName()).getName();
        String extension = getExtension(originalName);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Invalid image type. Only JPG, PNG and GIF files are allowed.");
        }

        String contentType = part.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ENGLISH))) {
            throw new IllegalArgumentException("Invalid image type. Only JPG, PNG and GIF files are allowed.");
        }

        String newFileName = System.currentTimeMillis() + "_" + Math.abs(originalName.hashCode()) + "." + extension;
        Path destination = new File(uploadDirectory, newFileName).toPath();

        try (InputStream input = part.getInputStream()) {
            Files.copy(input, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        return newFileName;
    }

    /**
     * Deletes an old image from the uploads folder. Used when an image is
     * replaced or a record is deleted.
     */
    public static void deleteImage(File uploadDirectory, String imageName) {
        if (WebUtil.isBlank(imageName) || uploadDirectory == null) {
            return;
        }
        File file = new File(uploadDirectory, new File(imageName).getName());
        if (file.exists()) {
            // Failure to delete an old image is not a fatal error.
            file.delete();
        }
    }

    private static String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot < 0 || dot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dot + 1).toLowerCase(Locale.ENGLISH);
    }
}
