package lk.ijse.nrlbag.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import lk.ijse.nrlbag.App;

public class LoadingPageController {


    // label to display loading percentage
    @FXML
    private Label lblPercent;

    // progress bar to visually show loading progress
    @FXML
    private ProgressBar progressBar;

    //this method is automatically called after FXML is loaded
    public void initialize() {
        startLoading();
    }

    // start the loading
    private void startLoading() {

        // Task is used to run background operations in JavaFX
        Task<Void> task = new Task<>() {

            // this method run in a background thread
            @Override
            protected Void call() throws Exception {

                // in here load the 1% to 100%
                for (int i = 1; i <= 100; i++) {
                    // at every one increment after that sleep for the simulate time
                    Thread.sleep(30);
                    // update the progress bar value
                    updateProgress(i, 100);
                    // update label text
                    updateMessage(i + "%");
                }
                return null;
            }
        };

        // bind progress bar to task progress
        progressBar.progressProperty().bind(task.progressProperty());
        // bind label text to task msg
        lblPercent.textProperty().bind(task.messageProperty());

        // when task finish, open the dashboard
        task.setOnSucceeded(e -> openDashBoard());

        // start the task in a separate thread
        new Thread(task).start();

    }

    // load the dashboard
    private void openDashBoard() {
        try {
            App.setRoot("layout");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
