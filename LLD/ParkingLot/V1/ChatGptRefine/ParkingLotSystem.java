package LLD.ParkingLot.V1.ChatGptRefine;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

// Enums

enum VehicleType {
    TWO_WHEELER(1), FOUR_WHEELER(2), EIGHT_WHEELER(3);
    private final int value;
    VehicleType(int value) { this.value = value; }
    public int getValue() { return value; }
}

enum SpotType {
    TWO_WHEELER(1), FOUR_WHEELER(2), EIGHT_WHEELER(3);
    private final int value;
    SpotType(int value) { this.value = value; }
    public int getValue() { return value; }

    public boolean isCompatibleWith(VehicleType type) {
        return this.value >= type.getValue();
    }
}

// Models

class Vehicle {
    private String id;
    private VehicleType vehicleType;

    public Vehicle(String id, VehicleType vehicleType) {
        this.id = id;
        this.vehicleType = vehicleType;
    }

    public String getId() { return id; }
    public VehicleType getVehicleType() { return vehicleType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Vehicle{id='" + id + "', type=" + vehicleType + "}";
    }
}

class ParkingSpot {
    private final int floorNo;
    private final int spotNo;
    private final SpotType spotType;

    public ParkingSpot(int floorNo, int spotNo, SpotType spotType) {
        this.floorNo = floorNo;
        this.spotNo = spotNo;
        this.spotType = spotType;
    }

    public int getFloorNo() { return floorNo; }
    public int getSpotNo() { return spotNo; }
    public SpotType getSpotType() { return spotType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingSpot)) return false;
        ParkingSpot that = (ParkingSpot) o;
        return floorNo == that.floorNo && spotNo == that.spotNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floorNo, spotNo);
    }

    @Override
    public String toString() {
        return "[Floor:" + floorNo + ", Spot:" + spotNo + ", Type:" + spotType + "]";
    }
}

class Ticket {
    private final String id;
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;
    private final LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    public Ticket(String id, Vehicle vehicle, ParkingSpot parkingSpot) {
        this.id = id;
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.checkInTime = LocalDateTime.now();
    }

    public String getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getParkingSpot() { return parkingSpot; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public LocalDateTime getCheckOutTime() { return checkOutTime; }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    @Override
    public String toString() {
        return "Ticket{id='" + id + "', vehicle=" + vehicle + ", spot=" + parkingSpot + ", in=" + checkInTime + ", out=" + checkOutTime + '}';
    }
}

// Strategy Interfaces

interface FindSpotStrategy {
    ParkingSpot findSpot(List<ParkingSpot> availableSpots, VehicleType vehicleType);
}

interface CostCalculationStrategy {
    double calculateCost(Ticket ticket);
}

// Strategy Implementations

class FindSpotStrategyImpl implements FindSpotStrategy {
    @Override
    public ParkingSpot findSpot(List<ParkingSpot> availableSpots, VehicleType vehicleType) {
        for (ParkingSpot spot : availableSpots) {
            if (spot.getSpotType().isCompatibleWith(vehicleType)) {
                return spot;
            }
        }
        return null;
    }
}

class HourlyCostStrategy implements CostCalculationStrategy {
    private static final double RATE_PER_HOUR = 50.0;

    @Override
    public double calculateCost(Ticket ticket) {
        LocalDateTime in = ticket.getCheckInTime();
        LocalDateTime out = ticket.getCheckOutTime() != null ? ticket.getCheckOutTime() : LocalDateTime.now();
        long hours = Math.max(1, Duration.between(in, out).toHours());
        return hours * RATE_PER_HOUR;
    }
}

// Services

class TicketService {
    private final Map<ParkingSpot, Ticket> spotTicketMap = new HashMap<>();
    private final Map<Vehicle, Ticket> vehicleTicketMap = new HashMap<>();

    public Ticket createTicket(Vehicle vehicle, ParkingSpot spot) {
        Ticket ticket = new Ticket(UUID.randomUUID().toString(), vehicle, spot);
        spotTicketMap.put(spot, ticket);
        vehicleTicketMap.put(vehicle, ticket);
        return ticket;
    }

    public Ticket getTicketByVehicle(Vehicle vehicle) {
        return vehicleTicketMap.get(vehicle);
    }

    public void paymentAcceptedCallback(Vehicle vehicle) {
        Ticket ticket = vehicleTicketMap.get(vehicle);
        ticket.setCheckOutTime(LocalDateTime.now());
        spotTicketMap.remove(ticket.getParkingSpot());
        vehicleTicketMap.remove(vehicle);
    }
}

class PaymentService {
    private final CostCalculationStrategy strategy = new HourlyCostStrategy();
    private final TicketService ticketService;

    public PaymentService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public double calculateAmount(Vehicle vehicle) {
        Ticket ticket = ticketService.getTicketByVehicle(vehicle);
        return strategy.calculateCost(ticket);
    }

    public void acceptPayment(Vehicle vehicle) {
        double amount = calculateAmount(vehicle);
        System.out.println("Paid Rs." + amount + " for vehicle: " + vehicle.getId());
        ticketService.paymentAcceptedCallback(vehicle);
    }
}

class ParkingLotService {
    private final List<ParkingSpot> availableSpots = new ArrayList<>();

    public void createParkingLot(int floorCount, int spotsPerFloor) {
        for (int floor = 1; floor <= floorCount; floor++) {
            for (int spot = 1; spot <= spotsPerFloor; spot++) {
                SpotType type = spot % 3 == 0 ? SpotType.EIGHT_WHEELER : (spot % 2 == 0 ? SpotType.FOUR_WHEELER : SpotType.TWO_WHEELER);
                availableSpots.add(new ParkingSpot(floor, spot, type));
            }
        }
    }

    public List<ParkingSpot> getAvailableSpots() { return availableSpots; }
    public void markSpotUnavailable(ParkingSpot spot) { availableSpots.remove(spot); }
    public void markSpotAvailable(ParkingSpot spot) { availableSpots.add(spot); }
}

// Facade

class ParkingLotFacade {
    private final ParkingLotService lotService = new ParkingLotService();
    private final FindSpotStrategy spotStrategy = new FindSpotStrategyImpl();
    private final TicketService ticketService = new TicketService();
    private final PaymentService paymentService = new PaymentService(ticketService);

    public void createParkingLot(int floors, int spotsPerFloor) {
        lotService.createParkingLot(floors, spotsPerFloor);
    }

    public Ticket addVehicle(Vehicle vehicle) {
        ParkingSpot spot = spotStrategy.findSpot(lotService.getAvailableSpots(), vehicle.getVehicleType());
        if (spot == null) throw new RuntimeException("No parking spot available");
        lotService.markSpotUnavailable(spot);
        return ticketService.createTicket(vehicle, spot);
    }

    public double getAmount(Vehicle vehicle) {
        return paymentService.calculateAmount(vehicle);
    }

    public void acceptPayment(Vehicle vehicle) {
        paymentService.acceptPayment(vehicle);
    }
}

// Main class

public class ParkingLotSystem {
    public static void main(String[] args) throws InterruptedException {
        ParkingLotFacade facade = new ParkingLotFacade();
        facade.createParkingLot(2, 3);

        Vehicle v1 = new Vehicle("V001", VehicleType.FOUR_WHEELER);
        Ticket t1 = facade.addVehicle(v1);
        System.out.println("Ticket: " + t1);

        Thread.sleep(3000); // Simulate time passing

        double amount = facade.getAmount(v1);
        System.out.println("Amount to pay: Rs. " + amount);
        facade.acceptPayment(v1);
    }
}
