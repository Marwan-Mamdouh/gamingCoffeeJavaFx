package com.gamingCoffee.services;

import com.gamingCoffee.models.ConsoleType;
import com.gamingCoffee.models.SpotType;

public record SessionData(int sessionId, int noController, double duration,
                          SpotType spotType, ConsoleType consoleType) {

}
