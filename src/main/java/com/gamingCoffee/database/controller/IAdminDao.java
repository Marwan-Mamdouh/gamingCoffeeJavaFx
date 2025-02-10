package com.gamingCoffee.database.controller;

import com.gamingCoffee.database.entities.Admin;
import java.sql.SQLException;
import java.util.List;

public interface IAdminDao {

  Admin getUserData(String userName) throws SQLException;

  boolean addUser(Admin newAdmin) throws SQLException;

  boolean removeUser(String username) throws SQLException;

  boolean changePassword(String username, String newPassword) throws SQLException;

  //  void removeUserByUserId(Admin admin) throws SQLException;

  //  void changeUserName(String username, String newUserName) throws SQLException;

  //  int getUserId(String userName) throws SQLException;

  //  String getUserTitle(String username) throws SQLException;

  boolean addPhoneNumber(Admin admin) throws SQLException;

  List<Admin> getAdmins() throws SQLException;

  String getPasswordByUsername(String username) throws SQLException;

  Admin getAdminInfo(String username) throws SQLException;

  boolean removePhoneNumber(String username) throws SQLException;
}
