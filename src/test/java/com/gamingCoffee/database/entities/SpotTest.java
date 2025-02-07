package com.gamingCoffee.database.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.SpotState;
import com.gamingCoffee.models.SpotType;
import org.junit.jupiter.api.Test;

class SpotTest {

  @Test
  void testBuilderWithAllFields() {
    Spot spot = new Spot.Builder().spotId(1).spotType(SpotType.PUBLIC)
        .spotState(SpotState.AVAILABLE).displayId(101).displaySize(24).displayType("LED")
        .consoleId(201).consoleType(ConsoleType.PS5).build();

    assertEquals(1, spot.getSpotId());
    assertEquals(SpotType.PUBLIC, spot.getSpotType());
    assertEquals(SpotState.AVAILABLE, spot.getSpotState());
    assertEquals(101, spot.getDisplayId());
    assertEquals(24, spot.getDisplaySize());
    assertEquals("LED", spot.getDisplayType());
    assertEquals(201, spot.getConsoleId());
    assertEquals(ConsoleType.PS5, spot.getConsoleType());
  }

  @Test
  void testBuilderWithOptionalFields() {
    Spot spot = new Spot.Builder().spotId(2).spotType(SpotType.PRIVATE).build();

    assertEquals(2, spot.getSpotId());
    assertEquals(SpotType.PRIVATE, spot.getSpotType());
    assertNull(spot.getSpotState()); // Optional field, should be null
    assertEquals(0, spot.getDisplayId()); // Optional field, should be 0
    assertEquals(0, spot.getDisplaySize()); // Optional field, should be 0
    assertNull(spot.getDisplayType()); // Optional field, should be null
    assertEquals(0, spot.getConsoleId()); // Optional field, should be 0
    assertNull(spot.getConsoleType()); // Optional field, should be null
  }

  @Test
  void testBuilderValidation() {
    assertThrows(IllegalArgumentException.class,
        () -> new Spot.Builder().spotId(0)); // Invalid spotId
    assertThrows(IllegalArgumentException.class,
        () -> new Spot.Builder().displayId(-1)); // Invalid displayId
    assertThrows(IllegalArgumentException.class,
        () -> new Spot.Builder().displaySize(0)); // Invalid displaySize
    assertThrows(IllegalArgumentException.class,
        () -> new Spot.Builder().displayType("")); // Invalid displayType
    assertThrows(IllegalArgumentException.class,
        () -> new Spot.Builder().consoleId(-1)); // Invalid consoleId
  }
}