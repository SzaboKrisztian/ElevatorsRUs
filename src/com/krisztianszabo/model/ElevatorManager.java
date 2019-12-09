package com.krisztianszabo.model;

import java.util.ArrayList;
import java.util.Arrays;
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

  public void update() {
    for (Elevator elevator : elevators) {
      elevator.update();
    }
  }

  public void sendMessage(String message) {
    message = message.toUpperCase();
    if (message.startsWith("MSG ")) {
      message = message.substring(4);
      String[] msgComponents = message.split(" ");
      if (msgComponents[0].startsWith("P")) {
        int panelId = Integer.parseInt(msgComponents[0].substring(1));
        if (msgComponents[1].equals("UP")) {
          callElevator(panelId, TravelDirection.UP);
        } else if (msgComponents[1].equals("DOWN")) {
          callElevator(panelId, TravelDirection.DOWN);
        }
      } else if (msgComponents[0].startsWith("E")) {
        int elevatorId = Integer.parseInt(msgComponents[0].substring(1));
      }
    }
  }

  public List<Elevator> getElevators() {
    return elevators;
  }

  private void callElevator(int doorPanelId, TravelDirection direction) {
    DoorPanel currentDoorPanel = null;
    for (DoorPanel doorPanel : doorPanels) {
      if (doorPanel.getId() == doorPanelId) {
        currentDoorPanel = doorPanel;
      }
    }
    if (currentDoorPanel != null) {
      List<Elevator> associatedElevators = new ArrayList<>();
      for (Elevator elevator : elevators) {
        if (Arrays.binarySearch(currentDoorPanel.getElevators(), elevator.getId()) >= 0) {
          associatedElevators.add(elevator);
        }
      }
      // TODO I'm missing the travelling direction in the howmanyticksaway method...
      // Determine tick distance for all elevators
      int[] distances = new int[associatedElevators.size()];
      for (int i = 0; i < associatedElevators.size(); i++) {
        distances[i] = associatedElevators.get(i).howManyTicksAways(currentDoorPanel.getFloor());
      }
      // Find idle elevator

      // If not, just assign the elevator that'll most likely get there first
    }
  }
}
