package LLD.HotelReservationSystem.V1.Approach_2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// Custom exceptions for robust error handling
class RoomNotAvailableException extends Exception {
    public RoomNotAvailableException(String message) {
        super(message);
    }
}

class DateOverlapException extends Exception {
    public DateOverlapException(String message) {
        super(message);
    }
}

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class BookingNotFoundException extends Exception {
    public BookingNotFoundException(String message) {
        super(message);
    }
}

class PaymentFailedException extends Exception {
    public PaymentFailedException(String message) {
        super(message);
    }
}

// Room class to represent a hotel room
class Room {
    private final String roomNumber;
    private final RoomType roomType;

    public Room(String roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Room [Number=" + roomNumber + ", Type=" + roomType.getName() + "]";
    }
}

// RoomType class to define room categories
class RoomType {
    private final String type;
    private final String name;
    private final String description;
    private final int amount;

    public RoomType(String type, String name, String description, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.type = type;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }
}

// Guest class to represent a hotel guest
class Guest {
    private final String id;
    private final String name;
    private final String email;
    private final String phoneNumber;

    public Guest(String name, String email, String phoneNumber) {
        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty() || phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Name, email, and phone number cannot be empty");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Guest [ID=" + id + ", Name=" + name + ", Email=" + email + ", Phone=" + phoneNumber + "]";
    }
}

// Booking class to represent a room booking
class Booking {
    private final String id;
    private final Guest guest;
    private final Room room;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public Booking(Guest guest, Room room, LocalDate fromDate, LocalDate toDate) {
        this.id = UUID.randomUUID().toString();
        this.guest = guest;
        this.room = room;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getId() {
        return id;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public int getTotalAmount() {
        long days = fromDate.until(toDate).getDays();
        return (int) (days * room.getRoomType().getAmount());
    }

    @Override
    public String toString() {
        return "Booking [ID=" + id + ", Guest=" + guest.getName() + ", Room=" + room.getRoomNumber() +
               ", From=" + fromDate + ", To=" + toDate + ", Amount=$" + getTotalAmount() + "]";
    }
}

// Payment class to handle payment details
class Payment {
    private final String id;
    private final PaymentMode paymentMode;
    private final String transactionId;
    private final PaymentStatus status;

    public Payment(PaymentMode paymentMode, String transactionId, PaymentStatus status) {
        this.id = UUID.randomUUID().toString();
        this.paymentMode = paymentMode;
        this.transactionId = transactionId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Payment [ID=" + id + ", Mode=" + paymentMode + ", Transaction=" + transactionId + ", Status=" + status + "]";
    }
}

// Enums for payment mode and status
enum PaymentMode {
    CASH, CREDIT_CARD, DEBIT_CARD, UPI
}

enum PaymentStatus {
    SUCCESS, FAILED, PENDING
}

// Invoice class to generate billing details
class Invoice {
    private final String id;
    private final Booking booking;
    private final Payment payment;
    private final LocalDate date;

    public Invoice(Booking booking, Payment payment, LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.booking = booking;
        this.payment = payment;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public Booking getBooking() {
        return booking;
    }

    public Payment getPayment() {
        return payment;
    }

    public int getAmount() {
        return booking.getTotalAmount();
    }

    public LocalDate getDate() {
        return date;
    }

    public String getInvoiceDetails() {
        return "Invoice [ID=" + id + ", Booking ID=" + booking.getId() + ", Guest=" + booking.getGuest().getName() +
               ", Room=" + booking.getRoom().getRoomNumber() + ", Amount=$" + getAmount() +
               ", Payment ID=" + payment.getId() + ", Date=" + date + "]";
    }
}

// Staff class to represent hotel staff
class Staff {
    private final String staffId;
    private final String name;

    public Staff(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff name cannot be empty");
        }
        this.staffId = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Staff [ID=" + staffId + ", Name=" + name + "]";
    }
}

// RoomService to manage rooms
class RoomService {
    private final List<Room> rooms;

    public RoomService() {
        rooms = new ArrayList<>();
    }

    public void addRoom(Staff staff, String roomNumber, RoomType roomType) {
        Room room = new Room(roomNumber, roomType);
        rooms.add(room);
        System.out.println("Room added by " + staff.getName() + ": " + room);
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public List<Room> findAvailableRooms(RoomType roomType, LocalDate fromDate, LocalDate toDate, List<Booking> bookings)
            throws InvalidBookingException {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate) || fromDate.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Invalid booking dates");
        }
        return rooms.stream()
                .filter(room -> room.getRoomType().equals(roomType) && isRoomAvailable(room, fromDate, toDate, bookings))
                .collect(Collectors.toList());
    }

    private boolean isRoomAvailable(Room room, LocalDate fromDate, LocalDate toDate, List<Booking> bookings) {
        return bookings.stream()
                .filter(booking -> booking.getRoom().equals(room))
                .noneMatch(booking -> !(toDate.isBefore(booking.getFromDate()) || fromDate.isAfter(booking.getToDate())));
    }
}

// GuestService to manage guests
class GuestService {
    private final List<Guest> guests;

