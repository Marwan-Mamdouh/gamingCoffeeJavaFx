package com.gamingCoffee.database.entities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Result(int day, double income, int sessionCount, double expenses, int expensesCount,
                     double finalResult) {

  public Result {
    if (day <= 0) {
      throw new IllegalArgumentException("can not create a day with: " + day);
    }
    if (income < 0) {
      throw new IllegalArgumentException("can not create an income with: " + income);
    }
    if (sessionCount < 0) {
      throw new IllegalArgumentException("can not create sessions count with: " + sessionCount);
    }
    if (expenses < 0) {
      throw new IllegalArgumentException("can not create expenses with: " + expenses);
    }
    if (expensesCount < 0) {
      throw new IllegalArgumentException("can not create a expenses Count with: " + expensesCount);
    }
    if (finalResult <= 0) {
      throw new IllegalArgumentException("can not create a final Result with: " + finalResult);
    }
  }

  @Contract("_, _, _, _, _, _ -> new")
  public static @NotNull Result create(int day, double income, int sessionCount, double expenses,
      int expensesCount, double finalResult) {
    return new Result(day, income, sessionCount, expenses, expensesCount, finalResult);
  }

  public int getDay() {
    return day;
  }

  public double getIncome() {
    return income;
  }

  public double getSessionCount() {
    return sessionCount;
  }

  public double getExpenses() {
    return expenses;
  }

  public double getExpensesCount() {
    return expensesCount;
  }

  public double getFinalResult() {
    return finalResult;
  }
}