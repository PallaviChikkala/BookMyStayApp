/**
 * UseCase11ConcurrentBookingSimulation.java
 *
 * This class demonstrates concurrent booking simulation with thread safety
 * for the Hotel Booking Management System.
 * It ensures correct room allocation under multi-threaded conditions.
 *
 * @author YourName
 * @version 11.0
 */

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
}

// Inventory Service with synchronized access
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public synchronized void addRoomType(String roomType, int availability) {
        inventory.put(roomType, availability);
    }

    public synchronized int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Thread-safe allocation
    public synchronized boolean allocateRoom(String roomType) {
        int availability = getAvailability(roomType);
        if (availability > 0) {
            inventory.put(roomType, availability - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Availability: " + entry.getValue());
        }
    }
}

// Booking Service with synchronized allocation
class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processReservation(Reservation reservation) {
        synchronized (inventory) {
            if (inventory.allocateRoom(reservation.getRoomType())) {
                System.out.println("Reservation Confirmed for " + reservation.getGuestName() +
                        " | Room Type: " + reservation.getRoomType() +
                        " | Reservation ID: " + reservation.getReservationID());
            } else {
                System.out.println("Reservation Failed for " + reservation.getGuestName() +
                        " | Room Type: " + reservation.getRoomType() +
                        " (No Availability)");
            }
        }
    }
}

// Runnable task for concurrent booking
class BookingTask implements Runnable {
    private BookingService bookingService;
    private Reservation reservation;

    public BookingTask(BookingService bookingService, Reservation reservation) {
        this.bookingService = bookingService;
        this.reservation = reservation;
    }

    @Override
    public void run() {
        bookingService.processReservation(reservation);
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application Name: Book My Stay");
        System.out.println("Version: 11.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Simulate concurrent booking requests
        Thread t1 = new Thread(new BookingTask(bookingService, new Reservation("RES-501", "Alice", "Single Room")));
        Thread t2 = new Thread(new BookingTask(bookingService, new Reservation("RES-502", "Bob", "Single Room")));
        Thread t3 = new Thread(new BookingTask(bookingService, new Reservation("RES-503", "Charlie", "Single Room"))); // should fail
        Thread t4 = new Thread(new BookingTask(bookingService, new Reservation("RES-504", "Diana", "Double Room")));
        Thread t5 = new Thread(new BookingTask(bookingService, new Reservation("RES-505", "Eve", "Double Room"))); // should fail

        // Start threads simultaneously
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        // Wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Display final inventory
        inventory.displayInventory();
    }
}