package com.gamingCoffee.controllers;

import com.gamingCoffee.services.SessionService;
import com.gamingCoffee.services.SpotService;
import com.gamingCoffee.utiles.PopupUtil;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class EndSessionController implements Initializable {

  // Buttons
  @FXML
  private Button cancelButton;

  // fields
  @FXML
  private ChoiceBox<Integer> spotNumberChoiceBox;


  @FXML
  void cancelAction() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  void endSessionAction() {
    int spotNumber = spotNumberChoiceBox.getValue();
    try {
      double sessionPrice = SessionService.endSession(spotNumber);
      if (sessionPrice != 0.0) {
        PopupUtil.showPopup("Success", "Session on Spot Number: " + spotNumber
                + " has ended successfully, its total price is: " + sessionPrice,
            AlertType.INFORMATION);
        cancelAction();
      }
    } catch (NumberFormatException e) {
      PopupUtil.showPopup("Failed",
          "Can not End Session: " + spotNumber + " Number, " + e.getMessage(), AlertType.ERROR);
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
      spotNumberChoiceBox.getItems().addAll(SpotService.getBusySpotsNumbers());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
