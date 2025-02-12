package com.gamingCoffee.services;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.IAdminDao;
import com.gamingCoffee.database.controller.IControllerDao;
import com.gamingCoffee.database.entities.Controller;
import com.gamingCoffee.models.ControllerType;
import com.gamingCoffee.utiles.AdminUsernameHolder;
import com.gamingCoffee.utiles.ListUtils;
import java.sql.SQLException;
import javafx.collections.ObservableList;

public class ControllerService {

  private static final IControllerDao controllerDao = new ControllerDao(
      DatabaseConnection.INSTANCE.getConnection());

  /**
   * @param controllerId   (int)
   * @param controllerType (ControllerType)
   * @return boolean
   * @produce false if the controller were added true otherwise
   */
  public static boolean addController(int controllerId, ControllerType controllerType) {
    try {
      IdsUtil.validateIdPositive(controllerId);
      return controllerDao.addController(
          new Controller.Builder().controllerId(controllerId).controllerType(controllerType)
              .build());
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't add new Controller. " + e.getMessage(), e);
    }
  }

  /**
   * @param controllerId (int)
   * @param password     (String)
   * @return boolean
   * @produce false if the controller were deleted true otherwise
   */
  public static boolean removeController(int controllerId, String password) {
    try {
      IdsUtil.validateIdPositive(controllerId);
      IAdminDao adminDao = new AdminDao(DatabaseConnection.INSTANCE.getConnection());
      if (AdminService.verifyPassword(password,
          adminDao.getPasswordByUsername(AdminUsernameHolder.getAdminName()))) {
        return controllerDao.removeController(controllerId);
      } else {
        return false;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't remove Controller. " + e.getMessage(), e);
    }
  }

  /**
   * @param controllerId (int)
   * @return String
   * @produce a description for the controller that ID sent, null if there is an error
   */
  public static String checkController(int controllerId) {
    try {
      IdsUtil.validateIdPositive(controllerId);
      Controller controller = controllerDao.checkController(controllerId);
      if (controller == null) {
        return "Wrong Controller ID";
      }
      return "Are you sure you want to remove Controller with this Information? Controller ID: "
          + controllerId + ", Controller Type: " + controller.getControllerType() + ".";
    } catch (SQLException e) {
      throw new RuntimeException(
          "Failed, no match for Controller ID:" + controllerId + ". " + e.getMessage(), e);
    }
  }

  /**
   * @return ObservableList<Controller>
   * @produce a type of list can be rendering via the app
   */
  public static ObservableList<Controller> convertControllerList() {
    try {
      return ListUtils.toObservableList(controllerDao.getAllController());
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't convert controllers list. " + e.getMessage(), e);
    }
  }
}