package com.gamingCoffee.database.controller;

import com.gamingCoffee.database.entities.Spot;
import java.sql.SQLException;
import java.util.List;

public interface ISpotDao {

  boolean addSpot(Spot spot) throws SQLException;

  boolean removeById(int spotId) throws SQLException;

  List<Spot> getFreeSpots() throws SQLException;

  List<String> getFreeSpotsNumbers() throws SQLException;

  List<String> getBusySpotsNumbers() throws SQLException;

  List<Spot> getAllSpots() throws SQLException;

  Spot getSpotPrivacyAndConsoleType(int spotNumber) throws SQLException;

  Spot checkSpot(int spot_id) throws SQLException;
}