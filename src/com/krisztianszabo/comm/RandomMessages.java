package com.krisztianszabo.comm;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RandomMessages implements CommunicationStrategy {
  private static LocalTime lastMsg = LocalTime.now();

  @Override
  public List<String> getNewMessages() {
    if (LocalTime.now().isAfter(lastMsg.plusSeconds(5))) {
      List<String> result = new ArrayList<>();
      result.add("MSG P" + (int) (Math.random() * 16) + " " + ((int) (Math.random() * 2) == 0 ? "UP" : "DOWN"));
      return result;
    } else {
      return null;
    }
  }
}
