package LLD.CarRentalSystem.V1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum VehicleType {CAR_ECONOMY, CAR_SUV, CAR_LUXURY, BIKE_SPORT}
enum BookingStatus {PENDING, CONFIRMED, COMPLETED, CANCELLED} // Fixed typo
enum PaymentStatus {FAILED, COMPLETED}

class Store {
    private String name;
    private List<Vehicle> vehicles;

    public Store(String name) {
        this.name = name;
        vehicles = new ArrayList<>();
    }

    public String getName() {return name;}
    public List<Vehicle> getVehicles() {return vehicles;}
    public void addVehicle(Vehicle vehicle) {vehicles.add(vehicle);}
}

class Vehicle {
    private String modelNumber;
    private String registrationNumber;
    private VehicleType vehicleType;
    private int seats;

    public Vehicle(String modelNumber, String registrationNumber, VehicleType type, int seats) {
        this.modelNumber = modelNumber;
        this.registrationNumber = registrationNumber;
        this.vehicleType = type;
        this.seats = seats;
    }

    public String getModelNumber() {return modelNumber;}
    public String getRegistrationNumber() {return registrationNumber;}
    public VehicleType getVehicleType() {return vehicleType;}
    public int getSeats() {return seats;}
    
    // Added equals and hashCode for proper comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicle vehicle = (Vehicle) obj;
        return registrationNumber.equals(vehicle.registrationNumber);
    }
    
    @Override
    public int hashCode() {
        return registrationNumber.hashCode();
    }
}

class Booking {
    private String bookingId;
    private Customer customer;
    private Vehicle vehicle;
    private LocalDateTime createdAt;
    private LocalDateTime estimatedStartAt;
    private LocalDateTime estimatedEndAt;
    private BookingStatus bookingStatus;
    private List<Payment> payments;

    public Booking(Customer customer, Vehicle vehicle, LocalDateTime startAt, LocalDateTime endAt) {
        this.bookingId = "Booking-" + System.currentTimeMillis();
        this.customer = customer;
        this.vehicle = vehicle;
        this.createdAt = LocalDateTime.now();
        this.estimatedStartAt = startAt;
        this.estimatedEndAt = endAt;
        this.bookingStatus = BookingStatus.PENDING;
        this.payments = new ArrayList<>();
    }

    public String getBookingId() {return bookingId;}
    public Customer getCustomer() {return customer;}
    public Vehicle getVehicle() {return vehicle;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public LocalDateTime getEstimatedStartAt() {return estimatedStartAt;}
    public LocalDateTime getEstimatedEndAt() {return estimatedEndAt;}
    public BookingStatus getBookingStatus() {return bookingStatus;}
    public List<Payment> getPayments() {return payments;} // Fixed method name

    public void changeVehicle(Vehicle vehicle) {this.vehicle = vehicle;}
    public void confirmBooking() {bookingStatus = BookingStatus.CONFIRMED;} // Fixed typo
    public void cancelBooking() {bookingStatus = BookingStatus.CANCELLED;}
    public void completeBooking() {bookingStatus = BookingStatus.COMPLETED;}
    public void addPayment(Payment payment) {payments.add(payment);} // Added method
}

class TripMeta {
    private Booking booking;
    private LocalDateTime tripStartAt;
    private LocalDateTime tripEndAt;
    private int tripStartKilometers;
    private int tripEndKilometers;
    private List<VehicleDamage> damages;

    public TripMeta(Booking booking, LocalDateTime tripStartAt, int tripStartKilometers) {
        this.booking = booking;
        this.tripStartAt = tripStartAt;
        this.tripStartKilometers = tripStartKilometers;
        this.damages = new ArrayList<>();
    }

    public Booking getBooking() {return booking;}
    public LocalDateTime getTripStartAt() {return tripStartAt;}
    public LocalDateTime getTripEndAt() {return tripEndAt;}
    public int getTripStartKilometers() {return tripStartKilometers;}
    public int getTripEndKilometers() {return tripEndKilometers;}
    public List<VehicleDamage> getDamages() {return damages;}

