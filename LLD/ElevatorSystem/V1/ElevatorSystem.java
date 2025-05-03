package LLD.ElevatorSystem.V1;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

enum ElevatorState { RUNNING, CLOSED, MAINTENANCE }
enum Direction {UP, DOWN, IDLE}

class Elevator {
    private final String id;
    private int currFloor;
    private ElevatorState currState;
    private Direction currDirection;
    private TreeSet<Integer> upRequests;
    private TreeSet<Integer> downRequests;

    private final int DOOR_OPEN_DURATION = 500;
    private ExecutorService worker = Executors.newSingleThreadExecutor();

    public Elevator(String id, int currFloor, ElevatorState state) {
        this.id = id;
        this.currFloor = currFloor;
        this.currState = state;
        this.currDirection = Direction.IDLE;
        upRequests = new TreeSet<>();
        downRequests = new TreeSet<>((a, b) -> b-a);
    }

    public String getId() {
        return id;
    }

    public int getCurrFloor() {
        return currFloor;
    }

    public ElevatorState getCurrState() {
        return currState;
    }

    public Direction getCurrDirection() {
        return currDirection;
    }

    private void setCurrDirection(Direction direction) {
        currDirection = direction;
    }

    private void setCurrState(ElevatorState state) {
        currState = state;
    }

    public synchronized void addRequest(int destination) {
        Direction requestDirection = currFloor < destination ? Direction.UP: Direction.DOWN;
        if (requestDirection == Direction.UP) {
            upRequests.add(destination);
        } else {
            downRequests.add(destination);
        }
        notifyAll();
    }

    public void start() {
        Runnable task = () -> {
            try {
                run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger("Elevator interrupted: " + e.getMessage());
            }
        };
        worker.execute(task);
    }

    public synchronized void stop() {
        setCurrState(ElevatorState.CLOSED);
        notifyAll();
        worker.shutdown();
    }

    private synchronized void run() throws InterruptedException {
        while (currState == ElevatorState.RUNNING) {
            if (currState == ElevatorState.RUNNING && upRequests.isEmpty() && downRequests.isEmpty()) {
                setCurrDirection(Direction.IDLE);
                wait();
            }
            while (!upRequests.isEmpty() || !downRequests.isEmpty()) {
                processRequest();
            }
        }
        setCurrDirection(Direction.IDLE);
    }

    private void processRequest() throws InterruptedException {
        if (upRequests.isEmpty() && downRequests.isEmpty()) return;

        if (currDirection == Direction.UP && upRequests.isEmpty()) {
            setCurrDirection(Direction.DOWN);
        } else if (currDirection == Direction.DOWN && downRequests.isEmpty()) {
            setCurrDirection(Direction.UP);
        } else if (currDirection == Direction.IDLE) {
            // If curr direction is `IDLE` then setting currect direction based on in which direction we have higher request
            if (upRequests.size() > downRequests.size()) {
                setCurrDirection(Direction.UP);
            } else {
                setCurrDirection(Direction.DOWN);
            }
        } 
        // else {
        //     throw new IllegalArgumentException("Invalid Request; Elevator ID: " + getId() + " Current Floor: " + getCurrFloor());
        // }

        TreeSet<Integer> requests = currDirection == Direction.UP? upRequests: downRequests;

        while (!requests.isEmpty()) {
            int nextFloor = requests.first();
            if (currFloor == nextFloor) {
                requests.remove(nextFloor);
                logger("Reached floor: " + nextFloor);
                handleReachedFloor(requests, nextFloor);
            } else {
                logger("Current Floor: " + getCurrFloor() + " Current Direction: " + getCurrDirection().toString() +  " | Moving to floor: " + nextFloor);
                if (currDirection == Direction.UP) {
                    currFloor++;
                } else {
                    currFloor--;
                }
                handleReachingFloor(nextFloor);
            }
        }
    }
    
    private void handleReachingFloor(int nextFloor) {}

