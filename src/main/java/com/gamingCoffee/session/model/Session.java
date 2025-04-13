package com.gamingCoffee.session.model;

import java.util.Objects;

public class Session {

  // Required fields (optional in the builder)
  private final int sessionId;
  private final int spotId;
  private final int noControllers;
  private final String creator;
  private final String startTime;
  private final String endTime;
  private final SessionState sessionState;
  private final double duration;
  private final double sessionPrice;

  // Private constructor to enforce the use of the Builder
  private Session(Builder builder) {
    this.sessionId = builder.sessionId;
    this.spotId = builder.spotId;
    this.creator = builder.creator;
    this.noControllers = builder.noControllers;
    this.startTime = builder.startTime;
    this.endTime = builder.endTime;
    this.duration = builder.duration;
    this.sessionState = builder.sessionState;
    this.sessionPrice = builder.sessionPrice;
  }

  // Getters
  public int getSessionId() {
    return sessionId;
  }

  public int getSpotId() {
    return spotId;
  }

  public String getCreator() {
    return creator;
  }

  public int getNoControllers() {
    return noControllers;
  }

  public String getStartTime() {
    return startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public double getDuration() {
    return duration;
  }

  public SessionState getSessionState() {
    return sessionState;
  }

  public double getSessionPrice() {
    return sessionPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Session session)) {
      return false;
    }
    return getSessionId() == session.getSessionId() && getSpotId() == session.getSpotId()
        && getNoControllers() == session.getNoControllers()
        && Double.compare(getDuration(), session.getDuration()) == 0
        && Double.compare(getSessionPrice(), session.getSessionPrice()) == 0 && Objects.equals(
        getCreator(), session.getCreator()) && Objects.equals(getStartTime(),
        session.getStartTime()) && Objects.equals(getEndTime(), session.getEndTime())
        && getSessionState() == session.getSessionState();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSessionId(), getSpotId(), getNoControllers(), getCreator(),
        getStartTime(), getEndTime(), getSessionState(), getDuration(), getSessionPrice());
  }

  @Override
  public String toString() {
    return "Session{" + "sessionId=" + sessionId + ", spotId=" + spotId + ", creator=" + creator
        + ", noControllers=" + noControllers + ", startTime='" + startTime + '\'' + ", endTime='"
        + endTime + '\'' + ", sessionState=" + sessionState + ", duration=" + duration
        + ", sessionPrice=" + sessionPrice + '}';
  }

  // Builder class
  public static class Builder {

    // Optional fields with default values
    private int sessionId = 0; // Default value
    private int spotId = 0; // Default value
    private String creator = null; // Default value
    private int noControllers = 0; // Default value
    private String startTime = null; // Default value
    private String endTime = null; // Default value
    private SessionState sessionState = null; // Default value
    private double duration = 0.0; // Default value
    private double sessionPrice = 0.0; // Default value

    // Methods to set optional fields
    public Builder sessionId(int sessionId) {
      if (sessionId <= 0) {
        throw new IllegalArgumentException("Session ID must be positive.");
      }
      this.sessionId = sessionId;
      return this;
    }

    public Builder spotId(int spotId) {
      if (spotId <= 0) {
        throw new IllegalArgumentException("Spot ID must be positive.");
      }
      this.spotId = spotId;
      return this;
    }

    public Builder creator(String creator) {
      this.creator = creator;
      return this;
    }

    public Builder noControllers(int noControllers) {
      if (noControllers < 1) {
        throw new IllegalArgumentException("Number of controllers cannot be less than 1.");
      }
      this.noControllers = noControllers;
      return this;
    }

    public Builder startTime(String startTime) {
      this.startTime = startTime;
      return this;
    }

    public Builder endTime(String endTime) {
      this.endTime = endTime;
      return this;
    }

    public Builder duration(double duration) {
      if (duration <= 0) {
        throw new IllegalArgumentException("Duration must be positive.");
      }
      this.duration = duration;
      return this;
    }

    public Builder sessionState(SessionState sessionState) {
      this.sessionState = sessionState;
      return this;
    }

    public Builder sessionPrice(double sessionPrice) {
      if (sessionPrice <= 0) {
        throw new IllegalArgumentException("Session price cannot be negative.");
      }
      this.sessionPrice = sessionPrice;
      return this;
    }

    // Build method to create the Session object
    public Session build() {
      return new Session(this);
    }
  }
}