package com.gamingCoffee.database.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpenseTest {

  @Test
  void testBuilderWithAllFields() {
    LocalDate date = LocalDate.now();
    Expense expense = new Expense.Builder().expenseId(1).creator("Admin1").expenseAmount(100.50)
        .expenseDate(date).note("Office supplies").build();

    assertEquals(1, expense.getExpenseId());
    assertEquals("Admin1", expense.getCreator());
    assertEquals(100.50, expense.getExpenseAmount());
    assertEquals(date, expense.getExpenseDate());
    assertEquals("Office supplies", expense.getNote());
  }

  @Test
  void testBuilderWithOptionalFields() {
    // Build with only required fields (current implementation allows invalid values!)
    Expense expense = new Expense.Builder().expenseId(1) // Required
        .expenseAmount(50.0) // Required
        .build();

    assertEquals(1, expense.getExpenseId());
    assertEquals(50.0, expense.getExpenseAmount());
    assertNull(expense.getCreator()); // Optional field
    assertNull(expense.getExpenseDate()); // Optional field
    assertNull(expense.getNote()); // Optional field
  }

  @Test
  void testValidationInBuilder() {
    // Test invalid expenseId
    assertThrows(IllegalArgumentException.class, () -> new Expense.Builder().expenseId(0));

    // Test invalid expenseAmount
    assertThrows(IllegalArgumentException.class, () -> new Expense.Builder().expenseAmount(-10.0));
  }

  @Test
  void testEqualsAndHashCode() {
    LocalDate date = LocalDate.now();
    Expense expense1 = new Expense.Builder().expenseId(1).creator("Admin1").expenseAmount(100.0)
        .expenseDate(date).note("Supplies").build();

    Expense expense2 = new Expense.Builder().expenseId(1).creator("Admin1").expenseAmount(100.0)
        .expenseDate(date).note("Supplies").build();

    Expense expense3 = new Expense.Builder().expenseId(2).expenseAmount(200.0).build();

    assertEquals(expense1, expense2);
    assertNotEquals(expense1, expense3);
    assertEquals(expense1.hashCode(), expense2.hashCode());
  }

  @Test
  void testToString() {
    LocalDate date = LocalDate.now();
    Expense expense = new Expense.Builder().expenseId(1).creator("Admin1").expenseAmount(100.0)
        .expenseDate(date).note("Supplies").build();

    String expected =
        "Expense{expenseId=1, creator='Admin1', expenseAmount=100.0, expenseDate=" + date
            + ", note='Supplies'}";
    assertEquals(expected, expense.toString());
  }
}