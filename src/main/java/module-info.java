module gamingcoffee.demo {
  requires javafx.fxml;
  requires java.sql;
  requires bcrypt;
  requires jdk.compiler;
  requires de.jensd.fx.glyphs.fontawesome;
  requires javafx.controls;
  requires org.jetbrains.annotations;

  opens com.gamingCoffee to javafx.fxml;

  exports com.gamingCoffee.database.connection;
  exports com.gamingCoffee.utiles;
  exports com.gamingCoffee.admin.repository;
  exports com.gamingCoffee.admin.service;
  exports com.gamingCoffee.admin.model;
  exports com.gamingCoffee.admin.dao;
  exports com.gamingCoffee.controller.repository;
  exports com.gamingCoffee.controller.service;
  exports com.gamingCoffee.controller.model;
  exports com.gamingCoffee.controller.dao;
  exports com.gamingCoffee.session.repository;
  exports com.gamingCoffee.session.service;
  exports com.gamingCoffee.session.model;
  exports com.gamingCoffee.session.dao;
  exports com.gamingCoffee.expense.repository;
  exports com.gamingCoffee.expense.service;
  exports com.gamingCoffee.expense.model;
  exports com.gamingCoffee.expense.dao;
  exports com.gamingCoffee.result.repository;
  exports com.gamingCoffee.result.service;
  exports com.gamingCoffee.result.model;
  exports com.gamingCoffee.result.dao;
  exports com.gamingCoffee.spot.repository;
  exports com.gamingCoffee.spot.service;
  exports com.gamingCoffee.spot.model;
  exports com.gamingCoffee.spot.dao;
  exports com.gamingCoffee;
  exports com.gamingCoffee.uiController;
  opens com.gamingCoffee.uiController to javafx.fxml;
}