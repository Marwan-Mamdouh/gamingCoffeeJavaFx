package com.gamingCoffee.spot.dao;

import com.gamingCoffee.spot.repository.SpotRepository;
import com.gamingCoffee.spot.model.Spot;
import com.gamingCoffee.spot.model.ConsoleType;
import com.gamingCoffee.spot.model.SpotState;
import com.gamingCoffee.spot.model.SpotType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class SpotDao implements SpotRepository {

  private final Connection connection;

  public SpotDao(Connection connection) {
    this.connection = connection;
  }

  /**
   * Deletes a spot from the database by its ID.
   *
   * @param spotId The ID of the spot to delete.
   * @return {@code true} if a spot was deleted, {@code false} if no spot matched the ID.
   * @throws RuntimeException if a database error occurs.
   */
  @Override
  public boolean removeById(int spotId) {
    final String sql = "DELETE FROM spots WHERE spot_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, spotId);
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to delete spot with ID: " + spotId, e);
    }
  }

  /**
   * @return (List < Spot >)
   * @throws RuntimeException if a database error occurs.
   * @produce a list of all Spots
   */
  @Override
  public List<Spot> getAllSpots() {
    final String sql = "SELECT spot_id, spot_privacy, spot_state, display_id, display_size, "
        + "display_type, console_id, console_type FROM spots";
    final List<Spot> spots = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        spots.add(makeFullSpot(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to get Spots. " + e.getMessage(), e);
    }
    return spots;
  }

  /**
   * @param newSpot (Spot) object
   * @return {@code true} if a spot was inserted, {@code false} otherwise.
   * @throws RuntimeException if a database error occurs.
   */
  public boolean addSpot(@NotNull Spot newSpot) {
    final String sql = "INSERT INTO spots (spot_privacy, spot_state, display_id, display_type,"
        + " display_size, console_id, console_type) VALUES (?, 'AVAILABLE', ?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, newSpot.getSpotType().toString());
      statement.setInt(2, newSpot.getDisplayId());
      statement.setString(3, newSpot.getDisplayType());
      statement.setInt(4, newSpot.getDisplaySize());
      statement.setInt(5, newSpot.getConsoleId());
      statement.setString(6, newSpot.getConsoleType().toString());
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to add a spot. " + e.getMessage(), e);
    }
  }

  /**
   * @param spotId (int)
   * @return (Spot) object
   * @throws RuntimeException if a database error occurs.
   */
  @Override
  public Spot checkSpot(int spotId) {
    final String sql = "SELECT spot_privacy, console_id, console_type, display_id, display_size, "
        + "display_type FROM spots WHERE spotId = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, spotId);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return makeSpotForCheck(rs);
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException(" Failed, no match for spot ID: " + spotId + ". " + e.getMessage(),
          e);
    }
  }

  /**
   * @return List<Spot>, get all free spots from the db
   * @throws RuntimeException if a database error occurs.
   */
  @Override
  public List<Spot> getFreeSpots() {
    final String sql = "SELECT spot_id, spot_privacy, console_type, display_type, display_size FROM"
        + " spots WHERE spot_state = 'AVAILABLE'";
    final List<Spot> spots = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        spots.add(makeSpot(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to get Free Spots. " + e.getMessage(), e);
    }
    return spots;
  }

  @Override
  public List<Integer> getBusySpotsNumbers() {
    return getSpotsByAvailability("NOT_AVAILABLE");
  }

  @Override
  public List<Integer> getFreeSpotsNumbers() {
    return getSpotsByAvailability("AVAILABLE");
  }

  public double getPricePerHour(@NotNull SpotType spotType, @NotNull ConsoleType consoleType) {
    final String sql = "SELECT price FROM prices WHERE service_type = ? OR service_type = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, spotType.toString());
      statement.setString(2, consoleType.toString());
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return addPrices(rs);
        }
      }
      return 0.0;
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public boolean setPrice(@NotNull Enum<?> serviceType, double price) {
    final String sql = "UPDATE prices SET price = ? WHERE service_type = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setDouble(1, price);
      statement.setString(2, serviceType.toString());
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public double getServicePrice(@NotNull Enum<?> serviceType) {
    final String sql = "SELECT price FROM prices WHERE service_type = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, serviceType.toString());
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return rs.getDouble("price");
        } else {
          throw new RuntimeException("No match for:" + serviceType + " in db.");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private List<Integer> getSpotsByAvailability(String availability) {
    final String sql = "SELECT spot_id FROM spots WHERE spot_state = ?";
    final List<Integer> spots = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, availability);
      try (ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {
          spots.add(rs.getInt("spot_id"));
        }
        return spots;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to get Spots. " + e.getMessage());
    }
  }

  private Spot makeFullSpot(ResultSet rs) throws SQLException {
    try {
      return new Spot.Builder().spotId(rs.getInt("spot_id"))
          .spotType(SpotType.valueOf(rs.getString("spot_privacy")))
          .spotState(SpotState.valueOf(rs.getString("spot_state")))
          .displaySize(rs.getInt("display_size")).consoleId(rs.getInt("console_id"))
          .consoleType(ConsoleType.valueOf(rs.getString("console_type")))
          .displayId(rs.getInt("display_id")).displayType(rs.getString("display_type")).build();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to build Spot. " + e.getMessage(), e);
    }
  }

  private Spot makeSpotForCheck(ResultSet rs) throws SQLException {
    try {
      return new Spot.Builder().spotType(SpotType.valueOf(rs.getString("spot_privacy")))
          .consoleId(rs.getInt("console_id")).displaySize(rs.getInt("display_size"))
          .consoleType(ConsoleType.valueOf(rs.getString("console_type")))
          .displayId(rs.getInt("display_id")).displayType(rs.getString("display_type")).build();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to build Spot. " + e.getMessage(), e);
    }
  }

  private Spot makeSpot(ResultSet rs) {
    try {
      return new Spot.Builder().spotId(rs.getInt("spot_id"))
          .spotType(SpotType.valueOf(rs.getString("spot_privacy")))
          .consoleType(ConsoleType.valueOf(rs.getString("console_type")))
          .displayType(rs.getString("display_type")).displaySize(rs.getInt("display_size")).build();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to build Spot. " + e.getMessage(), e);
    }
  }

  private double addPrices(ResultSet resultSet) throws SQLException {
    double price = 0.0;
    try {
      while (resultSet.next()) {
        price += resultSet.getDouble("price");
      }
      return price;
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