    public GuestService() {
        guests = new ArrayList<>();
    }

    public Guest registerGuest(Staff staff, String name, String email, String phoneNumber) {
        Guest guest = new Guest(name, email, phoneNumber);
        guests.add(guest);
        System.out.println("Guest registered by " + staff.getName() + ": " + guest);
        return guest;
    }

    public List<Guest> getAllGuests() {
        return new ArrayList<>(guests);
    }
}

// BookingService to manage bookings
class BookingService {
    private final List<Booking> bookings;
    private final RoomService roomService;

    public BookingService(RoomService roomService) {
        this.roomService = roomService;
        this.bookings = new ArrayList<>();
    }

    public Booking createBooking(Staff staff, Guest guest, RoomType roomType, LocalDate fromDate, LocalDate toDate)
            throws RoomNotAvailableException, InvalidBookingException, DateOverlapException {
        if (guest == null || roomType == null) {
            throw new InvalidBookingException("Guest and room type cannot be null");
        }
        List<Room> availableRooms = roomService.findAvailableRooms(roomType, fromDate, toDate, bookings);
        if (availableRooms.isEmpty()) {
            throw new RoomNotAvailableException("No available rooms of type " + roomType.getName() + " for the selected dates");
        }

        Room room = availableRooms.get(0); // Select the first available room
        Booking booking = new Booking(guest, room, fromDate, toDate);
        bookings.add(booking);
        System.out.println("Booking created by " + staff.getName() + ": " + booking);
        return booking;
    }

    public void cancelBooking(Staff staff, String bookingId) throws BookingNotFoundException {
        Booking booking = bookings.stream()
                .filter(b -> b.getId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: " + bookingId));
        bookings.remove(booking);
        System.out.println("Booking cancelled by " + staff.getName() + ": " + booking);
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }
}

// PaymentService to manage payments
class PaymentService {
    private final List<Payment> payments;

    public PaymentService() {
        payments = new ArrayList<>();
    }

    public Payment makePayment(Staff staff, int amount, PaymentMode paymentMode, String transactionId, PaymentStatus status)
            throws PaymentFailedException {
        if (amount <= 0) {
            throw new PaymentFailedException("Payment amount must be positive");
        }
        if (status == PaymentStatus.FAILED) {
            throw new PaymentFailedException("Payment failed for transaction: " + transactionId);
        }
        Payment payment = new Payment(paymentMode, transactionId, status);
        payments.add(payment);
        System.out.println("Payment processed by " + staff.getName() + ": " + payment);
        return payment;
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments);
    }
}

// InvoiceService to manage invoices
class InvoiceService {
    private final List<Invoice> invoices;

    public InvoiceService() {
        invoices = new ArrayList<>();
    }

    public Invoice generateInvoice(Staff staff, Booking booking, Payment payment) {
        Invoice invoice = new Invoice(booking, payment, LocalDate.now());
        invoices.add(invoice);
        System.out.println("Invoice generated by " + staff.getName() + ": " + invoice.getInvoiceDetails());
        return invoice;
    }

    public List<Invoice> getAllInvoices() {
        return new ArrayList<>(invoices);
    }
}

// StaffService to manage staff
class StaffService {
    private final List<Staff> staff;

    public StaffService() {
        staff = new ArrayList<>();
    }

    public Staff registerStaff(String name) {
        Staff staffMember = new Staff(name);
        staff.add(staffMember);
        System.out.println("Staff registered: " + staffMember);
        return staffMember;
    }

    public List<Staff> getAllStaff() {
        return new ArrayList<>(staff);
    }
}

// Facade to coordinate staff operations
class HotelManagementFacade {
    private final RoomService roomService;
    private final GuestService guestService;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final InvoiceService invoiceService;
    private final StaffService staffService;

    public HotelManagementFacade() {
        this.roomService = new RoomService();
        this.guestService = new GuestService();
        this.bookingService = new BookingService(roomService);
        this.paymentService = new PaymentService();
        this.invoiceService = new InvoiceService();
        this.staffService = new StaffService();
    }

    public Staff registerStaff(String name) {
        return staffService.registerStaff(name);
    }

    public void addRoom(Staff staff, String roomNumber, RoomType roomType) {
        roomService.addRoom(staff, roomNumber, roomType);
    }

    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    public Guest registerGuest(Staff staff, String name, String email, String phoneNumber) {
        return guestService.registerGuest(staff, name, email, phoneNumber);
    }

    public List<Guest> getAllGuests() {
        return guestService.getAllGuests();
    }

    public Booking createBooking(Staff staff, Guest guest, RoomType roomType, LocalDate fromDate, LocalDate toDate)
            throws RoomNotAvailableException, InvalidBookingException, DateOverlapException {
        return bookingService.createBooking(staff, guest, roomType, fromDate, toDate);
    }

    public void cancelBooking(Staff staff, String bookingId) throws BookingNotFoundException {
        bookingService.cancelBooking(staff, bookingId);
    }

    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    public Payment makePayment(Staff staff, int amount, PaymentMode paymentMode, String transactionId, PaymentStatus status)
            throws PaymentFailedException {
        return paymentService.makePayment(staff, amount, paymentMode, transactionId, status);
    }

