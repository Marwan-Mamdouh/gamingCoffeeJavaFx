package com.gamingCoffee.database.controller;

import com.gamingCoffee.database.entities.Session;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ISessionDao {

  List<Session> getCurrentSessions() throws SQLException;

  List<Session> getSessionsPerDate(String date) throws SQLException;

  boolean createOpenSession(Session newSession) throws SQLException;

  boolean createTimedSession(Session newSession, double hours) throws SQLException;

  boolean endSession(Session oldSession) throws SQLException;

  //  int getTotalPrice(int session_id) throws SQLException;

  Session getNoControllersAndDuration(int sessionId) throws SQLException;

  void writeSessionPrice(double sessionPrice, int sessionId) throws SQLException;

  double[] getSessionCountAndSumPrices(String date) throws SQLException;

  int getRunningSessionBySpot(int spotId) throws SQLException;

  int getLastSessionId() throws SQLException;
}