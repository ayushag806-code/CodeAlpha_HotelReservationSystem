package CodeAlpha_HotelReservationSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Room Class managing room categories and availability
class Room {
    private int roomNumber;
    private String category; // Standard, Deluxe, Suite
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomNumber, String category, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public int getRoomNumber() { return roomNumber; }
    public String getCategory() { return category; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { isAvailable = available; }
}

// Reservation Class representing a booking transaction
class Reservation {
    private String bookingId;
    private String guestName;
    private int roomNumber;
    private int nights;
    private double totalAmount;

    public Reservation(String bookingId, String guestName, int roomNumber, int nights, double totalAmount) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.nights = nights;
        this.totalAmount = totalAmount;
    }

    public String getBookingId() { return bookingId; }
    public String getGuestName() { return guestName; }
    public int getRoomNumber() { return roomNumber; }
    public int getNights() { return nights; }
    public double getTotalAmount() { return totalAmount; }

    public void displayBookingDetails() {
        System.out.println("\n--- RESERVATION TICKET ---");
        System.out.println("Booking ID  : " + bookingId);
        System.out.println("Guest Name  : " + guestName);
        System.out.println("Room Number : " + roomNumber);
        System.out.println("Nights      : " + nights);
        System.out.printf("Total Paid  : $%.2f\n", totalAmount);
        System.out.println("--------------------------");
    }
}

// Main Hotel Reservation System Platform
public class HotelReservationSystem {
    private static List<Room> rooms = new ArrayList<>();
    private static Map<String, Reservation> reservations = new HashMap<>();
    private static int bookingCounter = 1001;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize Hotel Rooms
        rooms.add(new Room(101, "Standard", 80.0));
        rooms.add(new Room(102, "Standard", 80.0));
        rooms.add(new Room(201, "Deluxe", 150.0));
        rooms.add(new Room(202, "Deluxe", 150.0));
        rooms.add(new Room(301, "Suite", 250.0));

        while (true) {
            System.out.println("\n=== CODEALPHA HOTEL RESERVATION SYSTEM ===");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Search Rooms by Category");
            System.out.println("3. Make a Reservation (Book Room)");
            System.out.println("4. View Booking Details");
            System.out.println("5. Cancel Reservation");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 6.");
                continue;
            }

            switch (choice) {
                case 1:
                    displayAllRooms();
                    break;

                case 2:
                    System.out.print("Enter Category (Standard, Deluxe, Suite): ");
                    String catQuery = scanner.nextLine().trim();
                    searchRoomsByCategory(catQuery);
                    break;

                case 3:
                    makeReservation(scanner);
                    break;

                case 4:
                    viewBooking(scanner);
                    break;

                case 5:
                    cancelReservation(scanner);
                    break;

                case 6:
                    System.out.println("Exiting Hotel System. Good luck with your CodeAlpha internship!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please choose between 1 and 6.");
            }
        }
    }

    private static void displayAllRooms() {
        System.out.println("\n--- ALL HOTEL ROOMS ---");
        System.out.printf("%-10s %-12s %-15s %-12s\n", "Room No", "Category", "Price/Night", "Status");
        System.out.println("--------------------------------------------------");
        for (Room r : rooms) {
            String status = r.isAvailable() ? "Available" : "Booked";
            System.out.printf("%-10d %-12s $%-14.2f %-12s\n", r.getRoomNumber(), r.getCategory(), r.getPricePerNight(), status);
        }
    }

    private static void searchRoomsByCategory(String category) {
        System.out.println("\n--- ROOMS IN CATEGORY: " + category.toUpperCase() + " ---");
        boolean found = false;
        System.out.printf("%-10s %-15s %-12s\n", "Room No", "Price/Night", "Status");
        System.out.println("----------------------------------------");
        for (Room r : rooms) {
            if (r.getCategory().equalsIgnoreCase(category)) {
                found = true;
                String status = r.isAvailable() ? "Available" : "Booked";
                System.out.printf("%-10d $%-14.2f %-12s\n", r.getRoomNumber(), r.getPricePerNight(), status);
            }
        }
        if (!found) {
            System.out.println("No rooms found matching that category.");
        }
    }

    private static void makeReservation(Scanner scanner) {
        displayAllRooms();
        System.out.print("\nEnter Room Number to book: ");
        int roomNum;
        try {
            roomNum = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid room number format.");
            return;
        }

        Room selectedRoom = null;
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNum) {
                selectedRoom = r;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room number does not exist.");
            return;
        }

        if (!selectedRoom.isAvailable()) {
            System.out.println("Sorry, this room is already booked!");
            return;
        }

        System.out.print("Enter Guest Full Name: ");
        String guestName = scanner.nextLine().trim();
        if (guestName.isEmpty()) {
            System.out.println("Guest name cannot be blank.");
            return;
        }

        System.out.print("Enter number of nights: ");
        int nights;
        try {
            nights = Integer.parseInt(scanner.nextLine());
            if (nights <= 0) {
                System.out.println("Nights must be at least 1.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
            return;
        }

        double totalAmount = nights * selectedRoom.getPricePerNight();
        System.out.printf("Total Cost for %d night(s): $%.2f\n", nights, totalAmount);
        System.out.print("Simulate Payment? (yes/no): ");
        String payConfirm = scanner.nextLine().trim();

        if (payConfirm.equalsIgnoreCase("yes") || payConfirm.equalsIgnoreCase("y")) {
            selectedRoom.setAvailable(false);
            String bookingId = "BK" + (bookingCounter++);
            Reservation newRes = new Reservation(bookingId, guestName, roomNum, nights, totalAmount);
            reservations.put(bookingId, newRes);

            System.out.println("\nPayment Successful! Room Booked.");
            newRes.displayBookingDetails();
        } else {
            System.out.println("Payment cancelled. Reservation aborted.");
        }
    }

    private static void viewBooking(Scanner scanner) {
        System.out.print("Enter Booking ID (e.g., BK1001): ");
        String bId = scanner.nextLine().trim().toUpperCase();
        if (reservations.containsKey(bId)) {
            reservations.get(bId).displayBookingDetails();
        } else {
            System.out.println("No booking found with ID: " + bId);
        }
    }

    private static void cancelReservation(Scanner scanner) {
        System.out.print("Enter Booking ID to cancel: ");
        String bId = scanner.nextLine().trim().toUpperCase();
        if (reservations.containsKey(bId)) {
            Reservation res = reservations.get(bId);
            // Free up the room
            for (Room r : rooms) {
                if (r.getRoomNumber() == res.getRoomNumber()) {
                    r.setAvailable(true);
                    break;
                }
            }
            reservations.remove(bId);
            System.out.println("Reservation " + bId + " has been successfully cancelled and the room is now available.");
        } else {
            System.out.println("Booking ID not found.");
        }
    }
}