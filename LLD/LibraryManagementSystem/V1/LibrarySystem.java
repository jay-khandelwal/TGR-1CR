package LLD.LibraryManagementSystem.V1;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

// Base user class with complete implementation
class User {
    private String userId;
    private String username;
    private String name;
    
    public User(String username, String name) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.name = name;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getName() {
        return name;
    }
}

// Book represents the abstract book entity (title, author, etc.)
class Book {
    private String bookId;
    private String name;
    private String author;
    private String isbn;
    private String category;
    
    public Book(String isbn, String name, String author, String category) {
        this.bookId = UUID.randomUUID().toString();
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.category = category;
    }

    public String getBookId() {
        return bookId;
    }

    public String getName() {
        return name;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public String getCategory() {
        return category;
    }
}

// BookItem represents a physical copy of a book
class BookItem {
    private String barcode;
    private Book book;
    private boolean isAvailable;
    private String location;
    
    public BookItem(Book book, String barcode, String location) {
        this.book = book;
        this.barcode = barcode;
        this.location = location;
        this.isAvailable = true;
    }

    public Book getBook() {
        return book;
    }

    public String getBarcode() {
        return barcode;
    }
    
    public String getLocation() {
        return location;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailability(boolean availability) {
        this.isAvailable = availability;
    }
}

// Librarian extends User with additional permissions
class Librarian extends User {
    private String employeeId;
    
    public Librarian(String username, String name, String employeeId) {
        super(username, name);
        this.employeeId = employeeId;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
}

// Borrower extends User with loan functionality
class Borrower extends User {
    private List<Loan> loans;
    private List<Fine> fines;
    
    public Borrower(String username, String name) {
        super(username, name);
        this.loans = new ArrayList<>();
        this.fines = new ArrayList<>();
    }
    
    public List<Loan> getLoans() {
        return loans;
    }
    
    public List<Fine> getFines() {
        return fines;
    }
    
    public void addLoan(Loan loan) {
        this.loans.add(loan);
    }
    
    public void removeLoan(Loan loan) {
        this.loans.remove(loan);
    }
    
    public void addFine(Fine fine) {
        this.fines.add(fine);
    }
    
    public int getTotalFineAmount() {
        return fines.stream()
                .filter(fine -> !fine.isPaid())
                .mapToInt(Fine::getAmount)
                .sum();
    }
    
    public void payFine(Fine fine, int amount) {
        fine.makePayment(amount);
    }
}

// Loan tracks a book checkout with proper timestamps
class Loan {
    private String loanId;
    private Librarian issuedBy;
    private Borrower borrower;
    private BookItem bookItem;
    private LocalDateTime issuedAt;
    private LocalDateTime dueDate;
    private LocalDateTime returnedAt;
    private List<LocalDateTime> renewalDates;
    private int maxRenewalsAllowed;
    
    public Loan(Librarian issuedBy, Borrower borrower, BookItem bookItem, int loanPeriodDays, int maxRenewals) {
        this.loanId = UUID.randomUUID().toString();
        this.issuedBy = issuedBy;
        this.borrower = borrower;
        this.bookItem = bookItem;
        this.issuedAt = LocalDateTime.now();
        this.dueDate = issuedAt.plusDays(loanPeriodDays);
        this.renewalDates = new ArrayList<>();
        this.maxRenewalsAllowed = maxRenewals;
    }

    public String getLoanId() {
        return loanId;
    }

    public Librarian getIssuedBy() {
        return issuedBy;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public BookItem getBookItem() {
        return bookItem;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }
    
    public List<LocalDateTime> getRenewalDates() {
        return renewalDates;
    }
    
    public boolean isOverdue() {
        if (returnedAt != null) {
            return returnedAt.isAfter(dueDate);
        }
        return LocalDateTime.now().isAfter(dueDate);
    }
    
    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        
        LocalDateTime endDate = (returnedAt != null) ? returnedAt : LocalDateTime.now();
        return ChronoUnit.DAYS.between(dueDate, endDate);
    }

    public void markReturned() {
        this.returnedAt = LocalDateTime.now();
    }

    public boolean canRenew() {
        return renewalDates.size() < maxRenewalsAllowed && !isOverdue();
    }
    
