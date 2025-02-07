package com.gamingCoffee.services;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.ISessionDao;
import com.gamingCoffee.database.controller.ISpotDao;
import com.gamingCoffee.database.entities.Session;
import com.gamingCoffee.database.entities.Spot;
import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.SpotType;
import com.gamingCoffee.utiles.AdminUsernameHolder;
import com.gamingCoffee.utiles.PopupUtil;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class SessionService {

  // make instances of Dao class to read and write in DB
  private static final ISessionDao sessionDao = new SessionDao(
      DatabaseConnection.INSTANCE.getConnection());
  private static final ISpotDao spotDao = new SpotDao(DatabaseConnection.INSTANCE.getConnection());

  public static double calculateSessionPrice(Session session, Spot spot) {
    double basePrice;

    // Base price based on console type
    if (ConsoleType.PS5 == spot.getConsoleType()) {
      basePrice = 30.0;
    } else if (ConsoleType.PS4 == spot.getConsoleType()) {
      basePrice = 20.0;
    } else {
      throw new IllegalArgumentException("Invalid console type: " + spot.getConsoleType());
    }
    // Additional charge for 4 controllers
    if (session.getNoControllers() == 4) {
      basePrice += 10.0;
    }
    // Additional charge for private spot
    if (SpotType.PRIVATE == spot.getSpotType()) {
      basePrice += 10.0;
    }
    // Total price = base price * duration
    return basePrice * session.getDuration();
  }

  public static boolean createSession(int spotNumber, int controllersNumber, String endTime) {
    try {
      if (endTime.isBlank()) {
        return createOpenedSession(spotNumber, controllersNumber);
      } else {
        return createTimedSession(spotNumber, Double.parseDouble(endTime), controllersNumber);
      }
    } catch (Exception e) {
      PopupUtil.showPopup("Error", "The Session could not be created: " + e.getMessage(),
          AlertType.ERROR);
    }
    return false;
  }

  public static double endSession(int sessionNumber, int spotNumber) {
    try {// send end session order to DB
      if (!endSessionHelper(sessionNumber, spotNumber)) {
        // calculate the price of the session and store it in sessionPrice for reuse it
        double sessionPrice = calculateSessionPrice(
            sessionDao.getNoControllersAndDuration(sessionNumber),
            spotDao.getSpotPrivacyAndConsoleType(spotNumber));
        // write session price to db
        sessionDao.writeSessionPrice(sessionPrice, sessionNumber);

        return sessionPrice;
      }
      PopupUtil.showPopup("Error", "Can not End Session: " + sessionNumber + " Number.",
          AlertType.INFORMATION);
      return 0.0;
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return 0.0;
    }
  }

  /**
   * Fetch data from the database and convert it to a displayable format.
   */
  public static ObservableList<Session> getSessionRunningList() throws SQLException {
    return FXCollections.observableArrayList(sessionDao.getCurrentSessions());
  }

  public static ObservableList<Session> getSessionsListByDate(LocalDate date) throws SQLException {
    return FXCollections.observableArrayList(
        sessionDao.getSessionsPerDate(date.format(DateTimeFormatter.ofPattern("yyMMdd"))));
  }

  public static double[] getSessionsCountAndPrice(LocalDate date) throws SQLException {
    return sessionDao.getSessionCountAndSumPrices(
        date.format(DateTimeFormatter.ofPattern("yyMMdd")));
  }

  private static boolean endSessionHelper(int sessionId, int spotId) {
    try {
      return sessionDao.endSession(new Session.Builder().sessionId(sessionId).spotId(spotId)
          .creator(AdminUsernameHolder.getAdminName()).build());
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return false;
    }
  }

  private static boolean createOpenedSession(int spotNumber, int controllersNumber)
      throws SQLException {
    return sessionDao.createOpenSession(new Session.Builder().sessionId(
            GenerateId.generateId(GenerateId.getLastIdFromDb(sessionDao))).spotId(spotNumber)
        .creator(AdminUsernameHolder.getAdminName()).noControllers(controllersNumber).build());
  }

  private static boolean createTimedSession(int spotNumber, double time, int controllersNumber)
      throws SQLException {
    return sessionDao.createTimedSession(new Session.Builder().sessionId(
                GenerateId.generateId(GenerateId.getLastIdFromDb(sessionDao))).spotId(spotNumber)
            .creator(AdminUsernameHolder.getAdminName()).noControllers(controllersNumber).build(),
        time);
  }
}