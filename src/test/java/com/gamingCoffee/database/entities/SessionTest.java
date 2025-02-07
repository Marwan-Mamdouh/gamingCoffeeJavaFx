package com.gamingCoffee.database.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gamingCoffee.models.SessionState;
import org.junit.jupiter.api.Test;

class SessionTest {

  @Test
  void testBuilderWithAllFields() {
    Session session = new Session.Builder().sessionId(1).spotId(101).creator("marwan")
        .noControllers(2).startTime("10:00").endTime("12:00").duration(120)
        .sessionState(SessionState.RUNNING).build();

    assertEquals(1, session.getSessionId());
    assertEquals(101, session.getSpotId());
    assertEquals("marwan", session.getCreator());
    assertEquals(2, session.getNoControllers());
    assertEquals("10:00", session.getStartTime());
    assertEquals("12:00", session.getEndTime());
    assertEquals(120, session.getDuration());
    assertEquals(SessionState.RUNNING, session.getSessionState());
  }

  @Test
  void testBuilderWithOptionalFields() {
    Session session = new Session.Builder().sessionId(2).spotId(102).build();

    assertEquals(2, session.getSessionId());
    assertEquals(102, session.getSpotId());
    assertEquals(null, session.getCreator()); // Optional field, should be 0
    assertEquals(0, session.getNoControllers()); // Optional field, should be 0
    assertNull(session.getStartTime()); // Optional field, should be null
    assertNull(session.getEndTime()); // Optional field, should be null
    assertEquals(0, session.getDuration()); // Optional field, should be 0
    assertNull(session.getSessionState()); // Optional field, should be null
  }

  @Test
  void testBuilderValidation() {
    assertThrows(IllegalArgumentException.class,
        () -> new Session.Builder().sessionId(0)); // Invalid sessionId
    assertThrows(IllegalArgumentException.class,
        () -> new Session.Builder().spotId(-1)); // Invalid spotId
//    assertThrows(IllegalArgumentException.class,
//        () -> new Session.Builder().creator(null)); // Invalid adminId
    assertThrows(IllegalArgumentException.class,
        () -> new Session.Builder().noControllers(0)); // Invalid noControllers
  }
}