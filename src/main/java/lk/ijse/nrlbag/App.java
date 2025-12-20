package lk.ijse.nrlbag;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene loginScene; //scene for login
    private static Scene scene; //scene for main layout
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException { //start method to set the initial scene
        primaryStage = stage;
        setLoginScene("login");
    }

    //method to change the root of the current scene
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void setLoginScene(String fxml) throws IOException {
        loginScene = new Scene(loadFXML(fxml), 1366, 768);
        primaryStage.setScene(loginScene);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    //method to setup the primary stage with the main layout scene
    public static void setupPrimaryStageScene(String fxmlFileName) throws Exception {

        scene = new Scene(loadFXML(fxmlFileName), 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/lk/ijse/nrlbag/view/" +fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}