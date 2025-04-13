package com.gamingCoffee.controller.repository;

import com.gamingCoffee.controller.model.Controller;
import java.sql.SQLException;
import java.util.List;

public interface ControllerRepository {

  boolean addController(Controller controller) throws SQLException;

  boolean removeController(int controllerId) throws SQLException;

  Controller checkController(int controllerId) throws SQLException;

  List<Controller> getAllController() throws SQLException;
}
