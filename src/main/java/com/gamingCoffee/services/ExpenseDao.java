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
   * @param expenseId
   * @throws SQLException
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
   * @param date
   * @return
   * @throws SQLException
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
   * @param date
   * @return
   * @throws SQLException
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

  private Expense makeExpense(ResultSet rs) throws SQLException {
    return new Expense.Builder().expenseId(rs.getInt("exp_id")).creator(rs.getString("exp_creator"))
        .expenseAmount(rs.getDouble("exp_amount")).note(rs.getString("exp_note"))
        .expenseDate(LocalDate.parse(rs.getString("exp_date"))).build();
  }
}
