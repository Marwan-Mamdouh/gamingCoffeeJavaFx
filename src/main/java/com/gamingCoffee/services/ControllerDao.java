package com.gamingCoffee.services;

import com.gamingCoffee.database.controller.IControllerDao;
import com.gamingCoffee.database.entities.Controller;
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
   * @throws SQLException
   */
  @Override
  public boolean addController(Controller controller) throws SQLException {
    String sql = "INSERT INTO controllers (controller_id, controller_type) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, controller.getControllerId());
      statement.setString(2, controller.getControllerType().toString());
      return statement.execute();
    }
  }

  /**
   * @throws SQLException
   */
  @Override
  public boolean removeController(int controllerId) throws SQLException {
    String sql = "DELETE FROM controllers WHERE controller_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, controllerId);
      return statement.execute();
    }
  }

  /**
   * @throws SQLException
   */
  public Controller checkController(int controllerId) throws SQLException {
    String sql = "SELECT controller_type FROM controllers WHERE controller_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, controllerId);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return new Controller.Builder().controllerId(controllerId)
              .controllerType(ControllerType.valueOf(rs.getString("controller_type"))).build();
        }
      }
    }
    return null;
  }

  @Override
  public List<Controller> getAllController() throws SQLException {
    String sql = "SELECT * FROM controllers";
    List<Controller> controllers = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        controllers.add(new Controller.Builder().controllerType(
                ControllerType.valueOf(rs.getString("controller_type")))
            .controllerId(rs.getInt("controller_id")).build());
      }
    }
    return controllers;
  }
}
