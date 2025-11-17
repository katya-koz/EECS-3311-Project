package org.openjfx.EECS_3311_Project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

public class UsersDatabaseUtils {

    private static final String homeDirectory = System.getProperty("user.home");
    private static final Path dataFolderDirectory = Paths.get(homeDirectory, "EECS_3311_Project_CSV_Files");

    public static final String userDB_CSV = dataFolderDirectory.resolve("UserDB.csv").toString();
    public static final String bookingDB = dataFolderDirectory.resolve("BookingDB.csv").toString();

    //scene changer

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String email, String accountType)
    {
        Parent root = null;

        if (email != null && accountType != null)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(UsersDatabaseUtils.class.getResource(fxmlFile));
                root = loader.load();
                HomePageController homePageController = loader.getController();
                homePageController.setUserInfo(email, accountType);
            } catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        else //if they are on the logged out screen
        {
            try
            {
                root = FXMLLoader.load(UsersDatabaseUtils.class.getResource(fxmlFile));

            } catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600,400));
        stage.show();
    }

    private static String getResourcePath(String resourceName) {
        try {
            // This searches the classpath for the file
            URL resourceUrl = UsersDatabaseUtils.class.getResource(resourceName);

            if (resourceUrl == null) {
                throw new RuntimeException("Cannot find resource: " + resourceName);
            }

            // Convert the URL to a clean file path
            return Paths.get(resourceUrl.toURI()).toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading resource: " + resourceName, e);
        }
    }

    public static void initiliazeDBFolder()
    {
        try {
            if (!Files.exists(dataFolderDirectory))
            {
                Files.createDirectories(dataFolderDirectory);

                loadCSVs("UserDB.csv", dataFolderDirectory.resolve("UserDB.csv"));
                loadCSVs("BookingDB.csv", dataFolderDirectory.resolve("BookingDB.csv"));
            }
        } catch (IOException e) {
            throw new RuntimeException("could not create database directory", e);
        }
    }

    private static void loadCSVs(String fileName, Path destination) throws IOException {
        InputStream inputStream = UsersDatabaseUtils.class.getResourceAsStream(fileName);

        if (inputStream == null)
        {
            throw new IOException("Could not find csv file");
        }

        //creating the csv files
        try (OutputStream outputStream = Files.newOutputStream(destination))
        {
            inputStream.transferTo(outputStream);
        }
        finally {
            inputStream.close();
        }

    }


}
