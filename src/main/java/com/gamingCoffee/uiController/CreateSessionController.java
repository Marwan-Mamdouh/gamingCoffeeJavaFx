package com.gamingCoffee.uiController;

import com.gamingCoffee.session.service.SessionService;
import com.gamingCoffee.spot.service.SpotService;
import com.gamingCoffee.utiles.PopupUtil;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateSessionController implements Initializable {

  // field
  @FXML
  private ChoiceBox<Integer> spotNumberChoiceBox;
  @FXML
  private TextField noControllersFiled;
  @FXML
  private TextField endTimeFiled;

  // buttons
  @FXML
  private Button cancelButton;

  // action buttons
  @FXML
  void cancelAction() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  void createSessionAction() {
    try {
      if (SessionService.createSession(spotNumberChoiceBox.getValue(),
          Integer.parseInt(noControllersFiled.getText()), endTimeFiled.getText())) {
        PopupUtil.showPopup("Success", "Session Created.", AlertType.INFORMATION);
        cancelAction();
      } else {
        PopupUtil.showPopup("Failed", "Something went Wrong. ", AlertType.ERROR);
      }
    } catch (NumberFormatException e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  /**
   * @param location  The location used to resolve relative paths for the root object, or
   *                  {@code null} if the location is not known.
   * @param resources The resources used to localize the root object, or {@code null} if the root
   *                  object was not localized.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      spotNumberChoiceBox.getItems().addAll(SpotService.getFreeSpotsNumbers());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}