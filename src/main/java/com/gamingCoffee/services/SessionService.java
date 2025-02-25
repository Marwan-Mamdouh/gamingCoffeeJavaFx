package com.gamingCoffee.services;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.ISessionDao;
import com.gamingCoffee.database.controller.ISpotDao;
import com.gamingCoffee.database.entities.Session;
import com.gamingCoffee.database.entities.Spot;
import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.SpotType;
import com.gamingCoffee.utiles.AdminUsernameHolder;
import com.gamingCoffee.utiles.ListUtils;
import com.gamingCoffee.utiles.PopupUtil;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.collections.ObservableList;

public class SessionService {

  // make instances of Dao class to read and write in DB
  private static final ISessionDao sessionDao = new SessionDao(
      DatabaseConnection.INSTANCE.getConnection());
  private static final ISpotDao spotDao = new SpotDao(DatabaseConnection.INSTANCE.getConnection());

  /**
   * @param session (Session)
   * @param spot    (Spot)
   * @return (double)
   * @produce the result of the session (the Cost) based on the gavin Session and spot information
   */
  public static double calculateSessionPrice(Session session, Spot spot) {
    double basePrice = checkDeviceType(spot.getConsoleType());

    // Additional charge for 4 controllers
    basePrice = checkNoController(basePrice, session.getNoControllers());

    // Additional charge for private spot
    basePrice = checkSpotType(basePrice, spot.getSpotType());
    // Total price = base price * duration
    return Math.round(basePrice * session.getDuration());
  }

  /**
   * @param spotNumber        (int) can not be blank
   * @param controllersNumber (int) can not be blank
   * @param endTime           (String) can be blank
   * @return (boolean) false if every thing works will
   * @produce false if everything works will
   */
  public static boolean createSession(int spotNumber, int controllersNumber, String endTime) {
    try {
      if (endTime.isBlank()) {
        return createOpenedSession(spotNumber, controllersNumber);
      } else {
        return createTimedSession(spotNumber, Double.parseDouble(endTime), controllersNumber);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to create Session. " + e.getMessage(), e);
    }
  }

  /**
   * @param spotNumber (int)
   * @return (double)
   * @produce the price of the session based on gavin sessionNumber and spotNumber or 0.0 if
   * something went wrong
   */
  public static double endSession(int spotNumber) {
    try {// send end session order to DB
      if (endSessionHelper(spotNumber)) {
        // calculate the price of the session and store it in sessionPrice for reuse it
        Session session = sessionDao.getSessionIdAndControllersAndDuration(spotNumber);
        double sessionPrice = calculateSessionPrice(session,
            spotDao.getSpotPrivacyAndConsoleType(spotNumber));
        // write session price to db
        if (sessionDao.writeSessionPrice(sessionPrice, session.getSessionId())) {
          return sessionPrice;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed, Couldn't end Session on spot ID: " + spotNumber + ". " + e.getMessage(), e);
    }
    throw new RuntimeException("Failed, Couldn't end Session on spot ID: " + spotNumber + ", 1.");
  }

  /**
   * @return ObservableList<Session>
   * @throws SQLException if something went wrong in the db
   * @produce convert it to a displayable format.
   */
  public static ObservableList<Session> getSessionRunningList() throws SQLException {
    return ListUtils.toObservableList(getRunningSessions());
  }

  /**
   * @param date (LocalDate)
   * @return ObservableList<Session>
   * @throws SQLException if something went wrong in the db
   * @produce convert it to a displayable format.
   */
  public static ObservableList<Session> getSessionsListByDate(LocalDate date) throws SQLException {
    return ListUtils.toObservableList(getSessionPerDate(date));
  }

  /**
   * @param date (LocalDate)
   * @return array of doubles
   * @throws SQLException if something went wrong in the db
   */
  public static double[] getSessionsCountAndPrice(LocalDate date) throws SQLException {
    return sessionDao.getSessionCountAndSumPrices(
        date.format(DateTimeFormatter.ofPattern("yyMMdd")));
  }

  private static List<Session> getSessionPerDate(LocalDate date) throws SQLException {
    return sessionDao.getSessionsPerDate(date.format(DateTimeFormatter.ofPattern("yyMMdd")));
  }

  /**
   * @return List<Session>
   * @throws SQLException if something went wrong in the db
   * @produce a List of current running Sessions
   */
  private static List<Session> getRunningSessions() throws SQLException {
    return sessionDao.getCurrentSessions();
  }

  private static boolean endSessionHelper(int spotId) {
    try {
      return sessionDao.endSession(
          new Session.Builder().spotId(spotId).creator(AdminUsernameHolder.getAdminName()).build());
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return false;
    }
  }

  private static boolean createOpenedSession(int spotNumber, int controllersNumber)
      throws SQLException {
    return sessionDao.createOpenSession(
        new Session.Builder().sessionId(IdsUtil.generateId(IdsUtil.getLastIdFromDb(sessionDao)))
            .spotId(spotNumber).creator(AdminUsernameHolder.getAdminName())
            .noControllers(controllersNumber).build());
  }

  private static boolean createTimedSession(int spotNumber, double time, int controllersNumber)
      throws SQLException {
    return sessionDao.createTimedSession(
        new Session.Builder().sessionId(IdsUtil.generateId(IdsUtil.getLastIdFromDb(sessionDao)))
            .spotId(spotNumber).creator(AdminUsernameHolder.getAdminName())
            .noControllers(controllersNumber).build(), time);
  }

  private static double checkDeviceType(ConsoleType consoleType) {
    // Base price based on console type
    switch (consoleType) {
      case PS4 -> {
        return 20.0;
      }
      case PS5 -> {
        return 30.0;
      }
      default -> throw new IllegalArgumentException("Invalid console type: " + consoleType);
    }
  }

  private static double checkNoController(double price, int noControllers) {
    switch (noControllers) {
      case 1, 2 -> {
        return price;
      }
      case 3, 4 -> {
        return price + 10;
      }
      case 5, 6 -> {
        return price + 15;
      }
      default -> throw new IllegalArgumentException("Invalid Controller numbers.");
    }
  }

  private static double checkSpotType(double price, SpotType spotType) {
    switch (spotType) {
      case PRIVATE -> {
        return price + 10;
      }
      case PUBLIC -> {
        return price;
      }
      default -> throw new IllegalArgumentException("Invalid Spot Type: " + spotType + ".");
    }
  }
}