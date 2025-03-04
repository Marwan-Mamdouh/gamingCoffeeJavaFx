package com.gamingCoffee.services;

import com.gamingCoffee.database.connection.DatabaseConnection;
import com.gamingCoffee.database.controller.IResultDao;
import com.gamingCoffee.database.entities.Result;
import com.gamingCoffee.utiles.ListUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.collections.ObservableList;

public class ResultService {

  private final static IResultDao resultDao = new ResultDao(
      DatabaseConnection.INSTANCE.getConnection());

  public static ObservableList<Result> getResultDay(LocalDate date) {
    return ListUtils.toObservableList(getResultHelper(date));
  }

  public static ObservableList<Result> getResultByMonth(LocalDate date) {
    return ListUtils.toObservableList(resultDao.getResultByMonth(date.getMonth()));
  }

  private static List<Result> getResultHelper(LocalDate date) {
    return List.of(resultDao.getResultByDay(
        Integer.parseInt(date.format(DateTimeFormatter.ofPattern("yyMMdd"))), date));
  }
}
