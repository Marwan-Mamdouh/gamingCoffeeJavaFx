package com.gamingCoffee.database.controller;

import com.gamingCoffee.database.entities.Session;
import java.sql.SQLException;
import java.util.List;

public interface ISessionDao {

  List<Session> getCurrentSessions() throws SQLException;

  List<Session> getSessionsPerDate(String date) throws SQLException;

  boolean createOpenSession(Session newSession) throws SQLException;

  boolean createTimedSession(Session newSession, double hours) throws SQLException;

  boolean endSession(Session oldSession) throws SQLException;

  boolean writeSessionPrice(double sessionPrice, int sessionId) throws SQLException;

  Session getSessionIdAndControllersAndDuration(int sessionId) throws SQLException;

  Session getSessionData(int spotId) throws SQLException;

  double[] getSessionCountAndSumPrices(String date) throws SQLException;

  int getLastSessionId() throws SQLException;
}