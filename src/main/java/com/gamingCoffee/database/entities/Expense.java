package com.gamingCoffee.database.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Expense {

  private final int expenseId;
  private final String creator;
  private final double expenseAmount;
  private final LocalDate expenseDate;
  private final String note;

  private Expense(Builder builder) {
    this.expenseId = builder.expenseId;
    this.creator = builder.creator;
    this.expenseAmount = builder.expenseAmount;
    this.expenseDate = builder.expenseDate;
    this.note = builder.note;
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

  @Override
  public String toString() {
    return "Expense{" + "expenseId=" + expenseId + ", creator='" + creator + "', expenseAmount="
        + expenseAmount + ", expenseDate=" + expenseDate + ", note='" + note + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Expense expense = (Expense) o;
    return getExpenseId() == expense.getExpenseId()
        && Double.compare(getExpenseAmount(), expense.getExpenseAmount()) == 0 && Objects.equals(
        getCreator(), expense.getCreator()) && Objects.equals(getExpenseDate(),
        expense.getExpenseDate()) && Objects.equals(getNote(), expense.getNote());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getExpenseId(), getCreator(), getExpenseAmount(), getExpenseDate(),
        getNote());
  }

  public static class Builder {

    private int expenseId = 0;
    private String creator = null;
    private double expenseAmount = 0.0;
    private LocalDate expenseDate = null;
    private String note = null;

    public Builder expenseId(int expenseId) {
      if (expenseId <= 0) {
        throw new IllegalArgumentException("Expense ID must be more than 0.");
      }
      this.expenseId = expenseId;
      return this;
    }

    public Builder creator(String creator) {
      if (creator == null || creator.isBlank()) {
        throw new IllegalArgumentException("creator cannot be null or blank.");
      }
      this.creator = creator;
      return this;
    }

    public Builder expenseAmount(double amount) {
      if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be Positive.");
      }
      this.expenseAmount = amount;
      return this;
    }

    public Builder expenseDate(LocalDate date) {
      if (date == null) {
        throw new IllegalArgumentException("expenseDate cannot be null.");
      }
      this.expenseDate = date;
      return this;
    }

    public Builder note(String note) {
      this.note = note;
      return this;
    }

    public Expense build() {
      return new Expense(this);
    }
  }
}
