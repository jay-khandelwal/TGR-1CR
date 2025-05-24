package LLD.CarRentalSystem.V2_Calude_AI;

// Enums and Constants
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

enum CarType {
    ECONOMY, COMPACT, SUV, LUXURY, TRUCK
}

enum CarStatus {
    AVAILABLE, BOOKED, MAINTENANCE, RENTED
}

enum BookingStatus {
    ACTIVE, COMPLETED, CANCELLED
}

// Core Entity Classes
class User {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String licenseNumber;
    
    public User(String userId, String name, String email, String phone, String licenseNumber) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
    }
    
    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getLicenseNumber() { return licenseNumber; }
}

class Car {
    private String carId;
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private CarType type;
    private CarStatus status;
    private String locationId;
    private double dailyRate;
    
    public Car(String carId, String licensePlate, String make, String model, 
               int year, CarType type, String locationId, double dailyRate) {
        this.carId = carId;
        this.licensePlate = licensePlate;
        this.make = make;
        this.model = model;
        this.year = year;
        this.type = type;
        this.status = CarStatus.AVAILABLE;
        this.locationId = locationId;
        this.dailyRate = dailyRate;
    }
    
    // Getters and Setters
    public String getCarId() { return carId; }
    public String getLicensePlate() { return licensePlate; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public CarType getType() { return type; }
    public CarStatus getStatus() { return status; }
    public void setStatus(CarStatus status) { this.status = status; }
    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }
    public double getDailyRate() { return dailyRate; }
    
    @Override
    public String toString() {
        return String.format("%s %s %d (%s) - $%.2f/day", make, model, year, type, dailyRate);
    }
}

class Location {
    private String locationId;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    
    public Location(String locationId, String name, String address, String city, String state, String zipCode) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }
    
    // Getters
    public String getLocationId() { return locationId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    
    @Override
    public String toString() {
        return String.format("%s - %s, %s, %s %s", name, address, city, state, zipCode);
    }
}

class Booking {
    private String bookingId;
    private String userId;
    private String carId;
    private String pickupLocationId;
    private String dropoffLocationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime actualReturnTime;
    private BookingStatus status;
    private double totalCost;
    private LocalDateTime createdAt;
    
