package com.gamingCoffee.expense.repository;

import com.gamingCoffee.expense.model.Expense;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository {

  boolean addExpense(Expense expense) throws SQLException;

  Expense checkExpense(int expenseId) throws SQLException;

  boolean removeExpense(int expenseId) throws SQLException;

  List<Expense> getExpensesByDate(LocalDate date) throws SQLException;

  List<Expense> getExpensesByMonth(String date) throws SQLException;

  double getExpensePriceByDay(LocalDate date) throws SQLException;
}
