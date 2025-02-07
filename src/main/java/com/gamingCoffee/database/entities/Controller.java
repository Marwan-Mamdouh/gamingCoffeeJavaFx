package com.gamingCoffee.database.entities;

import com.gamingCoffee.models.ControllerType;
import java.util.Objects;

public class Controller {

  // Required fields
  private final int controllerId;
  private final ControllerType controllerType;

  private Controller(Builder builder) {
    this.controllerId = builder.controllerId;
    this.controllerType = builder.controllerType;
  }

  public ControllerType getControllerType() {
    return controllerType;
  }

  public int getControllerId() {
    return controllerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Controller that = (Controller) o;
    return controllerId == that.controllerId && controllerType == that.controllerType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(controllerId, controllerType);
  }

  @Override
  public String toString() {
    return String.format("Controller{controllerId=%d, controllerType=%s}", controllerId,
        controllerType);
  }

  public static class Builder {

    // Required fields
    private ControllerType controllerType = null;
    private int controllerId = 0;

    // Constructor for required fields
    public Builder controllerType(ControllerType controllerType) {
      this.controllerType = controllerType;
      return this;
    }

    // Method to set optional field
    public Builder controllerId(int controllerId) {
      if (controllerId <= 0) {
        throw new IllegalArgumentException("Controller ID must be positive.");
      }
      this.controllerId = controllerId;
      return this;
    }

    // Build method to create the Controller object
    public Controller build() {
      return new Controller(this);
    }
  }
}
