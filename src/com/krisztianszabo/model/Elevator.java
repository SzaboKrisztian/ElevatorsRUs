package com.krisztianszabo.model;

import java.util.Arrays;

public class Elevator {

  public enum State {
    IDLE,
    MOVING_UP,
    MOVING_DOWN,
    SERVICING_FLOOR
  }

  private int id;
  private int[] floors;
  private State state;
  private TravelDirection travelDirection;
  private int numTicksSinceChange;
  private int position;
  private boolean[] stopHereOnTheWayUp;
  private boolean[] stopHereOnTheWayDown;

  public Elevator(int id, int[] floors) {
    this.id = id;
    this.floors = floors;
    Arrays.sort(floors);
    this.position = floors[0] * 3;
    this.stopHereOnTheWayUp = new boolean[floors.length];
    this.stopHereOnTheWayDown = new boolean[floors.length];
    this.state = State.IDLE;
    this.travelDirection = null;
    this.numTicksSinceChange = 0;
  }

  public int getId() {
    return id;
  }

  public int[] getFloors() {
    return Arrays.copyOf(this.floors, this.floors.length);
  }

  public int getPosition() {
    return this.position;
  }

  public State getState() {
    return this.state;
  }

  public TravelDirection getTravelDirection() {
    return this.travelDirection;
  }

  public void askToStop(TravelDirection direction, int floor) {
    int floorIndex = Arrays.binarySearch(floors, floor);
    if (floorIndex >= 0) {
      if (direction.equals(TravelDirection.UP)) {
        stopHereOnTheWayUp[floorIndex] = true;
      } else {
        stopHereOnTheWayDown[floorIndex] = true;
      }
    }
  }

  public int howManyTicksAways(int floor) {
    int floorIndex;
    boolean found = false;
    for (floorIndex = 0; floorIndex < floors.length; floorIndex++) {
      if (floors[floorIndex] == floor) {
        found = true;
        break;
      }
    }
    if (!found) {
      return -1;
    } else {
      return countMinTicksBetween(floorIndex, travelDirection);
    }
  }

  // TODO This method is probably fucked up
  private int countMinTicksBetween(int destinationFloor, TravelDirection direction) {
    int result = 0;
    int currentFloor = position / 3;
    if (direction == TravelDirection.DOWN) {
      if (currentFloor >= destinationFloor) {
        for (int i = destinationFloor; i >= currentFloor; i--) {
          result += 3;
          result += stopHereOnTheWayDown[i] ? 2 : 0;
        }
        return result;
      } else {
        int lowestFloor = 0;
        while (!stopHereOnTheWayDown[lowestFloor]) {
          lowestFloor++;
        }
        for (int i = currentFloor; i >= lowestFloor; i--) {
          result += 3;
          result += stopHereOnTheWayDown[i] ? 2 : 0;
        }
        for (int i = lowestFloor; i <= destinationFloor; i++) {
          result += 3;
          result += stopHereOnTheWayUp[i] ? 2 : 0;
        }
      }
    } else if (direction == TravelDirection.UP) {
      if (currentFloor <= destinationFloor) {
        for (int i = currentFloor; i <= destinationFloor; i++) {
          result += 3;
          result += stopHereOnTheWayDown[i] ? 2 : 0;
        }
        return result;
      } else {
        int highestFloor = floors.length - 1;
        while (!stopHereOnTheWayDown[highestFloor]) {
          highestFloor--;
        }
        for (int i = currentFloor; i <= highestFloor; i++) {
          result += 3;
          result += stopHereOnTheWayDown[i] ? 2 : 0;
        }
        for (int i = highestFloor; i >= destinationFloor; i--) {
          result += 3;
          result += stopHereOnTheWayUp[i] ? 2 : 0;
        }
      }
    } else {
      result = Math.abs(currentFloor - destinationFloor) * 3;
    }

    return result;
  }

  public void update() {
    numTicksSinceChange += 1;
    switch (state) {
      case IDLE:
        break;
      case MOVING_DOWN:
        position -= 1;
        if (isOnFloor() && shouldStopAtCurrentFloor()) {
          changeState(State.SERVICING_FLOOR);
        }
        break;
      case MOVING_UP:
        position += 1;
        if (isOnFloor() && shouldStopAtCurrentFloor()) {
          changeState(State.SERVICING_FLOOR);
        }
        break;
      case SERVICING_FLOOR:
        if (numTicksSinceChange == 2) {
          if (!areThereRequests()) {
            changeState(State.IDLE);
            travelDirection = null;
          } else if (travelDirection.equals(TravelDirection.UP)) {
            if (areThereRequestsAbove()) {
              changeState(State.MOVING_UP);
            }
          } else if (travelDirection.equals(TravelDirection.DOWN)) {
            if (areThereRequestsBelow()) {
              changeState(State.MOVING_DOWN);
            }
          }
        }
        break;
    }
  }

  private void changeState(State newState) {
    this.state = newState;
    numTicksSinceChange = 0;
  }

  private boolean areThereRequests() {
    for (int i = 0; i < stopHereOnTheWayDown.length; i++) {
      if (stopHereOnTheWayDown[i] || stopHereOnTheWayUp[i]) {
        return true;
      }
    }
    return false;
  }

  private boolean areThereRequestsAbove() {
    for (int i = position / 3; i < stopHereOnTheWayDown.length; i++) {
      if (stopHereOnTheWayDown[i] || stopHereOnTheWayUp[i]) {
        return true;
      }
    }
    return false;
  }

  private boolean areThereRequestsBelow() {
    for (int i = position / 3; i >= 0; i--) {
      if (stopHereOnTheWayUp[i] || stopHereOnTheWayDown[i]) {
        return true;
      }
    }
    return false;
  }

  private boolean shouldStopAtCurrentFloor() {
    if (state == State.MOVING_UP) {
      return stopHereOnTheWayUp[position / 3];
    } else {
      return stopHereOnTheWayDown[position / 3];
    }
  }

  private boolean isOnFloor() {
    return position % 3 == 0;
  }

  @Override
  public String toString() {
    return "ID: " + this.id + " position: " + position;
  }
}
