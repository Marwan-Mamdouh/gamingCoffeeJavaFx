package com.gamingCoffee.services;

import com.gamingCoffee.database.controller.ISessionDao;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GenerateId {

/*
  private static int localLastId;


  private static int sequence = 250125090; // This should be persisted in a database or file

  public static void main(String[] args) {
    int generatedId = generateId(2502);
    System.out.println("Generated ID: " + generatedId);
  }


  private static int getLastId() {
    return localLastId;
  }

  private static void setLastId(int lastId) {
    localLastId = lastId;
  }
*/

  public static int getLastIdFromDb(ISessionDao sessionDao) throws SQLException {
    return sessionDao.getLastSessionId();
  }

  public static int generateId(int lastId) {
    // Step 1: validate last ID var
    if (lastId == 0) {
      throw new IllegalArgumentException("something went wrong, generate ID class.");

    } else if (isFiveDigitsOrLess(lastId)) {
      lastId = extendInt(lastId);
//      lastId = Integer.parseInt(String.format("%06d", lastId));
//      System.out.println(lastId);
    }

    // Step 2: Get today's date in the format yyMMdd
    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

    // Step 3: compare the DB ID with today's ID
    if (isIdFromToday(lastId, date)) {
      // if they are the same day then increment it
      return ++lastId;
    } else {
      // else set the new today's ID with 001 by Combining the date and sequence parts
      return Integer.parseInt(date + String.format("%03d", 1));
    }
  }

  private static boolean isIdFromToday(int lastId, String date) {
    return date.equals(getDateFromId(lastId));
  }

  private static String getDateFromId(int lastId) {
    return Integer.toString(lastId).substring(0, 6);
  }

  private static boolean isFiveDigitsOrLess(int num) {
    return num < 99999;
  }

  private static int extendInt(int smallId) {
    StringBuilder newId = new StringBuilder(Integer.toString(smallId));
    while (newId.length() < 6) {
      newId.append(0);
    }
    return Integer.parseInt(newId.toString());
  }
}
