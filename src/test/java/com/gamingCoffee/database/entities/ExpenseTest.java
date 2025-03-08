package com.gamingCoffee.database.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpenseTest {

  @Test
  void shouldCreateExpenseSuccessfully() {
    Expense expense = new Expense(1, "Admin", 100.0, LocalDate.of(2024, 3, 8), "Office supplies");

    assertThat(expense).isNotNull();
    assertThat(expense.getExpenseId()).isEqualTo(1);
    assertThat(expense.getCreator()).isEqualTo("Admin");
    assertThat(expense.getExpenseAmount()).isEqualTo(100.0);
    assertThat(expense.getExpenseDate()).isEqualTo(LocalDate.of(2024, 3, 8));
    assertThat(expense.getNote()).isEqualTo("Office supplies");
  }

  @Test
  void shouldThrowExceptionForInvalidExpenseId() {
    assertThatThrownBy(() -> new Expense(0, "Admin", 100.0, LocalDate.now(), "Test")).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Expense ID must be more than 0.");
  }

  @Test
  void shouldThrowExceptionForInvalidCreator() {
    assertThatThrownBy(() -> new Expense(1, "", 100.0, LocalDate.now(), "Test")).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Creator cannot be null or blank.");

    assertThatThrownBy(() -> new Expense(1, null, 100.0, LocalDate.now(), "Test")).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Creator cannot be null or blank.");
  }

  @Test
  void shouldThrowExceptionForInvalidExpenseAmount() {
    assertThatThrownBy(() -> new Expense(1, "Admin", -10.0, LocalDate.now(), "Test")).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Amount must be positive.");
  }

  @Test
  void shouldThrowExceptionForNullExpenseDate() {
    assertThatThrownBy(() -> new Expense(1, "Admin", 100.0, null, "Test")).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Expense date cannot be null.");
  }

  @Test
  void shouldCreateExpenseUsingCreateMethod() {
    Expense expense = Expense.create("Admin", 50.0, "Lunch");

    assertThat(expense).isNotNull();
    assertThat(expense.getExpenseId()).isEqualTo(1);
    assertThat(expense.getCreator()).isEqualTo("Admin");
    assertThat(expense.getExpenseAmount()).isEqualTo(50.0);
    assertThat(expense.getExpenseDate()).isEqualTo(LocalDate.now());
    assertThat(expense.getNote()).isEqualTo("Lunch");
  }

  @Test
  void shouldCreateExpenseUsingReadMethod() {
    LocalDate date = LocalDate.of(2024, 3, 8);
    Expense expense = Expense.read(2, "Admin", 200.0, date, "Rent");

    assertThat(expense).isNotNull();
    assertThat(expense.getExpenseId()).isEqualTo(2);
    assertThat(expense.getCreator()).isEqualTo("Admin");
    assertThat(expense.getExpenseAmount()).isEqualTo(200.0);
    assertThat(expense.getExpenseDate()).isEqualTo(date);
    assertThat(expense.getNote()).isEqualTo("Rent");
  }

  @Test
  void shouldCheckEqualityOfExpenses() {
    LocalDate date = LocalDate.of(2024, 3, 8);
    Expense expense1 = new Expense(3, "Admin", 300.0, date, "Bonus");
    Expense expense2 = new Expense(3, "Admin", 300.0, date, "Bonus");

    assertThat(expense1).isEqualTo(expense2);
    assertThat(expense1.hashCode()).isEqualTo(expense2.hashCode());
  }

  @Test
  void shouldCheckExpensesAreNotEqualWhenDifferent() {
    Expense expense1 = new Expense(4, "Admin", 400.0, LocalDate.of(2024, 3, 8), "Salaries");
    Expense expense2 = new Expense(5, "User", 500.0, LocalDate.of(2024, 3, 9), "Electricity");

    assertThat(expense1).isNotEqualTo(expense2);
  }
}
