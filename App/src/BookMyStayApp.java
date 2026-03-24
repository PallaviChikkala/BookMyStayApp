
import java.util.HashMap;
import java.util.Map;

// Inventory class responsible for managing room availability
class RoomInventory {
    private Map<String, Integer> inventory;

    // Constructor initializes the inventory
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Register a room type with its availability
    public void addRoomType(String roomType, int availability) {
        inventory.put(roomType, availability);
    }

    // Retrieve availability for a given room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (e.g., after booking or cancellation)
    public void updateAvailability(String roomType, int newAvailability) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newAvailability);
        } else {
            System.out.println("Room type not found in inventory: " + roomType);
        }
    }

    // Display current inventory state
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Availability: " + entry.getValue());
        }
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application Name: Book My Stay");
        System.out.println("Version: 3.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types with availability
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 2);

        // Display inventory
        inventory.displayInventory();

        // Example update: one Single Room booked
        System.out.println("\nBooking 1 Single Room...");
        int currentSingleAvailability = inventory.getAvailability("Single Room");
        inventory.updateAvailability("Single Room", currentSingleAvailability - 1);

        // Display updated inventory
        inventory.displayInventory();
    }
}