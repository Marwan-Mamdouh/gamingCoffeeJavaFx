package com.gamingCoffee.utiles;

public class AdminUsernameHolder {

  private static String adminUsername;

  public static String getAdminName() {
    return adminUsername;
  }

  public static void setAdminName(String adminUsername) {
    AdminUsernameHolder.adminUsername = adminUsername;
  }
}
