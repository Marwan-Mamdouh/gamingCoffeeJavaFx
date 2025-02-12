package com.gamingCoffee.utiles;

import java.util.List;
import java.util.function.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListUtils {

  // For simple conversion without exception handling
  public static <T> ObservableList<T> toObservableList(List<T> list) {
    return FXCollections.observableArrayList(list);
  }

  // For conversion with exception handling and popup
  public static <T> ObservableList<T> getObservableListOrHandle(Supplier<List<T>> supplier) {
    try {
      return FXCollections.observableArrayList(supplier.get());
    } catch (Exception e) {
      PopupUtil.showErrorPopup(e);
      return FXCollections.observableArrayList(); // Return empty list instead of null
    }
  }
}