    public void addVehicleDamage(VehicleDamage damage) {damages.add(damage);}
    public void setTripEndAt(LocalDateTime tripEndAt) {this.tripEndAt = tripEndAt;} // Added method
    public void setTripEndKilometers(int tripEndKilometers) {this.tripEndKilometers = tripEndKilometers;}
}

class VehicleDamage {
    private String component; // Fixed variable name casing
    private double fine;

    public VehicleDamage(String component, double fine) {
        this.component = component;
        this.fine = fine;
    }

    public String getComponent() {return component;}
    public double getFine() {return fine;}
}

class Customer {
    private String username;
    private String name;
    private String licenseNumber;
    private List<Booking> bookings;

    public Customer(String username, String name, String licenseNumber) {
        this.username = username;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.bookings = new ArrayList<>();
    }

    public String getUsername() {return username;}
    public String getName() {return name;}
    public String getLicenseNumber() {return licenseNumber;}
    public List<Booking> getBookings() {return bookings;}

    public void addBooking(Booking booking) {this.bookings.add(booking);}
}

class Payment {
    private String transactionId;
    private Booking booking;
    private PaymentStatus paymentStatus;
    private double fare;
    private LocalDateTime createdAt;

    public Payment(Booking booking, double fare, PaymentStatus status) {
        this.transactionId = "TXN-" + System.currentTimeMillis();
        this.booking = booking;
        this.fare = fare;
        this.paymentStatus = status;
        this.createdAt = LocalDateTime.now();
    }

