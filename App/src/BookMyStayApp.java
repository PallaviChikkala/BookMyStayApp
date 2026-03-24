
abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double pricePerNight;

    // Constructor
    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    // Getter methods
    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    // Abstract method to display room details
    public abstract void displayRoomDetails();
}

// Concrete class for Single Room
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

// Concrete class for Double Room
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

// Concrete class for Suite Room
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

// Application entry point
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking System!");
        System.out.println("Application Name: Book My Stay");
        System.out.println("Version: 2.0\n");

        // Initialize room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;

        // Display room details and availability
        single.displayRoomDetails();
        System.out.println("Availability: " + singleRoomAvailability + " rooms\n");

        doubleRoom.displayRoomDetails();
        System.out.println("Availability: " + doubleRoomAvailability + " rooms\n");

        suite.displayRoomDetails();
        System.out.println("Availability: " + suiteRoomAvailability + " rooms\n");
    }
}