package com.gamingCoffee.controllers;

import com.gamingCoffee.services.ExpenseService;
import com.gamingCoffee.utiles.PopupUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddExpenseController {

  // Button
  @FXML
  private Button cancelButton;

  // textFields
  @FXML
  private TextField expenseAmountField;
  @FXML
  private TextField expenseNoteField;

  @FXML
  void addExpenseButtonAction() {
    String expenseAmount = expenseAmountField.getText();
    String expenseNote = expenseNoteField.getText();

    if (expenseNote.isBlank() || expenseAmount.isBlank()) {
      PopupUtil.showPopup("Failed", "Can not Add Expense with Blank fields", AlertType.ERROR);

    } else if (!ExpenseService.addExpense(Integer.parseInt(expenseAmount), expenseNote)) {
      PopupUtil.showPopup("Success", "Done Adding Expense", AlertType.INFORMATION);
      cancelAction();
    }
  }

  @FXML
  void cancelAction() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }
}