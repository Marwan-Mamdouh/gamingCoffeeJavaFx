package com.gamingCoffee.services;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.ISpotDao;
import com.gamingCoffee.database.entities.Spot;
import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.SpotType;
import com.gamingCoffee.utiles.PopupUtil;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class SpotService {

  private static final ISpotDao spotDao = new SpotDao(DatabaseConnection.INSTANCE.getConnection());

  public static ObservableList<Spot> getFreeSpots() {
    try {
      return FXCollections.observableArrayList(spotDao.getFreeSpots());
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return null;
    }
  }

  public static ObservableList<Spot> getAllSpots() {
    try {
      return FXCollections.observableArrayList(spotDao.getAllSpots());
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return null;
    }
  }

  public static void addSpot(int consoleId, ConsoleType consoleType, SpotType spotType,
      int displayId, String displayType, int displaySize) {
    try {
      spotDao.addSpot(
          new Spot.Builder().consoleId(consoleId).consoleType(consoleType).spotType(spotType)
              .displayId(displayId).displayType(displayType).displaySize(displaySize).build());

      PopupUtil.showPopup("Success",
          "Spot with console ID:" + consoleId + " and display ID:" + displayId
              + " just added successfully!", AlertType.INFORMATION);

    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  public static void removeSpotById(int spotId) {
    try {
      spotDao.removeById(spotId);
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

//  public static void removeSpotByType(SpotType spotType, ConsoleType consoleType,
//      String displayType, int displaySize) throws SQLException {
//    spotDao.removeByType(
//        new Spot.Builder().spotType(spotType).consoleType(consoleType).displayType(displayType)
//            .displaySize(displaySize).build());
//  }

  public static String checkSpotById(int spotId) {
    try {
      Spot spot = spotDao.checkSpot(spotId);
      if (spot == null) {
        return "Wrong Spot Number";
      }
      return "Are you sure you want to remove Admin with this Information? console ID: "
          + spot.getConsoleId() + ", console Type: " + spot.getConsoleType() + ", Screen ID: "
          + spot.getDisplayId() + ", Screen Type: " + spot.getDisplayType() + ", Screen Size: "
          + spot.getDisplaySize();
    } catch (SQLException e) {
      PopupUtil.showErrorPopup(e);
      return "";
    }
  }
}
