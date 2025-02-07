package com.gamingCoffee.services;

//import static com.gamingCoffee.services.AdminService.hashPassword;

import com.gamingCoffee.database.controller.IAdminDao;
import com.gamingCoffee.database.entities.Admin;
import com.gamingCoffee.models.Position;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDao implements IAdminDao {

  private final Connection connection;

  public AdminDao(Connection connection) {
    this.connection = connection;
  }

  /**
   * @param userName String //   * @param password String
   * @return String Welcome if the username and password are correct and other massage if one of
   * them not right
   * @throws SQLException in the case of something went wrong we call to the db happened
   */
  @Override
  public Admin getUserData(String userName) throws SQLException {
    String sql = "SELECT title, password FROM admins WHERE username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, userName);
      try (ResultSet rs = statement.executeQuery()) {
        return new Admin.Builder().username(userName).title(Position.valueOf(rs.getString("title")))
            .password(rs.getString("password")).build();
      }
    }
  }

  /**
   * @param newAdmin get admin (username, password, title, age, number if possible) form admin
   *                 factory and write the instance down to the db
   * @throws SQLException if something wrong happened while connecting to db
   */
  @Override
  public void addUser(Admin newAdmin) throws SQLException {
    String sql = "INSERT INTO admins (username, password, title, age, salary, hire_date) VALUES "
        + "(?, ?, ?, ?, ?,date('now')) ";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, newAdmin.getUsername());
      statement.setString(2, newAdmin.getPassword());
      statement.setString(3, newAdmin.getTitle().toString());
      statement.setInt(4, newAdmin.getAge());
      statement.setInt(5, newAdmin.getSalary());
      statement.executeUpdate();
    }
  }

  /**
   * @param username String
   * @throws SQLException if something wrong happened while connecting to db
   */
  @Override
  public void removeUser(String username) throws SQLException {
    String sql = "DELETE FROM admins WHERE username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      statement.executeUpdate();
    }
  }

//  /**
//   * @param admin int
//   * @throws SQLException if something wrong happened while connecting to db
//   */
//  @Override
//  public void removeUserByUserId(Admin admin) throws SQLException {
//    String sql = "DELETE FROM admins WHERE username = ?";
//    try (PreparedStatement statement = connection.prepareStatement(sql)) {
////      statement.setInt(1, admin.getAdminId());
////      statement.setString(1, admin.getTitle().toString());
//      statement.setString(1, admin.getUsername());
//      statement.executeUpdate();
//    }
//  }

//  /**
//   * @param admin_id    int
//   * @param newUserName String
//   * @throws SQLException if something wrong happened while connecting to db
//   */
//  @Override
//  public void changeUserName(int admin_id, String newUserName) throws SQLException {
//    String sql = "UPDATE admins SET username = ? WHERE admin_id = ?";
//    try (PreparedStatement statement = connection.prepareStatement(sql)) {
//      statement.setString(1, newUserName);
//      statement.setInt(2, admin_id);
//      statement.executeUpdate();
//    }
//  }

  /**
   * @param username    String
   * @param newPassword String
   * @throws SQLException if something wrong happened while connecting to db
   */
  @Override
  public boolean changePassword(String username, String newPassword) throws SQLException {
    String sql = "UPDATE admins SET password = ? WHERE username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, newPassword);
      statement.setString(2, username);
      return statement.execute();
    }
  }

//  /**
//   * @param userName String
//   * @return phoneNumber int
//   * @throws SQLException if something wrong happened while connecting to db
//   */
//  @Override
//  public int getUserId(String userName) throws SQLException {
//    String sql = "SELECT admin_id FROM admins WHERE username = ?";
//    try (PreparedStatement statement = connection.prepareStatement(sql)) {
//      statement.setString(1, userName);
//      ResultSet rs = statement.executeQuery();
//      return rs.getInt("admin_id");
//    }
//  }

  /**
   * @param admin username, phone Number
   * @throws SQLException if something wrong happened while connecting to db
   */
  @Override
  public void addPhoneNumber(Admin admin) throws SQLException {
    String sql = "INSERT INTO admin_phones (username, phone_number) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, admin.getUsername());
      statement.setString(2, admin.getPhoneNumber());
      statement.executeUpdate();
    }
  }

  @Override
  public List<Admin> getAdmins() throws SQLException {
    String sql = "SELECT a.username, a.age, a.hire_date, a.salary, p.phone_number FROM admins a "
        + "LEFT JOIN admin_phones p ON a.username = p.username";
    ArrayList<Admin> admins = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        admins.add(new Admin.Builder().username(rs.getString("username")).age(rs.getInt("age"))
            .hiringDate(rs.getString("hire_date")).salary(rs.getInt("salary"))
            .phoneNumber(rs.getString("phone_number")).build());
      }
      return admins;
    }
  }

  public Admin getAdminInfo(String username) throws SQLException {
    String sql = "SELECT a.age, a.hire_date, a.salary, p.phone_number FROM admins a LEFT JOIN "
        + "admin_phones p ON a.username = p.username WHERE a.username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return new Admin.Builder().username(username).age(rs.getInt("age"))
              .hiringDate(rs.getString("hire_date")).salary(rs.getInt("salary"))
              .phoneNumber(rs.getString("phone_number")).build();
        }
      }
    }
    return null;
  }

  public String getUserTitle(String username) throws SQLException {
    String sql = "SELECT title FROM admin WHERE username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return rs.getString("title");
        }
      }
    }
    return null;
  }

  @Override
  public String getPasswordByUsername(String username) throws SQLException {
    String sql = "SELECT password FROM admins where username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return rs.getString("password");
        }
      }
    }
    return null;
  }

  public void removePhoneNumber(String username) throws SQLException {
    String sql = "DELETE phone_number FROM admin_phones WHERE username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      statement.executeUpdate();
    }
  }
}
