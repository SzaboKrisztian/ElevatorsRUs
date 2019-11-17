package com.krisztianszabo.model;

import com.krisztianszabo.config.ConfigurationLoadingStrategy;
import com.krisztianszabo.config.IniLoader;

public class ElevatorSystem {
  private ConfigurationLoadingStrategy configLoader;
  private ElevatorManager elevatorManager;

  public ElevatorSystem() {
    configLoader = new IniLoader("config.ini");
    elevatorManager = new ElevatorManager(configLoader.getElevators(),
        configLoader.getDoorPanels(), configLoader.getFloors());
  }

  public void start() {
    while (true) {

    }
  }
}
