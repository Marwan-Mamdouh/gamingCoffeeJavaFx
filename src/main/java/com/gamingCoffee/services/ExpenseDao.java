package com.gamingCoffee.services;

import com.gamingCoffee.database.controller.IExpenseDao;
import com.gamingCoffee.database.entities.Expense;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDao implements IExpenseDao {

  private final Connection connection;

  public ExpenseDao(Connection connection) {
    this.connection = connection;
  }

  /**
   * @param expense object (expenseCreator, expenseAmount, expenseDate, expenseNote)
   * @return false if the statement done with no result set
   * @throws RuntimeException if something went wrong
   * @produce add a new expense to db
   */
  @Override
  public boolean addExpense(Expense expense) {
    final String sql = "INSERT INTO expenses (exp_creator, exp_amount, exp_date, exp_note) VALUES "
        + "(?, ?, DATE('now', 'localtime'), ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, expense.getCreator());
      statement.setDouble(2, expense.getExpenseAmount());
      statement.setString(3, expense.getNote());
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't add new Expense. " + e.getMessage(), e);
    }
  }

  /**
   * @param expenseId (int) get it from the user
   * @throws RuntimeException if something went wrong
   * @produce a description for the gavin expense ID
   */
  @Override
  public Expense checkExpense(int expenseId) {
    final String sql = "SELECT exp_id, exp_creator, exp_amount, exp_date, exp_note FROM expenses"
        + " WHERE exp_id = ? LIMIT 1 OFFSET 0";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, expenseId);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return makeExpense(rs);
        }
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException(
          "Failed, no match for Expense ID:" + expenseId + ". " + e.getMessage(), e);
    }
  }

  /**
   * @param expenseId int (Expense ID)
   * @return false if the statement done with no result set
   * @throws RuntimeException if something went wrong with the database
   * @produce remove the gavin ID controller
   */
  @Override
  public boolean removeExpense(int expenseId) {
    final String sql = "DELETE FROM expenses WHERE exp_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, expenseId);
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(
          "Failed, Couldn't remove Expense ID:" + expenseId + ". " + e.getMessage(), e);
    }
  }

  /**
   * @param date LocalDate from date Picker fx
   * @return a list of Expense based of the date
   * @throws RuntimeException if something went wrong with the database
   * @produce a List of Expense by gavin date
   */
  @Override
  public List<Expense> getExpensesByDate(LocalDate date) {
    final String sql = "SELECT exp_id, exp_creator, exp_amount, exp_date, exp_note FROM expenses "
        + "WHERE exp_date = ?";
    final List<Expense> expenses = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, date.toString());
      try (ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {
          expenses.add(makeExpense(rs));
        }
        return expenses;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't get Expense by Date. " + e.getMessage(), e);
    }
  }

  /**
   * @param date LocalDate from date Picker fx
   * @return list of Expense form the start of the month till the date
   * @throws RuntimeException if something went wrong with the database
   * @produce a list of expense from the start of the month till the gavin date
   */
  @Override
  public List<Expense> getExpensesByMonth(String date) {
    final String sql = "SELECT exp_id, exp_creator, exp_amount, exp_date, exp_note FROM expenses"
        + " WHERE exp_date LIKE '" + date + "%'";
    // 2025-03-03
    final List<Expense> expenses = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        expenses.add(makeExpense(rs));
      }
      return expenses;
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't get Expense by month. " + e.getMessage(), e);
    }
  }

  /**
   * @param date LocalDate from date Picker fx
   * @return (double) total Expense based on gavin date
   * @throws RuntimeException if something went wrong with the database
   * @produce the sum of the Expense based on gavin date, 0.0 of nothing there
   */
  @Override
  public double getExpensePriceByDay(LocalDate date) {
    final String sql = "SELECT SUM(exp_amount) AS expense_price FROM expenses where exp_date = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, date.toString());
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return rs.getDouble("expense_price");
        }
        return 0.0;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to get the Sum of the Expense by Date. " + e.getMessage(),
          e);
    }
  }

  /**
   * @param rs result set to extract expense object
   * @return (Expense object) build an Expense object from the result set gavin
   * @throws SQLException if something went wrong with the database
   * @produce an Expense object
   */
  private Expense makeExpense(ResultSet rs) throws SQLException {
    try {
      return new Expense.Builder().expenseId(rs.getInt("exp_id"))
          .creator(rs.getString("exp_creator")).expenseAmount(rs.getDouble("exp_amount"))
          .note(rs.getString("exp_note")).expenseDate(LocalDate.parse(rs.getString("exp_date")))
          .build();
    } catch (SQLException e) {
      throw new RuntimeException("Failed to build an Expense object. " + e.getMessage(), e);
    }
  }
}
