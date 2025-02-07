package com.gamingCoffee.utiles;

import com.gamingCoffee.controllers.HomeController;
import com.gamingCoffee.controllers.MainController;
import com.gamingCoffee.models.Position;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChangeViewUtil {

  public static void changeView(Stage stage, String title, String resource, boolean newView) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource(resource));
      Scene scene = new Scene(fxmlLoader.load());

      stage.setTitle(title);
      stage.setScene(scene);

      if (newView) {
        stage.show();
      }
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  public static void startLoginPage(Stage currentStage) {
    changeView(currentStage, "Gaming Coffee Management App", "/fxml/login.fxml", false);
  }

  public static void dashBoardPage(Position title, Stage currentStage) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/fxml/dashBoard.fxml"));
      Scene homeScene = new Scene(fxmlLoader.load());

      if (title == Position.OWNER) {
        HomeController homeController = fxmlLoader.getController();
        homeController.showOwnerButton();
      }
      currentStage.setScene(homeScene);

    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  public static void closeStage(Stage stage) {
    stage.close();
  }
}
