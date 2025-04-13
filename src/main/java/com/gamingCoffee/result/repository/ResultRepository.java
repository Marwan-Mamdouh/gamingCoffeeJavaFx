package com.gamingCoffee.result.repository;

import com.gamingCoffee.result.model.Result;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public interface ResultRepository {

  Result getResultByDay(int id, LocalDate date);

  List<Result> getResultByMonth(Month month);

}
