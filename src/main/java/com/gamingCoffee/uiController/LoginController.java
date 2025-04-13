package com.gamingCoffee.uiController;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.admin.repository.AdminRepository;
import com.gamingCoffee.admin.model.Admin;
import com.gamingCoffee.admin.dao.AdminDao;
import com.gamingCoffee.admin.service.AdminService;
import com.gamingCoffee.utiles.AdminUsernameHolder;
import com.gamingCoffee.utiles.ChangeViewUtil;
import com.gamingCoffee.utiles.PageDataUtil;
import com.gamingCoffee.utiles.PopupUtil;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {

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
    PageDataUtil.showPasswordCheckBox(showPasswordCheckbox, showPasswordFiled, passwordFiled);
  }

  @FXML
  void setLoginButtonAction(ActionEvent event) {
    cleanLabels();
    String username = userNameFiled.getText();
    String password = PageDataUtil.getPasswordFromFields(showPasswordCheckbox, passwordFiled,
        showPasswordFiled);
    AdminRepository adminDao = new AdminDao(DatabaseConnection.INSTANCE.getConnection());
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

  /**
   * @param location  The location used to resolve relative paths for the root object, or
   *                  {@code null} if the location is not known.
   * @param resources The resources used to localize the root object, or {@code null} if the root
   *                  object was not localized.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    userNameFiled.setOnAction(this::setLoginButtonAction);
    passwordFiled.setOnAction(this::setLoginButtonAction);
    showPasswordFiled.setOnAction(this::setLoginButtonAction);
  }
}