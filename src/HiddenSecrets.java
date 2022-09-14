package src;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.Directory;
import com.drew.metadata.Tag;
import com.drew.imaging.ImageMetadataReader;

import java.io.*;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Scanner;

public class HiddenSecrets {
    public static void getHiddenSecrets(File file) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(
                    new FileInputStream(file)
            );
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.format("[%s] - %s = %s%n",
                            directory.getName(), tag.getTagName(), tag.getDescription());
                }
                if (directory.hasErrors()) {
                    for (String error : directory.getErrors()) {
                        System.err.format("ERROR: %s%n", error);
                    }
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("That file does not exist.");
        } catch (IOException ioe) {
            System.out.println("Problem reading from file stream.");
        } catch (ImageProcessingException ipe) {
            System.out.println("Failed to process the image meta-data");
        }
    }

    public static void getHiddenSecretsFromURL(URL url) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(
                    url.openStream()
            );
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.format("[%s] - %s = %s%n",
                            directory.getName(), tag.getTagName(), tag.getDescription());
                }
                if (directory.hasErrors()) {
                    for (String error : directory.getErrors()) {
                        System.err.format("ERROR: %s%n", error);
                    }
                }
            }
        } catch (ImageProcessingException e) {
            System.out.println("Failed to process the image meta-data.");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Problem reading from URL.");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.print("File path or URL to image: ");
            Scanner scanner = new Scanner(System.in);
            String location = scanner.nextLine().trim();

            // add a lazy exit clause
            if (location.equalsIgnoreCase("end") || location.equalsIgnoreCase("exit")) {
                return;
            }

            if (location.startsWith("https://") || location.startsWith("http://")) {
                getHiddenSecretsFromURL(new URL(location));
            } else {
                getHiddenSecrets(Paths.get(location).toFile());
            }
            System.out.println();
        }
    }
}
