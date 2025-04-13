package com.gamingCoffee.admin.repository;

import com.gamingCoffee.admin.model.Admin;
import java.sql.SQLException;
import java.util.List;

public interface AdminRepository {

  Admin getUserData(String userName) throws SQLException;

  boolean addUser(Admin newAdmin) throws SQLException;

  boolean removeUser(String username) throws SQLException;

  boolean changePassword(String username, String newPassword) throws SQLException;

  boolean addPhoneNumber(Admin admin) throws SQLException;

  List<Admin> getAdmins() throws SQLException;

  String getPasswordByUsername(String username) throws SQLException;

  Admin getAdminInfo(String username) throws SQLException;

  boolean removePhoneNumber(String username) throws SQLException;
}
