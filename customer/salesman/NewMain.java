package oop.assignment;

import java.util.List;

public class NewMain {
    public static void main(String[] args) {
        // Load salesmen and cars from file or create manually
        List<Salesman> salesmenList = Salesman.loadSalesmen("salesmen.txt"); // Implement this if not yet done
        List<Car> carList = Car.loadCarsFromFile("cars.txt"); // Implement this if not yet done

        // For testing: use first salesman as the logged-in user
        if (salesmenList != null && !salesmenList.isEmpty()) {
            Salesman currentSalesman = salesmenList.get(0); // Or perform actual login logic
            new SalesmanDashboard(currentSalesman, salesmenList, carList); // Launch the dashboard
        } else {
            System.out.println("No salesman data found. Please register a salesman first.");
        }
    }
}
