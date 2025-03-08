package com.gamingCoffee.database.entities;

import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.SpotState;
import com.gamingCoffee.models.SpotType;
import java.util.Objects;

public class Spot {

  // Spot data
  private final int spotId;
  private final SpotType spotType;
  private final SpotState spotState;

  // Display data (monitor)
  private final int displayId;
  private final int displaySize;
  private final String displayType;

  // Console data
  private final int consoleId;
  private final ConsoleType consoleType;

  // Private constructor to enforce the use of the Builder
  private Spot(Builder builder) {
    this.spotId = builder.spotId;
    this.spotType = builder.spotType;
    this.spotState = builder.spotState;
    this.displayId = builder.displayId;
    this.displaySize = builder.displaySize;
    this.displayType = builder.displayType;
    this.consoleId = builder.consoleId;
    this.consoleType = builder.consoleType;
  }

  // Getters
  public int getSpotId() {
    return spotId;
  }

  public SpotType getSpotType() {
    return spotType;
  }

  public SpotState getSpotState() {
    return spotState;
  }

  public int getDisplayId() {
    return displayId;
  }

  public int getDisplaySize() {
    return displaySize;
  }

  public String getDisplayType() {
    return displayType;
  }

  public int getConsoleId() {
    return consoleId;
  }

  public ConsoleType getConsoleType() {
    return consoleType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Spot spot)) {
      return false;
    }
    return getSpotId() == spot.getSpotId() && getDisplayId() == spot.getDisplayId()
        && getDisplaySize() == spot.getDisplaySize() && getConsoleId() == spot.getConsoleId()
        && getSpotType() == spot.getSpotType() && getSpotState() == spot.getSpotState()
        && Objects.equals(getDisplayType(), spot.getDisplayType())
        && getConsoleType() == spot.getConsoleType();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSpotId(), getSpotType(), getSpotState(), getDisplayId(),
        getDisplaySize(), getDisplayType(), getConsoleId(), getConsoleType());
  }

  @Override
  public String toString() {
    return "Spot{" + "spotId=" + spotId + ", spotType=" + spotType + ", spotState=" + spotState
        + ", displayId=" + displayId + ", displaySize=" + displaySize + ", displayType='"
        + displayType + '\'' + ", consoleId=" + consoleId + ", consoleType=" + consoleType + '}';
  }

  // Builder class
  public static class Builder {

    // Spot data
    private int spotId = 0; // Default value
    private SpotType spotType = null; // Default value
    private SpotState spotState = null; // Default value

    // Display data (monitor)
    private int displayId = 0; // Default value
    private int displaySize = 0; // Default value
    private String displayType = null; // Default value

    // Console data
    private int consoleId = 0; // Default value
    private ConsoleType consoleType = null; // Default value

    // Methods to set optional fields
    public Builder spotId(int spotId) {
      if (spotId <= 0) {
        throw new IllegalArgumentException("Spot ID must be positive.");
      }
      this.spotId = spotId;
      return this;
    }

    public Builder spotType(SpotType spotType) {
      this.spotType = spotType;
      return this;
    }

    public Builder spotState(SpotState spotState) {
      this.spotState = spotState;
      return this;
    }

    public Builder displayId(int displayId) {
      if (displayId <= 0) {
        throw new IllegalArgumentException("Display ID must be positive.");
      }
      this.displayId = displayId;
      return this;
    }

    public Builder displaySize(int displaySize) {
      if (displaySize <= 0) {
        throw new IllegalArgumentException("Display size must be positive.");
      }
      this.displaySize = displaySize;
      return this;
    }

    public Builder displayType(String displayType) {
      if (displayType == null || displayType.isBlank()) {
        throw new IllegalArgumentException("Display type cannot be null or blank.");
      }
      this.displayType = displayType;
      return this;
    }

    public Builder consoleId(int consoleId) {
      if (consoleId <= 0) {
        throw new IllegalArgumentException("Console ID must be positive.");
      }
      this.consoleId = consoleId;
      return this;
    }

    public Builder consoleType(ConsoleType consoleType) {
      this.consoleType = consoleType;
      return this;
    }

    // Build method to create the Spot object
    public Spot build() {
      return new Spot(this);
    }
  }
}