package com.gamingCoffee.database.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gamingCoffee.models.Position;
import org.junit.jupiter.api.Test;

class AdminTest {

  @Test
  void buildAdminWithAllFields_Success() {
    Admin admin = new Admin.Builder().username("adminUser").password("securePass123")
        .title(Position.EMPLOYEE).phoneNumber("01012345678").hiringDate("2023-01-15").age(30)
        .salary(50000).build();

    assertThat(admin.getUsername()).isEqualTo("adminUser");
    assertThat(admin.getPassword()).isEqualTo("securePass123");
    assertThat(admin.getTitle()).isEqualTo(Position.EMPLOYEE);
    assertThat(admin.getPhoneNumber()).isEqualTo("01012345678");
    assertThat(admin.getHiringDate()).isEqualTo("2023-01-15");
    assertThat(admin.getAge()).isEqualTo(30);
    assertThat(admin.getSalary()).isEqualTo(50000);
  }

//  @Test
//  void buildWithoutRequiredFields_ThrowsException() {
//    Admin.Builder builderWithoutUsername = new Admin.Builder().password("pass123").age(25);
//    assertThatThrownBy(builderWithoutUsername::build).isInstanceOf(IllegalStateException.class)
//        .hasMessageContaining("username");
//
//    Admin.Builder builderWithoutPassword = new Admin.Builder().username("user").age(25);
//    assertThatThrownBy(builderWithoutPassword::build).isInstanceOf(IllegalStateException.class)
//        .hasMessageContaining("password");
//  }

  @Test
  void invalidUsername_ThrowsException() {
    Admin.Builder builderNullUsername = new Admin.Builder().password("pass");
    assertThatThrownBy(() -> builderNullUsername.username(null)).isInstanceOf(
        IllegalArgumentException.class).hasMessageContaining("Username cannot be null or blank");

    Admin.Builder builderBlankUsername = new Admin.Builder().password("pass");
    assertThatThrownBy(() -> builderBlankUsername.username("   ")).isInstanceOf(
        IllegalArgumentException.class);
  }

  @Test
  void invalidPassword_ThrowsException() {
    Admin.Builder builder = new Admin.Builder().username("user");
    assertThatThrownBy(() -> builder.password(null)).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Password cannot be null or blank");
  }

  @Test
  void invalidPhoneNumber_ThrowsException() {
    Admin.Builder builder = new Admin.Builder().username("user").password("pass");
    assertThatThrownBy(() -> builder.phoneNumber("123456")).isInstanceOf(
        IllegalArgumentException.class).hasMessageContaining("Egyptian phone number");

    assertThatThrownBy(() -> builder.phoneNumber("02123456789")) // Invalid prefix
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void validEgyptianPhoneNumber_Success() {
    Admin.Builder builder = new Admin.Builder().username("user").password("pass")
        .phoneNumber("+201012345678");
    Admin admin = builder.build();
    assertThat(admin.getPhoneNumber()).isEqualTo("+201012345678");
  }

  @Test
  void invalidAge_ThrowsException() {
    Admin.Builder builder = new Admin.Builder().username("user").password("pass");
    assertThatThrownBy(() -> builder.age(0)).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("positive");

    assertThatThrownBy(() -> builder.age(80)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void negativeSalary_ThrowsException() {
    Admin.Builder builder = new Admin.Builder().username("user").password("pass");
    assertThatThrownBy(() -> builder.salary(-1000)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void equalsAndHashCode_Consistent() {
    Admin admin1 = new Admin.Builder().username("user1").password("pass1").age(30).build();
    Admin admin2 = new Admin.Builder().username("user1").password("pass1").age(30).build();
    assertThat(admin1).isEqualTo(admin2);
    assertThat(admin1.hashCode()).isEqualTo(admin2.hashCode());
  }

  @Test
  void toString_ContainsAllFields() {
    Admin admin = new Admin.Builder().username("testUser").password("testPass")
        .title(Position.OWNER).phoneNumber("01198765432").hiringDate("2022-05-20").age(28)
        .salary(40000).build();
    String toStringResult = admin.toString();
    assertThat(toStringResult).contains("testUser").contains("testPass").contains("OWNER")
        .contains("01198765432").contains("2022-05-20").contains("28").contains("40000");
  }
}