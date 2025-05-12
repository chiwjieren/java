package customer;

import java.io.Serializable;
import java.util.Date;

public class Purchase implements Serializable {
    private int purchaseId;
    private Customer customer;
    private Car car;
    private Date purchaseDate;
    private double purchaseAmount;
    private int rating;         // Rating from 1-5
    private String feedback;    // Customer feedback
    
    public Purchase(int purchaseId, Customer customer, Car car, Date purchaseDate, double purchaseAmount) {
        this.purchaseId = purchaseId;
        this.customer = customer;
        this.car = car;
        this.purchaseDate = purchaseDate;
        this.purchaseAmount = purchaseAmount;
        this.rating = 0;
        this.feedback = "";
    }

    // Getters and Setters
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "Purchase #" + purchaseId + ": " + car.getYear() + " " + car.getMake() + " " + car.getModel();
    }
} 