package com.gamingCoffee.database.controller;

import com.gamingCoffee.database.entities.Result;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public interface IResultDao {

  Result getResultByDay(int id, LocalDate date);

  List<Result> getResultByMonth(Month month);

}
