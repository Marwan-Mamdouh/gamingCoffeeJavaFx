package com.gamingCoffee.utiles;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class PopupUtil {

  /**
   * Displays a pop-up window with a title and message.
   *
   * @param title   The title of the pop-up window.
   * @param message The message to display in the pop-up.
   * @param type    The type of alert (e.g., AlertType. INFORMATION, AlertType. ERROR).
   */
  public static void showPopup(String title, String message, AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null); // No header text
    alert.setContentText(message);

    // Set the owner stage (optional, but useful for modal dialogs)
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.setAlwaysOnTop(true); // Ensure the pop-up is on top

    alert.showAndWait(); // Show the pop-up and wait for the user to close it
  }

  public static void showErrorPopup(Exception e) {
    showPopup("Error", "Something went wrong... " + e.getMessage(), AlertType.ERROR);
  }
}