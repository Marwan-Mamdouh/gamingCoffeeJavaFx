package com.gamingCoffee.services;

import com.gamingCoffee.database.controller.ISpotDao;
import com.gamingCoffee.database.entities.Spot;
import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.SpotState;
import com.gamingCoffee.models.SpotType;
import com.gamingCoffee.utiles.PopupUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpotDao implements ISpotDao {

  private final Connection connection;

  public SpotDao(Connection connection) {
    this.connection = connection;
  }

//  /**
//   * @throws SQLException
//   */
//  @Override
//  public void addPublicSpot(Spot spot) throws SQLException {
//    addSpot(spot);
//  }
//
//  /**
//   * @throws SQLException
//   */
//  @Override
//  public void addPrivateSpot(Spot spot) throws SQLException {
//    addSpot(spot);
//  }

  /**
   * @throws SQLException
   */
  @Override
  public void removeById(int spotId) throws SQLException {
    String sql = "DELETE FROM spots WHERE spot_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, spotId);
//      statement.setInt(2, spot.getConsoleId());
//      statement.setInt(3, spot.getDisplayId());
      statement.execute();
    }
  }

  /**
   * @throws SQLException
   */
  @Override
  public void removeByType(Spot spot) throws SQLException {
    String sql = "DELETE FROM spots WHERE WHERE spot_privacy = ? AND console_type = ? AND "
        + "display_type = ? AND display_size = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, String.valueOf(spot.getSpotType()));
      statement.setString(2, String.valueOf(spot.getConsoleType()));
      statement.setString(3, spot.getDisplayType());
      statement.setInt(4, spot.getDisplaySize());
    }
  }

  /**
   * @return
   * @throws SQLException
   */
  @Override
  public List<Spot> getFreeSpots() throws SQLException {
    return getSpots("AVAILABLE");
  }

  /**
   * @return List of spots
   * @throws SQLException if something went wrong in sql or the db
   */
  @Override
  public List<Spot> getAllSpots() throws SQLException {
    String sql = "SELECT * FROM spots";
    List<Spot> spots = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        spots.add(new Spot.Builder().spotId(rs.getInt("spot_id"))
            .spotType(SpotType.valueOf(rs.getString("spot_privacy")))
            .spotState(SpotState.valueOf(rs.getString("spot_state")))
            .consoleId(rs.getInt("console_id")).displaySize(rs.getInt("display_size"))
            .consoleType(ConsoleType.valueOf(rs.getString("console_type")))
            .displayId(rs.getInt("display_id")).displayType(rs.getString("display_type")).build());
      }
    }
    return spots;
  }

  /**
   * @return
   * @throws SQLException
   */
  @Override
  public List<Spot> getFreePrivateSpots() throws SQLException {
    return getFreeOnes("PRIVATE");
  }

  /**
   * @return
   * @throws SQLException
   */
  @Override
  public List<Spot> getFreePublicSpots() throws SQLException {
    return getFreeOnes("PUBLIC");
  }

  @Override
  public Spot getSpotPrivacyAndConsoleType(int spotNumber) {
    String sql = "SELECT spot_privacy, console_type from spots where spot_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, spotNumber);
      try (ResultSet rs = statement.executeQuery()) {
        return new Spot.Builder().spotType(SpotType.valueOf(rs.getString("spot_privacy")))
            .consoleType(ConsoleType.valueOf(rs.getString("console_type"))).build();
      }
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
    return null;
  }

  public void addSpot(Spot spot) throws SQLException {
    String sql = "INSERT INTO spots (spot_privacy, spot_state, display_id, display_type, "
        + "display_size, console_id, console_type) VALUES (?, 'AVAILABLE', ?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, spot.getSpotType().toString());
      statement.setInt(2, spot.getDisplayId());
      statement.setString(3, spot.getDisplayType());
      statement.setInt(4, spot.getDisplaySize());
      statement.setInt(5, spot.getConsoleId());
      statement.setString(6, spot.getConsoleType().toString());
      statement.execute();
    }
  }

  @Override
  public Spot checkSpot(int spot_id) throws SQLException {
    String sql = "SELECT spot_privacy, console_id, console_type, display_id, display_size, "
        + "display_type FROM spots WHERE spot_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, spot_id);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return new Spot.Builder().spotType(SpotType.valueOf(rs.getString("spot_privacy")))
              .consoleId(rs.getInt("console_id")).displaySize(rs.getInt("display_size"))
              .consoleType(ConsoleType.valueOf(rs.getString("console_type")))
              .displayId(rs.getInt("display_id")).displayType(rs.getString("display_type")).build();
        }
      }
    }
    return null;
  }

  private List<Spot> getSpots(String availability) throws SQLException {
    String sql = "SELECT spot_id, spot_privacy, console_type, display_type, display_size FROM spots"
        + " WHERE spot_state = ?";
    List<Spot> spots = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, availability);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        spots.add(makeSpot(rs));
      }
    }
    return spots;
  }

  private List<Spot> getFreeOnes(String privacy) throws SQLException {
    String sql = "SELECT spot_id FROM spots WHERE spot_privacy = ? AND spot_state = 'AVAILABLE'";
    List<Spot> spots = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, privacy);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        spots.add(makeSpot(rs));
      }
    }
    return spots;
  }

  private Spot makeSpot(ResultSet rs) throws SQLException {
    return new Spot.Builder().spotId(rs.getInt("spot_id"))
        .spotType(SpotType.valueOf(rs.getString("spot_privacy")))
        .consoleType(ConsoleType.valueOf(rs.getString("console_type")))
        .displayType(rs.getString("display_type")).displaySize(rs.getInt("display_size")).build();
    //Spot.readSpot(rs.getInt("spot_id"), SpotType.valueOf(rs.getString("spot_privacy")),
    //        ConsoleType.valueOf(rs.getString("console_type")), rs.getString("display_type"),
    //        rs.getInt("display_size"))
  }
}
