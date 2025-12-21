package lk.ijse.nrlbag.util;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import lk.ijse.nrlbag.App;

import java.net.URL;

public class SetBackground {
    public static void setBackground(Pane rootPane) {
        // 1 Load the image from resources (Portable path)
        // This looks inside 'src/main/resources' for the path
        String imagePath = "/lk/ijse/nrlbag/images/Blue-Abstract-Design-PNG-Cutout-300x225.png";
        URL imageUrl = App.class.getResource(imagePath);

        // Safety check to prevent crashing if path is wrong
        if (imageUrl == null) {
            System.out.println("Error: Image not found at " + imagePath);
            return;
        }

        Image image = new Image(imageUrl.toExternalForm());

        // 2 Define the "Cover" behavior
        // Width=1.0, Height=1.0, AsPercentage=true, Contain=false, Cover=false
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);

        // 3 Create the BackgroundImage
        BackgroundImage bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,  // Do not repeat horizontally
                BackgroundRepeat.NO_REPEAT,  // Do not repeat vertically
                BackgroundPosition.CENTER,   // Center the image
                backgroundSize
        );

        // 4 Apply it to pane
        rootPane.setBackground(new Background(bgImage));
    }
}
