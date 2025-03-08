package com.gamingCoffee.database.entities;

import java.time.LocalDate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Expense(int expenseId, String creator, double expenseAmount, LocalDate expenseDate,
                      String note) {

  public Expense {
    validate(expenseId, creator, expenseAmount, expenseDate);
  }

  @Contract("_, _, _ -> new")
  public static @NotNull Expense create(String creator, double expenseAmount, String note) {
    return new Expense(1, creator, expenseAmount, LocalDate.now(), note);
  }

  @Contract("_, _, _, _, _ -> new")
  public static @NotNull Expense read(int expenseId, String creator, double expenseAmount,
      LocalDate date, String note) {
    return new Expense(expenseId, creator, expenseAmount, date, note);
  }

  public int getExpenseId() {
    return expenseId;
  }

  public String getCreator() {
    return creator;
  }

  public double getExpenseAmount() {
    return expenseAmount;
  }

  public LocalDate getExpenseDate() {
    return expenseDate;
  }

  public String getNote() {
    return note;
  }

  private static void validate(int expenseId, String creator, double expenseAmount,
      LocalDate date) {
    if (expenseId <= 0) {
      throw new IllegalArgumentException("Expense ID must be more than 0.");
    }
    if (creator == null || creator.isBlank()) {
      throw new IllegalArgumentException("Creator cannot be null or blank.");
    }
    if (expenseAmount <= 0) {
      throw new IllegalArgumentException("Amount must be positive.");
    }
    if (date == null) {
      throw new IllegalArgumentException("Expense date cannot be null.");
    }
  }
}