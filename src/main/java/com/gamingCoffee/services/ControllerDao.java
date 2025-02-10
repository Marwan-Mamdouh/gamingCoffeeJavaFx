package com.gamingCoffee.services;

import com.gamingCoffee.database.controller.IControllerDao;
import com.gamingCoffee.database.entities.Controller;
import com.gamingCoffee.database.entities.Controller.Builder;
import com.gamingCoffee.models.ControllerType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerDao implements IControllerDao {

  private final Connection connection;

  public ControllerDao(Connection connection) {
    this.connection = connection;
  }


  /**
   * @param controller object
   * @return false bc there is no return from this query
   * @throws RuntimeException if something went wrong with the db
   * @produce write new controller to the db
   */
  @Override
  public boolean addController(Controller controller) {
    final String sql = "INSERT INTO controllers (controller_id, controller_type) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, controller.getControllerId());
      statement.setString(2, controller.getControllerType().toString());
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't add new Controller." + controller.toString(), e);
    }
  }

  /**
   * @param controllerId (int)
   * @return false bc there is no return from this query
   * @throws SQLException if something went wrong with the db
   * @produce remove controller form db based on controller ID gavin
   */
  @Override
  public boolean removeController(int controllerId) throws SQLException {
    final String sql = "DELETE FROM controllers WHERE controller_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, controllerId);
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't remove Controller ID:" + controllerId, e);
    }
  }

  /**
   * @param controllerId (int)
   * @return controller (object)
   * @throws SQLException if something went wrong with the db
   * @produce a controller from the db based on controller ID gavin
   */
  public Controller checkController(int controllerId) {
    final String sql = "SELECT controller_type FROM controllers WHERE controller_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, controllerId);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return new Builder().controllerId(controllerId)
              .controllerType(ControllerType.valueOf(rs.getString("controller_type"))).build();
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed, no match for controller ID:" + controllerId, e);
    }
  }

  /**
   * @return List<Controller> (object)
   * @throws SQLException if something went wrong with the db
   * @produce a list of all controllers
   */
  @Override
  public List<Controller> getAllController() throws SQLException {
    final String sql = "SELECT * FROM controllers";
    final List<Controller> controllers = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        controllers.add(
            new Builder().controllerType(ControllerType.valueOf(rs.getString("controller_type")))
                .controllerId(rs.getInt("controller_id")).build());
      }
      return controllers;
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't get Controllers Data.", e);
    }
  }
}
