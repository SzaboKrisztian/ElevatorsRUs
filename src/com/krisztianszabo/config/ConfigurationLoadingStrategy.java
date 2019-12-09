package com.krisztianszabo.config;

import com.krisztianszabo.model.DoorPanel;
import com.krisztianszabo.model.Elevator;

import java.util.List;

public interface ConfigurationLoadingStrategy {
  List<DoorPanel> getDoorPanels();

  List<Elevator> getElevators();

  int[] getFloors();
}
