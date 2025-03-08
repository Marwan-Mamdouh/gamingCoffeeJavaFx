package com.gamingCoffee.database.entities;

import com.gamingCoffee.models.ControllerType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Controller(int controllerId, ControllerType controllerType) {

  public Controller {
    if (controllerId <= 0) {
      throw new IllegalArgumentException("Controller ID Couldn't be less than 0.");
    }
  }

  @Contract("_, _ -> new")
  public static @NotNull Controller with(int controllerId, ControllerType controllerType) {
    return new Controller(controllerId, controllerType);
  }

  public int getControllerId() {
    return controllerId;
  }

  public ControllerType getControllerType() {
    return controllerType;
  }
}