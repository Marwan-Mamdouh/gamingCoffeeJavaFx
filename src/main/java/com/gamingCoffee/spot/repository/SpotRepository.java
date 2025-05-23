package com.gamingCoffee.spot.repository;

import com.gamingCoffee.spot.model.Spot;
import com.gamingCoffee.spot.model.ConsoleType;
import com.gamingCoffee.spot.model.SpotType;
import java.sql.SQLException;
import java.util.List;

public interface SpotRepository {

  boolean addSpot(Spot spot) throws SQLException;

  boolean removeById(int spotId) throws SQLException;

  List<Spot> getFreeSpots() throws SQLException;

  List<Integer> getFreeSpotsNumbers() throws SQLException;

  List<Integer> getBusySpotsNumbers() throws SQLException;

  List<Spot> getAllSpots() throws SQLException;

  Spot checkSpot(int spot_id) throws SQLException;

  double getPricePerHour(SpotType spotType, ConsoleType consoleType);

  boolean setPrice(Enum<?> serviceType, double price);

  double getServicePrice(Enum<?> serviceType);
}