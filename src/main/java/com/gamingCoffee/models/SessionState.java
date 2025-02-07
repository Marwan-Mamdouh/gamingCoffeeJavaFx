package com.gamingCoffee.models;

public enum SessionState {
  RUNNING, // duration = ROUND((JULIANDAY(NEW.end_time) - JULIANDAY(NEW.start_time)) * 1440) -- Convert days to minutes
  DONE
}
