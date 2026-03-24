/**
 * UseCase7AddOnServiceSelection.java
 *
 * This class demonstrates add-on service selection for reservations
 * in the Hotel Booking Management System.
 * It models optional services without modifying core booking or inventory logic.
 *
 * @author YourName
 * @version 7.0
 */

import java.util.*;

// Reservation class (simplified for demonstration)
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

// Add-On Service class
class AddOnService {
    private String serviceName;
    private double serviceCost;

    public AddOnService(String serviceName, double serviceCost) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + serviceCost + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Attach service to a reservation
    public void addServiceToReservation(String reservationID, AddOnService service) {
        reservationServices.putIfAbsent(reservationID, new ArrayList<>());
        reservationServices.get(reservationID).add(service);
        System.out.println("Service '" + service.getServiceName() +
                "' added to Reservation ID: " + reservationID);
    }

    // Display services for a reservation
    public void displayServicesForReservation(String reservationID) {
        List<AddOnService> services = reservationServices.getOrDefault(reservationID, new ArrayList<>());
        if (services.isEmpty()) {
            System.out.println("No add-on services selected for Reservation ID: " + reservationID);
        } else {
            System.out.println("Add-On Services for Reservation ID: " + reservationID);
            for (AddOnService service : services) {
                System.out.println(" - " + service);
            }
        }
    }

    // Calculate total additional cost
    public double calculateAdditionalCost(String reservationID) {
        List<AddOnService> services = reservationServices.getOrDefault(reservationID, new ArrayList<>());
        double total = 0;
        for (AddOnService service : services) {
            total += service.getServiceCost();
        }
        return total;
    }
}

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application Name: Book My Stay");
        System.out.println("Version: 7.0\n");

        // Create reservations
        Reservation r1 = new Reservation("RES-101", "Alice", "Single Room");
        Reservation r2 = new Reservation("RES-102", "Bob", "Suite Room");

        r1.displayReservation();
        r2.displayReservation();

        // Initialize Add-On Service Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Define services
        AddOnService breakfast = new AddOnService("Breakfast", 300.0);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 800.0);
        AddOnService spaAccess = new AddOnService("Spa Access", 1200.0);

        // Attach services to reservations
        serviceManager.addServiceToReservation(r1.getReservationID(), breakfast);
        serviceManager.addServiceToReservation(r1.getReservationID(), airportPickup);
        serviceManager.addServiceToReservation(r2.getReservationID(), spaAccess);

        // Display services
        System.out.println();
        serviceManager.displayServicesForReservation(r1.getReservationID());
        System.out.println("Total Additional Cost: ₹" + serviceManager.calculateAdditionalCost(r1.getReservationID()));

        System.out.println();
        serviceManager.displayServicesForReservation(r2.getReservationID());
        System.out.println("Total Additional Cost: ₹" + serviceManager.calculateAdditionalCost(r2.getReservationID()));
    }
}