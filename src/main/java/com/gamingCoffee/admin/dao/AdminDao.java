package com.gamingCoffee.admin.dao;

import com.gamingCoffee.admin.repository.AdminRepository;
import com.gamingCoffee.admin.model.Admin;
import com.gamingCoffee.admin.model.Position;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class AdminDao implements AdminRepository {

  private final Connection connection;

  public AdminDao(Connection connection) {
    this.connection = connection;
  }

  /**
   * @param userName String
   * @return (Admin) object contains admin, title from admins table based on username
   * @throws RuntimeException in the case of something went wrong when connecting to the db
   */
  @Override
  public Admin getUserData(String userName) {
    final String sql = "SELECT title, password FROM admins WHERE username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, userName);
      try (ResultSet rs = statement.executeQuery()) {
        return new Admin.Builder().username(userName).title(Position.valueOf(rs.getString("title")))
            .password(rs.getString("password")).build();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed, no match for username. " + e.getMessage(), e);
    }
  }

  /**
   * @param newAdmin {@code Admin} contains {@code username (String)}, {@code password (String)},
   *                 {@code title (Position)}, {@code age (int)}, {@code number (String)} (if
   *                 possible)
   * @throws RuntimeException if something wrong happened while connecting to db
   */
  @Override
  public boolean addUser(@NotNull Admin newAdmin) {
    final String sql = "INSERT INTO admins (username, password, title, age, salary, hire_date) "
        + "VALUES (?, ?, ?, ?, ?,date('now')) ";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, newAdmin.getUsername());
      statement.setString(2, newAdmin.getPassword());
      statement.setString(3, newAdmin.getTitle().toString());
      statement.setInt(4, newAdmin.getAge());
      statement.setDouble(5, newAdmin.getSalary());
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to Add new Admin. " + e.getMessage(), e);
    }
  }

  /**
   * @param username String
   * @throws RuntimeException if something went wrong with the db
   */
  @Override
  public boolean removeUser(String username) {
    final String sql = "DELETE FROM admins WHERE username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed, no match for username. " + e.getMessage(), e);
    }
  }

  /**
   * @param username    String
   * @param newPassword String
   * @throws RuntimeException if something went wrong with the db
   */
  @Override
  public boolean changePassword(String username, String newPassword) {
    final String sql = "UPDATE admins SET password = ? WHERE username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, newPassword);
      statement.setString(2, username);
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed, can not change password. " + e.getMessage(), e);
    }
  }

  /**
   * @param admin (Admin)
   * @throws RuntimeException if something wrong happened while connecting to db
   */
  @Override
  public boolean addPhoneNumber(@NotNull Admin admin) {
    final String sql = "INSERT INTO admin_phones (username, phone_number) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, admin.getUsername());
      statement.setString(2, admin.getPhoneNumber());
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Can not add phone number. " + e.getMessage(), e);
    }
  }

  @Override
  public List<Admin> getAdmins() {
    final String sql = "SELECT a.username, a.title, a.age, a.hire_date, a.salary, p.phone_number"
        + " FROM admins a LEFT JOIN admin_phones p ON a.username = p.username";
    return queryAdmins(sql);
  }

  @Override
  public Admin getAdminInfo(String username) {
    final String sql = "SELECT a.username, a.age, a.hire_date, a.salary, p.phone_number FROM"
        + " admins a LEFT JOIN admin_phones p ON a.username = p.username WHERE a.username = ?";
    final List<Admin> admins = queryAdmins(sql, username);
    return admins.isEmpty() ? null : admins.getFirst();
  }

  @Override
  public String getPasswordByUsername(String username) {
    final String sql = "SELECT password FROM admins where username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return rs.getString("password");
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException(
          "Failed, no match for username:" + username + ". " + e.getMessage(), e);
    }
  }

  @Override
  public boolean removePhoneNumber(String username) {
    final String sql = "DELETE phone_number FROM admin_phones WHERE username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(
          "Failed, Can not remove Admin: " + username + " phone number. " + e.getMessage(), e);
    }
  }

  private Admin makeAdmin(@NotNull ResultSet rs) {
    try {
      return new Admin.Builder().username(rs.getString("username")).age(rs.getInt("age"))
          .hiringDate(rs.getString("hire_date")).salary(rs.getDouble("salary"))
          .phoneNumber(rs.getString("phone_number")).title(Position.valueOf(rs.getString("title")))
          .build();
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Can not build an Admin. " + e.getMessage(), e);
    }
  }

  // Helper method to execute the query and map results to Admin objects
  @Contract("null -> fail")
  private @NotNull List<Admin> queryAdmins(String... parameters) {
    final List<Admin> admins = new ArrayList<>();
    if (parameters == null || parameters.length < 1) {
      throw new RuntimeException("Failed, can not call queryAdmins with no parameters. layer 1");
    }
    try (PreparedStatement statement = connection.prepareStatement(parameters[0])) {
      // Set query parameters if provided
      if (parameters.length > 1) {
        statement.setString(1, parameters[1]);
      }
      try (ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {
          admins.add(makeAdmin(rs));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Cannot get Admin Data. " + e.getMessage(), e);
    }
    return admins;
  }
}
