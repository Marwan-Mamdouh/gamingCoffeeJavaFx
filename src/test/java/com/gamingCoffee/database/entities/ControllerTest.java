package com.gamingCoffee.database.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gamingCoffee.models.ControllerType;
import org.junit.jupiter.api.Test;

class ControllerTest {

  @Test
  void testBuilderWithRequiredFields() {
    // Create a Controller object with only the required field (controllerType)
    Controller controller = new Controller.Builder().controllerType(ControllerType.PS5CONTROLLER)
        .build();

    assertEquals(ControllerType.PS5CONTROLLER, controller.getControllerType());
    assertEquals(0, controller.getControllerId()); // Default value for optional field
  }

  @Test
  void testBuilderWithAllFields() {
    // Create a Controller object with all fields
    Controller controller = new Controller.Builder().controllerType(ControllerType.PS4CONTROLLER)
        .controllerId(1) // Optional field
        .build();

    assertEquals(ControllerType.PS4CONTROLLER, controller.getControllerType());
    assertEquals(1, controller.getControllerId());
  }

  @Test
  void testBuilderValidation() {
    // Test validation for required fields
//    assertThrows(IllegalArgumentException.class,
//        () -> new Controller.Builder().build()); // Null controllerType

    // Test validation for optional fields
    Controller.Builder builder = new Controller.Builder().controllerType(
        ControllerType.PS5CONTROLLER);
    assertThrows(IllegalArgumentException.class,
        () -> builder.controllerId(0)); // Invalid controllerId (0)
    assertThrows(IllegalArgumentException.class,
        () -> builder.controllerId(-1)); // Invalid controllerId (negative)
  }

  @Test
  void testEqualsAndHashCode() {
    // Create two Controller objects with the same values
    Controller controller1 = new Controller.Builder().controllerType(ControllerType.PS5CONTROLLER)
        .controllerId(1).build();

    Controller controller2 = new Controller.Builder().controllerType(ControllerType.PS5CONTROLLER)
        .controllerId(1).build();

    // Create a Controller object with different values
    Controller controller3 = new Controller.Builder().controllerType(ControllerType.PS5CONTROLLER)
        .controllerId(2).build();

    // Test equality
    assertEquals(controller1, controller2); // Objects with the same values should be equal
    assertNotEquals(controller1, controller3); // Objects with different values should not be equal

    // Test hash code
    assertEquals(controller1.hashCode(),
        controller2.hashCode()); // Hash codes should be equal for equal objects
  }

  @Test
  void testToString() {
    // Create a Controller object
    Controller controller = new Controller.Builder().controllerType(ControllerType.PS5CONTROLLER)
        .controllerId(1).build();

    // Test the toString method
    String expected = "Controller{controllerId=1, controllerType=PS5CONTROLLER}";
    assertEquals(expected, controller.toString());
  }

  @Test
  void testDefaultControllerId() {
    // Create a Controller object without setting the optional controllerId
    Controller controller = new Controller.Builder().controllerType(ControllerType.PS5CONTROLLER)
        .build();

    // Test that the default controllerId is 0
    assertEquals(0, controller.getControllerId());
  }
}