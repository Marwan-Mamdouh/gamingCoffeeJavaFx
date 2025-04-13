package com.gamingCoffee.database.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gamingCoffee.spot.model.ConsoleType;
import com.gamingCoffee.spot.model.Spot;
import com.gamingCoffee.spot.model.SpotState;
import com.gamingCoffee.spot.model.SpotType;
import org.junit.jupiter.api.Test;

class SpotTest {

  @Test
  void shouldCreateValidSpot() {
    Spot spot = new Spot.Builder().spotId(1).spotType(SpotType.PUBLIC)
        .spotState(SpotState.AVAILABLE).displayId(101).displaySize(27).displayType("LED")
        .consoleId(201).consoleType(ConsoleType.PS5).build();

    assertNotNull(spot);
    assertEquals(1, spot.getSpotId());
    assertEquals(27, spot.getDisplaySize());
    assertEquals(ConsoleType.PS5, spot.getConsoleType());
  }

  @Test
  void shouldThrowExceptionForNegativeSpotId() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Spot.Builder().spotId(-5).build());
    assertEquals("Spot ID must be positive.", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionForZeroDisplaySize() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Spot.Builder().displaySize(0).build());
    assertEquals("Display size must be greater than zero.", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionForBlankDisplayType() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Spot.Builder().displayType(""));
    assertEquals("Display type cannot be null or blank.", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionForNullSpotState() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Spot.Builder().spotState(null).build());
    assertEquals("Spot state cannot be null.", exception.getMessage());
  }

  @Test
  void shouldHaveConsistentEqualsAndHashCode() {
    Spot spot1 = new Spot.Builder().spotId(1).spotType(SpotType.PRIVATE)
        .spotState(SpotState.AVAILABLE).displayId(101).displaySize(27).displayType("LED")
        .consoleId(201).consoleType(ConsoleType.PS5).build();

    Spot spot2 = new Spot.Builder().spotId(1).spotType(SpotType.PRIVATE)
        .spotState(SpotState.AVAILABLE).displayId(101).displaySize(27).displayType("LED")
        .consoleId(201).consoleType(ConsoleType.PS5).build();

    assertEquals(spot1, spot2);
    assertEquals(spot1.hashCode(), spot2.hashCode());
  }
}