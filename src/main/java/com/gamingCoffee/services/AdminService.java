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
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

  /**
   * @param username    String
   * @param password    String
   * @param title       Position
   * @param age         int
   * @param phoneNumber String
   * @param salary      int
   * @produce add admin (after creating one) to db
   */
  public static boolean addAdmin(String username, String password, Position title, int age,
      String phoneNumber, int salary) {
    try { // create an Admin instance
      Admin admin = new Admin.Builder().username(username).title(title).salary(salary)
          .password(hashPassword(password)).age(age).phoneNumber(phoneNumber).build();

      boolean result = adminDao.addUser(admin);
      if (!phoneNumber.isBlank() && result) {
        return adminDao.addPhoneNumber(admin);
      } else {
        return result;
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed, couldn't add Admin:" + username, e);
    }
  }

  /**
   * @param username String
   * @return String description of user to check if he is the one
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
      throw new RuntimeException("Failed, no match in the db", e);
    }
  }

  /**
   * @param username String
   * @param password String
   * @return boolean false if the query is done without errors
   */
  public static boolean deleteAdmin(String username, String password) {
    try {
      if (verifyPassword(password,
          adminDao.getPasswordByUsername(AdminUsernameHolder.getAdminName()))) {
        return adminDao.removeUser(username);
      } else {
        throw new RuntimeException("Wrong password.");
      }
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return false;
    }
  }

  /**
   * @return ObservableList<Admin>
   * @produce a render able list for app
   */
  public static ObservableList<Admin> convertAdminsList() {
    return FXCollections.observableArrayList(Objects.requireNonNull(getAdminsList()));
  }

  /**
   * @return List<Admin>
   * @produce the list of all admins
   */
  private static List<Admin> getAdminsList() {
    try {
      return adminDao.getAdmins();
    } catch (SQLException e) {
      PopupUtil.showErrorPopup(e);
      return null;
    }
  }

  /**
   * @param username (String)
   * @return String
   * @produce description to validate the user to remove it in other function
   */
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

  /**
   * @param username    (String)
   * @param oldPassword (String)
   * @param newPassword (String)
   * @return boolean false if the password changed and everything is good
   * @produce validate the user and set the new password
   */
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
