package com.gamingCoffee.utiles;

import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PageDataUtil {

  public static String getPasswordFromFields(CheckBox showPasswordCheckBox,
      PasswordField passwordField, TextField textField) {
    if (showPasswordCheckBox.isSelected()) {
      return textField.getText();
    } else {
      return passwordField.getText();
    }
  }

  public static void showPasswordCheckBox(CheckBox checkBox, TextField textField,
      PasswordField passwordField) {
    if (checkBox.isSelected()) {
      textField.setText(passwordField.getText());
      textField.setVisible(true);
      passwordField.setVisible(false);
    } else {
      passwordField.setText(textField.getText());
      passwordField.setVisible(true);
      textField.setVisible(false);
    }
  }
}
