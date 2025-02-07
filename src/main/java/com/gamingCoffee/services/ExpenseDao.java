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
   * @throws SQLException if something went wrong
   */
  @Override
  public boolean addExpense(Expense expense) throws SQLException {
    String sql = "INSERT INTO expenses (exp_creator, exp_amount, exp_date, exp_note) VALUES "
        + "(?, ?, date('now', 'localtime'), ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, expense.getCreator());
      statement.setDouble(2, expense.getExpenseAmount());
      statement.setString(3, expense.getNote());
      return statement.execute();
    }
  }

  /**
   * @param expenseId (int) get it from the user
   * @throws SQLException if something went wrong
   */
  @Override
  public Expense checkExpense(int expenseId) throws SQLException {
    String sql = "SELECT * FROM expenses WHERE exp_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, expenseId);
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return makeExpense(rs);
        }
        return null;
      }
    }
  }

  /**
   * @param expenseId int (Expense ID)
   * @return false if the statement done with no result set
   * @throws SQLException if something went wrong with the database
   */
  @Override
  public boolean removeExpense(int expenseId) throws SQLException {
    String sql = "DELETE FROM expenses WHERE exp_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, expenseId);
      return statement.execute();
    }
  }

  /**
   * @param date LocalDate from date Picker fx
   * @return a list of Expense based of the date
   * @throws SQLException if something went wrong with the database
   */
  @Override
  public List<Expense> getExpensesByDate(LocalDate date) throws SQLException {
    String sql = "SELECT * FROM expenses WHERE exp_date = ?";
    List<Expense> expenses = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, date.toString());
      try (ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {
          expenses.add(makeExpense(rs));
        }
      }
    }
    return expenses;
  }

  /**
   * @param date LocalDate from date Picker fx
   * @return list of Expense form the start of the month till the date
   * @throws SQLException if something went wrong with the database
   */
  @Override
  public List<Expense> getExpenseByMonth(LocalDate date) throws SQLException {
    String sql = "SELECT * FROM expenses WHERE DATE(exp_date) BETWEEN DATE(?, 'start of month')"
        + " AND DATE(?)";
    List<Expense> expenses = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, date.toString());
      statement.setString(2, date.toString());
      try (ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {
          expenses.add(makeExpense(rs));
        }
      }
    }
    return expenses;
  }

  /**
   * @param date LocalDate from date Picker fx
   * @return (double) total Expense based on gavin date
   * @throws SQLException if something went wrong with the database
   */
  @Override
  public double getExpensePriceByDay(LocalDate date) throws SQLException {
    String sql = "SELECT SUM(exp_amount) AS expense_price FROM expenses where exp_date = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, date.toString());
      try (ResultSet rs = statement.executeQuery()) {
        if (rs.next()) {
          return rs.getDouble("expense_price");
        }
      }
    }
    return 0.0;
  }

  /**
   * @param rs result set to extract expense object
   * @return (Expense object) build an Expense object from the result set gavin
   * @throws SQLException if something went wrong with the database
   */
  private Expense makeExpense(ResultSet rs) throws SQLException {
    return new Expense.Builder().expenseId(rs.getInt("exp_id")).creator(rs.getString("exp_creator"))
        .expenseAmount(rs.getDouble("exp_amount")).note(rs.getString("exp_note"))
        .expenseDate(LocalDate.parse(rs.getString("exp_date"))).build();
  }
}
