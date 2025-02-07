package com.gamingCoffee.database.controller;

import com.gamingCoffee.database.entities.Spot;
import java.sql.SQLException;
import java.util.List;

public interface ISpotDao {

//  void addPublicSpot(Spot spot) throws SQLException;
//  void addPrivateSpot(Spot spot) throws SQLException;

  void addSpot(Spot spot) throws SQLException;

  void removeById(int spotId) throws SQLException;

  void removeByType(Spot spot) throws SQLException;

  List<Spot> getFreeSpots() throws SQLException;

  List<Spot> getAllSpots() throws SQLException;

  List<Spot> getFreePrivateSpots() throws SQLException;

  List<Spot> getFreePublicSpots() throws SQLException;

  Spot getSpotPrivacyAndConsoleType(int spotNumber) throws SQLException;

  Spot checkSpot(int spot_id) throws SQLException;
}