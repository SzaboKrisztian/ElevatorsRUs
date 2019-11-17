package com.krisztianszabo.config;

import com.krisztianszabo.model.DoorPanel;
import com.krisztianszabo.model.Elevator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class IniLoader implements ConfigurationLoadingStrategy {
  private final String headingPattern = "(\\[(.)*\\])";
  private final String elevatorPattern = "([0-9]+(: )(-?([0-9]+)(, )?)+)";
  private final String doorPanelPattern = "([0-9]+(: )(-?([0-9]+))(; )(([0-9])+(, )?)+)";
  private List<String> configFileData;
  private List<Elevator> elevators;
  private List<DoorPanel> doorPanels;
  private int[] floors;

  public IniLoader(String filename) {
    File iniFile = new File(filename);

    // Try loading the contents of the file into memory
    try {
      Path filePath = Paths.get(iniFile.getAbsolutePath());
      configFileData = Files.readAllLines(filePath, Charset.defaultCharset());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Cannot open configuration file.");
      System.exit(1);
    }

    // Extract and validate elevator data, then instantiate the Elevator objects
    List<String> elevatorsData = getSection("Elevators");
    if (validateSection(elevatorsData, elevatorPattern)) {
      this.elevators = instantiateElevators(elevatorsData);
    } else {
      System.out.println("Malformed elevators data. Aborting.");
      System.exit(1);
    }

    // Extract and validate door panel data, then instantiate the DoorPanel objects
    List<String> doorPanelsData = getSection("DoorPanels");
    if (validateSection(doorPanelsData, doorPanelPattern)) {
      this.doorPanels = instantiateDoorPanels(doorPanelsData);
    } else {
      System.out.println("Malformed door panels data. Aborting.");
      System.exit(1);
    }

    // Find the lowest and highest floors
    if (!findFloors()) {
      System.out.println("Elevators data missing. Could not compute lowest and highest floors. Aborting.");
      System.exit(1);
    }

    // Check if all the loaded data is consistent
    if (!areElevatorsValid()) {
      System.out.println("Duplicate elevator entries found. Aborting.");
      System.exit(1);
    }
    if (!areDoorPanelsValid()) {
      System.out.println("Duplicate door panels found, or door panels o");
    }
    System.out.println();
  }

  @Override
  public List<Elevator> getElevators() {
    return this.elevators;
  }

  @Override
  public List<DoorPanel> getDoorPanels() {
    return this.doorPanels;
  }

  @Override
  public int[] getFloors() {
    return floors;
  }

  private List<String> getSection(String title) {
    List<String> result = new ArrayList<>();
    boolean foundHeading = false;
    for (String currentLine : configFileData) {
      if (currentLine.strip().startsWith("[" + title + "]")) {
        foundHeading = true;
        continue;
      }
      if (foundHeading) {
        if (currentLine.strip().matches(headingPattern)) {
          break;
        } else {
          if (!currentLine.strip().isEmpty()) {
            result.add(currentLine.strip());
          }
        }
      }
    }
    return result;
  }

  private boolean validateSection(List<String> sectionLines, String regex) {
    for (String line : sectionLines) {
      if (!line.matches(regex)) {
        return false;
      }
    }
    return true;
  }

  private List<Elevator> instantiateElevators(List<String> elevatorsData) {
    List<Elevator> elevators = new ArrayList<>();
    for (String line : elevatorsData) {
      String[] splitLine = line.split(":");
      int id = Integer.parseInt(splitLine[0].strip());
      String[] floorStrings = splitLine[1].strip().split(",");
      int[] floors = new int[floorStrings.length];
      for (int i = 0; i < floorStrings.length; i++) {
        floors[i] = Integer.parseInt(floorStrings[i].strip());
      }
      elevators.add(new Elevator(id, floors));
    }

    return elevators;
  }

  private List<DoorPanel> instantiateDoorPanels(List<String> doorPanelsData) {
    List<DoorPanel> doorPanels = new ArrayList<>();
    for (String line : doorPanelsData) {
      String[] splitLine = line.split(":");
      int id = Integer.parseInt(splitLine[0].strip());
      String[] doorPanelData = splitLine[1].strip().split(";");
      int floor = Integer.parseInt(doorPanelData[0].strip());
      String[] elevatorStrings = doorPanelData[1].strip().split(",");
      int[] elevators = new int[elevatorStrings.length];
      for (int i = 0; i < elevatorStrings.length; i++) {
        elevators[i] = Integer.parseInt(elevatorStrings[i].strip());
      }
      doorPanels.add(new DoorPanel(id, floor, elevators));
    }

    return doorPanels;
  }

  private boolean findFloors() {
    if (elevators != null) {
      List<Integer> floors = new ArrayList<>();
      for (Elevator elevator : elevators) {
        for (int floor : elevator.getFloors()) {
          if (!floors.contains(floor)) {
            floors.add(floor);
          }
        }
      }

      int[] result = new int[floors.size()];
      for (int i = 0; i < floors.size(); i++) {
        result[i] = floors.get(i);
      }
      Arrays.sort(result);
      this.floors = result;
      return true;
    } else {
      return false;
    }
  }

  private boolean areElevatorsValid() {
    Set<Integer> elevatorIds = new HashSet<>();
    for (Elevator elevator : elevators) {
      elevatorIds.add(elevator.getId());
    }
    return elevatorIds.size() == elevators.size();
  }

  private boolean areDoorPanelsValid() {
    Set<Integer> doorPanelsIds = new HashSet<>();
    for (DoorPanel doorPanel : doorPanels) {
      // binarySearch returns negative numbers if the element is not found
      if (Arrays.binarySearch(this.floors, doorPanel.getFloor()) >= 0) {
        doorPanelsIds.add(doorPanel.getId());
      } else {
        return false;
      }
    }
    return doorPanelsIds.size() == doorPanels.size();
  }
}
