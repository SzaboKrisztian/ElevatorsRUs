package com.krisztianszabo.model;

import java.util.Arrays;

public class DoorPanel {
  private int id;
  private int floor;
  private int[] elevators;
  private String toString;

  public DoorPanel(int id, int floor, int[] elevators) {
    this.id = id;
    this.floor = floor;
    this.elevators = elevators;
    Arrays.sort(elevators);
    this.toString = generateToString();
  }

  public int getId() {
    return this.id;
  }

  public int getFloor() {
    return this.floor;
  }

  public int[] getElevators() {
    return this.elevators;
  }

  private String generateToString() {
    String result = "I'm door panel ID#" + id + ", on floor " + floor + ", communicating with elevators: ";
    for (int elevator : elevators) {
      result = result.concat(Integer.toString(elevator).concat(", "));
    }
    result = result.substring(0, result.lastIndexOf(", "));
    return result;
  }

  @Override
  public String toString() {
    return this.toString;
  }
}
