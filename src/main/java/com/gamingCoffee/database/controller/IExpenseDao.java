package com.gamingCoffee.database.controller;

import com.gamingCoffee.database.entities.Expense;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface IExpenseDao {

  boolean addExpense(Expense expense) throws SQLException;

  Expense checkExpense(int expenseId) throws SQLException;

  boolean removeExpense(int expenseId) throws SQLException;

  List<Expense> getExpensesByDate(LocalDate date) throws SQLException;

  List<Expense> getExpenseByMonth(LocalDate date) throws SQLException;

  double getExpensePriceByDay(LocalDate date) throws SQLException;
}
