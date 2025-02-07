package com.gamingCoffee.services;

import com.gamingCoffee.database.controller.ISessionDao;
import com.gamingCoffee.database.entities.Session;
import com.gamingCoffee.utiles.PopupUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SessionDao implements ISessionDao {

  private final Connection connection;

  public SessionDao(Connection connection) {
    this.connection = connection;
  }

  /**
   * @return list of ten of current sessions
   */
  @Override
  public List<Session> getCurrentSessions() {
    String sql = "SELECT session_id, spot_id, creator, controllers_number, start_time, end_time,"
        + " ROUND((JULIANDAY(datetime('now', 'localtime')) - JULIANDAY(start_time)) * 24, 2) "
        + "AS duration FROM sessions WHERE session_state = 'RUNNING'";
    List<Session> sessions = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        sessions.add(readSession(rs));
      }
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
    return sessions;
  }
  // 250125001
  // 000000002
  // 260125001

  /**
   * @return list of session created in gavin date
   * @Pram
   */
  @Override
  public List<Session> getSessionsPerDate(String date) {
    String sql =
        "SELECT session_id, duration, session_price FROM sessions WHERE session_id BETWEEN " + date
            + "000 AND " + date + "999 AND session_state = 'DONE'";
    List<Session> sessions = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        sessions.add(new Session.Builder().sessionId(rs.getInt("session_id"))
            .duration(rs.getDouble("duration")).sessionPrice(rs.getDouble("session_price"))
            .build());
      }
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
    return sessions;
  }

  /**
   * @throws SQLException if something want wrong in the db
   */
  @Override
  public boolean createOpenSession(Session newSession) throws SQLException {
    String sql = "INSERT INTO sessions (session_id, spot_id, creator, controllers_number, "
        + "start_time) VALUES (?, ?, ?, ?, datetime('now', 'localtime'))";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, newSession.getSessionId());
      statement.setInt(2, newSession.getSpotId());
      statement.setString(3, newSession.getCreator());
      statement.setInt(4, newSession.getNoControllers());
      return statement.execute();
    }
  }

  public boolean createTimedSession(Session newSession, double hours) throws SQLException {
    String sql = "INSERT INTO sessions (session_id, spot_id, creator, controllers_number, "
        + "start_time, end_time) VALUES (?, ?, ?, ?, datetime('now', 'localtime'), "
        + "datetime('now', 'localtime', '+" + hours + "hours'))";
    // inject hours parameter like this bc the "?" way did not work.
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, newSession.getSessionId());
      statement.setInt(2, newSession.getSpotId());
      statement.setString(3, newSession.getCreator());
      statement.setInt(4, newSession.getNoControllers());
      return statement.execute();
    }
  }

  /**
   * @throws SQLException if something want wrong in the db
   */
  @Override
  public boolean endSession(Session oldSession) throws SQLException {
    String sql = "UPDATE sessions SET end_time = datetime('now', 'localtime'),"
        + " creator = ? WHERE spot_id = ? AND session_id = ? AND session_state = 'RUNNING'";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, oldSession.getCreator());
      statement.setInt(2, oldSession.getSpotId());
      statement.setInt(3, oldSession.getSessionId());
      return statement.execute();
    }
  }

  @Override
  public Session getNoControllersAndDuration(int sessionId) throws SQLException {
    String sql = "SELECT controllers_number, duration from sessions where session_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, sessionId);
      try (ResultSet rs = statement.executeQuery()) {
        return new Session.Builder().noControllers(rs.getInt("controllers_number"))
            .duration(rs.getDouble("duration")).build();
      }
    }
  }

  @Override
  public void writeSessionPrice(double sessionPrice, int sessionId) throws SQLException {
    String sql = "UPDATE sessions SET session_price = ? where session_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setDouble(1, sessionPrice);
      statement.setInt(2, sessionId);
      statement.executeUpdate();
    }
  }


  public double[] getSessionCountAndSumPrices(String date) throws SQLException {
    String sql = "SELECT COUNT(*) AS session_count, SUM(session_price) AS total_price FROM sessions"
        + " WHERE session_id BETWEEN " + date + "000 AND " + date + "999 AND session_state = "
        + "'DONE'";
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      if (rs.next()) {
        return new double[]{rs.getInt("session_count"), rs.getDouble("total_price")};
      }
      return null;
    }
  }

  public int getRunningSessionBySpot(int spotId) throws SQLException {
    String sql = "SELECT session_id FROM sessions WHERE spot_id = ? and session_state = 'RUNNING'";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, spotId);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        return rs.getInt("session_id");
      }
      return 0;
    }
  }

  @Override
  public int getLastSessionId() throws SQLException {
    String sql = "SELECT session_id FROM sessions ORDER BY session_id DESC LIMIT 1";
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      if (rs.next()) {
        return rs.getInt("session_id");
      } else {
        return 0;
      }
    }
  }

  private Session readSession(ResultSet rs) {
    try {
      return new Session.Builder().sessionId(rs.getInt("session_id")).spotId(rs.getInt("spot_id"))
          .creator(rs.getString("creator")).noControllers(rs.getInt("controllers_number"))
          .startTime(rs.getString("start_time")).endTime(rs.getString("end_time"))
          .duration(rs.getDouble("duration")).build();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