    public boolean renew(int renewalPeriodDays) {
        if (!canRenew()) return false;
        
        LocalDateTime now = LocalDateTime.now();
        renewalDates.add(now);
        this.dueDate = now.plusDays(renewalPeriodDays);
        return true;
    }
}

// Fine with proper implementation
class Fine {
    private String fineId;
    private Loan loan;
    private int amount;
    private boolean isPaid;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    
    public Fine(Loan loan, int amount) {
        this.fineId = UUID.randomUUID().toString();
        this.loan = loan;
        this.amount = amount;
        this.isPaid = false;
        this.createdAt = LocalDateTime.now();
    }
    
    public String getFineId() {
        return fineId;
    }
    
    public Loan getLoan() {
        return loan;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public boolean isPaid() {
        return isPaid;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getPaidAt() {
        return paidAt;
    }
    
    public void makePayment(int paymentAmount) {
        if (paymentAmount >= amount) {
            this.isPaid = true;
            this.paidAt = LocalDateTime.now();
        }
    }
}

// Library configuration with more options
interface LibraryConfig {
    int getMaxLoansAllowed();
    int getLoanPeriodDays();
    int getMaxRenewalsAllowed();
    int getRenewalPeriodDays();
}

class StandardLibraryConfig implements LibraryConfig {
    @Override
    public int getMaxLoansAllowed() {
        return 5;
    }
    
    @Override
    public int getLoanPeriodDays() {
        return 14;
    }
    
    @Override
    public int getMaxRenewalsAllowed() {
        return 2;
    }
    
    @Override
    public int getRenewalPeriodDays() {
        return 7;
    }
}

// Book service interface
interface IBookService {
    void addBook(Book book);
    void addBookItem(Book book, BookItem bookItem);
    List<BookItem> getAvailableBookItems(String bookId);
    Book findBookByIsbn(String isbn);
    BookItem findBookItemByBarcode(String barcode);
    List<Book> searchBooksByName(String name);
    List<Book> searchBooksByAuthor(String author);
    List<Book> searchBooksByCategory(String category);
}

// Enhanced borrow service interface
interface IBorrowService {
    boolean canBorrow(Borrower borrower);
    Loan borrowBook(Librarian librarian, Borrower borrower, BookItem bookItem);
    boolean canRenewLoan(Loan loan);
    boolean renewLoan(Loan loan);
    List<Loan> getOverdueLoans();
}

// Return service with fine calculation
interface IReturnService {
    Fine calculateFine(Loan loan);
    boolean returnBook(Loan loan);
}

// Fine strategy for different fine policies
interface IFineStrategy {
    int calculateFineAmount(Loan loan);
}

// Implementations

class InMemoryBookService implements IBookService {
    private Map<String, Book> booksById = new HashMap<>();
    private Map<String, Book> booksByIsbn = new HashMap<>();
    private Map<String, List<BookItem>> bookItemsByBookId = new HashMap<>();
    private Map<String, BookItem> bookItemsByBarcode = new HashMap<>();
    
    @Override
    public void addBook(Book book) {
        booksById.put(book.getBookId(), book);
        booksByIsbn.put(book.getIsbn(), book);
        bookItemsByBookId.put(book.getBookId(), new ArrayList<>());
    }
    
    @Override
    public void addBookItem(Book book, BookItem bookItem) {
        if (!bookItemsByBookId.containsKey(book.getBookId())) {
            bookItemsByBookId.put(book.getBookId(), new ArrayList<>());
        }
        bookItemsByBookId.get(book.getBookId()).add(bookItem);
        bookItemsByBarcode.put(bookItem.getBarcode(), bookItem);
    }
    
    @Override
    public List<BookItem> getAvailableBookItems(String bookId) {
        if (!bookItemsByBookId.containsKey(bookId)) {
            return new ArrayList<>();
        }
        
        return bookItemsByBookId.get(bookId).stream()
                .filter(BookItem::isAvailable)
                .collect(Collectors.toList());
    }
    
    @Override
    public Book findBookByIsbn(String isbn) {
        return booksByIsbn.get(isbn);
    }
    
    @Override
    public BookItem findBookItemByBarcode(String barcode) {
        return bookItemsByBarcode.get(barcode);
    }
    
    @Override
    public List<Book> searchBooksByName(String name) {
        return booksById.values().stream()
                .filter(book -> book.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> searchBooksByAuthor(String author) {
        return booksById.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> searchBooksByCategory(String category) {
        return booksById.values().stream()
                .filter(book -> book.getCategory().toLowerCase().contains(category.toLowerCase()))
                .collect(Collectors.toList());
    }
}

class StandardBorrowService implements IBorrowService {
    private LibraryConfig config;
    private Map<String, Loan> activeLoans = new HashMap<>();
    
    public StandardBorrowService(LibraryConfig config) {
        this.config = config;
    }
    
    @Override
    public boolean canBorrow(Borrower borrower) {
        // Fixed logic: borrower can borrow if they have fewer loans than the maximum allowed
        return borrower.getLoans().size() < config.getMaxLoansAllowed();
    }
    
    @Override
    public Loan borrowBook(Librarian librarian, Borrower borrower, BookItem bookItem) {
        if (!canBorrow(borrower)) {
            throw new IllegalStateException("Borrower has reached maximum loan limit");
        }
        
        if (!bookItem.isAvailable()) {
            throw new IllegalStateException("Book item is not available for borrowing");
        }
        
        Loan loan = new Loan(librarian, borrower, bookItem, config.getLoanPeriodDays(), config.getMaxRenewalsAllowed());
        bookItem.setAvailability(false);
        borrower.addLoan(loan);
        activeLoans.put(loan.getLoanId(), loan);
        
        return loan;
    }
    
    @Override
    public boolean canRenewLoan(Loan loan) {
        return loan.canRenew();
    }
    
    @Override
    public boolean renewLoan(Loan loan) {
        if (!canRenewLoan(loan)) {
            return false;
        }
        
        return loan.renew(config.getRenewalPeriodDays());
    }
    
    @Override
    public List<Loan> getOverdueLoans() {
        return activeLoans.values().stream()
                .filter(Loan::isOverdue)
                .collect(Collectors.toList());
    }
}

class PerDayFineStrategy implements IFineStrategy {
    private int finePerDay;
    
    public PerDayFineStrategy(int finePerDay) {
        this.finePerDay = finePerDay;
    }
    
    @Override
    public int calculateFineAmount(Loan loan) {
        long daysOverdue = loan.getDaysOverdue();
        return (int) (daysOverdue * finePerDay);
    }
}

class StandardReturnService implements IReturnService {
    private IFineStrategy fineStrategy;
    
    public StandardReturnService(IFineStrategy fineStrategy) {
        this.fineStrategy = fineStrategy;
    }
    
    @Override
    public Fine calculateFine(Loan loan) {
        int fineAmount = fineStrategy.calculateFineAmount(loan);
        
        if (fineAmount > 0) {
            return new Fine(loan, fineAmount);
        }
        
        return null;
    }
    
    @Override
    public boolean returnBook(Loan loan) {
        if (loan.getReturnedAt() != null) {
            return false; // Book already returned
        }
        
        loan.markReturned();
        loan.getBookItem().setAvailability(true);
        
        Fine fine = calculateFine(loan);
        if (fine != null) {
            loan.getBorrower().addFine(fine);
        }
        
        return true;
    }
}

// Main system class that ties everything together
class LibraryManagementSystem {
    private IBookService bookService;
    private IBorrowService borrowService;
    private IReturnService returnService;
    private LibraryConfig config;
    
    private Map<String, Librarian> librarians = new HashMap<>();
    private Map<String, Borrower> borrowers = new HashMap<>();
    
    public LibraryManagementSystem() {
        this.config = new StandardLibraryConfig();
        this.bookService = new InMemoryBookService();
        this.borrowService = new StandardBorrowService(config);
        this.returnService = new StandardReturnService(new PerDayFineStrategy(2)); // $2 per day fine
    }
    
    // User management
    public void registerLibrarian(Librarian librarian) {
        librarians.put(librarian.getUserId(), librarian);
    }
    
    public void registerBorrower(Borrower borrower) {
        borrowers.put(borrower.getUserId(), borrower);
    }
    
    // Book management
    public void addNewBook(Book book) {
        bookService.addBook(book);
    }
    
    public void addBookCopy(Book book, String barcode, String location) {
        BookItem bookItem = new BookItem(book, barcode, location);
        bookService.addBookItem(book, bookItem);
    }
    
    public List<Book> searchBooks(String query) {
        List<Book> results = new ArrayList<>();
        results.addAll(bookService.searchBooksByName(query));
        results.addAll(bookService.searchBooksByAuthor(query));
        return results.stream().distinct().collect(Collectors.toList());
    }
    
    // Loan management
    public Loan checkoutBook(String librarianId, String borrowerId, String bookBarcode) {
        Librarian librarian = librarians.get(librarianId);
        Borrower borrower = borrowers.get(borrowerId);
        
        if (librarian == null || borrower == null) {
            throw new IllegalArgumentException("Invalid librarian or borrower ID");
        }
        
        // Find book item by barcode
        BookItem bookItem = findBookItemByBarcode(bookBarcode);
        
        if (bookItem == null) {
            throw new IllegalArgumentException("Book item not found with barcode: " + bookBarcode);
        }
        
        return borrowService.borrowBook(librarian, borrower, bookItem);
    }
    
    public boolean returnBook(String loanId) {
        Loan loan = findLoanById(loanId);
        
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found with ID: " + loanId);
        }
        
        return returnService.returnBook(loan);
    }
    
    public boolean renewBook(String loanId) {
        Loan loan = findLoanById(loanId);
        
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found with ID: " + loanId);
        }
        
        return borrowService.renewLoan(loan);
    }
    
    // Helper methods
    private BookItem findBookItemByBarcode(String barcode) {
        return bookService.findBookItemByBarcode(barcode);
    }
    
    private Loan findLoanById(String loanId) {
        for (Borrower borrower : borrowers.values()) {
            for (Loan loan : borrower.getLoans()) {
                if (loan.getLoanId().equals(loanId)) {
                    return loan;
                }
            }
        }
        return null;
    }
    
    // Reporting
    public List<Loan> getOverdueLoans() {
        return borrowService.getOverdueLoans();
    }
    
    public int collectFine(String borrowerId, String fineId, int amount) {
        Borrower borrower = borrowers.get(borrowerId);
        
        if (borrower == null) {
            throw new IllegalArgumentException("Borrower not found with ID: " + borrowerId);
        }
        
        Fine fine = borrower.getFines().stream()
                .filter(f -> f.getFineId().equals(fineId))
                .findFirst()
                .orElse(null);
        
        if (fine == null) {
            throw new IllegalArgumentException("Fine not found with ID: " + fineId);
        }
        
        borrower.payFine(fine, amount);
        return borrower.getTotalFineAmount();
    }
}

// Demo class
public class LibrarySystem {
    public static void main(String[] args) {
        // Create library system
        LibraryManagementSystem library = new LibraryManagementSystem();
        
        // Register users
        Librarian librarian = new Librarian("john_lib", "John Smith", "EMP001");
        Borrower borrower = new Borrower("alice_user", "Alice Johnson");
        
        library.registerLibrarian(librarian);
        library.registerBorrower(borrower);
        
        // Add books
        Book book1 = new Book("978-0134685991", "Effective Java", "Joshua Bloch", "Programming");
        Book book2 = new Book("978-0132350884", "Clean Code", "Robert C. Martin", "Programming");
        
        library.addNewBook(book1);
        library.addNewBook(book2);
        
        // Add book copies
        library.addBookCopy(book1, "B001", "Shelf A1");
        library.addBookCopy(book1, "B002", "Shelf A1");
        library.addBookCopy(book2, "B003", "Shelf B2");
        
        // Demo checkout
        try {
            Loan loan = library.checkoutBook(librarian.getUserId(), borrower.getUserId(), "B001");
            System.out.println("Book checked out: " + loan.getBookItem().getBook().getName());
            System.out.println("Due date: " + loan.getDueDate());
            
            // Demo renew
            boolean renewed = library.renewBook(loan.getLoanId());
            System.out.println("Book renewed: " + renewed);
            System.out.println("New due date: " + loan.getDueDate());
            
            // Demo return
            boolean returned = library.returnBook(loan.getLoanId());
            System.out.println("Book returned: " + returned);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}