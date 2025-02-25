package com.gamingCoffee.services;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.ISpotDao;
import com.gamingCoffee.database.entities.Spot;
import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.SpotType;
import com.gamingCoffee.utiles.ListUtils;
import com.gamingCoffee.utiles.PopupUtil;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class SpotService {

  private static final ISpotDao spotDao = new SpotDao(DatabaseConnection.INSTANCE.getConnection());

  /**
   * @return (ObservableList < Spot >) a list but in displayable format
   */
  public static ObservableList<Spot> getSpots() throws SQLException {
    return ListUtils.toObservableList(spotDao.getFreeSpots());
  }

  public static ObservableList<Spot> getAllSpots() throws SQLException {
    return ListUtils.toObservableList(spotDao.getAllSpots());
  }

  public static ObservableList<Integer> getFreeSpotsNumbers() throws SQLException {
    return ListUtils.toObservableList(spotDao.getFreeSpotsNumbers());
  }

  public static ObservableList<Integer> getBusySpotsNumbers() throws SQLException {
    return ListUtils.toObservableList(spotDao.getBusySpotsNumbers());
  }

  public static void addSpot(int consoleId, ConsoleType consoleType, SpotType spotType,
      int displayId, String displayType, int displaySize) {
    try {
      IdsUtil.validateIdPositive(consoleId);
      IdsUtil.validateIdPositive(displayId);
      if (spotDao.addSpot(
          new Spot.Builder().consoleId(consoleId).consoleType(consoleType).spotType(spotType)
              .displayId(displayId).displayType(displayType).displaySize(displaySize).build())) {
        PopupUtil.showPopup("Success",
            "Spot with console ID:" + consoleId + " and display ID:" + displayId
                + " just added successfully!", AlertType.INFORMATION);
      }
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  /**
   * @param spotId (int) The ID of the spot to delete (must be positive).
   * @return {@code true} if a spot was deleted, {@code false} if no spot matched the ID.
   * @throws IllegalArgumentException if {@code spotId} is invalid.
   * @throws RuntimeException         if {@code spotId} have no match int the ID column in the db.
   */
  public static boolean removeSpotById(int spotId) {
    try {
      IdsUtil.validateIdPositive(spotId);
      return spotDao.removeById(spotId);

    } catch (Exception e) {
      throw new RuntimeException("Failed to delete Spot with ID: " + spotId + ". " + e.getMessage(),
          e);
    }
  }

  /**
   * @param spotId (int) The ID of the spot to delete (must be positive).
   * @return description of the spot.
   */
  public static String checkSpotById(int spotId) {
    try {
      IdsUtil.validateIdPositive(spotId);
      Spot spot = spotDao.checkSpot(spotId);
      if (spot == null) {
        return "Wrong Spot Number";
      }
      return "Are you sure you want to remove Admin with this Information? console ID: "
          + spot.getConsoleId() + ", console Type: " + spot.getConsoleType() + ", Screen ID: "
          + spot.getDisplayId() + ", Screen Type: " + spot.getDisplayType() + ", Screen Size: "
          + spot.getDisplaySize();
    } catch (SQLException e) {
      throw new RuntimeException(
          "Failed to get Spots. no match for spot ID:" + spotId + ". " + e.getMessage(), e);
    }
  }

  public static boolean updatePrices(double price, Enum<?> consoleType) {
    return spotDao.setPrice(consoleType, price);
  }

  public static double getPrices(Enum<?> serviceType) {
    return spotDao.getServicePrice(serviceType);
  }
}
