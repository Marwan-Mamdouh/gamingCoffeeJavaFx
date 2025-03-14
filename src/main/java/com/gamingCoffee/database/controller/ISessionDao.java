package com.gamingCoffee.database.controller;

import com.gamingCoffee.database.entities.Session;
import com.gamingCoffee.services.SessionData;
import java.sql.SQLException;
import java.util.List;

public interface ISessionDao {

  List<Session> getCurrentSessions() throws SQLException;

  List<Session> getSessionsPerDate(String date) throws SQLException;

  SessionData getSessionData(int spotId) throws SQLException;

  boolean createOpenSession(Session newSession) throws SQLException;

  boolean createTimedSession(Session newSession, double hours) throws SQLException;

  boolean endSession(Session oldSession) throws SQLException;

  boolean writeSessionPrice(double sessionPrice, int sessionId) throws SQLException;

  double[] getSessionCountAndSumPrices(String date) throws SQLException;

  int getLastSessionId() throws SQLException;
}