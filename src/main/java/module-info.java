module gamingcoffee.demo {
  requires javafx.fxml;
  requires java.sql;
  requires bcrypt;
  requires jdk.compiler;
  requires de.jensd.fx.glyphs.fontawesome;
  requires javafx.controls;
  requires org.jetbrains.annotations;

  opens com.gamingCoffee.controllers to javafx.fxml;

  exports com.gamingCoffee.controllers;
  exports com.gamingCoffee.database.controller;
  exports com.gamingCoffee.database.connection;
  exports com.gamingCoffee.utiles;
  exports com.gamingCoffee.models;
  exports com.gamingCoffee.database.entities;
  exports com.gamingCoffee.services;
}