    private void handleReachedFloor(TreeSet<Integer> requests, int nextFloor) throws InterruptedException {
        handleDoorOpen();
        handleDoorClose();
    }

    private void handleDoorOpen() throws InterruptedException {
        logger("Door Opened");
        Thread.sleep(DOOR_OPEN_DURATION);
    }

    private void handleDoorClose() {
        logger("Door Closed");
    }

    private void logger(Object obj) {
        System.out.println("[Elevator: " + getId() + "]: " + obj);
    }
}

interface ElevatorFinder {
    Elevator find(int floor, List<Elevator> elevators);
}

class BasicElevatorFinder implements ElevatorFinder {

    @Override
    public Elevator find(int floor, List<Elevator> elevators) {
        for (Elevator elevator: elevators) {
            if (elevator.getCurrState() == ElevatorState.RUNNING) {
                return elevator;
            }
        }
        return null;
    }

}

class AvailableElevatorFinder implements ElevatorFinder {
    @Override
    public Elevator find(int floor, List<Elevator> elevators) {
        Elevator optimalElevator = null;

        for (Elevator elevator: elevators) {
            if (elevator.getCurrState() == ElevatorState.RUNNING) {
                if (optimalElevator == null) optimalElevator = elevator;
                else if (elevator.getCurrDirection() == Direction.IDLE) {
                    return elevator;
                }
            }
        }
        return optimalElevator;
    }
}


class ElevatorController {
    ElevatorFinder elevatorFinder;
    private List<Elevator> elevators;
    private int floorStart;
    private int floorEnd;

    public ElevatorController(ElevatorFinder elevatorFinder, int floorStart, int floorEnd) {
        this.elevatorFinder = elevatorFinder;
        elevators = new ArrayList<>();
        this.floorStart = floorStart;
        this.floorEnd = floorEnd;
    }

    public Elevator addElevator(String id, int currFloor, ElevatorState state) {
        Elevator elevator = new Elevator(id, currFloor, state);
        elevators.add(elevator);
        return elevator;
    }

    public Elevator requestElevatorInternal(Elevator elevator, int destination) throws IllegalAccessException {
        if (!isValidFloorRequested(destination)) {
            throw new IllegalAccessException("An error");
        }
        elevator.addRequest(destination);
        return elevator;
    }

    public Elevator requestElevatorExternal(int floor) throws IllegalAccessException {
        if (!isValidFloorRequested(floor)) {
            throw new IllegalAccessException("An error");
        }
        Elevator elevator = elevatorFinder.find(floor, elevators);
        elevator.addRequest(floor);
        return elevator;
    }

    public void start() {
        for (Elevator elevator: elevators) {
            elevator.start();
        }
    }

    public void stop() {
        for (Elevator elevator: elevators) {
            elevator.stop();
        }
    }

    private boolean isValidFloorRequested(int floor) {
        return floorStart <= floor && floor <= floorEnd;
    }
}

public class ElevatorSystem {
    public static void main(String[] args) throws IllegalAccessException, InterruptedException {
        // ElevatorFinder basicElevatorFinder = new BasicElevatorFinder();
        ElevatorFinder idleOrActiveElevatorFinder = new AvailableElevatorFinder();
        ElevatorController controller = new ElevatorController(idleOrActiveElevatorFinder, 0, 10);

        controller.addElevator("Elv-1", 0, ElevatorState.RUNNING);
        controller.addElevator("Elv-2", 0, ElevatorState.RUNNING);

        controller.start();

        Elevator elevator = controller.requestElevatorExternal(1);
        controller.requestElevatorInternal(elevator, 9);

        Thread.sleep(1000);
        
        elevator = controller.requestElevatorExternal(1);
        controller.requestElevatorInternal(elevator, 0);
        controller.requestElevatorInternal(elevator, 3);
        
        Thread.sleep(1000);

        controller.stop();
    }
}
