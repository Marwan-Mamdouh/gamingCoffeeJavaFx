package com.gamingCoffee.database.entities;

import com.gamingCoffee.models.Position;
import java.util.Objects;

public class Admin {

  //  private final int adminId;
  private final String username;
  private final String password;
  private final Position title;
  private final String phoneNumber;
  private final String hiringDate;
  private final int age;
  private final int salary;

  // Private constructor to enforce the use of the Builder
  private Admin(Builder builder) {
//    this.adminId = builder.adminId;
    this.title = builder.title;
    this.username = builder.username;
    this.password = builder.password;
    this.phoneNumber = builder.phoneNumber;
    this.hiringDate = builder.hiringDate;
    this.age = builder.age;
    this.salary = builder.salary;
  }

  // Getters
  public String getUsername() {
    return username;
  }

  public Position getTitle() {
    return title;
  }

  public String getPassword() {
    return password;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getHiringDate() {
    return hiringDate;
  }

  public int getAge() {
    return age;
  }

  public int getSalary() {
    return salary;
  }

  @Override
  public String toString() {
    return "Admin{" + "username='" + username + '\'' + ", password='" + password + '\'' + ", title="
        + title + ", phoneNumber='" + phoneNumber + '\'' + ", hiringDate='" + hiringDate + '\''
        + ", age=" + age + ", salary=" + salary + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Admin admin = (Admin) o;
    return getAge() == admin.getAge() && getSalary() == admin.getSalary() && Objects.equals(
        getUsername(), admin.getUsername()) && Objects.equals(getPassword(), admin.getPassword())
        && getTitle() == admin.getTitle() && Objects.equals(getPhoneNumber(),
        admin.getPhoneNumber()) && Objects.equals(getHiringDate(), admin.getHiringDate());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUsername(), getPassword(), getTitle(), getPhoneNumber(), getHiringDate(),
        getAge(), getSalary());
  }

  // Builder class
  public static class Builder {

    // Optional fields with default values
//    private int adminId = 0;
    private String username = null;
    private Position title = null;
    private String password = null;
    private String phoneNumber = null;
    private String hiringDate = null;
    private int age = 0;
    private int salary = 0;

    // Methods to set optional fields
//    public Builder adminId(int adminId) {
//      if (adminId <= 0) {
//        throw new IllegalArgumentException("Admin ID must be positive.");
//      }
//      this.adminId = adminId;
//      return this;
//    }

    public Builder username(String username) {
      if (username == null || username.isBlank()) {
        throw new IllegalArgumentException("Username cannot be null or blank.");
      }
      this.username = username;
      return this;
    }

    public Builder password(String password) {
      if (password == null || password.isBlank()) {
        throw new IllegalArgumentException("Password cannot be null or blank.");
      }
      this.password = password;
      return this;
    }

    public Builder title(Position title) {
      this.title = title;
      return this;
    }

    public Builder hiringDate(String hiringDate) {
      this.hiringDate = hiringDate;
      return this;
    }

    public Builder phoneNumber(String phoneNumber) {
      if (!isValidEgyptianPhoneNumber(phoneNumber)) {
        throw new IllegalArgumentException(
            "Invalid phone number. Please enter a valid Egyptian phone number.");
      }
      this.phoneNumber = phoneNumber;
      return this;
    }

    public Builder age(int age) {
      if (age < 1) {
        throw new IllegalArgumentException("Age must be positive.");
      } else if (age >= 80) {
        throw new IllegalArgumentException("Does he still alive?");
      } else {
        this.age = age;
        return this;
      }
    }

    public Builder salary(int salary) {
      if (salary < 0) {
        throw new IllegalArgumentException("Salary must be positive.");
      }
      this.salary = salary;
      return this;
    }

    // Build method to create the Admin object
    public Admin build() {
      return new Admin(this);
    }

    // Helper method to validate Egyptian phone numbers
    private boolean isValidEgyptianPhoneNumber(String phoneNumber) {
      // return true if it is null to make it an optional field
      if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
        return true;
      }
      // Regex pattern for Egyptian phone numbers (local or international format)
      String regex = "^(010|011|012|015)\\d{8}$|^\\+20(10|11|12|15)\\d{8}$";
      return phoneNumber.trim().matches(regex);
    }
  }
}