    public Invoice generateInvoice(Staff staff, Booking booking, Payment payment) {
        return invoiceService.generateInvoice(staff, booking, payment);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }
}

// Main class to simulate staff operations
public class Main {
    public static void main(String[] args) {
        HotelManagementFacade hms = new HotelManagementFacade();

        try {
            // Register staff
            Staff staff1 = hms.registerStaff("Alice Johnson");
            Staff staff2 = hms.registerStaff("Bob Smith");

            // Create room types
            RoomType deluxeRoom = new RoomType("Deluxe", "Deluxe Room", "A luxurious room with all amenities", 200);
            RoomType standardRoom = new RoomType("Standard", "Standard Room", "A comfortable room with basic amenities", 100);

            // Add rooms
            hms.addRoom(staff1, "101", deluxeRoom);
            hms.addRoom(staff1, "102", standardRoom);
            hms.addRoom(staff1, "103", deluxeRoom); // Additional Deluxe room

            // Display all rooms
            System.out.println("\nAll Rooms:");
            hms.getAllRooms().forEach(System.out::println);

            // Register guests
            Guest guest1 = hms.registerGuest(staff1, "John Doe", "john@aa.com", "1234567890");
            Guest guest2 = hms.registerGuest(staff2, "Jane Doe", "jane@gmail.com", "0987654321");
            Guest guest3 = hms.registerGuest(staff1, "Mike Brown", "mike@example.com", "1122334455");

            // Display all guests
            System.out.println("\nAll Guests:");
            hms.getAllGuests().forEach(System.out::println);

            // Create bookings (multiple bookings for Deluxe room)
            Booking booking1 = hms.createBooking(
                    staff1,
                    guest1,
                    deluxeRoom,
                    LocalDate.of(2025, 5, 10),
                    LocalDate.of(2025, 5, 12)
            );
            Booking booking2 = hms.createBooking(
                    staff2,
                    guest2,
                    deluxeRoom,
                    LocalDate.of(2025, 5, 15),
                    LocalDate.of(2025, 5, 17)
            );
            Booking booking3 = hms.createBooking(
                    staff1,
                    guest3,
                    standardRoom,
                    LocalDate.of(2025, 5, 10),
                    LocalDate.of(2025, 5, 12)
            );

            // Attempt an overlapping booking (should fail)
            try {
                hms.createBooking(
                        staff2,
                        guest2,
                        deluxeRoom,
                        LocalDate.of(2025, 5, 11),
                        LocalDate.of(2025, 5, 13)
                );
            } catch (DateOverlapException e) {
                System.out.println("Expected error: " + e.getMessage());
            }

            // Display all bookings
            System.out.println("\nAll Bookings:");
            hms.getAllBookings().forEach(System.out::println);

            // Process payments and generate invoices
            Payment payment1 = hms.makePayment(staff1, booking1.getTotalAmount(), PaymentMode.CREDIT_CARD, "TXN123", PaymentStatus.SUCCESS);
            hms.generateInvoice(staff1, booking1, payment1);

            Payment payment2 = hms.makePayment(staff2, booking2.getTotalAmount(), PaymentMode.UPI, "TXN124", PaymentStatus.SUCCESS);
            hms.generateInvoice(staff2, booking2, payment2);

            Payment payment3 = hms.makePayment(staff1, booking3.getTotalAmount(), PaymentMode.CASH, "TXN125", PaymentStatus.SUCCESS);
            hms.generateInvoice(staff1, booking3, payment3);

            // Attempt a failed payment
            try {
                hms.makePayment(staff2, booking2.getTotalAmount(), PaymentMode.DEBIT_CARD, "TXN126", PaymentStatus.FAILED);
            } catch (PaymentFailedException e) {
                System.out.println("Expected error: " + e.getMessage());
            }

            // Display all invoices
            System.out.println("\nAll Invoices:");
            hms.getAllInvoices().forEach(invoice -> System.out.println(invoice.getInvoiceDetails()));

            // Cancel a booking
            hms.cancelBooking(staff1, booking1.getId());

            // Create a new booking for the same dates after cancellation
            Booking booking4 = hms.createBooking(
                    staff1,
                    guest3,
                    deluxeRoom,
                    LocalDate.of(2025, 5, 10),
                    LocalDate.of(2025, 5, 12)
            );
            Payment payment4 = hms.makePayment(staff1, booking4.getTotalAmount(), PaymentMode.CREDIT_CARD, "TXN127", PaymentStatus.SUCCESS);
            hms.generateInvoice(staff1, booking4, payment4);

            // Display updated bookings and invoices
            System.out.println("\nAll Bookings After Cancellation and New Booking:");
            hms.getAllBookings().forEach(System.out::println);

            System.out.println("\nAll Invoices After New Booking:");
            hms.getAllInvoices().forEach(invoice -> System.out.println(invoice.getInvoiceDetails()));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}