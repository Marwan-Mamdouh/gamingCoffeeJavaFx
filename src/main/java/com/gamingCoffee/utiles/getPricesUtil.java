package com.gamingCoffee.utiles;

import java.util.Scanner;

public class getPricesUtil {

  private static Scanner scanner;

  public static int setPsPricePerHour() {
    try {
      int psPrice = scanner.nextInt();
      if (psPrice <= 0) {
        throw new IllegalArgumentException("prices must be positive");
      }
      return psPrice;
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
    return 0;
  }

  public static int setPrivateSpotPrice() {
    try {
      int privateSpotPrice = scanner.nextInt();
      if (privateSpotPrice <= 0) {
        throw new IllegalArgumentException("prices must be positive");
      }
      return privateSpotPrice;
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
    return 0;
  }

  public static int setNoControllerPrice() {
    try {
      int noControllerPrice = scanner.nextInt();
      if (noControllerPrice <= 0) {
        throw new IllegalArgumentException("prices must be positive");
      }
      return noControllerPrice;
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
    }
    return 0;
  }
}
