/**
 * BookMyStayApp.java
 *
 * This class demonstrates booking cancellation and inventory rollback
 * for the Hotel Booking Management System.
 * It ensures safe reversal of state changes and consistent recovery.
 *
 * @author YourName
 * @version 10.0
 */

import java.util.*;

// Reservation class
class Reservation {
    private String reservationID;
    private String guestName;
    private String roomType;
    private String roomID;
    private boolean isCancelled;

    public Reservation(String reservationID, String guestName, String roomType, String roomID) {
        this.reservationID = reservationID;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomID = roomID;
        this.isCancelled = false;
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

    public String getRoomID() {
        return roomID;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationID +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomID +
                " | Status: " + (isCancelled ? "Cancelled" : "Confirmed"));
    }
}

// Inventory Service
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int availability) {
        inventory.put(roomType, availability);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public boolean allocateRoom(String roomType) {
        int availability = getAvailability(roomType);
        if (availability > 0) {
            inventory.put(roomType, availability - 1);
            return true;
        }
        return false;
    }

    public void restoreRoom(String roomType) {
        inventory.put(roomType, getAvailability(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Availability: " + entry.getValue());
        }
    }
}

// Booking Service
class BookingService {
    private RoomInventory inventory;
    private Map<String, Reservation> reservations;
    private Stack<String> rollbackStack;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.reservations = new HashMap<>();
        this.rollbackStack = new Stack<>();
    }

    // Generate unique room ID
    private String generateRoomID(String roomType, String guestName) {
        return roomType.substring(0, 3).toUpperCase() + "-" + guestName + "-" + UUID.randomUUID().toString().substring(0, 5);
    }

    // Confirm reservation
    public void confirmReservation(String reservationID, String guestName, String roomType) {
        if (inventory.allocateRoom(roomType)) {
            String roomID = generateRoomID(roomType, guestName);
            Reservation reservation = new Reservation(reservationID, guestName, roomType, roomID);
            reservations.put(reservationID, reservation);
            System.out.println("Reservation Confirmed for " + guestName +
                    " | Room Type: " + roomType +
                    " | Room ID: " + roomID);
        } else {
            System.out.println("Reservation Failed for " + guestName +
                    " | Room Type: " + roomType +
                    " (No Availability)");
        }
    }

    // Cancel reservation
    public void cancelReservation(String reservationID) {
        Reservation reservation = reservations.get(reservationID);
        if (reservation == null) {
            System.out.println("Cancellation Failed: Reservation ID " + reservationID + " not found.");
            return;
        }
        if (reservation.isCancelled()) {
            System.out.println("Cancellation Failed: Reservation ID " + reservationID + " already cancelled.");
            return;
        }

        // Perform rollback
        reservation.cancel();
        inventory.restoreRoom(reservation.getRoomType());
        rollbackStack.push(reservation.getRoomID());

        System.out.println("Reservation Cancelled for " + reservation.getGuestName() +
                " | Room Type: " + reservation.getRoomType() +
                " | Room ID: " + reservation.getRoomID());
    }

    // Display all reservations
    public void displayReservations() {
        System.out.println("\n--- Reservations ---");
        for (Reservation reservation : reservations.values()) {
            reservation.displayReservation();
        }
    }

    // Display rollback stack
    public void displayRollbackStack() {
        System.out.println("\n--- Rollback Stack (Released Room IDs) ---");
        for (String roomID : rollbackStack) {
            System.out.println("Released Room ID: " + roomID);
        }
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application Name: Book My Stay");
        System.out.println("Version: 10.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Confirm reservations
        bookingService.confirmReservation("RES-401", "Alice", "Single Room");
        bookingService.confirmReservation("RES-402", "Bob", "Double Room");
        bookingService.confirmReservation("RES-403", "Charlie", "Suite Room");

        // Display reservations and inventory
        bookingService.displayReservations();
        inventory.displayInventory();

        // Cancel reservations
        System.out.println("\n--- Performing Cancellations ---");
        bookingService.cancelReservation("RES-402"); // Bob cancels
        bookingService.cancelReservation("RES-404"); // Invalid ID
        bookingService.cancelReservation("RES-402"); // Duplicate cancellation

        // Display updated reservations, inventory, and rollback stack
        bookingService.displayReservations();
        inventory.displayInventory();
        bookingService.displayRollbackStack();
    }
}