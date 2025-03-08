package com.gamingCoffee.database.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gamingCoffee.models.ControllerType;
import org.junit.jupiter.api.Test;

class ControllerTest {

  @Test
  void shouldCreateControllerSuccessfully() {
    Controller controller = new Controller(1, ControllerType.PS4CONTROLLER);

    assertThat(controller).isNotNull();
    assertThat(controller.controllerId()).isEqualTo(1);
    assertThat(controller.controllerType()).isEqualTo(ControllerType.PS4CONTROLLER);
  }

  @Test
  void shouldThrowExceptionForInvalidControllerId() {
    assertThatThrownBy(() -> new Controller(0, ControllerType.PS5CONTROLLER)).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Controller ID Couldn't be less than 0.");

    assertThatThrownBy(() -> new Controller(-5, ControllerType.PS5CONTROLLER)).isInstanceOf(
        IllegalArgumentException.class).hasMessage("Controller ID Couldn't be less than 0.");
  }

  @Test
  void shouldCreateControllerUsingWithMethod() {
    Controller controller = Controller.with(2, ControllerType.PS5CONTROLLER);

    assertThat(controller).isNotNull();
    assertThat(controller.controllerId()).isEqualTo(2);
    assertThat(controller.controllerType()).isEqualTo(ControllerType.PS5CONTROLLER);
  }

  @Test
  void shouldCheckEqualityOfControllers() {
    Controller controller1 = new Controller(3, ControllerType.PS4CONTROLLER);
    Controller controller2 = new Controller(3, ControllerType.PS4CONTROLLER);

    assertThat(controller1).isEqualTo(controller2);
    assertThat(controller1.hashCode()).isEqualTo(controller2.hashCode());
  }

  @Test
  void shouldCheckControllersAreNotEqualWhenDifferent() {
    Controller controller1 = new Controller(4, ControllerType.PS5CONTROLLER);
    Controller controller2 = new Controller(5, ControllerType.PS4CONTROLLER);

    assertThat(controller1).isNotEqualTo(controller2);
  }
}