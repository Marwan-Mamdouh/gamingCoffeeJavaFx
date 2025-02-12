package com.gamingCoffee.services;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.IAdminDao;
import com.gamingCoffee.database.controller.IExpenseDao;
import com.gamingCoffee.database.entities.Expense;
import com.gamingCoffee.database.entities.Expense.Builder;
import com.gamingCoffee.utiles.AdminUsernameHolder;
import com.gamingCoffee.utiles.ListUtils;
import com.gamingCoffee.utiles.PopupUtil;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class ExpenseService {

  private static final IExpenseDao expenseDao = new ExpenseDao(
      DatabaseConnection.INSTANCE.getConnection());

  /**
   * @param amount (double) the cost of the expense
   * @param note   (String) the description of the expense
   * @return (boolean) false if the every thing work normal true otherwise
   * @produce write a new Expense to the db
   */
  public static boolean addExpense(double amount, String note) {
    return writeExpense(
        new Builder().creator(AdminUsernameHolder.getAdminName()).expenseAmount(amount).note(note)
            .build());
  }

  /**
   * @param expenseId (int) the ID we check the database with
   * @return (String) "Wrong Expense ID" if expenseDao.checkExpense(expenseId) return null, some
   * information about the Expense otherwise, Pop Up  window with Error Alert if something went
   * wrong
   * @produce a description for the expense ID gavin
   */
  public static String checkExpense(int expenseId) {
    try {
      IdsUtil.validateIdPositive(expenseId);
      final Expense expense = expenseDao.checkExpense(expenseId);
      if (expense == null) {
        return "Wrong Expense ID";
      }
      return "Are you sure you want to remove Expense with this Information? Expense ID: "
          + expenseId + ", Creator: " + expense.getCreator() + ", Amount: "
          + expense.getExpenseAmount() + ", Date: " + expense.getExpenseDate() + ", Note: "
          + expense.getNote();
    } catch (SQLException e) {
      throw new RuntimeException(
          "Failed, no match to Expense ID: " + expenseId + ". " + e.getMessage(), e);
    }
  }

  /**
   * @param expenseId     (int) the ID we check the database with
   * @param typedPassword (String) password from the user, Pop Up will start to indicate if it is
   *                      work or not
   * @produce remove the Expense with the gavin ID from the db
   */
  public static void removeExpense(int expenseId, String typedPassword) throws SQLException {
    final IAdminDao adminDao = new AdminDao(DatabaseConnection.INSTANCE.getConnection());
    final String dbPassword = adminDao.getPasswordByUsername(AdminUsernameHolder.getAdminName());
    if (AdminService.verifyPassword(typedPassword, dbPassword)) {
      try {
        expenseDao.removeExpense(expenseId);
        PopupUtil.showPopup("Success", "Expense with Id has Removed.", AlertType.INFORMATION);
      } catch (Exception e) {
        throw new RuntimeException(
            "Failed, Couldn't remove Expense ID:" + expenseId + ". " + e.getMessage(), e);
      }
    } else {
      PopupUtil.showPopup("Failed", "Wrong Password.", AlertType.ERROR);
    }
  }

  /**
   * @param date (LocalDate) to send it to db to query with it on db
   * @return (ObservableList < Expense >) convert list to this type to be able to render it in the
   * app
   * @produce a list that can javaFX can render
   */
  public static ObservableList<Expense> makeTableExpense(LocalDate date) {
    try {
      return ListUtils.toObservableList(expenseDao.getExpenseByMonth(date));
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't convert Expense list. " + e.getMessage(), e);
    }
  }

  /**
   * @param date (LocalDate) to send it to db to query with it on db
   * @return (double)
   * @produce a total price of the expenses based on sent date
   */
  public static double getTotalTodayExpense(LocalDate date) throws SQLException {
    return expenseDao.getExpensePriceByDay(date);
  }

  // helper functions

  /**
   * @param expense (Expense) object
   * @return (boolean) false if everything goes right true otherwise, send Expense Object to the Dao
   * class to write the expense to db
   */
  private static boolean writeExpense(Expense expense) {
    try {
      return expenseDao.addExpense(expense);
    } catch (SQLException e) {
      throw new RuntimeException("Failed, Couldn't write Expense to db. " + e.getMessage(), e);
    }
  }
}
