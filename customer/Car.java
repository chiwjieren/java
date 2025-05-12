package customer;

import java.io.Serializable;

public class Car implements Serializable {
    private int carId;
    private String make;
    private String model;
    private int year;
    private double price;
    private String type;
    private boolean isAvailable;
    private String imageUrl;
    private String description;

    public Car(int carId, String make, String model, int year, double price, String type, boolean isAvailable, String imageUrl, String description) {
        this.carId = carId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.type = type;
        this.isAvailable = isAvailable;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Getters and Setters
    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return year + " " + make + " " + model;
    }
} 