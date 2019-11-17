package com.krisztianszabo.model;

import java.util.List;

public class ElevatorManager {
  private List<Elevator> elevators;
  private List<DoorPanel> doorPanels;
  private int[] floors;

  public ElevatorManager(List<Elevator> elevators, List<DoorPanel> doorPanels, int[] floors) {
    this.elevators = elevators;
    this.doorPanels = doorPanels;
    this.floors = floors;
  }
}
