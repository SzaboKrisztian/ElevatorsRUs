package com.krisztianszabo.config;

import com.krisztianszabo.model.DoorPanel;
import com.krisztianszabo.model.Elevator;

public class ConfigurationLoader {
  private static ConfigurationLoadingStrategy loadingStrategy = new IniLoader("config.ini");

  public static void main(String[] args) {
    System.out.println(loadingStrategy.getElevators().size() + " elevators:");
    for (Elevator elevator : loadingStrategy.getElevators()) {
      System.out.println(elevator);
    }
    System.out.println(loadingStrategy.getDoorPanels().size() + " door panels:");
    for (DoorPanel doorPanel : loadingStrategy.getDoorPanels()) {
      System.out.println(doorPanel);
    }
    System.out.println(loadingStrategy.getFloors().length + " floors serviced:");
    String floors = "";
    for (int floor : loadingStrategy.getFloors()) {
      floors = floors.concat(String.valueOf(floor).concat(", "));
    }
    floors = floors.substring(0, floors.lastIndexOf(", "));
    System.out.println(floors);
  }
}
