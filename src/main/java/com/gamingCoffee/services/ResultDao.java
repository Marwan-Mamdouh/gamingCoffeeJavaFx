package com.gamingCoffee.services;

import com.gamingCoffee.database.controller.IResultDao;
import com.gamingCoffee.database.entities.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ResultDao implements IResultDao {

  private final Connection connection;

  public ResultDao(Connection connection) {
    this.connection = connection;
  }

  /**
   * @param result (Object)
   * @return (boolean) true if every thing goes will false otherwise
   */
  boolean addNewResult(@NotNull Result result) {
    final String sql = "INSERT INTO result (day, income, sessions_count, expenses, expense_count, "
        + "final_result) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setDouble(1, result.getDay());
      statement.setDouble(2, result.getIncome());
      statement.setDouble(3, result.getSessionCount());
      statement.setDouble(4, result.getExpenses());
      statement.setDouble(5, result.getExpensesCount());
      statement.setDouble(6, result.getFinalResult());
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Couldn't write a result to db bc: " + e.getMessage());
    }
  }

  /**
   * @param date (String)
   * @return (Result Object)
   */
  @Override
  public Result getResultByDay(int id, LocalDate date) {
    final String sql = "SELECT day, income, sessions_count, expenses, expense_count, final_result"
        + " FROM result WHERE day = ? LIMIT 1 OFFSET 0";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        return buildResult(rs);
      } else {
        if (writeResult(id, date)) {
          return getResultByDay(id, date);
        } else {
          throw new RuntimeException("couldn't get data or write it.");
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * @param month (Month enum)
   * @return list of results based on gavin month
   */
  @Override
  public List<Result> getResultByMonth(@NotNull Month month) {
    final String sql = "SELECT day, income, sessions_count, expenses, expense_count, final_result"
        + " FROM result WHERE day like '25%" + month.getValue() + "%' LIMIT 31 OFFSET 0;";
    final List<Result> results = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(
        sql); ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        results.add(buildResult(rs));
      }
      return results;
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private boolean writeResult(int id, @NotNull LocalDate date) {
    final String sql = "INSERT INTO result (day, income, sessions_count, expenses, expense_count,"
        + " final_result) SELECT ?, inc.total_income, inc.sessions_count, expe.total_expenses,"
        + " expe.expense_count, COALESCE(inc.total_income - expe.total_expenses, inc.total_income)"
        + " AS final_result FROM (SELECT ROUND(SUM(session_price),2) AS total_income, COUNT(*) AS"
        + " sessions_count FROM sessions WHERE session_id LIKE '" + id + "%') AS inc,"
        + " (SELECT ROUND(SUM(exp_amount),2) AS total_expenses, COUNT(*) AS expense_count"
        + " FROM Expenses WHERE exp_date = ?) AS expe;";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      statement.setString(2, date.toString());
      return 0 < statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private @NotNull Result buildResult(@NotNull ResultSet rs) {
    try {
      return Result.create(rs.getInt("day"), rs.getInt("income"), rs.getInt("sessions_count"),
          rs.getInt("expenses"), rs.getInt("expense_count"), rs.getInt("final_result"));
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}