/**
 * BookMyStayApp.java
 *
 * This class demonstrates error handling and validation
 * for the Hotel Booking Management System.
 * It validates booking requests and prevents invalid state changes.
 *
 * @author YourName
 * @version 9.0
 */

import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

// Inventory Service with validation
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int availability) {
        inventory.put(roomType, availability);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    // Validate and allocate room
    public void allocateRoom(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        int availability = inventory.get(roomType);
        if (availability <= 0) {
            throw new InvalidBookingException("No availability for room type: " + roomType);
        }
        inventory.put(roomType, availability - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Availability: " + entry.getValue());
        }
    }
}

// Booking Service with validation
class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void confirmReservation(Reservation reservation) {
        try {
            inventory.allocateRoom(reservation.getRoomType());
            System.out.println("Reservation Confirmed for " + reservation.getGuestName() +
                    " | Room Type: " + reservation.getRoomType() +
                    " | Reservation ID: " + reservation.getReservationID());
        } catch (InvalidBookingException e) {
            System.out.println("Reservation Failed for " + reservation.getGuestName() +
                    " | Reason: " + e.getMessage());
        }
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application Name: Book My Stay");
        System.out.println("Version: 9.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 0); // deliberately unavailable
        inventory.addRoomType("Suite Room", 2);

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Simulate reservations with validation
        Reservation r1 = new Reservation("RES-301", "Alice", "Single Room");
        Reservation r2 = new Reservation("RES-302", "Bob", "Double Room"); // should fail
        Reservation r3 = new Reservation("RES-303", "Charlie", "Suite Room");
        Reservation r4 = new Reservation("RES-304", "Diana", "Penthouse"); // invalid type

        bookingService.confirmReservation(r1);
        bookingService.confirmReservation(r2);
        bookingService.confirmReservation(r3);
        bookingService.confirmReservation(r4);

        // Display final inventory
        inventory.displayInventory();
    }
}