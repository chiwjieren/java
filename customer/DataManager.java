import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager instance;
    private List<Car> cars;
    private List<Customer> customers;
    private List<Purchase> purchases;
    private Customer currentCustomer;
    
    private static final String CARS_FILE = "cars.dat";
    private static final String CUSTOMERS_FILE = "customers.dat";
    private static final String PURCHASES_FILE = "purchases.dat";
    
    private DataManager() {
        loadData();
        
        // If no data exists, initialize with sample data
        if (cars.isEmpty()) {
            initializeSampleData();
        }
    }
    
    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        cars = loadFromFile(CARS_FILE);
        if (cars == null) cars = new ArrayList<>();
        
        customers = loadFromFile(CUSTOMERS_FILE);
        if (customers == null) customers = new ArrayList<>();
        
        purchases = loadFromFile(PURCHASES_FILE);
        if (purchases == null) purchases = new ArrayList<>();
    }
    
    private <T> List<T> loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename + ". Creating new file on save.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading from file: " + filename);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public void saveData() {
        saveToFile(cars, CARS_FILE);
        saveToFile(customers, CUSTOMERS_FILE);
        saveToFile(purchases, PURCHASES_FILE);
    }
    
    private <T> void saveToFile(List<T> list, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + filename);
            e.printStackTrace();
        }
    }
    
    private void initializeSampleData() {
        // Add sample cars
        cars.add(new Car(1, "Toyota", "Corolla", 2020, 25000, "Sedan", true, "corolla.jpg", "Reliable economy car with good fuel efficiency."));
        cars.add(new Car(2, "Honda", "Civic", 2021, 27000, "Sedan", true, "civic.jpg", "Sporty compact car with modern features."));
        cars.add(new Car(3, "Ford", "Mustang", 2019, 45000, "Sports", true, "mustang.jpg", "Classic American muscle car with powerful engine."));
        cars.add(new Car(4, "Tesla", "Model 3", 2022, 60000, "Electric", true, "model3.jpg", "All-electric sedan with autopilot features."));
        cars.add(new Car(5, "BMW", "X5", 2020, 65000, "SUV", true, "x5.jpg", "Luxury SUV with excellent handling and comfort."));
        cars.add(new Car(6, "Mercedes-Benz", "S-Class", 2021, 110000, "Luxury", true, "sclass.jpg", "Premium luxury sedan with cutting-edge technology."));
        cars.add(new Car(7, "Lamborghini", "Aventador", 2020, 450000, "Supercar", true, "aventador.jpg", "Exotic supercar with V12 engine and scissor doors."));
        cars.add(new Car(8, "Ferrari", "488", 2019, 330000, "Supercar", true, "ferrari488.jpg", "Italian supercar with track-focused performance."));
        cars.add(new Car(9, "Proton", "Saga", 2022, 15000, "Sedan", true, "saga.jpg", "Affordable Malaysian sedan for everyday use."));
        cars.add(new Car(10, "Perodua", "Myvi", 2021, 17000, "Hatchback", true, "myvi.jpg", "Popular Malaysian hatchback with good fuel economy."));
        
        // Save the sample data
        saveData();
    }
    
    // Getters and operations for Cars
    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }
    
    public List<Car> getAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars) {
            if (car.isAvailable()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }
    
    public List<Car> getCarsByPriceRange(double minPrice, double maxPrice) {
        List<Car> filteredCars = new ArrayList<>();
        for (Car car : getAvailableCars()) {
            if (car.getPrice() >= minPrice && car.getPrice() <= maxPrice) {
                filteredCars.add(car);
            }
        }
        return filteredCars;
    }
    
    public List<Car> getLocalCars() {
        List<Car> localCars = new ArrayList<>();
        for (Car car : getAvailableCars()) {
            String make = car.getMake().toLowerCase();
            if (make.equals("proton") || make.equals("perodua")) {
                localCars.add(car);
            }
        }
        return localCars;
    }
    
    public List<Car> getLuxuryCars() {
        return getCarsByPriceRange(200000, 300000);
    }
    
    public List<Car> getAffordableCars() {
        return getCarsByPriceRange(0, 30000);
    }
    
    public Car getCarById(int id) {
        for (Car car : cars) {
            if (car.getCarId() == id) {
                return car;
            }
        }
        return null;
    }
    
    public void updateCar(Car car) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getCarId() == car.getCarId()) {
                cars.set(i, car);
                saveData();
                return;
            }
        }
    }
    
    // Getters and operations for Customers
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }
    
    public Customer getCustomerById(int id) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == id) {
                return customer;
            }
        }
        return null;
    }
    
    public Customer getCustomerByEmail(String email) {
        for (Customer customer : customers) {
            if (customer.getEmail().equalsIgnoreCase(email)) {
                return customer;
            }
        }
        return null;
    }
    
    public boolean authenticateCustomer(String email, String password) {
        Customer customer = getCustomerByEmail(email);
        if (customer != null && customer.getPassword().equals(password)) {
            currentCustomer = customer;
            return true;
        }
        return false;
    }
    
    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveData();
    }
    
    public void updateCustomer(Customer customer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerId() == customer.getCustomerId()) {
                customers.set(i, customer);
                if (currentCustomer != null && currentCustomer.getCustomerId() == customer.getCustomerId()) {
                    currentCustomer = customer;
                }
                saveData();
                return;
            }
        }
    }
    
    // Getters and operations for Purchases
    public List<Purchase> getAllPurchases() {
        return new ArrayList<>(purchases);
    }
    
    public List<Purchase> getPurchasesByCustomer(Customer customer) {
        List<Purchase> customerPurchases = new ArrayList<>();
        for (Purchase purchase : purchases) {
            if (purchase.getCustomer().getCustomerId() == customer.getCustomerId()) {
                customerPurchases.add(purchase);
            }
        }
        return customerPurchases;
    }
    
    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
        purchase.getCustomer().addPurchase(purchase);
        purchase.getCar().setAvailable(false);
        updateCar(purchase.getCar());
        updateCustomer(purchase.getCustomer());
        saveData();
    }
    
    public void updatePurchase(Purchase purchase) {
        for (int i = 0; i < purchases.size(); i++) {
            if (purchases.get(i).getPurchaseId() == purchase.getPurchaseId()) {
                purchases.set(i, purchase);
                saveData();
                return;
            }
        }
    }
    
    // Current user management
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
    
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
    }
    
    public void logout() {
        this.currentCustomer = null;
    }
    
    // Helper methods
    public int getNextCustomerId() {
        int maxId = 0;
        for (Customer customer : customers) {
            if (customer.getCustomerId() > maxId) {
                maxId = customer.getCustomerId();
            }
        }
        return maxId + 1;
    }
    
    public int getNextPurchaseId() {
        int maxId = 0;
        for (Purchase purchase : purchases) {
            if (purchase.getPurchaseId() > maxId) {
                maxId = purchase.getPurchaseId();
            }
        }
        return maxId + 1;
    }
} 