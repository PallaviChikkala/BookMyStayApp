/**
 * BookMyStayApp.java
 *
 * This class demonstrates data persistence and system recovery
 * for the Hotel Booking Management System.
 * It serializes booking and inventory state to a file and restores it on restart.
 *
 * @author YourName
 * @version 12.0
 */

import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
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

// Inventory Service (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
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

// Persistence Service
class PersistenceService {
    private static final String FILE_NAME = "system_state.ser";

    public static void saveState(RoomInventory inventory, List<Reservation> reservations) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(reservations);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static Object[] loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            List<Reservation> reservations = (List<Reservation>) ois.readObject();
            System.out.println("\nSystem state loaded successfully.");
            return new Object[]{inventory, reservations};
        } catch (FileNotFoundException e) {
            System.out.println("\nNo previous state found. Starting fresh.");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading system state: " + e.getMessage());
            return null;
        }
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application Name: Book My Stay");
        System.out.println("Version: 12.0\n");

        RoomInventory inventory;
        List<Reservation> reservations;

        // Attempt to load previous state
        Object[] state = PersistenceService.loadState();
        if (state != null) {
            inventory = (RoomInventory) state[0];
            reservations = (List<Reservation>) state[1];
        } else {
            // Initialize fresh state
            inventory = new RoomInventory();
            inventory.addRoomType("Single Room", 2);
            inventory.addRoomType("Double Room", 1);
            inventory.addRoomType("Suite Room", 1);

            reservations = new ArrayList<>();
        }

        // Simulate new reservations
        Reservation r1 = new Reservation("RES-601", "Alice", "Single Room");
        Reservation r2 = new Reservation("RES-602", "Bob", "Suite Room");

        if (inventory.allocateRoom(r1.getRoomType())) reservations.add(r1);
        if (inventory.allocateRoom(r2.getRoomType())) reservations.add(r2);

        // Display reservations and inventory
        System.out.println("\n--- Current Reservations ---");
        for (Reservation r : reservations) {
            r.displayReservation();
        }
        inventory.displayInventory();

        // Save state before shutdown
        PersistenceService.saveState(inventory, reservations);
    }
}