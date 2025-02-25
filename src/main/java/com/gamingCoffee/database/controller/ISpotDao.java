package com.gamingCoffee.database.controller;

import com.gamingCoffee.database.entities.Spot;
import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.SpotType;
import java.sql.SQLException;
import java.util.List;

public interface ISpotDao {

  boolean addSpot(Spot spot) throws SQLException;

  boolean removeById(int spotId) throws SQLException;

  List<Spot> getFreeSpots() throws SQLException;

  List<Integer> getFreeSpotsNumbers() throws SQLException;

  List<Integer> getBusySpotsNumbers() throws SQLException;

  List<Spot> getAllSpots() throws SQLException;

  Spot getSpotPrivacyAndConsoleType(int spotNumber) throws SQLException;

  Spot checkSpot(int spot_id) throws SQLException;

  double getPricePerHour(SpotType spotType, ConsoleType consoleType);

  boolean setPrice(Enum<?> serviceType, double price);

  double getServicePrice(Enum<?> serviceType);
}