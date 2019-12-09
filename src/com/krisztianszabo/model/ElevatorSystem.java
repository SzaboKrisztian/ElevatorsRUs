package com.krisztianszabo.model;

import com.krisztianszabo.comm.CommunicationStrategy;
import com.krisztianszabo.comm.RandomMessages;
import com.krisztianszabo.config.ConfigurationLoadingStrategy;
import com.krisztianszabo.config.IniLoader;
import com.krisztianszabo.view.ConsoleOutput;
import com.krisztianszabo.view.DrawingStrategy;

import java.time.LocalTime;
import java.util.List;

public class ElevatorSystem {
  private ConfigurationLoadingStrategy configLoader;
  private ElevatorManager elevatorManager;
  private CommunicationStrategy communicationStrategy;
  private DrawingStrategy drawingStrategy;

  public ElevatorSystem() {
    configLoader = new IniLoader("config.ini");
    elevatorManager = new ElevatorManager(configLoader.getElevators(),
        configLoader.getDoorPanels(), configLoader.getFloors());
    communicationStrategy = new RandomMessages();
    drawingStrategy = new ConsoleOutput(elevatorManager.getElevators());
  }

  public void start() {
    LocalTime lastTick = LocalTime.now();
    while (true) {
      if (LocalTime.now().isAfter(lastTick.plusSeconds(1))) {
        lastTick = LocalTime.now();
        elevatorManager.update();
        drawingStrategy.draw();
      }
      List<String> messages = communicationStrategy.getNewMessages();
      if (messages != null) {
        for (String message : messages) {
          elevatorManager.sendMessage(message);
        }
      }
      try {
        Thread.sleep(33);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
