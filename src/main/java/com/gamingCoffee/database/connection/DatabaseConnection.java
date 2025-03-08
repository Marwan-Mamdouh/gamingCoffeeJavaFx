package com.gamingCoffee.database.connection;

import com.gamingCoffee.utiles.PopupUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Enum singleton
public enum DatabaseConnection {

  INSTANCE; // The single instance
  private final Connection connection;

  DatabaseConnection() {
    try {
      connection = DriverManager.getConnection(
          "jdbc:sqlite:src/main/java/com/gamingCoffee/database/database.db");
      // Enable foreign key support
      try (Statement stmt = connection.createStatement()) {
        stmt.execute("PRAGMA foreign_keys = ON;");
      }

      // Register a shutdown hook to close the connection when the app exits
      Runtime.getRuntime().addShutdownHook(new Thread(this::close));

    } catch (SQLException e) {
      PopupUtil.showErrorPopup(e);
      throw new RuntimeException("Error connecting to the database.", e);
    }
  }

  public Connection getConnection() {
    return connection;
  }

  /**
   * @throws RuntimeException ()
   */
  public void close() {
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error closing database connection: " + e.getMessage(), e);
    }
  }
}