    public String getTransactionId() { return transactionId; }
    public Booking getBooking() { return booking; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public double getFare() { return fare; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

class PricingRule {
    private VehicleType vehicleType;
    private double baseRate; // per day
    private double perKmRate;
    private double extraHourRate;

    public PricingRule(VehicleType vehicleType, double baseRate, double perKmRate, double extraHourRate) {
        this.vehicleType = vehicleType;
        this.baseRate = baseRate;
        this.perKmRate = perKmRate;
        this.extraHourRate = extraHourRate;
    }

    public VehicleType getVehicleType() { return vehicleType; }
    public double getBaseRate() { return baseRate; }
    public double getPerKmRate() { return perKmRate; }
    public double getExtraHourRate() { return extraHourRate; }
}

class InventoryManager {
    private List<Store> stores; // Added private modifier

    public InventoryManager() {
        this.stores = new ArrayList<>();
    }

    public void createStore(Store store) {
        stores.add(store);
    }

    public void addVehicle(Store store, Vehicle vehicle) {
        store.addVehicle(vehicle);
    }
    
    public List<Store> getStores() { return stores; } // Added getter
    
    public List<Vehicle> getAllVehicles() { // Added method to get all vehicles
        List<Vehicle> allVehicles = new ArrayList<>();
        for (Store store : stores) {
            allVehicles.addAll(store.getVehicles());
        }
        return allVehicles;
    }
}

class ReservationManager {
    private List<Booking> bookings; // Added private modifier

    public ReservationManager() { // Added constructor
        this.bookings = new ArrayList<>();
    }

    public Booking createBooking(Customer customer, Vehicle vehicle, LocalDateTime from, LocalDateTime to) {
        if (!isAvailable(vehicle, from, to)) {
            throw new IllegalArgumentException("Vehicle not available in provided date range");
        }

        Booking booking = new Booking(customer, vehicle, from, to);
        bookings.add(booking);
        customer.addBooking(booking); // Add booking to customer
        return booking; // Return the created booking
    }

    public void confirmBooking(Booking booking) {booking.confirmBooking();}
    public void cancelBooking(Booking booking) {booking.cancelBooking();}
    public void completeBooking(Booking booking) {booking.completeBooking();}

    public boolean isAvailable(Vehicle vehicle, LocalDateTime from, LocalDateTime to) {
        for (Booking booking : bookings) {
            if (booking.getVehicle().equals(vehicle) && 
                (booking.getBookingStatus() == BookingStatus.CONFIRMED || 
                 booking.getBookingStatus() == BookingStatus.PENDING)) {
                // Check for overlap
                if (!(to.isBefore(booking.getEstimatedStartAt()) || from.isAfter(booking.getEstimatedEndAt()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Vehicle> getAvailableVehicles(List<Vehicle> allVehicles, LocalDateTime from, LocalDateTime to) {
        List<Vehicle> available = new ArrayList<>();
        for (Vehicle v : allVehicles) {
            if (isAvailable(v, from, to)) {
                available.add(v);
            }
        }
        return available;
    }

    public List<Vehicle> getAvailableVehicles(List<Vehicle> allVehicles, VehicleType type, LocalDateTime from, LocalDateTime to) {
        List<Vehicle> filtered = new ArrayList<>();
        for (Vehicle v : allVehicles) {
            if (v.getVehicleType() == type && isAvailable(v, from, to)) {
                filtered.add(v);
            }
        }
        return filtered;
    }
    
    public List<Booking> getBookings() { return bookings; } // Added getter
}

class PricingService {
    private Map<VehicleType, PricingRule> pricingRules;

    public PricingService() {
        pricingRules = new HashMap<>();
        initializePricingRules();
    }

    private void initializePricingRules() {
        pricingRules.put(VehicleType.CAR_ECONOMY, new PricingRule(VehicleType.CAR_ECONOMY, 1500, 10, 100));
        pricingRules.put(VehicleType.CAR_SUV, new PricingRule(VehicleType.CAR_SUV, 3000, 15, 150));
        pricingRules.put(VehicleType.CAR_LUXURY, new PricingRule(VehicleType.CAR_LUXURY, 5000, 20, 200));
        pricingRules.put(VehicleType.BIKE_SPORT, new PricingRule(VehicleType.BIKE_SPORT, 1000, 8, 80));
    }

    public double calculateRentalCost(VehicleType vehicleType, LocalDateTime fromDate, LocalDateTime toDate) {
        PricingRule rule = pricingRules.get(vehicleType);
        if (rule == null) return 0.0;

        long hours = java.time.Duration.between(fromDate, toDate).toHours();
        long days = hours / 24;
        long extraHours = hours % 24;

        return (days * rule.getBaseRate()) + (extraHours * rule.getExtraHourRate());
    }

    public double calculateAdditionalKmCharges(VehicleType vehicleType, int extraKm) {
        PricingRule rule = pricingRules.get(vehicleType);
        return rule != null ? extraKm * rule.getPerKmRate() : 0.0;
    }
    
    public double calculateDamageFines(List<VehicleDamage> damages) { // Added method
        double totalFine = 0.0;
        for (VehicleDamage damage : damages) {
            totalFine += damage.getFine();
        }
        return totalFine;
    }
}

interface PaymentStrategy {
    boolean process(Booking booking, double amount);
}

// Concrete implementations of payment strategies
class CreditCardPayment implements PaymentStrategy {
    @Override
    public boolean process(Booking booking, double amount) {
        // Simulate credit card payment processing
        System.out.println("Processing credit card payment of $" + amount + " for booking " + booking.getBookingId());
        return true; // Assume payment is successful
    }
}

class DebitCardPayment implements PaymentStrategy {
    @Override
    public boolean process(Booking booking, double amount) {
        // Simulate debit card payment processing
        System.out.println("Processing debit card payment of $" + amount + " for booking " + booking.getBookingId());
        return true; // Assume payment is successful
    }
}

class PaymentService {
    private PaymentStrategy paymentStrategy;

    public PaymentService(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public Payment processPayment(Booking booking, double amount) { // Fixed method name and logic
        Payment payment;
        if (paymentStrategy.process(booking, amount)) {
            payment = new Payment(booking, amount, PaymentStatus.COMPLETED);
        } else {
            payment = new Payment(booking, amount, PaymentStatus.FAILED);
        }
        booking.addPayment(payment);
        return payment;
    }
    
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) { // Added method to change strategy
        this.paymentStrategy = paymentStrategy;
    }
}

// Main system class to coordinate all components
class CarRentalSystem {
    private InventoryManager inventoryManager;
    private ReservationManager reservationManager;
    private PricingService pricingService;
    private PaymentService paymentService;
    private List<Customer> customers;

    public CarRentalSystem() {
        this.inventoryManager = new InventoryManager();
        this.reservationManager = new ReservationManager();
        this.pricingService = new PricingService();
        this.paymentService = new PaymentService(new CreditCardPayment()); // Default payment strategy
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void addStore(Store store) {
        inventoryManager.createStore(store);
    }

    public void addVehicleToStore(Store store, Vehicle vehicle) {
        inventoryManager.addVehicle(store, vehicle);
    }

    public List<Vehicle> searchAvailableVehicles(LocalDateTime from, LocalDateTime to) {
        List<Vehicle> allVehicles = inventoryManager.getAllVehicles();
        return reservationManager.getAvailableVehicles(allVehicles, from, to);
    }

    public List<Vehicle> searchAvailableVehicles(VehicleType type, LocalDateTime from, LocalDateTime to) {
        List<Vehicle> allVehicles = inventoryManager.getAllVehicles();
        return reservationManager.getAvailableVehicles(allVehicles, type, from, to);
    }

    public Booking createBooking(Customer customer, Vehicle vehicle, LocalDateTime from, LocalDateTime to) {
        return reservationManager.createBooking(customer, vehicle, from, to);
    }

    public double calculateRentalCost(VehicleType vehicleType, LocalDateTime from, LocalDateTime to) {
        return pricingService.calculateRentalCost(vehicleType, from, to);
    }

    public Payment processPayment(Booking booking, double amount) {
        return paymentService.processPayment(booking, amount);
    }

    public void confirmBooking(Booking booking) {
        reservationManager.confirmBooking(booking);
    }

    public void completeBooking(Booking booking) {
        reservationManager.completeBooking(booking);
    }

    // Getters
    public InventoryManager getInventoryManager() { return inventoryManager; }
    public ReservationManager getReservationManager() { return reservationManager; }
    public PricingService getPricingService() { return pricingService; }
    public PaymentService getPaymentService() { return paymentService; }
    public List<Customer> getCustomers() { return customers; }
}

public class RentalSystem {
    public static void main(String[] args) {
        // Demo usage
        CarRentalSystem system = new CarRentalSystem();
        
        // Create store
        Store store1 = new Store("Downtown Store");
        system.addStore(store1);
        
        // Add vehicles
        Vehicle car1 = new Vehicle("Honda Civic", "ABC123", VehicleType.CAR_ECONOMY, 5);
        Vehicle car2 = new Vehicle("Toyota RAV4", "DEF456", VehicleType.CAR_SUV, 7);
        system.addVehicleToStore(store1, car1);
        system.addVehicleToStore(store1, car2);
        
        // Create customer
        Customer customer = new Customer("john_doe", "John Doe", "DL123456");
        system.addCustomer(customer);
        
        // Search and book
        LocalDateTime from = LocalDateTime.now().plusDays(1);
        LocalDateTime to = LocalDateTime.now().plusDays(3);
        
        List<Vehicle> availableVehicles = system.searchAvailableVehicles(from, to);
        System.out.println("Available vehicles: " + availableVehicles.size());
        
        if (!availableVehicles.isEmpty()) {
            Vehicle selectedVehicle = availableVehicles.get(0);
            Booking booking = system.createBooking(customer, selectedVehicle, from, to);
            
            double cost = system.calculateRentalCost(selectedVehicle.getVehicleType(), from, to);
            System.out.println("Rental cost: $" + cost);
            
            Payment payment = system.processPayment(booking, cost);
            if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
                system.confirmBooking(booking);
                System.out.println("Booking confirmed: " + booking.getBookingId());
            }
        }
    }
}