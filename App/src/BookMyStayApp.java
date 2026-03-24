/**
 * UseCase4RoomSearch.java
 *
 * This class demonstrates room search and availability check
 * for the Hotel Booking Management System.
 * It introduces read-only access to inventory and separation of concerns.
 *
 * @author YourName
 * @version 4.0
 */

import java.util.HashMap;
import java.util.Map;

// Abstract Room class
abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double pricePerNight;

    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public abstract void displayRoomDetails();
}

// Concrete room classes
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType() +
                ", Beds: " + getNumberOfBeds() +
                ", Price: ₹" + getPricePerNight());
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 1800.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType() +
                ", Beds: " + getNumberOfBeds() +
                ", Price: ₹" + getPricePerNight());
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 3500.0);
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType() +
                ", Beds: " + getNumberOfBeds() +
                ", Price: ₹" + getPricePerNight());
    }
}

// Inventory class
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

    public Map<String, Integer> getAllAvailability() {
        return new HashMap<>(inventory); // defensive copy
    }
}

// Search service for read-only access
class RoomSearchService {
    private RoomInventory inventory;
    private Room[] rooms;

    public RoomSearchService(RoomInventory inventory, Room[] rooms) {
        this.inventory = inventory;
        this.rooms = rooms;
    }

    // Display only available rooms
    public void displayAvailableRooms() {
        System.out.println("Available Rooms:");
        for (Room room : rooms) {
            int availability = inventory.getAvailability(room.getRoomType());
            if (availability > 0) {
                room.displayRoomDetails();
                System.out.println("Availability: " + availability + " rooms\n");
            }
        }
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application Name: Book My Stay");
        System.out.println("Version: 4.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 0); // deliberately unavailable
        inventory.addRoomType("Suite Room", 2);

        // Initialize room objects
        Room[] rooms = { new SingleRoom(), new DoubleRoom(), new SuiteRoom() };

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory, rooms);

        // Display available rooms (read-only)
        searchService.displayAvailableRooms();
    }
}