package lk.ijse.nrlbag.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import lk.ijse.nrlbag.App;

public class LoadingPageController {


    @FXML
    private Label lblPercent;

    @FXML
    private ProgressBar progressBar;


    public void initialize() {
        startLoading();
    }

    private void startLoading() {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 1; i <= 100; i++) {
                    Thread.sleep(30);
                    updateProgress(i, 100);
                    updateMessage(i + "%");
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        lblPercent.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> openDashBoard());

        new Thread(task).start();

    }

    private void openDashBoard() {
        try {
            App.setRoot("dashBoard");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
