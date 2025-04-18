package com.gamingCoffee.uiController;

import com.gamingCoffee.session.model.Session;
import com.gamingCoffee.spot.model.Spot;
import com.gamingCoffee.spot.model.ConsoleType;
import com.gamingCoffee.spot.model.SpotType;
import com.gamingCoffee.session.service.SessionService;
import com.gamingCoffee.spot.service.SpotService;
import com.gamingCoffee.utiles.ChangeViewUtil;
import com.gamingCoffee.utiles.PopupUtil;
import com.gamingCoffee.utiles.TableViewUtils;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

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
    ChangeViewUtil.changeView(new Stage(), "Owner Page",
        "/fxml/ownerPage.fxml", true);
  }

  @FXML
  public void createSessionButtonAction() {
    ChangeViewUtil.changeView(new Stage(), "Create Session",
        "/fxml/createSession.fxml", true);
  }

  @FXML
  void endSessionButtonAction() {
    ChangeViewUtil.changeView(new Stage(), "End Session",
        "/fxml/endSession.fxml", true);
  }

  @FXML
  void expensesMenuButtonAction() {
    ChangeViewUtil.changeView(new Stage(), "Expenses Page",
        "/fxml/addExpense.fxml", true);
  }

  @FXML
  void resultTodayButtonAction() {
    ChangeViewUtil.changeView(new Stage(), "Result Page",
        "/fxml/resultsToday.fxml", true);
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

    try {
      spotsTableView.setItems(SpotService.getSpots());
    } catch (SQLException e) {
      PopupUtil.showErrorPopup(e);
    }
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
