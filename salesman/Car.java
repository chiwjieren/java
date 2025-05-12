package salesman;

import java.io.*;
import java.util.*;

public class Car {
    private String carID, type, brand, model, status;
    private int year, mileage;
    private double price;

    public Car(String carID, String type, String brand, String model, int year, double price, String status, int mileage) {
        this.carID = carID;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.status = status;
        this.mileage = mileage;
    }

    // Getters and setters
    public String getCarID() { return carID; }
    public String getType() { return type; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public int getMileage() { return mileage; }

    public void setStatus(String status) { this.status = status; }

    public String getCarInfo() {
        return "Car ID: " + carID +
               "\nType: " + type +
               "\nBrand: " + brand +
               "\nModel: " + model +
               "\nYear: " + year +
               "\nPrice: RM" + price +
               "\nMileage: " + mileage + " km" +
               "\nStatus: " + status;
    }

    public static List<Car> loadCarsFromFile(String filename) {
        List<Car> cars = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 8) {
                    String carID = parts[0];
                    String type = parts[1];
                    String brand = parts[2];
                    String model = parts[3];
                    int year = Integer.parseInt(parts[4]);
                    double price = Double.parseDouble(parts[5]);
                    String status = parts[7];
                    int mileage = Integer.parseInt(parts[6]);
                    cars.add(new Car(carID, type, brand, model, year, price, status, mileage));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading cars: " + e.getMessage());
        }
        return cars;
    }

        public static void saveCarsToFile(List<Car> cars, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Car car : cars) {
                writer.println(String.join(",", car.getCarID(), car.getType(), car.getBrand(), car.getModel(),
                        String.valueOf(car.getYear()), String.valueOf(car.getPrice()),
                        String.valueOf(car.getMileage()), car.getStatus()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
