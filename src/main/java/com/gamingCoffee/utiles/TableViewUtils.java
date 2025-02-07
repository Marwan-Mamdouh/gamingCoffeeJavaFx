package com.gamingCoffee.utiles;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Button;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;

public class TableViewUtils {

  public static <T> void copySelectedCellsToClipboard(TableView<T> tableView) {
    List<TablePosition> selectedCells = new ArrayList<>(
        tableView.getSelectionModel().getSelectedCells());

    if (selectedCells.isEmpty()) {
      System.out.println("No cells selected.");
      return;
    }

    // Sort cells by row and column order
    selectedCells.sort((pos1, pos2) -> {
      int rowCompare = Integer.compare(pos1.getRow(), pos2.getRow());
      if (rowCompare != 0) {
        return rowCompare;
      } else {
        return Integer.compare(tableView.getColumns().indexOf(pos1.getTableColumn()),
            tableView.getColumns().indexOf(pos2.getTableColumn()));
      }
    });

    // Group cells by row
    Map<Integer, List<TablePosition>> cellsByRow = new LinkedHashMap<>();
    for (TablePosition pos : selectedCells) {
      cellsByRow.computeIfAbsent(pos.getRow(), k -> new ArrayList<>()).add(pos);
    }

    // Build clipboard string
    StringBuilder clipboardString = new StringBuilder();

    for (List<TablePosition> rowCells : cellsByRow.values()) {
      StringBuilder rowString = new StringBuilder();
      for (TablePosition pos : rowCells) {
        T item = tableView.getItems().get(pos.getRow());
        Object cellValue = pos.getTableColumn().getCellData(item);
        rowString.append(cellValue != null ? cellValue.toString() : "").append("\t");
      }
      if (rowString.length() > 0) {
        rowString.setLength(rowString.length() - 1); // Remove trailing tab
      }
      clipboardString.append(rowString).append("\n");
    }

    if (clipboardString.length() > 0) {
      clipboardString.setLength(clipboardString.length() - 1); // Remove trailing newline
    }

    // Copy to clipboard
    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent content = new ClipboardContent();
    content.putString(clipboardString.toString());
    clipboard.setContent(content);
  }

  public static <T> void makeTableCopyable(TableView<T> tableView) {
    tableView.getSelectionModel().setCellSelectionEnabled(true);
    tableView.setOnKeyPressed(event -> {
      if (event.isShortcutDown() && event.getCode() == KeyCode.C) {
        TableViewUtils.copySelectedCellsToClipboard(tableView);
        event.consume();
      }
    });
  }

  public static <T> void copyButton(TableView<T> tableView, Button button) {
    button.setOnAction(event -> {
      TableViewUtils.copySelectedCellsToClipboard(tableView);
    });
  }
}