package com.krisztianszabo.view;

import com.krisztianszabo.model.Elevator;

import java.util.List;

public class ConsoleOutput implements DrawingStrategy {
  private List<Elevator> elevators;

  public ConsoleOutput(List<Elevator> elevators) {
    this.elevators = elevators;
  }

  @Override
  public void draw() {
    for (Elevator elevator : elevators) {
      System.out.println(elevator);
    }
  }
}
