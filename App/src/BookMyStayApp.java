
import java.util.*;

// Reservation class
class Reservation {
    private String reservationID;
    private String guestName;
    private String roomType;

    public Reservation(String reservationID, String guestName, String roomType) {
        this.reservationID = reservationID;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationID() {
        return reservationID;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationID +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// Booking History class
class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation to history
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Reservation stored in history: " + reservation.getReservationID());
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(history); // defensive copy
    }
}

// Booking Report Service
class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all reservations in chronological order
    public void displayBookingHistory() {
        System.out.println("\n--- Booking History ---");
        for (Reservation reservation : history.getAllReservations()) {
            reservation.displayReservation();
        }
    }

    // Generate summary report by room type
    public void generateSummaryReport() {
        System.out.println("\n--- Booking Summary Report ---");
        Map<String, Integer> summary = new HashMap<>();
        for (Reservation reservation : history.getAllReservations()) {
            summary.put(reservation.getRoomType(),
                    summary.getOrDefault(reservation.getRoomType(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Total Bookings: " + entry.getValue());
        }
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application Name: Book My Stay");
        System.out.println("Version: 8.0\n");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed reservations
        Reservation r1 = new Reservation("RES-201", "Alice", "Single Room");
        Reservation r2 = new Reservation("RES-202", "Bob", "Suite Room");
        Reservation r3 = new Reservation("RES-203", "Charlie", "Single Room");
        Reservation r4 = new Reservation("RES-204", "Diana", "Double Room");

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);
        history.addReservation(r4);

        // Initialize report service
        BookingReportService reportService = new BookingReportService(history);

        // Display booking history
        reportService.displayBookingHistory();

        // Generate summary report
        reportService.generateSummaryReport();
    }
}