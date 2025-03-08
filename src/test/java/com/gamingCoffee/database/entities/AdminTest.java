package com.gamingCoffee.database.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gamingCoffee.models.Position;
import org.junit.jupiter.api.Test;

class AdminTest {

  @Test
  void shouldCreateAdminSuccessfully() {
    Admin admin = new Admin.Builder().username("adminUser").password("securePass")
        .title(Position.OWNER).phoneNumber("01012345678").hiringDate("2024-03-08").age(30)
        .salary(5000.0).build();

    assertThat(admin).isNotNull();
    assertThat(admin.getUsername()).isEqualTo("adminUser");
    assertThat(admin.getPassword()).isEqualTo("securePass");
    assertThat(admin.getTitle()).isEqualTo(Position.OWNER);
    assertThat(admin.getPhoneNumber()).isEqualTo("01012345678");
    assertThat(admin.getHiringDate()).isEqualTo("2024-03-08");
    assertThat(admin.getAge()).isEqualTo(30);
    assertThat(admin.getSalary()).isEqualTo(5000.0);
  }

  @Test
  void shouldThrowExceptionForBlankUsername() {
    assertThatThrownBy(() -> new Admin.Builder().username("").build()).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Username cannot be null or blank.");
  }

  @Test
  void shouldThrowExceptionForBlankPassword() {
    assertThatThrownBy(() -> new Admin.Builder().password("").build()).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Password cannot be null or blank.");
  }

  @Test
  void shouldThrowExceptionForInvalidPhoneNumber() {
    assertThatThrownBy(() -> new Admin.Builder().phoneNumber("99999999").build()).isInstanceOf(
            IllegalArgumentException.class)
        .hasMessage("Invalid phone number. Please enter a valid Egyptian phone number.");
  }

  @Test
  void shouldThrowExceptionForNegativeAge() {
    assertThatThrownBy(() -> new Admin.Builder().age(-5).build()).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Age must be positive.");
  }

  @Test
  void shouldThrowExceptionForVeryHighAge() {
    assertThatThrownBy(() -> new Admin.Builder().age(100).build()).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Does he still alive?");
  }

  @Test
  void shouldThrowExceptionForNegativeSalary() {
    assertThatThrownBy(() -> new Admin.Builder().salary(-1000.0).build()).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Salary must be positive.");
  }

  @Test
  void shouldCheckEqualityOfTwoAdmins() {
    Admin admin1 = new Admin.Builder().username("adminUser").password("securePass")
        .title(Position.EMPLOYEE).phoneNumber("01012345678").hiringDate("2024-03-08").age(30)
        .salary(5000.0).build();

    Admin admin2 = new Admin.Builder().username("adminUser").password("securePass")
        .title(Position.EMPLOYEE).phoneNumber("01012345678").hiringDate("2024-03-08").age(30)
        .salary(5000.0).build();

    assertThat(admin1).isEqualTo(admin2);
    assertThat(admin1.hashCode()).isEqualTo(admin2.hashCode());
  }

  @Test
  void shouldCheckAdminsAreNotEqualWhenDifferent() {
    Admin admin1 = new Admin.Builder().username("admin1").password("pass1").title(Position.OWNER)
        .phoneNumber("01012345678").hiringDate("2024-03-08").age(30).salary(5000.0).build();

    Admin admin2 = new Admin.Builder().username("admin2") // Different username
        .password("pass1").title(Position.OWNER).phoneNumber("01012345678").hiringDate("2024-03-08")
        .age(30).salary(5000.0).build();

    assertThat(admin1).isNotEqualTo(admin2);
  }
}