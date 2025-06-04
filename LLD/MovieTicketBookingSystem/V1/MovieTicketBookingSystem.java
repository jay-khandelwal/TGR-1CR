package LLD.MovieTicketBookingSystem.V1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

enum SeatType {STANDARD, DELUX, SUPER_DELUX}
enum BookingStatus {PENDING, CONFIRMED, FAILED}
enum PaymentStatus {PENDING, COMPLETED, FAILED}
enum SeatStatus {AVAILABLE, BOOKED}

class Movie {
    private String id;
    private String name;
    private String description;
    private String language;
    private int durationMinutes;

    public Movie(String id, String name, String description, String language, int durationMinutes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.language = language;
        this.durationMinutes = durationMinutes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}

class Location {
    private String laneNo;
    private String pincode;
    private String city;
    private String state;
    private String country;

    public Location(String laneNo, String pincode, String city, String state, String country) {
        this.laneNo = laneNo;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public String getLaneNo() {
        return laneNo;
    }

    public String getPincode() {
        return pincode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }
}

class Theater {
    private String id;
    private String name;
    private Location location;
    private List<Screen> screens;

    public Theater(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.screens = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public void addScreen(Screen screen) {
        this.screens.add(screen);
    }
}

class Screen {
    private String id;
    private List<Seat> seats;

    public Screen(String id) {
        this.id = id;
        this.seats = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void addSeat(Seat seat) {
        this.seats.add(seat);
    }
}

class Seat {
    private String id;
    private SeatType seatType;
    
    public Seat(String id, SeatType seatType) {
        this.id = id;
        this.seatType = seatType;
    }

    public String getId() {
        return id;
    }

