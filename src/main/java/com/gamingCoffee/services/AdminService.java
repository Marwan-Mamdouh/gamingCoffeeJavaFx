package com.gamingCoffee.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.IAdminDao;
import com.gamingCoffee.database.entities.Admin;
import com.gamingCoffee.models.Position;
import com.gamingCoffee.utiles.AdminUsernameHolder;
import com.gamingCoffee.utiles.PopupUtil;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class AdminService {

  private static final IAdminDao adminDao = new AdminDao(
      DatabaseConnection.INSTANCE.getConnection());

  /**
   * @param dbPassword    is a ResultSet contain a password form db
   * @param typedPassword is a String that align with some username in db
   * @return boolean, true if the password form db = password gavin, false otherwise //   * @throws
   * SQLException if something wrong happened while connecting to db
   */
  public static boolean verifyPassword(String typedPassword, String dbPassword) {
    if (dbPassword.isBlank()) {
      return false;
    }
    Result result = BCrypt.verifyer().verify(typedPassword.toCharArray(), dbPassword);
    return result.verified;
  }

  /**
   * @param password String
   * @return encrypted password to store in the db
   */
  public static String hashPassword(String password) {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray());
  }

  public static void addAdmin(String username, String password, Position title, int age,
      String phoneNumber, int salary) {
    try {
      Admin admin = new Admin.Builder().username(username).title(title).salary(salary)
          .password(hashPassword(password)).age(age).phoneNumber(phoneNumber).build();

      adminDao.addUser(admin);
      if (!phoneNumber.isBlank()) {
        adminDao.addPhoneNumber(admin);
      }
      PopupUtil.showPopup("Success", "Admin " + username + " just added successfully!",
          AlertType.INFORMATION);

    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

/*
  public static void removeAdmin(String username, String password) {
    try {

      String dbPassword = adminDao.getPasswordByUsername(username);

      if (verifyPassword(password, dbPassword)) {
        adminDao.removeUser(username);
        adminDao.removePhoneNumber(username);

      } else {
        PopupUtil.showPopup("Error", "Wrong Password", AlertType.ERROR);
      }
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }
*/

  public static String checkToRemoveAdmin(String username) {
    try {
      Admin admin = adminDao.getAdminInfo(username);
      if (admin == null) {
        return "Wrong username";
      } else {
        return "Are you sure you want to remove Admin with this Information? Username: " + username
            + ", Age: " + admin.getAge() + ", Hire Date: " + admin.getHiringDate() + ", Salary: "
            + admin.getSalary() + ", Phone Number: " + admin.getPhoneNumber();
      }
    } catch (SQLException e) {
      PopupUtil.showErrorPopup(e);
      return "";
    }
  }

  public static boolean deleteAdmin(String username, String password) {
    try {
      if (verifyPassword(password,
          adminDao.getPasswordByUsername(AdminUsernameHolder.getAdminName()))) {
        adminDao.removeUser(username);
        PopupUtil.showPopup("Success", "Admin: " + username + " just deleted successfully",
            AlertType.INFORMATION);
        return true;
      } else {
        PopupUtil.showPopup("Delete filed", "Can not find username: " + username,
            AlertType.INFORMATION);
        return false;
      }

    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return false;
    }
  }

  public static ObservableList<Admin> convertAdminsList() {
    try {
      return FXCollections.observableArrayList(adminDao.getAdmins());
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return null;
    }
  }

  public static String checkToChangePassword(String username) {
    try {
      Admin admin = adminDao.getAdminInfo(username);
      if (admin == null) {
        return "Wrong username";
      } else {
        return "Are you sure you want to change Password for this Admin? Username: " + username
            + ", Age: " + admin.getAge() + ", Hire Date: " + admin.getHiringDate() + ", Salary: "
            + admin.getSalary() + ", Phone Number: " + admin.getPhoneNumber();
      }
    } catch (SQLException e) {
      PopupUtil.showErrorPopup(e);
      return "";
    }
  }

  public static boolean changePassword(String username, String oldPassword, String newPassword) {
    try {
      if (verifyPassword(oldPassword, adminDao.getPasswordByUsername(username))) {
        return adminDao.changePassword(username, hashPassword(newPassword));
      } else {
        return false;
      }
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return false;
    }
  }
}
