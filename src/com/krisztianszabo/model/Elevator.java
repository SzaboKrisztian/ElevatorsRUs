package com.krisztianszabo.model;

import java.util.Arrays;

public class Elevator {
  private int id;
  private int[] floors;
  private String toString;

  public Elevator(int id, int[] floors) {
    this.id = id;
    this.floors = floors;
    Arrays.sort(floors);
    this.toString = generateToString();
  }

  public int getId() {
    return id;
  }

  public int[] getFloors() {
    return Arrays.copyOf(this.floors, this.floors.length);
  }

  private String generateToString() {
    String result = "Elevator ID#" + id + ", travels to floors: ";
    for (int floor : floors) {
      result = result.concat(Integer.toString(floor).concat(", "));
    }
    result = result.substring(0, result.lastIndexOf(", "));
    return result;
  }

  @Override
  public String toString() {
    return this.toString;
  }
}
