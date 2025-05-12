package oop.assignment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class SalesmanDashboard extends JFrame {
    private Salesman salesman;
    private List<Salesman> salesmenlist;
    private List<Car> cars;
    private List<Booking> bookings;

    // Constructor (your original code + View Sales button)
    public SalesmanDashboard(Salesman s, List<Salesman> salesmenlist, List<Car> cars) {
        this.salesman = s;
        this.salesmenlist = salesmenlist;
        this.cars = cars;
        this.bookings = Booking.readBookings("bookings.txt");

        // Window settings (unchanged from your original)
        setTitle("Salesman Dashboard - " + s.getName());
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1)); // Changed to 5 rows (original had 4)

        // GUI Buttons (your original buttons + View Sales)
        JButton profileButton = new JButton("Profile");
        JButton viewCars = new JButton("View Car Status");
        JButton updateCar = new JButton("Update Car Status");
        JButton collectPayment = new JButton("Collect Car Payment");
        JButton viewSales = new JButton("View My Sales"); 
        JButton exit = new JButton("Exit");

        // 0. profile 
        profileButton.addActionListener(e -> new Profile(salesman, salesmenlist));

        
        // 1. Original View Car Status (unchanged)
        viewCars.addActionListener(e -> {
            String[] columnNames = {"Car ID", "Type", "Brand", "Model", "Year", "Price (RM)", "Mileage", "Status"};
            String[][] rowData = new String[cars.size()][8];
            for (int i = 0; i < cars.size(); i++) {
                Car car = cars.get(i);
                rowData[i][0] = car.getCarID();
                rowData[i][1] = car.getType();
                rowData[i][2] = car.getBrand();
                rowData[i][3] = car.getModel();
                rowData[i][4] = String.valueOf(car.getYear());
                rowData[i][5] = String.format("%.2f", car.getPrice());
                rowData[i][6] = String.valueOf(car.getMileage());
                rowData[i][7] = car.getStatus();
            }
            JTable carTable = new JTable(rowData, columnNames);
            carTable.setEnabled(false);
            JScrollPane scrollPane = new JScrollPane(carTable);
            scrollPane.setPreferredSize(new Dimension(600, 300));
            JOptionPane.showMessageDialog(this, scrollPane, "All Cars", JOptionPane.INFORMATION_MESSAGE);
        });

        // 2. Original Update Car Status (unchanged)
        updateCar.addActionListener(e -> updateCarStatus());

        // 3. Original Collect Payment (unchanged)
        collectPayment.addActionListener(e -> new collectPayment(salesman, bookings, cars));

        // 4. NEW: View Sales Button
        viewSales.addActionListener(e -> {
            new viewSales(salesman, bookings, cars);
        });


        // 5. Original Exit Button (unchanged)
        exit.addActionListener(e -> {
            Car.saveCarsToFile(cars, "cars.txt");
            Salesman.saveSalesmen("salesmen.txt", salesmenlist);
            System.exit(0);
        });

        // Add all buttons (original + View Sales)
        add(profileButton);
        add(viewCars);
        add(updateCar);
        add(collectPayment);
        add(viewSales); 
        add(exit);

        setVisible(true);
    }

    // Original updateCarStatus method (unchanged)
    private void updateCarStatus() {
        String[] columnNames = {"Car ID", "Type", "Brand", "Model", "Year", "Price", "Mileage", "Status"};
        String[] statuses = {"available", "booked", "paid", "cancel"};

        String[][] rowData = new String[cars.size()][8];
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            rowData[i][0] = car.getCarID();
            rowData[i][1] = car.getType();
            rowData[i][2] = car.getBrand();
            rowData[i][3] = car.getModel();
            rowData[i][4] = String.valueOf(car.getYear());
            rowData[i][5] = String.format("%.2f", car.getPrice());
            rowData[i][6] = String.valueOf(car.getMileage());
            rowData[i][7] = car.getStatus();
        }

        DefaultTableModel model = new DefaultTableModel(rowData, columnNames) {
            @Override public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        JComboBox<String> statusBox = new JComboBox<>(statuses);
        table.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(statusBox));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 300));

        if (JOptionPane.showConfirmDialog(this, scrollPane, "Update Car Status", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            for (int i = 0; i < table.getRowCount(); i++) {
                cars.get(i).setStatus(table.getValueAt(i, 7).toString());
            }
            Car.saveCarsToFile(cars, "cars.txt");
            JOptionPane.showMessageDialog(this, "Status updated!");
        }
    }

    // Helper method for View Sales
    private List<Booking> getBookingsForSalesman() {
        List<Booking> filtered = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getSalesmanID().equals(salesman.getID())) {
                filtered.add(b);
            }
        }
        return filtered;
    }
}