package com.gamingCoffee.services;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.IAdminDao;
import com.gamingCoffee.database.controller.IExpenseDao;
import com.gamingCoffee.database.entities.Expense;
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

  public static boolean addExpense(double amount, String note) {
    return writeExpense(
        new Expense.Builder().creator(AdminUsernameHolder.getAdminName()).expenseAmount(amount)
            .note(note).build());
  }

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

  public static ObservableList<Expense> makeTableExpense(LocalDate date) {
    return convertExpenseList(getExpenseByMonth(date));
  }

  // helper functions
  private static List<Expense> getExpenseByMonth(LocalDate date) {
    try {
      return expenseDao.getExpenseByMonth(date);
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
    return null;
  }

  private static ObservableList<Expense> convertExpenseList(List<Expense> expenses) {
    try {
      return FXCollections.observableArrayList(expenses);
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return null;
    }
  }

  private static boolean writeExpense(Expense expense) {
    try {
      return expenseDao.addExpense(expense);
    } catch (SQLException e) {
      PopupUtil.showErrorPopup(e);
      return true;
    }
  }
}
