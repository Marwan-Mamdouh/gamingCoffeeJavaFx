package com.gamingCoffee.services;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.IAdminDao;
import com.gamingCoffee.database.controller.IExpenseDao;
import com.gamingCoffee.database.entities.Expense;
import com.gamingCoffee.database.entities.Expense.Builder;
import com.gamingCoffee.utiles.AdminUsernameHolder;
import com.gamingCoffee.utiles.PopupUtil;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class ExpenseService {

  private static final IExpenseDao expenseDao = new ExpenseDao(
      DatabaseConnection.INSTANCE.getConnection());

  /**
   * @param amount (double) the cost of the expense
   * @param note   (String) the description of the expense
   * @return (boolean) false if the every thing work normal true otherwise
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
   */
  public static String checkExpense(int expenseId) {
    try {
      Expense expense = expenseDao.checkExpense(expenseId);
      if (expense == null) {
        return "Wrong Expense ID";
      }
      return "Are you sure you want to remove Expense with this Information? Expense ID: "
          + expenseId + ", Creator: " + expense.getCreator() + ", Amount: "
          + expense.getExpenseAmount() + ", Date: " + expense.getExpenseDate() + ", Note: "
          + expense.getNote();
    } catch (SQLException e) {
      PopupUtil.showErrorPopup(e);
      return "";
    }
  }

  /**
   * @param expenseId     (int) the ID we check the database with
   * @param typedPassword (String) password from the user, Pop Up will start to indicate if it is
   *                      work or not
   */
  public static void removeExpense(int expenseId, String typedPassword) throws SQLException {
    IAdminDao adminDao = new AdminDao(DatabaseConnection.INSTANCE.getConnection());
    String dbPassword = adminDao.getPasswordByUsername(AdminUsernameHolder.getAdminName());
    if (AdminService.verifyPassword(typedPassword, dbPassword)) {
      try {
        expenseDao.removeExpense(expenseId);
        PopupUtil.showPopup("Success", "Expense with Id has Removed", AlertType.INFORMATION);
      } catch (Exception e) {
        PopupUtil.showErrorPopup(e);
      }
    } else {
      PopupUtil.showPopup("Failed", "Wrong Password", AlertType.ERROR);
    }
  }

  /**
   * @param date (LocalDate) to send it to db to query with it on db
   * @return (ObservableList < Expense >) convert list to this type to be able to render it in the
   * app
   */
  public static ObservableList<Expense> makeTableExpense(LocalDate date) {
    return convertExpenseList(getExpenseByMonth(date));
  }

  /**
   * @param date (LocalDate) to send it to db to query with it on db
   * @return (double) produce the total price of the expenses based on sent date
   */
  public static double getTotalTodayExpense(LocalDate date) throws SQLException {
    return expenseDao.getExpensePriceByDay(date);
  }

  // helper functions

  /**
   * @param date (LocalDate) date to send it to the db
   * @return (List < Expense >) list of Expense based on date we send it to db
   */
  private static List<Expense> getExpenseByMonth(LocalDate date) {
    try {
      return expenseDao.getExpenseByMonth(date);
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
    return null;
  }

  /**
   * @param expenses (List<Expense>) list of Expense object
   * @return (ObservableList < Expense >) return this type of list so the java fx can render it to
   * the screen wrong
   */
  private static ObservableList<Expense> convertExpenseList(List<Expense> expenses) {
    try {
      return FXCollections.observableArrayList(expenses);
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return null;
    }
  }

  /**
   * @param expense (Expense) object
   * @return (boolean) false if everything goes right true otherwise, send Expense Object to the Dao
   * class to write the expense to db
   */
  private static boolean writeExpense(Expense expense) {
    try {
      return expenseDao.addExpense(expense);
    } catch (SQLException e) {
      PopupUtil.showErrorPopup(e);
      return true;
    }
  }
}
