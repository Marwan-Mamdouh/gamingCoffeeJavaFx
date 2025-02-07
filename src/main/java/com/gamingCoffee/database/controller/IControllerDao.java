package com.gamingCoffee.database.controller;

import com.gamingCoffee.database.entities.Controller;
import java.sql.SQLException;
import java.util.List;

public interface IControllerDao {

  boolean addController(Controller controller) throws SQLException;

  boolean removeController(int controllerId) throws SQLException;

  Controller checkController(int controllerId) throws SQLException;

  List<Controller> getAllController() throws SQLException;
}
