package com.gamingCoffee.services;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.IAdminDao;
import com.gamingCoffee.database.controller.IControllerDao;
import com.gamingCoffee.database.entities.Controller;
import com.gamingCoffee.models.ControllerType;
import com.gamingCoffee.utiles.AdminUsernameHolder;
import com.gamingCoffee.utiles.PopupUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ControllerService {

  private static final IControllerDao controllerDao = new ControllerDao(
      DatabaseConnection.INSTANCE.getConnection());

  public static boolean addController(int controllerId, ControllerType controllerType) {
    try {
      return controllerDao.addController(
          new Controller.Builder().controllerId(controllerId).controllerType(controllerType)
              .build());
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return false;
    }
  }

  public static boolean removeController(int controllerId, String password) {
    try {
      IAdminDao adminDao = new AdminDao(DatabaseConnection.INSTANCE.getConnection());
      if (AdminService.verifyPassword(password,
          adminDao.getPasswordByUsername(AdminUsernameHolder.getAdminName()))) {
        return controllerDao.removeController(controllerId);
      } else {
        return false;
      }
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return false;
    }
  }


  public static String checkController(int controllerId) {
    try {
      Controller controller = controllerDao.checkController(controllerId);
      if (controller == null) {
        return "Wrong Controller ID";
      }
      return "Are you sure you want to remove Controller with this Information? Controller ID: "
          + controllerId + ", Controller Type: " + controller.getControllerType() + ".";
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return null;
    }
  }

  public static ObservableList<Controller> convertControllerList() {
    try {
      return FXCollections.observableArrayList(controllerDao.getAllController());
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return null;
    }
  }
}
