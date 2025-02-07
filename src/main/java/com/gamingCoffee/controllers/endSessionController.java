package com.gamingCoffee.controllers;

import com.gamingCoffee.services.SessionService;
import com.gamingCoffee.utiles.PopupUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class endSessionController {

  // Buttons
  @FXML
  private Button cancelButton;

  // fields
  @FXML
  private TextField sessionIdFiled;
  @FXML
  private TextField spotNumberFiled;

  @FXML
  void cancelAction() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  void endSessionAction() {
    try {
      int sessionNumber = Integer.parseInt(sessionIdFiled.getText());
      double sessionPrice = SessionService.endSession(sessionNumber,
          Integer.parseInt(spotNumberFiled.getText()));
      if (sessionPrice != 0.0) {
        PopupUtil.showPopup("Success",
            "Session: " + sessionNumber + " has ended successfully, its total price is: "
                + sessionPrice, AlertType.INFORMATION);
        cancelAction();
        return;
      }
      // tell the Admin the price
      PopupUtil.showPopup("Failed", "Something went Wrong.", AlertType.ERROR);
    } catch (NumberFormatException e) {
      PopupUtil.showErrorPopup(e);
    }
  }
}