    public SeatType getSeatType() {
        return seatType;
    }
}

class Show {
    private String id;
    private Screen screen;
    private Movie movie;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Show(Screen screen, Movie movie, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = UUID.randomUUID().toString();
        this.screen = screen;
        this.movie = movie;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getID() {
        return id;
    }

    public Screen getScreen() {
        return screen;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}

class ShowSeat {
    private String id;
    private Seat seat;
    private SeatStatus status;
    private double price;

    public ShowSeat(Seat seat, SeatStatus status, double price) {
        this.id = UUID.randomUUID().toString();
        this.seat = seat;
        this.status = status;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getSeatId() {
        return seat.getId();
    }

    public Seat getSeat() {
        return seat;
    }

    public boolean isAvailable() {
        return this.status == SeatStatus.AVAILABLE;
    }

    public void makeAvailable() {
        this.status = SeatStatus.AVAILABLE;
    }

    public void book() {
        this.status = SeatStatus.BOOKED;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class Booking {
    private String id;
    private Show show;
    private User user;
    private List<ShowSeat> seats;
    private BookingStatus bookingStatus;
    private Payment payment;

    public Booking(Show show, User user, List<ShowSeat> seats) {
        this.id = UUID.randomUUID().toString();
        this.show = show;
        this.user = user;
        this.seats = seats;
        this.bookingStatus = BookingStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public Show getShow() {
        return show;
    }

    public User getUser() {
        return user;
    }

    public List<ShowSeat> getSeats() {
        return seats;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void confirmBooking() {
        this.bookingStatus = BookingStatus.CONFIRMED;
    }

    public void failBooking() {
        this.bookingStatus = BookingStatus.FAILED;
    }

    public double getTotalAmount() {
        if (seats == null || seats.isEmpty()) {
            return 0.0;
        }
        return seats.stream().mapToDouble(ShowSeat::getPrice).sum();
    }
}

class Payment {
    private String id;
    private Booking booking;
    private double amount;
    private PaymentStatus paymentStatus;

    public Payment(Booking booking, double amount) {
        this.id = UUID.randomUUID().toString();
        this.booking = booking;
        this.amount = amount;
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public Booking getBooking() {
        return booking;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void confirmPayment() {
        this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public void failPayment() {
        this.paymentStatus = PaymentStatus.FAILED;
    }
}

class User {
    private String id;
    private String name;
    private String email;
    private List<Booking> bookings;

    public User(String name, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.bookings = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }
}

class MovieManager {
    private Map<String, Movie> movies;

    public MovieManager() {
        this.movies = new HashMap<>();
    }

    public void createMovie(String name, String description, String language, int durationMinutes) {
        Movie movie = new Movie(UUID.randomUUID().toString(), name, description, language, durationMinutes);
        movies.put(movie.getId(), movie);
    }

    public List<Movie> getMovies() {
        return new ArrayList<>(movies.values());
    }

    public Movie getMovie(String movieId) {
        return movies.get(movieId);
    }
}

class TheaterManager {
    private Map<String, Theater> theaters;
    private Map<String, Screen> screens;

    public TheaterManager() {
        this.theaters = new HashMap<>();
        this.screens = new HashMap<>();
    }

    public void createTheater(String name, Location location) {
        Theater theater = new Theater(UUID.randomUUID().toString(), name, location);
        theaters.put(theater.getId(), theater);
    }

    public void createScreen(String theaterId, String screenId) {
        Theater theater = theaters.get(theaterId);
        if (theater != null) {
            Screen screen = new Screen(screenId);
            theater.addScreen(screen);
            screens.put(screen.getId(), screen);
        } else {
            throw new IllegalArgumentException("Theater not found");
        }
    }

    public void addSeatToScreen(String screenId, Seat seat) {
        Screen screen = screens.get(screenId);
        if (screen != null) {
            screen.addSeat(seat);
        } else {
            throw new IllegalArgumentException("Screen not found");
        }
    }

    public void addSeatsToScreen(String screenId, List<Seat> seats) {
        Screen screen = screens.get(screenId);
        if (screen != null) {
            for (Seat seat : seats) {
                screen.addSeat(seat);
            }
        } else {
            throw new IllegalArgumentException("Screen not found");
        }
    }

    public Theater getTheater(String theaterId) {
        return theaters.get(theaterId);
    }

    public Screen getScreen(String screenId) {
        return screens.get(screenId);
    }

    public List<Theater> getAllTheaters() {
        return new ArrayList<>(theaters.values());
    }
}

class ShowManager {
    private Map<String, Show> shows;
    private Map<Show, Map<String, ShowSeat>> seats;

    public ShowManager() {
        this.shows = new HashMap<>();
        this.seats = new HashMap<>();
    }

    public Show createShow(Screen screen, Movie movie, LocalDateTime startTime, LocalDateTime endTime) {
        Show show = new Show(screen, movie, startTime, endTime);
        shows.put(show.getID(), show);
        initializeSeats(show, screen);
        return show;
    }

    public List<Show> getShows() {
        return new ArrayList<>(shows.values());
    }

    public List<Show> getShows(Screen screen) {
        List<Show> response = new ArrayList<>();
        for (Show show: shows.values()) {
            if (show.getScreen().getId().equals(screen.getId())) {
                response.add(show);
            }
        }
        return response;
    }

    public Show getShow(String showId) {
        return shows.get(showId);
    }

    private void initializeSeats(Show show, Screen screen) {
        seats.put(show, new HashMap<>());
        Map<String, ShowSeat> seatMap = seats.get(show);
        for (Seat seat: screen.getSeats()) {
            double price = getPriceForSeatType(seat.getSeatType());
            seatMap.put(seat.getId(), new ShowSeat(seat, SeatStatus.AVAILABLE, price));
        }
    }

    private double getPriceForSeatType(SeatType seatType) {
        switch (seatType) {
            case STANDARD: return 100.0;
            case DELUX: return 150.0;
            case SUPER_DELUX: return 200.0;
            default: return 100.0;
        }
    }

    public void checkAvailability(List<String> seatIds, Show show) {
        Map<String, ShowSeat> seatMap = seats.get(show);
        if (seatMap == null) {
            throw new IllegalArgumentException("Show not found");
        }

        for (String seatId: seatIds) {
            if (!seatMap.containsKey(seatId)) {
                throw new IllegalArgumentException("Invalid seat selected: " + seatId);
            }
            if (!seatMap.get(seatId).isAvailable()) {
                throw new IllegalArgumentException("Seat " + seatId + " is not available");
            }
        }
    }

    public List<ShowSeat> getShowSeats(Show show, List<String> seatIds) {
        Map<String, ShowSeat> seatMap = seats.get(show);
        if (seatMap == null) {
            throw new IllegalArgumentException("Show not found");
        }
        
        return seatIds.stream()
            .map(seatMap::get)
            .collect(Collectors.toList());
    }

    public List<ShowSeat> getAllShowSeats(Show show) {
        Map<String, ShowSeat> seatMap = seats.get(show);
        if (seatMap == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(seatMap.values());
    }
}

class BookingManager {
    private Map<String, Booking> bookings;
    private ShowManager showManager;
    private UserManager userManager;
    private LockingManager lockingManager;

    public BookingManager(ShowManager showManager, UserManager userManager, LockingManager lockingManager) {
        this.showManager = showManager;
        this.userManager = userManager;
        this.lockingManager = lockingManager;
        this.bookings = new HashMap<>();
    }

    public String makeBooking(String userId, List<String> seatIds, Show show) {
        User user = userManager.getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }

        // Check seat availability
        showManager.checkAvailability(seatIds, show);

        // Lock the seats
        lockingManager.checkAndHoldTheSeats(show, seatIds, user);

        // Get the actual ShowSeat objects
        List<ShowSeat> selectedSeats = showManager.getShowSeats(show, seatIds);

        // Create booking
        Booking booking = new Booking(show, user, selectedSeats);
        bookings.put(booking.getId(), booking);
        
        // Add booking to user's booking list
        user.addBooking(booking);
        
        return booking.getId();
    }

    public void confirmBooking(Booking booking) {
        booking.confirmBooking();

        // Mark seats as booked
        for (ShowSeat seat: booking.getSeats()) {
            seat.book();
        }
        
        // Release locks
        lockingManager.releaseLocks(booking.getShow(), 
            booking.getSeats().stream()
                .map(ShowSeat::getSeatId)
                .collect(Collectors.toList()));
    }

    public void failBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            booking.failBooking();
            // Release locks
            lockingManager.releaseLocks(booking.getShow(), 
                booking.getSeats().stream()
                    .map(ShowSeat::getSeatId)
                    .collect(Collectors.toList()));
        }
    }
    
    public Booking getBooking(String bookingId) {
        return bookings.get(bookingId);
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }
}

class UserManager {
    private Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public User createUser(String name, String email) {
        User user = new User(name, email);
        users.put(user.getId(), user);
        return user;
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}

class Lock {
    private String objectId;
    private User user;
    private Long lockedAt;
    private int timeoutInSec;

    public Lock(String objectId, User user, int timeoutInSec) {
        this.objectId = objectId;
        this.user = user;
        this.timeoutInSec = timeoutInSec;
        this.lockedAt = System.currentTimeMillis();
    }

    public String getObjectId() {
        return objectId;
    }

    public User getUser() {
        return user;
    }

    public Long getLockedAt() {
        return lockedAt;
    }

    public int getTimeoutInSec() {
        return timeoutInSec;
    }

    public boolean isLockValid() {
        return (System.currentTimeMillis() - lockedAt) / 1000 < timeoutInSec;
    }
}

class LockingManager {
    private Map<Show, Map<String, Lock>> locks = new HashMap<>();
    private int timeoutInSec = 180;

    public void checkAndHoldTheSeats(Show show, List<String> seatIds, User user) {
        synchronized (show) {
            locks.putIfAbsent(show, new HashMap<>());
            Map<String, Lock> seatLocks = locks.get(show);
            
            // Check if any seat is locked by another user
            for (String seatId: seatIds) {
                Lock existingLock = seatLocks.get(seatId);
                if (existingLock != null && existingLock.isLockValid() && 
                    !existingLock.getUser().getId().equals(user.getId())) {
                    throw new IllegalArgumentException("Seat " + seatId + " is on hold, please wait.");
                }
            }

            // Lock all seats for this user
            for (String seatId: seatIds) {
                seatLocks.put(seatId, new Lock(seatId, user, timeoutInSec));
            }
        }
    }

    public void releaseLocks(Show show, List<String> seatIds) {
        synchronized (show) {
            Map<String, Lock> seatLocks = locks.get(show);
            if (seatLocks != null) {
                for (String seatId : seatIds) {
                    seatLocks.remove(seatId);
                }
            }
        }
    }

    public void cleanupExpiredLocks() {
        for (Map<String, Lock> seatLocks : locks.values()) {
            seatLocks.entrySet().removeIf(entry -> !entry.getValue().isLockValid());
        }
    }
}

class PaymentManager {
    private BookingManager bookingManager;
    private Map<String, Payment> payments;

    public PaymentManager(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
        this.payments = new HashMap<>();
    }

    public String initiatePayment(String bookingId) {
        Booking booking = bookingManager.getBooking(bookingId);
        
        if (booking == null) {
            throw new IllegalArgumentException("Invalid booking id");
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new IllegalArgumentException("Booking is not in pending state");
        }

        double amount = booking.getTotalAmount();
        Payment payment = new Payment(booking, amount);
        payments.put(payment.getId(), payment);
        booking.setPayment(payment);
        
        return payment.getId();
    }

    public void processPayment(String paymentId) {
        Payment payment = payments.get(paymentId);
        
        if (payment == null) {
            throw new IllegalArgumentException("Invalid payment id");
        }

        try {
            // Simulate payment processing
            // In real implementation, this would integrate with payment gateway
            payment.confirmPayment();
            bookingManager.confirmBooking(payment.getBooking());
        } catch (Exception e) {
            payment.failPayment();
            bookingManager.failBooking(payment.getBooking().getId());
            throw new RuntimeException("Payment failed: " + e.getMessage());
        }
    }

    public Payment getPayment(String paymentId) {
        return payments.get(paymentId);
    }
}

public class MovieTicketBookingSystem {
    private MovieManager movieManager;
    private TheaterManager theaterManager;
    private ShowManager showManager;
    private UserManager userManager;
    private LockingManager lockingManager;
    private BookingManager bookingManager;
    private PaymentManager paymentManager;

    public MovieTicketBookingSystem() {
        this.movieManager = new MovieManager();
        this.theaterManager = new TheaterManager();
        this.showManager = new ShowManager();
        this.userManager = new UserManager();
        this.lockingManager = new LockingManager();
        this.bookingManager = new BookingManager(showManager, userManager, lockingManager);
        this.paymentManager = new PaymentManager(bookingManager);
    }

    // Getters for all managers
    public MovieManager getMovieManager() { return movieManager; }
    public TheaterManager getTheaterManager() { return theaterManager; }
    public ShowManager getShowManager() { return showManager; }
    public UserManager getUserManager() { return userManager; }
    public BookingManager getBookingManager() { return bookingManager; }
    public PaymentManager getPaymentManager() { return paymentManager; }

    // Main method to run and test the system
    public static void main(String[] args) {
        MovieTicketBookingSystem system = new MovieTicketBookingSystem();
        system.demonstrateCompleteBookingFlow();
    }

    // Complete demonstration of booking flow
    public void demonstrateCompleteBookingFlow() {
        try {
            System.out.println("=== Movie Ticket Booking System Demo ===\n");

            // 1. Create a movie
            System.out.println("1. Creating movie...");
            movieManager.createMovie("Avengers: Endgame", "Epic superhero finale", "English", 180);
            Movie movie = movieManager.getMovies().get(0);
            System.out.println("   Created movie: " + movie.getName());

            // 2. Create theater with location
            System.out.println("\n2. Creating theater...");
            Location location = new Location("123 Mall Road", "110001", "Delhi", "Delhi", "India");
            theaterManager.createTheater("PVR Cinemas", location);
            Theater theater = theaterManager.getAllTheaters().get(0);
            System.out.println("   Created theater: " + theater.getName());

            // 3. Create screen and add seats
            System.out.println("\n3. Creating screen and adding seats...");
            theaterManager.createScreen(theater.getId(), "Screen-1");
            Screen screen = theater.getScreens().get(0);
            
            // Add different types of seats
            List<Seat> seats = new ArrayList<>();
            // Add 10 standard seats
            for (int i = 1; i <= 10; i++) {
                seats.add(new Seat("A" + i, SeatType.STANDARD));
            }
            // Add 5 delux seats
            for (int i = 1; i <= 5; i++) {
                seats.add(new Seat("B" + i, SeatType.DELUX));
            }
            // Add 3 super delux seats
            for (int i = 1; i <= 3; i++) {
                seats.add(new Seat("C" + i, SeatType.SUPER_DELUX));
            }
            
            theaterManager.addSeatsToScreen(screen.getId(), seats);
            System.out.println("   Added " + seats.size() + " seats to screen");

            // 4. Create show
            System.out.println("\n4. Creating show...");
            LocalDateTime startTime = LocalDateTime.now().plusHours(2);
            LocalDateTime endTime = startTime.plusMinutes(movie.getDurationMinutes());
            Show show = showManager.createShow(screen, movie, startTime, endTime);
            System.out.println("   Created show at: " + startTime);

            // 5. Create users
            System.out.println("\n5. Creating users...");
            User user1 = userManager.createUser("John Doe", "john@example.com");
            User user2 = userManager.createUser("Jane Smith", "jane@example.com");
            System.out.println("   Created users: " + user1.getName() + " and " + user2.getName());

            // 6. Display available seats
            System.out.println("\n6. Available seats for the show:");
            List<ShowSeat> availableSeats = showManager.getAllShowSeats(show);
            for (ShowSeat seat : availableSeats) {
                if (seat.isAvailable()) {
                    System.out.println("   Seat " + seat.getSeatId() + 
                        " (" + seat.getSeat().getSeatType() + ") - ₹" + seat.getPrice());
                }
            }

            // 7. Make booking for user1
            System.out.println("\n7. Making booking for " + user1.getName() + "...");
            List<String> selectedSeats1 = List.of("A1", "A2", "B1");
            String bookingId1 = bookingManager.makeBooking(user1.getId(), selectedSeats1, show);
            Booking booking1 = bookingManager.getBooking(bookingId1);
            System.out.println("   Booking created: " + bookingId1);
            System.out.println("   Total amount: ₹" + booking1.getTotalAmount());

            // 8. Process payment for booking1
            System.out.println("\n8. Processing payment...");
            String paymentId1 = paymentManager.initiatePayment(bookingId1);
            System.out.println("   Payment initiated: " + paymentId1);
            
            paymentManager.processPayment(paymentId1);
            System.out.println("   Payment completed successfully!");
            System.out.println("   Booking status: " + booking1.getBookingStatus());

            // 9. Try to book same seats with user2 (should fail)
            System.out.println("\n9. Trying to book same seats with another user...");
            try {
                bookingManager.makeBooking(user2.getId(), List.of("A1", "A2"), show);
            } catch (Exception e) {
                System.out.println("   Expected error: " + e.getMessage());
            }

            // 10. Make successful booking for user2 with different seats
            System.out.println("\n10. Making successful booking for " + user2.getName() + "...");
            List<String> selectedSeats2 = List.of("A3", "C1");
            String bookingId2 = bookingManager.makeBooking(user2.getId(), selectedSeats2, show);
            Booking booking2 = bookingManager.getBooking(bookingId2);
            
            String paymentId2 = paymentManager.initiatePayment(bookingId2);
            paymentManager.processPayment(paymentId2);
            
            System.out.println("   Booking created: " + bookingId2);
            System.out.println("   Total amount: ₹" + booking2.getTotalAmount());
            System.out.println("   Booking status: " + booking2.getBookingStatus());

            // 11. Display final booking summary
            System.out.println("\n=== BOOKING SUMMARY ===");
            System.out.println("Movie: " + movie.getName());
            System.out.println("Theater: " + theater.getName());
            System.out.println("Show time: " + show.getStartTime());
            
            System.out.println("\nBookings made:");
            for (Booking booking : bookingManager.getAllBookings()) {
                System.out.println("- " + booking.getUser().getName() + 
                    " booked " + booking.getSeats().size() + " seats for ₹" + booking.getTotalAmount());
                System.out.println("  Seats: " + booking.getSeats().stream()
                    .map(ShowSeat::getSeatId)
                    .collect(Collectors.joining(", ")));
            }

            // 12. Show remaining available seats
            System.out.println("\nRemaining available seats:");
            long availableCount = showManager.getAllShowSeats(show).stream()
                .mapToLong(seat -> seat.isAvailable() ? 1 : 0)
                .sum();
            System.out.println("Total available seats: " + availableCount + "/" + seats.size());

            System.out.println("\n=== Demo completed successfully! ===");

        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}