/**
 * UseCase6RoomAllocationService.java
 *
 * This class demonstrates reservation confirmation and room allocation
 * for the Hotel Booking Management System.
 * It ensures inventory consistency and prevents double-booking.
 *
 * @author YourName
 * @version 6.0
 */

import java.util.*;

// Reservation class representing a guest's booking intent
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Booking Request Queue
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    public Reservation getNextRequest() {
        return requestQueue.poll(); // FIFO dequeue
    }

    public boolean hasRequests() {
        return !requestQueue.isEmpty();
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
    private Map<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
    }

    // Generate unique room ID
    private String generateRoomID(String roomType, String guestName) {
        return roomType.substring(0, 3).toUpperCase() + "-" + guestName + "-" + UUID.randomUUID().toString().substring(0, 5);
    }

    // Process a booking request
    public void processRequest(Reservation reservation) {
        String roomType = reservation.getRoomType();
        if (inventory.allocateRoom(roomType)) {
            String roomID = generateRoomID(roomType, reservation.getGuestName());

            allocatedRooms.putIfAbsent(roomType, new HashSet<>());
            allocatedRooms.get(roomType).add(roomID);

            System.out.println("Reservation Confirmed for " + reservation.getGuestName() +
                    " | Room Type: " + roomType +
                    " | Room ID: " + roomID);
        } else {
            System.out.println("Reservation Failed for " + reservation.getGuestName() +
                    " | Room Type: " + roomType +
                    " (No Availability)");
        }
    }

    public void displayAllocatedRooms() {
        System.out.println("\nAllocated Rooms:");
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Room IDs: " + entry.getValue());
        }
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application Name: Book My Stay");
        System.out.println("Version: 6.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Diana", "Single Room"));
        bookingQueue.addRequest(new Reservation("Eve", "Single Room")); // should fail

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Process requests in FIFO order
        while (bookingQueue.hasRequests()) {
            Reservation next = bookingQueue.getNextRequest();
            bookingService.processRequest(next);
        }

        // Display final inventory and allocations
        inventory.displayInventory();
        bookingService.displayAllocatedRooms();
    }
}