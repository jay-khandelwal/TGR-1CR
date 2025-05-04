package LLD.ParkingLot.V1.Mine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum SpotType {
    TWO_WHEELER(2), FOUR_WHEELER(4), EIGHT_WHEELER(8);

    private final int value;

    SpotType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

enum VehicleType {
    TWO_WHEELER(2), FOUR_WHEELER(4), EIGHT_WHEELER(8);

    private final int value;

    VehicleType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

class Vehicle {
    private final String vehicleNumber;
    private final VehicleType vehicleType;

    public Vehicle(String vehicleNumber, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}
class ParkingSpot {
    private final String spotNumber;
    private SpotType spotType;

    public ParkingSpot(String spotNumber, SpotType spotType) {
        this.spotNumber = spotNumber;
        this.spotType = spotType;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public SpotType getSpotType() {
        return spotType;
    }
}
// class Floor {
//     // Should we keep parking spot list here or, somewhere seperatly
//     List<ParkingSpot> spots = new ArrayList<>();

//     public void addSpot(String spotNumber, SpotType spotType) {
//         ParkingSpot spot = new ParkingSpot(spotNumber, spotType);
//         spots.add(spot);
//     }
// }

class Floor {
    private final int floorNumber;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getFloorNumber() {
        return this.floorNumber;
    }
}

class Ticket {
    private static int counter = 0;

    private final String ticketId;
    private final ParkingSpot spot;
    private final Vehicle vehicle;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOurTime;

    public Ticket(ParkingSpot spot, Vehicle vehicle) {
        this.ticketId = "TCK-" + ++counter;
        this.spot = spot;
        this.vehicle = vehicle;
        this.checkInTime = LocalDateTime.now();
    }

    public String getTicketId() {
        return ticketId;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public LocalDateTime getCheckOurTime() {
        return checkOurTime;
    }

    public void setCheckOurTime(LocalDateTime checkOurTime) {
        this.checkOurTime = checkOurTime;
    }
}

interface FindSpotStrategy {
    ParkingSpot findSpot(VehicleType vehicleType);
}
interface ParkingSpotService {
    void addSpot(String spotNumber, SpotType spotType);
    void removeSpot(String spotNumber);
    List<ParkingSpot> getParkingSpots();
}
interface VehicleService {
    Vehicle addOrGetVehicle(String vehicleNumber, VehicleType vehicleType);
    void removeVehicle(String vehicleNumber);
}
interface FloorService {
    List<ParkingSpot> getParkingSpots();
    List<ParkingSpot> getParkingSpots(int floorNumber);
    void addFloor(int floorNumber, ParkingSpotService parkingSpotService);
}
interface TicketService {
    Ticket createTicket(ParkingSpot spot, Vehicle vehicle);
    List<Ticket> getActiveTickets();
    List<Ticket> getInActiveTickets();
    boolean hasActiveTicket(ParkingSpot spot);
    boolean hasActiveTicket(Vehicle vehicle);
    int getParkingCost(Ticket ticket);
    void paymentAcceptedCallback(String ticketId);
}
interface PaymentStrategy {
    void acceptPayment(Ticket ticket, int amount);
}

class BasicFindSpotStrategy implements FindSpotStrategy {
    private FloorService floorService;
    private TicketService ticketService;

    public BasicFindSpotStrategy(FloorService floorService, TicketService ticketService) {
        this.floorService = floorService;
        this.ticketService = ticketService;
    }

    @Override
    public ParkingSpot findSpot(VehicleType vehicleType) {
        List<ParkingSpot> spots =  floorService.getParkingSpots();
        for (ParkingSpot spot: spots) {
            if (spot.getSpotType().getValue() == vehicleType.getValue() && !ticketService.hasActiveTicket(spot)) {
                return spot;
            }
        }
        return null;
    }
}

class InMemoryParkingSpotService implements ParkingSpotService {
    Map<String, ParkingSpot> spots = new HashMap<>();

    @Override
    public void addSpot(String spotNumber, SpotType spotType) {
        ParkingSpot spot = new ParkingSpot(spotNumber, spotType);
        spots.put(spotNumber, spot);
    }

    @Override
    public void removeSpot(String spotNumber) {
        if (spots.containsKey(spotNumber)) {
            spots.remove(spotNumber);
        }
    }

    @Override
    public List<ParkingSpot> getParkingSpots() {
        return new ArrayList<>(spots.values());
    }
}

class InMemoryVehicleService implements VehicleService {
    Map<String, Vehicle> vehicles = new HashMap<>();

    @Override
    public Vehicle addOrGetVehicle(String vehicleNumber, VehicleType vehicleType) {
        vehicles.putIfAbsent(vehicleNumber, new Vehicle(vehicleNumber, vehicleType));
        return vehicles.get(vehicleNumber);
    }

    @Override
    public void removeVehicle(String vehicleNumber) {
        if (vehicles.containsKey(vehicleNumber)) {
            vehicles.remove(vehicleNumber);
        }
    } 
}

class InMemoryFloorService implements FloorService {
    Map<Integer, ParkingSpotService> floors =  new HashMap<>();

    @Override
    public List<ParkingSpot> getParkingSpots(int floorNumber) {
        if (!floors.containsKey(floorNumber)) {
            throw new IllegalArgumentException("Invalid Floor number.");
        }
        ParkingSpotService parkingSpotService = floors.get(floorNumber);
        return parkingSpotService.getParkingSpots();
    }

    @Override
    public List<ParkingSpot> getParkingSpots() {
        List<ParkingSpot> allSpots = new ArrayList<>();

        for (Map.Entry<Integer, ParkingSpotService> entry: floors.entrySet()) {
            allSpots.addAll(entry.getValue().getParkingSpots());
        }

        return allSpots;
    }

    @Override
    public void addFloor(int floorNumber, ParkingSpotService parkingSpotService) {
        floors.put(floorNumber, parkingSpotService);
    }
    
}

class InMemoryTicketService implements TicketService {
    Map<String, Ticket> activeTickets = new HashMap<>();
    Map<String, Ticket> inActiveTickets = new HashMap<>();

    Map<ParkingSpot, Ticket> ticketBySpotMap = new HashMap<>();
    Map<Vehicle, Ticket> ticketByVehicleMap = new HashMap<>();


    @Override
    public Ticket createTicket(ParkingSpot spot, Vehicle vehicle) {
        if (hasActiveTicket(vehicle)) {
            throw new IllegalArgumentException("Provided vehicle is already parked at some other spot.");
        } else if (hasActiveTicket(spot)) {
            throw new IllegalArgumentException("Provided Spot is already in used by other vehicle.");
        }
        Ticket ticket = new Ticket(spot, vehicle);
        activeTickets.put(ticket.getTicketId(), ticket);
        handleCreateTicketEvent(ticket, spot, vehicle);
        return ticket;
    }

    @Override
    public List<Ticket> getActiveTickets() {
        return new ArrayList<>(activeTickets.values());
    }

    @Override
    public List<Ticket> getInActiveTickets() {
        return new ArrayList<>(inActiveTickets.values());
    }

    @Override
    public boolean hasActiveTicket(ParkingSpot spot) {
        return ticketBySpotMap.containsKey(spot);
    }

    @Override
    public boolean hasActiveTicket(Vehicle vehicle) {
        return ticketByVehicleMap.containsKey(vehicle);
    }

    @Override
    public int getParkingCost(Ticket ticket) {
        if (!activeTickets.containsKey(ticket.getTicketId())) {
            throw new IllegalArgumentException("Not a valid ticket ID.");
        }
        System.out.println("Ticket cost of vehicle: " + ticket.getVehicle().getVehicleNumber() + " is Rs." + 100);
        return 100;
    }

    private void handleCreateTicketEvent(Ticket ticket, ParkingSpot spot, Vehicle vehicle) {
        ticketBySpotMap.put(spot, ticket);
        ticketByVehicleMap.put(vehicle, ticket);
        System.out.println("Parket vehicle: " + vehicle.getVehicleNumber() + " successfully at spot: " + spot.getSpotNumber());
    }

    @Override
    public void paymentAcceptedCallback(String ticketId) {
        Ticket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Not a valid ticket ID.");
        }
        activeTickets.remove(ticketId);
        ticketBySpotMap.remove(ticket.getSpot());
        ticketByVehicleMap.remove(ticket.getVehicle());

        ticket.setCheckOurTime(LocalDateTime.now());

        inActiveTickets.put(ticketId, ticket);
    }
    
}

class BasicPaymentStrategy implements PaymentStrategy {
    TicketService ticketService;

    public BasicPaymentStrategy(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void acceptPayment(Ticket ticket, int amount) {
        int expectedAmount = ticketService.getParkingCost(ticket);
        if (expectedAmount == amount) {
            System.out.println("Payment accepted: " + amount + " successfully against ticket ID: " + ticket.getTicketId());
            ticketService.paymentAcceptedCallback(ticket.getTicketId());
        } else {
            System.out.println("Please give amount: " + expectedAmount + " for ticket: " + ticket.getTicketId());
        }
    }
    
}

class ParkingLotFacade {
    private VehicleService vehicleService;
    private FloorService floorService;
    private TicketService ticketService;
    private FindSpotStrategy findSpotStrategy;
    private PaymentStrategy paymentService;

    public ParkingLotFacade() {
        this.vehicleService = new InMemoryVehicleService();
        this.floorService = new InMemoryFloorService();
        this.ticketService = new InMemoryTicketService();
        this.findSpotStrategy = new BasicFindSpotStrategy(floorService, ticketService);
        this.paymentService = new BasicPaymentStrategy(ticketService);

        addFloor();
    }

    private void addFloor() {
        for (int i=0; i<3; i++) {
            ParkingSpotService parkingSpotService = new InMemoryParkingSpotService();
            String floorPrefix = "FLR-" + i;
            parkingSpotService.addSpot(floorPrefix+"1" , SpotType.TWO_WHEELER);
            parkingSpotService.addSpot(floorPrefix+"2" , SpotType.TWO_WHEELER);
            parkingSpotService.addSpot(floorPrefix+"3" , SpotType.FOUR_WHEELER);
            parkingSpotService.addSpot(floorPrefix+"4" , SpotType.FOUR_WHEELER);
            parkingSpotService.addSpot(floorPrefix+"5" , SpotType.FOUR_WHEELER);
            parkingSpotService.addSpot(floorPrefix+"6" , SpotType.EIGHT_WHEELER);
            parkingSpotService.addSpot(floorPrefix+"7" , SpotType.EIGHT_WHEELER);
            floorService.addFloor(i, parkingSpotService);
        }
    }

    public Ticket parkVehicle(String vehicleNumber, VehicleType vehicleType) {
        Vehicle vehicle = vehicleService.addOrGetVehicle(vehicleNumber, vehicleType);
        ParkingSpot spot = findSpotStrategy.findSpot(vehicleType);
        if (spot == null) {
            System.out.println("Vehicle not found");
            return null;
        }
        Ticket ticket = ticketService.createTicket(spot, vehicle);
        return ticket;
    }

    public int getParkingCost(Ticket ticket) {
        return ticketService.getParkingCost(ticket);
    }

    public void pay(Ticket ticket, int amount) {
        paymentService.acceptPayment(ticket, amount);
    }
}


public class ParkingLotSystem {
    public static void main(String[] args) {
        ParkingLotFacade parkingLot = new ParkingLotFacade();

        Ticket ticket = parkingLot.parkVehicle("DL 1001", VehicleType.TWO_WHEELER);
        // parkingLot.parkVehicle("DL 1001", VehicleType.TWO_WHEELER);
        int parkingCost = parkingLot.getParkingCost(ticket);
        parkingLot.pay(ticket, parkingCost);
    }
}