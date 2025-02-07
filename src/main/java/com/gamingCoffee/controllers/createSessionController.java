package com.gamingCoffee.controllers;

import com.gamingCoffee.services.SessionService;
import com.gamingCoffee.utiles.PopupUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class createSessionController {

  // field
  @FXML
  private TextField spotNumberFiled;
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
      if (!SessionService.createSession(Integer.parseInt(spotNumberFiled.getText()),
          Integer.parseInt(noControllersFiled.getText()), endTimeFiled.getText())) {
        PopupUtil.showPopup("Success", "The Session has created.", AlertType.INFORMATION);
        cancelAction();
      } else {
        PopupUtil.showPopup("Failed", "Something went Wrong. ", AlertType.ERROR);
      }
    } catch (NumberFormatException e) {
      PopupUtil.showErrorPopup(e);
    }
  }
}