    public Booking(String bookingId, String userId, String carId, String pickupLocationId, 
                   String dropoffLocationId, LocalDateTime startTime, LocalDateTime endTime, double totalCost) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.carId = carId;
        this.pickupLocationId = pickupLocationId;
        this.dropoffLocationId = dropoffLocationId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.status = BookingStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public String getUserId() { return userId; }
    public String getCarId() { return carId; }
    public String getPickupLocationId() { return pickupLocationId; }
    public String getDropoffLocationId() { return dropoffLocationId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public LocalDateTime getActualReturnTime() { return actualReturnTime; }
    public void setActualReturnTime(LocalDateTime actualReturnTime) { this.actualReturnTime = actualReturnTime; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public double getTotalCost() { return totalCost; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

class SearchCriteria {
    private String locationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private CarType carType;
    
    public SearchCriteria(String locationId, LocalDateTime startTime, LocalDateTime endTime, CarType carType) {
        this.locationId = locationId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.carType = carType;
    }
    
    public SearchCriteria(String locationId, LocalDateTime startTime, LocalDateTime endTime) {
        this(locationId, startTime, endTime, null);
    }
    
    // Getters
    public String getLocationId() { return locationId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public CarType getCarType() { return carType; }
}

// Repository Layer - Data Access
interface UserRepository {
    void save(User user);
    User findById(String userId);
    List<User> findAll();
}

interface CarRepository {
    void save(Car car);
    Car findById(String carId);
    List<Car> findByLocationId(String locationId);
    List<Car> findByStatus(CarStatus status);
    List<Car> findAll();
}

interface LocationRepository {
    void save(Location location);
    Location findById(String locationId);
    List<Location> findAll();
}

interface BookingRepository {
    void save(Booking booking);
    Booking findById(String bookingId);
    List<Booking> findByUserId(String userId);
    List<Booking> findByCarId(String carId);
    List<Booking> findByCarIdAndStatus(String carId, BookingStatus status);
    List<Booking> findActiveBookingsByCarId(String carId);
}

// In-Memory Implementations
class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    
    @Override
    public void save(User user) {
        users.put(user.getUserId(), user);
    }
    
    @Override
    public User findById(String userId) {
        return users.get(userId);
    }
    
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}

class InMemoryCarRepository implements CarRepository {
    private final Map<String, Car> cars = new ConcurrentHashMap<>();
    
    @Override
    public void save(Car car) {
        cars.put(car.getCarId(), car);
    }
    
    @Override
    public Car findById(String carId) {
        return cars.get(carId);
    }
    
    @Override
    public List<Car> findByLocationId(String locationId) {
        return cars.values().stream()
                  .filter(car -> car.getLocationId().equals(locationId))
                  .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    @Override
    public List<Car> findByStatus(CarStatus status) {
        return cars.values().stream()
                  .filter(car -> car.getStatus().equals(status))
                  .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars.values());
    }
}

class InMemoryLocationRepository implements LocationRepository {
    private final Map<String, Location> locations = new ConcurrentHashMap<>();
    
    @Override
    public void save(Location location) {
        locations.put(location.getLocationId(), location);
    }
    
    @Override
    public Location findById(String locationId) {
        return locations.get(locationId);
    }
    
    @Override
    public List<Location> findAll() {
        return new ArrayList<>(locations.values());
    }
}

class InMemoryBookingRepository implements BookingRepository {
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    
    @Override
    public void save(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
    }
    
    @Override
    public Booking findById(String bookingId) {
        return bookings.get(bookingId);
    }
    
    @Override
    public List<Booking> findByUserId(String userId) {
        return bookings.values().stream()
                      .filter(booking -> booking.getUserId().equals(userId))
                      .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    @Override
    public List<Booking> findByCarId(String carId) {
        return bookings.values().stream()
                      .filter(booking -> booking.getCarId().equals(carId))
                      .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    @Override
    public List<Booking> findByCarIdAndStatus(String carId, BookingStatus status) {
        return bookings.values().stream()
                      .filter(booking -> booking.getCarId().equals(carId) && booking.getStatus().equals(status))
                      .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    @Override
    public List<Booking> findActiveBookingsByCarId(String carId) {
        return findByCarIdAndStatus(carId, BookingStatus.ACTIVE);
    }
}

// Service Layer - Business Logic
class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void registerUser(User user) throws Exception {
        if (userRepository.findById(user.getUserId()) != null) {
            throw new Exception("User already exists");
        }
        userRepository.save(user);
    }
    
    public User getUser(String userId) throws Exception {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new Exception("User not found");
        }
        return user;
    }
}

class LocationService {
    private final LocationRepository locationRepository;
    
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
    
    public void addLocation(Location location) {
        locationRepository.save(location);
    }
    
    public Location getLocation(String locationId) throws Exception {
        Location location = locationRepository.findById(locationId);
        if (location == null) {
            throw new Exception("Location not found");
        }
        return location;
    }
    
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}

class CarService {
    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;
    
    public CarService(CarRepository carRepository, BookingRepository bookingRepository) {
        this.carRepository = carRepository;
        this.bookingRepository = bookingRepository;
    }
    
    public void addCar(Car car) {
        carRepository.save(car);
    }
    
    public Car getCar(String carId) throws Exception {
        Car car = carRepository.findById(carId);
        if (car == null) {
            throw new Exception("Car not found");
        }
        return car;
    }
    
    public List<Car> searchAvailableCars(SearchCriteria criteria) {
        List<Car> availableCars = new ArrayList<>();
        List<Car> locationCars = carRepository.findByLocationId(criteria.getLocationId());
        
        for (Car car : locationCars) {
            if (isCarAvailable(car, criteria)) {
                availableCars.add(car);
            }
        }
        
        return availableCars;
    }
    
    private boolean isCarAvailable(Car car, SearchCriteria criteria) {
        // Check car type if specified
        if (criteria.getCarType() != null && !car.getType().equals(criteria.getCarType())) {
            return false;
        }
        
        // Check if car is in available status
        if (car.getStatus() != CarStatus.AVAILABLE) {
            return false;
        }
        
        // Check if car has conflicting bookings
        return !hasConflictingBooking(car.getCarId(), criteria.getStartTime(), criteria.getEndTime());
    }
    
    private boolean hasConflictingBooking(String carId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> activeBookings = bookingRepository.findActiveBookingsByCarId(carId);
        
        for (Booking booking : activeBookings) {
            if (isTimeOverlapping(booking.getStartTime(), booking.getEndTime(), startTime, endTime)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isTimeOverlapping(LocalDateTime start1, LocalDateTime end1, 
                                      LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    public void updateCarStatus(String carId, CarStatus status) throws Exception {
        Car car = getCar(carId);
        car.setStatus(status);
        carRepository.save(car);
    }
    
    public void updateCarLocation(String carId, String locationId) throws Exception {
        Car car = getCar(carId);
        car.setLocationId(locationId);
        carRepository.save(car);
    }
}

class PricingService {
    public double calculateTotalCost(Car car, LocalDateTime startTime, LocalDateTime endTime) {
        long days = java.time.Duration.between(startTime, endTime).toDays();
        if (days == 0) days = 1; // Minimum 1 day rental
        return days * car.getDailyRate();
    }
    
    // Future: Add dynamic pricing, discounts, etc.
    public double applyDiscounts(double basePrice, String userId) {
        // Placeholder for loyalty discounts, promotional codes, etc.
        return basePrice;
    }
}

class BookingService {
    private final BookingRepository bookingRepository;
    private final CarService carService;
    private final UserService userService;
    private final LocationService locationService;
    private final PricingService pricingService;
    private final AtomicLong bookingIdCounter;
    
    public BookingService(BookingRepository bookingRepository, CarService carService, 
                         UserService userService, LocationService locationService, 
                         PricingService pricingService) {
        this.bookingRepository = bookingRepository;
        this.carService = carService;
        this.userService = userService;
        this.locationService = locationService;
        this.pricingService = pricingService;
        this.bookingIdCounter = new AtomicLong(1);
    }
    
    public synchronized Booking createBooking(String userId, String carId, String pickupLocationId, 
                                            String dropoffLocationId, LocalDateTime startTime, 
                                            LocalDateTime endTime) throws Exception {
        
        // Validate user, car, and locations
        userService.getUser(userId);
        Car car = carService.getCar(carId);
        locationService.getLocation(pickupLocationId);
        locationService.getLocation(dropoffLocationId);
        
        if (car.getStatus() != CarStatus.AVAILABLE) {
            throw new Exception("Car is not available");
        }
        
        // Calculate total cost
        double totalCost = pricingService.calculateTotalCost(car, startTime, endTime);
        totalCost = pricingService.applyDiscounts(totalCost, userId);
        
        // Create booking
        String bookingId = "BOOK" + bookingIdCounter.getAndIncrement();
        Booking booking = new Booking(bookingId, userId, carId, pickupLocationId, 
                                    dropoffLocationId, startTime, endTime, totalCost);
        
        bookingRepository.save(booking);
        carService.updateCarStatus(carId, CarStatus.BOOKED);
        
        return booking;
    }
    
    public boolean cancelBooking(String bookingId, String userId) throws Exception {
        Booking booking = getBooking(bookingId);
        
        if (!booking.getUserId().equals(userId)) {
            throw new Exception("Unauthorized to cancel this booking");
        }
        
        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new Exception("Booking cannot be cancelled");
        }
        
        // Check cancellation policy
        if (LocalDateTime.now().isAfter(booking.getStartTime().minusHours(24))) {
            throw new Exception("Cannot cancel booking within 24 hours of start time");
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        carService.updateCarStatus(booking.getCarId(), CarStatus.AVAILABLE);
        
        return true;
    }
    
    public boolean pickupCar(String bookingId) throws Exception {
        Booking booking = getBooking(bookingId);
        
        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new Exception("Invalid booking status for pickup");
        }
        
        carService.updateCarStatus(booking.getCarId(), CarStatus.RENTED);
        return true;
    }
    
    public boolean returnCar(String bookingId, String locationId) throws Exception {
        Booking booking = getBooking(bookingId);
        
        Car car = carService.getCar(booking.getCarId());
        if (car.getStatus() != CarStatus.RENTED) {
            throw new Exception("Car is not currently rented");
        }
        
        booking.setActualReturnTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(booking);
        
        carService.updateCarStatus(booking.getCarId(), CarStatus.AVAILABLE);
        carService.updateCarLocation(booking.getCarId(), locationId);
        
        return true;
    }
    
    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    public Booking getBooking(String bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new Exception("Booking not found");
        }
        return booking;
    }
}

// Facade Pattern - Main System Interface
class CarRentalSystem {
    private final UserService userService;
    private final LocationService locationService;
    private final CarService carService;
    private final BookingService bookingService;
    
    public CarRentalSystem() {
        // Initialize repositories
        UserRepository userRepo = new InMemoryUserRepository();
        LocationRepository locationRepo = new InMemoryLocationRepository();
        CarRepository carRepo = new InMemoryCarRepository();
        BookingRepository bookingRepo = new InMemoryBookingRepository();
        
        // Initialize services
        this.userService = new UserService(userRepo);
        this.locationService = new LocationService(locationRepo);
        this.carService = new CarService(carRepo, bookingRepo);
        this.bookingService = new BookingService(bookingRepo, carService, userService, 
                                               locationService, new PricingService());
    }
    
    // User operations
    public void registerUser(User user) throws Exception {
        userService.registerUser(user);
    }
    
    public User getUser(String userId) throws Exception {
        return userService.getUser(userId);
    }
    
    // Location operations
    public void addLocation(Location location) {
        locationService.addLocation(location);
    }
    
    public Location getLocation(String locationId) throws Exception {
        return locationService.getLocation(locationId);
    }
    
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }
    
    // Car operations
    public void addCar(Car car) {
        carService.addCar(car);
    }
    
    public Car getCar(String carId) throws Exception {
        return carService.getCar(carId);
    }
    
    public List<Car> searchAvailableCars(SearchCriteria criteria) {
        return carService.searchAvailableCars(criteria);
    }
    
    // Booking operations
    public Booking makeBooking(String userId, String carId, String pickupLocationId, 
                              String dropoffLocationId, LocalDateTime startTime, 
                              LocalDateTime endTime) throws Exception {
        return bookingService.createBooking(userId, carId, pickupLocationId, 
                                          dropoffLocationId, startTime, endTime);
    }
    
    public boolean cancelBooking(String bookingId, String userId) throws Exception {
        return bookingService.cancelBooking(bookingId, userId);
    }
    
    public boolean pickupCar(String bookingId) throws Exception {
        return bookingService.pickupCar(bookingId);
    }
    
    public boolean returnCar(String bookingId, String locationId) throws Exception {
        return bookingService.returnCar(bookingId, locationId);
    }
    
    public List<Booking> getUserBookings(String userId) {
        return bookingService.getUserBookings(userId);
    }
    
    public Booking getBooking(String bookingId) throws Exception {
        return bookingService.getBooking(bookingId);
    }
}

// Demo Class
public class CarRentalSystemDemo {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();
        
        try {
            // Setup locations
            Location loc1 = new Location("LOC001", "Downtown Branch", "123 Main St", "New York", "NY", "10001");
            Location loc2 = new Location("LOC002", "Airport Branch", "456 Airport Rd", "New York", "NY", "10002");
            rentalSystem.addLocation(loc1);
            rentalSystem.addLocation(loc2);
            
            // Setup cars
            Car car1 = new Car("CAR001", "ABC123", "Toyota", "Camry", 2023, CarType.ECONOMY, "LOC001", 50.0);
            Car car2 = new Car("CAR002", "XYZ789", "BMW", "X5", 2023, CarType.SUV, "LOC001", 120.0);
            rentalSystem.addCar(car1);
            rentalSystem.addCar(car2);
            
            // Setup users
            User user1 = new User("USER001", "John Doe", "john@email.com", "555-1234", "DL123456");
            rentalSystem.registerUser(user1);
            
            // Demo workflow
            System.out.println("=== Car Rental System Demo ===");
            
            // Search for cars
            LocalDateTime startTime = LocalDateTime.now().plusDays(1);
            LocalDateTime endTime = LocalDateTime.now().plusDays(3);
            SearchCriteria criteria = new SearchCriteria("LOC001", startTime, endTime);
            
            List<Car> availableCars = rentalSystem.searchAvailableCars(criteria);
            System.out.println("Available cars: " + availableCars.size());
            
            // Make booking
            if (!availableCars.isEmpty()) {
                Booking booking = rentalSystem.makeBooking("USER001", availableCars.get(0).getCarId(), 
                                                         "LOC001", "LOC001", startTime, endTime);
                System.out.println("Booking created: " + booking.getBookingId());
                
                // Pickup and return
                rentalSystem.pickupCar(booking.getBookingId());
                System.out.println("Car picked up");
                
                rentalSystem.returnCar(booking.getBookingId(), "LOC002");
                System.out.println("Car returned to different location");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}