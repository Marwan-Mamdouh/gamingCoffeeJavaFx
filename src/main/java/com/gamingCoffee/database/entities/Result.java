package com.gamingCoffee.database.entities;

import java.util.Objects;

public class Result {

  private final int day;
  private final double income;
  private final double sessionCount;
  private final double expenses;
  private final double expensesCount;
  private final double finalResult;

  private Result(Builder builder) {
    this.day = builder.day;
    this.income = builder.income;
    this.sessionCount = builder.sessionCount;
    this.expenses = builder.expenses;
    this.expensesCount = builder.expensesCount;
    this.finalResult = builder.finalResult;
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

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Result result = (Result) o;
    return getDay() == result.getDay() && getIncome() == result.getIncome()
        && getSessionCount() == result.getSessionCount() && getExpenses() == result.getExpenses()
        && getExpensesCount() == result.getExpensesCount()
        && getFinalResult() == result.getFinalResult();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDay(), getIncome(), getSessionCount(), getExpenses(), getExpensesCount(),
        getFinalResult());
  }

  @Override
  public String toString() {
    return "Result{day=" + day + ", income=" + income + ", sessionCount=" + sessionCount
        + ", expenses=" + expenses + ", expensesCount=" + expensesCount + ", finalResult="
        + finalResult + '}';
  }

  public static class Builder {

    private int day = 250101;
    private double income = 0;
    private double sessionCount = 0;
    private double expenses = 0;
    private double expensesCount = 0;
    private double finalResult = 0;


    public Builder day(int day) {
      if (day <= 0) {
        throw new IllegalArgumentException("can not create a day with: " + day);
      }
      this.day = day;
      return this;
    }

    public Builder income(int income) {
      if (income < 0) {
        throw new IllegalArgumentException("can not create an income with: " + income);
      }
      this.income = income;
      return this;
    }

    public Builder sessionCount(int sessionCount) {
      if (sessionCount < 0) {
        throw new IllegalArgumentException("can not create sessions count with: " + sessionCount);
      }
      this.sessionCount = sessionCount;
      return this;
    }

    public Builder expenses(int expenses) {
      if (expenses < 0) {
        throw new IllegalArgumentException("can not create expenses with: " + expenses);
      }
      this.expenses = expenses;
      return this;
    }

    public Builder expensesCount(int expensesCount) {
      if (expensesCount < 0) {
        throw new IllegalArgumentException(
            "can not create a expenses Count with: " + expensesCount);
      }
      this.expensesCount = expensesCount;
      return this;
    }

    public Builder finalResult(int finalResult) {
      if (finalResult <= 0) {
        throw new IllegalArgumentException("can not create a final Result with: " + finalResult);
      }
      this.finalResult = finalResult;
      return this;
    }

    public Result build() {
      return new Result(this);
    }
  }
}
