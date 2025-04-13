package com.gamingCoffee.database.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gamingCoffee.session.model.SessionState;
import com.gamingCoffee.session.model.Session;
import org.junit.jupiter.api.Test;

class SessionTest {

  @Test
  void shouldCreateValidSession() {
    Session session = new Session.Builder().sessionId(1).spotId(10).creator("Admin")
        .noControllers(2).startTime("2025-03-08 12:00").endTime("2025-03-08 14:00").duration(2.0)
        .sessionState(SessionState.DONE).sessionPrice(100.0).build();

    assertNotNull(session);
    assertEquals(1, session.getSessionId());
    assertEquals("Admin", session.getCreator());
    assertEquals(100.0, session.getSessionPrice());
    assertEquals(SessionState.DONE, session.getSessionState());
  }

  @Test
  void shouldThrowExceptionForNegativeSessionId() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Session.Builder().sessionId(-1).build());
    assertEquals("Session ID must be positive.", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionForNegativeDuration() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Session.Builder().duration(-1).build());
    assertEquals("Duration must be positive.", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionForNegativePrice() {
    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> new Session.Builder().sessionPrice(-50).build());
    assertEquals("Session price cannot be negative.", exception.getMessage());
  }

  @Test
  void shouldHaveConsistentEqualsAndHashCode() {
    Session session1 = new Session.Builder().sessionId(1).spotId(10).creator("Admin")
        .noControllers(2).sessionPrice(100.0).sessionState(SessionState.DONE).build();

    Session session2 = new Session.Builder().sessionId(1).spotId(10).creator("Admin")
        .noControllers(2).sessionPrice(100.0).sessionState(SessionState.DONE).build();

    assertEquals(session1, session2);
    assertEquals(session1.hashCode(), session2.hashCode());
  }
}