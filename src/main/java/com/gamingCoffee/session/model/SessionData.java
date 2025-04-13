package com.gamingCoffee.session.model;

import com.gamingCoffee.spot.model.ConsoleType;
import com.gamingCoffee.spot.model.SpotType;

public record SessionData(int sessionId, int noController, double duration, SpotType spotType,
                          ConsoleType consoleType) {

}
