package com.gamingCoffee.controllers;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.IAdminDao;
import com.gamingCoffee.database.entities.Admin;
import com.gamingCoffee.services.AdminDao;
import com.gamingCoffee.services.AdminService;
import com.gamingCoffee.utiles.AdminUsernameHolder;
import com.gamingCoffee.utiles.ChangeViewUtil;
import com.gamingCoffee.utiles.PageDataUtil;
import com.gamingCoffee.utiles.PopupUtil;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

  // buttons
  @FXML
  private Button loginButton;
  @FXML
  private Button cancelButton;

  // labels
  @FXML
  private Label usernameMassageLabel;
  @FXML
  private Label passwordMassageLabel;

  // fields
  @FXML
  private TextField userNameFiled;
  @FXML
  private TextField showPasswordFiled;
  @FXML
  private PasswordField passwordFiled;

  // check box
  @FXML
  private CheckBox showPasswordCheckbox;

  @FXML
  void selectCheckbox() {
    PageDataUtil.showPasswordCheckBox(showPasswordCheckbox, showPasswordFiled,
        passwordFiled);
//    if (showPasswordCheckbox.isSelected()) {
//      showPasswordFiled.setText(passwordFiled.getText());
//      showPasswordFiled.setVisible(true);
//      passwordFiled.setVisible(false);
//    } else {
//      passwordFiled.setText(showPasswordFiled.getText());
//      passwordFiled.setVisible(true);
//      showPasswordFiled.setVisible(false);
//    }
  }

  @FXML
  public void setLoginButtonAction() {
    cleanLabels();
    String username = userNameFiled.getText();
    String password = PageDataUtil.getPasswordFromFields(showPasswordCheckbox, passwordFiled,
        showPasswordFiled);
    IAdminDao adminDao = new AdminDao(DatabaseConnection.INSTANCE.getConnection());
    if (!username.isBlank() && !password.isBlank()) {
      try {
        tryLoginWith(adminDao.getUserData(username), password);

      } catch (SQLException e) {
        PopupUtil.showErrorPopup(e);

      } catch (Exception e) {
        usernameMassageLabel.setText("Wrong Username.");
      }

    } else {
      usernameMassageLabel.setText("Enter username and password to check...");
    }
  }

  @FXML
  public void setCancelButtonAction() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  private void tryLoginWith(Admin admin, String password) {
    if (AdminService.verifyPassword(password, admin.getPassword())) {
      AdminUsernameHolder.setAdminName(admin.getUsername());
      ChangeViewUtil.dashBoardPage(admin.getTitle(), (Stage) loginButton.getScene().getWindow());
    } else {
      passwordMassageLabel.setText("Wrong Password.");
    }
  }

  private void cleanLabels() {
    usernameMassageLabel.setText("");
    passwordMassageLabel.setText("");
  }
}