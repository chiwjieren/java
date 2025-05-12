package oop.assignment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class viewSales {
    private Salesman salesman;
    private List<Booking> bookings;
    private List<Car> cars;

    public viewSales(Salesman salesman, List<Booking> bookings, List<Car> cars) {
        this.salesman = salesman;
        this.bookings = bookings;
        this.cars = cars;
        showSalesDialog();
    }

    private List<Booking> getBookingsForSalesman() {
        List<Booking> filtered = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getSalesmanID().equals(salesman.getID())) {
                filtered.add(b);
            }
        }
        return filtered;
    }

    private void showSalesDialog() {
        List<Booking> myBookings = getBookingsForSalesman();
        
        if (myBookings.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sales found for you.", "My Sales", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Define column names
        String[] columnNames = {"Booking ID", "Car ID", "Brand/Model", "Customer ID", 
                               "Price (RM)", "Booking Date", "Status"};
        
        // Create table data
        Object[][] rowData = new Object[myBookings.size()][columnNames.length];
        for (int i = 0; i < myBookings.size(); i++) {
            Booking booking = myBookings.get(i);
            Car car = findCarById(booking.getCarID());
            String carInfo = (car != null) ? car.getBrand() + " " + car.getModel() : "N/A";
            double carPrice = (car != null) ? car.getPrice() : 0.0;
            
            rowData[i][0] = booking.getBookingID();
            rowData[i][1] = booking.getCarID();
            rowData[i][2] = carInfo;
            rowData[i][3] = booking.getCustomerID();
            rowData[i][4] = carPrice; // Get price from car instead of booking
            rowData[i][5] = booking.getBookingDate();
            rowData[i][6] = booking.getStatus();
        }
        
        // Create the table model (non-editable)
        DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Double.class; // Proper rendering for price
                return String.class;
            }
        };
        
        JTable salesTable = new JTable(model);
        salesTable.setAutoCreateRowSorter(true);
        salesTable.setFillsViewportHeight(true);
       
        
        JScrollPane scrollPane = new JScrollPane(salesTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Calculate totals using CAR prices
        double totalSales = myBookings.stream()
            .filter(b -> "paid".equalsIgnoreCase(b.getStatus()))
            .mapToDouble(b -> {
                Car car = findCarById(b.getCarID());
                return (car != null) ? car.getPrice() : 0.0;
            })
            .sum();
        
        int totalTransactions = myBookings.size();
        int paidTransactions = (int) myBookings.stream()
                .filter(b -> "paid".equalsIgnoreCase(b.getStatus()))
                .count();
        
        // Create info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(new JLabel("Total Sales Value: RM " + String.format("%.2f", totalSales)));
        infoPanel.add(new JLabel("Total Transactions: " + totalTransactions));
        infoPanel.add(new JLabel("Paid Transactions: " + paidTransactions));
        
        // Main panel combining table and info
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        // Show dialog
        JOptionPane.showMessageDialog(null, mainPanel, 
                "Sales Report for " + salesman.getName(), 
                JOptionPane.PLAIN_MESSAGE);
    }
    
    private Car findCarById(String carId) {
        for (Car car : cars) {
            if (car.getCarID().equals(carId)) {
                return car;
            }
        }
        return null;
    }
}