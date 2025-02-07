package com.gamingCoffee.controllers;

import com.gamingCoffee.database.entities.Session;
import com.gamingCoffee.database.entities.Spot;
import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.SpotType;
import com.gamingCoffee.services.SessionService;
import com.gamingCoffee.services.SpotService;
import com.gamingCoffee.utiles.ChangeViewUtil;
import com.gamingCoffee.utiles.PopupUtil;
import com.gamingCoffee.utiles.TableViewUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HomeController implements Initializable {

  // session table
  @FXML
  private TableColumn<Session, Integer> sessionIdColumn;
  @FXML
  private TableColumn<Session, Integer> spotIdColumn;
  @FXML
  private TableColumn<Session, String> creatorColumn;
  @FXML
  private TableColumn<Session, Integer> noControllerColumn;
  @FXML
  private TableColumn<Session, String> startTimeColumn;
  @FXML
  private TableColumn<Session, String> endTimeColumn;
  @FXML
  private TableColumn<Session, Integer> durationColumn;
  @FXML
  private TableView<Session> sessionsTableView;

  // spots table
  @FXML
  private TableColumn<Spot, Integer> spotNumberColumn;
  @FXML
  private TableColumn<Spot, ConsoleType> consoleTypeColumn;
  @FXML
  private TableColumn<Spot, SpotType> spotPrivacyColumn;
  @FXML
  private TableColumn<Spot, String> screenTypeColumn;
  @FXML
  private TableColumn<Spot, Integer> screenSizeColumn;
  @FXML
  private TableView<Spot> spotsTableView;

  @FXML
  private Button onwerMenuButton;
  @FXML
  private Button expensesMenuButton;
  @FXML
  public Button logOutButton;
  @FXML
  private Button exitButton;

  @FXML
  public void refreshSessionsButtonAction() {
    try {
      makeSpotsTable();
      makeSessionsTable();
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  @FXML
  public void refreshSpotsButtonAction() {
    try {
      makeSpotsTable();
      makeSessionsTable();
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }

  @FXML
  void ownerMenuButtonAction() {
    ChangeViewUtil.changeView(new Stage(), "Owner Page", "/fxml/remakeOwnerPage.fxml", true);
  }

  @FXML
  public void createSessionButtonAction() {
    ChangeViewUtil.changeView(new Stage(), "Create Session", "/fxml/createSession.fxml", true);
  }

  @FXML
  void endSessionButtonAction() {
    ChangeViewUtil.changeView(new Stage(), "End Session", "/fxml/endSession.fxml", true);
  }

  @FXML
  void expensesMenuButtonAction() {
    PopupUtil.showPopup("Expenses Page", "this page is under development for now",
        AlertType.INFORMATION);
    // todo complete this function
//    ChangeViewUtil.changeView(new Stage(), "Expenses Page", "/fxml/Expenses.fxml", true);
  }

  @FXML
  void resultTodayButtonAction() {
    ChangeViewUtil.changeView(new Stage(), "Result Page", "/fxml/resultsToday.fxml", true);
  }

  @FXML
  public void logOutButtonAction() {
    ChangeViewUtil.startLoginPage((Stage) logOutButton.getScene().getWindow());
  }

  @FXML
  void exitButtonAction() {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }

  private void makeSessionsTable() {
    // Set cell value factories
    sessionIdColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
    spotIdColumn.setCellValueFactory(new PropertyValueFactory<>("spotId"));
    creatorColumn.setCellValueFactory(new PropertyValueFactory<>("creator"));
    noControllerColumn.setCellValueFactory(new PropertyValueFactory<>("noControllers"));
    startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
    endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

    try {
      // Set the items in the TableView
      sessionsTableView.setItems(SessionService.getSessionRunningList());

    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
  }


  private void makeSpotsTable() {
    spotNumberColumn.setCellValueFactory(new PropertyValueFactory<>("spotId"));
    consoleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("consoleType"));
    spotPrivacyColumn.setCellValueFactory(new PropertyValueFactory<>("spotType"));
    screenTypeColumn.setCellValueFactory(new PropertyValueFactory<>("displayType"));
    screenSizeColumn.setCellValueFactory(new PropertyValueFactory<>("displaySize"));

    spotsTableView.setItems(SpotService.getFreeSpots());
    spotsTableView.getSelectionModel().setCellSelectionEnabled(true);
  }

  public void showOwnerButton() {
    onwerMenuButton.setVisible(true);
  }

  /**
   * @param location  The location used to resolve relative paths for the root object, or
   *                  {@code null} if the location is not known.
   * @param resources The resources used to localize the root object, or {@code null} if the root
   *                  object was not localized.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    makeSpotsTable();
    makeSessionsTable();
    TableViewUtils.makeTableCopyable(spotsTableView);
    TableViewUtils.makeTableCopyable(sessionsTableView);
  }
}
