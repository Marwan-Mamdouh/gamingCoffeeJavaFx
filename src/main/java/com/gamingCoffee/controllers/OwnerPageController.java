package com.gamingCoffee.controllers;

import com.gamingCoffee.database.entities.Admin;
import com.gamingCoffee.database.entities.Controller;
import com.gamingCoffee.database.entities.Expense;
import com.gamingCoffee.database.entities.Spot;
import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.ControllerType;
import com.gamingCoffee.models.Position;
import com.gamingCoffee.models.SpotState;
import com.gamingCoffee.models.SpotType;
import com.gamingCoffee.services.AdminService;
import com.gamingCoffee.services.ControllerService;
import com.gamingCoffee.services.ExpenseService;
import com.gamingCoffee.services.SpotService;
import com.gamingCoffee.utiles.PageDataUtil;
import com.gamingCoffee.utiles.PopupUtil;
import com.gamingCoffee.utiles.TableViewUtils;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class OwnerPageController implements Initializable {


  // Buttons
  // Admin
  @FXML
  private Button deleteUserButton;
  @FXML
  private Button changePasswordButton;

  // Spot
  @FXML
  private Button removeSpotButton;

  // Controller
  @FXML
  private Button removeControllerButton;
  @FXML
  private Button removeExpenseButton;

  // Label
  // Admin
  @FXML
  private Label removeUserDataLabel;
  @FXML
  private Label changeUserPasswordLabel;

  // Spot
  @FXML
  private Label spotDataLabel;

  // Controller
  @FXML
  private Label controllerDataLabel;

  // Expenses
  @FXML
  private Label expensesDataLabel;

  // Tables
  // Admins table
  @FXML
  private TableView<Admin> adminsTable;
  @FXML
  private TableColumn<Admin, String> usernameColumn;
  @FXML
  private TableColumn<Admin, Integer> ageColumn;
  @FXML
  private TableColumn<Admin, Date> hireDateColumn;
  @FXML
  private TableColumn<Admin, Integer> salaryColumn;
  @FXML
  private TableColumn<Admin, String> phoneNumberColumn;

  // Spot Table
  @FXML
  private TableView<Spot> spotTable;
  @FXML
  private TableColumn<Spot, Integer> spotIdColumn;
  @FXML
  private TableColumn<Spot, SpotType> privacyColumn;
  @FXML
  private TableColumn<Spot, SpotState> spotStateColumn;
  @FXML
  private TableColumn<Spot, Integer> consoleIdColumn;
  @FXML
  private TableColumn<Spot, ConsoleType> consoleTypeColumn;
  @FXML
  private TableColumn<Spot, Integer> screenIdColumn;
  @FXML
  private TableColumn<Spot, Integer> screenSizeColumn;
  @FXML
  private TableColumn<Spot, String> screenTypeColumn;

  // controllers Table
  @FXML
  private TableView<Controller> controllersTable;
  @FXML
  private TableColumn<Controller, Integer> controllerIdColumn;
  @FXML
  private TableColumn<Controller, ControllerType> controllerTypeColumn;

  // Expense Table
  @FXML
  private TableView<Expense> expensesTable;
  @FXML
  private TableColumn<Expense, Integer> expensesIdColumn;
  @FXML
  private TableColumn<Expense, String> expensesCreatorColumn;
  @FXML
  private TableColumn<Expense, Integer> expensesAmountColumn;
  @FXML
  private TableColumn<Expense, LocalDate> expensesDateColumn;
  @FXML
  private TableColumn<Expense, String> expensesNoteColumn;


  // Data Picker
  @FXML
  private DatePicker birthDayField;

  @FXML
  private DatePicker expensesDatePicker;

  // choice Boxes
  // Admin
  @FXML
  private ChoiceBox<Position> choiceBoxPosition;

  // Spot
  @FXML
  private ChoiceBox<ConsoleType> consoleTypeChoiceBox;
  @FXML
  private ChoiceBox<SpotType> spotPrivacyChoiceBox;

  //Controller
  @FXML
  private ChoiceBox<ControllerType> controllerTypeChoiceBox;

  // check Boxes
  // Admin
  @FXML
  private CheckBox showPasswordCheckBoxAddUser;
  @FXML
  private CheckBox showPasswordCheckBoxRemoveUser;
  @FXML
  private CheckBox showPasswordCheckBoxChangePassword;
  @FXML
  private CheckBox showPasswordCheckBoxNewPassword;

  // Spot
  @FXML
  private CheckBox showPasswordCheckBoxRemoveSpot;

  // Controller
  @FXML
  private CheckBox checkBoxShowPassRemoveController;
  @FXML
  private CheckBox checkBoxPasswordExpenses;


  // Fields
  // admin
  @FXML
  private TextField usernameField;
  @FXML
  private TextField passwordTextAddUser;
  @FXML
  private TextField passwordTextRemoveUser;
  @FXML
  private TextField ageField;
  @FXML
  private TextField phoneNumberField;
  @FXML
  private TextField removeUsernameField;
  @FXML
  private TextField salaryField;
  @FXML
  private PasswordField passwordAddUser;
  @FXML
  private PasswordField passwordRemoveUser;
  @FXML
  private TextField usernameChangePassword;
  @FXML
  private TextField passwordTextChangePassword;
  @FXML
  private PasswordField passwordChangePassword;
  @FXML
  private TextField passwordTextNewPassword;
  @FXML
  private PasswordField passwordNewPassword;

  // Spot
  @FXML
  private TextField consoleIdAddSpotField;
  @FXML
  private TextField displayIdField;
  @FXML
  private TextField displayTypeField;
  @FXML
  private TextField displaySizeField;
  @FXML
  private TextField removeSpotNumber;
  @FXML
  private TextField passwordTextRemoveSpot;
  @FXML
  private PasswordField passwordRemoveSpot;

  // Controller
  @FXML
  private TextField addControllerIdField;
  @FXML
  private TextField removeControllerIdField;
  @FXML
  private TextField passwordTextRemoveController;
  @FXML
  private PasswordField passwordRemoveController;

  // Expense
  @FXML
  private TextField expensesIdField;
  @FXML
  private TextField passwordTextExpenses;
  @FXML
  private PasswordField passwordExpenses;

  // Actions
  // Admin
  @FXML
  void addAdminButtonAction() {
    try {
      AdminService.addAdmin(usernameField.getText(),
          PageDataUtil.getPasswordFromFields(showPasswordCheckBoxAddUser, passwordAddUser,
              passwordTextAddUser), choiceBoxPosition.getValue(),
          Integer.parseInt(ageField.getText()), phoneNumberField.getText(),
          Integer.parseInt(salaryField.getText()));
      clearAddUserFields();
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  @FXML
  void checkUserRemoveAdminButtonAction() {
    removeUserDataLabel.setText(AdminService.checkToRemoveAdmin(removeUsernameField.getText()));
    deleteUserButton.setVisible(true);
  }

  @FXML
  void deleteUserButtonAction() {
    if (AdminService.deleteAdmin(removeUsernameField.getText(),
        PageDataUtil.getPasswordFromFields(showPasswordCheckBoxRemoveUser, passwordRemoveUser,
            passwordTextRemoveUser))) {
      deleteUserButton.setVisible(false);
      removeUserDataLabel.setText("");
    }
  }

  @FXML
  void refreshAdminsTableButtonAction() {
    makeAdminTable();
  }

  @FXML
  void showPasswordCheckBoxAddUserAction() {
    PageDataUtil.showPasswordCheckBox(showPasswordCheckBoxAddUser, passwordTextAddUser,
        passwordAddUser);
  }

  @FXML
  void showPasswordCheckBoxRemoveUserAction() {
    PageDataUtil.showPasswordCheckBox(showPasswordCheckBoxRemoveUser, passwordTextRemoveUser,
        passwordRemoveUser);
//    }
  }

  private void makeAdminTable() {
    // Set cell value factories
    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
    hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hiringDate"));
    salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
    phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

    // Set the items in the TableView
    try {
      adminsTable.setItems(AdminService.convertAdminsList());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void clearAddUserFields() {
    usernameField.setText("");
    passwordAddUser.setText("");
    passwordTextAddUser.setText("");
    ageField.setText("");
    phoneNumberField.setText("");
    salaryField.setText("");
  }

  @FXML
  void showPasswordCheckBoxChangePasswordAction() {
    PageDataUtil.showPasswordCheckBox(showPasswordCheckBoxChangePassword,
        passwordTextChangePassword, passwordChangePassword);
  }

  @FXML
  void showPasswordCheckBoxNewPasswordAction() {
    PageDataUtil.showPasswordCheckBox(showPasswordCheckBoxNewPassword, passwordTextNewPassword,
        passwordNewPassword);
  }

  @FXML
  void checkUserChangePasswordButtonAction() {
    try {
      changeUserPasswordLabel.setText(
          AdminService.checkToChangePassword(usernameChangePassword.getText()));
      changePasswordButton.setVisible(true);
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  @FXML
  void changePasswordButtonAction() {
    String newPassword = PageDataUtil.getPasswordFromFields(showPasswordCheckBoxNewPassword,
        passwordNewPassword, passwordTextNewPassword);
    String oldPassword = PageDataUtil.getPasswordFromFields(showPasswordCheckBoxChangePassword,
        passwordChangePassword, passwordTextChangePassword);

    if (oldPassword.isBlank() || newPassword.isBlank()) {
      PopupUtil.showPopup("Failed", "Can not set empty Password", AlertType.ERROR);

    } else if (AdminService.changePassword(usernameChangePassword.getText(), oldPassword,
        newPassword)) {
      PopupUtil.showPopup("Success", "The new Password has set.", AlertType.INFORMATION);
    }
  }

  // Spots
  @FXML
  void refreshSpotsTableButtonAction() {
    makeSpotTable();
  }

  @FXML
  void addSpotButtonAction() {
    SpotService.addSpot(Integer.parseInt(consoleIdAddSpotField.getText()),
        consoleTypeChoiceBox.getValue(), spotPrivacyChoiceBox.getValue(),
        Integer.parseInt(displayIdField.getText()), displayTypeField.getText(),
        Integer.parseInt(displaySizeField.getText()));
  }

  @FXML
  void checkSpotButtonAction() {
    try {
      spotDataLabel.setText(
          SpotService.checkSpotById(Integer.parseInt(removeSpotNumber.getText())));
      removeSpotButton.setVisible(true);
    } catch (NumberFormatException e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  @FXML
  void showPasswordCheckBoxRemoveSpotAction() {
    PageDataUtil.showPasswordCheckBox(showPasswordCheckBoxRemoveSpot, passwordTextRemoveSpot,
        passwordRemoveSpot);
  }

  @FXML
  void removeSpotButtonAction() {
    try {
      SpotService.removeSpotById(Integer.parseInt(removeSpotNumber.getText()));
    } catch (NumberFormatException e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  private void makeSpotTable() {
    // Set cell value factories
    spotIdColumn.setCellValueFactory(new PropertyValueFactory<>("spotId"));
    privacyColumn.setCellValueFactory(new PropertyValueFactory<>("spotType"));
    spotStateColumn.setCellValueFactory(new PropertyValueFactory<>("spotState"));
    consoleIdColumn.setCellValueFactory(new PropertyValueFactory<>("consoleId"));
    consoleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("consoleType"));
    screenIdColumn.setCellValueFactory(new PropertyValueFactory<>("displayId"));
    screenSizeColumn.setCellValueFactory(new PropertyValueFactory<>("displaySize"));
    screenTypeColumn.setCellValueFactory(new PropertyValueFactory<>("displayType"));

    // Set the items in the TableView
    try {
      spotTable.setItems(SpotService.getAllSpots());
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  // Controller
  @FXML
  void addControllerButtonAction() {
    try {
      int controllerId = Integer.parseInt(addControllerIdField.getText());
      if (ControllerService.addController(controllerId, controllerTypeChoiceBox.getValue())) {
        PopupUtil.showPopup("Success",
            "Controller with ID" + controllerId + " just added successfully!",
            AlertType.INFORMATION);
      } else {
        PopupUtil.showPopup("Error", "Something went wrong", AlertType.INFORMATION);
      }
    } catch (NumberFormatException e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  @FXML
  void checkControllerButtonAction() {
    controllerDataLabel.setText(
        ControllerService.checkController(Integer.parseInt(removeControllerIdField.getText())));
    removeControllerButton.setVisible(true);
  }

  @FXML
  void checkBoxShowPassRemoveControllerAction() {
    PageDataUtil.showPasswordCheckBox(checkBoxShowPassRemoveController,
        passwordTextRemoveController, passwordRemoveController);
  }

  @FXML
  void removeControllerButtonAction() {
    try {
      int controllerId = Integer.parseInt(removeControllerIdField.getText());
      if (ControllerService.removeController(controllerId,
          PageDataUtil.getPasswordFromFields(checkBoxShowPassRemoveController,
              passwordRemoveController, passwordTextRemoveController))) {
        PopupUtil.showPopup("Success",
            "Controller with ID" + controllerId + " just Deleted successfully !",
            AlertType.INFORMATION);
      }
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  @FXML
  void refreshControllerTableButtonAction() {
    makeControllerTable();
  }

  private void makeControllerTable() {
    controllerIdColumn.setCellValueFactory(new PropertyValueFactory<>("controllerId"));
    controllerTypeColumn.setCellValueFactory(new PropertyValueFactory<>("controllerType"));

    // Set the items in the TableView
    try {
      controllersTable.setItems(ControllerService.convertControllerList());
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  // Expenses
  @FXML
  void checkExpenseButtonAction() {
    try {
      expensesDataLabel.setText(
          ExpenseService.checkExpense(Integer.parseInt(expensesIdField.getText())));
      removeExpenseButton.setVisible(true);
    } catch (NumberFormatException e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  @FXML
  void checkBoxPasswordExpensesAction() {
    PageDataUtil.showPasswordCheckBox(checkBoxPasswordExpenses, passwordTextExpenses,
        passwordExpenses);
  }

  @FXML
  void removeExpenseButtonAction() {
    try {
      ExpenseService.removeExpense(Integer.parseInt(expensesIdField.getText()),
          PageDataUtil.getPasswordFromFields(checkBoxPasswordExpenses, passwordExpenses,
              passwordTextExpenses));
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  @FXML
  void getExpensesButtonAction() {
    if (expensesDatePicker.getValue() == null) {
      PopupUtil.showPopup("Failed", "Can not get Expense without date.", AlertType.ERROR);
      return;
    }
    makeExpenseTable(expensesDatePicker.getValue());
  }

  private void makeExpenseTable(LocalDate date) {
    expensesIdColumn.setCellValueFactory(new PropertyValueFactory<>("expenseId"));
    expensesCreatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator"));
    expensesAmountColumn.setCellValueFactory(new PropertyValueFactory<>("expenseAmount"));
    expensesDateColumn.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));
    expensesNoteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));

    // Set the items in the TableView
    try {
      expensesTable.setItems(ExpenseService.makeTableExpense(date));
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  /**
   * @param location  The location used to resolve relative paths for the root object, or
   *                  {@code null} if the location is not known.
   * @param resources The resources used to localize the root object, or {@code null} if the root
   *                  object was not localized.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    makeAdminTable();
    makeSpotTable();
    makeControllerTable();
    setChoiceBoxValues();
    TableViewUtils.makeTableCopyable(adminsTable);
    TableViewUtils.makeTableCopyable(spotTable);
    TableViewUtils.makeTableCopyable(controllersTable);
    TableViewUtils.makeTableCopyable(expensesTable);
  }

  private void setChoiceBoxValues() {
    choiceBoxPosition.getItems().addAll(Position.OWNER, Position.EMPLOYEE);

    consoleTypeChoiceBox.getItems().addAll(ConsoleType.PS4, ConsoleType.PS5);
    spotPrivacyChoiceBox.getItems().addAll(SpotType.PRIVATE, SpotType.PUBLIC);

    controllerTypeChoiceBox.getItems()
        .addAll(ControllerType.PS4CONTROLLER, ControllerType.PS5CONTROLLER);
  }
}