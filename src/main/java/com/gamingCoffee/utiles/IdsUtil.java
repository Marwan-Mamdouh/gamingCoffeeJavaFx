package com.gamingCoffee.utiles;

import com.gamingCoffee.database.controller.ISessionDao;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IdsUtil {


  /**
   * @param sessionDao (ISessionDao)
   * @return (int)
   * @produce the ID of last created session
   */
  public static int getLastIdFromDb(ISessionDao sessionDao) throws SQLException {
    return sessionDao.getLastSessionId();
  }

  /**
   * @param lastId (int)
   * @return (int) as yyMMdd000, 'yy' is the second two numbers form the year, MM is the month
   * number, dd is the day number, and all this number are from the today's date then add to them
   * three zeros to count how many sessions has been created till now
   * @produce the new Session ID to write in the db
   */
  public static int generateId(int lastId) {
    // Step 1: validate last ID var
    if (lastId == 0) {
      throw new IllegalArgumentException(
          "There is no Sessions in 'sessions' Table, or something went wrong with the db.");

    } else if (isFiveDigitsOrLess(lastId)) {
      // if the lastId is less than 6 digits then mak it 6
      lastId = extendInt(lastId);
    }

    // Step 2: Get today's date in the format yyMMdd
    String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

    // Step 3: compare the last Session ID form the DB with today's ID
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

  /**
   * @param id (int)
   * @throws IllegalArgumentException if the id is not positive
   * @produce check if id is not positive
   */
  public static void validateIdPositive(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("Invalid ID: " + id + ".");
    }
  }
}
