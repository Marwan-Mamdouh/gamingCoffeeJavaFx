package com.gamingCoffee.uiController;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
        MainController.class.getResource("/fxml/login.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
    stage.setTitle("Gaming Coffee Management App");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}