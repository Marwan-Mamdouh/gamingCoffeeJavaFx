package com.gamingCoffee.controllers;

import com.gamingCoffee.database.entities.Session;
import com.gamingCoffee.services.SessionService;
import com.gamingCoffee.utiles.PopupUtil;
import com.gamingCoffee.utiles.TableViewUtils;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ResultController {

//  private final ISessionDao sessionDao = new SessionDao(
//      DatabaseConnection.INSTANCE.getConnection());

  // Label
  @FXML
  private Label sessionCountHolder;
  @FXML
  private Label sessionPriceHolder;

  // Buttons
  @FXML
  private Button closeButton;

  // DatePicker
  @FXML
  private DatePicker datePickerFiled;

  // session Table
  @FXML
  private TableColumn<Session, Integer> SessionIdColumn;
  @FXML
  private TableColumn<Session, Double> durationColumn;
  @FXML
  private TableColumn<Session, Double> costColumn;
  @FXML
  private TableView<Session> resultTable;

  @FXML
  void closeAction() {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  void getDataAction() throws SQLException {
    LocalDate date = datePickerFiled.getValue();
    makeResultTable(date);
    fillLabels(SessionService.getSessionsCountAndPrice(date));
  }

  private void makeResultTable(LocalDate date) {
    SessionIdColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
    durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
    costColumn.setCellValueFactory(new PropertyValueFactory<>("sessionPrice"));

    try {
      resultTable.setItems(SessionService.getSessionsListByDate(date));
      TableViewUtils.makeTableCopyable(resultTable);
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  private void fillLabels(double[] values) {
    fillCountLabel(values[0]);
    fillPriceLabel(values[1]);
  }

  private void fillCountLabel(double count) {
    sessionCountHolder.setText(String.valueOf(count));
  }

  private void fillPriceLabel(double price) {
    sessionPriceHolder.setText(String.valueOf(price));
  }
}
