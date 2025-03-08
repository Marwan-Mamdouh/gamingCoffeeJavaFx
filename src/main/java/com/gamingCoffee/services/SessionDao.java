package com.gamingCoffee.services;

import com.gamingCoffee.database.controller.ISessionDao;
import com.gamingCoffee.database.entities.Session;
import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.SpotType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SessionDao implements ISessionDao {

  private final Connection connection;

  public SessionDao(Connection connection) {
    this.connection = connection;
  }

  /**
   * @return a list of current running sessions
   * @throws RuntimeException if something want wrong in the db
   */
  @Override
  public List<Session> getCurrentSessions() {
    final String sql = "SELECT session_id, spot_id, creator, controllers_number, start_time, "
        + "end_time, ROUND((JULIANDAY(datetime('now', 'localtime')) - JULIANDAY(start_time)) * 24,"
        + " 2) AS duration FROM sessions WHERE session_state = 'RUNNING' LIMIT 20 OFFSET 0";
    final List<Session> sessions = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        sessions.add(readSessionForDashboard(rs));
      }
      return sessions;
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't get Current Sessions. " + e.getMessage(), e);
    }
  }

  /**
   * @param date (String)
   * @return a list of session created in gavin date
   * @throws RuntimeException if something want wrong in the db
   */
  @Override
  public List<Session> getSessionsPerDate(String date) {
    final String sql =
        "SELECT session_id, duration, session_price FROM sessions WHERE session_id LIKE '" + date
            + "%' AND session_state = 'DONE' LIMIT 999 OFFSET 0";
    // inject date parameter like this bc the "?" way did not work.
    final List<Session> sessions = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        sessions.add(readSessionForResults(rs));
      }
      return sessions;
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't get Session per Date. " + e.getMessage(), e);
    }
  }

  /**
   * @param newSession (Session)
   * @return (boolean), false if everything works will
   * @throws RuntimeException if something want wrong in the db
   */
  @Override
  public boolean createOpenSession(Session newSession) {
    final String sql = "INSERT INTO sessions (session_id, spot_id, creator, controllers_number, "
        + "start_time) VALUES (?, ?, ?, ?, datetime('now', 'localtime'))";
    return createSession(newSession, sql);
  }

  /**
   * @param newSession (Session)
   * @param hours      (double)
   * @return (boolean), false if everything works will
   * @throws RuntimeException if something want wrong in the db
   */
  @Override
  public boolean createTimedSession(Session newSession, double hours) {
    final String sql = "INSERT INTO sessions (session_id, spot_id, creator, controllers_number, "
        + "start_time, end_time) VALUES (?, ?, ?, ?, datetime('now', 'localtime'), "
        + "datetime('now', 'localtime', '+" + hours + " hours'))";
    // inject hours parameter like this bc the "?" way did not work.
    return createSession(newSession, sql);
  }

  /**
   * @param oldSession (Session)
   * @return (boolean), false if everything works will
   * @throws RuntimeException if something want wrong in the db
   */
  @Override
  public boolean endSession(@NotNull Session oldSession) {
    final String sql = "UPDATE sessions SET end_time = datetime('now', 'localtime'), creator = ? "
        + "WHERE spot_id = ? AND session_state = 'RUNNING'";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, oldSession.getCreator());
      statement.setInt(2, oldSession.getSpotId());
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(
          "Failed, Couldn't end session" + oldSession + ". " + e.getMessage(), e);
    }
  }

  /**
   * @param spotId (int)
   * @return (Session) object contains the controllers number and the duration of the session based
   * on gavin session ID
   * @throws RuntimeException if something want wrong in the db
   */
  @Override
  public SessionData getSessionData(int spotId) {
    final String sql = "SELECT se.session_id, se.controllers_number, se.duration, sp.spot_privacy,"
        + " sp.console_type FROM sessions se INNER JOIN spots sp ON se.spot_id = sp.spot_id WHERE "
        + "se.spot_id = ? ORDER BY se.start_time DESC LIMIT 1 OFFSET 0";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, spotId);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return buildCalculateObject(rs);
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't get Session Data. " + e.getMessage(), e);
    }
  }

  /**
   * @param sessionPrice (double)
   * @param sessionId    (int)
   * @throws RuntimeException if something want wrong in the db
   * @produce write the session Price to the db
   */
  @Override
  public boolean writeSessionPrice(double sessionPrice, int sessionId) {
    final String sql = "UPDATE sessions SET session_price = ROUND(?, 2) where session_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setDouble(1, sessionPrice);
      statement.setInt(2, sessionId);
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to write the session price. " + e.getMessage(), e);
    }
  }

  /**
   * @param date (String)
   * @return (array of doubles) if everything go right in the db
   * @throws RuntimeException if something want wrong in the db
   */
  @Override
  public double[] getSessionCountAndSumPrices(String date) {
    final String sql = "SELECT COUNT(*) AS session_count, SUM(session_price) AS total_price FROM"
        + " sessions WHERE session_id LIKE '" + date + "%' AND session_state = 'DONE'";
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      if (rs.next()) {
        return new double[]{rs.getInt("session_count"), rs.getDouble("total_price")};
      }
      throw new IllegalArgumentException("Couldn't get Result with this Date.");
    } catch (SQLException e) {
      throw new RuntimeException(
          "Failed, Couldn't get Session count and the total prices of them. " + e.getMessage(), e);
    }
  }

  /**
   * @return (int)
   * @throws RuntimeException if something want wrong in the db
   * @produce the last session ID in the sessions table from session_id column
   */
  @Override
  public int getLastSessionId() {
    final String sql = "SELECT session_id FROM sessions ORDER BY session_id DESC LIMIT 1 OFFSET 0";
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      if (rs.next()) {
        return rs.getInt("session_id");
      }
      throw new RuntimeException("Failed, Couldn't get last Session ID. 1");
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't get last Session ID. " + e.getMessage(), e);
    }
  }

  // Helper method to execute the insert query with common parameters and extra ones if needed.
  private boolean createSession(@NotNull Session newSession, String sql) {
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      // Set common session parameters
      statement.setInt(1, newSession.getSessionId());
      statement.setInt(2, newSession.getSpotId());
      statement.setString(3, newSession.getCreator());
      statement.setInt(4, newSession.getNoControllers());

      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(
          "Failed, Couldn't Create Session: " + newSession + ". " + e.getMessage(), e);
    }
  }

  private Session readSessionForDashboard(ResultSet rs) {
    try {
      return new Session.Builder().sessionId(rs.getInt("session_id")).spotId(rs.getInt("spot_id"))
          .creator(rs.getString("creator")).noControllers(rs.getInt("controllers_number"))
          .startTime(rs.getString("start_time")).endTime(rs.getString("end_time"))
          .duration(rs.getDouble("duration")).build();
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed, Couldn't build Session for result Page. " + e.getMessage(), e);
    }
  }

  private Session readSessionForResults(@NotNull ResultSet rs) {
    try {
      return new Session.Builder().sessionId(rs.getInt("session_id"))
          .duration(rs.getDouble("duration")).sessionPrice(rs.getDouble("session_price")).build();
    } catch (SQLException e) {
      throw new RuntimeException(
          "Failed, Couldn't build Session for result Page. " + e.getMessage(), e);
    }
  }

  @Contract("_ -> new")
  private @NotNull SessionData buildCalculateObject(@NotNull ResultSet resultSet) {
    try {
      return new SessionData(resultSet.getInt("session_id"), resultSet.getInt("controllers_number"),
          resultSet.getDouble("duration"), SpotType.valueOf(resultSet.getString("spot_privacy")),
          ConsoleType.valueOf(resultSet.getString("console_type")));
    } catch (SQLException e) {
      throw new RuntimeException("Couldn't build Session Data" + e.getMessage());
    }
  }
}
