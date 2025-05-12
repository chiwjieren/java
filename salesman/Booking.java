package salesman;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a booking in the car rental system.
 */
public class Booking {
    private String bookingID;
    private String date;
    private String status;
    private String salesmanID;
    private String customerID;
    private String carID;
    private boolean payment;
    private double price;

    /**
     * Constructs a Booking object.
     */
    public Booking(String bookingID, String date, String status, boolean payment,
                   String salesmanID, String customerID, String carID, double price) {
        this.bookingID = bookingID;
        this.date = date;
        this.status = status;
        this.payment = payment;
        this.salesmanID = salesmanID;
        this.customerID = customerID;
        this.carID = carID;
        this.price = price;
    }

    // Getters
    public String getBookingID() { return bookingID; }
    public String getSalesmanID() { return salesmanID; }
    public String getCarID() { return carID; }
    public boolean getPayment() { return payment; }
    public String getStatus() { return status; }
    public String getDate() { return date; }
    public String getCustomerID() { return customerID; }
    public double getPrice() { return price; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setPayment(boolean payment) { this.payment = payment; }
    public void setPrice(double price) { this.price = price; }

    /**
     * Converts the Booking object to a CSV string format.
     */
    @Override
    public String toString() {
        return String.join(",",
            bookingID,
            date,
            status,
            String.valueOf(payment),
            salesmanID,
            customerID,
            carID,
            String.valueOf(price)
        );
    }

    /**
     * Reads bookings from a file and returns a list of Booking objects.
     */
    public static List<Booking> readBookings(String filename) {
        List<Booking> list = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");
                if (data.length >= 7) {
                    double price = data.length >= 8 ? Double.parseDouble(data[7]) : 0.0;
                    Booking b = new Booking(
                        data[0], data[1], data[2],
                        Boolean.parseBoolean(data[3]),
                        data[4], data[5], data[6],
                        price
                    );
                    list.add(b);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Saves a list of Booking objects to a file.
     */
    public static void saveBookings(String filename, List<Booking> bookings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Booking b : bookings) {
                writer.println(b.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}